package kiul.kiulduelsv2.gui.settings;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.UserPreferences;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.gui.layout.CosmeticEnum;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SettingsInventory implements Listener {

    public static void settings(Player p, int page, Material border) {
        int invSize = 54;
        Inventory inventory = Bukkit.createInventory(p, invSize, "Settings");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null, null));
        }

        int[] slots = {11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
        for (int slot : slots) {
            inventory.setItem(slot, ItemStackMethods.createItemStack(ChatColor.GRAY + "?", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null, null));
        }

        for (int i = 0; i <= invSize / 9; i++) {
            if (i * 9 < invSize && i * 9 >= 0) {
                inventory.setItem(i * 9, ItemStackMethods.createItemStack("", border, 1, emptylore, null, null, null));
            }
            if ((i * 9) - 1 < invSize && (i * 9) - 1 >= 0) {
                inventory.setItem((i * 9) - 1, ItemStackMethods.createItemStack("", border, 1, emptylore, null, null, null));
            }
        }

        for (int i = (20 * page); i < (20 * page)+20; i++) {
            SettingsEnum[] settingsEnumValues = SettingsEnum.values();
            SettingsEnum item = settingsEnumValues[i];
            int slot = slots[i];

            List<String> lore = new ArrayList<>();
            for (String itemLore : item.getLore()) {
                lore.add((itemLore));
            }

            String enabled;
            if (UserPreferences.get().getBoolean(p.getUniqueId() + "." + item.getLocalName())) {
                enabled = "tick";
            } else {
                enabled = "cross";
            }
            lore.add(enabled);

            String itemName = ItemStackMethods.translateHexColorCodes("&#", "", item.getDisplayName());
            inventory.setItem(slot, ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getLocalName()));
        }
        p.openInventory(inventory);

    }

    @EventHandler
    public void settingsInventoryClick (InventoryClickEvent e) {
        if (e.getView().getTitle().equalsIgnoreCase("Settings")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                Player p = (Player) e.getView().getPlayer();
                String localName = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING);
                ItemStack clickedItem = e.getCurrentItem();

                UserPreferences.get().set(p.getUniqueId()+"."+localName,!UserPreferences.get().getBoolean(p.getUniqueId()+"."+localName));
                UserPreferences.save();
                settings(p,0,Material.WHITE_STAINED_GLASS_PANE);
            }
        }
    }
}
