package com.example.mods.lore_cleanup;

import com.example.PrisonsModConfig;
import com.example.mixin.accessor.GuiChestAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class LoreCleanupManager {
    public static void cleanupLore(ItemStack stack, List<String> lore) {
        if (true) {
            int obtainableFromIx = lore.indexOf("§7§5§o§e§l§nObtainable From:");
            if (obtainableFromIx != -1) {
                // -1 for the space line before obtainable from
                lore.subList(obtainableFromIx - 1, lore.size()).clear();
            } else {
                int mutatedObtainableFromIx = lore.indexOf("§7§5§o§2§l§nObtainable From:");
                if (mutatedObtainableFromIx != -1) {
                    // -1 for the space line before obtainable from
                    lore.subList(mutatedObtainableFromIx - 1, lore.size()).clear();
                }
            }
        }

        if (true) {
            int requiresCC = lore.indexOf("§7§5§o§c§lNOTE:§r§c Requires the Cosmic Client");
            if (requiresCC != -1) {
                // -1 for the space line before obtainable from
                lore.subList(requiresCC - 1, requiresCC + 3).clear();
            }
        }

        if (PrisonsModConfig.INSTANCE.debug.lore) {
            lore.add("§o");
            if (stack.hasTagCompound()) {
                NBTTagCompound compound = stack.getTagCompound();
                if (compound.hasKey("_x", Constants.NBT.TAG_STRING)) {
                    lore.add("§e_x: §r§f" + compound.getString("_x"));
                }
                String type = compound.getCompoundTag("cosmicData").getCompoundTag("customBlockData").getString("type");
                if (!type.equals("")) {
                    lore.add("§eCustom Block Type: §r§f" + type);
                }
            }

            if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
                IInventory inv = ((GuiChestAccessor)Minecraft.getMinecraft().currentScreen).lowerChestInventory();

                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (stack.equals(inv.getStackInSlot(i))) {
                        lore.add("§eSlot: §r§f" + i);
                    }
                }
            }
        }
    }
}
