package com.example.mixin.events;

import com.example.mods.events.ClientTitleReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    @Inject(method = "handleTitle", at = @At("HEAD"))
    void onTitle(S45PacketTitle packetIn, CallbackInfo ci) {
        if (packetIn.getType() == S45PacketTitle.Type.TITLE) {
            MinecraftForge.EVENT_BUS.post(new ClientTitleReceivedEvent.Title(packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : ""));
        } else if (packetIn.getType() == S45PacketTitle.Type.SUBTITLE) {
            MinecraftForge.EVENT_BUS.post(new ClientTitleReceivedEvent.Subtitle(packetIn.getMessage() != null ? packetIn.getMessage().getFormattedText() : ""));
        }
    }
}
