package com.example.mixin.fly_speed_boost;

import com.example.PrisonsModConfig;
import net.minecraft.entity.player.PlayerCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerCapabilities.class)
public class PlayerCapabilitiesMixin {
    @Inject(method = "getFlySpeed()F", at = @At(value = "TAIL"), cancellable = true)
    private void addBlockDestroyEffects(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(cir.getReturnValueF() * PrisonsModConfig.INSTANCE.misc.flySpeedBoost);
    }
}
