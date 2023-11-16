package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class DuelMethods {

    public static HashMap<String,List<Player>> allowedToReRoll = new HashMap<>();

    public static HashMap<String,List<Boolean>> reRollYes = new HashMap<>();
    public static HashMap<String,List<Boolean>> reRollNo = new HashMap<>();

    public static HashMap<String, List<Player>> playersInMap = new HashMap<>();
    static HashMap<String, List<List<Player>>> mapTeams = new HashMap<>();

    static ArrayList<Player> preDuel = new ArrayList<>();

    // haha now you need to redo it idiot
    public static void startArcadeDuel (String arenaName,String kitName,ArrayList<Player> players) {
        playersInMap.put(arenaName,players);
        List<Player> teamOne = players.subList(0,players.size()/2);
       List<Player> teamTwo = players.subList(players.size()/2,players.size());
       List<List<Player>> arenaTeams = new ArrayList<>() {{
           add(teamOne);
           add(teamTwo);
        }};
       mapTeams.put(arenaName,arenaTeams);

       for (Player p : teamOne) {
           p.setDisplayName(ChatColor.RED+p.getDisplayName());
           p.teleport(Arenadata.get().getLocation("arenas."+arenaName+"team1"));
       }
        for (Player p : teamTwo) {
            p.setDisplayName(ChatColor.BLUE+p.getDisplayName());
            p.teleport(Arenadata.get().getLocation("arenas."+arenaName+"team2"));
        }
        for (Player p : players) {
            try {
                KitMethods.loadGlobalKit(p, kitName);
            } catch (IOException err) {
                err.printStackTrace();
            }
            preDuel.add(p);
            preDuelCountdown(p);
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
                        p.sendTitle(ChatColor.DARK_AQUA + "" + time, "Fight!", 0, 15, 5);
                    }
                    this.time--;
                } else {
                    preDuel.remove(p);
                }

            }
        }, 0L, 20L);
    }


    public void endDuel (String arenaName) {
        List<Player> players = playersInMap.get(arenaName);
        playersInMap.remove(arenaName);
        mapTeams.remove(arenaName);


    }

    public static void promptReRoll (List<Player> players, String arenaName) {
        // Create a clickable message with two components
        ComponentBuilder message = new ComponentBuilder("Re-Roll this randomly generated map? ")
                .color(net.md_5.bungee.api.ChatColor.YELLOW);

        // Add the first clickable component
        message.append("[✓]")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true)
                .underlined(true)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "reroll yes"));

        // Add a separator
        message.append(" ");

        // Add the second clickable component
        message.append("[❌]")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .bold(true)
                .underlined(true)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "reroll no"));

        // Send the message
        allowedToReRoll.put(arenaName,players);
        for (Player p : players) {
            p.spigot().sendMessage(message.create());
        }
         new BukkitRunnable() {
            int time = 10; //or any other number you want to start countdown from

            @Override
            public void run() {
                if (allowedToReRoll.get(arenaName).isEmpty()) {
                    if (reRollYes.size() > reRollNo.size()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.YELLOW + "Voting Complete! Map Re-Rolling!");
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        TerrainArena.generateTerrain(players.get(0).getWorld(),Arenadata.get().getLocation("arena." + arenaName + ".corner1"),Arenadata.get().getLocation("arena." + arenaName + ".corner2"),7);
                    } else {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.YELLOW + "Voting Complete! Game Starting!");
                            reRollNo.remove(arenaName);
                            reRollYes.remove(arenaName);
                        }
                    }
                    cancel();
                    return;
                }
                if (this.time == 5) {
                    for (Player p : players) {
                        p.sendMessage(ChatColor.YELLOW + "Voting ends in 5 seconds");
                    }
                }
                if (this.time <= 0) {
                    if (reRollYes.size() > reRollNo.size()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.YELLOW + "Voting Complete! Map Re-Rolling!");
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        TerrainArena.generateTerrain(players.get(0).getWorld(),Arenadata.get().getLocation("arena." + arenaName + ".corner1"),Arenadata.get().getLocation("arena." + arenaName + ".corner2"),7);
                    } else {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.YELLOW + "Voting Complete! Game Starting!");
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                    }
                    cancel();
                    return;
                }
                this.time--;
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 20L);
    }




}
