package com.example.mods.waypoints;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReadPingsInChat {
    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
//        System.out.println("message => \n" + event.message.getUnformattedText());
    }
}
