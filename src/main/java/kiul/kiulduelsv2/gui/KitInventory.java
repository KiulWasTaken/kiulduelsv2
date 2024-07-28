package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class KitInventory implements Listener {



    public static void kitInventory(Player p) {
        Inventory inventory = Bukkit.createInventory(p, 45, "Kit Selector");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
        }


        for (KitEnum item : KitEnum.values()) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }
                String itemName = ItemStackMethods.translateHexColorCodes("&#","",item.getDisplayName());

                if (item.getMaterial().name().contains("TERRACOTTA")) {
                    lore.add(ChatColor.GOLD + "⏵ " + ChatColor.GRAY + "Click to Select");
                    inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                    List<String> types = Queue.queueTypesLowercase();
                    for (String type : types) {
                        String[] strings = item.getlocalName().split("-");
                        String matchStr = strings[2].toLowerCase();
                        int matchInt = Integer.parseInt(strings[1]);
                        if (type.equalsIgnoreCase(matchStr) && matchInt == KitMethods.kitSlot.get(p).get(type)) {
                            lore.clear();
                            lore.add(ChatColor.GOLD + "⏵ " + ChatColor.GRAY + "Click to Edit");
                            inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(C.t(item.getActiveHex()+KitMethods.kitSlot.get(p).get(type).toString()), item.getActiveMaterial(), 1, lore, null, null, item.getlocalName()));
                        }
                    }


                } else {


                    if (item.getRequiredPermission() == null) {
                        inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                    } else {
                     if (p.hasPermission(item.getRequiredPermission())) {
                         inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                     } else {
                         inventory.setItem(item.getInventorySlot(),ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
                     }
                    }
                }
        }

        p.openInventory(inventory);

    }


    @EventHandler
    public void onGUIClick (InventoryClickEvent e) {
        Player p = (Player)e.getView().getPlayer();
        if (e.getView().getTitle().equals("Kit Selector")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                int num = Integer.parseInt(name);
                String[] strings = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING).split("-");
                String type = strings[strings.length-1].toLowerCase();
                KitMethods.kitSlot.get(p).put(type,num);
                KitInventory.kitInventory(p);
                p.playSound(p,Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,1f,1f);
            }
            if (e.getCurrentItem().getItemMeta().getLore().get(0).trim().equals(ChatColor.GOLD + "⏵ " + ChatColor.GRAY + "Click to Edit")) {
                String[] strings = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING).split("-");
                String type = strings[strings.length-1].toLowerCase();
                ClickMethods.enterKitEditor(p,type);
                p.closeInventory();
                p.playSound(p,Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,1f,1.5f);
            }
        }
    }
}
