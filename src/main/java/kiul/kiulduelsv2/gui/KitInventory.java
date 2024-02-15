package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class KitInventory {



    public static void kitInventory(Player p) {

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.2F, 0.5F);

        Inventory inventory = Bukkit.createInventory(p, 9, "Kit Selector");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {

            if (i <= 8) {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.LIGHT_GRAY_STAINED_GLASS, 1, emptylore, null, null,null));
            } else if (i <= 17) {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
            } else {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
            }
        }


        for (KitEnum item : KitEnum.values()) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }
                String itemName = ItemStackMethods.translateHexColorCodes("&#","",item.getDisplayName());
                if (item.getlocalName().contains(KitMethods.kitSlot.get(p).toString())) {
                    lore.add(ChatColor.GOLD + "⏵ " + ChatColor.GRAY+"Click to Edit");
                    inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(ChatColor.GREEN+KitMethods.kitSlot.get(p).toString(), Material.LIME_TERRACOTTA, 1, lore, null, null, item.getlocalName()));
                } else {
                    inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));
                    lore.add(ChatColor.GOLD + "⏵ " + ChatColor.GRAY+"Click to Select");
                }
        }

        p.openInventory(inventory);

    }

}
