package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.ParseMethods;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.KitEditor;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

public class DuelListeners implements Listener {


    @EventHandler (priority = EventPriority.HIGHEST)
    public void deathUpdateDuel(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && ((Player) e.getEntity()).getHealth() <= e.getFinalDamage() && p.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && p.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
            if (ArenaMethods.findPlayerArena(p) != null) {
                String arenaName = ArenaMethods.findPlayerArena(p);
                Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
                if (duel != null) {
                    e.setCancelled(true);
                    DuelMethods.inventoryPreview.put(p, InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
                    DuelMethods.armourPreview.put(p, InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
                    duel.killPlayer(p.getUniqueId());

                    DuelListeners.duelStatistics.get(p.getUniqueId()).put("dead", true);
                    if (p.getKiller() != null) {
                        Userdata.get().set(p.getKiller().getUniqueId() + ".stats.kills", Userdata.get().getInt(p.getKiller().getUniqueId() + ".stats.kills") + 1);
                    }
                    Userdata.get().set(p.getUniqueId() + ".stats.deaths", Userdata.get().getInt(p.getUniqueId() + ".stats.deaths") + 1);


                    // Create a firework effect with the specified colors
                    FireworkEffect effect = FireworkEffect.builder()
                            .with(FireworkEffect.Type.BALL_LARGE) // You can choose different types like STAR, CREEPER, etc.
                            .withColor(Color.LIME)
                            .withColor(Color.WHITE)
                            .build();

                    // Apply the effect to the firework

                    if (!duel.isFfa()) {

                        List<UUID> team1 = duel.getRedTeam();
                        List<UUID> team2 = duel.getBlueTeam();


                        // if only one team has players, end the game.
                        if (team2.size() == 0) {
                            Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK_ROCKET);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.addEffect(effect);

                            // Set the power of the firework
                            meta.setPower(0); // Power 0 means instant explosion

                            // Apply the meta to the firework
                            firework.setFireworkMeta(meta);
                            firework.detonate();
                            if (duel.isRated()) {
                                DuelMethods.updateElo(duel.getBlueTeamMembers(), duel.getRedTeamMembers());
                            }
                            DuelMethods.updateCareer(duel.getBlueTeamMembers(), duel.getRedTeamMembers(), duel.isRated());
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
                                    int getDamageDealt = (int) duelStatistics.get(team1UUIDs).get("damage_dealt");
                                    int getDamageTaken = (int) duelStatistics.get(team1UUIDs).get("damage_taken");
                                    int damageDelta = getDamageDealt - getDamageTaken;
                                    ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(team1UUIDs + ".stats.damagedelta");
                                    damageDeltaPerRound.add(0, damageDelta);
                                    if (damageDeltaPerRound.size() > 10) {
                                        damageDeltaPerRound.remove(10);
                                    }
                                    Userdata.get().set(team1UUIDs + ".stats.damagedelta", damageDeltaPerRound);
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
                                    int losses = Userdata.get().getInt(allUUIDs + ".stats.losses");
                                    Userdata.get().set(allUUIDs + ".stats.losses", losses + 1);
                                    Userdata.get().set(allUUIDs + ".stats.streak", 0);
                                    int getDamageDealt = (int) duelStatistics.get(allUUIDs).get("damage_dealt");
                                    int getDamageTaken = (int) duelStatistics.get(allUUIDs).get("damage_taken");
                                    int damageDelta = getDamageDealt - getDamageTaken;
                                    ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(allUUIDs + ".stats.damagedelta");
                                    damageDeltaPerRound.add(0, damageDelta);
                                    if (damageDeltaPerRound.size() > 10) {
                                        damageDeltaPerRound.remove(10);
                                    }
                                    Userdata.get().set(allUUIDs + ".stats.damagedelta", damageDeltaPerRound);
                                    Userdata.save();
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Recap.sendMatchRecap(onlinePlayers, duel.getAllRedTeamPlayers(), duel.isRated());
                                        UtilMethods.becomeNotSpectator(onlinePlayers);
                                        UtilMethods.teleportLobby(onlinePlayers);
                                        duel.remove(allUUIDs);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                            }


                        } else if (team1.size() == 0) {
                            Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK_ROCKET);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.addEffect(effect);

                            // Set the power of the firework
                            meta.setPower(0); // Power 0 means instant explosion

                            // Apply the meta to the firework
                            firework.setFireworkMeta(meta);
                            firework.detonate();
                            if (duel.isRated()) {
                                DuelMethods.updateElo(duel.getRedTeamMembers(), duel.getBlueTeamMembers());
                            }
                            DuelMethods.updateCareer(duel.getRedTeamMembers(), duel.getBlueTeamMembers(), duel.isRated());
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
                                    int getDamageDealt = (int) duelStatistics.get(team2UUIDs).get("damage_dealt");
                                    int getDamageTaken = (int) duelStatistics.get(team2UUIDs).get("damage_taken");
                                    int damageDelta = getDamageDealt - getDamageTaken;
                                    ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(team2UUIDs + ".stats.damagedelta");
                                    damageDeltaPerRound.add(0, damageDelta);
                                    if (damageDeltaPerRound.size() > 10) {
                                        damageDeltaPerRound.remove(10);
                                    }
                                    Userdata.get().set(team2UUIDs + ".stats.damagedelta", damageDeltaPerRound);
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
                                    int losses = Userdata.get().getInt(allUUIDs + ".stats.losses");
                                    Userdata.get().set(allUUIDs + ".stats.losses", losses + 1);
                                    Userdata.get().set(allUUIDs + ".stats.streak", 0);
                                    int getDamageDealt = (int) duelStatistics.get(allUUIDs).get("damage_dealt");
                                    int getDamageTaken = (int) duelStatistics.get(allUUIDs).get("damage_taken");
                                    int damageDelta = getDamageDealt - getDamageTaken;
                                    ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(allUUIDs + ".stats.damagedelta");
                                    damageDeltaPerRound.add(0, damageDelta);
                                    if (damageDeltaPerRound.size() > 10) {
                                        damageDeltaPerRound.remove(10);
                                    }
                                    Userdata.get().set(allUUIDs + ".stats.damagedelta", damageDeltaPerRound);
                                    Userdata.save();
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Recap.sendMatchRecap(onlinePlayers, duel.getAllBlueTeamPlayers(), duel.isRated());
                                        UtilMethods.becomeNotSpectator(onlinePlayers);
                                        UtilMethods.teleportLobby(onlinePlayers);
                                        duel.remove(allUUIDs);
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
                        int losses = Userdata.get().getInt(p.getUniqueId() + ".stats.losses");
                        Userdata.get().set(p.getUniqueId() + ".stats.losses", losses + 1);
                        Userdata.get().set(p.getUniqueId() + ".stats.streak", 0);
                        if (duel.getPlayers().size() <= 1) {
                            Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK_ROCKET);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.addEffect(effect);

                            // Set the power of the firework
                            meta.setPower(0); // Power 0 means instant explosion

                            // Apply the meta to the firework
                            firework.setFireworkMeta(meta);
                            firework.detonate();
                            Player victor = Bukkit.getPlayer(duel.getPlayers().get(0));
                            victor.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                            int wins = Userdata.get().getInt(victor.getUniqueId() + ".stats.wins");
                            int streak = Userdata.get().getInt(victor.getUniqueId() + ".stats.streak");
                            Userdata.get().set(victor.getUniqueId() + ".stats.wins", wins + 1);
                            Userdata.get().set(victor.getUniqueId() + ".stats.streak", streak + 1);
                            int getDamageDealt = (int) duelStatistics.get(victor.getUniqueId()).get("damage_dealt");
                            int getDamageTaken = (int) duelStatistics.get(victor.getUniqueId()).get("damage_taken");
                            int damageDelta = getDamageDealt - getDamageTaken;
                            ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(victor.getUniqueId() + ".stats.damagedelta");
                            damageDeltaPerRound.add(0, damageDelta);
                            if (damageDeltaPerRound.size() > 10) {
                                damageDeltaPerRound.remove(10);
                            }
                            Userdata.get().set(victor.getUniqueId() + ".stats.damagedelta", damageDeltaPerRound);
                            if (Userdata.get().getInt(victor.getUniqueId() + ".stats.streak") > Userdata.get().getInt(victor.getUniqueId() + ".stats.best_streak")) {
                                Userdata.get().set(victor.getUniqueId() + ".stats.best_streak", Userdata.get().getInt(victor.getUniqueId() + ".stats.streak"));
                            }
                            Userdata.save();
                            victor.playSound(victor, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                            DuelMethods.inventoryPreview.put(victor, InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getContents()));
                            DuelMethods.armourPreview.put(victor, InventoryToBase64.itemStackArrayToBase64(victor.getInventory().getArmorContents()));
                            for (UUID allUUIDs : duel.getAllContained()) {
                                Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                                new BukkitRunnable() {
                                    List<Player> winners = new ArrayList<>() {{
                                        add(victor);
                                    }};

                                    @Override
                                    public void run() {
                                        Recap.sendMatchRecap(onlinePlayers, winners, duel.isRated());
                                        UtilMethods.becomeNotSpectator(onlinePlayers);
                                        UtilMethods.teleportLobby(onlinePlayers);
                                        int getDamageDealt = (int) duelStatistics.get(allUUIDs).get("damage_dealt");
                                        int getDamageTaken = (int) duelStatistics.get(allUUIDs).get("damage_taken");
                                        int damageDelta = getDamageDealt - getDamageTaken;
                                        ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(allUUIDs + ".stats.damagedelta");
                                        damageDeltaPerRound.add(0, damageDelta);
                                        if (damageDeltaPerRound.size() > 10) {
                                            damageDeltaPerRound.remove(10);
                                        }
                                        Userdata.get().set(allUUIDs + ".stats.damagedelta", damageDeltaPerRound);
                                        duel.remove(allUUIDs);
                                    }
                                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);

                            }
                            ArenaMethods.regenerateArena(arenaName);
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
            Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
            if (duel != null) {
                duel.remove(p.getUniqueId());
                if (duelStatistics.get(p.getUniqueId()) != null) {
                    DuelListeners.duelStatistics.get(p.getUniqueId()).put("dead", true);
                }
                if (p.getLastDamageCause() != null) {
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
                                    duel.remove(allUUIDs);
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
                                    duel.remove(allUUIDs);
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
                                for (UUID allUUIDs : duel.getAllContained()) {
                                    Player onlinePlayers = Bukkit.getPlayer(allUUIDs);
                                    if (onlinePlayers != null) {
                                        List<Player> winners = new ArrayList<>() {{
                                            add(victor);
                                        }};
                                        Recap.sendMatchRecap(onlinePlayers, winners, duel.isRated());
                                        UtilMethods.becomeNotSpectator(onlinePlayers);
                                        UtilMethods.teleportLobby(onlinePlayers);
                                    }
                                    duel.remove(allUUIDs);
                                }

                            }
                        }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                        ArenaMethods.regenerateArena(arenaName);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(DuelMethods.preDuel.contains(e.getPlayer()) || KitEditor.inEditor.containsKey(e.getPlayer())) {
            Location to = e.getFrom();
            to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            e.setTo(to);
        }
    }

    List<String> allowedInteractions = new ArrayList<>() {{
        add("HELMET");
        add("CHESTPLATE");
        add("LEGGINGS");
        add("BOOTS");
    }};

    @EventHandler
    public void preventUseItemsInEditor(PlayerInteractEvent e) {
        if(KitEditor.inEditor.containsKey(e.getPlayer())) {
            for (String allowedTypes : allowedInteractions) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType().name().contains(allowedTypes)) {
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventTeleportLeave (PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
        if (duel != null && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),duel.getArena())) {
                p.sendMessage(C.failPrefix +""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
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
                p.sendMessage(C.failPrefix +""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                p.teleport(e.getFrom());
            }
        }
    }

    /**
     * "preventHurtInInactiveArena"
     * When a player is damaged, if they are inside an arena but not in a duel or in a duel's living participants, cancel the event.
     **/
    @EventHandler
    public void preventHurtInInactiveArena (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (ArenaMethods.findPlayerArena(p) != null) {

                if (C.duelManager.findDuelForMember(p.getUniqueId()) == null) {
                    e.setCancelled(true);
                } else {
                    Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
                    if (!duel.getPlayers().contains(p.getUniqueId()) || duel.getPlayers().size() <= 1) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * "announcePlayerDeathsAndResurrections"
     * checks for whenever a player is killed or resurrects using a totem and broadcasts it to all players in the duel.
     * also broadcasts the killer/popper's weapon and method used if applicable.
     **/
    @EventHandler (priority = EventPriority.LOWEST)
    public void announcePlayerDeathsAndResurrections (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Duel duel = C.duelManager.findDuelForMember(((Player) e.getEntity()).getUniqueId());
            if (duel != null) {
                Player p1 = null;
                if (e.getDamager() instanceof ExplosiveMinecart cart) {
                    if (cart.getLastDamageCause() instanceof EntityDamageByEntityEvent lastDamageCause) {
                        if ((lastDamageCause.getDamager().getType() == EntityType.ARROW || lastDamageCause.getDamager().getType() == EntityType.SPECTRAL_ARROW || lastDamageCause.getDamager().getType() == EntityType.PLAYER)) {
                            Player damager = null;
                            if ((lastDamageCause.getDamager() instanceof Projectile arrow)) {
                                damager = (Player) arrow.getShooter();
                            }
                            if (lastDamageCause.getDamager() instanceof Player) {
                                damager = (Player) lastDamageCause.getDamager();
                            }
                            p1 = damager;
                        }
                    }
                }
                if (e.getDamager() instanceof AbstractArrow arrow) {
                    if (arrow.getShooter() instanceof Player) {
                        p1 = (Player) arrow.getShooter();
                    }
                }
                if (e.getDamager() instanceof Player) {
                    p1 = (Player) e.getDamager();
                }

                Player p2 = (Player) e.getEntity();
                if (p1 == null) {
                    return;
                }

                if (p2.getHealth() - e.getFinalDamage() <= 0 && p2.getNoDamageTicks() == 0) {
                    boolean wasResurrected = p2.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || p2.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
                    String outcome = wasResurrected ? "was popped" : "was ";
                    if (!wasResurrected) {
                        if (e.getEntity() instanceof AbstractArrow) {
                            outcome+= "shot by";
                        } else if (e.getEntity() instanceof ExplosiveMinecart) {
                            outcome+= "blown up";
                        } else if (e.getEntity() instanceof Player killer) {
                            outcome+= "slain";
                            switch (killer.getInventory().getItemInMainHand().getType()) {
                                case MACE:
                                    outcome = "was smashed";
                                    break;
                                case NETHERITE_AXE:
                                    outcome = "was beheaded";
                                    break;
                                case TRIDENT:
                                    outcome = "was impaled";
                                    break;
                            }
                        }
                    }

                    String killer;
                    if (e.getDamager() instanceof Player) {
                        killer = "";
                    } else {
                        String typeName = e.getDamager().getType().name();
                        String[] words = typeName.split("_");
                        for (int i = 0; i < words.length; i++) {
                            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
                        }
                        String entityName = words[0]+" "+words[1];
                        String prefix = "";
                        String suffix = "";
                        if (e.getEntity() instanceof AbstractArrow) {
                            suffix = " from ";
                        } else if (e.getEntity() instanceof ExplosiveMinecart) {
                            prefix = "a ";
                            suffix = " shot by ";
                        }

                        killer = prefix+entityName+suffix;
                    }
                    Component finalPrefixComponent = Component.empty();
                    Component finalItemComponent = Component.empty();
                    ItemStack item = p1.getInventory().getItemInMainHand();
                    if (item.hasItemMeta() && item.getType() != Material.AIR) {
                            ItemMeta itemMeta = item.getItemMeta();
                        String displayName = itemMeta.getDisplayName();
                        if (displayName.isEmpty()) {
                            if (item.getMaxStackSize() > 1) {
                                displayName = item.getItemMeta().getItemName() + " x" + item.getAmount();
                            } else {

                                displayName = item.getItemMeta().getItemName();
                                if (item.getItemMeta().getItemName().isEmpty()) {
                                    displayName = item.getType().name().toLowerCase();
                                    displayName.replaceAll("\\_"," ");
                                }
                            }
                        }
                        Component prefix = MiniMessage.miniMessage().deserialize("<#e33630>⚔ ");
                        if (wasResurrected) {
                             prefix = MiniMessage.miniMessage().deserialize("<#ebbc3d>⚔ ");
                        }
                        Component name = MiniMessage.miniMessage().deserialize(ParseMethods.parseLegacyHexToMiniMessage(displayName));
                        Component itemPreviewComponent = Component.empty().append(Component.text("using ")).append(Component.text("[").color(NamedTextColor.AQUA).append(name).append(Component.text("]").color(NamedTextColor.AQUA)));
                        finalItemComponent = Component.empty().append(itemPreviewComponent).hoverEvent(item.asHoverEvent(UnaryOperator.identity()));
                        finalPrefixComponent = prefix;
                    }


                    Component deathMessage = Component.empty().append(finalPrefixComponent).append(Component.text(p2.getName()+" "+outcome+" by " + killer + p1.getName() + " ").append(finalItemComponent));

                    // send the compiled death message to all participants
                    for (UUID allContainedUUIDs : duel.getAllContained()) {
                        Player duelMember = Bukkit.getPlayer(allContainedUUIDs);
                        if (duelMember != null) {
                            duelMember.sendMessage(deathMessage);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEnderPearlHit(ProjectileHitEvent event) {
        // Check if the projectile is an Ender Pearl
        if (event.getEntity() instanceof EnderPearl) {
            EnderPearl enderPearl = (EnderPearl) event.getEntity();

            // Check if the entity who threw the Ender Pearl is a player
            if (enderPearl.getShooter() instanceof Player) {
                Player p = (Player) enderPearl.getShooter();
                if (C.duelManager.findDuelForMember(p.getUniqueId()) != null) {
                    Vector center = ArenaMethods.getArenaRegion(C.duelManager.findDuelForMember(p.getUniqueId()).getArena()).getCenter();

                    Location hitLocation = event.getEntity().getLocation();
                    Location blockHitLocation = event.getHitBlock() != null ? event.getHitBlock().getLocation() : null;

                    // Check if it hit a barrier block
                    if (blockHitLocation != null && blockHitLocation.getBlock().getType() == Material.BARRIER) {
                        // Cancel the event so the pearl doesn't do the default behavior
                        event.setCancelled(true);

                        // Calculate the vector to move the pearl toward the center of the arena
                        // Move the Ender Pearl back a couple of blocks towards the center of the arena
                        double distanceToMove = 1.0; // Adjust this for how far you want to move it
                        Vector directionToCenter = center.subtract(hitLocation.toVector()).normalize().multiply(distanceToMove);

                        // Calculate the new teleport location
                        Location newLocation = hitLocation.add(directionToCenter);

                        // Ensure the Ender Pearl stays above the ground (optional, depending on how you want it to land)
                        if (newLocation.getBlock().getType() == Material.AIR) {
                            // If the location is air, move it to the nearest solid block beneath it
                            while (newLocation.getBlock().getType() == Material.AIR && newLocation.getBlockY() > 0) {
                                newLocation.subtract(0, 1, 0); // Move down one block at a time
                            }
                        }

                        // Teleport the Ender Pearl to the new location
                        enderPearl.teleport(newLocation.add(0,1,0));

                        // Optional: Adjust the velocity to make it fall more naturally instead of shooting downwards
                        enderPearl.setVelocity(enderPearl.getVelocity().multiply(0.8).setY(-0.5)); // Lower Y velocity, adjusted for falling
                    }
                }
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
           put("damage_taken",0);
           put("dead",false);
           put("uuid",duelUUID);


        }};
        return duelStats;
    }

    @EventHandler
    public void hitStatTracker (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p1 = null;
            if (e.getDamager() instanceof ExplosiveMinecart cart) {
                if (cart.getLastDamageCause() instanceof EntityDamageByEntityEvent lastDamageCause) {
                    if ((lastDamageCause.getDamager().getType() == EntityType.ARROW || lastDamageCause.getDamager().getType() == EntityType.SPECTRAL_ARROW || lastDamageCause.getDamager().getType() == EntityType.PLAYER)) {
                        Player damager = null;
                        if ((lastDamageCause.getDamager() instanceof Projectile arrow)) {
                            damager = (Player) arrow.getShooter();
                        }
                        if (lastDamageCause.getDamager() instanceof Player) {
                            damager = (Player) lastDamageCause.getDamager();
                        }
                        p1 = damager;
                    }
                }
            }
            if (e.getDamager() instanceof AbstractArrow arrow) {
                if (arrow.getShooter() instanceof Player) {
                    p1 = (Player) arrow.getShooter();
                }
            }
            if (e.getDamager() instanceof Player) {
                p1 = (Player) e.getDamager();
            }

            Player p2 = (Player) e.getEntity();
            if (p1 == null) {
                return;
            }

            Player damaged = p2;
            Player damager = p1;
            Duel damagedDuel = C.duelManager.findDuelForMember(damaged.getUniqueId());
            Duel damagerDuel = C.duelManager.findDuelForMember(damager.getUniqueId());
            if (damagerDuel != null && damagedDuel != null) {

                int getDamagerHitsDealt = (int) duelStatistics.get(damager.getUniqueId()).get("hits_dealt");

                int getDamagerCombo = (int) duelStatistics.get(damager.getUniqueId()).get("combo");
                int getDamagerDamageDealt = (int) duelStatistics.get(damager.getUniqueId()).get("damage_dealt");

                int getDamagedHitsTaken = (int) duelStatistics.get(damaged.getUniqueId()).get("hits_taken");
                int getDamagedDamageTaken = (int) duelStatistics.get(damaged.getUniqueId()).get("damage_taken");


                duelStatistics.get(damager.getUniqueId()).put("hits_dealt", getDamagerHitsDealt + 1);
                duelStatistics.get(damager.getUniqueId()).put("combo", getDamagerCombo + 1);
                duelStatistics.get(damager.getUniqueId()).put("damage_dealt", getDamagerDamageDealt + (int) e.getFinalDamage());
                duelStatistics.get(damaged.getUniqueId()).put("hits_taken", getDamagedHitsTaken + 1);
                duelStatistics.get(damaged.getUniqueId()).put("damage_taken", getDamagedDamageTaken + (int) e.getFinalDamage());
                if ((int) duelStatistics.get(damager.getUniqueId()).get("combo") > (int) duelStatistics.get(damager.getUniqueId()).get("longest_combo")) {
                    duelStatistics.get(damager.getUniqueId()).put("longest_combo", duelStatistics.get(damager.getUniqueId()).get("combo"));
                }
                duelStatistics.get(damaged.getUniqueId()).put("combo", 0);
                if (damaged.getHealth() <= e.getFinalDamage() && damaged.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && damaged.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
                    int kills = Userdata.get().getInt(damager.getUniqueId() + ".stats.kills");
                    Userdata.get().set(damager.getUniqueId() + ".stats.kills", kills + 1);
                    Userdata.save();
                }
            }
        }
    }

    @EventHandler
    public void spectatorPreventHurt (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player spectator) {
            Duel duel = C.duelManager.findDuelForMember(spectator.getUniqueId());
            if (duel != null) {
                if (duel.getSpectators().contains(spectator.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
