package com.example.mixin;

import com.example.mixin.accessor.ChatComponentStyleAccessor;
import com.example.mods.ChatProcessor;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class HandleChatMixin {
    @Inject(method = "handleChat(Lnet/minecraft/network/play/server/S02PacketChat;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;onClientChat(BLnet/minecraft/util/IChatComponent;)Lnet/minecraft/util/IChatComponent;"))
    public void handleChat(S02PacketChat packetIn, CallbackInfo ci) {
        ChatProcessor.processSiblings(packetIn.getChatComponent());
    }
}
