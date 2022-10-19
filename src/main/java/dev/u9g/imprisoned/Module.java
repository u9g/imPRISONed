package dev.u9g.imprisoned;

import dev.u9g.imprisoned.mods.events.ClickedRunCommandInChatEvent;
import dev.u9g.imprisoned.mods.events.PlayerOutlineColorEvent;
import dev.u9g.imprisoned.mods.events.PlayerScaleEvent;

public class Module {
    public void onPlayerOutlineColor(PlayerOutlineColorEvent event) {}
    public void onPlayerScaleEvent(PlayerScaleEvent event) {}
    public void onClickedRunCommandInChatEvent(ClickedRunCommandInChatEvent event) {}
}
