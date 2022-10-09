package com.example.mods.item_overlays;

import com.example.PrisonsModConfig;
import com.example.mixin.accessor.GuiChestAccessor;
import dev.u9g.configlib.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemProgressManager {
    /**
     * This callback will render a durability bar with the progress returned by this function on the itemstack
     * @param itemStack stack to render
     * @return 0 <= x <= 1 OR -1 to not render
     */
    public static double progressToRender(@NotNull ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return -1;
        NBTTagCompound compound = itemStack.getTagCompound();
        if (PrisonsModConfig.INSTANCE.misc.drawBarForPets && compound.getString("_x").equals("pet")) {
            NBTTagCompound data = compound.getCompoundTag("cosmicData");
            if (data.getKeySet().size() != 0) {
                if (!data.hasKey("cooldown", 99)) {
                    System.out.println("found a pet without a cooldown");
                    return -1;
                }

                int timeSinceLastUse = (int)(Minecraft.getSystemTime() - data.getLong("lastUsed"));
                long cooldown = data.getLong("cooldown");
                if (cooldown >= timeSinceLastUse) {
                    return timeSinceLastUse / (double)cooldown;
                }
            }
        }
        return -1;
    }
    private static Pattern playersLine = Pattern.compile("Players: (\\d+)");

    /**
     * return -1 to not do anything
     */
    public static int renderCountOnItem(ItemStack stack) {
        if (!PrisonsModConfig.INSTANCE.gui.warpOverlayEnabled) return -1;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (!(screen instanceof GuiChest) ||
            !"§lWarps".equals(((GuiChestAccessor)screen).lowerChestInventory().getDisplayName().getUnformattedText()) ||
            !stack.hasTagCompound()) return -1;
        for (String line : stack.getTooltip(Minecraft.getMinecraft().thePlayer, false)) {
            Matcher matcher = playersLine.matcher(Utils.removeColor(line));
            if (matcher.matches()) {
                int playerCount = Integer.parseInt(matcher.group(1));
                if (!PrisonsModConfig.INSTANCE.gui.showZerosInWarp && playerCount == 0) return -1;
                return playerCount;
            }
        }
        return -1;
    }
}
