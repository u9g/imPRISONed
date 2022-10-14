package dev.u9g.imprisoned.mixin.accessor;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiEditSign.class)
public interface GuiEditSignAccessor {
    @Accessor("tileSign")
    TileEntitySign tileSign();
}
