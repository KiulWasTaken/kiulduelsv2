package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.Region;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.inventory.InventoryListeners;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
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
                Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
                if (duel != null) {
                    e.setCancelled(true);
                    DuelMethods.inventoryPreview.put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
                    DuelMethods.armourPreview.put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
                    UtilMethods.becomeSpectator(p);
                    duel.killPlayer(p.getUniqueId());
                    DuelListeners.duelStatistics.get(p.getUniqueId()).put("dead",true);
                    if (p.getKiller() != null) {
                        Userdata.get().set(p.getKiller().getUniqueId() + ".stats.kills", Userdata.get().getInt(p.getKiller().getUniqueId() + ".stats.kills") + 1);
                    }
                    Userdata.get().set(p.getUniqueId()+".stats.deaths",Userdata.get().getInt(p.getUniqueId()+".stats.deaths")+1);

                    if (!duel.isFfa()) {

                        List<UUID> team1 = duel.getRedTeam();
                        List<UUID> team2 = duel.getBlueTeam();


                        // if only one team has players, end the game.
                        if (team2.size() == 0) {
                            if (duel.isRated()) {
                                DuelMethods.updateElo(duel.getBlueTeamMembers(), duel.getRedTeamMembers());
                            }
                            DuelMethods.updateCareer(duel.getBlueTeamMembers(),duel.getRedTeamMembers(),duel.isRated());
                            // team 1 wins
                            for (UUID team1UUIDs : duel.getRedTeam()) {
                                if (Bukkit.getPlayer(team1UUIDs) != null) {
                                    Player team1Members = Bukkit.getPlayer(team1UUIDs);
                                    team1Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                    DuelMethods.inventoryPreview.put(team1Members, InventoryToBase64.itemStackArrayToBase64(team1Members.getInventory().getContents()));
                                    DuelMethods.armourPreview.put(team1Members, InventoryToBase64.itemStackArrayToBase64(team1Members.getInventory().getArmorContents()));
                                    int wins = Userdata.get().getInt(team1UUIDs + ".stats.wins");
                                    int streak = Userdata.get().getInt(team1UUIDs + ".stats.streak");
                                    Userdata.get().set(team1UUIDs + ".stats.wins", wins + 1);
                                    Userdata.get().set(team1UUIDs + ".stats.streak", streak + 1);
                                    if (Userdata.get().getInt(team1UUIDs + ".stats.streak") > Userdata.get().getInt(team1UUIDs + ".stats.best_streak")) {
                                        Userdata.get().set(team1UUIDs + ".stats.best_streak", Userdata.get().getInt(team1UUIDs + ".stats.streak"));
                                    }
                                    team1Members.playSound(team1Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            duel.remove(team1Members.getUniqueId());
                                            UtilMethods.teleportLobby(team1Members);
                                        }
                                    }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 41);
                                } else {
                                    duel.remove(team1UUIDs);
                                }
                            }
                            ArenaMethods.regenerateArena(arenaName);

                                    for (UUID allUUIDs : duel.getAllContained()) {
                                        Player onlinePlayers = Bukkit.getPlayer(allUUIDs);

                                                if (duel.getBlueTeamMembers().contains(allUUIDs)) {
                                                    onlinePlayers.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                                    int losses = Userdata.get().getInt(allUUIDs+".stats.losses");
                                                    Userdata.get().set(allUUIDs+".stats.losses",losses+1);
                                                    Userdata.get().set(allUUIDs+".stats.streak",0);
                                                    Userdata.save();
                                                }
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                Recap.sendMatchRecap(onlinePlayers, duel.getAllRedTeamPlayers(), duel.isRated());
                                                UtilMethods.becomeNotSpectator(onlinePlayers);
                                                UtilMethods.teleportLobby(onlinePlayers);
                                            }
                                        }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                                    }


                        } else if (team1.size() == 0) {
                            if (duel.isRated()) {
                                DuelMethods.updateElo(duel.getRedTeamMembers(), duel.getBlueTeamMembers());
                            }
                            DuelMethods.updateCareer(duel.getBlueTeamMembers(),duel.getRedTeamMembers(),duel.isRated());
                            for (UUID team2UUIDs : team2) {
                                if (Bukkit.getPlayer(team2UUIDs) != null) {
                                    Player team2Members = Bukkit.getPlayer(team2UUIDs);
                                    team2Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                    DuelMethods.inventoryPreview.put(team2Members, InventoryToBase64.itemStackArrayToBase64(team2Members.getInventory().getContents()));
                                    DuelMethods.armourPreview.put(team2Members, InventoryToBase64.itemStackArrayToBase64(team2Members.getInventory().getArmorContents()));
                                    int wins = Userdata.get().getInt(team2UUIDs + ".stats.wins");
                                    int streak = Userdata.get().getInt(team2UUIDs + ".stats.streak");
                                    Userdata.get().set(team2UUIDs + ".stats.wins", wins + 1);
                                    Userdata.get().set(team2UUIDs + ".stats.streak", streak + 1);
                                    if (Userdata.get().getInt(team2UUIDs + ".stats.streak") > Userdata.get().getInt(team2UUIDs + ".stats.best_streak")) {
                                        Userdata.get().set(team2UUIDs + ".stats.best_streak", Userdata.get().getInt(team2UUIDs + ".stats.streak"));
                                    }
                                    Userdata.save();
                                    team2Members.playSound(team2Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            duel.remove(team2Members.getUniqueId());
                                            UtilMethods.teleportLobby(team2Members);

                                        }
                                    }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 41);
                                }
                            }
                            ArenaMethods.regenerateArena(arenaName);

                                    for (UUID allUUIDs : duel.getAllContained()) {
                                        Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                                        if (duel.getRedTeamMembers().contains(allUUIDs)) {
                                            onlinePlayers.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                            int losses = Userdata.get().getInt(allUUIDs+".stats.losses");
                                            Userdata.get().set(allUUIDs+".stats.losses",losses+1);
                                            Userdata.get().set(allUUIDs+".stats.streak",0);
                                            Userdata.save();
                                        }
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                            Recap.sendMatchRecap(onlinePlayers, duel.getAllBlueTeamPlayers(), duel.isRated());
                                            UtilMethods.becomeNotSpectator(onlinePlayers);
                                            UtilMethods.teleportLobby(onlinePlayers);

                                            }
                                        }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                                    }

                        }
                    } else {
                        try {
                            KitMethods.spectatorKit(p);
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                        p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                        int losses = Userdata.get().getInt(p.getUniqueId()+".stats.losses");
                        Userdata.get().set(p.getUniqueId()+".stats.losses",losses+1);
                        Userdata.get().set(p.getUniqueId()+".stats.streak",0);
                        if (duel.getPlayers().size() <= 1) {
                            Player victor = Bukkit.getPlayer(duel.getPlayers().get(0));
                            victor.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                            int wins = Userdata.get().getInt(victor.getUniqueId()+".stats.wins");
                            int streak = Userdata.get().getInt(victor.getUniqueId()+".stats.streak");
                            Userdata.get().set(victor.getUniqueId()+".stats.wins",wins+1);
                            Userdata.get().set(victor.getUniqueId()+".stats.streak",streak+1);
                            if (Userdata.get().getInt(victor.getUniqueId()+".stats.streak") > Userdata.get().getInt(victor.getUniqueId()+".stats.best_streak")) {
                                Userdata.get().set(victor.getUniqueId()+".stats.best_streak",Userdata.get().getInt(victor.getUniqueId()+".stats.streak"));
                            }
                            Userdata.save();
                            victor.playSound(victor, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                            DuelMethods.inventoryPreview.put(victor,InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getContents()));
                            DuelMethods.armourPreview.put(victor,InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getArmorContents()));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    UtilMethods.teleportLobby(victor);
                                }
                            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                            for (UUID allUUIDs : duel.getAllContained()) {
                                Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                                UtilMethods.becomeNotSpectator(onlinePlayers);
                                UtilMethods.teleportLobby(onlinePlayers);

                                    if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {
                                        if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").toString() == DuelListeners.duelStatistics.get(victor.getUniqueId()).get("uuid")) {
                                            List<Player> winners = new ArrayList<>() {{add(victor);}};
                                            Recap.sendMatchRecap(onlinePlayers,winners, duel.isRated());
                                        }
                                    }
                                }
                            }
                        ArenaMethods.regenerateArena(arenaName);
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
            Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
            if (duel != null) {
                duel.remove(p.getUniqueId());
                DuelListeners.duelStatistics.get(p.getUniqueId()).put("dead",true);
                if (p.getLastDamageCause().getEntity() != null) {
                    if (p.getLastDamageCause().getEntity() instanceof Player) {
                        Userdata.get().set(p.getLastDamageCause().getEntity().getUniqueId()+".stats.kills",Userdata.get().getInt((p.getLastDamageCause().getEntity().getUniqueId()+".stats.kills"))+1);
                    }
                }

                Userdata.get().set(p.getUniqueId()+".stats.deaths",Userdata.get().getInt(p.getUniqueId()+".stats.deaths")+1);
                DuelMethods.inventoryPreview.put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
                DuelMethods.armourPreview.put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
                if (!duel.isFfa()) {

                    List<UUID> team1 = duel.getRedTeam();
                    List<UUID> team2 = duel.getBlueTeam();


                    // if only one team has players, end the game.
                    if (team2.size() == 0) {
                        // team 1 wins
                        for (UUID team1UUIDs : duel.getRedTeam()) {
                            if (Bukkit.getPlayer(team1UUIDs) != null) {
                                Player team1Members = Bukkit.getPlayer(team1UUIDs);
                                team1Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                DuelMethods.inventoryPreview.put(team1Members, InventoryToBase64.itemStackArrayToBase64(team1Members.getInventory().getContents()));
                                DuelMethods.armourPreview.put(team1Members, InventoryToBase64.itemStackArrayToBase64(team1Members.getInventory().getArmorContents()));
                                int wins = Userdata.get().getInt(team1Members.getUniqueId() + ".stats.wins");
                                int streak = Userdata.get().getInt(team1Members.getUniqueId() + ".stats.streak");
                                Userdata.get().set(team1Members.getUniqueId() + ".stats.wins", wins + 1);
                                Userdata.get().set(team1Members.getUniqueId() + ".stats.streak", streak + 1);
                                if (Userdata.get().getInt(team1Members.getUniqueId() + ".stats.streak") > Userdata.get().getInt(team1Members.getUniqueId() + ".stats.best_streak")) {
                                    Userdata.get().set(team1Members.getUniqueId() + ".stats.best_streak", Userdata.get().getInt(team1Members.getUniqueId() + ".stats.streak"));
                                }
                                team1Members.playSound(team1Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        duel.remove(team1Members.getUniqueId());
                                        UtilMethods.teleportLobby(team1Members);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            } else {
                                duel.remove(team1UUIDs);
                            }
                        }
                        ArenaMethods.regenerateArena(arenaName);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (UUID allUUIDs : duel.getAllContained()) {
                                    Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                                    Recap.sendMatchRecap(onlinePlayers, duel.getAllBlueTeamPlayers(), duel.isRated());
                                    if (duel.getRedTeamMembers().contains(allUUIDs)) {
                                        onlinePlayers.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                        int losses = Userdata.get().getInt(onlinePlayers.getUniqueId() + ".stats.losses");
                                        Userdata.get().set(onlinePlayers.getUniqueId() + ".stats.losses", losses + 1);
                                        Userdata.get().set(onlinePlayers.getUniqueId() + ".stats.streak", 0);
                                    }
                                    UtilMethods.becomeNotSpectator(onlinePlayers);
                                    UtilMethods.teleportLobby(onlinePlayers);
                                }
                            }
                        }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                        Userdata.save();
                    } else if (team1.size() == 0) {
                        for (UUID team2UUIDs : team2) {
                            if (Bukkit.getPlayer(team2UUIDs) != null) {
                                Player team2Members = Bukkit.getPlayer(team2UUIDs);
                                team2Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                                DuelMethods.inventoryPreview.put(team2Members, InventoryToBase64.itemStackArrayToBase64(team2Members.getInventory().getContents()));
                                DuelMethods.armourPreview.put(team2Members, InventoryToBase64.itemStackArrayToBase64(team2Members.getInventory().getArmorContents()));
                                int wins = Userdata.get().getInt(team2Members.getUniqueId() + ".stats.wins");
                                int streak = Userdata.get().getInt(team2Members.getUniqueId() + ".stats.streak");
                                Userdata.get().set(team2Members.getUniqueId() + ".stats.wins", wins + 1);
                                Userdata.get().set(team2Members.getUniqueId() + ".stats.streak", streak + 1);
                                if (Userdata.get().getInt(team2Members.getUniqueId() + ".stats.streak") > Userdata.get().getInt(team2Members.getUniqueId() + ".stats.best_streak")) {
                                    Userdata.get().set(team2Members.getUniqueId() + ".stats.best_streak", Userdata.get().getInt(team2Members.getUniqueId() + ".stats.streak"));
                                }
                                Userdata.save();
                                team2Members.playSound(team2Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        duel.remove(team2Members.getUniqueId());
                                        UtilMethods.teleportLobby(team2Members);

                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }
                        }
                        ArenaMethods.regenerateArena(arenaName);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (UUID allUUIDs : duel.getAllContained()) {
                                    Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                                    Recap.sendMatchRecap(onlinePlayers, duel.getAllBlueTeamPlayers(), duel.isRated());
                                    if (duel.getRedTeamMembers().contains(allUUIDs)) {
                                        onlinePlayers.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                                        int losses = Userdata.get().getInt(onlinePlayers.getUniqueId() + ".stats.losses");
                                        Userdata.get().set(onlinePlayers.getUniqueId() + ".stats.losses", losses + 1);
                                        Userdata.get().set(onlinePlayers.getUniqueId() + ".stats.streak", 0);
                                    }
                                    UtilMethods.becomeNotSpectator(onlinePlayers);
                                    UtilMethods.teleportLobby(onlinePlayers);
                                }
                            }
                        }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                        Userdata.save();
                    }
                } else {
                    try {
                        KitMethods.spectatorKit(p);
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                    int losses = Userdata.get().getInt(p.getUniqueId() + ".stats.losses");
                    Userdata.get().set(p.getUniqueId() + ".stats.losses", losses + 1);
                    Userdata.get().set(p.getUniqueId() + ".stats.streak", 0);
                    if (duel.getPlayers().size() <= 1) {
                        Player victor = Bukkit.getPlayer(duel.getPlayers().get(0));
                        victor.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                        int wins = Userdata.get().getInt(victor.getUniqueId() + ".stats.wins");
                        int streak = Userdata.get().getInt(victor.getUniqueId() + ".stats.streak");
                        Userdata.get().set(victor.getUniqueId() + ".stats.wins", wins + 1);
                        Userdata.get().set(victor.getUniqueId() + ".stats.streak", streak + 1);
                        if (Userdata.get().getInt(victor.getUniqueId() + ".stats.streak") > Userdata.get().getInt(victor.getUniqueId() + ".stats.best_streak")) {
                            Userdata.get().set(victor.getUniqueId() + ".stats.best_streak", Userdata.get().getInt(victor.getUniqueId() + ".stats.streak"));
                        }
                        Userdata.save();
                        victor.playSound(victor, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        DuelMethods.inventoryPreview.put(victor, InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getContents()));
                        DuelMethods.armourPreview.put(victor, InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getArmorContents()));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                UtilMethods.teleportLobby(victor);
                            }
                        }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                        for (UUID allUUIDs : duel.getAllContained()) {
                            Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                            UtilMethods.becomeNotSpectator(onlinePlayers);
                            UtilMethods.teleportLobby(onlinePlayers);
                            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {
                                if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").toString() == DuelListeners.duelStatistics.get(victor.getUniqueId()).get("uuid")) {
                                    List<Player> winners = new ArrayList<>() {{add(victor);}};
                                    Recap.sendMatchRecap(onlinePlayers, winners, duel.isRated());
                                }
                            }
                        }
                    }
                    ArenaMethods.regenerateArena(arenaName);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(DuelMethods.preDuel.contains(e.getPlayer()) || ClickMethods.inEditor.containsKey(e.getPlayer())) {
            Location to = e.getFrom();
            to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            e.setTo(to);
        }
    }

    @EventHandler
    public void preventTeleportLeave (PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
        if (duel != null && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),duel.getArena())) {
                p.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void preventMoveLeave (PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
        if (duel != null && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),duel.getArena())) {
                p.sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                p.teleport(e.getFrom());
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
                Duel damagedDuel = C.duelManager.findDuelForMember(damaged.getUniqueId());
                Duel damagerDuel = C.duelManager.findDuelForMember(damager.getUniqueId());
                if (damagerDuel != null && damagedDuel != null) {

                    int getDamagerHitsDealt = (int) duelStatistics.get(damager.getUniqueId()).get("hits_dealt");

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
                    if (damaged.getHealth() <= e.getFinalDamage() && damaged.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && damaged.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
                        int kills = Userdata.get().getInt(damager.getUniqueId()+".stats.kills");
                        Userdata.get().set(damager.getUniqueId()+".stats.kills",kills+1);
                        Userdata.save();
                    }
                }
            }
        }
    }
}
