package dev.u9g.imprisoned.mixin.events;

import dev.u9g.imprisoned.Imprisoned;
import dev.u9g.imprisoned.mods.events.PlayerScaleEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class RenderPlayerMixin {
    @Inject(method = "preRenderCallback(Lnet/minecraft/entity/EntityLivingBase;F)V", at = @At("HEAD"))
    public void imprisoned$scalePlayers(EntityLivingBase entitylivingbaseIn, float partialTickTime, CallbackInfo ci) {
        PlayerScaleEvent event = new PlayerScaleEvent(entitylivingbaseIn);
        Imprisoned.modules.forEach(c -> c.onPlayerScaleEvent(event));
        if (event.isScaleSet()) {
            GlStateManager.scale(event.getScale(), event.getScale(), event.getScale());
        }
    }
}
