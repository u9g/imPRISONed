package dev.u9g.imprisoned.mixin.mod_settings_hotkey;

import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.configlib.config.MyModConfigEditor;
import dev.u9g.configlib.config.ScreenElementWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin {
    @Inject(method = "renderGameOverlay", at = @At("TAIL"))
    private void keypress(float partialTicks, CallbackInfo ci) {
        if (Minecraft.getMinecraft().currentScreen == null && Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            Minecraft.getMinecraft().displayGuiScreen(new ScreenElementWrapper(new MyModConfigEditor(PrisonsModConfig.INSTANCE)));
        }
    }
}
