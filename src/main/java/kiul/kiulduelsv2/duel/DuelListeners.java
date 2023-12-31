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
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DuelListeners implements Listener {



    @EventHandler
    public void deathUpdateDuel (PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (ArenaMethods.findPlayerArena(p) != null) {
            String arenaName = ArenaMethods.findPlayerArena(p);
            if (DuelMethods.playersInMap.get(arenaName).contains(p)) {
                UtilMethods.becomeSpectator(p);
                DuelMethods.playersInMap.remove(arenaName,p);
                for (List<Player> team : DuelMethods.mapTeams.get(arenaName)) {
                    if (team.contains(p)) {
                        team.remove(p);
                    }
                }
                try{KitMethods.spectatorKit(p);} catch (IOException er) {er.printStackTrace();}
                List<List<Player>> gameTeams = DuelMethods.mapTeams.get(p);

                List<Player> team1 = gameTeams.get(0);
                List<Player> team2 = gameTeams.get(1);


                // if only one team has players, end the game.
                if (team2.size() == 0)  {
                    // team 1 wins
                    for (Player team1Members : team1) {
                        team1Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                        team1Members.playSound(team1Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {

                            @Override
                            public void run() {
                                UtilMethods.teleportLobby(team1Members);
                            }
                        }, 20L);
                    }
                    int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
                    for (Entity nearbyEntities : p.getWorld().getNearbyEntities(Arenadata.get().getLocation("arenas." + arenaName + ".center"), size, size, size)) {
                        if (nearbyEntities instanceof Player spectators) {
                            UtilMethods.becomeNotSpectator(spectators);
                            UtilMethods.teleportLobby(spectators);
                        }
                    }
                    ArenaMethods.regenerateArena(arenaName);

                } else {
                    for (Player team2Members : team2) {
                        team2Members.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                        team2Members.playSound(team2Members, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), new Runnable() {

                            @Override
                            public void run() {
                                UtilMethods.teleportLobby(team2Members);
                            }
                        }, 20L);
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
