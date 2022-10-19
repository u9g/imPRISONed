package dev.u9g.imprisoned.mods.game.mining_displays;

import dev.u9g.imprisoned.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector2f;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiningDisplays {
    private static final Pattern ENERGY_LINE_PATTERN = Pattern.compile("§.§.§.\\(§.(.+)§. / (.+)\\)");

    @SubscribeEvent
    public void renderHotbar(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HELMET) return;
        makeMiningLevelBar();
        makeEnergyBar();
    }

    private void makeMiningLevelBar() {
        drawBar(0, "§6§lMining Level §r§f" + Minecraft.getMinecraft().thePlayer.experienceLevel, Minecraft.getMinecraft().thePlayer.experience);
    }

    private void makeEnergyBar() {
        ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (heldItem == null || !heldItem.hasTagCompound()) return;
        Optional<String> energyLine = Minecraft.getMinecraft().thePlayer.getHeldItem().getTooltip(Minecraft.getMinecraft().thePlayer, false)
                .stream().filter(s -> ENERGY_LINE_PATTERN.matcher(s).matches()).findFirst();
        if (!energyLine.isPresent()) return;

        String line = energyLine.get();
        Matcher matcher = ENERGY_LINE_PATTERN.matcher(line);
        matcher.matches();
        int amt = Utils.parse(matcher.group(1));
        int max = Utils.parse(matcher.group(2));
        float factor = (amt > max) ? 1 : (float) amt / (float) max;
        drawBar(30, "§b§lCosmic Energy§r " + line, factor);
    }

    public static void drawBar(int yOff, String toWrite, float factor) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.pushMatrix();
        GlStateManager.scale(sr.getScaleFactor(), sr.getScaleFactor(), sr.getScaleFactor());
        GlStateManager.color(1, 1, 1, 1); // reset color so text color doesn't bleed to next lines
        GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
        Vector2f bar = new Vector2f((float)(sr.getScaledWidth_double() / sr.getScaleFactor() - 190), 15);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("imPRISONed", "gui/statBars.png"));
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        gui.drawTexturedModalRect((int) bar.x, (int) bar.y + yOff , 0, 30, 182, 5);
        gui.drawTexturedModalRect((int) bar.x, (int) bar.y + yOff, 0, 35, (int) (factor * 182f), 5);
        fr.drawStringWithShadow(toWrite, (int)(bar.x + 182 - fr.getStringWidth(toWrite)), (int)(bar.y + yOff - 9), 0xFFFFFF);
        GlStateManager.popMatrix();
    }
}
