package kiul.kiulduelsv2.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueueInventory {



    public static void queueInventory(Player p) {

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.2F, 0.5F);

        Inventory inventory = Bukkit.createInventory(p, 45, "Queue");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i,ItemStackMethods.createItemStack("",Material.BLACK_STAINED_GLASS_PANE,1,null, null, null));

        }


        for (QueueEnum item : QueueEnum.values()) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }
                String itemName = ItemStackMethods.translateHexColorCodes("&#","",item.getDisplayName());

                if (item.getSkullValue() != null) {
                    inventory.setItem(item.getInventorySlot(),ItemStackMethods.createPlayerHead(item.getSkullValue(),itemName));
                } else {
                    inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null));
                }
        }

        p.openInventory(inventory);

    }

}
