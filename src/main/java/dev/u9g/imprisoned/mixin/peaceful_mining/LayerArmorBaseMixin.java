package dev.u9g.imprisoned.mixin.peaceful_mining;

import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.imprisoned.mods.peaceful_mining.PeacefulMining;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public class LayerArmorBaseMixin {
    @Shadow
    private float alpha;

    @Unique
    private boolean modifiedAlpha = false;

    // disables armor rendering
//    @Inject(method = "doRenderLayer", at = @At("HEAD"), cancellable = true)
//    private void onRenderAllArmor(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
//        if (PrisonsModConfig.INSTANCE.misc.peacefulSkilling && Config.INSTANCE.getTransparentArmorLayer() == 0 && Utils.INSTANCE.getInSkyblock() && entitylivingbaseIn != Minecraft.getMinecraft().thePlayer)
//            ci.cancel();
//    }

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemArmor;getColor(Lnet/minecraft/item/ItemStack;)I"))
    private void imprisoned$makeArmorRenderingTransparent$pre(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot, CallbackInfo ci) {
        float t = PeacefulMining.transparency(entitylivingbaseIn);
        if (t == 1f || !PeacefulMining.shouldChangeTransparency(entitylivingbaseIn)) return;
        if (PrisonsModConfig.INSTANCE.misc.peacefulSkilling && entitylivingbaseIn != Minecraft.getMinecraft().thePlayer) {
            modifiedAlpha = true;
            this.alpha = t;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }
    }

    @Dynamic
    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.AFTER, ordinal = 1))
    private void imprisoned$makeArmorRenderingTransparent$post(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot, CallbackInfo ci) {
        if (modifiedAlpha) {
            this.alpha = 1f;
            modifiedAlpha = false;
        }
    }
}
