package dev.u9g.imprisoned;

import dev.u9g.imprisoned.mods.BlockHighlighter;
import dev.u9g.imprisoned.mods.Midas;
import dev.u9g.imprisoned.mods.ShowItemLore;
import dev.u9g.imprisoned.mods.ShowVignette;
import dev.u9g.imprisoned.mods.wormhole_colorizer.WormholeColorizer;
import dev.u9g.imprisoned.mods.overlays.GeneralOverlay;
import dev.u9g.imprisoned.mods.overlays.BragOverlay;
import dev.u9g.imprisoned.mods.chat_modifier.ChatProcessor;
import dev.u9g.imprisoned.mods.overlays.TradeOverlay;
import dev.u9g.imprisoned.mods.hotbar_text.BuffTextManager;
import dev.u9g.imprisoned.mods.nbt_dumper.NBTDumper;
import dev.u9g.imprisoned.mods.waypoints.ReadPingsInChat;
import dev.u9g.imprisoned.mods.waypoints.WaypointManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "imPRISONed", version = "0.0.1")
public class Imprisoned {
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
        MinecraftForge.EVENT_BUS.register(new GeneralOverlay());
        MinecraftForge.EVENT_BUS.register(new ReadPingsInChat());
        MinecraftForge.EVENT_BUS.register(new ShowVignette());
        MinecraftForge.EVENT_BUS.register(new ShowItemLore());
        MinecraftForge.EVENT_BUS.register(new WormholeColorizer());
        MinecraftForge.EVENT_BUS.register(new Midas());
    }
}
