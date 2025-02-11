package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.CustomKitData;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.party.PartyManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.pattychips.pattyeventv2.Commands.Practice;
import org.pattychips.pattyeventv2.Methods.ItemStackMethod;
import org.pattychips.pattyeventv2.PattyEventV2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static kiul.kiulduelsv2.C.partyManager;
import static kiul.kiulduelsv2.C.t;
import static org.pattychips.pattyeventv2.Methods.JoinSpectatorMethod.togglespecName;

public class KitMethods {

    public static HashMap<Player,HashMap<String,Integer>> kitSlot = new HashMap<>();
    public static void lobbyKit (Player p) throws IOException {
        ItemStack[] kitContents;
        List<String> lore = new ArrayList<>();
        lore.add(C.t("&7Click to enable/disable spectator visibility"));
        ItemStack specVisibility = ItemStackMethod.createItemStack(togglespecName, ((PattyEventV2.hidingSpectators.contains(p.getUniqueId())) ? Material.RED_CANDLE:Material.LIME_CANDLE), 1, lore, null, 0, false, false);
        lore.clear();

        if (partyManager.findPartyForMember(p.getUniqueId()) != null) {
            if (partyManager.findPartyForMember(p.getUniqueId()).isLeader(p.getUniqueId())) {
                kitContents = InventoryToBase64.fromBase64((String) CustomKitData.get().get("global.partyleader.inventory")).getContents();
            } else {
                kitContents = InventoryToBase64.fromBase64((String) CustomKitData.get().get("global.partymember.inventory")).getContents();
            }
        } else {
            {
                kitContents = InventoryToBase64.fromBase64((String) CustomKitData.get().get("global.lobby.inventory")).getContents();
            }
        }
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
        if (partyManager.findPartyForMember(p.getUniqueId()) != null) {
            Party party = partyManager.findPartyForMember(p.getUniqueId());
            int teamSwitchSlot = 0;
            if (C.PAT_MODE) {teamSwitchSlot = 4;}
            if (party.teamOne().contains(p.getUniqueId())) {

                lore.add(ChatColor.GRAY + "Right-Click to change party team");
                p.getInventory().setItem(teamSwitchSlot, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "RED", Material.RED_WOOL, 1, lore, null, null, "partyteam"));
                lore.clear();

            } else {
                lore.add(ChatColor.GRAY + "Right-Click to change party team");
                p.getInventory().setItem(teamSwitchSlot, ItemStackMethods.createItemStack(ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE", Material.BLUE_WOOL, 1, lore, null, null, "partyteam"));
                lore.clear();
            }
        }
        if (C.PAT_MODE) {
            p.getInventory().setItem(8, specVisibility);
            if (p.getWorld().getName().equalsIgnoreCase("practice")) {
                Practice.loadPracKit(p.getPlayer());
            }
        }
    }



    public static void spectatorKit (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.itemStackArrayFromBase64((String) CustomKitData.get().get("global.spectator.inventory"));
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
        p.setAllowFlight(true);
        p.setFlying(true);
    }

    public static void saveInventoryToSelectedKitSlot (Player p,String type) {
            CustomKitData.get().set(p.getUniqueId() + "." + type + ".kit-slot-" + kitSlot.get(p).get(type) + ".inventory", InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
            CustomKitData.get().set(p.getUniqueId() + "." + type +  ".kit-slot-" + kitSlot.get(p).get(type) + ".armour", InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
            CustomKitData.save();
    }

    public static void saveItemToSavedItemsArray (Player p, ItemStack givenItemStack) {
        if (givenItemStack != null) {
            ItemStack item = givenItemStack.clone();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING, "item");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(C.t("&#4DBA4B&m                                                 "));
            lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Take x1 Item");
            lore.add(ChatColor.GRAY + "Shift-Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase From Saved Items");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            ItemStack[] savedItemsArray = new ItemStack[28];
            if (CustomKitData.get().get(p.getUniqueId() + ".saved_items") != null) {
                try {
                    savedItemsArray = InventoryToBase64.itemStackArrayFromBase64((String) CustomKitData.get().get(p.getUniqueId() + ".saved_items"));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            List<ItemStack> savedItems = new ArrayList<>(Arrays.stream(savedItemsArray).toList());
            if (savedItems.contains(item)) {
                return;
            }
            savedItems.add(0, item);
            if (savedItems.size() > 27) {
                savedItems.remove(27);
            }
            savedItemsArray = savedItems.toArray(ItemStack[]::new);

            CustomKitData.get().set(p.getUniqueId() + ".saved_items", InventoryToBase64.itemStackArrayToBase64(savedItemsArray));
            CustomKitData.save();
        }
    }

    public static void eraseItemFromSavedItemsArray (Player p, ItemStack givenItemStack) {
        if (givenItemStack != null) {
            ItemStack item = givenItemStack.clone();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING, "item");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(C.t("&#4DBA4B&m                                                 "));
            lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Take x1 Item");
            lore.add(ChatColor.GRAY + "Shift-Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase From Saved Items");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            ItemStack[] savedItemsArray = new ItemStack[28];
            if (CustomKitData.get().get(p.getUniqueId() + ".saved_items") != null) {
                try {
                    savedItemsArray = InventoryToBase64.itemStackArrayFromBase64((String) CustomKitData.get().get(p.getUniqueId() + ".saved_items"));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            List<ItemStack> savedItems = new ArrayList<>(Arrays.stream(savedItemsArray).toList());
            if (!savedItems.contains(item)) {
                return;
            }
            savedItems.remove(item);
            savedItemsArray = savedItems.toArray(ItemStack[]::new);
            CustomKitData.get().set(p.getUniqueId() + ".saved_items", InventoryToBase64.itemStackArrayToBase64(savedItemsArray));
            CustomKitData.save();
        }
    }
    public static boolean savedItemsArrayContains(Player p, ItemStack givenItemStack) {
        if (givenItemStack != null) {
            ItemStack item = givenItemStack.clone();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING, "item");
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(C.t("&#4DBA4B&m                                                 "));
            lore.add(ChatColor.GRAY + "Left Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Take x1 Item");
            lore.add(ChatColor.GRAY + "Shift-Right Click " + ChatColor.GOLD + "⏵" + ChatColor.WHITE + " Erase From Saved Items");
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            ItemStack[] savedItemsArray = new ItemStack[28];
            if (CustomKitData.get().get(p.getUniqueId() + ".saved_items") != null) {
                try {
                    savedItemsArray = InventoryToBase64.itemStackArrayFromBase64((String) CustomKitData.get().get(p.getUniqueId() + ".saved_items"));
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }
            List<ItemStack> savedItems = new ArrayList<>(Arrays.stream(savedItemsArray).toList());
            return savedItems.contains(item);
        } else {
            return false;
        }
    }

    public static void loadSelectedKitSlot (Player p,String type) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.itemStackArrayFromBase64((String) CustomKitData.get().get(p.getUniqueId() + "." + type + ".kit-slot-" + kitSlot.get(p).get(type) + ".inventory"));
        ItemStack[] armourContents = InventoryToBase64.itemStackArrayFromBase64((String) CustomKitData.get().get(p.getUniqueId() + "." + type + ".kit-slot-" + kitSlot.get(p).get(type) + ".armour"));
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(armourContents);
    }

    public static void loadGlobalKit (Player p, String kitName) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) CustomKitData.get().get("global."+kitName+".inventory")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
    }
}
