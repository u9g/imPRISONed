package dev.u9g.imprisoned.mods.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Allows making the player larger by a multiplier
 */
public class PlayerScaleEvent extends Event {
    private boolean scaleSet = false;
    private float scale = 1;
    private final EntityLivingBase entity;

    public PlayerScaleEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public boolean isScaleSet() {
        return scaleSet;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scaleSet = true;
        this.scale = scale;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}
