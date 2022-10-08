package com.example.mixin.perf;

import com.example.PrisonsModConfig;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDiggingFX.class)
public class EntityDiggingFXMixin {
    @Inject(method = "renderParticle", at = @At("HEAD"), cancellable = true)
    public void renderItemOverlay(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ, CallbackInfo ci) {
        if (!PrisonsModConfig.INSTANCE.perf.renderDiggingParticles) {
            ci.cancel();
        }
    }
}
