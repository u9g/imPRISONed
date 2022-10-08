package com.example.mixin;

import com.example.mods.TradeOverlay;
import com.example.mods.BragOverlay;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class GuiChestMixin {
    @Shadow protected int xSize;

    @Shadow protected int ySize;

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void renderOverlay(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        BragOverlay.render((GuiContainer) (Object) this, mouseX, mouseY);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    public void renderOverlay_(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if ((Object)this instanceof GuiChest) {
            GuiChest chest = (GuiChest)(Object)this;

        }
    }
}
