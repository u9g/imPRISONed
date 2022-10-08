package com.example.mixin.perf;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Mixin(World.class)
public class WorldMixin {
//    @Mutable @Shadow @Final public List<TileEntity> loadedTileEntityList = new CopyOnWriteArrayList<>();
}
