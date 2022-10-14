package dev.u9g.imprisoned.mixin.perf;

import dev.u9g.imprisoned.PrisonsModConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public abstract class EffectRendererMixin {
    @Inject(method = "addBlockDestroyEffects(Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;)V", at = @At("HEAD"), cancellable = true)
    private void addBlockDestroyEffects(BlockPos pos, IBlockState state, CallbackInfo ci) {
        if (!PrisonsModConfig.INSTANCE.perf.renderDiggingParticles) ci.cancel();
    }
}
