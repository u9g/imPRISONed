package com.example.mods.wormhole_colorizer;

import com.example.PrisonsModConfig;
import com.example.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WormholeColorizer {
    private static final Pattern failRate = Pattern.compile("§c§l ?\\d+% failure rate");
    private static final Pattern success = Pattern.compile("§a§l ?(\\d+)% success rate");

    @SubscribeEvent
    public void tick(RenderWorldLastEvent event) {
        if (!PrisonsModConfig.INSTANCE.misc.wormholeHighlight) return;

        World world = Minecraft.getMinecraft().theWorld;
        int bestSuccessRate = -1;
        Entity toHighlight = null;
        for (EntityArmorStand failStands : world.getEntities(EntityArmorStand.class, e -> e.hasCustomName() &&
                failRate.matcher(e.getDisplayName().getUnformattedText()).matches())) {
            List<Entity> entities = world.getEntitiesWithinAABB(EntityArmorStand.class, failStands.getEntityBoundingBox().expand(0, 1, 0));
            Entity successRateEntity = entities.stream().filter(c -> success.matcher(c.getDisplayName().getUnformattedText()).matches()).findFirst().get();
            Matcher m = success.matcher(successRateEntity.getDisplayName().getUnformattedText());
            m.matches();
            int successRatePercent = Integer.parseInt(m.group(1));
            if (successRatePercent > bestSuccessRate) {
                bestSuccessRate = successRatePercent;
                toHighlight = successRateEntity;
            }
        }
        if (toHighlight != null) RenderUtil.renderBoundingBox(toHighlight.posX-.5, toHighlight.posY, toHighlight.posZ-.5, 0x00FF00, 0.4f, event.partialTicks, true, false);
    }
}
