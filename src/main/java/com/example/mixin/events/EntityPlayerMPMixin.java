package com.example.mixin.events;

import com.example.mods.events.PlayerDeathEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public class EntityPlayerMPMixin {
    @Inject(method = "onDeath(Lnet/minecraft/util/DamageSource;)V", at = @At(value = "HEAD"), cancellable = true)
    public void onDeath(DamageSource cause, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PlayerDeathEvent((EntityLivingBase) (Object) this, cause))) {
            ci.cancel();
        }
    }
}
