package kiul.kiulduelsv2.gui.clickevents;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class QueueClickEvent implements Listener {

    @EventHandler
    public void queueGuiClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("queue")) {
                switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "ArcadeSMP":

                        break;


            }
        }
    }
}
