package com.example.mods.waypoints;

import com.example.PrisonsModConfig;
import com.example.utils.RenderUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WaypointManager {
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

    public void registerWaypoint(double x, double y, double z, Duration stayFor, String waypointName) {
        waypoints.add(new Waypoint(x, y, z, System.currentTimeMillis() + stayFor.toMillis(), waypointName));
    }

    public void removeWaypoint(double x, double y, double z) {
        waypoints.removeIf(c -> c.x == x && c.y == y && c.z == z);
    }

    public void removeWaypoint(String name) {
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
}
