package com.example.mods;

import com.example.PrisonsModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ShowItemLore {
    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent event) {
        if (PrisonsModConfig.INSTANCE.misc.showDroppedItemLore && event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        EntityItem item = getEntityItemPlayerIsLookingAt(Minecraft.getMinecraft().getRenderViewEntity(), 20);
        if (item != null) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            List<String> lines = item.getEntityItem().getTooltip(Minecraft.getMinecraft().thePlayer, false);
            GuiUtils.drawHoveringText(lines,
                    sr.getScaledWidth()/2 + 50, sr.getScaledHeight()/2 - ((7*lines.size()) + 10)/2,
                    sr.getScaledWidth(), sr.getScaledHeight(),
                    300,
                    fr);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            RenderHelper  .disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
        }
    }

    public static EntityItem getEntityItemPlayerIsLookingAt(Entity p, int distance){
        int dis = distance;
        List<EntityItem> Items = p.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(p.posX - dis, p.posY - dis, p.posZ - dis, p.posX + dis, p.posY + dis, p.posZ + dis));
        double calcdist = dis;
        Vec3 pos = p.getPositionEyes(0);
        Vec3 lookvec = p.getLookVec();
        Vec3 var8 = pos.addVector(lookvec.xCoord * dis, lookvec.yCoord * dis, lookvec.zCoord * dis);
        Vec3 var = pos.addVector(lookvec.xCoord * 2, lookvec.yCoord * 2, lookvec.zCoord * 2);
        EntityItem pointedEntity = null;
        for (EntityItem entity : Items){
            float bordersize = entity.getCollisionBorderSize();
            AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - entity.width / 2, entity.posY,
                    entity.posZ - entity.width / 2, entity.posX + entity.width / 2,
                    entity.posY + entity.height, entity.posZ + entity.width / 2);
            aabb.expand(bordersize, bordersize, bordersize);
            double d = calcdist;
            MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);
            if (aabb.isVecInside(pos)) {
                if (0.0D < d || d == 0.0D) {
                    pointedEntity = entity;
                    d = 0.0D;
                }
            } else if (mop0 != null) {
                double d1 = pos.distanceTo(mop0.hitVec);

                if (d1 < d || d == 0.0D) {
                    pointedEntity = entity;
                    d = d1;
                }
            }
        }
        return pointedEntity;
    }
}
