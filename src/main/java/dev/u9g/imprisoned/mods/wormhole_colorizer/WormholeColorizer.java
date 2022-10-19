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
    private static final Pattern failRate = Pattern.compile("§c§l ?(\\d+)% failure rate");

    @SubscribeEvent
    public void tick(RenderWorldLastEvent event) {
        if (!PrisonsModConfig.INSTANCE.misc.wormholeHighlightHighestPercent && !PrisonsModConfig.INSTANCE.misc.wormholeHighlightLowestPercent) return;

        World world = Minecraft.getMinecraft().theWorld;
        int bestSuccessRate = -1;
        Entity best = null;
        int worstSuccessRate = 200;
        Entity worst = null;
        for (EntityArmorStand failStand : world.getEntities(EntityArmorStand.class, e -> e.hasCustomName() &&
                failRate.matcher(e.getDisplayName().getUnformattedText()).matches())) {
            Matcher m = failRate.matcher(failStand.getDisplayName().getUnformattedText());
            m.matches();
            int successRatePercent = 100-Integer.parseInt(m.group(1));
            if (successRatePercent > bestSuccessRate) {
                bestSuccessRate = successRatePercent;
                best = failStand;
            }
            if (successRatePercent < worstSuccessRate) {
                worstSuccessRate = successRatePercent;
                worst = failStand;
            }
        }

        if (PrisonsModConfig.INSTANCE.misc.wormholeHighlightLowestPercent && worst != null) {
            RenderUtil.renderBoundingBox(worst.posX-.5, worst.posY + .35, worst.posZ-.5, 0xFF00F00, 0.4f, event.partialTicks, true, false);
        }

        if (PrisonsModConfig.INSTANCE.misc.wormholeHighlightHighestPercent && best != null) {
            RenderUtil.renderBoundingBox(best.posX-.5, best.posY + .35, best.posZ-.5, 0x00FF00, 0.4f, event.partialTicks, true, false);
        }
    }
}
