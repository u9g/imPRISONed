package com.example.mixin;

import com.example.mods.chat_modifier.ChatProcessor;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {
    @Shadow public abstract void sendChatMessage(String msg, boolean addToChat);

    @Redirect(method = "handleComponentClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;sendChatMessage(Ljava/lang/String;Z)V"))
    public void handleComponentClick(GuiScreen instance, String msg, boolean addToChat) {
        if (!ChatProcessor.processChatRunCommand(msg)) {
            sendChatMessage(msg, false);
        }
    }
}
