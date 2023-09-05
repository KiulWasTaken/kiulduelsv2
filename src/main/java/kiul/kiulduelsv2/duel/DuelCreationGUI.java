package kiul.kiulduelsv2.duel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DuelCreationGUI implements Listener {

    @EventHandler
    public void cancelClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("Main Menu") || e.getView().getTitle().equalsIgnoreCase("Team Manager")) {
            e.setCancelled(true);
        }
    }

    public void openMainMenu (Player p) {

        Inventory inv = Bukkit.createInventory(null, 9,"Main Menu");


        // items.add(xyz);

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName("");
        filler.setItemMeta(fillerMeta);

        ItemStack empty = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName("");
        empty.setItemMeta(emptyMeta);

        ItemStack maps = new ItemStack(Material.DEAD_BUSH);
        ItemMeta mapsMeta = maps.getItemMeta();
        mapsMeta.setDisplayName(ChatColor.GOLD + "Select Map");
        maps.setItemMeta(mapsMeta);

        ItemStack kits = new ItemStack(Material.GOLDEN_CARROT);
        ItemMeta kitsMeta = kits.getItemMeta();
        kitsMeta.setDisplayName(ChatColor.YELLOW + "Select Kit");
        kits.setItemMeta(kitsMeta);

        ItemStack teams = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta teamsMeta = teams.getItemMeta();
        teamsMeta.setDisplayName(ChatColor.RED + "Manage Teams");
        teams.setItemMeta(teamsMeta);


        List<ItemStack> itemStackList = new ArrayList<>(Arrays.asList(filler,filler,maps,empty,kits,empty,teams,filler,filler));
        ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
        inv.setContents(itemStacks);
      //  ItemStack[] items = filler,filler,maps,empty,kits,empty,teams,filler,filler;
        p.openInventory(inv);
    }
    
    boolean team3Enabled = false;

    public void openTeamManager (Player p) {

        Inventory inv = Bukkit.createInventory(null, 27,"Team Manager");

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName("");
        filler.setItemMeta(fillerMeta);

        ItemStack empty = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName("");
        empty.setItemMeta(emptyMeta);

        ItemStack redTeam = new ItemStack(Material.RED_WOOL);
        ItemMeta redTeamMeta = redTeam.getItemMeta();
        redTeamMeta.setDisplayName(ChatColor.RED +"Red Team Size:");
        redTeam.setItemMeta(redTeamMeta);

        ItemStack blueTeam = new ItemStack(Material.BLUE_WOOL);
        ItemMeta blueTeamMeta = blueTeam.getItemMeta();
        blueTeamMeta.setDisplayName(ChatColor.BLUE + "Blue Team Size:");
        blueTeam.setItemMeta(blueTeamMeta);

        ItemStack purpleTeam = new ItemStack(Material.PURPLE_WOOL);
        ItemMeta purpleTeamMeta = purpleTeam.getItemMeta();
        purpleTeamMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Purple Team Size:");
        purpleTeam.setItemMeta(purpleTeamMeta);

        ItemStack enableTeam3 = new ItemStack(Material.GRAY_WOOL);
        ItemMeta enableTeam3Meta = enableTeam3.getItemMeta();
        enableTeam3Meta.setDisplayName(ChatColor.GOLD + "Enable Team 3");
        enableTeam3.setItemMeta(enableTeam3Meta);

        ItemStack add1blue = new ItemStack(Material.LIME_TERRACOTTA);
        ItemMeta add1blueMeta = add1blue.getItemMeta();
        add1blueMeta.setDisplayName(ChatColor.GREEN + "+1");
        add1blueMeta.setLocalizedName("ADD1BLUE");
        add1blue.setItemMeta(add1blueMeta);

        ItemStack add1red = new ItemStack(Material.LIME_TERRACOTTA);
        ItemMeta add1redMeta = add1red.getItemMeta();
        add1redMeta.setDisplayName(ChatColor.GREEN + "+1");
        add1redMeta.setLocalizedName("ADD1RED");
        add1red.setItemMeta(add1redMeta);

        ItemStack remove1blue = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta remove1blueMeta = remove1blue.getItemMeta();
        remove1blueMeta.setDisplayName(ChatColor.RED + "-1");
        remove1blueMeta.setLocalizedName("REMOVE1BLUE");
        remove1blue.setItemMeta(remove1blueMeta);

        ItemStack remove1red = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta remove1redMeta = remove1red.getItemMeta();
        remove1redMeta.setDisplayName(ChatColor.RED + "-1");
        remove1redMeta.setLocalizedName("REMOVE1RED");
        remove1red.setItemMeta(remove1redMeta);

        ItemStack add1purple = new ItemStack(Material.LIME_TERRACOTTA);
        ItemMeta add1purpleMeta = add1purple.getItemMeta();
        add1purpleMeta.setDisplayName(ChatColor.GREEN + "+1");
        add1purpleMeta.setLocalizedName("ADD1PURPLE");
        add1purple.setItemMeta(add1purpleMeta);

        ItemStack remove1purple = new ItemStack(Material.PURPLE_TERRACOTTA);
        ItemMeta remove1purpleMeta = remove1purple.getItemMeta();
        remove1purpleMeta.setDisplayName(ChatColor.RED + "-1");
        remove1purpleMeta.setLocalizedName("REMOVE1PURPLE");
        remove1purple.setItemMeta(remove1purpleMeta);
        
        
        
        if (team3Enabled) {
            List<ItemStack> itemStackList = new ArrayList<>(Arrays.asList(
                    filler,empty,add1blue,empty,add1red,empty,add1purple,empty,filler,
                    filler,empty,blueTeam,empty,redTeam,empty,purpleTeam,empty,filler,
                    filler,empty,remove1blue,empty,remove1red,remove1purple,empty,empty,filler
            ));
            ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
            inv.setContents(itemStacks);
        } else {
            List<ItemStack> itemStackList = new ArrayList<>(Arrays.asList(
                    filler,empty,add1blue,empty,add1red,empty,empty,empty,filler,
                    filler,empty,blueTeam,empty,redTeam,empty,enableTeam3,empty,filler,
                    filler,empty,remove1blue,empty,remove1red,empty,empty,empty,filler
            ));
            ItemStack[] itemStacks = itemStackList.toArray(new ItemStack[0]);
            inv.setContents(itemStacks);
        }

        p.openInventory(inv);
    }

    public void openKitSelector (Player p) {
        Inventory inv = Bukkit.createInventory(null, 27,"Kit Selector");
        
        int slot = 1;

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta fillerMeta = filler.getItemMeta();
        fillerMeta.setDisplayName("");
        filler.setItemMeta(fillerMeta);

        ItemStack empty = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta emptyMeta = empty.getItemMeta();
        emptyMeta.setDisplayName("");
        empty.setItemMeta(emptyMeta);

        ItemStack kitSlot1 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot1Meta = kitSlot1.getItemMeta();
        kitSlot1Meta.setDisplayName("");
        kitSlot1.setItemMeta(kitSlot1Meta);

        ItemStack kitSlot2 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot2Meta = kitSlot2.getItemMeta();
        kitSlot2Meta.setDisplayName("");
        kitSlot2.setItemMeta(kitSlot2Meta);

        ItemStack kitSlot3 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot3Meta = kitSlot3.getItemMeta();
        kitSlot3Meta.setDisplayName("");
        kitSlot3.setItemMeta(kitSlot3Meta);

        ItemStack kitSlot4 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot4Meta = kitSlot4.getItemMeta();
        kitSlot4Meta.setDisplayName("");
        kitSlot4.setItemMeta(kitSlot4Meta);

        ItemStack kitSlot5 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot5Meta = kitSlot5.getItemMeta();
        kitSlot5Meta.setDisplayName("");
        kitSlot5.setItemMeta(kitSlot5Meta);

        ItemStack kitSlot6 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot6Meta = kitSlot6.getItemMeta();
        kitSlot6Meta.setDisplayName("");
        kitSlot6.setItemMeta(kitSlot6Meta);

        ItemStack kitSlot7 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot7Meta = kitSlot7.getItemMeta();
        kitSlot7Meta.setDisplayName("");
        kitSlot7.setItemMeta(kitSlot7Meta);

        ItemStack kitSlot8 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot8Meta = kitSlot8.getItemMeta();
        kitSlot8Meta.setDisplayName("");
        kitSlot8.setItemMeta(kitSlot8Meta);

        ItemStack kitSlot9 = new ItemStack(Material.CYAN_TERRACOTTA);
        ItemMeta kitSlot9Meta = kitSlot9.getItemMeta();
        kitSlot9Meta.setDisplayName("");
        kitSlot9.setItemMeta(kitSlot9Meta);

        switch (slot) {
            case 1:
                kitSlot1.setType(Material.LIME_TERRACOTTA);
                break;
            case 2:
                kitSlot2.setType(Material.LIME_TERRACOTTA);
                break;
            case 3:
                kitSlot3.setType(Material.LIME_TERRACOTTA);
                break;
            case 4:
                kitSlot4.setType(Material.LIME_TERRACOTTA);
                break;
            case 5:
                kitSlot5.setType(Material.LIME_TERRACOTTA);
                break;
            case 6:
                kitSlot6.setType(Material.LIME_TERRACOTTA);
                break;
            case 7:
                kitSlot7.setType(Material.LIME_TERRACOTTA);
                break;
            case 8:
                kitSlot8.setType(Material.LIME_TERRACOTTA);
                break;
            case 9:
                kitSlot9.setType(Material.LIME_TERRACOTTA);
                break;
        }
        p.openInventory(inv);
    }

    public void openMapSelector (Player p) {

    }
}
