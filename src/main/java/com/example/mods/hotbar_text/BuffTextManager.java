package com.example.mods.hotbar_text;

import com.example.PrisonsModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BuffTextManager {
    private final Buff cauldron = new Buff("Cauldron");
    private final Buff haste = new Buff("Haste");
    private int numberOfBuildingBlocks = 0;

    public BuffTextManager() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Minecraft.getMinecraft().addScheduledTask(this::scanInventoryForBuildingBlocks);
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void scanInventoryForBuildingBlocks() {
        if (Minecraft.getMinecraft().currentScreen != null) return;
        numberOfBuildingBlocks = 0;
        for (Slot slot : Minecraft.getMinecraft().thePlayer.inventoryContainer.inventorySlots) {
            if (!slot.getHasStack()) continue;
            ItemStack stack = slot.getStack();
            if (stack.hasTagCompound() && stack.getTagCompound().hasKey("_x") &&
                    stack.getTagCompound().getString("_x").equals("buildingblock")) {
                numberOfBuildingBlocks += stack.stackSize;
            }
        }
    }

    @SubscribeEvent
    public void onReceiveMessage(ClientChatReceivedEvent event) {
        String unformatted = event.message.getUnformattedText().trim();
        if (unformatted.equals("Sets your current highest unlocked tier of ore's /skill booster to 200% for 10 minutes.")) {
            cauldron.setActiveFor(Duration.ofMinutes(10));
        } else if (unformatted.equals("(!) Haste Pet: you now have a 50% Mining Speed Booster with 1 minute remaining.")) {
            haste.setActiveFor(Duration.ofMinutes(1));
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR) {
            if (PrisonsModConfig.INSTANCE.gui.cauldronText || PrisonsModConfig.INSTANCE.gui.hasteText) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(2, 2, 0);
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
                if (PrisonsModConfig.INSTANCE.gui.hasteText) {
                    int hasteColor = Minecraft.getSystemTime() <= haste.expiresAt ? 0xFF33ff57 : 0xFFFF0000;
                    fr.drawStringWithShadow("Haste", (sr.getScaledWidth() / 2 - 91 - fr.getStringWidth("Haste") * 2) / 2, (sr.getScaledHeight() - fr.FONT_HEIGHT * 2) / 2, hasteColor);
                }

                if (PrisonsModConfig.INSTANCE.gui.cauldronText) {
                    int cauldronColor = Minecraft.getSystemTime() <= cauldron.expiresAt ? 0xFF33ff57 : 0xFFFF0000;
                    fr.drawStringWithShadow("Cauldron", (sr.getScaledWidth() / 2 + 93) / 2, (sr.getScaledHeight() - fr.FONT_HEIGHT * 2) / 2, cauldronColor);
                }
                GlStateManager.popMatrix();
            }

            if (PrisonsModConfig.INSTANCE.gui.renderBuildingBlocks && numberOfBuildingBlocks > 0) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 0);
                GlStateManager.scale(4, 4, 1f);
                ItemStack stack = new ItemStack(Blocks.end_stone);
                stack.addEnchantment(Enchantment.efficiency, 1);
                drawItemStackWithText(stack, 0, 0, String.valueOf(numberOfBuildingBlocks));
                GlStateManager.popMatrix();
            }
        }
    }

    public static void drawItemStackWithText(ItemStack stack, int x, int y, String text) {
        if (stack == null) return;
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

        RenderHelper.enableGUIStandardItemLighting();
        itemRender.zLevel = -145; //Negates the z-offset of the below method.
        itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, stack, x, y, text);
        itemRender.zLevel = 0;
        RenderHelper.disableStandardItemLighting();
    }

    public static class Buff {
        public String name;
        public long expiresAt;

        public Buff(String name) {
            this.name = name;
        }

        public void setActiveFor(Duration lastsFor) {
            this.expiresAt = Minecraft.getSystemTime() + lastsFor.toMillis();
        }
    }
}
