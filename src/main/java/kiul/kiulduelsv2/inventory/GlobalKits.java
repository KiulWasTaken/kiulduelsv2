package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GlobalKits {

    public static void instantiate () {
        ArrayList<String> lore = new ArrayList<>() {{
            add(" ");
        }};
        Inventory lobbyKit = Bukkit.createInventory(null,36,"lobby");
        lobbyKit.setItem(33, ItemStackMethods.createItemStack("Kit Editor", Material.WRITABLE_BOOK,1,lore,null,null,"kiteditor"));
        lobbyKit.setItem(32, ItemStackMethods.createItemStack("Queue", Material.NETHER_STAR,1,lore,null,null,"queue"));
        lobbyKit.setItem(36, ItemStackMethods.createItemStack("Settings", Material.PAPER,1,lore,null,null,"settings"));
        lobbyKit.setItem(31, ItemStackMethods.createItemStack("Settings", Material.PAPER,1,lore,null,null,"queue"));
        Userdata.get().set("kits.global.lobby.inventory", InventoryToBase64.toBase64(lobbyKit));
        Userdata.get().set("kits.global.lobby.armour",InventoryToBase64.itemStackArrayToBase64(null));

        Inventory queueKit = Bukkit.createInventory(null,36,"queue");
        queueKit.setItem(36,ItemStackMethods.createItemStack("Leave Queue",Material.REDSTONE,1,lore,null,null,"leavequeue"));

        Inventory partyMemberLobbyKit = Bukkit.createInventory(null,36,"lobbyPM");

        Inventory partyLeaderLobbyKit = Bukkit.createInventory(null,36,"lobbyPL");

        Inventory spectatorKit = Bukkit.createInventory(null,36,"spectator");

        Userdata.save();
    }
}
