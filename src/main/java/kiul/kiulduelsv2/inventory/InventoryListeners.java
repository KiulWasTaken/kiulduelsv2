package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.EnchantEnum;
import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemEnum;
import kiul.kiulduelsv2.gui.ItemInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.lobbyKit;

public class InventoryListeners implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {

        if (e.getWhoClicked() instanceof Player p) {
            if (e.getView().getTitle().equalsIgnoreCase(ItemInventory.itemInvTitle)) {

                if (e.getClickedInventory() != null && e.getClickedInventory() == p.getOpenInventory().getTopInventory() || e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                    e.setCancelled(true);
                    if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                        e.getCursor().setAmount(0);
                        return;
                    }
                    if (e.getClickedInventory() != null && e.getClickedInventory() == p.getOpenInventory().getBottomInventory() && e.getClick() == ClickType.SHIFT_LEFT) {
                        e.setCancelled(true);
                        e.getCurrentItem().setType(Material.AIR);
                        return;
                    }
                    for (ItemEnum item : ItemEnum.values()) {
                        if (e.getCurrentItem() == null)
                            break;
                        if (item.getDisplayName() != null && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(item.getDisplayName()))) {
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(ItemEnum.backtomain.getDisplayName()))) {
                                ItemInventory.itemInventory(p);
                                break;
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(ItemEnum.clearinventory.getDisplayName()))) {
                                for (ItemStack items : p.getInventory().getContents()) {
                                    if (items != null && items.getType() != Material.AIR) {
                                        items.setAmount(0);
                                    }
                                }
                                return;
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(ItemEnum.enchantmenu.getDisplayName()))) {
                                EnchantInventory.itemEnchantInventory(p);
                                return;
                            }
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(ItemEnum.itemamount.getDisplayName()))) {
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
                                        ItemInventory.subItemInventory(p, p.getOpenInventory().getTopInventory().getSize(), subItem.getInventory(), amount);
                                        break;
                                    }
                                }
                                break;
                            } else if (item.getInventorySize() != null) {
                                ItemInventory.subItemInventory(p, item.getInventorySize(), item.getInventory(), 1);
                                break;
                            }
                        } else {
                            if (e.getClickedInventory() != null && e.getClickedInventory().equals(p.getOpenInventory().getTopInventory())) {
                                for (ItemEnum subItem : ItemEnum.values()) {
                                    if (e.getCurrentItem() != null && e.getCurrentItem().getType() == subItem.getMaterial()) {
                                        if (subItem.getInventorySize() == null && subItem.getInventory() != null) {
                                            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).endsWith("x" + e.getCurrentItem().getAmount())) {
                                                ItemStack itemStack = e.getCurrentItem().clone();
                                                ItemMeta itemMeta = itemStack.getItemMeta();
                                                itemMeta.setDisplayName("");
                                                itemMeta.setLore(null);
                                                itemStack.setAmount(e.getCurrentItem().getAmount());
                                                itemStack.setItemMeta(itemMeta);
                                                p.getInventory().addItem(itemStack);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (e.getView().getTitle().equalsIgnoreCase(EnchantInventory.itemEnchantInvTitle)) {
                    if (e.getClickedInventory() != null && e.getClickedInventory() == p.getOpenInventory().getTopInventory() || e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) {
                        e.setCancelled(true);
                        if (e.getCursor() != null && e.getCursor().getType() != Material.AIR) {
                            e.getCursor().setAmount(0);
                            return;
                        }
                        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(EnchantEnum.iteminventory.getDisplayName()))) {
                            ItemInventory.itemInventory(p);
                            return;
                        }
                        if (e.getCursor() != null && e.getCursor().getType() != Material.AIR && e.getCurrentItem().getType() != Material.AIR) {
                            e.getCursor().setAmount(0);
                            return;
                        }
                        if (e.getCurrentItem() != null && e.getCurrentItem().getType() == EnchantEnum.resetenchants.getMaterial()) {
                            if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                if (p.getInventory().getItemInMainHand().getEnchantments().size() > 0) {
                                    for (Map.Entry<Enchantment, Integer> a : p.getInventory().getItemInMainHand().getEnchantments().entrySet()) {
                                        p.getInventory().getItemInMainHand().removeEnchantment(a.getKey());
                                    }
                                } else {
                                    p.sendMessage(C.t("&7[&4⏩&7] &cItem already has no enchantments"));
                                }
                            } else {
                                p.sendMessage(C.t("&7[&4⏩&7] &cHold an item to enchant"));
                            }
                        }
                        for (EnchantEnum enchant : EnchantEnum.values()) {
                            if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
                                if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                    for (Map.Entry<Enchantment, Integer> entry : e.getCurrentItem().getEnchantments().entrySet()) {
                                        if (entry.getKey().canEnchantItem(p.getInventory().getItemInMainHand())) {
                                            for (Map.Entry<Enchantment, Integer> entry1 : p.getInventory().getItemInMainHand().getEnchantments().entrySet()) {
                                                if (entry.getKey().conflictsWith(entry1.getKey())) {
                                                    p.sendMessage(C.t("&7[&4⏩&7] &cItem has a conflicting enchantment"));
                                                    return;
                                                }
                                            }
                                            p.getInventory().getItemInMainHand().addEnchantment(entry.getKey(), entry.getValue());
                                        } else {
                                            p.sendMessage(C.t("&7[&4⏩&7] &cYou cannot add this enchant to this item"));
                                        }
                                        break;
                                    }
                                } else {
                                    p.sendMessage(C.t("&7[&4⏩&7] &cHold an item to enchant"));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

        @EventHandler
        public void setSlotonPlayerJoin (PlayerJoinEvent e){
            if (!e.getPlayer().hasPlayedBefore()) {
                Userdata.get().set("selected-slot." + e.getPlayer().getUniqueId(), 1);
                Userdata.save();
                kitSlot.put(e.getPlayer(), 1);
            } else {
                kitSlot.put(e.getPlayer(), (int) Userdata.get().get("selected-slot." + e.getPlayer().getUniqueId()));
            }
            try {
                KitMethods.lobbyKit(e.getPlayer());
            } catch (IOException er) {
                er.printStackTrace();
            }
        }

        @EventHandler
        public void saveSlotonPlayerLeave (PlayerQuitEvent e){
            Userdata.get().set("selected-slot." + e.getPlayer().getUniqueId(), kitSlot.get(e.getPlayer()));
            Userdata.save();
        }
}
