package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;

public class KitMethods {

    public static HashMap<Player,Integer> kitSlot = new HashMap<>();
    public static void lobbyKit (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global.lobby")).getContents();
        p.getInventory().setContents(kitContents);
        p.getInventory().setArmorContents(null);
    }

    public static void saveInventorytoSelectedKitSlot (Player p) {
        Userdata.get().set("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p),p.getInventory());
    }

    public static void loadSelectedKitSlot (Player p) throws IOException {
        ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p))).getContents();
    }
}
