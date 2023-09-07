package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import static kiul.kiulduelsv2.duel.DuelMethods.playersInMap;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.DuelMethods.*;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UtilMethods {

    public static void teleportLobby (Player p) {
        p.teleport(p.getWorld().getSpawnLocation());
        try {KitMethods.lobbyKit(p);} catch (IOException err) {err.printStackTrace();}
    }

    public static void spectatePlayer (Player spectator,Player player) {

    }

    public static void becomeSpectator (Player player) {
        String arenaName = ArenaMethods.findPlayerArena(player);
        List<Player> playersInDuel = playersInMap.get(arenaName);
        for (Player aliveplayers : playersInDuel) {
            aliveplayers.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),player);
        }
    }

    public static void becomeNotSpectator (Player player) {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.showPlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),player);
        }
    }


 }
