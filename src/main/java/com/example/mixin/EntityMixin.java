package com.example.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityZombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isBurning", at = @At("RETURN"), cancellable = true)
    void onFire(CallbackInfoReturnable<Boolean> cir) {
        // they just light them on fire for some reason??
        if ((Object) this instanceof EntityGiantZombie) {
            cir.setReturnValue(false);
        }
    }
}
