package dev.u9g.imprisoned.mods;

import dev.u9g.imprisoned.Module;
import dev.u9g.imprisoned.mods.events.PlayerOutlineColorEvent;
import dev.u9g.imprisoned.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ShowBoxAboveEveryonesHead implements Module {
    @SubscribeEvent
    public void onPlayerOutlineColor(RenderWorldLastEvent event) {
        if (true) return;
        for (EntityLivingBase e : Minecraft.getMinecraft().theWorld.playerEntities) {
            EntityLivingBase player = e;
            AxisAlignedBB bb = new AxisAlignedBB(
                    player.posX - .5, player.posY + .5, player.posZ - .5,
                    player.posX + .5, player.posY + 4.5, player.posZ + .5);
            List<Entity> entities = Minecraft.getMinecraft().theWorld.getEntitiesWithinAABB(EntityArmorStand.class, bb);
            List<String> names = entities.stream().map(c -> c.getDisplayName().getFormattedText()).collect(Collectors.toList());

            RenderUtil.renderBeaconBeam(e.posX, e.posY + 3, e.posZ, 0xFF00FF, 1f, event.partialTicks, true);
        }
    }
}
