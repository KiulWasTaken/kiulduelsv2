package kiul.kiulduelsv2.scoreboard;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
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
        double winRate = C.safeDivide(wins,(wins+losses))*100;
        double kdRatio = C.safeDivide(kills,deaths);

        Score first = objective.getScore(" ");
        Score second = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&a"));
        Score third = objective.getScore("  " + C.t("&7Wins&8 » &f"+wins));
        Score fourth = objective.getScore("  " + C.t("&7Losses&8 » &f"+losses));
        Score fifth = objective.getScore("  " + C.t("&7Winrate&8 » &f"+df.format(winRate)+"%"));
        Score sixth = objective.getScore("  " + C.t("&7Winstreak&8 » &f"+streak));
        Score seventh = objective.getScore("  " + C.t("&7Best Streak&8 » &f"+best_streak));
        Score eighth = objective.getScore("  " + C.t("&7Kills&8 » &f"+kills));
        Score ninth = objective.getScore("  " + C.t("&7Deaths&8 » &f"+deaths));
        Score tenth = objective.getScore("  " + C.t("&7K/D&8 » &f"+df.format(kdRatio)));
        Score eleventh = objective.getScore("  " + C.t(" "));
        Score twelfth = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&c"));
        Score thirteenth = objective.getScore("  " + C.t("&7» &fUnranked &7«"));
        Score fourteenth = objective.getScore("  " + C.t("&7||||||||||||||||||||||||||||| &f0%"));
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

    public static Sidebar duelSidebar (Player p, List<Player> duelMembers, String rating, long duelStartTime) {
        int[] times = C.splitTimestampSince(duelStartTime);

        Component ping = Component.empty();
        for (int i = 0; i < duelMembers.size(); i++) {
            Player members = duelMembers.get(i);
            if (members == p) {
                ping.append(Component.text( members.getPing()+"ms ").color(NamedTextColor.GREEN));
            } else {
                ping.append(Component.text( members.getPing()+"ms ").color(NamedTextColor.RED));
            }
            if (i != duelMembers.size()-1) {
                ping.append(Component.text("- ").color(NamedTextColor.WHITE));
            }

        }

        MiniMessage miniMessage = MiniMessage.miniMessage();
        SidebarComponent lines = SidebarComponent.builder()
                .addComponent(SidebarComponent.staticLine(miniMessage.deserialize("Duel - " + rating)))
                .addDynamicLine(() -> miniMessage.deserialize(" <gray>Duration  <dark_gray>» <white>" + String.format("%02d:%02d:%02d",times[0],times[1],times[2])))
                .addDynamicLine(() ->  miniMessage.deserialize(" <gray>Latency <dark_gray>» <white>").append(ping))
                .build();
        ComponentSidebarLayout layout = new ComponentSidebarLayout(
                SidebarComponent.staticLine(miniMessage.deserialize("<#256A05><bold>K<#2C6E06><bold>I<#347307><bold>U<#3B7707><bold>L<#427B08><bold>.<#4A7F09><bold>N<#51840A><bold>E<#58880B><bold>T <#608E0C><bold>P<#67950D><bold>R<#6E9C0E><bold>A<#76A310><bold>C<#7DAA11><bold>T<#84B112><bold>I<#8CB814><bold>C<#93BF15><bold>E")),
                lines
        );
        Sidebar sidebar = C.scoreboardLibrary.createSidebar();
        layout.apply(sidebar);
    return sidebar;}
}
