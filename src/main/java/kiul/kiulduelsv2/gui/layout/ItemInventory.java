package kiul.kiulduelsv2.gui.layout;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.gui.ClickMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;

import static kiul.kiulduelsv2.gui.ClickMethods.inEditor;

public class ItemInventory implements Listener {

    public static String itemInvTitle = C.t("&#238332&lI&#29983a&lt&#2eac42&le&#34c14a&lm &#39d651&lI&#3fea59&ln&#44ff61&lv&#3fea59&le&#39d651&ln&#34c14a&lt&#2eac42&lo&#29983a&lr&#238332&ly");

    public static void itemInventory(Player p) {

        Inventory inventory = Bukkit.createInventory(p, 27, itemInvTitle);

        for (ItemEnum item : ItemEnum.values()) {
            if (item.getInventorySize() != null) {
                List<String> lore = new ArrayList<>();
                lore.add(C.t("&6⏵ &7Click to expand"));

                inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(item.getDisplayName(), item.getMaterial(), 1, lore, null, null,null));
            }
        }

        p.openInventory(inventory);

    }

    public static void subItemInventory(Player p, Integer invSize, String inv, Integer itemAmount,Material itemType) {

        Inventory inventory = Bukkit.createInventory(p, invSize, itemInvTitle);
        if (itemType == null) {
            itemType = Material.POTION;
        }

        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
        }
        if (inv != "potions") {
            inventory.setItem(invSize - 5, ItemStackMethods.createItemStack(ItemEnum.itemamount.getDisplayName(), ItemEnum.itemamount.getMaterial(), itemAmount, List.of(new String[]{"&7Left click &6⏵&8 to cycle item amount up", "&7Right click &6⏵&8 to cycle item amount down"}), null, null, null));
        } else {
            inventory.setItem(invSize - 5, ItemStackMethods.createItemStack(ItemEnum.itemType.getDisplayName(), itemType, itemAmount, List.of(new String[]{"&7Left click &6⏵&8 to cycle item type"}), null, null, null));
        }
        inventory.setItem(invSize - 9, ItemStackMethods.createItemStack(ItemEnum.backtomain.getDisplayName(), ItemEnum.backtomain.getMaterial(), 1, List.of(new String[]{""}), null, null,null));

        inventory.setItem(invSize - 1, ItemStackMethods.createItemStack(ItemEnum.clearinventory.getDisplayName(), ItemEnum.clearinventory.getMaterial(), 1, List.of(new String[]{"&6⏵ &7Wipe your inventory of all its contents"}), null, null,null));

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

                    String displayName = C.t(item.getMaterial().toString() + (potion ? " of " + item.getPotionType():"") + " &7x" + amount).toLowerCase().replaceAll("_", " ");
                    displayName = "&r&l" + displayName.substring(0, 1).toUpperCase() + displayName.substring(1);

                    if (potion == false) {
                        inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(displayName, item.getMaterial(), amount, lore, null, null,null));
                    } else {
                        ItemStack i = new ItemStack(itemType);
                        PotionMeta iM = (PotionMeta) i.getItemMeta();
                        iM.setBasePotionType(item.getPotionType());
                        i.setItemMeta(iM);
                        displayName = C.t("&fPotion of ") + C.t("&f"+item.getPotionType().name() + " &7x" + itemAmount).toLowerCase().replaceAll("_", " ");
                        inventory.setItem(item.getInventorySlot(), ItemStackMethods.createPotion(displayName, itemType, itemAmount, item.getPotionType()));
                    }
                }
            }
        }

        p.openInventory(inventory);

    }


    @EventHandler
    public void itemInventoryClickEvent (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(ItemInventory.itemInvTitle)) {

            if (e.getClickedInventory() != null && e.getClickedInventory() == p.getOpenInventory().getTopInventory() || e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                e.setCancelled(true);
                if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                    e.getCursor().setAmount(0);
                    return;
                }
                for (ItemEnum item : ItemEnum.values()) {
                    if (e.getCurrentItem() == null)
                        break;
                    if (item.getDisplayName() != null && e.getCurrentItem().getItemMeta().getItemName().equalsIgnoreCase(C.t(item.getDisplayName()))) {
                        if (e.getCurrentItem().getItemMeta().getItemName().equalsIgnoreCase(C.t(ItemEnum.backtomain.getDisplayName()))) {
                            ItemInventory.itemInventory(p);
                            break;
                        }
                        if (e.getCurrentItem().getItemMeta().getItemName().equalsIgnoreCase(C.t(ItemEnum.clearinventory.getDisplayName()))) {
                            for (ItemStack items : p.getInventory().getContents()) {
                                if (items != null && items.getType() != Material.AIR) {
                                    items.setAmount(0);
                                }
                            }
                            return;
                        }
                        if (e.getCurrentItem().getItemMeta().getItemName().equalsIgnoreCase(C.t(ItemEnum.itemType.getDisplayName()))) {
                            int amount = 1;
                            Material itemType = Material.POTION;
                            if (e.getClick() == ClickType.LEFT) {
                                switch (e.getCurrentItem().getType()) {
                                    case POTION:
                                        itemType = Material.SPLASH_POTION;
                                        break;
                                    case SPLASH_POTION:
                                        itemType = Material.TIPPED_ARROW;
                                        amount = 64;
                                        break;
                                    case TIPPED_ARROW:
                                        itemType = Material.POTION;
                                        break;
                                }
                            } else if (e.getClick() == ClickType.RIGHT) {
                                switch (e.getCurrentItem().getType()) {
                                    case POTION:
                                        itemType = Material.TIPPED_ARROW;
                                        amount = 64;
                                        break;
                                    case SPLASH_POTION:
                                        itemType = Material.POTION;
                                        break;
                                    case TIPPED_ARROW:
                                        itemType = Material.SPLASH_POTION;
                                        break;
                                }
                            }
                            ItemInventory.subItemInventory(p, p.getOpenInventory().getTopInventory().getSize(), "potions", amount, itemType);
                            break;
                        }
                        if (e.getCurrentItem().getItemMeta().getItemName().equalsIgnoreCase(C.t(ItemEnum.itemamount.getDisplayName()))) {
                            int amount = 0;
                            if (e.getClick() == ClickType.LEFT) {
                                switch (e.getCurrentItem().getAmount()) {
                                    case 1:
                                        amount = 4;
                                        break;
                                    case 4:
                                        amount = 8;
                                        break;
                                    case 8:
                                        amount = 16;
                                        break;
                                    case 16:
                                        amount = 32;
                                        break;
                                    case 32:
                                        amount = 64;
                                        break;
                                    case 64:
                                        amount = 1;
                                        break;
                                }
                            } else if (e.getClick() == ClickType.RIGHT) {
                                switch (e.getCurrentItem().getAmount()) {
                                    case 1:
                                        amount = 64;
                                        break;
                                    case 4:
                                        amount = 1;
                                        break;
                                    case 8:
                                        amount = 4;
                                        break;
                                    case 16:
                                        amount = 8;
                                        break;
                                    case 32:
                                        amount = 16;
                                        break;
                                    case 64:
                                        amount = 32;
                                        break;
                                }
                            }
                            for (ItemEnum subItem : ItemEnum.values()) {
                                if (p.getOpenInventory().getTopInventory().getItem(0).getType() == subItem.getMaterial()) {
                                    ItemInventory.subItemInventory(p, p.getOpenInventory().getTopInventory().getSize(), subItem.getInventory(), amount, null);
                                    break;
                                }
                            }
                            break;
                        } else if (item.getInventorySize() != null) {
                            ItemInventory.subItemInventory(p, item.getInventorySize(), item.getInventory(), 1, null);
                            break;
                        }
                    } else {
                        if (e.getClickedInventory() != null && e.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                            if (e.getCurrentItem() != null) {
                                if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getItemName()).endsWith("x" + e.getCurrentItem().getAmount())) {
                                    ItemStack itemStack = e.getCurrentItem().clone();
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    itemMeta.setItemName(null);
                                    itemMeta.setLore(null);
                                    itemStack.setAmount(e.getCurrentItem().getAmount());
                                    itemStack.setItemMeta(itemMeta);
                                    if (ClickMethods.itemAmountIsWithinLimit(p, itemStack, inEditor.get(p))) {
                                        p.getInventory().addItem(itemStack);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
