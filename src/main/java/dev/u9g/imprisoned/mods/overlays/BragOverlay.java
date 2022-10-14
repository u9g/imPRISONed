package dev.u9g.imprisoned.mods.overlays;

import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.imprisoned.mixin.accessor.GuiChestAccessor;
import dev.u9g.imprisoned.mods.events.SlotClickEvent;
import dev.u9g.imprisoned.utils.ApiUtil;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;

public class BragOverlay {
    private static Map<UUID, ResourceLocation> capes = new HashMap<>();
    private static Map<UUID, ResourceLocation> skins = new HashMap<>();
    private static Map<UUID, String> skinTypes = new HashMap<>();
    private static Map<String, UUID> nameToUuid = new HashMap<>();
    private static Map<UUID, EntityOtherPlayerMP> players = new HashMap<>(); // TODO: Make cache

    private static void getPlayerUUID(String name, Consumer<UUID> uuidCallback) {
        String nameF = name.toLowerCase();
        if (nameToUuid.containsKey(nameF)) {
            uuidCallback.accept(nameToUuid.get(nameF));
            return;
        }

        ApiUtil.INSTANCE
                .request()
                .url("https://api.mojang.com/users/profiles/minecraft/" + nameF)
                .requestJson()
                .thenAccept(jsonObject -> {
                    if (jsonObject.has("id") && jsonObject.get("id").isJsonPrimitive() &&
                            ((JsonPrimitive) jsonObject.get("id")).isString()) {
                        String uuid = jsonObject.get("id").getAsString();
                        UUID theUUID = parseDashlessUUID(uuid);
                        nameToUuid.put(nameF, theUUID);
                        uuidCallback.accept(theUUID);
                        return;
                    }
                    uuidCallback.accept(null);
                }).exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    private static UUID parseDashlessUUID(String dashlessUuid) {
        // From: https://stackoverflow.com/a/30760478/
        BigInteger most = new BigInteger(dashlessUuid.substring(0, 16), 16);
        BigInteger least = new BigInteger(dashlessUuid.substring(16, 32), 16);
        return new UUID(most.longValue(), least.longValue());
    }

    private static List<String> usernameGettingUUID = new ArrayList<>();

    private static Item stained_glass_pane = Item.getItemFromBlock(Blocks.stained_glass_pane);

    @SubscribeEvent
    public void onClick(SlotClickEvent event) {
        if (!PrisonsModConfig.INSTANCE.gui.bragOverlayEnabled || !PrisonsModConfig.INSTANCE.gui.rightClickItemsToEquip || !(event.guiContainer instanceof GuiChest)) return;
        IInventory chest = ((GuiChestAccessor) event.guiContainer).lowerChestInventory();
        String unformattedName = chest.getDisplayName().getUnformattedText();
        if (!unformattedName.startsWith("§lInventory ") || event.clickedButton != 1) return; // right click

        ItemStack stack = event.slot.getStack();
        if (stack == null || !(stack.getItem() instanceof ItemArmor)) return;

        event.setCanceled(true);
        String username = unformattedName.substring(unformattedName.lastIndexOf(' ')+1);
        UUID uuid = nameToUuid.get(username.toLowerCase());
        if (uuid == null) return;

        EntityOtherPlayerMP player = players.get(uuid);
        if (player == null) return;

        player.inventory.armorInventory[3-((ItemArmor) stack.getItem()).armorType] = stack;
    }

    @SubscribeEvent
    public void onRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest) {
            render((GuiChest) event.gui, event.getMouseX(), event.getMouseY());
        }
    }

    public static void render(GuiChest gui, int mouseX, int mouseY) {
        if (!PrisonsModConfig.INSTANCE.gui.bragOverlayEnabled) return;
        IInventory chest = ((GuiChestAccessor) gui).lowerChestInventory();
        String unformattedName = chest.getDisplayName().getUnformattedText();
        if (unformattedName.startsWith("§lInventory ")) {
            String username = unformattedName.substring(unformattedName.lastIndexOf(' ')+1);
            if (nameToUuid.containsKey(username.toLowerCase())) {
                usernameGettingUUID.remove(username);
                UUID uuid = nameToUuid.get(username.toLowerCase());
                boolean firstTime = false;
                if (!players.containsKey(uuid)) {
                    makePlayer(uuid);
                    firstTime = true;
                }
                EntityOtherPlayerMP player = players.get(uuid);
                if (firstTime) {
                    player.inventory.armorInventory[3] = chest.getStackInSlot(1);
                    player.inventory.armorInventory[2] = chest.getStackInSlot(3);
                    player.inventory.armorInventory[1] = chest.getStackInSlot(5);
                    player.inventory.armorInventory[0] = chest.getStackInSlot(7);
                }
//                    if (player.getName().equals("U9G")) {
//                        player.inventory.armorInventory[3] = new ItemStack(Blocks.oak_fence_gate);
//                    }
//                    GuiInventory.drawEntityOnScreen(950, 435, 100, -25, 25, player);
                /*for (int i = 0; i < 4; i++) {
                    player.inventory.armorInventory[i] = null;
                }*/
                if (gui.getSlotUnderMouse() != null) {
                    ItemStack stack = gui.getSlotUnderMouse().getStack();
                    if (stack != null && stack.getItem() != stained_glass_pane) {
                        player.inventory.mainInventory[player.inventory.currentItem] = gui.getSlotUnderMouse().getStack();
                    } else {
                        player.inventory.mainInventory[player.inventory.currentItem] = null;
                    }
                } else {
                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                }
                // move with mouse
                GuiInventory.drawEntityOnScreen(250, 435, 100, (565-mouseX)/*-650*/, 280-mouseY, player);
                // dont move with mouse
//                    GuiInventory.drawEntityOnScreen(250, 435, 100, -25, 0, player);
            } else if (!usernameGettingUUID.contains(username)) {
                usernameGettingUUID.add(username);
                getPlayerUUID(username, uuid -> {});
            }
        }
    }

    public static void makePlayer(UUID uuid) {
        GameProfile fakeProfile = Minecraft
                .getMinecraft()
                .getSessionService()
                .fillProfileProperties(new GameProfile(uuid, "CoolGuy123"), false);
        players.put(uuid, new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, fakeProfile) {
            public ResourceLocation getLocationSkin() {
                ResourceLocation skin = skins.get(uuid);
                return skin == null
                        ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID())
                        : skin;
            }

            public ResourceLocation getLocationCape() {
                return capes.get(uuid);
            }

            public String getSkinType() {
                String skinType = skinTypes.get(uuid);
                return skinType == null ? DefaultPlayerSkin.getSkinType(uuid) : skinType;
            }
        });
        Minecraft.getMinecraft().getSkinManager().loadProfileTextures(fakeProfile, (type, location, profileTexture) -> {
            switch (type) {
                case SKIN:
                    skins.put(uuid, location);
                    String skinType = profileTexture.getMetadata("model");

                    if (skinType == null) {
                        skinType = "default";
                    }

                    skinTypes.put(uuid, skinType);

                    break;
                case CAPE:
                    capes.put(uuid, location);
            }
        }, false);
    }
}
