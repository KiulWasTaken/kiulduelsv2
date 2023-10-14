package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemInventory {

    public static String itemInvTitle = C.t("&#238332&lI&#29983a&lt&#2eac42&le&#34c14a&lm &#39d651&lI&#3fea59&ln&#44ff61&lv&#3fea59&le&#39d651&ln&#34c14a&lt&#2eac42&lo&#29983a&lr&#238332&ly");

    public static void itemInventory(Player p) {

        Inventory inventory = Bukkit.createInventory(p, 27, itemInvTitle);

        for (ItemEnum item : ItemEnum.values()) {
            if (item.getInventorySize() != null) {
                List<String> lore = new ArrayList<>();
                lore.add(C.t("&6⏵ &7Click to expand"));

                inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(item.getDisplayName(), item.getMaterial(), 1, lore, null, null));
            }
        }

        p.openInventory(inventory);

    }

    public static void subItemInventory(Player p, Integer invSize, String inv, Integer itemAmount) {

        Inventory inventory = Bukkit.createInventory(p, invSize, itemInvTitle);

        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null));
        }

        inventory.setItem(invSize - 5, ItemStackMethods.createItemStack(ItemEnum.itemamount.getDisplayName(), ItemEnum.itemamount.getMaterial(), itemAmount, List.of(new String[]{""}), null, null));

        inventory.setItem(invSize - 9, ItemStackMethods.createItemStack(ItemEnum.backtomain.getDisplayName(), ItemEnum.backtomain.getMaterial(), 1, List.of(new String[]{""}), null, null));

        for (ItemEnum item : ItemEnum.values()) {
            if (item.getInventorySize() == null) {
                if (item.getInventory() != null && item.getInventory().equalsIgnoreCase(inv)) {
                    int amount = itemAmount;
                    boolean potion = true;
                    if (item.getMaterial() != Material.SPLASH_POTION && item.getMaterial() != Material.POTION && item.getMaterial() != Material.TIPPED_ARROW) {
                        potion = false;
                    }
                    if (item.getMaterial().getMaxStackSize() < itemAmount) {
                        amount = item.getMaterial().getMaxStackSize();
                    }
                    List<String> lore = new ArrayList<>();
                    lore.add(C.t("&6⏵ &7Click to take " + amount + "x " + item.getMaterial()));

                    String displayName = C.t(item.getMaterial().toString() + (potion ? " of " + item.getPotionData().getType():"") + " &7x" + amount).toLowerCase().replaceAll("_", " ");
                    displayName = "&r&l" + displayName.substring(0, 1).toUpperCase() + displayName.substring(1);

                    if (potion == false) {
                        inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(displayName, item.getMaterial(), amount, lore, null, null));
                    } else {
                        inventory.setItem(item.getInventorySlot(), ItemStackMethods.createPotion(displayName, item.getMaterial(), amount, item.getPotionData()));
                    }
                }
            }
        }

        p.openInventory(inventory);

    }

}
