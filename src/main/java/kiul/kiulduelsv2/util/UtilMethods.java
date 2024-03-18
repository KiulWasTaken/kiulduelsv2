package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;

import static kiul.kiulduelsv2.duel.DuelMethods.inDuel;
import static kiul.kiulduelsv2.duel.DuelMethods.playersInMap;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.DuelMethods.*;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UtilMethods {

    public static void teleportLobby (Player p) {
        if (inDuel.contains(p)) {
            inDuel.remove(p);
        }
        p.teleport(p.getWorld().getSpawnLocation());
        p.setDisplayName(ChatColor.WHITE + p.getName());
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setGameMode(GameMode.SURVIVAL);
        try {KitMethods.lobbyKit(p);} catch (IOException err) {err.printStackTrace();}
    }

    public static void spectatePlayer (Player player,Player spectating) {
        player.teleport(spectating.getLocation());
        String arenaName = ArenaMethods.findPlayerArena(player);
        List<Player> playersInDuel = playersInMap.get(arenaName);
        for (Player alivePlayers : playersInDuel) {
            alivePlayers.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),player);
        }
        inDuel.add(player);
    }

    public static void becomeSpectator (Player player) {
        String arenaName = ArenaMethods.findPlayerArena(player);
        List<Player> playersInDuel = playersInMap.get(arenaName);
        for (Player alivePlayers : playersInDuel) {
            alivePlayers.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),player);
        }
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
    }

    public static void becomeNotSpectator (Player player) {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.showPlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),player);
        }
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setFlying(false);
        player.setAllowFlight(false);
    }


 }
