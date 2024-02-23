package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.party.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import javax.security.auth.callback.CallbackHandler;
import java.util.ArrayList;
import java.util.List;

public class GlobalKits {

    public static void instantiate () {
        ArrayList<String> lore = new ArrayList<>() {{
            add(" ");
        }};
        Inventory lobbyKit = Bukkit.createInventory(null,36,"lobby");
        lore.add(ChatColor.GRAY + "Right click to open the Kit Menu");
        lobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.DARK_PURPLE + "Kit Editor", Material.ENCHANTED_BOOK,1,lore,null,null,"kiteditor"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right click to Queue");
        lobbyKit.setItem(4, ItemStackMethods.createItemStack( ChatColor.GRAY + "Queue", Material.GRAY_DYE,1,lore,null,null,"queue"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right click to open Settings");
        lobbyKit.setItem(5, ItemStackMethods.createItemStack(ChatColor.WHITE + "Settings", Material.PAPER,1,lore,null,null,"settings"));
        lore.clear();
        Userdata.get().set("kits.global.lobby.inventory", InventoryToBase64.itemStackArrayToBase64(lobbyKit.getContents()));

        Inventory queueKit = Bukkit.createInventory(null,36,"queue");
        lore.add(ChatColor.GRAY + "Right click to Leave Queue");
        queueKit.setItem(8,ItemStackMethods.createItemStack(ChatColor.RED + "Leave Queue",Material.RED_DYE,1,lore,null,null,"leavequeue"));
        lore.clear();
        Userdata.get().set("kits.global.queue.inventory", InventoryToBase64.itemStackArrayToBase64(queueKit.getContents()));

        Inventory partyMemberLobbyKit = Bukkit.createInventory(null,36,"lobbyPM");

        Inventory partyLeaderLobbyKit = Bukkit.createInventory(null,36,"lobbyPL");

        Inventory spectatorKit = Bukkit.createInventory(null,36,"spectator");
        lore.add(ChatColor.GRAY + "Right click to Stop Spectating");
        spectatorKit.setItem(9,ItemStackMethods.createItemStack(ChatColor.RED + "Stop Spectating",Material.RED_DYE,1,lore,null,null,"leavegame"));
        lore.clear();
        Userdata.get().set("kits.global.spectator.inventory", InventoryToBase64.itemStackArrayToBase64(spectatorKit.getContents()));

        Userdata.save();
    }
}
