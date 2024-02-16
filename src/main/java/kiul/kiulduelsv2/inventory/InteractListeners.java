package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.KitInventory;
import kiul.kiulduelsv2.gui.QueueInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.IOException;

public class InteractListeners implements Listener {

    @EventHandler
    public void hotbarInteractListener (PlayerInteractEvent e) {
        if (ClickMethods.inEditor.contains(e.getPlayer())) {
            Player p = e.getPlayer();
            if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                e.setCancelled(true);
                ItemInventory.itemInventory(p);
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                EnchantInventory.itemEnchantInventory(p);
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName() != null) {
                switch (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName()) {
                    case "queue":
                        e.setCancelled(true);
                        QueueInventory.queueInventory(e.getPlayer());
                        break;
                    case "kiteditor":
                        e.setCancelled(true);
                        KitInventory.kitInventory(e.getPlayer());
                        break;
                    case "leavequeue":
                        e.setCancelled(true);
                        try {
                            KitMethods.lobbyKit(e.getPlayer());
                        } catch (IOException r) {
                            r.printStackTrace();
                        }
                        break;

                }
            }
        }
    }
}
