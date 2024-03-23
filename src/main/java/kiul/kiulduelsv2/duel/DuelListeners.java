package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.Region;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.inventory.InventoryListeners;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class DuelListeners implements Listener {



    @EventHandler
    public void deathUpdateDuel (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && ((Player) e.getEntity()).getHealth() <= e.getFinalDamage() && p.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && p.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
            if (ArenaMethods.findPlayerArena(p) != null) {
                String arenaName = ArenaMethods.findPlayerArena(p);
                if (DuelMethods.playersInMap.get(arenaName).contains(p)) {
                    e.setCancelled(true);
                    UtilMethods.becomeSpectator(p);
                    DuelMethods.playersInMap.get(arenaName).remove(p);
                    DuelMethods.inDuel.remove(p);
                    DuelListeners.duelStatistics.get(p.getUniqueId()).put("dead",true);
                    DuelMethods.inventoryPreview.put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
                    DuelMethods.armourPreview.put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
                    if (DuelMethods.mapTeams.get(arenaName) != null) {
                        for (List<Player> team : DuelMethods.mapTeams.get(arenaName)) {
                            if (team.contains(p)) {
                                team.remove(p);
                            }
                        }
                        try {
                            KitMethods.spectatorKit(p);
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                        List<List<Player>> gameTeams = DuelMethods.mapTeams.get(arenaName);

                        List<Player> team1 = gameTeams.get(0);
                        List<Player> team2 = gameTeams.get(1);


                        // if only one team has players, end the game.
                        if (team2.size() == 0) {
                            // team 1 wins
                            for (Player team1Members : team1) {
                                team1Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                DuelMethods.inventoryPreview.put(team1Members,InventoryToBase64.itemStackArrayToBase64(team1Members.getInventory().getContents()));
                                DuelMethods.armourPreview.put(team1Members,InventoryToBase64.itemStackArrayToBase64(team1Members.getInventory().getArmorContents()));
                                team1Members.playSound(team1Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (DuelMethods.inDuel.contains(team1Members)) {
                                            DuelMethods.inDuel.remove(team1Members);
                                        }
                                        UtilMethods.teleportLobby(team1Members);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                            }

                            for (Player team2Members : team2) {
                                team2Members.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        UtilMethods.teleportLobby(team2Members);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                            ArenaMethods.regenerateArena(arenaName);
                            DuelMethods.mapTeams.remove(arenaName);
                            DuelMethods.playersInMap.remove(arenaName);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                        if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {
                                            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").equals(DuelListeners.duelStatistics.get(team1.get(0).getUniqueId()).get("uuid"))) {
                                                DuelMethods.sendMatchRecap(onlinePlayers, team1.get(0), "SMP");
                                            }
                                        }
                                        if (ArenaMethods.getArenaRegion(arenaName).contains(onlinePlayers.getLocation())) {
                                            UtilMethods.becomeNotSpectator(onlinePlayers);
                                            UtilMethods.teleportLobby(onlinePlayers);

                                        }
                                    }
                                }
                            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            DuelMethods.mapTeams.remove(arenaName);
                        } else if (team1.size() == 0) {
                            for (Player team2Members : team2) {
                                team2Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                DuelMethods.inventoryPreview.put(team2Members,InventoryToBase64.itemStackArrayToBase64(team2Members.getInventory().getContents()));
                                DuelMethods.armourPreview.put(team2Members,InventoryToBase64.itemStackArrayToBase64(team2Members.getInventory().getArmorContents()));
                                team2Members.playSound(team2Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (DuelMethods.inDuel.contains(team2Members)) {
                                            DuelMethods.inDuel.remove(team2Members);
                                        }
                                        UtilMethods.teleportLobby(team2Members);

                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }

                            for (Player team1Members : team1) {
                                team1Members.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        UtilMethods.teleportLobby(team1Members);


                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                            ArenaMethods.regenerateArena(arenaName);
                            DuelMethods.mapTeams.remove(arenaName);
                            DuelMethods.playersInMap.remove(arenaName);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {

                                        if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {

                                            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").equals(DuelListeners.duelStatistics.get(team2.get(0).getUniqueId()).get("uuid")) ) {
                                                DuelMethods.sendMatchRecap(onlinePlayers, team2.get(0), "SMP");
                                            }
                                        }
                                        if (ArenaMethods.getArenaRegion(arenaName).contains(onlinePlayers.getLocation())) {
                                            UtilMethods.becomeNotSpectator(onlinePlayers);
                                            UtilMethods.teleportLobby(onlinePlayers);
                                        }
                                    }
                                }
                            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            DuelMethods.mapTeams.remove(arenaName);
                        }
                    } else {
                        try {
                            KitMethods.spectatorKit(p);
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                        p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                        if (DuelMethods.playersInMap.get(arenaName).size() <=1) {
                            Player victor = DuelMethods.playersInMap.get(arenaName).get(0);
                            victor.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                            victor.playSound(victor, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                            DuelMethods.inventoryPreview.put(victor,InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getContents()));
                            DuelMethods.armourPreview.put(victor,InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getArmorContents()));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    UtilMethods.teleportLobby(victor);
                                }
                            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                            Region region = ArenaMethods.getArenaRegion(arenaName);
                            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                                    if (region.contains(onlinePlayers.getLocation())) {
                                        UtilMethods.becomeNotSpectator(onlinePlayers);
                                        UtilMethods.teleportLobby(onlinePlayers);
                                        if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {

                                            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").toString() == DuelListeners.duelStatistics.get(victor.getUniqueId()).get("uuid")) {
                                                DuelMethods.sendMatchRecap(onlinePlayers, victor, "SMP");
                                            }
                                        }
                                    }

                            }
                            ArenaMethods.regenerateArena(arenaName);
                            DuelMethods.playersInMap.remove(arenaName);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerQuitUpdateDuel (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ArenaMethods.findPlayerArena(p) != null) {
            String arenaName = ArenaMethods.findPlayerArena(p);
            if (DuelMethods.playersInMap.get(arenaName) != null) {
                if (DuelMethods.playersInMap.get(arenaName).contains(p)) {
                    DuelMethods.playersInMap.get(arenaName).remove(p);
                    if (DuelMethods.mapTeams.get(arenaName) != null) {
                        for (List<Player> team : DuelMethods.mapTeams.get(arenaName)) {
                            if (team.contains(p)) {
                                team.remove(p);
                            }
                        }

                        List<List<Player>> gameTeams = DuelMethods.mapTeams.get(arenaName);

                        List<Player> team1 = gameTeams.get(0);
                        List<Player> team2 = gameTeams.get(1);

                        // if only one team has players, end the game.
                        if (team2.size() == 0) {
                            // team 1 wins
                            for (Player team1Members : team1) {
                                team1Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                team1Members.playSound(team1Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        UtilMethods.teleportLobby(team1Members);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                            for (Player team2Members : team2) {
                                team2Members.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        UtilMethods.teleportLobby(team2Members);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                            int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
                            double sideLength = Math.pow(((Arenadata.get().getDouble("arenas." + arenaName + ".size") * 2 - 1) * 16), 2);
                            double maxDistance = (Math.sqrt(sideLength * 2) / 2) + 2;
                            for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), maxDistance, maxDistance, maxDistance)) {
                                if (nearbyEntities instanceof Player spectators) {
                                    UtilMethods.becomeNotSpectator(spectators);
                                    UtilMethods.teleportLobby(spectators);
                                }
                            }
                            ArenaMethods.regenerateArena(arenaName);
                            DuelMethods.mapTeams.remove(arenaName);
                            DuelMethods.playersInMap.remove(arenaName);
                        } else if (team1.size() == 0) {
                            for (Player team2Members : team2) {
                                team2Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                team2Members.playSound(team2Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        UtilMethods.teleportLobby(team2Members);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                            for (Player team1Members : team1) {
                                team1Members.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        UtilMethods.teleportLobby(team1Members);

                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                            int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
                            for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), size, size, size)) {
                                if (nearbyEntities instanceof Player spectators) {
                                    UtilMethods.becomeNotSpectator(spectators);
                                    UtilMethods.teleportLobby(spectators);
                                }
                            }
                            ArenaMethods.regenerateArena(arenaName);
                            DuelMethods.mapTeams.remove(arenaName);
                            DuelMethods.playersInMap.remove(arenaName);
                        }
                    } else {
                        p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                        if (DuelMethods.playersInMap.get(arenaName).size() <= 1) {
                            Player victor = DuelMethods.playersInMap.get(arenaName).get(0);
                            victor.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                            victor.playSound(victor, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    UtilMethods.teleportLobby(victor);
                                }
                            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                            Region region = ArenaMethods.getArenaRegion(arenaName);
                            for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), 200, 200, 200)) {
                                if (nearbyEntities instanceof Player spectators) {
                                    if (region.contains(nearbyEntities.getLocation())) {
                                        UtilMethods.becomeNotSpectator(spectators);
                                        UtilMethods.teleportLobby(spectators);
                                    }
                                }
                            }
                            ArenaMethods.regenerateArena(arenaName);
                            DuelMethods.playersInMap.remove(arenaName);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(DuelMethods.preDuel.contains(e.getPlayer()) || ClickMethods.inEditor.contains(e.getPlayer())) {
            Location to = e.getFrom();
            to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            e.setTo(to);
        }
    }

    @EventHandler
    public void preventTeleportLeave (PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (DuelMethods.inDuel.contains(p) && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),ArenaMethods.findPlayerArena(p))) {
                p.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void preventMoveLeave (PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (DuelMethods.inDuel.contains(p) && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),ArenaMethods.findPlayerArena(p))) {
                p.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                e.setCancelled(true);
            }
        }
    }

    public static HashMap<UUID, Map<String,Object>> duelStatistics = new HashMap<>();

    public static Map<String,Object> createStatsArraylist() {
        UUID duelUUID = UUID.randomUUID();
        Map<String,Object> duelStats = new HashMap<>() {{
           put("hits_dealt",0);
           put("hits_taken",0);
           put("combo",0);
           put("longest_combo",0);
           put("damage_dealt",0);
           put("dead",false);
           put("uuid",duelUUID);


        }};
        return duelStats;
    }

    @EventHandler
    public void hitStatTracker (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player ) {
                Player damaged = (Player) e.getEntity();
                Player damager = (Player) e.getDamager();
                if (DuelMethods.inDuel.contains(damager) && DuelMethods.inDuel.contains(damaged)) {

                    int getDamagerHitsDealt = (int) duelStatistics.get(damager.getUniqueId()).get("hits_dealt");
                    damager.sendMessage(getDamagerHitsDealt + "");

                    int getDamagerCombo = (int) duelStatistics.get(damager.getUniqueId()).get("combo");
                    int getDamagerDamageDealt = (int) duelStatistics.get(damager.getUniqueId()).get("damage_dealt");

                    int getDamagedHitsTaken = (int) duelStatistics.get(damaged.getUniqueId()).get("hits_taken");


                    duelStatistics.get(damager.getUniqueId()).put("hits_dealt", getDamagerHitsDealt + 1);
                    duelStatistics.get(damager.getUniqueId()).put("combo", getDamagerCombo + 1);
                    duelStatistics.get(damager.getUniqueId()).put("damage_dealt", getDamagerDamageDealt + (int) e.getFinalDamage());
                    duelStatistics.get(damaged.getUniqueId()).put("hits_taken", getDamagedHitsTaken + 1);
                    if ((int) duelStatistics.get(damager.getUniqueId()).get("combo") > (int) duelStatistics.get(damager.getUniqueId()).get("longest_combo")) {
                        duelStatistics.get(damager.getUniqueId()).put("longest_combo", duelStatistics.get(damager.getUniqueId()).get("combo"));
                    }
                    duelStatistics.get(damaged.getUniqueId()).put("combo", 0);
                }
            }
        }
    }
}
