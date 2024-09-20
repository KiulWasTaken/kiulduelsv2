package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.config.CustomKitData;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.layout.ItemEditInventory;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static kiul.kiulduelsv2.gui.ClickMethods.inEditor;
import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;

public class InventoryListeners implements Listener {

    public static HashMap<Player, Inventory> previousInventory = new HashMap<>();

    @EventHandler
    public void updatePreviousInventoryEvent (InventoryCloseEvent e) {
        if (inEditor.containsKey((Player)e.getPlayer()) && !e.getView().getTitle().equals("Customize Armour") && !e.getView().getTitle().equals(ItemEditInventory.itemEnchantInvTitle)) {
            previousInventory.put((Player)e.getPlayer(),e.getInventory());
        } else {
            previousInventory.remove((Player) e.getPlayer());
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {

        if (e.getView().getTitle().equalsIgnoreCase("Statistics") || e.getView().getTitle().contains("'s Inventory")) {
            e.setCancelled(true);
        }
    }


        @EventHandler
        public void setSlotonPlayerJoin (PlayerJoinEvent e){
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        e.getPlayer().setGameMode(GameMode.SURVIVAL);

        if (Userdata.get().get(e.getPlayer().getUniqueId()+".stats.wins") == null) {
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.wins", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.losses", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.streak", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.best_streak", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.kills", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.deaths", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.damagedelta", new ArrayList<Integer>());

        }
        e.getPlayer().setDisplayName(null);
            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }
            if (!DuelsDB.playerExists(e.getPlayer().getUniqueId())) {
                DuelsDB.setupPlayer(e.getPlayer().getUniqueId());
            } else {
                DuelsDB.checkIntegrity(e.getPlayer().getUniqueId());
            }
            kitSlot.put(e.getPlayer(),new HashMap<>());
            if (!e.getPlayer().hasPlayedBefore() ||  Userdata.get().get(e.getPlayer().getUniqueId()+ ".selected-slot." +types.get(0)) == null) {

                for (String type : types) {
                    Userdata.get().set(e.getPlayer().getUniqueId()+".selected-slot." +type,1);
                }
                Userdata.save();
                for (String type : types) {
                    kitSlot.get(e.getPlayer()).put(type,Userdata.get().getInt(e.getPlayer().getUniqueId()+".selected-slot."+type));
                }

                try {KitMethods.lobbyKit(e.getPlayer());} catch (IOException er) {er.printStackTrace();}
            } else {
                for (String type : types) {
                    kitSlot.get(e.getPlayer()).put(type,Userdata.get().getInt(e.getPlayer().getUniqueId()+".selected-slot."+type));
                }
            }
            try {
                KitMethods.lobbyKit(e.getPlayer());
            } catch (IOException er) {
                er.printStackTrace();
            }
        }

        @EventHandler
        public void saveSlotonPlayerLeave (PlayerQuitEvent e){
            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }
            for (String type : types) {
                Userdata.get().set(e.getPlayer().getUniqueId()+".selected-slot." + type, kitSlot.get(e.getPlayer()).get(type));
            }
            Userdata.save();
        }
}
