package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.CustomKitData;
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
        ArrayList<String> lore = new ArrayList<>();
        Inventory lobbyKit = Bukkit.createInventory(null,36,"lobby");
        lore.add(ChatColor.GRAY + "Right-click to select & edit your kits");
        lobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT EDITOR", Material.ENCHANTED_BOOK,1,lore,null,null,"kiteditor"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to queue");
        lobbyKit.setItem(4, ItemStackMethods.createItemStack(C.t(C.LIGHT_GREEN+"&lQUEUE"), Material.EMERALD,1,lore,null,null,"queue"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to open settings");
        lobbyKit.setItem(5, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "SETTINGS", Material.REPEATER,1,lore,null,null,"settings"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to create a party");
        lobbyKit.setItem(0, ItemStackMethods.createItemStack(C.t(C.RED+"" + ChatColor.BOLD+"CREATE PARTY") , Material.FIREWORK_ROCKET,1,lore,null,null,"party"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to open the shop");
        lobbyKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.WHITE + "" + ChatColor.BOLD + "STORE", Material.NETHER_STAR,1,lore,null,null,"shop"));
        lore.clear();
        CustomKitData.get().set("global.lobby.inventory", InventoryToBase64.itemStackArrayToBase64(lobbyKit.getContents()));

        Inventory queueKit = Bukkit.createInventory(null,36,"queue");
        lore.add(ChatColor.GRAY + "Right-click to Leave Queue");
        queueKit.setItem(8,ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "LEAVE QUEUE",Material.RED_DYE,1,lore,null,null,"leavequeue"));
        lore.clear();
        CustomKitData.get().set("global.queue.inventory", InventoryToBase64.itemStackArrayToBase64(queueKit.getContents()));

        Inventory partyMemberLobbyKit = Bukkit.createInventory(null,36,"lobbyPM");
        lore.add(ChatColor.GRAY + "Right-click to leave party");
        partyMemberLobbyKit.setItem(8,ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "LEAVE PARTY",Material.RED_DYE,1,lore,null,null,"leaveparty"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to view party member info");
        partyMemberLobbyKit.setItem(5, ItemStackMethods.createItemStack(C.t(C.PINK+"&lPARTY INFO"), Material.GLOBE_BANNER_PATTERN,1,lore,null,null,"partyinfo"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to select & edit your kits");
        partyMemberLobbyKit.setItem(4, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT EDITOR", Material.ENCHANTED_BOOK,1,lore,null,null,"kiteditor"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to open settings");
        partyMemberLobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "SETTINGS", Material.REPEATER,1,lore,null,null,"settings"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to open the shop");
        partyMemberLobbyKit.setItem(1, ItemStackMethods.createItemStack(ChatColor.WHITE + "" + ChatColor.BOLD + "STORE", Material.NETHER_STAR,1,lore,null,null,"shop"));
        lore.clear();
        CustomKitData.get().set("global.partymember.inventory", InventoryToBase64.itemStackArrayToBase64(partyMemberLobbyKit.getContents()));

        Inventory partyLeaderLobbyKit = Bukkit.createInventory(null,36,"lobbyPL");
        lore.add(ChatColor.GRAY + "Right-Click to queue for party-based gamemodes");
        partyLeaderLobbyKit.setItem(4, ItemStackMethods.createItemStack( ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "PARTY FIGHT", Material.PINK_SHULKER_BOX,1,lore,null,null,"partyqueue"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-Click to disband the party");
        partyLeaderLobbyKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "DISBAND PARTY", Material.RED_DYE,1,lore,null,null,"partydisband"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right-click to open settings");
        partyLeaderLobbyKit.setItem(5, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "SETTINGS", Material.REPEATER,1,lore,null,null,"settings"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right click to Queue");
        partyLeaderLobbyKit.setItem(4, ItemStackMethods.createItemStack(C.t(C.LIGHT_GREEN+"&lQUEUE"), Material.EMERALD,1,lore,null,null,"queue"));
        lore.clear();
        lore.add(ChatColor.GRAY + "Right click to open the Kit Menu");
        partyLeaderLobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT EDITOR", Material.ENCHANTED_BOOK,1,lore,null,null,"kiteditor"));
        lore.clear();
        CustomKitData.get().set("global.partyleader.inventory", InventoryToBase64.itemStackArrayToBase64(partyLeaderLobbyKit.getContents()));

        Inventory spectatorKit = Bukkit.createInventory(null,36,"spectator");
        lore.add(ChatColor.GRAY + "Right-Click to Stop Spectating");
        spectatorKit.setItem(8,ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "STOP SPECTATING",Material.RED_DYE,1,lore,null,null,"leavegame"));
        lore.clear();
        CustomKitData.get().set("global.spectator.inventory", InventoryToBase64.itemStackArrayToBase64(spectatorKit.getContents()));

        CustomKitData.save();
    }
}
