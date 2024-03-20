package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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

        Location center = Arenadata.get().getLocation("arenas."+arenaName+".center");
        int size = Arenadata.get().getInt("arenas."+arenaName+".size");
        World world = center.getWorld();

        Chunk SEChunk = world.getChunkAt(center.getChunk().getX()+size,center.getChunk().getZ()+size);
        Chunk NWChunk = world.getChunkAt(center.getChunk().getX()-size,center.getChunk().getZ()-size);


        Location teamOneSpawn = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 64, SEChunk.getZ() << 4).add(8, 0, 8);
        Location teamTwoSpawn = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 64, NWChunk.getZ() << 4).add(8, 0, 8);
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
                preDuel.add(p);
                preDuelCountdown(p);
                inDuel.add(p);
                    p.setSaturation(5);
                    p.setFoodLevel(20);
                    p.setHealth(20);
                    p.setGameMode(GameMode.SURVIVAL);
                    for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                        p.removePotionEffect(potionEffect.getType());
                    }
            } catch (IOException err) {
                err.printStackTrace();
            }

        }

    }

    public static void preDuelCountdown (Player p) {
        int timer = 10;
        Bukkit.getScheduler().runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {
            int time = timer; //or any other number you want to start countdown from

            @Override
            public void run() {
                if (this.time >= 0) {
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

    public static void startRealisticDuel (List<Player> players, String arenaName,boolean reRolled) {
        ArenaMethods.arenasInUse.add(arenaName);
        int size = players.size();

        Location duelCentre = Arenadata.get().getLocation("arenas." + arenaName + ".center");
        Location teleportTo = new Location(duelCentre.getWorld(),duelCentre.getX(),150,duelCentre.getZ());
        Map<String,Object> duelStatistics = DuelListeners.createStatsArraylist();
        for (Player p : players) {
            p.teleport(teleportTo);
            p.setGameMode(GameMode.SPECTATOR);
            p.setAllowFlight(true);
            p.setFlying(true);
            DuelListeners.duelStatistics.put(p,duelStatistics);
        }


        if (ArenaMethods.getSuitableArena() != null && !reRolled) {
            String backupArena = ArenaMethods.getSuitableArena();
            ArenaMethods.arenasInUse.add(backupArena);
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
            allowedToReRoll.put(arenaName, players);
            reRollYes.put(arenaName, new ArrayList<>());
            reRollNo.put(arenaName, new ArrayList<>());
            for (Player p : players) {
                p.setAllowFlight(true);
                p.setFlying(true);
                p.spigot().sendMessage(message.create());
            }
            new BukkitRunnable() {
                int time = 15; //or any other number you want to start countdown from

                @Override
                public void run() {
                    for (Player p : players) {
                        String message = "§7"+time;
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }
                    players.removeIf(Objects::isNull);
                    for (Player p : players) {
                        if (!p.isOnline()) {
                            players.remove(p);
                        }
                    }
                    if (players.size() != size) {
                        for (Player p : players) {
                            UtilMethods.teleportLobby(p);
                        }
                        ArenaMethods.arenasInUse.remove(arenaName);
                        ArenaMethods.arenasInUse.remove(backupArena);
                        cancel();
                        return;
                    }

                    if (this.time <= 0) {
                        cancel();
                        if (reRollYes.get(arenaName).size() > reRollNo.get(arenaName).size()) {
                            for (Player p : players) {
                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Map Re-Rolling!");
                            }
                            reRollNo.remove(arenaName);
                            reRollYes.remove(arenaName);
                            startRealisticDuel(players,backupArena,true);
                            ArenaMethods.regenerateArena(arenaName);
                            return;

                        } else {
                            for (Player p : players) {
                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Game Starting!");
                                p.setFlying(false);
                                p.setAllowFlight(false);
                            }
                            reRollNo.remove(arenaName);
                            reRollYes.remove(arenaName);
                            beginDuel(arenaName,players);
                            ArenaMethods.arenasInUse.remove(backupArena);
                            return;
                        }
                    }

                    if (reRollYes.get(arenaName).size() > reRollNo.get(arenaName).size() && allowedToReRoll.get(arenaName).isEmpty()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Map Re-Rolling!");
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            p.teleport(teleportTo);
                            preDuel.add(p);
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        TerrainArena.generateTerrainPerformant(duelCentre,4);
                        startRealisticDuel(players,backupArena,true);
                        cancel();
                    } else if (reRollYes.get(arenaName).size() < reRollNo.get(arenaName).size() && allowedToReRoll.get(arenaName).isEmpty()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Game Starting!");
                            p.setFlying(false);
                            p.setAllowFlight(false);
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        beginDuel(arenaName, players);
                        ArenaMethods.arenasInUse.remove(backupArena);

                        cancel();
                    }
                    time--;
                }
            }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 20L);
        } else {
            for (Player p : players) {
                p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"No backup maps available, game starting in 10 seconds.");
                p.setAllowFlight(true);
                p.setFlying(true);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    players.removeIf(Objects::isNull);
                    for (Player p : players) {
                        if (!p.isOnline()) {
                            players.remove(p);
                        }
                     }
                    if (players.size() == size) {
                        beginDuel(arenaName, players);
                    } else {
                        for (Player p : players) {
                            UtilMethods.teleportLobby(p);
                            ArenaMethods.arenasInUse.remove(arenaName);
                        }
                    }
                }
            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class),200);
        }
    }


    public static void startPartyDuel (String arenaName,List<Player> players,List<Player> teamOne,List<Player> teamTwo, boolean ffa) {
        Map<String,Object> duelStatistics = DuelListeners.createStatsArraylist();
        for (Player play : players) {
            if (preDuel.contains(play)) {
                preDuel.remove(play);
            }
            play.setGameMode(GameMode.SURVIVAL);
            DuelListeners.duelStatistics.put(play,duelStatistics);
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

        for (int i = y; i > -63; i--) {
            Location newLoc = new Location(location.getWorld(), location.getX(), i, location.getZ());
            if (newLoc.getBlock().getType().toString() != "AIR") {
            return newLoc.getBlock();
            }
        }
        return location.getBlock();
    }


    public static void openStatsGUI (ArrayList<Player> players,Player p) {
        int invSize = 18+(int)Math.ceil(players.size() / 7.0)*9;
        Inventory inventory = Bukkit.createInventory(null,invSize,"Statistics");
        ItemStack item = ItemStackMethods.createItemStack(" ", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null);
        int rowCount = invSize / 9;

        for(int index = 0; index < invSize; index++) {
            int row = index / 9;
            int column = (index % 9) + 1;

            if(row == 0 || row == rowCount-1 || column == 1 || column == 9)
                inventory.setItem(index, item);
        }
        ArrayList<String> lore = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            lore.add("Hits: " + (int)DuelListeners.duelStatistics.get(players.get(i)).get("hits_dealt"));
            lore.add("Hits Taken: " + (int)DuelListeners.duelStatistics.get(players.get(i)).get("hits_taken"));
            lore.add("Damage Dealt: " + (int)DuelListeners.duelStatistics.get(players.get(i)).get("damage_dealt"));
            lore.add("Longest Combo: " + (int)DuelListeners.duelStatistics.get(players.get(i)).get("longest_combo"));

            inventory.addItem(ItemStackMethods.createSkullItem(players.get(i).getDisplayName(),players.get(i),lore));
            lore.clear();
        }



        p.openInventory(inventory);
    }

    public static void sendMatchRecap (Player p, Player winner,String mode) {
        ArrayList<Player> duelMembers = new ArrayList<>();
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (DuelListeners.duelStatistics.get(onlinePlayers) != null) {
                if (DuelListeners.duelStatistics.get(onlinePlayers).get("uuid").toString().equalsIgnoreCase(DuelListeners.duelStatistics.get(p).get("uuid").toString())) {
                    duelMembers.add(onlinePlayers);
                }
            }
        }
            p.sendMessage(C.t("&#50bd4a&m&l-------|&r &#378033&lMatch Recap &#50bd4a&m&l|-------"));
        for (Player player : duelMembers) {
            if (player == winner) {
                String displayName = ChatColor.WHITE+ player.getDisplayName();
                p.sendMessage(C.t("&#50bd4a▸&7 ") + displayName + C.t("&6 (WINNER)"));
            } else {
                String displayName = ChatColor.GRAY+ player.getDisplayName();
                p.sendMessage(C.t("&#50bd4a▸&7 ") + displayName);
            }
        }
            p.sendMessage("");
            TextComponent message = new TextComponent("Click to see more information..");
            message.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            message.setItalic(true);
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recap " + DuelListeners.duelStatistics.get(p).get("uuid").toString()));
            p.spigot().sendMessage(message);
            p.sendMessage(C.t("&#50bd4a&m&l---------------------------"));

    }
}
