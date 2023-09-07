package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class DuelListeners {



    @EventHandler
    public void deathUpdateDuel (PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (ArenaMethods.findPlayerArena(p) != null) {
            String arenaName = ArenaMethods.findPlayerArena(p);
            if (DuelMethods.playersInMap.get(arenaName).contains(p)) {
                UtilMethods.becomeSpectator(p);
                DuelMethods.playersInMap.remove(p);
                try{KitMethods.spectatorKit(p);} catch (IOException er) {er.printStackTrace();}
                ArrayList<Player> team1 = new ArrayList<>();
                ArrayList<Player> team2 = new ArrayList<>();
                ArrayList<Player> team3 = new ArrayList<>();
                ArrayList<Player> ffa = new ArrayList<>();
                for (Player players : DuelMethods.playersInMap.get(arenaName)) {
                    switch (ChatColor.getLastColors(players.getDisplayName())) {
                        case "§c":
                            team1.add(players);
                            break;
                        case "§9":
                            team2.add(players);
                            break;
                        case "§a":
                            team3.add(players);
                            break;
                        case "§f":
                            ffa.add(players);
                            break;
                    }
                }
                if (ffa.size() <= 1) {
                    Player winner = ffa.get(0);
                    winner.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!","");
                    winner.playSound(winner, Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable(){

                        @Override
                        public void run(){
                            UtilMethods.teleportLobby(winner);
                            int size = Arenadata.get().getInt("arenas."+arenaName+".size");
                            for (Entity nearbyEntities :  p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas."+arenaName+".center"),size,size,size)) {
                                if (nearbyEntities instanceof Player spectators) {
                                    UtilMethods.becomeNotSpectator(spectators);
                                    UtilMethods.teleportLobby(spectators);
                                }
                            }
                        }
                    }, 20L);
                
                    


                }
                // if only one team has players, end the game.
                if (team2.size() == 0 && team3.size() == 0)  {
                    // team 1 wins
                    for (Player team1Members : team1) {
                        team1Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                        team1Members.playSound(team1Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {

                            @Override
                            public void run() {
                                UtilMethods.teleportLobby(team1Members);
                                int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
                                for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), size, size, size)) {
                                    if (nearbyEntities instanceof Player spectators) {
                                        UtilMethods.becomeNotSpectator(spectators);
                                        UtilMethods.teleportLobby(spectators);
                                    }
                                }
                            }
                        }, 20L);
                    }
                } else if (team1.size() == 0 && team2.size() == 0) {
                    // team 3 wins
                    for (Player team3Members : team3) {
                        team3Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                        team3Members.playSound(team3Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {

                            @Override
                            public void run() {
                                UtilMethods.teleportLobby(team3Members);
                                int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
                                for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), size, size, size)) {
                                    if (nearbyEntities instanceof Player spectators) {
                                        UtilMethods.becomeNotSpectator(spectators);
                                        UtilMethods.teleportLobby(spectators);
                                    }
                                }
                            }
                        }, 20L);
                    }
                } else if (team1.size() == 0 && team3.size() == 0) {
                    // team 2 wins
                    for (Player team2Members : team1) {
                        team2Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                        team2Members.playSound(team2Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {

                            @Override
                            public void run() {
                                UtilMethods.teleportLobby(team2Members);
                                int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
                                for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), size, size, size)) {
                                    if (nearbyEntities instanceof Player spectators) {
                                        UtilMethods.becomeNotSpectator(spectators);
                                        UtilMethods.teleportLobby(spectators);
                                    }
                                }
                            }
                        }, 20L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(DuelMethods.preDuel.contains(e.getPlayer())) {
            Location to = e.getFrom();
            to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            e.setTo(to);
        }
    }
}
