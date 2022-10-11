package com.example;

import com.google.gson.annotations.Expose;
import dev.u9g.configlib.config.Config;
import dev.u9g.configlib.config.GuiTextures;
import dev.u9g.configlib.config.annotations.*;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Category(name = "GUI", desc = "Options that toggle GUI options.")
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

        @ConfigOption(
                name = "Skins",
                desc = ""
        )
        @ConfigGroupHeader(groupId = 99)
        public boolean _skins_ = false;

        @Expose
        @ConfigOption(
                name = "Change skin's item",
                desc = "Change skins to their corresponding applicable item, backpacks = chain chestplate"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 99)
        public boolean changeSkinItem = true;

        @Expose
        @ConfigOption(
                name = "Write skin letters",
                desc = "Writes two letters representing the name of the skin on the item in the player's inventory."
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 99)
        public boolean writeSkinLetters = true;

        @ConfigOption(
                name = "Brag Overlay",
                desc = ""
        )
        @ConfigGroupHeader(groupId = 0)
        public boolean _brag_overlay_ = false;

        @Expose
        @ConfigOption(
                name = "Enable brag overlay",
                desc = "Enables the brag overlay."
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 0)
        public boolean bragOverlayEnabled = true;

        @Expose
        @ConfigOption(
                name = "Right click to equip",
                desc = "Allow right clicking items in the brag inventory to equip them to the player"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 0)
        public boolean rightClickItemsToEquip = true;

        @ConfigOption(
                name = "Trade Overlay",
                desc = ""
        )
        @ConfigGroupHeader(groupId = 1)
        public boolean _trade_overlay_ = false;

        @Expose
        @ConfigOption(
                name = "Enable trade overlay",
                desc = "Enables the trade overlay."
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 1)
        public boolean tradeOverlayEnabled = true;

        @Expose
        @ConfigOption(
                name = "Trade text",
                desc = "\u00a7eDrag text to change the appearance of the Overlay\n" +
                "\u00a7rTrade someone to show this Overlay with useful information"
        )
        @ConfigEditorDraggableList(
                exampleText = {
                        "\u00a77 - Money: $2.50B",
                        "\u00a77 - Energy: 2.50B\n   \u00a78(1 stack + 352.51M)",
                        "\u00a77 - Slots: 150\n   \u00a78(2 stacks + 22)",
                        "\u00a77 - Heroic Slots: 150\n   \u00a78(2 stacks + 22)",
                        "\u00a77 - Exec time: 150 mins\n   \u00a78(2 hrs + 30 mins)",
                }
        )
        @ConfigGroupMember(groupId = 1)
        public List<Integer> tradeText = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));

        @ConfigOption(
                name = "Warps Overlay",
                desc = ""
        )
        @ConfigGroupHeader(groupId = 2)
        public boolean _warps_overlay = false;

        @Expose
        @ConfigOption(
                name = "Enable warps overlay",
                desc = "Enables the warp overlay (show numbers over warps)"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 2)
        public boolean warpOverlayEnabled = true;

        @Expose
        @ConfigOption(
                name = "Show zero players",
                desc = "Enables the showing zero players over warp"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 2)
        public boolean showZerosInWarp = false;

        @ConfigOption(
                name = "General Overlay",
                desc = ""
        )
        @ConfigGroupHeader(groupId = 3)
        public boolean _general_overlay_ = false;

        @Expose
        @ConfigOption(
                name = "Enable general overlay",
                desc = "Enables the general overlay, which will show an item from the specified gui on the side of it"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayEnabled = true;

        @Expose
        @ConfigOption(
                name = "Show for Lootbox NPC",
                desc = "Shows the current lootbox"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayShowLootboxNPC = true;

        @Expose
        @ConfigOption(
                name = "Show for Tinkerer",
                desc = "Shows the tinker result"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayShowTinkerResult = true;

        @Expose
        @ConfigOption(
                name = "Show for BAH",
                desc = "Show for BAH item or the waiting item"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayShowBAHResult = true;

        @Expose
        @ConfigOption(
                name = "Show for Slot bot",
                desc = "Show the flash sale item"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayShowSlotBotResult = true;

        @Expose
        @ConfigOption(
                name = "Show for Presidential Flash Sale",
                desc = "Show the item in the shop"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayPresQuestShopResult = true;

        @Expose
        @ConfigOption(
                name = "Show for /quests",
                desc = "Show the item with reset times"
        )
        @ConfigEditorBoolean
        @ConfigGroupMember(groupId = 3)
        public boolean generalOverlayQuestsResult = true;
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
                name = "Show elite bandit spotted msgs",
                desc = "They look like: (!) Elite Bandits spotted in the Diamond Zone"
        )
        @ConfigEditorBoolean
        public boolean showEliteBanditSpottedMsgs = false;

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

        // TODO: Make this a hotkey
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

        @Expose
        @ConfigOption(
                name = "Waypoint through walls",
                desc = "Enables showing waypoint beacons through walls"
        )
        @ConfigEditorBoolean
        public boolean beaconsThroughWalls = true;

        @Expose
        @ConfigOption(
                name = "Show dropped item's lore",
                desc = "Enables showing the lore of the item entity you are pointing to"
        )
        @ConfigEditorBoolean
        public boolean showDroppedItemLore = true;
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
        public boolean lore = true;
    }
}
