package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;

public class KitMethods {

    public static HashMap<Player,Integer> kitSlot = new HashMap<>();
    public static void lobbyKit (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.lobby.inventory")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
    }

    public static void spectatorKit (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.spectator.inventory")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
    }

    public static void saveInventoryToSelectedKitSlot (Player p) {
        Userdata.get().set("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p) + ".inventory",InventoryToBase64.toBase64(p.getInventory()));
        Userdata.get().set("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p) + ".armour",InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
    }

    public static void loadSelectedKitSlot (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p) + ".inventory")).getContents();
        ItemStack[] armourContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p) + ".armour")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(armourContents);
    }

    public static void loadGlobalKit (Player p, String kitName) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global."+kitName+".inventory")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
    }
}
