package dev.u9g.imprisoned.mods.waypoints;

import dev.u9g.imprisoned.Imprisoned;
import dev.u9g.imprisoned.Module;
import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.imprisoned.mods.events.ClickedRunCommandInChatEvent;
import dev.u9g.imprisoned.utils.RenderUtil;
import com.google.common.collect.Lists;
import dev.u9g.imprisoned.utils.Utils;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WaypointManager extends Module {
    private static final Set<Waypoint> waypoints = ConcurrentHashMap.newKeySet();

    private static class Waypoint {
        public double x;
        public double y;
        public double z;
        public long expireAt;
        public String name;

        public Waypoint(double x, double y, double z, long expireAt, String name) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.expireAt = expireAt;
            this.name = name;
        }

        public void renderBeacon(float partialTicks) {
            RenderUtil.renderBeaconBeam(x, y, z, 0x772991, 1, partialTicks, PrisonsModConfig.INSTANCE.misc.beaconsThroughWalls);
        }

        public void renderNametag(float partialTicks) {
            RenderUtil.renderWayPoint(Lists.newArrayList(name), x, y + 2, z, 0x772991, partialTicks);
        }
    }

    public static void registerWaypoint(double x, double y, double z, Duration stayFor, String waypointName) {
        waypoints.add(new Waypoint(x, y, z, System.currentTimeMillis() + stayFor.toMillis(), waypointName));
    }

    public static void removeWaypoint(double x, double y, double z) {
        waypoints.removeIf(c -> c.x == x && c.y == y && c.z == z);
    }

    public static void removeWaypoint(String name) {
        waypoints.removeIf(c -> c.name.equals(name));
    }


    public void tickAndRenderWaypoints(float partialTicks) {
        Iterator<Waypoint> iterator = waypoints.iterator();
        while (iterator.hasNext()) {
            Waypoint waypoint = iterator.next();
            if (System.currentTimeMillis() > waypoint.expireAt) {
                iterator.remove();
            } else {
                waypoint.renderBeacon(partialTicks);
                waypoint.renderNametag(partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        tickAndRenderWaypoints(event.partialTicks);
    }

    @Override
    public void onClickedRunCommandInChatEvent(ClickedRunCommandInChatEvent event) {
        if (event.getCommandToRun().startsWith("/waypoint ")) {
            String[] parts = event.getCommandToRun().replace("/waypoint ", "").split(",");
            String name = parts[0];
            int x = Utils.parse(parts[1]);
            int y = Utils.parse(parts[2]);
            int z = Utils.parse(parts[3]);
            registerWaypoint(x, y, z, Duration.ofMinutes(30), name);
            event.dontSendToServer();
        }
    }
}
