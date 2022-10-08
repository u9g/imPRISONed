package com.example.mods;

import com.example.mixin.accessor.GuiChestAccessor;
import com.example.mixin.accessor.GuiContainerAccessor;
import dev.u9g.configlib.util.render.RenderUtils;
import dev.u9g.configlib.util.render.TextRenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.UUID;

public class TradeOverlay {
    @SubscribeEvent
    public void onDrawOverlay(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (event.gui instanceof GuiChest) {
            ItemStack stack = ((GuiChestAccessor)event.gui).lowerChestInventory().getStackInSlot(31);
            if (stack == null || !stack.getDisplayName().equals("§c§lDECLINE")) return;
            GuiChest chest = (GuiChest) event.gui;
            int xSize = ((GuiContainerAccessor)chest).xSize();
            int ySize = ((GuiContainerAccessor)chest).ySize();
            int i = (chest.width - xSize) / 2;
            int j = (chest.height - ySize) / 2;
            renderWindow(Side.LEFT, i, j, xSize, "§oYou");
            renderWindow(Side.RIGHT, i, j, xSize, "§oThem");
        }
    }

    private static void renderWindow(Side side, int x0, int y0, int x1, String header) {
        int windowWidth = 2 * x1 / 3;
        int left = side == Side.RIGHT ? x0 + x1 : x0 - windowWidth;
        RenderUtils.drawFloatingRectDark(left, y0, windowWidth, 126,
                false);
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        TextRenderUtils.drawStringCenteredScaledMaxWidth(header,
                fr, left + windowWidth/2, y0 + 7, false,
                120, 0xa368ef
        );

        long money = 0;
        int slots = 0;
        int heroicSlots = 0;
        int execTimeMins = 0;
        long energy = 0;
        for (ItemStack is : slots(side)) {
            if (is == null || !is.hasTagCompound()) continue;
            NBTTagCompound compound = is.getTagCompound();
            switch (compound.getString("_x")) {
                case "money": {
                    BigDecimal bd = new BigDecimal(compound.getString("money").replace(".+", ""));
                    money += bd.setScale(0, RoundingMode.HALF_UP).longValue();
                    break;
                }
                case "slotbotticket": {
                    if (is.getTagCompound().hasKey("__type")) {
                        heroicSlots += is.stackSize;
                    } else {
                        slots += is.stackSize;
                    }
                    break;
                }
                case "energy": {
                    energy += compound.getLong("joeEnergy-amount");
                    break;
                }
                case "exectimeextender": {
                    execTimeMins += compound.getInteger("exectimeextender")/60;
                    break;
                }
            }
        }

        int y = y0 + 7 + 7;
        if (money > 0) {
            fr.drawStringWithShadow("- Money: $" + getHumanReadablePriceFromNumber(money), left + fr.getCharWidth(' '), y, fr.getColorCode('7'));
            y += 10;
        }
        if (energy > 0) {
            long base = energy / Integer.MAX_VALUE;
            long remainder = energy % Integer.MAX_VALUE;
            // TODO: Does the stack part work?
            fr.drawStringWithShadow("§7- Energy: " + getHumanReadablePriceFromNumber(energy), left + fr.getCharWidth(' '), y, fr.getColorCode('7'));
            if (base > 0) {
                String line = "§8(" + base + " stack" + (base > 1 ? "s" : "");
                if (remainder > 0) {
                    line += " + " + getHumanReadablePriceFromNumber(remainder);
                }
                line += ")";
                fr.drawStringWithShadow(line, left + fr.getCharWidth(' ')*2 + fr.getCharWidth('-'), y, fr.getColorCode('f'));
            }
            y += 10;
        }
        if (slots > 0) {
            int stacks = slots / 64;
            int remainder = slots % 64;
            if (stacks > 0) {
                String line = "§8(" + stacks + " stack" + (stacks > 1 ? "s" : "");
                if (remainder > 0) {
                    line += " + " + remainder;
                }
                line += ")";
                fr.drawStringWithShadow(line, left + fr.getCharWidth(' ')*2 + fr.getCharWidth('-'), y, fr.getColorCode('f'));
            }
            fr.drawStringWithShadow("§7- Slots: " + slots + (slots > 64 ? " §8(" + (slots/64.0) + " stacks)" : ""), left + fr.getCharWidth(' '), y, fr.getColorCode('f'));
            y += 10;
        }
        if (heroicSlots > 0) {
            int stacks = heroicSlots / 64;
            int remainder = heroicSlots % 64;
            if (stacks > 0) {
                String line = "§8(" + stacks + " stack" + (stacks > 1 ? "s" : "");
                if (remainder > 0) {
                    line += " + " + remainder;
                }
                line += ")";
                fr.drawStringWithShadow(line, left + fr.getCharWidth(' ')*2 + fr.getCharWidth('-'), y, fr.getColorCode('f'));
            }
            fr.drawStringWithShadow("§7- Heroic Slots: " + heroicSlots + (heroicSlots > 64 ? " §8(" + (heroicSlots/64.0) + " stacks)" : ""), left + fr.getCharWidth(' '), y, fr.getColorCode('f'));
            y += 10;
        }
        if (execTimeMins > 0) {
            String line = "§7- Exec time: " + execTimeMins + "mins";
            fr.drawStringWithShadow(line, left + fr.getCharWidth(' '), y, fr.getColorCode('f'));
            y += 10;
            if (execTimeMins > 60) {
                String line2 = "(" + (execTimeMins / 60) + " hr" + (execTimeMins / 60 != 1 ? "s" : "");
                if (execTimeMins % 60 > 0) {
                    line2 += " + " + (execTimeMins % 60) + " min" + (execTimeMins % 60 != 1 ? "s" : "");
                }
                line2 += ")";
                fr.drawStringWithShadow(line2, left + fr.getCharWidth(' ')*2 + fr.getCharWidth('-'), y, fr.getColorCode('8'));
                y += 10;
            }
        }
    }

    public static String getHumanReadablePriceFromNumber(long number){

        if(number >= 1000000000){
            return String.format("%.2fB", number/ 1000000000.0);
        }

        if(number >= 1000000){
            return String.format("%.2fM", number/ 1000000.0);
        }

        if(number >= 100000){
            return String.format("%.2fL", number/ 100000.0);
        }

        if(number >=1000){
            return String.format("%.2fK", number/ 1000.0);
        }
        return String.valueOf(number);

    }

    private static final int[] slots = {
             5,  6,  7,  8,
            14, 15, 16, 17,
            23, 24, 25, 26,
            32, 33, 34, 35,
            41, 42, 43, 44,
            50, 51, 52, 53
    };

    private static Iterable<ItemStack> slots(Side side) {
        return () -> new Iterator<ItemStack>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < slots.length && Minecraft.getMinecraft().currentScreen instanceof GuiChest;
            }

            @Override
            public ItemStack next() {
                IInventory inv = ((GuiChestAccessor)Minecraft.getMinecraft().currentScreen).lowerChestInventory();
                int slotNum = slots[i++];
                if (side == Side.LEFT) {
                    slotNum -= 5;
                }
                return inv.getStackInSlot(slotNum);
            }
        };
    }

    private enum Side {
        LEFT, RIGHT
    }
}
