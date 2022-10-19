package dev.u9g.imprisoned.mods.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Allows setting the player's color
 */
public class PlayerOutlineColorEvent extends Event {
    private boolean colorSet = false;
    private int color = 0;
    private final EntityLivingBase entity;

    public PlayerOutlineColorEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public boolean isColorSet() {
        return colorSet;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.colorSet = true;
        this.color = color;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}
