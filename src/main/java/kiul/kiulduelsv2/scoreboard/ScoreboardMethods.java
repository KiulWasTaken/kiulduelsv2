package kiul.kiulduelsv2.scoreboard;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.UserPreferences;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.party.Party;
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

import static org.bukkit.Bukkit.getServer;

public class ScoreboardMethods {

    public static DecimalFormat df = new DecimalFormat("#.##");
    public static HashMap<Player,BukkitTask> activeBoard = new HashMap<>();


    public static void startLobbyScoreboardTask(Player p) {
        if (!UserPreferences.get().getBoolean(p.getUniqueId()+".scoreboard")) {return;}
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

    public static void startDuelSidebar(Player p, List<Player> duelMembers, String rating, long duelStartTime,String duelType,boolean isPartyGame,boolean isFfa) {
        if (activeBoard.get(p) != null) {
            activeBoard.get(p).cancel();
            activeBoard.remove(p);
        }
        if (p.getScoreboard().equals(p.getServer().getScoreboardManager().getMainScoreboard())) {p.setScoreboard(getServer().getScoreboardManager().getNewScoreboard());} //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
        Scoreboard scoreboard = p.getScoreboard(); //Personalized scoreboard
        Party party = C.partyManager.findPartyForMember(p.getUniqueId());

        /* enemy/teammate identification for sidebar */

        List<Player> teammates = new ArrayList<>();
        List<Player> enemies = new ArrayList<>();
        if (!isPartyGame) {
            for (Player duelMember : duelMembers) {
                if (party != null) {
                    if (party.isMember(duelMember.getUniqueId())) {
                        teammates.add(duelMember);
                    } else {
                        enemies.add(duelMember);
                    }
                } else {
                    teammates.add(p);
                    enemies.add(duelMember);
                }
            }
        } else {
            if (isFfa) {
                List<Player> partyPlayers = party.teamOnePlayers();
                partyPlayers.addAll(party.teamTwoPlayers());
                partyPlayers.remove(p);
                teammates.add(p);
                enemies.addAll(partyPlayers);
            } else {
                enemies.addAll(party.getEnemyTeamPlayers(p.getUniqueId()));
                teammates.addAll(party.getTeamPlayers(p.getUniqueId()));
            }
        }

        /* team colour logic */

        Team redTeamOne = scoreboard.registerNewTeam("red");
        redTeamOne.setPrefix(ChatColor.RED+""+ChatColor.BOLD+"[RED] "+ChatColor.RESET+ChatColor.RED+"");


        Team blueTeamTwo = scoreboard.registerNewTeam("blue");
        blueTeamTwo.setPrefix(ChatColor.BLUE+""+ChatColor.BOLD+"[BLUE] "+ChatColor.RESET+ChatColor.BLUE+"");

        if (isPartyGame) {
            if (isFfa) {
                for (Player enemy : enemies) {
                    redTeamOne.addEntry(enemy.getName());
                }
                blueTeamTwo.addEntry(p.getName());
            } else {
                for (Player teamOnePlayer : party.teamOnePlayers()) {
                    redTeamOne.addEntry(teamOnePlayer.getName());
                }
                for (Player teamTwoPlayer : party.teamTwoPlayers()) {
                    blueTeamTwo.addEntry(teamTwoPlayer.getName());
                }
            }
        } else {
            for (int i = 0; i < duelMembers.size(); i++) {
                if (i >= duelMembers.size()/2) {
                    redTeamOne.addEntry(duelMembers.get(i).getName());
                } else {
                    blueTeamTwo.addEntry(duelMembers.get(i).getName());
                }
            }
        }


        int myTeamElo = 0;
        for (Player teammate : teammates) {
            int elo = (int) DuelsDB.readPlayer(teammate.getUniqueId(), "stat_elo_"+duelType.toLowerCase());
            myTeamElo += elo;
        }
        int enemyTeamElo = 0;
        for (Player enemy : enemies) {
            int elo = (int) DuelsDB.readPlayer(enemy.getUniqueId(), "stat_elo_"+duelType.toLowerCase());
            enemyTeamElo += elo;
        }
        enemyTeamElo = enemyTeamElo/enemies.size();
        myTeamElo = myTeamElo/teammates.size();
        double expected = 1.0 / (1.0 + Math.pow(10.0, (enemyTeamElo - myTeamElo) / 400.0));

        final BukkitTask runnable = new BukkitRunnable() {

            @Override
            public void run() {

                    int[] times = C.splitTimestampSince(duelStartTime);

                    StringBuilder ping = new StringBuilder();
                    for (int i = 0; i < duelMembers.size(); i++) {
                        Player member = duelMembers.get(i);
                        if (member == p || (C.partyManager.findPartyForMember(p.getUniqueId()) != null && C.partyManager.findPartyForMember(p.getUniqueId()).isMember(member.getUniqueId()))) {

                            ping.insert(0,ChatColor.GREEN+""+member.getPing()+"ms " + ChatColor.WHITE+"- ");
                        } else {
                            ping.append(ChatColor.RED).append(member.getPing()+"ms ");
                            if (i != duelMembers.size() - 1) {
                                ping.append(ChatColor.WHITE).append("- ");
                            }
                        }

                    }
                    if (duelMembers.size() > 2) {
                        ping = new StringBuilder();
                        ping.append(ChatColor.GREEN+""+p.getPing()+"ms ");
                    }



                    Objective objective = scoreboard.getObjective(p.getName()) == null ? scoreboard.registerNewObjective(p.getName(), "dummy") : scoreboard.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.



                    objective.setDisplayName(C.t(" &#256A05&lK&#2C6E06&lI&#347307&lU&#3B7707&lL&#427B08&l.&#4A7F09&lN&#51840A&lE&#58880B&lT &#608E0C&lP&#67950D&lR&#6E9C0E&lA&#76A310&lC&#7DAA11&lT&#84B112&lI&#8CB814&lC&#93BF15&lE "));
//                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//                    Team team = scoreboard.registerNewTeam("test");
//                    team.setSuffix();
//                    team.addEntry(ChatColor.translateAlternateColorCodes('&', "&a"));

                    // Add lines to the sideba

                    String duration = String.format("%02d:%02d:%02d", times[0], times[1], times[2]);

                    replaceScore(objective,6," ");
                    replaceScore(objective,5,C.t("&#5c944eIn Match &f-&7 " + rating));
                    replaceScore(objective,4,ChatColor.GRAY + " Duration " + ChatColor.DARK_GRAY + "» " + ChatColor.WHITE + duration);
                    replaceScore(objective,3,ChatColor.GRAY + " Latency " + ChatColor.DARK_GRAY + "» " + ping.toString());
                    replaceScore(objective,2,ChatColor.GRAY + " Win% " + ChatColor.DARK_GRAY + "» " + ChatColor.GREEN+(int)(expected*100) + "%"+ChatColor.DARK_GRAY+" / "+ChatColor.RED+""+(100-(int)(expected*100))+"%");
                    replaceScore(objective,1,"  " +C.t(" "));
                    replaceScore(objective,0,C.t("&7kiul.net &8(" + p.getPing() + "ms)"));

                    if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {objective.setDisplaySlot(DisplaySlot.SIDEBAR);} //Vital functionality
                    p.setScoreboard(scoreboard); //Vital functionality
            }
        }.runTaskTimer(C.plugin,1,20);
        activeBoard.put(p,runnable);
    }


    public static String getEntryFromScore(Objective o, int score) {
        if(o == null) return null;
        if(!hasScoreTaken(o, score)) return null;
        for (String s : o.getScoreboard().getEntries()) {
            if(o.getScore(s).getScore() == score) return o.getScore(s).getEntry();
        }
        return null;
    }

    public static boolean hasScoreTaken(Objective o, int score) {
        for (String s : o.getScoreboard().getEntries()) {
            if(o.getScore(s).getScore() == score) return true;
        }
        return false;
    }

    public static void replaceScore(Objective o, int score, String name) {
        if(hasScoreTaken(o, score)) {
            if(getEntryFromScore(o, score).equalsIgnoreCase(name)) return;
            if(!(getEntryFromScore(o, score).equalsIgnoreCase(name))) o.getScoreboard().resetScores(getEntryFromScore(o, score));
        }
        o.getScore(name).setScore(score);
    }

    public void showScoreboard(Player p) {
        if (p.getScoreboard().equals(p.getServer().getScoreboardManager().getMainScoreboard())) {p.setScoreboard(getServer().getScoreboardManager().getNewScoreboard());} //Per-player scoreboard, not necessary if all the same data, but we're personalizing the displayname and all
        Scoreboard score = p.getScoreboard(); //Personalized scoreboard
        Objective objective = score.getObjective(p.getName()) == null ? score.registerNewObjective(p.getName(), "dummy") : score.getObjective(p.getName()); //Per-player objectives, even though it doesn't matter what it's called since we're using per-player scoreboards.
        String displayName = "Welcome, " + p.getName() + "!";
        objective.setDisplayName("\u00A7d\u00A7l" + displayName);
        replaceScore(objective, 8, "\u00A7a\u00A7lPlayers:");
        replaceScore(objective, 7, "\u00A7f\u00A7l" + getServer().getOnlinePlayers().size());
        replaceScore(objective, 6, "\u00A7d" + Math.random());
        replaceScore(objective, 5, "\u00A7e" + Math.random());
        if (objective.getDisplaySlot() != DisplaySlot.SIDEBAR) {objective.setDisplaySlot(DisplaySlot.SIDEBAR);} //Vital functionality
        p.setScoreboard(score); //Vital functionality
    }
}
