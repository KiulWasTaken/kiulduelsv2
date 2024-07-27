package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.StatDB;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.EnchantEnum;
import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemEnum;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.lobbyKit;

public class InventoryListeners implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {

        if (e.getView().getTitle().equalsIgnoreCase("Statistics") || e.getView().getTitle().contains("'s Inventory")) {
            e.setCancelled(true);
        }

        if (e.getWhoClicked() instanceof Player p) {
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
                            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(C.t(ItemEnum.itemType.getDisplayName()))) {
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
                                    if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).endsWith("x" + e.getCurrentItem().getAmount())) {
                                        ItemStack itemStack = e.getCurrentItem().clone();
                                        ItemMeta itemMeta = itemStack.getItemMeta();
                                        itemMeta.setDisplayName("");
                                        itemMeta.setLore(null);
                                        itemStack.setAmount(e.getCurrentItem().getAmount());
                                        itemStack.setItemMeta(itemMeta);
                                        if (ClickMethods.itemAmountIsWithinLimit(p,itemStack)) {
                                            p.getInventory().addItem(itemStack);
                                        }
                                        return;
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


        @EventHandler
        public void setSlotonPlayerJoin (PlayerJoinEvent e){
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        e.getPlayer().setGameMode(GameMode.SURVIVAL);

        if (Userdata.get().get(e.getPlayer().getUniqueId()+".stats.wins") == null) {
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.wins", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.losses", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.streak", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.best_streak", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.kills", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.deaths", 0);
        }
            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }
            for (String type : types) {
                if (StatDB.readPlayer(e.getPlayer().getUniqueId(),"stat_elo_"+type) == null) {
                    StatDB.writePlayer(e.getPlayer().getUniqueId(),"stat_elo_"+type,700);
                }
            }
            if (!e.getPlayer().hasPlayedBefore() ||  Userdata.get().get(e.getPlayer().getUniqueId()+ ".selected-slot." +types.get(0)) == null) {
                for (String type : types) {
                    Userdata.get().set(e.getPlayer().getUniqueId()+".selected-slot." +type,1);
                }
                Userdata.save();
                for (String type : types) {
                    kitSlot.get(e.getPlayer()).put(type,Userdata.get().getInt(e.getPlayer().getUniqueId()+".selected-slot."+type));
                }

                try {KitMethods.lobbyKit(e.getPlayer());} catch (IOException er) {er.printStackTrace();}
            } else {
                kitSlot.put(e.getPlayer(),new HashMap<>());
                for (String type : types) {
                    kitSlot.get(e.getPlayer()).put(type,Userdata.get().getInt(e.getPlayer().getUniqueId()+".selected-slot."+type));
                }
            }
            try {
                KitMethods.lobbyKit(e.getPlayer());
            } catch (IOException er) {
                er.printStackTrace();
            }
        }

        @EventHandler
        public void saveSlotonPlayerLeave (PlayerQuitEvent e){
            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }
            for (String type : types) {
                Userdata.get().set(e.getPlayer().getUniqueId()+".selected-slot." + type, kitSlot.get(e.getPlayer()).get(type));
            }
            Userdata.save();
        }
}
