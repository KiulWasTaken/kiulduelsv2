package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
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
    public static HashMap<String, List<List<Player>>> mapTeams = new HashMap<>();

    static ArrayList<Player> preDuel = new ArrayList<>();

    public static ArrayList<Player> inDuel = new ArrayList<>();

    public static void beginDuel (String arenaName,List<Player> players) {
        for (Player play : players) {
            if (preDuel.contains(play)) {
                preDuel.remove(play);
            }
            play.setGameMode(GameMode.SURVIVAL);
        }
        Location teamOneSpawn = Arenadata.get().getLocation("arenas."+arenaName+".southeast");
        Location teamTwoSpawn = Arenadata.get().getLocation("arenas."+arenaName+".northwest");
        playersInMap.put(arenaName,players);
        List<Player> teamOne = new ArrayList<>();
        List<Player> teamTwo = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            if (i >= players.size()/2) {
                teamTwo.add(players.get(i));
            } else {
                teamOne.add(players.get(i));
            }
        }


        List<List<Player>> arenaTeams = new ArrayList<>() {{
            add(teamOne);
            add(teamTwo);
        }};
        mapTeams.put(arenaName,arenaTeams);
        for (Player p : teamOne) {
            p.teleport(getHighestBlockBelow(199,teamOneSpawn).getLocation());
        }
        for (Player p : teamTwo) {
            p.teleport(getHighestBlockBelow(199,teamTwoSpawn).getLocation());
        }
        for (Player p : players) {
            try {
                KitMethods.loadSelectedKitSlot(p);
            } catch (IOException err) {
                err.printStackTrace();
            }
            preDuel.add(p);
            preDuelCountdown(p);
            inDuel.add(p);
        }

    }

    public static void startClassicDuel (String arenaName,String kitName,ArrayList<Player> players) {

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
            inDuel.add(p);
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

    public static void startRealisticDuel (List<Player> players, String arenaName) {
        ArenaMethods.arenasInUse.add(arenaName);
        for (Player player : players) {
            player.sendMessage(arenaName);
        }

        Location duelCentre = Arenadata.get().getLocation("arenas." + arenaName + ".center");
        Location teleportTo = new Location(duelCentre.getWorld(),duelCentre.getX(),150,duelCentre.getZ());
        for (Player p : players) {
            p.teleport(teleportTo);
            p.setGameMode(GameMode.SPECTATOR);
        }
        // Create a clickable message with two components
        ComponentBuilder message = new ComponentBuilder("Re-Roll this randomly generated map? ")
                .color(net.md_5.bungee.api.ChatColor.GRAY).color(net.md_5.bungee.api.ChatColor.ITALIC);

        // Add the first clickable component
        message.append("[✓]")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .bold(true)
                .underlined(true)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reroll yes"));

        // Add a separator
        message.append(" ");

        // Add the second clickable component
        message.append("[❌]")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .bold(true)
                .underlined(true)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reroll no"));

        // Send the message
        allowedToReRoll.put(arenaName,players);
        reRollYes.put(arenaName,new ArrayList<>());
        reRollNo.put(arenaName,new ArrayList<>());
        for (Player p : players) {
            p.spigot().sendMessage(message.create());
        }
         new BukkitRunnable() {
            int time = 20; //or any other number you want to start countdown from

            @Override
            public void run() {
                if (this.time == 5) {
                    for (Player p : players) {
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting ends in 5 seconds");
                    }
                }
                if (this.time <= 0) {
                    if (reRollYes.size() > reRollNo.size()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Map Re-Rolling!");
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        TerrainArena.generateTerrain(players.get(0).getWorld(),Arenadata.get().getLocation("arena." + arenaName + ".center"),4,null,players);
                    } else {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Game Starting!");
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        beginDuel(arenaName,players);
                    }
                    cancel();
                    return;
                }

                    if (reRollYes.size() > reRollNo.size()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Map Re-Rolling!");
                            p.teleport(teleportTo);
                            preDuel.add(p);
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        TerrainArena.generateTerrain(players.get(0).getWorld(),Arenadata.get().getLocation("arena." + arenaName + ".center"),4,null,null);
                        cancel();
                    } else {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Game Starting!");
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        beginDuel(arenaName,players);
                        cancel();
                    }
                this.time--;
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 20L);
    }


    public static void startPartyDuel (String arenaName,List<Player> players,List<Player> teamOne,List<Player> teamTwo, boolean ffa) {
        for (Player play : players) {
            if (preDuel.contains(play)) {
                preDuel.remove(play);
            }
            play.setGameMode(GameMode.SURVIVAL);
        }
        Location teamOneSpawn = Arenadata.get().getLocation("arenas."+arenaName+".southeast");
        Location teamTwoSpawn = Arenadata.get().getLocation("arenas."+arenaName+".northwest");
        playersInMap.put(arenaName,players);
        if (!ffa) {
            List<List<Player>> arenaTeams = new ArrayList<>() {{
                add(teamOne);
                add(teamTwo);
            }};

            mapTeams.put(arenaName, arenaTeams);

            for (Player p : teamOne) {
                p.teleport(getHighestBlockBelow(199,teamOneSpawn).getLocation());
            }
            for (Player p : teamTwo) {
                p.teleport(getHighestBlockBelow(199,teamTwoSpawn).getLocation());
            }
        } else {
            Location spawn = Arenadata.get().getLocation("arenas."+arenaName+".center");
            for (Player p : players) {
                p.teleport(getHighestBlockBelow(199,spawn).getLocation());
            }
        }
        for (Player p : players) {
            try {
                KitMethods.loadSelectedKitSlot(p);
            } catch (IOException err) {
                err.printStackTrace();
            }
            preDuel.add(p);
            preDuelCountdown(p);
            inDuel.add(p);
        }

    }

    public static Block getHighestBlockBelow (int y, Location location) {

        for (int i = y; i < -63; i--) {
            Location newLoc = new Location(location.getWorld(), location.getX(), i, location.getZ());
            if (newLoc.getBlock().getType().toString() != "AIR") {
            return newLoc.getBlock();
            }
        }
        return location.getBlock();
    }
    
}
