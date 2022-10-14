package dev.u9g.imprisoned.mixin.future_signs;

import dev.u9g.imprisoned.PrisonsModConfig;
import dev.u9g.imprisoned.mixin.accessor.GuiEditSignAccessor;
import dev.u9g.imprisoned.mods.future_signs.IEditSign;
import dev.u9g.imprisoned.mods.future_signs.IModifiedSign;
import dev.u9g.imprisoned.mods.future_signs.SignSelectionList;
import dev.u9g.imprisoned.mods.future_signs.TextInputUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GuiEditSign.class)
public class GuiEditSignMixin extends GuiScreen implements IEditSign
{
    private final GuiEditSign that = (GuiEditSign)(Object)this;
    private TextInputUtil textInputUtil;
    private SignSelectionList globalSelector;

    @Shadow
    private
    int editLine;

    @Shadow
    private
    int updateCounter;

    @Shadow private TileEntitySign tileSign;

    @Shadow private GuiButton doneBtn;

    @Inject(method = "initGui()V", at = @At("TAIL"))
    private void initGui(CallbackInfo info) {
        this.textInputUtil = new TextInputUtil(this.fontRendererObj, () -> ((IModifiedSign) (((GuiEditSignAccessor) this.that).tileSign())).getText(this.editLine).getUnformattedText(), text ->
                ((IModifiedSign) (((GuiEditSignAccessor) this.that).tileSign())).setText(this.editLine, new ChatComponentText(text)), 90);
    }

    @Inject(method = "onGuiClosed()V", cancellable = true, at = @At("HEAD"))
    private void onGuiClosed(CallbackInfo info) {
        if (/*SkyBlockcatiaConfig.enableSignSelectionList*/PrisonsModConfig.INSTANCE.misc.newSigns)
        {
            Keyboard.enableRepeatEvents(false);
            SignSelectionList.processSignData(((GuiEditSignAccessor)this.that).tileSign());
            info.cancel();
        }
    }

    @Inject(method = "keyTyped(CI)V", cancellable = true, at = @At("HEAD"))
    private void keyTyped(char typedChar, int keyCode, CallbackInfo info) throws IOException {
        if (PrisonsModConfig.INSTANCE.misc.newSigns)
        {
            this.textInputUtil.insert(typedChar);
            if (keyCode == 1) {
                this.actionPerformed(this.doneBtn);
            }
            this.keyPressed(keyCode);
            info.cancel();
        }
    }

    @Inject(method = "drawScreen(IIF)V", cancellable = true, at = @At("HEAD"))
    private void drawScreenPre(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        if (/*SkyBlockcatiaConfig.enableOverwriteSignEditing*/PrisonsModConfig.INSTANCE.misc.newSigns) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit"), this.width / 2, 40, 16777215);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.width / 2, 0.0F, 50.0F);
            float f = 93.75F;
            GlStateManager.scale(-f, -f, -f);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            Block block = ((GuiEditSignAccessor)this.that).tileSign().getBlockType();

            if (block == Blocks.standing_sign) {
                float f1 = ((GuiEditSignAccessor)this.that).tileSign().getBlockMetadata() * 360 / 16.0F;
                GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.0625F, 0.0F);
            } else {
                int i = ((GuiEditSignAccessor)this.that).tileSign().getBlockMetadata();
                float f2 = 0.0F;

                if (i == 2)
                {
                    f2 = 180.0F;
                }

                if (i == 4)
                {
                    f2 = 90.0F;
                }

                if (i == 5)
                {
                    f2 = -90.0F;
                }
                GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(0.0F, -1.0625F, 0.0F);
            }
            IModifiedSign sign = ((IModifiedSign)((GuiEditSignAccessor)this.that).tileSign());
            sign.setSelectionState(this.editLine, this.textInputUtil.getSelectionStart(), this.textInputUtil.getSelectionEnd(), this.updateCounter / 6 % 2 == 0);
            TileEntityRendererDispatcher.instance.renderTileEntityAt(((GuiEditSignAccessor)this.that).tileSign(), -0.5D, -0.75D, -0.5D, 0.0F);
            ((IModifiedSign)((GuiEditSignAccessor)this.that).tileSign()).resetSelectionState();
            GlStateManager.popMatrix();
            super.drawScreen(mouseX, mouseY, partialTicks);

            if (/*SkyBlockcatiaConfig.enableSignSelectionList*/PrisonsModConfig.INSTANCE.misc.newSigns && this.globalSelector != null)
            {
                this.globalSelector.drawScreen(mouseX, mouseY, partialTicks);
            }
            info.cancel();
        }
    }

    @Inject(method = "drawScreen(IIF)V", at = @At("TAIL"))
    private void drawScreenPost(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        if (!/*SkyBlockcatiaConfig.enableOverwriteSignEditing*/PrisonsModConfig.INSTANCE.misc.newSigns /*&& SkyBlockEventHandler.isSkyBlock && SkyBlockcatiaConfig.enableSignSelectionList*/ && this.globalSelector != null) {
            this.globalSelector.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public TextInputUtil getTextInputUtil()
    {
        return this.textInputUtil;
    }

    @Override
    public SignSelectionList getSignSelectionList()
    {
        return this.globalSelector;
    }

    private boolean keyPressed(int keyCode) {
        if (keyCode == Keyboard.KEY_UP) {
            this.editLine = this.editLine - 1 & 3;
            this.textInputUtil.moveCaretToEnd();
            return true;
        } else if (keyCode != Keyboard.KEY_DOWN && keyCode != Keyboard.KEY_RETURN && keyCode != Keyboard.KEY_NUMPADENTER) {
            return this.textInputUtil.handleSpecialKey(keyCode);
        } else {
            this.editLine = this.editLine + 1 & 3;
            this.textInputUtil.moveCaretToEnd();
            return true;
        }
    }
}