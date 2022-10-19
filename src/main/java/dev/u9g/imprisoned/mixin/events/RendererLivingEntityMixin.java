package dev.u9g.imprisoned.mixin.events;

import dev.u9g.imprisoned.Imprisoned;
import dev.u9g.imprisoned.mods.events.PlayerOutlineColorEvent;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin {
    @Inject(method = "getColorMultiplier", at = @At("HEAD"), cancellable = true)
    public void imprisoned$callPlayerOutlineColorEvent(EntityLivingBase entitylivingbaseIn, float lightBrightness, float partialTickTime, CallbackInfoReturnable<Integer> cir) {
        PlayerOutlineColorEvent event = new PlayerOutlineColorEvent(entitylivingbaseIn);
        Imprisoned.modules.allModules.forEach(c -> c.onPlayerOutlineColor(event));
        if (event.isColorSet()) {
            cir.setReturnValue(event.getColor());
        }
    }
}
