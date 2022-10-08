package com.example.mixin.we_do_a_bit_of_trolling;

import com.example.mixin.accessor.GuiChestAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItemIntoGUI(Lnet/minecraft/item/ItemStack;II)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", shift = At.Shift.BEFORE))
    private void draw(ItemStack stack, int x, int y, CallbackInfo ci) {
        // Flip all items in the brag inventory...
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest &&
                ((GuiChestAccessor) Minecraft.getMinecraft().currentScreen).lowerChestInventory().getDisplayName()
                        .getUnformattedText().equals("§lInventory               U9G") &&
                // can't use .contains since it .contains uses .equals, the items may be equal but they will not have ref. equality
                !Minecraft.getMinecraft().thePlayer.inventoryContainer.getInventory().contains(stack)
        ) {
            GL11.glScaled(-1, -1, 1);
        }
    }
}
