package dev.u9g.imprisoned.mixin.lore_cleanup;

import dev.u9g.imprisoned.mods.lore_cleanup.LoreCleanupManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "renderToolTip(Lnet/minecraft/item/ItemStack;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawHoveringText(Ljava/util/List;IILnet/minecraft/client/gui/FontRenderer;)V", shift = At.Shift.BEFORE, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    public void drawHoveringText(ItemStack stack, int x, int y, CallbackInfo ci, List<String> loreLines, FontRenderer font) {
        LoreCleanupManager.cleanupLore(stack, loreLines);
    }
}
