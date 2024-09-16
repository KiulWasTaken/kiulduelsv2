package kiul.kiulduelsv2.scoreboard;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScoreboardMethods {

    public static DecimalFormat df = new DecimalFormat("#.##");
    public static HashMap<Player,BukkitTask> activeBoard = new HashMap<>();


    public static void startLobbyScoreboardTask(Player p) {
        if (activeBoard.get(p) != null) {
            activeBoard.get(p).cancel();
            activeBoard.remove(p);
        }
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
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
                    int wins = Userdata.get().getInt(p.getUniqueId() + ".stats.wins");
                    int losses = Userdata.get().getInt(p.getUniqueId() + ".stats.losses");
                    int streak = Userdata.get().getInt(p.getUniqueId() + ".stats.streak");
                    int best_streak = Userdata.get().getInt(p.getUniqueId() + ".stats.best_streak");
                    int kills = Userdata.get().getInt(p.getUniqueId() + ".stats.kills");
                    int deaths = Userdata.get().getInt(p.getUniqueId() + ".stats.deaths");
                    int crystalElo = (int) DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_crystal");
                    int cartElo = (int) DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_cart");
                    int smpElo = (int) DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_smp");
                    int shieldElo = (int) DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_shield");
                    String crystalPlacement = " &8(#" + DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_crystal_placement") + ")";
                    String smpPlacement = " &8(#" + DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_smp_placement") + ")";
                    String shieldPlacement = " &8(#" + DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_shield_placement") + ")";
                    String cartPlacement = " &8(#" + DuelsDB.readPlayer(p.getUniqueId(), "stat_elo_cart_placement") + ")";
                    double winRate = C.safeDivide(wins, (wins + losses)) * 100;
                    double kdRatio = C.safeDivide(kills, deaths);
                    double damageDealtDeltaRound = 0;
                    ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(p.getUniqueId() + ".stats.damagedelta");
                    for (int damageDelta : damageDeltaPerRound) {
                        damageDealtDeltaRound += damageDelta;
                    }
                    if (!damageDeltaPerRound.isEmpty()) {
                        damageDealtDeltaRound = (double)(damageDealtDeltaRound / damageDeltaPerRound.size());
                    }
                    Score first = objective.getScore("  " + C.t(" "));
                    Score second = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&a"));
                    Score third = objective.getScore("  " + C.t("&7Wins&8 » &f" + wins));
                    Score fourth = objective.getScore("  " + C.t("&7Losses&8 » &f" + losses));
                    Score fifth = objective.getScore("  " + C.t("&7Winrate&8 » &f" + df.format(winRate) + "%"));
                    Score sixth = objective.getScore("  " + C.t("&7Winstreak&8 » &f" + streak));
                    Score seventh = objective.getScore("  " + C.t("&7Best Streak&8 » &f" + best_streak));
//        Score eighth = objective.getScore("  " + C.t("&7Kills&8 » &f"+kills));
//        Score ninth = objective.getScore("  " + C.t("&7Deaths&8 » &f"+deaths));
                    Score eighth = objective.getScore("  " + C.t("&7K/D&8 » &f" + df.format(kdRatio)));
                    Score ninth = objective.getScore("  " + C.t("&7DDΔ/R&8 » &f" + df.format(damageDealtDeltaRound)));
                    Score tenth = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&c"));
                    Score eleventh = objective.getScore("  " + C.t("&7Standard&8 » &f" + smpElo + smpPlacement));
                    Score twelfth = objective.getScore("  " + C.t("&7Attrition&8 » &f" + shieldElo + shieldPlacement));
                    Score thirteenth = objective.getScore("  " + C.t("&7Vanilla&8 » &f" + crystalElo + crystalPlacement));
                    Score fourteenth = objective.getScore("  " + C.t("&7Cart&8 » &f" + cartElo + cartPlacement));
                    Score fifteenth = objective.getScore("  " + C.t(" "));
                    Score sixteenth = objective.getScore(C.t("&7kiul.net &8(" + p.getPing() + "ms)"));


                    first.setScore(0);
                    second.setScore(15);
                    third.setScore(14);
                    fourth.setScore(13);
                    fifth.setScore(12);
                    sixth.setScore(11);
                    seventh.setScore(10);
                    eighth.setScore(9);
                    ninth.setScore(8);
                    tenth.setScore(7);
                    eleventh.setScore(6);
                    twelfth.setScore(5);
                    thirteenth.setScore(4);
                    fourteenth.setScore(3);
                    fifteenth.setScore(2);
                    sixteenth.setScore(1);
                    p.setScoreboard(scoreboard);

            }
        }.runTaskTimer(C.plugin,1,80);
        activeBoard.put(p,runnable);

    }

    public static void startDuelSidebar(Player p, List<Player> duelMembers, String rating, long duelStartTime) {
        if (activeBoard.get(p) != null) {
            activeBoard.get(p).cancel();
            activeBoard.remove(p);
        }
        final BukkitTask runnable = new BukkitRunnable() {

            @Override
            public void run() {

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
                    Objective objective = scoreboard.registerNewObjective("sidebar", "dummy");

                    objective.setDisplayName(C.t(" &#256A05&lK&#2C6E06&lI&#347307&lU&#3B7707&lL&#427B08&l.&#4A7F09&lN&#51840A&lE&#58880B&lT &#608E0C&lP&#67950D&lR&#6E9C0E&lA&#76A310&lC&#7DAA11&lT&#84B112&lI&#8CB814&lC&#93BF15&lE "));
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    Team team = scoreboard.registerNewTeam("test");
                    team.setSuffix(C.t("&#5c944eIn Match &f-&7 " + rating));
                    team.addEntry(ChatColor.translateAlternateColorCodes('&', "&a"));

                    // Add lines to the sidebar
                    Score anotherEmptyLine = objective.getScore(" ");
                    anotherEmptyLine.setScore(5);
                    Score title = objective.getScore(ChatColor.translateAlternateColorCodes('&', "&a"));
                    title.setScore(4);

                    String duration = String.format("%02d:%02d:%02d", times[0], times[1], times[2]);
                    Score durationLine = objective.getScore(ChatColor.GRAY + " Duration " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + duration);
                    durationLine.setScore(3);

                    Score latencyLine = objective.getScore(ChatColor.GRAY + " Latency " + ChatColor.DARK_GRAY + "» " + ping.toString());
                    latencyLine.setScore(2);
                    Score emptyLine = objective.getScore("  " + C.t(" "));
                    emptyLine.setScore(1);
                    Score lastLine = objective.getScore(C.t("&7kiul.net &8(" + p.getPing() + "ms)"));
                    lastLine.setScore(0);
                    p.setScoreboard(scoreboard);
            }
        }.runTaskTimer(C.plugin,1,20);
        activeBoard.put(p,runnable);
    }
}
