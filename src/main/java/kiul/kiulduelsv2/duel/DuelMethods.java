package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class DuelMethods {

    public static HashMap<String, List<Player>> playersInMap = new HashMap<>();
    static HashMap<String, List<Player>> mapTeams = new HashMap<>();

    static ArrayList<Player> preDuel = new ArrayList<>();

    public static void startDuel(ArrayList<Player> players, Player duelOwner, boolean useSameKit, boolean teams, boolean randomTeams, boolean threeTeams, String arenaName) {
        Random random = new Random();
        if (teams) {
            if (randomTeams) {
                Collections.shuffle(players);
            }
            if (threeTeams) {
                List<Player> team1 = players.subList(0, (players.size() / 3));
                List<Player> team2 = players.subList(players.size() / 3, (players.size() / 3) * 2);
                List<Player> team3 = players.subList((players.size() / 3) * 2, players.size());
                for (Player p : team1) {
                    p.setDisplayName(ChatColor.RED + p.getName());
                    p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".team1").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                    mapTeams.put(arenaName, team1);
                }
                for (Player p : team2) {
                    p.setDisplayName(ChatColor.BLUE + p.getName());
                    p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".team2").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                    mapTeams.put(arenaName, team2);
                }
                for (Player p : team3) {
                    p.setDisplayName(ChatColor.GREEN + p.getName());
                    p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".center").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                    mapTeams.put(arenaName, team3);
                }
            } else {
                List<Player> team1 = players.subList(0, (players.size() / 2));
                List<Player> team2 = players.subList(players.size() / 2, players.size());
                for (Player p : team1) {
                    p.setDisplayName(ChatColor.RED + p.getName());
                    p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".team1").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                }
                for (Player p : team2) {
                    p.setDisplayName(ChatColor.BLUE + p.getName());
                    p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".team2").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                }
            }
            List<Player> toBePutInMap = new ArrayList<>();

            for (Player p : players) {
                if (useSameKit) {
                    try {
                        KitMethods.loadSelectedKitSlot(duelOwner);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                } else {
                    try {
                        KitMethods.loadSelectedKitSlot(p);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
                preDuel.add(p);
                preDuelCountdown(p);
                toBePutInMap.add(p);
            }
            playersInMap.put(arenaName,toBePutInMap);

        } else {
            List<Player> toBePutInMap = new ArrayList<>();
            for (Player p : players) {
                p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".center").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                if (useSameKit) {
                    try {
                        KitMethods.loadSelectedKitSlot(duelOwner);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                } else {
                    try {
                        KitMethods.loadSelectedKitSlot(p);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
                preDuel.add(p);
                preDuelCountdown(p);
                toBePutInMap.add(p);
            }
            playersInMap.put(arenaName,toBePutInMap);
        }
    }


    public static void preDuelCountdown (Player p) {
        Bukkit.getScheduler().runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {
            int time = 10; //or any other number you want to start countdown from

            @Override
            public void run() {
                if (this.time != 0) {
                    p.sendTitle(ChatColor.DARK_AQUA + "" + time, "Ready", 0, 15, 5);
                    if (this.time == 1) {
                        p.sendTitle(ChatColor.DARK_AQUA + "" + time, "Fuck em up!", 0, 15, 5);
                    }
                    this.time--;
                } else {
                    preDuel.remove(p);
                }

            }
        }, 0L, 20L);
    }

    public void playerDeathCheck (Player p) {

    }

    public void endDuel (String arenaName) {
        List<Player> players = playersInMap.get(arenaName);
        playersInMap.remove(arenaName);

    }




}
