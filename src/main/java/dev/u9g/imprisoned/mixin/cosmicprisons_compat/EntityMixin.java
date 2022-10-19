package dev.u9g.imprisoned.mixin.cosmicprisons_compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
