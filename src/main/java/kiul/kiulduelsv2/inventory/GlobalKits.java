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
import org.bukkit.inventory.ItemStack;
import org.pattychips.pattyeventv2.Methods.ItemStackMethod;
import org.pattychips.pattyeventv2.PattyEventV2;

import javax.security.auth.callback.CallbackHandler;
import java.util.ArrayList;
import java.util.List;

import static org.pattychips.pattyeventv2.Methods.JoinSpectatorMethod.*;

public class GlobalKits {

    public static void instantiate () {
        if (C.PAT_MODE) {

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Click to queue for party games");
            ItemStack partyQueue = ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "Party Fight", Material.PINK_SHULKER_BOX, 1, lore, null, null, "partyqueue");
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to open the cosmetic shop");
            ItemStack shop = ItemStackMethod.createItemStack(shopName, Material.CHEST, 1, lore, null, 0, false, false);
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to sit in/out of events");
            ItemStack sitout = ItemStackMethod.createItemStack("&cSit-Out", Material.OAK_STAIRS, 1, lore, null, 0, false, false);
            lore.clear();
            lore.add(C.t("&7&lRight-Click &6- &8Starts the game with a 3 second timer!"));
            ItemStack start = ItemStackMethod.createItemStack("&6&Start Game", Material.CLOCK, 1, lore, null, 0, false, false);
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to open the spectator teleport menu");
            ItemStack specCompass = ItemStackMethod.createItemStack(speccompassName, Material.RECOVERY_COMPASS, 1, lore, null, 0, true, false);
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to travel to the practice world");
            ItemStack practice = ItemStackMethod.createItemStack("&6Practice", Material.DIAMOND_SWORD, 1, lore, null, 0, false, true);
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to edit duels/event kits");
            ItemStack kitEditor = ItemStackMethod.createItemStack("&#9e369bKit-Editor", Material.ENCHANTED_BOOK, 1, lore, null, 0, false, false);
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to queue for duels");
            ItemStack queue = C.createItemStack(C.LIGHT_GREEN+"Duels-Queue", Material.EMERALD, 1, lore.toArray(String[]::new), null, 0, "queue", null);
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to queue");
            ItemStack duelQueueButton = ItemStackMethods.createItemStack(C.t(C.LIGHT_GREEN + "Queue"), Material.EMERALD, 1, lore, null, null, "queue");
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to open settings");
            ItemStack settings = ItemStackMethods.createItemStack(ChatColor.RED +  "Settings", Material.REPEATER, 1, lore, null, null, "settings");
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to create a party");
            ItemStack createParty = ItemStackMethods.createItemStack(C.t(C.RED +  "Create Party"), Material.FIREWORK_ROCKET, 1, lore, null, null, "party");
            lore.clear();
            lore.add(ChatColor.GRAY + "Click to open the shop");
            ItemStack tagShop = ItemStackMethods.createItemStack(ChatColor.WHITE +  "Tag Shop", Material.NETHER_STAR, 1, lore, null, null, "shop");
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to leave party");
            ItemStack leaveParty = ItemStackMethods.createItemStack(ChatColor.RED +  "Leave Party", Material.RED_DYE, 1, lore, null, null, "leaveparty");
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-Click to disband the party");
            ItemStack partydisband = ItemStackMethods.createItemStack(ChatColor.RED +  "Disband Party", Material.RED_DYE, 1, lore, null, null, "partydisband");
            lore.clear();

            Inventory lobbyKit = Bukkit.createInventory(null, 36, "lobby");
            lobbyKit.setItem(0,kitEditor);
            lobbyKit.setItem(1,queue);
            lobbyKit.setItem(2,sitout);
            lobbyKit.setItem(4,specCompass);
            lobbyKit.setItem(6,createParty);
            lobbyKit.setItem(7,practice);
            lobbyKit.setItem(12,settings);
            lobbyKit.setItem(13,shop);
            lobbyKit.setItem(14,tagShop);
            // lobbyKit.setItem(8,specVisibility);
            CustomKitData.get().set("global.lobby.inventory", InventoryToBase64.itemStackArrayToBase64(lobbyKit.getContents()));

            Inventory queueKit = Bukkit.createInventory(null, 36, "queue");
            lore.add(ChatColor.GRAY + "Right-click to Leave Queue");
            queueKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "LEAVE QUEUE", Material.RED_DYE, 1, lore, null, null, "leavequeue"));
            lore.clear();
            CustomKitData.get().set("global.queue.inventory", InventoryToBase64.itemStackArrayToBase64(queueKit.getContents()));

            Inventory partyMemberLobbyKit = Bukkit.createInventory(null, 36, "lobbyPM");
            partyMemberLobbyKit.setItem(0,kitEditor);
            partyMemberLobbyKit.setItem(1,tagShop);
            partyMemberLobbyKit.setItem(2,settings);
            //partyMemberLobbyKit.setItem(4,teamSwitcher);
            partyMemberLobbyKit.setItem(6,leaveParty);
            partyMemberLobbyKit.setItem(7,practice);
            //partyMemberLobbyKit.setItem(8,specVisibility);
            partyMemberLobbyKit.setItem(12,settings);
            partyMemberLobbyKit.setItem(13,shop);
            partyMemberLobbyKit.setItem(14,tagShop);
            CustomKitData.get().set("global.partymember.inventory", InventoryToBase64.itemStackArrayToBase64(partyMemberLobbyKit.getContents()));

            Inventory partyLeaderLobbyKit = Bukkit.createInventory(null, 36, "lobbyPL");
            partyLeaderLobbyKit.setItem(0,kitEditor);
            partyLeaderLobbyKit.setItem(1,partyQueue);
            partyLeaderLobbyKit.setItem(2,queue);
            //partyLeaderLobbyKit.setItem(4,teamSwitch);
            partyLeaderLobbyKit.setItem(6,partydisband);
            partyLeaderLobbyKit.setItem(7,practice);
            //partyMemberLobbyKit.setItem(8,specVisibility);
            partyLeaderLobbyKit.setItem(12,settings);
            partyLeaderLobbyKit.setItem(13,shop);
            partyLeaderLobbyKit.setItem(14,tagShop);
            CustomKitData.get().set("global.partyleader.inventory", InventoryToBase64.itemStackArrayToBase64(partyLeaderLobbyKit.getContents()));

            Inventory spectatorKit = Bukkit.createInventory(null, 36, "spectator");
            lore.add(ChatColor.GRAY + "Right-Click to Stop Spectating");
            spectatorKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "STOP SPECTATING", Material.RED_DYE, 1, lore, null, null, "leavegame"));
            lore.clear();
            CustomKitData.get().set("global.spectator.inventory", InventoryToBase64.itemStackArrayToBase64(spectatorKit.getContents()));
        } else {
            ArrayList<String> lore = new ArrayList<>();
            Inventory lobbyKit = Bukkit.createInventory(null, 36, "lobby");
            lore.add(ChatColor.GRAY + "Right-click to select & edit your kits");
            lobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT EDITOR", Material.ENCHANTED_BOOK, 1, lore, null, null, "kiteditor"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to queue");
            lobbyKit.setItem(4, ItemStackMethods.createItemStack(C.t(C.LIGHT_GREEN + "&lQUEUE"), Material.EMERALD, 1, lore, null, null, "queue"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to open settings");
            lobbyKit.setItem(5, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "SETTINGS", Material.REPEATER, 1, lore, null, null, "settings"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to create a party");
            lobbyKit.setItem(0, ItemStackMethods.createItemStack(C.t(C.RED + "" + ChatColor.BOLD + "CREATE PARTY"), Material.FIREWORK_ROCKET, 1, lore, null, null, "party"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to open the shop");
            lobbyKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.WHITE + "" + ChatColor.BOLD + "STORE", Material.NETHER_STAR, 1, lore, null, null, "shop"));
            lore.clear();
            CustomKitData.get().set("global.lobby.inventory", InventoryToBase64.itemStackArrayToBase64(lobbyKit.getContents()));

            Inventory queueKit = Bukkit.createInventory(null, 36, "queue");
            lore.add(ChatColor.GRAY + "Right-click to Leave Queue");
            queueKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "LEAVE QUEUE", Material.RED_DYE, 1, lore, null, null, "leavequeue"));
            lore.clear();
            CustomKitData.get().set("global.queue.inventory", InventoryToBase64.itemStackArrayToBase64(queueKit.getContents()));

            Inventory partyMemberLobbyKit = Bukkit.createInventory(null, 36, "lobbyPM");
            lore.add(ChatColor.GRAY + "Right-click to leave party");
            partyMemberLobbyKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "LEAVE PARTY", Material.RED_DYE, 1, lore, null, null, "leaveparty"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to view party member info");
            partyMemberLobbyKit.setItem(5, ItemStackMethods.createItemStack(C.t(C.PINK + "&lPARTY INFO"), Material.GLOBE_BANNER_PATTERN, 1, lore, null, null, "partyinfo"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to select & edit your kits");
            partyMemberLobbyKit.setItem(4, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT EDITOR", Material.ENCHANTED_BOOK, 1, lore, null, null, "kiteditor"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to open settings");
            partyMemberLobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "SETTINGS", Material.REPEATER, 1, lore, null, null, "settings"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to open the shop");
            partyMemberLobbyKit.setItem(1, ItemStackMethods.createItemStack(ChatColor.WHITE + "" + ChatColor.BOLD + "STORE", Material.NETHER_STAR, 1, lore, null, null, "shop"));
            lore.clear();
            CustomKitData.get().set("global.partymember.inventory", InventoryToBase64.itemStackArrayToBase64(partyMemberLobbyKit.getContents()));

            Inventory partyLeaderLobbyKit = Bukkit.createInventory(null, 36, "lobbyPL");
            lore.add(ChatColor.GRAY + "Right-Click to queue for party-based gamemodes");
            partyLeaderLobbyKit.setItem(4, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "PARTY FIGHT", Material.PINK_SHULKER_BOX, 1, lore, null, null, "partyqueue"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-Click to disband the party");
            partyLeaderLobbyKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "DISBAND PARTY", Material.RED_DYE, 1, lore, null, null, "partydisband"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right-click to open settings");
            partyLeaderLobbyKit.setItem(5, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "SETTINGS", Material.REPEATER, 1, lore, null, null, "settings"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right click to Queue");
            partyLeaderLobbyKit.setItem(4, ItemStackMethods.createItemStack(C.t(C.LIGHT_GREEN + "&lQUEUE"), Material.EMERALD, 1, lore, null, null, "queue"));
            lore.clear();
            lore.add(ChatColor.GRAY + "Right click to open the Kit Menu");
            partyLeaderLobbyKit.setItem(3, ItemStackMethods.createItemStack(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "KIT EDITOR", Material.ENCHANTED_BOOK, 1, lore, null, null, "kiteditor"));
            lore.clear();
            CustomKitData.get().set("global.partyleader.inventory", InventoryToBase64.itemStackArrayToBase64(partyLeaderLobbyKit.getContents()));

            Inventory spectatorKit = Bukkit.createInventory(null, 36, "spectator");
            lore.add(ChatColor.GRAY + "Right-Click to Stop Spectating");
            spectatorKit.setItem(8, ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "STOP SPECTATING", Material.RED_DYE, 1, lore, null, null, "leavegame"));
            lore.clear();
            CustomKitData.get().set("global.spectator.inventory", InventoryToBase64.itemStackArrayToBase64(spectatorKit.getContents()));
        }
        CustomKitData.save();
    }
}
