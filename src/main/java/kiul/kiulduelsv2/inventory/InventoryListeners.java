package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;

public class InventoryListeners implements Listener {

    @EventHandler
    public void setSlotonPlayerJoin (PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            Userdata.get().set("selected-slot." + e.getPlayer().getUniqueId(), 1);
            Userdata.save();
            kitSlot.put(e.getPlayer(),1);
        } else {
            kitSlot.put(e.getPlayer(),(int)Userdata.get().get("selected-slot." + e.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void saveSlotonPlayerLeave (PlayerQuitEvent e) {
        Userdata.get().set("selected-slot." + e.getPlayer().getUniqueId(), kitSlot.get(e.getPlayer()));
        Userdata.save();
    }
}
