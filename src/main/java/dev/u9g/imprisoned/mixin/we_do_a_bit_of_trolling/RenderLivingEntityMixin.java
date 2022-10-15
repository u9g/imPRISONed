package dev.u9g.imprisoned.mixin.we_do_a_bit_of_trolling;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public class RenderLivingEntityMixin {
    @Inject(method = "rotateCorpse", at = @At("TAIL"))
    void onRotateCorpse(EntityLivingBase ent, float p_77043_2_, float p_77043_3_, float partialTicks, CallbackInfo ci) {
        if (ent.deathTime > 0) return;
        if (ent instanceof EntityPlayer && ent.getName().equals("U9G") && ((EntityPlayer)ent).isWearing(EnumPlayerModelParts.CAPE)) {
            GlStateManager.translate(0.0f, ent.height + 0.1f, 0.0f);
            GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        }
    }
}
