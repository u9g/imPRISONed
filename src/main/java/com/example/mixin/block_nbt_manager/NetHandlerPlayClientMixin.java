package com.example.mixin.block_nbt_manager;

import com.example.PrisonsModConfig;
import com.example.mods.block_nbt_manager.TileEntityNBTManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    @Shadow private Minecraft gameController;

    @Inject(method = "handleUpdateTileEntity", at = @At("HEAD"))
    public void renderItemOverlay(S35PacketUpdateTileEntity packetIn, CallbackInfo ci) {
        if (this.gameController.theWorld.isBlockLoaded(packetIn.getPos())) {
            TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.getPos());
            TileEntityNBTManager.data.put(tileentity, packetIn.getNbtCompound());
        }
    }
}
