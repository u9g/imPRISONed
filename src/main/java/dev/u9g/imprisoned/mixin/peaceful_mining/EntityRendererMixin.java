package dev.u9g.imprisoned.mixin.peaceful_mining;

import dev.u9g.imprisoned.PrisonsModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPickaxe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Shadow private Minecraft mc;

    @Inject(method = "getMouseOver", at = @At("HEAD"), cancellable = true)
    public void imprisoned$allowHitThroughEntity(float partialTicks, CallbackInfo ci) {
        if (!PrisonsModConfig.INSTANCE.misc.peacefulSkilling) return;

        Entity entity = mc.getRenderViewEntity();

        if (entity != null && mc.theWorld != null && (mc.thePlayer != null &&
                mc.thePlayer.getHeldItem() != null &&
                mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe)) {
            ci.cancel();
            this.mc.mcProfiler.startSection("pick");
            mc.objectMouseOver = entity.rayTrace(mc.playerController.getBlockReachDistance(), partialTicks);
            this.mc.mcProfiler.endSection();
        }
    }
}
