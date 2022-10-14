package dev.u9g.imprisoned.mods.wormhole_colorizer;

import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.imprisoned.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WormholeColorizer {
    private static final Pattern failRate = Pattern.compile("§c§l ?\\d+% failure rate");
    private static final Pattern success = Pattern.compile("§a§l ?(\\d+)% success rate");

    @SubscribeEvent
    public void tick(RenderWorldLastEvent event) {
        if (!PrisonsModConfig.INSTANCE.misc.wormholeHighlightHighestPercent && !PrisonsModConfig.INSTANCE.misc.wormholeHighlightLowestPercent) return;

        World world = Minecraft.getMinecraft().theWorld;
        int bestSuccessRate = -1;
        Entity best = null;
        int worstSuccessRate = 200;
        Entity worst = null;
        // TODO: These are success-fail rates are just inverts of eachother, so we don't actually need to get the successrate
        for (EntityArmorStand failStands : world.getEntities(EntityArmorStand.class, e -> e.hasCustomName() &&
                failRate.matcher(e.getDisplayName().getUnformattedText()).matches())) {
            List<Entity> entities = world.getEntitiesWithinAABB(EntityArmorStand.class, failStands.getEntityBoundingBox().expand(0, 1, 0));
            Entity successRateEntity = entities.stream().filter(c -> success.matcher(c.getDisplayName().getUnformattedText()).matches()).findFirst().get();
            Matcher m = success.matcher(successRateEntity.getDisplayName().getUnformattedText());
            m.matches();
            int successRatePercent = Integer.parseInt(m.group(1));
            if (successRatePercent > bestSuccessRate) {
                bestSuccessRate = successRatePercent;
                best = successRateEntity;
            }
            if (successRatePercent < worstSuccessRate) {
                worstSuccessRate = successRatePercent;
                worst = successRateEntity;
            }
        }

        if (PrisonsModConfig.INSTANCE.misc.wormholeHighlightLowestPercent && worst != null) {
            RenderUtil.renderBoundingBox(worst.posX-.5, worst.posY, worst.posZ-.5, 0xFF00F00, 0.4f, event.partialTicks, true, false);
        }

        if (PrisonsModConfig.INSTANCE.misc.wormholeHighlightHighestPercent && best != null) {
            RenderUtil.renderBoundingBox(best.posX-.5, best.posY, best.posZ-.5, 0x00FF00, 0.4f, event.partialTicks, true, false);
        }
    }
}