package com.example.mods;

import com.example.mods.block_nbt_manager.TileEntityNBTManager;
import com.example.utils.RenderUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.stream.Collectors;

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
