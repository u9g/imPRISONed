package dev.u9g.imprisoned.mods;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHighlighter {
    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
//        for (TileEntity te : Minecraft.getMinecraft().theWorld.loadedTileEntityList) {
//            NBTTagCompound nbt = TileEntityNBTManager.data.get(te);
//            if (nbt == null) continue;
//            NBTTagCompound customBlockData = nbt.getCompoundTag("tag").getCompoundTag("customBlockData");
//            if (customBlockData.getString("type").equals("HALLOWEEN_CAULDRON")) {
//                BlockPos pos = te.getPos();
//                int color = customBlockData.getByte("on") == 1 ? 0xFF33ff57 : 0xFFFF0000;
//                RenderUtil.renderBoundingBox(pos.getX(), pos.getY(), pos.getZ(), color, 1f, event.partialTicks,
//                        false, true);
//            }
//        }
    }
}
