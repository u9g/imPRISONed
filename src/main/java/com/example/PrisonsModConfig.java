package com.example;

import com.google.gson.annotations.Expose;
import dev.u9g.configlib.config.Config;
import dev.u9g.configlib.config.GuiTextures;
import dev.u9g.configlib.config.annotations.Category;
import dev.u9g.configlib.config.annotations.ConfigEditorBoolean;
import dev.u9g.configlib.config.annotations.ConfigEditorSlider;
import dev.u9g.configlib.config.annotations.ConfigOption;
import net.minecraft.util.EnumChatFormatting;

public class PrisonsModConfig implements Config {
    public static PrisonsModConfig INSTANCE = new PrisonsModConfig();

    @Override
    public void executeRunnable(String s) {
    }

    @Override
    public String getHeaderText() {
        return "imPRISONed v1.0.0 by " + EnumChatFormatting.AQUA + "U9G" + EnumChatFormatting.RESET + ", config by " + EnumChatFormatting.DARK_PURPLE + "Moulberry";
    }

    @Override
    public void save() {

    }

    @Override
    public Badge[] getBadges() {
        return new Badge[] { new Badge(GuiTextures.GITHUB, "https://github.com/u9g") };
    }

    @Expose
    @Category(name = "Gui", desc = "Options that toggle gui options.")
    public Gui gui = new Gui();

    @Expose
    @Category(name = "Chat", desc = "Options that toggle chat options.")
    public Chat chat = new Chat();

    @Expose
    @Category(name = "Performance", desc = "Options for making your game faster.")
    public Performance perf = new Performance();

    @Expose
    @Category(name = "Misc", desc = "Options that don't fit in any other category.")
    public Misc misc = new Misc();

    @Expose
    @Category(name = "Debug", desc = "Options that help develop for the mod.")
    public Debug debug = new Debug();

    public static class Gui {
        @Expose
        @ConfigOption(
                name = "Building blocks",
                desc = "Write the number of building blocks in the top left of your gui"
        )
        @ConfigEditorBoolean
        public boolean renderBuildingBlocks = true;

        @Expose
        @ConfigOption(
                name = "Haste text",
                desc = "Write haste to the left of the hotbar"
        )
        @ConfigEditorBoolean
        public boolean hasteText = true;
        @Expose
        @ConfigOption(
                name = "Cauldron text",
                desc = "Write cauldron to the right of the hotbar"
        )
        @ConfigEditorBoolean
        public boolean cauldronText = true;
    }

    public static class Chat {
        @Expose
        @ConfigOption(
                name = "Show smuggler msgs",
                desc = "They look like: The Smuggler still has stock available! Find him hidden somewhere in the warzone to purchase rare items from him!"
        )
        @ConfigEditorBoolean
        public boolean showSmugglerMsgs = false;

        @Expose
        @ConfigOption(
                name = "Make brags hoverable",
                desc = "Write 'Click to view Username's inventory' when hovering a brag"
        )
        @ConfigEditorBoolean
        public boolean makeBragHover = true;
    }

    public static class Performance {
        @Expose
        @ConfigOption(
                name = "Show block digging particles",
                desc = "FPS Impact: \u00a74HIGH , for better perf: disable"
        )
        @ConfigEditorBoolean
        public boolean renderDiggingParticles = false;
    }

    public static class Misc {
        @Expose
        @ConfigOption(
                name = "Pets bar",
                desc = "Draw durability bar for cooldown on pet"
        )
        @ConfigEditorBoolean
        public boolean drawBarForPets = true;

        @Expose
        @ConfigOption(
                name = "Fly speed boost",
                desc = "Multiply the base flying speed by this amount:"
        )
        @ConfigEditorSlider(
                minValue = 1,
                maxValue = 2,
                minStep = 1f
        )
        public float flySpeedBoost = 1;

        @Expose
        @ConfigOption(
                name = "Use 1.15 signs",
                desc = "Adds ctrl+a, ctrl+c, ctrl+c, cursor selection, and more to signs"
        )
        @ConfigEditorBoolean
        public boolean newSigns = true;
    }

    public static class Debug {
        @Expose
        @ConfigOption(
                name = "NBT Dump Keybind",
                desc = "Ctrl+H will dump the hovered item's nbt to your clipboard. If not in a gui, will dump the hovered block's tiledata."
        )
        @ConfigEditorBoolean
        public boolean dumpNBT = true;

        @Expose
        @ConfigOption(
                name = "Add debug lines to lore",
                desc = "Adds some dev information to item lores."
        )
        @ConfigEditorBoolean
        public boolean lore = false;
    }
}
