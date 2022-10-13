package com.example.mods.chat_modifier;

import com.example.ExampleMod;
import com.example.PrisonsModConfig;
import com.example.mixin.accessor.ChatComponentStyleAccessor;
import com.example.mixin.accessor.GuiNewChatAccessor;
import com.example.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatProcessor {
    private static final Pattern ELITE_BANDITS_SPOTTED = Pattern.compile("\\(!\\) Elite Bandits spotted in the .+ Zone");
    private static final Pattern LOCATION_BRAG = Pattern.compile("»§b(.+) (-?[\\d,]+)x, (-?[\\d,]+)y, (-?[\\d,]+)z«");
    private boolean ignoreNextEmptyLine = false;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String unformatted = event.message.getUnformattedText();
        Matcher ELITE_BANDITS_SPOTTED_MATCHER = ELITE_BANDITS_SPOTTED.matcher(unformatted);
        if (!PrisonsModConfig.INSTANCE.chat.showSmugglerMsgs) {
            if (unformatted.equals("Smuggler - In Stock!")) {
                List<ChatLine> msgs = ((GuiNewChatAccessor)Minecraft.getMinecraft().ingameGUI.getChatGUI()).chatLines();
                if (msgs.size() > 0) {
                    msgs.remove(msgs.size() - 1); // remove white space
                }
                event.setCanceled(true);
            } else if (unformatted.equals("The Smuggler still has stock available! Find him hidden somewhere in the warzone to purchase rare items from him!")) {
                ignoreNextEmptyLine = true;
                event.setCanceled(true);
            } else if (ignoreNextEmptyLine && unformatted.equals("")) {
                ignoreNextEmptyLine = false;
                event.setCanceled(true);
            } else if (ELITE_BANDITS_SPOTTED_MATCHER.matches()) {
                if (PrisonsModConfig.INSTANCE.chat.showEliteBanditSpottedMsgs) event.setCanceled(true);
            }
        }
    }

    public static void processSiblings(IChatComponent sibling, IChatComponent parent) {
        if (PrisonsModConfig.INSTANCE.chat.makeBragHover) {
            ChatStyle style = ((ChatComponentStyleAccessor) sibling).style();
            if (style != null) {
                ClickEvent event = style.getChatClickEvent();
                if (event != null && event.getValue().startsWith("/brag ")) {
                    style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7Click to view §b" + event.getValue().split(" ")[1] + "'s §7inventory")));
                    return;
                }
            }
            for (IChatComponent _sibling : sibling.getSiblings()) {
                processSiblings(_sibling, parent);
            }
        }

        if (sibling instanceof ChatComponentText) {
            String txt = ((ChatComponentText)sibling).getUnformattedText();
            Matcher matcher = LOCATION_BRAG.matcher(txt);
            if (matcher.matches()) {
                // get username
                String username = "";
                try {
                    for (IChatComponent c : parent.getSiblings()) {
                        ClickEvent event = ((ChatComponentStyleAccessor)c).style().getChatClickEvent();
                        if (event != null && event.getAction() == ClickEvent.Action.SUGGEST_COMMAND && event.getValue().startsWith("/msg ")) {
                            username = event.getValue().trim().split(" ")[1];
                            break;
                        }
                    }
                } catch (Exception e) {e.printStackTrace();}
                // get username
                ChatStyle style = ((ChatComponentStyleAccessor) sibling).style();
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/waypoint " + username + "'s ping," + matcher.group(2) + "," + matcher.group(3) + "," + matcher.group(4)));
                style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7Click to §bmake a waypoint §7(for 30 min)")));
            }
        }
    }

    public static boolean processChatRunCommand(String value) {
        if (value.startsWith("/waypoint ")) {
            String[] parts = value.replace("/waypoint ", "").split(",");
            String name = parts[0];
            int x = Utils.parse(parts[1]);
            int y = Utils.parse(parts[2]);
            int z = Utils.parse(parts[3]);
            ExampleMod.waypointManager.registerWaypoint(x, y, z, Duration.ofMinutes(30), name);
            return true;
        }
        return false;
    }
}
