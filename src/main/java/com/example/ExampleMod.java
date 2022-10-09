package com.example;

import com.example.mods.BlockHighlighter;
import com.example.mods.overlays.BragOverlay;
import com.example.mods.chat_modifier.ChatProcessor;
import com.example.mods.overlays.TradeOverlay;
import com.example.mods.hotbar_text.BuffTextManager;
import com.example.mods.nbt_dumper.NBTDumper;
import com.example.mods.waypoints.ReadPingsInChat;
import com.example.mods.waypoints.WaypointManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "examplemod", version = "0.0.1")
public class ExampleMod {
    public static WaypointManager waypointManager;
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new NBTDumper());
        waypointManager = new WaypointManager();
        MinecraftForge.EVENT_BUS.register(waypointManager);
        MinecraftForge.EVENT_BUS.register(new ReadPingsInChat());
//        ClientRegistry.registerKeyBinding(new HandledKeybind("key.waypoint_make.desc", Keyboard.KEY_LBRACKET, "key.waypoint.category", () -> {
//            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
//            wm.registerWaypoint(player.posX, player.posY, player.posZ, Duration.ofDays(1));
//        }));
        MinecraftForge.EVENT_BUS.register(new BuffTextManager());
        MinecraftForge.EVENT_BUS.register(new BlockHighlighter());
        MinecraftForge.EVENT_BUS.register(new ChatProcessor());
        MinecraftForge.EVENT_BUS.register(new TradeOverlay());
        MinecraftForge.EVENT_BUS.register(new BragOverlay());
        MinecraftForge.EVENT_BUS.register(new ReadPingsInChat());
    }
}
