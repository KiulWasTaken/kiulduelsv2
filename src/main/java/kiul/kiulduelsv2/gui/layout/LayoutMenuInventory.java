package kiul.kiulduelsv2.gui.layout;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static kiul.kiulduelsv2.gui.KitEditor.inEditor;
import static kiul.kiulduelsv2.gui.layout.ItemEditInventory.*;

public class LayoutMenuInventory implements Listener {



    public static void open(Player p) {

        p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1F, 0.8F);

        Inventory inventory = Bukkit.createInventory(p, 27, "Edit Kit");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null, null));
            if (i >= 27) {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.LIME_STAINED_GLASS_PANE, 1, emptylore, null, null, null));
            }
        }


        if (p.getInventory().getItemInMainHand().getType() != Material.AIR && KitMethods.savedItemsArrayContains(p,p.getInventory().getItemInMainHand()) ) {
            inventory.setItem(10, C.createItemStack(C.t(LayoutMenuEnum.ERASE_ITEM.getDisplayName()), LayoutMenuEnum.ERASE_ITEM.getMaterial(), 1, LayoutMenuEnum.ERASE_ITEM.getLore(), null, null, LayoutMenuEnum.ERASE_ITEM.getlocalName(), null));
        } else {
            inventory.setItem(10, C.createItemStack(C.t(LayoutMenuEnum.SAVE_ITEM.getDisplayName()), LayoutMenuEnum.SAVE_ITEM.getMaterial(), 1, LayoutMenuEnum.SAVE_ITEM.getLore(), null, null, LayoutMenuEnum.SAVE_ITEM.getlocalName(), null));
        }

        for (LayoutMenuEnum item : LayoutMenuEnum.values()) {
            if (!item.getlocalName().contains("_item")) {
                inventory.setItem(item.getInventorySlot(), C.createItemStack(item.getDisplayName(), item.getMaterial(), 1, item.getLore(), null, null, item.getlocalName(), null));
            }
            }
        p.openInventory(inventory);

    }

    @EventHandler
    public void layoutMenuInventoryClickListener (InventoryClickEvent e) {
        Player p = (Player) e.getView().getPlayer();
        ItemStack clickedItem = e.getCurrentItem();
        if (e.getView().getTitle().equalsIgnoreCase("Edit Kit")) {
            e.setCancelled(true);
            if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin, "local"))) {
                String localName = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING);
                switch (localName) {
                    case "save":
                        if (e.getClick() == ClickType.LEFT) {
                            p.performCommand("save");
                        }
                        if (e.getClick() == ClickType.RIGHT) {
                            p.performCommand("cancel");
                        }
                        p.closeInventory();
                        break;
                    case "trim":
                        if (ItemEditInventory.isArmor(p.getInventory().getItemInMainHand())) {
                            currentItem.put(p, p.getInventory().getItemInMainHand());
                            trim(p, 1, Material.LIME_STAINED_GLASS_PANE);
                        } else {
                            p.sendMessage(C.failPrefix +"You cannot trim an item that is not armour!");
                        }
                        break;
                    case "item":
                        ItemInventory.itemInventory(p);
                        break;
                    case "rename":
                        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            takeTextFromNextChat.add(p);
                            p.closeInventory();
                            p.sendMessage(ChatColor.GRAY + "Send the name of your item as a chat message.");
                        } else {
                            p.sendMessage(C.failPrefix +"You cannot do this without an item in your hand!");
                        }
                        break;
                    case "enchant":
                        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {


                        ItemEditInventory.open(p,p.getInventory().getItemInMainHand());
                        } else {
                            p.sendMessage(C.failPrefix +"You cannot do this without an item in your hand!");
                        }
                        break;
                    case "save_item":
                        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            KitMethods.saveItemToSavedItemsArray(p, currentItem.get(p));
                            p.playSound(p, Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, 0.7f, 1f);
                            open(p);
                        } else {
                            p.sendMessage(C.failPrefix +"You cannot do this without an item in your hand!");
                        }
                        break;
                    case "erase_item":
                        if (p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            KitMethods.eraseItemFromSavedItemsArray(p, currentItem.get(p));
                            p.playSound(p, Sound.UI_STONECUTTER_TAKE_RESULT, 0.7f, 1f);
                            open(p);
                        } else {
                            p.sendMessage(C.failPrefix +"You cannot do this without an item in your hand!");
                        }
                        break;
                }
            }
        }
    }

    @EventHandler
    public void openInventoryOnCrouchInEditor (PlayerToggleSneakEvent e) {
        if (inEditor.containsKey(e.getPlayer())) {
            open(e.getPlayer());
        }
    }
}
