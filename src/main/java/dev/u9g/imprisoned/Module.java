package dev.u9g.imprisoned;

import dev.u9g.imprisoned.mods.events.PlayerOutlineColorEvent;
import dev.u9g.imprisoned.mods.events.PlayerScaleEvent;

public interface Module {
    default void onPlayerOutlineColor(PlayerOutlineColorEvent event) {}
    default void onPlayerScaleEvent(PlayerScaleEvent event) {}
}
