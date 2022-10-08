package com.example.mixin.show_progress_on_item;

import com.example.mods.show_progress_on_item.ItemProgressManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {
    @Shadow protected abstract void draw(WorldRenderer renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha);

    @Inject(method = "renderItemOverlayIntoGUI(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void renderItemOverlay(FontRenderer fr, ItemStack stack, int xPosition, int yPosition, String text, CallbackInfo ci) {
        if (stack == null) return;
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
}
