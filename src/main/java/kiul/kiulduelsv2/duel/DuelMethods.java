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
    static HashMap<String, List<Player>> mapTeams = new HashMap<>();

    static ArrayList<Player> preDuel = new ArrayList<>();

    public static void startPartyDuel(ArrayList<Player> players, Player duelOwner, boolean useSameKit, boolean teams, boolean randomTeams, boolean threeTeams, String arenaName) {
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

    public static void startDuel(List<Player> players, boolean arcade, String kit, String arenaName) {
        Random random = new Random();
                List<Player> team1 = players.subList(0, (players.size() / 2));
                List<Player> team2 = players.subList(players.size() / 2, players.size());

                for (Player p : team1) {
                    p.setDisplayName(ChatColor.RED + p.getName());
                    if (arcade) {
                        p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".team1").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                    } else {
                        Location center = Arenadata.get().getLocation("arena." + arenaName + ".center");
                        int y = center.getBlockY();
                        p.teleport(center.add(0,201-y,0));
                    }
                }
                for (Player p : team2) {
                    p.setDisplayName(ChatColor.BLUE + p.getName());
                    if (arcade) {
                        p.teleport(Arenadata.get().getLocation("arena." + arenaName + ".team2").add(random.nextDouble(0, 2), 0, random.nextDouble(0, 2)));
                    } else {
                        Location center = Arenadata.get().getLocation("arena." + arenaName + ".center");
                        int y = center.getBlockY();
                        p.teleport(center.add(0,201-y,0));
                    }
                }
                if (!arcade) {
                    TerrainArena.generateTerrain(players.get(0).getWorld(),Arenadata.get().getLocation("arena." + arenaName + ".corner1"),Arenadata.get().getLocation("arena." + arenaName + ".corner2"),7);
                }
            List<Player> toBePutInMap = new ArrayList<>();

            for (Player p : players) {
                if (arcade) {
                    try {
                        switch (kit) {
                            case "crystal":
                                ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.crystal.inventory")).getContents();
                                ItemStack[] armourContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.crystal.armour")).getContents();
                                p.getInventory().setContents(kitContents);
                                p.getInventory().setArmorContents(armourContents);
                                break;
                            case "smp":
                                ItemStack[] kitContents2 = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.smpcarrots.inventory")).getContents();
                                ItemStack[] armourContents2 = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.smpcarrots.armour")).getContents();
                                p.getInventory().setContents(kitContents2);
                                p.getInventory().setArmorContents(armourContents2);
                                break;
                        }
                        preDuel.add(p);
                        preDuelCountdown(p);
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
                toBePutInMap.add(p);
            }
            playersInMap.put(arenaName,toBePutInMap);
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
