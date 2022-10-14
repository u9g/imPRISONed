package dev.u9g.imprisoned.mods.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerDeathEvent extends LivingDeathEvent {
    public PlayerDeathEvent(EntityLivingBase entity, DamageSource source) {
        super(entity, source);
    }
}
