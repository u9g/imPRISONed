package com.example.mods;

import com.example.mods.events.PlayerDeathEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Midas {
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
    private static final UUID sideBoss = UUID.fromString("e2a6c9fb-7a5f-3789-9820-e8fd71ecc0bc");
    private static final UUID middleBoss = UUID.fromString("8c27394f-cde0-3b3f-95e4-ad6aded9d39c");
    public static String msg = "";
    public static long msgActiveUntil = Long.MAX_VALUE;

    @SubscribeEvent
    public void onEntityDeath(PlayerDeathEvent event) {
        // double fires
        if (event.entity instanceof EntityPlayer && event.entity.getUniqueID().equals(sideBoss)) {
            Corner corner = Corner.closestTo(event.entity);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("A side boss has just died in corner: " +
                    Corner.closestTo(event.entity).name() + " : " + event.source.damageType));

            msg = "§b§l" + corner.cornerName + " will respawn in 2 minutes";
            msgActiveUntil = System.currentTimeMillis() + Duration.ofSeconds(30).toMillis();
        }
    }


    @SubscribeEvent
    public void renderLast(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        // add waypoints in each corner
//        renderUpperTitle();
    }

    public static void renderUpperTitle() {
        if (System.currentTimeMillis() <= msgActiveUntil) return;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(width / 2), (float)(height / 2), 0.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.scale(4.0F, 4.0F, 4.0F);
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        fr.drawString(msg, (float)(-fr.getStringWidth(msg) / 2) + 10, -10f - 10f, 16777215 | 0xFF000000, true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    enum Corner {
        SOUTH_WEST("South West", new BlockPos(-205, 136, 69)),
        NORTH_WEST("North West", new BlockPos(-205, 139, -126)),
        NORTH_EAST("North East", new BlockPos(-8, 135, -127)),
        SOUTH_EAST("South East", new BlockPos(-7, 134, 70));

        public final String cornerName;
        public final BlockPos pos;

        Corner(String cornerName, BlockPos pos) {
            this.cornerName = cornerName;
            this.pos = pos;
        }

        public static Corner closestTo(Entity entity) {
            Corner closestCorner = null;
            double closest = Double.MAX_VALUE;

            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;
            for (Corner corner : Corner.values()) {
                double distSq = corner.pos.distanceSq(x, y, z);
                if (closest > distSq) {
                    closest = distSq;
                    closestCorner = corner;
                }
            }

            return closestCorner;
        }
    }
}
