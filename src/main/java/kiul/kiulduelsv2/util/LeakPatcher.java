package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.duel.Duel;
import kiul.kiulduelsv2.duel.DuelListeners;
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
    public void preventLeakOnQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ClickMethods.inEditor.contains(p)) {
            ClickMethods.inEditor.remove(p);
        }
        if (DuelListeners.duelStatistics.containsKey(p.getUniqueId())) {
            DuelListeners.duelStatistics.remove(p.getUniqueId());
        }
        if (DuelMethods.inventoryPreview.containsKey(p)) {
            DuelMethods.inventoryPreview.remove(p);
            DuelMethods.armourPreview.remove(p);
        }
        if (DuelMethods.preDuel.contains(p)) {
            DuelMethods.preDuel.remove(p);
        }
    }
}
