package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.StatDB;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.EnchantEnum;
import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemEnum;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.lobbyKit;

public class InventoryListeners implements Listener {

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {

        if (e.getView().getTitle().equalsIgnoreCase("Statistics") || e.getView().getTitle().contains("'s Inventory")) {
            e.setCancelled(true);
        }

        if (e.getWhoClicked() instanceof Player p) {

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
        }
            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }
            for (String type : types) {
                if (StatDB.readPlayer(e.getPlayer().getUniqueId(),"stat_elo_"+type) == null) {
                    StatDB.writePlayer(e.getPlayer().getUniqueId(),"stat_elo_"+type,700);
                }
                StatDB.updatePlayerPlacement("stat_elo_"+type);
            }
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
                kitSlot.put(e.getPlayer(),new HashMap<>());
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
