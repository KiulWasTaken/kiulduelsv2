package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ItemInventory {

    public static String itemInvTitle = C.t("&#238332&lI&#29983a&lt&#2eac42&le&#34c14a&lm &#39d651&lI&#3fea59&ln&#44ff61&lv&#3fea59&le&#39d651&ln&#34c14a&lt&#2eac42&lo&#29983a&lr&#238332&ly");

    public static void itemInventory(Player p) {

        Inventory inventory = Bukkit.createInventory(p, 27, itemInvTitle);

        for (ItemEnum item : ItemEnum.values()) {
            List<String> lore = new ArrayList<>();
            lore.add(C.t("&6⏵ &7Click to expand"));

            inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(item.getDisplayName(), item.getMaterial(), 1, lore));
        }

        p.openInventory(inventory);

    }

}
