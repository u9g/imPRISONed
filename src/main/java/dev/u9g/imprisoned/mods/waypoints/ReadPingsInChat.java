package dev.u9g.imprisoned.mods.waypoints;

import dev.u9g.imprisoned.Imprisoned;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.time.Duration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.u9g.imprisoned.utils.Utils.parse;

public class ReadPingsInChat {
    private static final Pattern GOLDEN_GOOSE = Pattern.compile("\\(!\\) The Golden Goose has been spotted at (-?[\\d,]+)x, (-?[\\d,]+)y, (-?[\\d,]+)z in the Midas Vault! /midas");
    private static final Pattern GOLDEN_GOOSE_KILLED = Pattern.compile("\\(!\\) (.+) killed the Golden Goose and received a Golden Egg Lootbox \\+ 75x Blood Diamonds!");
    private static final Pattern METEOR = Pattern.compile("§f(-?[\\d,]+)§7x, §f(-?[\\d,]+)§7y, §f(-?[\\d,]+)§7z");
    private static final Pattern SPAWNED_HEROIC_METEOR = Pattern.compile("\\(!\\) A Heroic meteor summoned by ([a-zA-Z0-9_]+) is falling from the sky at:");
    private static final Pattern SPAWNED_METEOR = Pattern.compile("\\(!\\) A meteor summoned by (.+) is falling from the sky at:");
    private static final Pattern METEORITE_SHOWER = Pattern.compile("\\(!\\) A meteorite shower is falling at:");
    private static final Pattern SOMETHING_SOON = Pattern.compile("(-?[\\d,]+)x, (-?[\\d,]+)y, (-?[\\d,]+)z \\(.+ Zone\\) ETA: .+");
    private static final Pattern MERCHANT = Pattern.compile("\\(!\\) A (.+) Merchant traveled to (-?[\\d,]+)x, (-?[\\d,]+)y, (-?[\\d,]+)z");
    private static final Pattern MERCHANT_KILLED = Pattern.compile("\\(!\\) A (.+) Merchant has been slain by ([a-zA-Z0-9_]+) at (-?[\\d,]+)x, (-?[\\d,]+)y, (-?[\\d,]+)z");
    private static final Pattern HEROIC_METEOR_LOOTED = Pattern.compile("\\(!\\) A Heroic meteor was looted by ([a-zA-Z0-9_]+): (-?[\\d,]+)x, (-?[\\d,]+)y, (-?[\\d,]+)z");
    private static final Pattern ASTEROID_COMP = Pattern.compile("\\(!\\) Competition: Asteroid Crater! /comp");
    private static final Pattern DROPSHIPS = Pattern.compile(" +§.§l(.+) Zone: §7§f(-?[\\d,]+)§7x, §f(-?[\\d,]+)§7y, §f(-?[\\d,]+)§7z§c§l \\* §f\\/help dropships");
    private static final Pattern DROPSHIPS_HOVER_COORDS = Pattern.compile("§.§l\\* §f(-?[\\d,]+)§7x, §f(-?[\\d,]+)§7y, §f(-?[\\d,]+)§7z");
    // TODO: Heroic alien invasion?
    // TODO: Location brags?
    // (!) EMERALD RUSH has spawned at 1584, 36, -367
    // (!) Chain Bandit Boss was killed by blackzaii at -1269x, 127y, -29z

    private boolean soonMessageIsMeteor = false;
    private String nextMeteorType = "errno";
    private boolean soonSomething = false;
    String nextSomething = "errno";

    @SubscribeEvent
    public void onReceiveChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText();
        Matcher GOLDEN_GOOSE_MATCHER = GOLDEN_GOOSE.matcher(message);
        Matcher METEOR_MATCHER = METEOR.matcher(message);
        Matcher SPAWNED_METEOR_MATCHER = SPAWNED_METEOR.matcher(message);
        Matcher SPAWNED_HEROIC_METEOR_MATCHER = SPAWNED_HEROIC_METEOR.matcher(message);
        Matcher HEROIC_METEOR_LOOTED_MATCHER = HEROIC_METEOR_LOOTED.matcher(message);
        Matcher METEORITE_SHOWER_MATCHER = METEORITE_SHOWER.matcher(message);
        Matcher SOMETHING_SOON_MATCHER = SOMETHING_SOON.matcher(message);
        Matcher MERCHANT_MATCHER = MERCHANT.matcher(message);
        Matcher MERCHANT_KILLED_MATCHER = MERCHANT_KILLED.matcher(message);
        Matcher ASTEROID_COMP_MATCHER = ASTEROID_COMP.matcher(message);
        Matcher DROPSHIPS_MATCHER = DROPSHIPS.matcher(message);
        Matcher GOLDEN_GOOSE_KILLED_MATCHER = GOLDEN_GOOSE_KILLED.matcher(message);

        if (GOLDEN_GOOSE_MATCHER.matches()) {
            int x = parse(GOLDEN_GOOSE_MATCHER.group(1));
            int y = parse(GOLDEN_GOOSE_MATCHER.group(2));
            int z = parse(GOLDEN_GOOSE_MATCHER.group(3));
            Imprisoned.waypointManager.registerWaypoint(x, y, z, Duration.ofMinutes(2), "Golden Goose");
        } else if (GOLDEN_GOOSE_KILLED_MATCHER.matches()) {
            Imprisoned.waypointManager.removeWaypoint("Golden Goose");
        } else if ("(!) A meteor is falling from the sky at:".equals(message) || SPAWNED_METEOR_MATCHER.matches()) {
            soonMessageIsMeteor = true;
            String name = SPAWNED_METEOR_MATCHER.matches() ? SPAWNED_METEOR_MATCHER.group(1) + "'s" : "Natural";
            nextMeteorType = name + " Meteor";
        } else if ("(!) A Heroic meteor is falling from the sky at:".equals(message) || SPAWNED_HEROIC_METEOR_MATCHER.matches()) {
            soonMessageIsMeteor = true;
            String name = SPAWNED_HEROIC_METEOR_MATCHER.matches() ? SPAWNED_HEROIC_METEOR_MATCHER.group(1) + "'s" : "Natural";
            nextMeteorType = name + " Heroic Meteor";
        } else if (soonMessageIsMeteor && METEOR_MATCHER.matches()) {
            soonMessageIsMeteor = false;
            int x = Integer.parseInt(METEOR_MATCHER.group(1));
            int y = Integer.parseInt(METEOR_MATCHER.group(2));
            int z = Integer.parseInt(METEOR_MATCHER.group(3));
            Imprisoned.waypointManager.registerWaypoint(x, y, z, Duration.ofMinutes(1), nextMeteorType);
        } else if (METEORITE_SHOWER_MATCHER.matches()) {
            soonSomething = true;
            nextSomething = "Meteorite Shower";
        } else if (ASTEROID_COMP_MATCHER.matches()) {
            soonSomething = true;
            nextSomething = "Asteroid Comp";
        } else if (soonSomething && SOMETHING_SOON_MATCHER.matches()) {
            soonSomething = false;
            int x = parse(SOMETHING_SOON_MATCHER.group(1));
            int y = parse(SOMETHING_SOON_MATCHER.group(2));
            int z = parse(SOMETHING_SOON_MATCHER.group(3));
            Imprisoned.waypointManager.registerWaypoint(x, y, z, Duration.ofMinutes(1), nextSomething);
        } else if (MERCHANT_MATCHER.matches()) {
            int x = parse(MERCHANT_MATCHER.group(2));
            int y = parse(MERCHANT_MATCHER.group(3));
            int z = parse(MERCHANT_MATCHER.group(4));
            Imprisoned.waypointManager.registerWaypoint(x, y, z, Duration.ofMinutes(5), MERCHANT_MATCHER.group(1) + " Merchant");
        } else if (MERCHANT_KILLED_MATCHER.matches()) {
            int x = parse(MERCHANT_KILLED_MATCHER.group(3));
            int y = parse(MERCHANT_KILLED_MATCHER.group(4));
            int z = parse(MERCHANT_KILLED_MATCHER.group(5));
            Imprisoned.waypointManager.removeWaypoint(x, y, z);
        } else if (HEROIC_METEOR_LOOTED_MATCHER.matches()) {
            int x = parse(HEROIC_METEOR_LOOTED_MATCHER.group(2));
            int y = parse(HEROIC_METEOR_LOOTED_MATCHER.group(3));
            int z = parse(HEROIC_METEOR_LOOTED_MATCHER.group(4));
            Imprisoned.waypointManager.removeWaypoint(x, y, z);
        } else if (DROPSHIPS_MATCHER.matches()) {
            String[] parts = event.message
                    .getSiblings().get(0)
                    .getChatStyle()
                    .getChatHoverEvent().getValue()
                    .getUnformattedText()
                    .split("\n");
            // §6§lChain Zone
            //§6§l* §f1,224§7x, §f79§7y, §f23§7z
            //
            //§e§lGold Zone
            //§e§l* §f773§7x, §f58§7y, §f20§7z
            //
            //§7§lIron Zone
            //§7§l* §f767§7x, §f106§7y, §f402§7z
            //
            //§b§lDiamond Zone
            //§b§l* §f1,117§7x, §f71§7y, §f363§7z
            Matcher chain   = DROPSHIPS_HOVER_COORDS.matcher(parts[1]);
            Matcher gold    = DROPSHIPS_HOVER_COORDS.matcher(parts[4]);
            Matcher iron    = DROPSHIPS_HOVER_COORDS.matcher(parts[7]);
            Matcher diamond = DROPSHIPS_HOVER_COORDS.matcher(parts[10]);
            if (!chain.matches() || !gold.matches() || !iron.matches() || !diamond.matches()) {
                new Error("Well somethings wrong...").printStackTrace();
                return;
            }
            for (Map.Entry<String, Matcher> pair : ImmutableMap.of("Chain", chain, "Gold", gold, "Iron", iron, "Diamond", diamond).entrySet()) {
                Matcher matcher = pair.getValue();
                Imprisoned.waypointManager.registerWaypoint(parse(matcher.group(1)), parse(matcher.group(2)), parse(matcher.group(3)),
                        Duration.ofMinutes(4), pair.getKey() + " Dropships");
            }
        }
    }
}
