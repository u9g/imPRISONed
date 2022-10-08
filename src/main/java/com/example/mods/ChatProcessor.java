package com.example.mods;

import com.example.PrisonsModConfig;
import com.example.mixin.accessor.ChatComponentStyleAccessor;
import com.example.mixin.accessor.GuiNewChatAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class ChatProcessor {
    private boolean ignoreNextEmptyLine = false;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String unformatted = event.message.getUnformattedText();
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
            }
        }
    }

    public static void processSiblings(IChatComponent sibling) {
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
                processSiblings(_sibling);
            }
        }
    }
}
