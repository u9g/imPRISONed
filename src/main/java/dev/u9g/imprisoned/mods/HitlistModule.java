package dev.u9g.imprisoned.mods;

import dev.u9g.configlib.util.Utils;
import dev.u9g.imprisoned.Module;
import dev.u9g.imprisoned.mixin.accessor.GuiChestAccessor;
import dev.u9g.imprisoned.mods.events.PlayerOutlineColorEvent;
import dev.u9g.imprisoned.mods.events.PlayerScaleEvent;
import dev.u9g.imprisoned.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HitlistModule implements Module {
    private static final Pattern ESCAPEE_LINE_ABOVE_HEAD = Pattern.compile("§.§l\\* Escapee \\*§r");
    private static final Pattern ESCAPEE_USERNAME_ABOVE_HEAD = Pattern.compile("§.([a-zA-Z0-9_\\s]+) ?§r");
    private static final Pattern ESCAPEE_USERNAME_IN_GUI = Pattern.compile("§.§lEscapee §f(.+)");
    private static final Pattern HITLIST_KILL_MSG = Pattern.compile("\\(!\\) /hitlist - You have slain (.+)!");
    private static final int[] HITLIST_GUI_SLOTS_WITH_PLAYERS = { 11, 13, 15, 19, 21, 23, 25, 29, 31, 33 };
    private static final Set<String> hitlistUsernames = new HashSet<>();

    @SubscribeEvent
    public void onPlayerOutlineColor(RenderWorldLastEvent event) {
//        for (EntityLivingBase player : Minecraft.getMinecraft().theWorld.playerEntities) {
//            if (player == Minecraft.getMinecraft().thePlayer) continue;
//            Entity viewer = Minecraft.getMinecraft().getRenderViewEntity();
//            double viewerX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * event.partialTicks;
//            double viewerY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * event.partialTicks;
//            double viewerZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * event.partialTicks;
//            double x = player.posX - viewerX;
//            double y = player.posY - viewerY;
//            double z = player.posZ - viewerZ;
//            //change both bbs to see the change and see it be applied
//            AxisAlignedBB bb = new AxisAlignedBB(
//                    x - .2, y + 1, z - .2,
//                    x + .2, y + 3, z + .2);
//            AxisAlignedBB bb1 = new AxisAlignedBB(
//                    player.posX - .2, player.posY + 1, player.posZ - .2,
//                    player.posX + .2, player.posY + 3, player.posZ + .2);
//            List<Entity> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, bb1);
//            List<String> names = entities.stream().map(c -> c.getDisplayName().getFormattedText()).collect(Collectors.toList());
//
//            if (names.stream().anyMatch(c -> ESCAPEE_LINE_ABOVE_HEAD.matcher(c).matches())) {
//                GlStateManager.disableCull();
//                GlStateManager.disableTexture2D();
//                RenderUtil.drawFilledBoundingBox(bb, 0.5f, 0xFF0000);
//                GlStateManager.enableTexture2D();
//                GlStateManager.enableCull();
//                RenderUtil.renderBeaconBeam(player.posX-0.5, player.posY + 3, player.posZ-0.5, 0xFF00FF, 1f, event.partialTicks, true);
//            }
//        }
    }

    @SubscribeEvent
    public void onChestOpen(TickEvent.ClientTickEvent event) {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (event.phase == TickEvent.Phase.END && screen instanceof GuiChest) {
            IInventory inventory = ((GuiChestAccessor)screen).lowerChestInventory();
            ItemStack stack = inventory.getStackInSlot(44);
            if (stack == null) return;
            Item item = stack.getItem();
            if (inventory.getDisplayName().getUnformattedText().equals("§l§r§lHit List") && item instanceof ItemBlock && ((ItemBlock)item).getBlock() == Blocks.stained_glass_pane) {
                hitlistUsernames.clear();
                for (int slotNum : HITLIST_GUI_SLOTS_WITH_PLAYERS) {
                    ItemStack slot = inventory.getStackInSlot(slotNum);
                    if (slot == null) slot = Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem();
                    if (slot == null) continue; // ???
                    //if (!slot.hasTagCompound()) continue; // this shouldnt be possible
                    Matcher matcher = ESCAPEE_USERNAME_IN_GUI.matcher(slot.getDisplayName());
                    if (matcher.matches()) {
                        hitlistUsernames.add(matcher.group(1));
                    } // else: Daily quota hit
                }
            }
        }
    }

    @SubscribeEvent
    public void onMessage(ClientChatReceivedEvent event) {
        Matcher m = HITLIST_KILL_MSG.matcher(Utils.removeColor(event.message.getUnformattedText()));
        /*
        [23:00:47] [main/INFO] (Minecraft) [CHAT] (!) /hitlist - You have slain XsSlayeR3!
        [23:00:47] [main/INFO] (Minecraft) [CHAT] You completed your daily hit list!
        [23:00:47] [main/INFO] (Minecraft) [CHAT] + Warden Gift Basket
        */
        if (m.matches()) {
            hitlistUsernames.remove(m.group(1));
        }
    }

    @Override
    public void onPlayerOutlineColor(PlayerOutlineColorEvent event) {
        EntityLivingBase player = event.getEntity();
        if (!event.getEntity().getDisplayName().getUnformattedText().equals("§f§l ")) return; // only escapees have this line
        AxisAlignedBB bb = new AxisAlignedBB(
                player.posX - .5, player.posY, player.posZ - .5,
                player.posX + .5, player.posY + 3, player.posZ + .5);
        List<Entity> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, bb);
        List<String> names = entities.stream().map(c -> c.getDisplayName().getFormattedText()).collect(Collectors.toList());

        if (names.stream().anyMatch(c -> ESCAPEE_LINE_ABOVE_HEAD.matcher(c).matches())) {
            Optional<String> usernameOfGuy = names.stream().filter(c -> ESCAPEE_USERNAME_ABOVE_HEAD.matcher(c).matches()).findFirst();
            usernameOfGuy.ifPresent(username -> {
                Matcher m = ESCAPEE_USERNAME_ABOVE_HEAD.matcher(username);
                m.matches();
                if (hitlistUsernames.contains(m.group(1))) event.setColor(0x8000FF00);
                else event.setColor(0x80FF0000);
            });
        }
    }
    /*
    {
      "unformatted": "§4§lSuper Bandit",
      "skinURL": "http://textures.minecraft.net/texture/bdf3945beecc243a0fa0083ce94ba55f923bc3c8acfaaa062bc09cd8dd35d986",
      "uuid": "1290066f-3856-32cf-9f89-1ef8776de7c4",
      "type": "EntityOtherPlayerMP"
    }
    */

    @Override
    public void onPlayerScaleEvent(PlayerScaleEvent event) {
        EntityLivingBase player = event.getEntity();
        if ("§4§lSuper Bandit".equals(player.getDisplayName().getUnformattedText())) {
            event.setScale(1.5f);
            return;
        }
        if (!event.getEntity().getDisplayName().getUnformattedText().equals("§f§l ")) return; // only escapees have this line
        AxisAlignedBB bb = new AxisAlignedBB(
                player.posX - .5, player.posY, player.posZ - .5,
                player.posX + .5, player.posY + 3, player.posZ + .5);
        List<Entity> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, bb);
        List<String> names = entities.stream().map(c -> c.getDisplayName().getFormattedText()).collect(Collectors.toList());

        if (names.stream().anyMatch(c -> ESCAPEE_LINE_ABOVE_HEAD.matcher(c).matches())) {
            event.setScale(1.5f);
        }
    }
}
