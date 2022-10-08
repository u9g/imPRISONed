package com.example.mods.waypoints;

import com.example.utils.RenderUtil;
import com.google.common.collect.Lists;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaypointManager {
    private static final List<Waypoint> waypoints = new ArrayList<>();
    private static final List<Waypoint> waypointsToAdd = new ArrayList<>();

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
            RenderUtil.renderBeaconBeam(x, y, z, 0x772991, 1, partialTicks, true);
        }

        public void renderNametag(float partialTicks) {
            RenderUtil.renderWayPoint(Lists.newArrayList(name), x, y, z, 0x772991, partialTicks);
        }
    }

    public void registerWaypoint(double x, double y, double z, Duration stayFor) {
        waypointsToAdd.add(new Waypoint(x, y, z, System.currentTimeMillis() + stayFor.toMillis(), "YOO WAYPOINT"));
    }

    public void tickAndRenderWaypoints(float partialTicks) {
        Iterator<Waypoint> iterator = waypoints.listIterator();
        while (iterator.hasNext()) {
            Waypoint waypoint = iterator.next();
            if (System.currentTimeMillis() > waypoint.expireAt) {
                iterator.remove();
            } else {
                waypoint.renderBeacon(partialTicks);
                waypoint.renderNametag(partialTicks);
            }
        }
        synchronized (waypointsToAdd) {
            waypoints.addAll(waypointsToAdd);
            waypointsToAdd.clear();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        tickAndRenderWaypoints(event.partialTicks);
    }
}
