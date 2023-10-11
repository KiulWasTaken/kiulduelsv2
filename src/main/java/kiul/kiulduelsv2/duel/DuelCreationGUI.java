package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuelCreationGUI implements Listener {

    @EventHandler
    public void cancelClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Kit Slot Selector") || e.getView().getTitle().equalsIgnoreCase("Team Manager")) {
            e.setCancelled(true);
        }
    }


    public void openKitSelector (Player p) {
        Inventory inv = Bukkit.createInventory(null, 27,"Kit Slot Selector");


        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName("");
        filler.setItemMeta(fillerMeta);

        ItemStack empty = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName("");
        empty.setItemMeta(emptyMeta);

        ItemStack kitSlot1 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot1Meta = kitSlot1.getItemMeta();
        kitSlot1Meta.setDisplayName("");
        kitSlot1.setItemMeta(kitSlot1Meta);

        ItemStack kitSlot2 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot2Meta = kitSlot2.getItemMeta();
        kitSlot2Meta.setDisplayName("");
        kitSlot2.setItemMeta(kitSlot2Meta);

        ItemStack kitSlot3 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot3Meta = kitSlot3.getItemMeta();
        kitSlot3Meta.setDisplayName("");
        kitSlot3.setItemMeta(kitSlot3Meta);

        ItemStack kitSlot4 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot4Meta = kitSlot4.getItemMeta();
        kitSlot4Meta.setDisplayName("");
        kitSlot4.setItemMeta(kitSlot4Meta);

        ItemStack kitSlot5 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot5Meta = kitSlot5.getItemMeta();
        kitSlot5Meta.setDisplayName("");
        kitSlot5.setItemMeta(kitSlot5Meta);

        ItemStack kitSlot6 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot6Meta = kitSlot6.getItemMeta();
        kitSlot6Meta.setDisplayName("");
        kitSlot6.setItemMeta(kitSlot6Meta);

        ItemStack kitSlot7 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot7Meta = kitSlot7.getItemMeta();
        kitSlot7Meta.setDisplayName("");
        kitSlot7.setItemMeta(kitSlot7Meta);

        ItemStack kitSlot8 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot8Meta = kitSlot8.getItemMeta();
        kitSlot8Meta.setDisplayName("");
        kitSlot8.setItemMeta(kitSlot8Meta);

        ItemStack kitSlot9 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot9Meta = kitSlot9.getItemMeta();
        kitSlot9Meta.setDisplayName("");
        kitSlot9.setItemMeta(kitSlot9Meta);

        switch (KitMethods.kitSlot.get(p)) {
            case 1:
                kitSlot1.setType(Material.LIME_TERRACOTTA);
                break;
            case 2:
                kitSlot2.setType(Material.LIME_TERRACOTTA);
                break;
            case 3:
                kitSlot3.setType(Material.LIME_TERRACOTTA);
                break;
            case 4:
                kitSlot4.setType(Material.LIME_TERRACOTTA);
                break;
            case 5:
                kitSlot5.setType(Material.LIME_TERRACOTTA);
                break;
            case 6:
                kitSlot6.setType(Material.LIME_TERRACOTTA);
                break;
            case 7:
                kitSlot7.setType(Material.LIME_TERRACOTTA);
                break;
            case 8:
                kitSlot8.setType(Material.LIME_TERRACOTTA);
                break;
            case 9:
                kitSlot9.setType(Material.LIME_TERRACOTTA);
                break;
        }
        p.openInventory(inv);
    }

    public void openMapSelector (Player p) {

    }
}
