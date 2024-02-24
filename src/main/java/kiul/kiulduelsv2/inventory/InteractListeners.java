package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.KitInventory;
import kiul.kiulduelsv2.gui.QueueInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.gui.clickevents.QueueClickEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.loadGlobalKit;

public class InteractListeners implements Listener {

    @EventHandler
    public void hotbarInteractListener (PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (ClickMethods.inEditor.contains(e.getPlayer())) {
            if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                e.setCancelled(true);
                ItemInventory.itemInventory(p);
                return;
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                EnchantInventory.itemEnchantInventory(p);
                return;
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName() != null) {
                switch (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLocalizedName()) {
                    case "queue":
                        e.setCancelled(true);
                        if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p)) != null) {
                            // if (KitMethods.kitMatchesCriteria(String kit,int playerKitSlot,Player p) {
                            // proceed
                            // } else {
                            // "you dickhead!"
                            // }

                            // FORMAT: Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p))


                            if (p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName() != null) {
                                String localName = p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
                                ClickMethods.queueAddCheck(QueueClickEvent.queue.get(localName), p, localName);
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
                            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Selected kit slot (" + KitMethods.kitSlot.get(p) + ") is empty!");
                        }
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
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (ClickMethods.inEditor.contains(p)) {
            event.getItemDrop().remove();
        }
    }
}
