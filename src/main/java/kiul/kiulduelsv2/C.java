package kiul.kiulduelsv2;

import kiul.kiulduelsv2.duel.DuelManager;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.party.PartyManager;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// not used in my user, implement it yourself.
public class C {

    public static Plugin plugin = Kiulduelsv2.getPlugin(Kiulduelsv2.class);
    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-fA-F])");

    public static String primary = "&#5c8a0b";
    public static String secondary = "&#5c944e";
    public static String text = "&#e0deca";

    public static double safeDivide(double dividend, double divisor) {
        if(Double.compare(divisor, Double.NaN) == 0) return Double.NaN;
        if(Double.compare(dividend, Double.NaN) == 0) return Double.NaN;
        if(Double.compare(divisor, 0.0) == 0) {
            if(Double.compare(dividend, 0.0) == -1) {
                dividend = 1.0;
            }
            divisor = 1.0;
        }
        if(Double.compare(divisor, -0.0) == 0) {
            if(Double.compare(dividend, -0.0) == 1) {
                dividend = 1.0;
            }
            divisor = 1.0;
        }
        return dividend / divisor;
    }

    public static String t(String textToTranslate) {
        Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
        StringBuffer buffer = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static PartyManager partyManager = new PartyManager();
    public static DuelManager duelManager = new DuelManager();
    public static ScoreboardLibrary scoreboardLibrary;

    public static int[] splitTimestampSince(long pastTimeStamp) {
        long millisecondsRemaining = System.currentTimeMillis() - pastTimeStamp;
        long hours = millisecondsRemaining / 3600000;
        long minutes = (millisecondsRemaining % 3600000) / 60000;
        long seconds = ((millisecondsRemaining % 3600000) % 60000) / 1000;
        return new int[]{(int)hours, (int)minutes, (int)seconds};
    }
    public static int[] splitTimestampUntil(long futureTimestamp) {
        long millisecondsRemaining = futureTimestamp - System.currentTimeMillis();
        long hours = millisecondsRemaining / 3600000;
        long minutes = (millisecondsRemaining % 3600000) / 60000;
        long seconds = ((millisecondsRemaining % 3600000) % 60000) / 1000;
        return new int[]{(int)hours, (int)minutes, (int)seconds};
    }

}
