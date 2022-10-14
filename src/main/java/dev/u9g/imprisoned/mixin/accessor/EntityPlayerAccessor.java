package dev.u9g.imprisoned.mixin.accessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityPlayer.class)
public interface EntityPlayerAccessor {
    @Accessor("spawnChunk")
    BlockPos spawnChunk();
}
