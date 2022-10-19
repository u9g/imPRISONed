package dev.u9g.imprisoned.mods;

import dev.u9g.imprisoned.mods.block_nbt_manager.TileEntityNBTManager;
import dev.u9g.imprisoned.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHighlighter {
    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        for (TileEntity te : Minecraft.getMinecraft().theWorld.loadedTileEntityList) {
            NBTTagCompound nbt = TileEntityNBTManager.data.get(te);
            if (nbt == null) continue;
            NBTTagCompound customBlockData = nbt.getCompoundTag("tag").getCompoundTag("customBlockData");
            if (customBlockData.getString("type").equals("HALLOWEEN_CAULDRON")) {
                BlockPos pos = te.getPos();
                int color = customBlockData.getByte("on") == 1 ? 0xff33ff57 : 0xffFF0000;
                RenderUtil.renderBoundingBox(pos.getX(), pos.getY(), pos.getZ(), color, 0.4f, event.partialTicks,
                        false, true);
            }
        }
    }
}
