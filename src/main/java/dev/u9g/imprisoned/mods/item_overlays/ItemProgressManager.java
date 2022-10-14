package dev.u9g.imprisoned.mods.item_overlays;

import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.imprisoned.mixin.accessor.GuiChestAccessor;
import com.google.common.collect.ImmutableMap;
import dev.u9g.configlib.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        } else if (PrisonsModConfig.INSTANCE.misc.midasSatchelBar && compound.getString("_x").equals("midassatchel") && compound.hasKey("__count")/*__count only exists when __count > 1*/ && compound.getShort("__count") != 15_000) {
            return (15_000 - compound.getShort("__count")) / (double) 15_000;
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

    private static Pattern ITEM_SKIN_NAME = Pattern.compile("§f§lItem Skin \\((.+)§f§l\\)");
    /**
     * @return null to render nothing
     */
    public static String renderStringOnItem(ItemStack stack) {
        if (!PrisonsModConfig.INSTANCE.gui.writeSkinLetters || stack == null || !stack.hasTagCompound() || !stack.getTagCompound().getString("_x").equals("skin")) return null;
        Matcher matcher = ITEM_SKIN_NAME.matcher(stack.getDisplayName());
        if (!matcher.matches()) return null;
        // TODO: FINISH
        String itemName = matcher.group(1);
        return Arrays.stream(itemName.split(itemName.contains(", ") ? ", " : " ")).limit(2).map(c -> {
            StringBuilder colorsAndFirstLetter = new StringBuilder();
            boolean lastWasSection = false;
            for (int i = 0; i < c.length(); i++) {
                boolean isSection = c.charAt(i) == '§';
                colorsAndFirstLetter.append(c.charAt(i));
                if (!isSection && !lastWasSection) {
                    break;
                }
                lastWasSection = isSection;
            }
            return colorsAndFirstLetter.toString();
        }).collect(Collectors.joining());
    }
    private static Pattern ATTACH_THIS_SKIN_TO = Pattern.compile("§5§o§7Attach this skin to any §f(.+)");
    private static Map<String, ItemStack> armorType2Stack = new ImmutableMap.Builder<String, ItemStack>()
            .put("Helmets", new ItemStack(Items.leather_helmet))
            .put("Chestplates", new ItemStack(Items.leather_chestplate))
            .put("Backpack_CUSTOM", new ItemStack(Items.chainmail_chestplate))
            .put("Leggings", new ItemStack(Items.leather_leggings))
            .put("Boots", new ItemStack(Items.leather_boots))
            .put("Axes", new ItemStack(Items.wooden_axe))
            .put("Swords", new ItemStack(Items.wooden_sword))
            .put("Pickaxes", new ItemStack(Items.wooden_pickaxe)).build();

    public static ItemStack replaceRenderedItemstack(ItemStack stack) {
        if (!PrisonsModConfig.INSTANCE.gui.changeSkinItem || stack == null || !stack.hasTagCompound() || !stack.getTagCompound().getString("_x").equals("skin")) return stack;
        Matcher matcher = ITEM_SKIN_NAME.matcher(stack.getDisplayName());
        if (!matcher.matches()) return stack;
        for (String line : stack.getTooltip(Minecraft.getMinecraft().thePlayer, false)) {
            Matcher attachSkinTo = ATTACH_THIS_SKIN_TO.matcher(line);
            if (!attachSkinTo.matches()) continue;
            String skinType = attachSkinTo.group(1);
            if (skinType.contains(", ")) { // Pickaxes§7, §fSwords
                skinType = skinType.substring(0, skinType.indexOf('§'));
            }
            if (skinType.equals("Chestplates")) {
                ItemStack stackToUse = armorType2Stack.get("Chestplates");
                if (stack.getTagCompound().getCompoundTag("cosmicData").getString("itemSkinType").contains("_BACKPACK")) {
                    stackToUse = armorType2Stack.get("Backpack_CUSTOM");
                }
                return stackToUse;
            }
            return armorType2Stack.get(skinType);
        }
        return stack;
    }

    /* return -1 to not tint */
    public static int setTintColor(ItemStack stack) {
        if (!stack.hasTagCompound()) return -1;
        NBTTagCompound tag = stack.getTagCompound();
        if (PrisonsModConfig.INSTANCE.misc.midasSatchelColor && tag.getString("_x").equals("midassatchel") && tag.getShort("__count") == 15_000) {
            return 0x00FF00;
        }
        return -1;
    }
}
