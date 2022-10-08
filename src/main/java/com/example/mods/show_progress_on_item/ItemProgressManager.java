package com.example.mods.show_progress_on_item;

import com.example.PrisonsModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jetbrains.annotations.NotNull;

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
}
