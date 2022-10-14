package dev.u9g.imprisoned.mixin.fly_speed_boost;

import dev.u9g.imprisoned.PrisonsModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityPlayerMixin {
    @Inject(method = "isSprinting()Z", at = @At("TAIL"), cancellable = true)
    private void isSprinting(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this == Minecraft.getMinecraft().thePlayer) {
            // I get lag-backed without this
            cir.setReturnValue(cir.getReturnValue() && (!Minecraft.getMinecraft().thePlayer.capabilities.isFlying || PrisonsModConfig.INSTANCE.misc.flySpeedBoost == 1));
        }
    }
}
