package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.duel.DuelListeners;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.KitEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeakPatcher implements Listener {


    @EventHandler
    public void preventLeakOnQuit (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (KitEditor.inEditor.containsKey(p)) {
            KitEditor.inEditor.remove(p);
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
