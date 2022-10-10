package com.example.mods.overlays;

import com.example.PrisonsModConfig;
import com.example.mixin.accessor.GuiChestAccessor;
import com.example.mixin.accessor.GuiContainerAccessor;
import com.example.mods.events.SlotClickEvent;
import com.example.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GeneralOverlay {
    @SubscribeEvent
    public void onClick(SlotClickEvent event) {
    }

    @SubscribeEvent
    public void onRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest) {
            render((GuiChest) event.gui, event.getMouseX(), event.getMouseY());
        }
    }

    private static Item stained_glass_pane = Item.getItemFromBlock(Blocks.stained_glass_pane);

    public static void render(GuiChest chest, int mouseX, int mouseY) {
        if (!PrisonsModConfig.INSTANCE.gui.generalOverlayEnabled) return;
        IInventory chestInv = ((GuiChestAccessor) chest).lowerChestInventory();
        String unformattedName = chestInv.getDisplayName().getUnformattedText();

        // TODO: Infamy
        switch (unformattedName) {
            // lootbox npc, but not real lootboxes
            case "Cosmic Lootbox": {
                if (!PrisonsModConfig.INSTANCE.gui.generalOverlayShowLootboxNPC) break;
                boolean shouldRender = chestInv.getSizeInventory() == 9;
                for (int i = 0; i < 9; i++) {
                    if (i == 4) continue;
                    ItemStack stack = chestInv.getStackInSlot(i);
                    shouldRender &= stack != null && stack.getItem() == stained_glass_pane && stack.getItemDamage() == 15;
                }

                if (!shouldRender) break;
            }
            case "§lTinkerer": {
                if (!PrisonsModConfig.INSTANCE.gui.generalOverlayShowTinkerResult) break;
            }
            case "§lBlack Market Auction": {
                if (!PrisonsModConfig.INSTANCE.gui.generalOverlayShowBAHResult) break;
                render(chest, chestInv.getStackInSlot(4));
                break;
            }
            case "§b§lCosmo§f§l-§d§lSlot Bot": {
                // todo: fix when cut off in pres on large gui scale
                // §c§lPresidential §b§lCosmo
                if (!PrisonsModConfig.INSTANCE.gui.generalOverlayShowSlotBotResult) break;
                render(chest, chestInv.getStackInSlot(18));
                break;
            }
            case "§c§lPresidential §8§lQuest Shop": {
                if (!PrisonsModConfig.INSTANCE.gui.generalOverlayPresQuestShopResult) break;
                render(chest, chestInv.getStackInSlot(22));
                break;
            }
        }

        if (unformattedName.startsWith("§lQuests ")) {
            if (!PrisonsModConfig.INSTANCE.gui.generalOverlayQuestsResult) return;
            render(chest, chestInv.getStackInSlot(39));
            return;
        }
    }

    public static void render(GuiChest chest, ItemStack stack) {
        if (stack == null) stack = Minecraft.getMinecraft().thePlayer.inventory.getItemStack();
        if (stack == null) return;
        int xSize = ((GuiContainerAccessor)chest).xSize();
        int ySize = ((GuiContainerAccessor)chest).ySize();
        int x0 = (chest.width - xSize) / 2 ;
        int y0 = (chest.height - ySize) / 2;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        int width = scaledResolution.getScaledWidth();
        int height = scaledResolution.getScaledHeight();
        RenderUtil.drawHoveringText(stack.getTooltip(Minecraft.getMinecraft().thePlayer, false),
                x0+xSize-8, y0 + 16, width, height, 300, Minecraft.getMinecraft().fontRendererObj);
    }
}
