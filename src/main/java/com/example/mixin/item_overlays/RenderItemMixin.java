package com.example.mixin.item_overlays;

import com.example.mods.item_overlays.ItemProgressManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {
    @Shadow protected abstract void draw(WorldRenderer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha);
    @ModifyArg(method = "renderItemAndEffectIntoGUI(Lnet/minecraft/item/ItemStack;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItemIntoGUI(Lnet/minecraft/item/ItemStack;II)V"), index = 0)
    public ItemStack renderItemOverlay(ItemStack stack) {
        return ItemProgressManager.replaceRenderedItemstack(stack);
    }

    @Inject(method = "renderItemOverlayIntoGUI(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderItemOverlay(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        if (stack == null) return;
        int playerCount = ItemProgressManager.renderCountOnItem(stack);
        if (playerCount >= 0) {
            String s = (playerCount == 0 ? EnumChatFormatting.RED : "") + String.valueOf(playerCount);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            fr.drawStringWithShadow(s, xPosition + 19 - 2 - fr.getStringWidth(s), yPosition + 6 + 3, 0xFFFFFF);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        } else {
            String s = ItemProgressManager.renderStringOnItem(stack);
            if (s != null) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                GlStateManager.disableBlend();
                fr.drawStringWithShadow(s, xPosition + 19 - 2 - fr.getStringWidth(s), yPosition + 6 + 3, 0xFFFFFF);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

        double progress = ItemProgressManager.progressToRender(stack);
        if (progress == -1) return;
        int j = (int)Math.round(13.0 - progress * 13.0);
        int i = (int)Math.round(255.0 - progress * 255.0);
        int colorI; // color used when making progress bar color
        {
            // let's just leave it at the green color when the bar is almost fully repaired
            double colorProgress = 0.03;
            colorI = (int)Math.round(255.0 - colorProgress * 255.0);
        }
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.draw(worldrenderer, xPosition + 2, yPosition + 13, 13, 2, 0, 0, 0, 255);
        this.draw(worldrenderer, xPosition + 2, yPosition + 13, 12, 1, (255 - colorI) / 4, 64, 0, 255);
        this.draw(worldrenderer, xPosition + 2, yPosition + 13, j, 1, 255 - i, colorI, 0, 255);
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }

    @Redirect(method = "renderQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getColorFromItemStack(Lnet/minecraft/item/ItemStack;I)I"))
    public int onTint(Item instance, ItemStack stack, int renderPass) {
        int color = ItemProgressManager.setTintColor(stack);
        return color != -1 ? color : instance.getColorFromItemStack(stack, renderPass);
    }
}
