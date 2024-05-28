package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class LeakPatcher implements Listener {


    @EventHandler
    public void preventLeakonQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ClickMethods.inEditor.contains(p)) {
            ClickMethods.inEditor.remove(p);
        }
    }
}
