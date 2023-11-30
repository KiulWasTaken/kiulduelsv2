package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class LeakPatcher implements Listener {


    @EventHandler
    public void preventLeakonQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ArenaMethods.findPlayerArena(p) != null) {
            String arenaName = ArenaMethods.findPlayerArena(p);
            if (DuelMethods.playersInMap.get(arenaName).contains(p)) {
                DuelMethods.playersInMap.remove(p);
                for (List<Player> team : DuelMethods.mapTeams.get(arenaName)) {
                    if (team.contains(p)) {
                        team.remove(p);
                    }
                }
            }
        }
    }
}
