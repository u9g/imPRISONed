package com.example.mixin.we_do_a_bit_of_trolling;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RendererLivingEntity.class)
public class RenderLivingEntityMixin {
    private boolean isCoolPerson;
    @Redirect(method = "rotateCorpse(Lnet/minecraft/entity/EntityLivingBase;FFF)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 0))
    public boolean equals(String s) {
        return (isCoolPerson = s.equals("U9G") || s.equals("Dinnerbone"));
    }

    @Redirect(method = "rotateCorpse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;isWearing(Lnet/minecraft/entity/player/EnumPlayerModelParts;)Z", ordinal = 0))
    boolean isWearing(EntityPlayer instance, EnumPlayerModelParts p_175148_1_) {
        if (isCoolPerson) {
            return !instance.isWearing(p_175148_1_);
        }
        return instance.isWearing(p_175148_1_);
    }
}
