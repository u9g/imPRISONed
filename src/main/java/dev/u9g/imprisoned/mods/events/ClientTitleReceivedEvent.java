package dev.u9g.imprisoned.mods.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class ClientTitleReceivedEvent extends Event {
    public static class Title extends ClientTitleReceivedEvent {
        public String text;

        public Title(String text) {
            this.text = text;
        }
    }

    public static class Subtitle extends ClientTitleReceivedEvent {
        public String text;

        public Subtitle(String text) {
            this.text = text;
        }
    }
}
