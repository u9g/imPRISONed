package com.example.mixin.we_do_a_bit_of_trolling;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class LivingEntityMixin {
    @Inject(method = "isChild", at = @At("HEAD"), cancellable = true)
    private void setChildState(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((Object)this instanceof EntityPlayer && ((EntityPlayer)(Object)this).getName().equals("_Dotstoops_"));
    }
}
