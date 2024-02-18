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
        lobbyKit.setItem(3, ItemStackMethods.createItemStack("Kit Editor", Material.ENCHANTED_BOOK,1,lore,null,null,"kiteditor"));
        lobbyKit.setItem(4, ItemStackMethods.createItemStack("Queue", Material.NETHER_STAR,1,lore,null,null,"queue"));
        lobbyKit.setItem(5, ItemStackMethods.createItemStack("Settings", Material.PAPER,1,lore,null,null,"settings"));
        Userdata.get().set("kits.global.lobby.inventory", InventoryToBase64.itemStackArrayToBase64(lobbyKit.getContents()));


        Inventory queueKit = Bukkit.createInventory(null,36,"queue");
        queueKit.setItem(8,ItemStackMethods.createItemStack("Leave Queue",Material.RED_DYE,1,lore,null,null,"leavequeue"));
        Userdata.get().set("kits.global.queue.inventory", InventoryToBase64.itemStackArrayToBase64(queueKit.getContents()));

        Inventory partyMemberLobbyKit = Bukkit.createInventory(null,36,"lobbyPM");

        Inventory partyLeaderLobbyKit = Bukkit.createInventory(null,36,"lobbyPL");

        Inventory spectatorKit = Bukkit.createInventory(null,36,"spectator");
        spectatorKit.setItem(9,ItemStackMethods.createItemStack("Stop Spectating",Material.RED_DYE,1,lore,null,null,"leavegame"));
        Userdata.get().set("kits.global.spectator.inventory", InventoryToBase64.itemStackArrayToBase64(spectatorKit.getContents()));

        Userdata.save();
    }
}
