package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.UserPreferences;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.duel.Duel;
import kiul.kiulduelsv2.duel.DuelManager;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.layout.ItemEditInventory;
import kiul.kiulduelsv2.gui.settings.SettingsEnum;
import kiul.kiulduelsv2.gui.settings.SettingsInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.pattychips.pattyeventv2.Commands.Practice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static kiul.kiulduelsv2.gui.KitEditor.inEditor;
import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;

public class InventoryListeners implements Listener {

    public static HashMap<Player, Inventory> previousInventory = new HashMap<>();

    @EventHandler
    public void updatePreviousInventoryEvent (InventoryCloseEvent e) {
        if (inEditor.containsKey((Player)e.getPlayer())){
            if (!e.getView().getTitle().equals("Customize Armour") && !e.getView().getTitle().equals(ItemEditInventory.itemEnchantInvTitle)) {
                previousInventory.put((Player) e.getPlayer(), e.getInventory());
            }
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
        if (!C.PAT_MODE) {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
        } else {
            Practice.tpWorld(e.getPlayer(),false);
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Duel duel : C.duelManager.getDuels()) {
                        for (UUID playerUUID : duel.getPlayers()) {
                            if (Bukkit.getPlayer(playerUUID) != null) Bukkit.getPlayer(playerUUID).setInvulnerable(false);
                        }
                    }
                }
            }.runTaskLater(C.plugin,1);
        }
        if (Userdata.get().get(e.getPlayer().getUniqueId()+".stats.wins") == null) {
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.wins", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.losses", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.streak", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.best_streak", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.kills", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.deaths", 0);
            Userdata.get().set(e.getPlayer().getUniqueId()+".stats.damagedelta", new ArrayList<Integer>());
        }

        for (SettingsEnum settingsEnum : SettingsEnum.values()) {
            if (UserPreferences.get().get(e.getPlayer().getUniqueId() + "." + settingsEnum.getLocalName()) == null) {
                UserPreferences.get().set(e.getPlayer().getUniqueId() + "." + settingsEnum.getLocalName(),true);
            }
        }
        UserPreferences.save();

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
            } else {
                for (String type : types) {
                    kitSlot.get(e.getPlayer()).put(type,Userdata.get().getInt(e.getPlayer().getUniqueId()+".selected-slot."+type));
                }
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
