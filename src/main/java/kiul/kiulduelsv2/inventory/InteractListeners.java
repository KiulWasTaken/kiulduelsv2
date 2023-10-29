package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.gui.QueueInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;

public class InteractListeners {

    @EventHandler
    public void hotbarInteractListener (PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            switch (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName()) {
                case "queue":
                    e.setCancelled(true);
                    QueueInventory.queueInventory(e.getPlayer());
                    break;
                case "leavequeue":
                    e.setCancelled(true);
                    try {
                        KitMethods.lobbyKit(e.getPlayer());
                    } catch (IOException r) {r.printStackTrace();}
                    break;

            }
        }
    }
}
