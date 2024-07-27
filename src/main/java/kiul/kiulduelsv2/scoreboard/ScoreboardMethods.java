package kiul.kiulduelsv2.scoreboard;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.StatDB;
import kiul.kiulduelsv2.duel.DuelListeners;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardMethods {

    public static DecimalFormat df = new DecimalFormat("#.##");

    public static Scoreboard lobbyScoreboard(Player p) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");

        objective.setDisplayName(C.t(" &#256A05&lK&#2C6E06&lI&#347307&lU&#3B7707&lL&#427B08&l.&#4A7F09&lN&#51840A&lE&#58880B&lT &#608E0C&lP&#67950D&lR&#6E9C0E&lA&#76A310&lC&#7DAA11&lT&#84B112&lI&#8CB814&lC&#93BF15&lE "));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);




        Team team = scoreboard.registerNewTeam("test");
        team.setSuffix(C.t("&#5c944eStatistics"));
        team.addEntry(ChatColor.translateAlternateColorCodes('&', "&a"));

        Team team2 = scoreboard.registerNewTeam("test2");
        team2.setSuffix(C.t("&#5c944eCompetitive"));
        team2.addEntry(ChatColor.translateAlternateColorCodes('&', "&c"));
        int wins = Userdata.get().getInt(p.getUniqueId()+".stats.wins");
        int losses = Userdata.get().getInt(p.getUniqueId()+".stats.losses");
        int streak = Userdata.get().getInt(p.getUniqueId()+".stats.streak");
        int best_streak = Userdata.get().getInt(p.getUniqueId()+".stats.best_streak");
        int kills = Userdata.get().getInt(p.getUniqueId()+".stats.kills");
        int deaths = Userdata.get().getInt(p.getUniqueId()+".stats.deaths");
        int crystalElo = (int) StatDB.readPlayer(p.getUniqueId(),"stat_elo_crystal");
        int smpElo = (int) StatDB.readPlayer(p.getUniqueId(),"stat_elo_smp");
        int shieldElo = (int) StatDB.readPlayer(p.getUniqueId(),"stat_elo_shield");
        String crystalPlacement = " &8(#"+StatDB.readPlayer(p.getUniqueId(),"stat_elo_crystal_placement")+")";
        String smpPlacement = " &8(#"+StatDB.readPlayer(p.getUniqueId(),"stat_elo_smp_placement")+")";
        String shieldPlacement = " &8(#"+StatDB.readPlayer(p.getUniqueId(),"stat_elo_shield_placement")+")";
        double winRate = C.safeDivide(wins,(wins+losses))*100;
        double kdRatio = C.safeDivide(kills,deaths);

        Score first = objective.getScore(" ");
        Score second = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&a"));
        Score third = objective.getScore("  " + C.t("&7Wins&8 » &f"+wins));
        Score fourth = objective.getScore("  " + C.t("&7Losses&8 » &f"+losses));
        Score fifth = objective.getScore("  " + C.t("&7Winrate&8 » &f"+df.format(winRate)+"%"));
        Score sixth = objective.getScore("  " + C.t("&7Winstreak&8 » &f"+streak));
        Score seventh = objective.getScore("  " + C.t("&7Best Streak&8 » &f"+best_streak));
//        Score eighth = objective.getScore("  " + C.t("&7Kills&8 » &f"+kills));
//        Score ninth = objective.getScore("  " + C.t("&7Deaths&8 » &f"+deaths));
        Score eighth = objective.getScore("  " + C.t("&7K/D&8 » &f"+df.format(kdRatio)));
        Score ninth = objective.getScore("  " + C.t(" "));
        Score tenth = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&c"));
        Score eleventh = objective.getScore("  " + C.t("&7Crystal&8 » &f" + crystalElo + crystalPlacement));
        Score twelfth = objective.getScore("  " + C.t("&7SMP&8 » &f" + smpElo + smpPlacement));
        Score thirteenth = objective.getScore("  " + C.t("&7Shield&8 » &f" + shieldElo + shieldPlacement));
        Score fourteenth = objective.getScore("  " + C.t(" "));
        Score fifteenth = objective.getScore("  " + C.t(" "));
        Score sixteenth = objective.getScore(C.t("&7kiul.net &8("+p.getPing()+"ms)"));


        first.setScore(15);
        second.setScore(14);
        third.setScore(13);
        fourth.setScore(12);
        fifth.setScore(11);
        sixth.setScore(10);
        seventh.setScore(9);
        eighth.setScore(8);
        ninth.setScore(7);
        tenth.setScore(6);
        eleventh.setScore(5);
        twelfth.setScore(4);
        thirteenth.setScore(3);
        fourteenth.setScore(2);
        fifteenth.setScore(1);
        sixteenth.setScore(0);



        return scoreboard;
    }

    public static Scoreboard duelScoreboard(Player p, List<Player> duelMembers) {

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("scoreboard", "dummy");

        objective.setDisplayName(C.t(" &#256A05&lK&#2C6E06&lI&#347307&lU&#3B7707&lL&#427B08&l.&#4A7F09&lN&#51840A&lE&#58880B&lT &#608E0C&lP&#67950D&lR&#6E9C0E&lA&#76A310&lC&#7DAA11&lT&#84B112&lI&#8CB814&lC&#93BF15&lE "));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team team = scoreboard.registerNewTeam("test");
        team.setSuffix(C.t("&#5c944eIn Match &f-&7 Competitive"));
        team.addEntry(ChatColor.translateAlternateColorCodes('&', "&a"));

        Score first= objective.getScore(" ");
        Score second = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&a"));
        for (int i = 0; i < duelMembers.size(); i++) {
            Score newScore = objective.getScore("  " + duelMembers.get(i).getDisplayName());
            newScore.setScore(i);
        }
        scoreboard.getEntries().size();
        int n = duelMembers.size();
        first.setScore(15);
        second.setScore(14);

        return scoreboard;
    }

    public static Scoreboard duelSidebar(Player p, List<Player> duelMembers, String rating, long duelStartTime) {
        int[] times = C.splitTimestampSince(duelStartTime);

        StringBuilder ping = new StringBuilder();
        for (int i = 0; i < duelMembers.size(); i++) {
            Player member = duelMembers.get(i);
            if (member == p) {
                ping.append(ChatColor.GREEN).append(member.getPing()).append("ms ");
            } else {
                ping.append(ChatColor.RED).append(member.getPing()).append("ms ");
            }
            if (i != duelMembers.size() - 1) {
                ping.append(ChatColor.WHITE).append("- ");
            }
        }

        // Create a new scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        // Create a new objective to display the sidebar
        Objective objective = scoreboard.registerNewObjective("duelSidebar", "dummy", ChatColor.translateAlternateColorCodes('&', "&a&lK&b&lI&c&lU&d&lL&e&l.&f&lN&g&lE&h&lT &i&lP&j&lR&k&lA&l&lC&m&lT&n&lI&o&lC&p&lE"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Add lines to the sidebar
        Score title = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&7Duel - " + rating));
        title.setScore(3);

        String duration = String.format("%02d:%02d:%02d", times[0], times[1], times[2]);
        Score durationLine = objective.getScore(ChatColor.GRAY + " Duration " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + duration);
        durationLine.setScore(2);

        Score latencyLine = objective.getScore(ChatColor.GRAY + " Latency " + ChatColor.DARK_GRAY + "» " + ping.toString());
        latencyLine.setScore(1);

        p.setScoreboard(scoreboard);

        return scoreboard;
    }
}
