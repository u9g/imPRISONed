package dev.u9g.imprisoned.mixin.peaceful_mining;

import dev.u9g.imprisoned.mods.peaceful_mining.PeacefulMining;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {
    @Inject(method = "renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", at = @At(value = "HEAD"))
    private void imprisoned$makeSkinTransparent$pre(EntityLivingBase entitylivingbase, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor, CallbackInfo ci) {
        PeacefulMining.makeSkinTransparentPre(entitylivingbase);
    }

    @Inject(method = "renderModel(Lnet/minecraft/entity/EntityLivingBase;FFFFFF)V", at = @At(value = "TAIL"))
    private void imprisoned$makeSkinTransparent$post(EntityLivingBase entitylivingbase, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor, CallbackInfo ci) {
        PeacefulMining.makeSkinTransparentPost(entitylivingbase);
    }
}
