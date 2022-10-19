package dev.u9g.imprisoned.mixin.events;

import dev.u9g.imprisoned.Imprisoned;
import dev.u9g.imprisoned.mods.chat_modifier.ChatProcessor;
import dev.u9g.imprisoned.mods.events.ClickedRunCommandInChatEvent;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {
    @Shadow public abstract void sendChatMessage(String msg, boolean addToChat);

    @Redirect(method = "handleComponentClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;sendChatMessage(Ljava/lang/String;Z)V"))
    public void handleComponentClick(GuiScreen instance, String cmdToRun, boolean addToChat) {
        ClickedRunCommandInChatEvent event = new ClickedRunCommandInChatEvent(cmdToRun);
        Imprisoned.modules.forEach(c -> c.onClickedRunCommandInChatEvent(event));
        if (event.shouldSendToServer()) {
            sendChatMessage(cmdToRun, false);
        }
    }
}
