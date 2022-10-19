package dev.u9g.imprisoned.mods.peaceful_mining;

import dev.u9g.imprisoned.PrisonsModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PeacefulMining {
    private static final int makeOpaqueAtDistance = 10;

    public static void makeSkinTransparentPre(EntityLivingBase entitylivingbase) {
        double distanceTo = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entitylivingbase);
        if (distanceTo > makeOpaqueAtDistance) return;
        if (shouldChangeTransparency(entitylivingbase)) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, transparency(entitylivingbase));
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569f);
        }
    }

    /**
     * @param entityLivingBase
     * @return 0-1 representing the players opacity with peaceful mining
     */
    public static float transparency(EntityLivingBase entityLivingBase) {
        double distanceTo = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entityLivingBase);
        if (distanceTo > makeOpaqueAtDistance) return 1;
        float opacity = PrisonsModConfig.INSTANCE.misc.peacefulSkillingTransparency + (100f - PrisonsModConfig.INSTANCE.misc.peacefulSkillingTransparency)/makeOpaqueAtDistance*(float)distanceTo;
        return opacity/100;
    }

    public static void makeSkinTransparentPost(EntityLivingBase entitylivingbase) {
        double distanceTo = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entitylivingbase);
        if (distanceTo > makeOpaqueAtDistance) return;
        if (shouldChangeTransparency(entitylivingbase)) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
        }
    }

    public static void makeHeadTransparentPre(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float partialTicks,
                                              float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        if (entity instanceof EntityPlayerSP) {
            float transparency = transparency(entity);
            if (transparency != 1f) {
                if (entity.hurtTime > 0) {
                    // See net.minecraft.client.renderer.entity.RendererLivingEntity.unsetBrightness
                    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
                    GlStateManager.enableTexture2D();
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
                    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
                    GlStateManager.disableTexture2D();
                    GlStateManager.bindTexture(0);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
                    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
                    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
                }
                GlStateManager.enableAlpha();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1f, 1f, 1f, transparency);
            }
        }
    }

    public static void makeHeadTransparentPost(EntityLivingBase entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale, CallbackInfo ci) {
        GlStateManager.disableBlend();
    }

    public static boolean shouldChangeTransparency(EntityLivingBase entityLivingBase) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        return PrisonsModConfig.INSTANCE.misc.peacefulSkilling &&
                player.getHeldItem() != null &&
                player.getHeldItem().getItem() instanceof ItemPickaxe &&
                entityLivingBase != player;
    }

}
