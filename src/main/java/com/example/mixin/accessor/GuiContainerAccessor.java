package com.example.mixin.accessor;

import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiContainer.class)
public interface GuiContainerAccessor {
    @Accessor("xSize")
    int xSize();
    @Accessor("ySize")
    int ySize();
}
