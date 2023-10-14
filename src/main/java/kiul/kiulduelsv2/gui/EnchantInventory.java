package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class EnchantInventory {

    public static String itemEnchantInvTitle = C.t("&#681dc8&lE&#7719cf&ln&#8615d6&lc&#9511dc&lh&#a40ee3&la&#b30aea&ln&#c206f1&lt &#d102f8&lI&#d102f8&ln&#c206f1&lv&#b30aea&le&#a40ee3&ln&#9511dc&lt&#8615d6&lo&#7719cf&lr&#681dc8&ly");

    public static void itemEnchantInventory(Player p) {

        Inventory inventory = Bukkit.createInventory(p, 45, itemEnchantInvTitle);

        for (int i = 0; i < 45; i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack(" ", Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, List.of(""), null, null));
        }

        for (EnchantEnum item : EnchantEnum.values()) {
            List<String> lore = new ArrayList<>();
            if (item.getEnchantment() != null) {
                lore.add(C.t("&6⏵ &7Click to enchant your item with " + item.getEnchantment().getKey()).replaceFirst("minecraft:", ""));
            }
            String displayName = item.getDisplayName();
            if (item.getEnchantment() != null) {
                displayName = C.t(item.getEnchantment().getKey().toString() + " " + item.getEnchantLvl()).toLowerCase().replaceFirst("minecraft:", "").replaceAll("_", " ");
                displayName = "&r&l" + displayName.substring(0, 1).toUpperCase() + displayName.substring(1);
            }
            inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(displayName, item.getMaterial(), 1, lore, item.getEnchantment(), item.getEnchantLvl()));
        }

        p.openInventory(inventory);

    }

}
