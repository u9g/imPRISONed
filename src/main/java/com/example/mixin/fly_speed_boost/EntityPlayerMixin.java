package com.example.mixin.fly_speed_boost;

import com.example.PrisonsModConfig;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityPlayerMixin {
    @Inject(method = "isSprinting()Z", at = @At("TAIL"), cancellable = true)
    private void isSprinting(CallbackInfoReturnable<Boolean> cir) {
        // I get lag-backed without this
        cir.setReturnValue(cir.getReturnValue() && PrisonsModConfig.INSTANCE.misc.flySpeedBoost == 1);
    }
}
