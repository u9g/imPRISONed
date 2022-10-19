package dev.u9g.imprisoned.mods;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ShowVignette {
    protected static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");

    // https://forums.minecraftforge.net/topic/66923-solved-1122-display-custom-vignette/
    private static void renderVignette(Minecraft mc, ScaledResolution scaledRes, float r, float g, float b, float a) {
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GlStateManager.color(r, g, b, a);
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(vignetteTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer bufferbuilder = tessellator.getWorldRenderer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return;
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaled = new ScaledResolution(mc);
        // hubs have a creative gameType
        WorldInfo info = Minecraft.getMinecraft().theWorld.getWorldInfo();
//        if ((info.getSpawnX() == -1536 && info.getSpawnY() == -1536 && info.getSpawnZ() == -1536) ||
//                (info.getSpawnX() == -1526 && info.getSpawnY() == 169 && info.getSpawnZ() == -317)) {
//            List<String> lines = sidebarLines();
//            boolean guarded = false;
//            for (String line : lines) {
//                if (line.trim().startsWith("§cGuarded")) {
//                    guarded = true;
//                    break;
//                }
//            }
//            if (!guarded) {
//                renderVignette(mc, scaled, 1, 0, 0, 0.3f);
//            }
//        }
    }
}
