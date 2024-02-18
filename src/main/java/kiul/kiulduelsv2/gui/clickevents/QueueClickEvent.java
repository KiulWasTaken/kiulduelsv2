package kiul.kiulduelsv2.gui.clickevents;

import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.loadGlobalKit;

public class QueueClickEvent implements Listener {

    public static HashMap<String,ArrayList<Player>> queue = new HashMap<>() {{
        put("SMP",new ArrayList<>());
    }};

    @EventHandler
    public void queueGuiClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("queue")) {
            e.setCancelled(true);
            if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p)) != null) {
                // if (KitMethods.kitMatchesCriteria(String kit,int playerKitSlot,Player p) {
                // proceed
                // } else {
                // "you dickhead!"
                // }

                // FORMAT: Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p))


                if (e.getCurrentItem().getItemMeta().getLocalizedName() != null) {
                    String localName = e.getCurrentItem().getItemMeta().getLocalizedName();
                    ClickMethods.queueAddCheck(queue.get(localName), p, localName);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.4f);
                    try {
                        loadGlobalKit(p, "queue");
                    } catch (IOException err) {
                        err.printStackTrace();
                    }

                } else {
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 0.3f, 0.4f);
                }
            } else {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Selected kit slot (" + KitMethods.kitSlot.get(p) + ") is empty!");
            }
        }
    }
}
