package dev.u9g.imprisoned.mixin.dont_render_particles;

import dev.u9g.imprisoned.PrisonsModConfig;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class EffectRendererMixin {
    @Inject(method = "renderParticles(Lnet/minecraft/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    public void renderParticles(Entity entityIn, float partialTicks, CallbackInfo ci) {
        if (!PrisonsModConfig.INSTANCE.perf.renderAllParticles) ci.cancel();
    }
}
