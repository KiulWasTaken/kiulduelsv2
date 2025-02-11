package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;

import kiul.kiulduelsv2.duel.Duel;
import kiul.kiulduelsv2.duel.DuelManager;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.DuelMethods.*;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.scoreboard.ScoreboardMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UtilMethods {

    public static void teleportLobby (Player p) {
        if (C.duelManager.findDuelForMember(p.getUniqueId()) != null) {
            C.duelManager.findDuelForMember(p.getUniqueId()).remove(p.getUniqueId());
        }
        World mainWorld = Bukkit.getWorld("world");
        p.teleport(mainWorld.getSpawnLocation());
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(5);
        p.setGameMode(GameMode.ADVENTURE);
        p.clearActivePotionEffects();
        ScoreboardMethods.startLobbyScoreboardTask(p);
        try {KitMethods.lobbyKit(p);} catch (IOException err) {err.printStackTrace();}
    }

    public static void spectatePlayer (Player player,Player spectating) {
        Duel duel = C.duelManager.findDuelForMember(spectating.getUniqueId());
        if (duel != null) {
            player.teleport(spectating.getLocation());
            List<UUID> playersInDuel = duel.getPlayers();
            for (UUID alivePlayerUUIDs : playersInDuel) {
                Bukkit.getPlayer(alivePlayerUUIDs).hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), player);
            }
            duel.addSpectator(player.getUniqueId());
            List<Player> allDuelMembers = duel.getAllRedTeamPlayers();
            allDuelMembers.addAll(duel.getAllBlueTeamPlayers());
            boolean isPartyGame = C.partyManager.findPartyForMember(spectating.getUniqueId()).getMembers().contains(duel.getRedTeam().get(0)) && C.partyManager.findPartyForMember(spectating.getUniqueId()).getMembers().contains(duel.getBlueTeam().get(0));
            ScoreboardMethods.startDuelSidebar(player,allDuelMembers,"Spectating",duel.getStartTime(),duel.getKitType(),isPartyGame,duel.isFfa());
            becomeSpectator(player);
        } else {
            player.sendMessage(C.failPrefix + spectating.getName() + " is not in a duel right now");
        }
    }

    public static void becomeSpectator (Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setGameMode(GameMode.ADVENTURE);
        try {KitMethods.spectatorKit(player);}catch (IOException err) {err.printStackTrace();}
    }

    public static void becomeNotSpectator (Player player) {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.showPlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),player);
        }
        if (C.duelManager.findDuelForMember(player.getUniqueId()) != null) {
            C.duelManager.findDuelForMember(player.getUniqueId()).remove(player.getUniqueId());
        }
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setFlying(false);
        player.setAllowFlight(false);

        try {KitMethods.lobbyKit(player);} catch (IOException err) {err.printStackTrace();}
    }


 }
