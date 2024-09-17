package kiul.kiulduelsv2.duel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.ChatPaginator;

public class Teams {

    public static void addPlayerToDuelTeam (Player p, boolean addToBlueTeam) {
        // Get the Scoreboard Manager and Main Scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create "blue" team with prefix [BLUE]
        Team blueTeam = scoreboard.getTeam("blue");
        if (blueTeam == null) {
            blueTeam = scoreboard.registerNewTeam("blue");
            blueTeam.setPrefix(ChatColor.BLUE+""+ChatColor.BOLD+"[BLUE] "+ChatColor.RESET);
            blueTeam.setColor(ChatColor.BLUE);
        }

        // Create "red" team with prefix [RED]
        Team redTeam = scoreboard.getTeam("red");
        if (redTeam == null) {
            redTeam = scoreboard.registerNewTeam("red");
            redTeam.setPrefix(ChatColor.RED+""+ChatColor.BOLD+"[RED] "+ChatColor.RESET);
            redTeam.setColor(ChatColor.RED);
        }

        // Add the player to the chosen team
        if (addToBlueTeam) {
            blueTeam.addEntry(p.getName()); // Add player to the "blue" team
        } else {
            redTeam.addEntry(p.getName()); // Add player to the "red" team
        }

        // Set the player's scoreboard
        p.setScoreboard(scoreboard);
    }

    public static void removePlayerFromDuelTeam (Player p) {
        // Get the Scoreboard Manager and Main Scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Create "blue" team with prefix [BLUE]
        Team blueTeam = scoreboard.getTeam("blue");
        if (blueTeam == null) {
            blueTeam = scoreboard.registerNewTeam("blue");
            blueTeam.setPrefix(ChatColor.BLUE+""+ChatColor.BOLD+"[BLUE] "+ChatColor.RESET);
            return;
        }

        // Create "red" team with prefix [RED]
        Team redTeam = scoreboard.getTeam("red");
        if (redTeam == null) {
            redTeam = scoreboard.registerNewTeam("red");
            redTeam.setPrefix(ChatColor.RED+""+ChatColor.BOLD+"[RED] "+ChatColor.RESET);
            return;
        }

        // Add the player to the chosen team
        if (blueTeam.hasEntry(p.getName())) {
            blueTeam.removeEntry(p.getName()); // Add player to the "blue" team
        } else {
            redTeam.removeEntry(p.getName()); // Add player to the "red" team
        }
    }
}
