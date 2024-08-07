package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.party.PartyManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static kiul.kiulduelsv2.C.partyManager;
import static kiul.kiulduelsv2.C.t;

public class KitMethods {

    public static HashMap<Player,HashMap<String,Integer>> kitSlot = new HashMap<>();
    public static void lobbyKit (Player p) throws IOException {
        ItemStack[] kitContents;
        List<String> lore = new ArrayList<>();
        if (partyManager.findPartyForMember(p.getUniqueId()) != null) {
            if (partyManager.findPartyForMember(p.getUniqueId()).isLeader(p.getUniqueId())) {
                kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.partyleader.inventory")).getContents();
            } else {
                kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.partymember.inventory")).getContents();
            }
        } else {
            kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.lobby.inventory")).getContents();
        }
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
        if (partyManager.findPartyForMember(p.getUniqueId()) != null) {
            Party party = partyManager.findPartyForMember(p.getUniqueId());
            if (party.teamOne().contains(p.getUniqueId())) {
                lore.add(ChatColor.GRAY + "Right-Click to change party team");
                p.getInventory().setItem(0, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "RED", Material.RED_WOOL, 1, lore, null, null, "partyteam"));
                lore.clear();
            } else {
                lore.add(ChatColor.GRAY + "Right-Click to change party team");
                p.getInventory().setItem(0, ItemStackMethods.createItemStack(ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE", Material.BLUE_WOOL, 1, lore, null, null, "partyteam"));
                lore.clear();
            }
        }
    }



    public static void spectatorKit (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.itemStackArrayFromBase64((String) Userdata.get().get("kits.global.spectator.inventory"));
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
        p.setAllowFlight(true);
        p.setFlying(true);
    }

    public static void saveInventoryToSelectedKitSlot (Player p,String type) {
            Userdata.get().set("kits." + p.getUniqueId() + "." + type + ".kit-slot-" + kitSlot.get(p).get(type) + ".inventory", InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
            Userdata.get().set("kits." + p.getUniqueId() + "." + type +  ".kit-slot-" + kitSlot.get(p).get(type) + ".armour", InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
            Userdata.save();

    }

    public static void loadSelectedKitSlot (Player p,String type) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.itemStackArrayFromBase64((String) Userdata.get().get("kits." + p.getUniqueId() + "." + type + ".kit-slot-" + kitSlot.get(p).get(type) + ".inventory"));
        ItemStack[] armourContents = InventoryToBase64.itemStackArrayFromBase64((String) Userdata.get().get("kits." + p.getUniqueId() + "." + type + ".kit-slot-" + kitSlot.get(p).get(type) + ".armour"));
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(armourContents);
    }

    public static void loadGlobalKit (Player p, String kitName) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global."+kitName+".inventory")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
    }
}
