package dev.u9g.imprisoned.mixin.redirect_chat_commands;

import dev.u9g.imprisoned.mods.chat_modifier.ChatProcessor;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class HandleChatMixin {
    @Inject(method = "handleChat(Lnet/minecraft/network/play/server/S02PacketChat;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onClientChat(BLnet/minecraft/util/IChatComponent;)Lnet/minecraft/util/IChatComponent;", remap = false))
    public void handleChat(S02PacketChat packetIn, CallbackInfo ci) {
        ChatProcessor.processSiblings(packetIn.getChatComponent(), packetIn.getChatComponent());
    }
}
