package dev.u9g.imprisoned.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.Slot;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static int parse(String numStr) {
        return Integer.parseInt(numStr.replace(",", ""));
    }

    public static void highlight(Slot slot, int color) {
        Gui.drawRect(
                slot.xDisplayPosition,
                slot.yDisplayPosition,
                slot.xDisplayPosition + 16,
                slot.yDisplayPosition + 16,
                color
        );
    }

    public static List<String> sidebarLines() {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (scoreboard == null) return Collections.EMPTY_LIST;
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
        if (objective == null) return Collections.EMPTY_LIST;
        return scoreboard
                .getSortedScores(objective).stream()
                .filter(c -> c != null && c.getPlayerName() != null && !c.getPlayerName().startsWith("#"))
                .limit(15)
                .map(c -> ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(c.getPlayerName()), c.getPlayerName()))
                .collect(Collectors.toList());
    }
}
