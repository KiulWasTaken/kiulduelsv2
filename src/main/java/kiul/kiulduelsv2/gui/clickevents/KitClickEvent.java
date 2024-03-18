package kiul.kiulduelsv2.gui.clickevents;

import kiul.kiulduelsv2.gui.KitInventory;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;

public class KitClickEvent implements Listener {

    @EventHandler
    public void onGUIClick (InventoryClickEvent e) {
        Player p = (Player)e.getView().getPlayer();
        if (e.getView().getTitle().equals("Kit Selector")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType() == Material.CYAN_TERRACOTTA) {
                String[] strings = e.getCurrentItem().getItemMeta().getDisplayName().split("7");
                int num = Integer.parseInt(strings[1]);
                KitMethods.kitSlot.put(p,num);
                KitInventory.kitInventory(p);
            } else if (e.getCurrentItem().getType() == Material.LIME_TERRACOTTA) {
                ClickMethods.enterKitEditor(p);
            }
        }
    }

}
