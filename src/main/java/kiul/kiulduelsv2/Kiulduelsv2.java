package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.ArenaListeners;
import kiul.kiulduelsv2.config.*;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.duel.DuelListeners;
import kiul.kiulduelsv2.duel.Recap;
import kiul.kiulduelsv2.gui.KitEditor;
import kiul.kiulduelsv2.gui.layout.ItemEditInventory;
import kiul.kiulduelsv2.gui.layout.ItemInventory;
import kiul.kiulduelsv2.gui.layout.KitInventory;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.layout.LayoutMenuInventory;
import kiul.kiulduelsv2.gui.queue.PartyQueueInventory;
import kiul.kiulduelsv2.gui.settings.SettingsInventory;
import kiul.kiulduelsv2.inventory.GlobalKits;
import kiul.kiulduelsv2.inventory.InteractListeners;
import kiul.kiulduelsv2.inventory.InventoryListeners;
import kiul.kiulduelsv2.scoreboard.ScoreboardListeners;
import kiul.kiulduelsv2.util.LeakPatcher;
import kiul.kiulduelsv2.util.TabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.pattychips.pattyeventv2.PattyEventV2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;

public final class Kiulduelsv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.broadcastMessage("\uefe9");
        String worldName = "kiulduels";
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator = worldCreator.generator(new EmptyChunkGenerator()); // Adjust the environment if needed
            worldCreator = worldCreator.generateStructures(false); // Enable or disable structures as needed
            worldCreator.createWorld();
        }

         C.PAT_MODE = getServer().getPluginManager().getPlugin("PattyEventV2") != null;

        // Config
        Userdata.setup();
        Arenadata.setup();
        CustomKitData.setup();
        UserPreferences.setup();

        // Plugin Methods
        GlobalKits.instantiate();

        // Listeners
        getServer().getPluginManager().registerEvents(new LeakPatcher(), this);
        getServer().getPluginManager().registerEvents(new ConfigListeners(), this);
        getServer().getPluginManager().registerEvents(new InventoryListeners(),this);
        getServer().getPluginManager().registerEvents(new ItemInventory(),this);
        getServer().getPluginManager().registerEvents(new ItemEditInventory(),this);
        getServer().getPluginManager().registerEvents(new Queue(),this);
        getServer().getPluginManager().registerEvents(new KitInventory(),this);
        getServer().getPluginManager().registerEvents(new InteractListeners(),this);
        getServer().getPluginManager().registerEvents(new PartyQueueInventory(),this);
        getServer().getPluginManager().registerEvents(new DuelListeners(),this);
        getServer().getPluginManager().registerEvents(new ArenaListeners(),this);
        getServer().getPluginManager().registerEvents(new ScoreboardListeners(),this);
        getServer().getPluginManager().registerEvents(new LayoutMenuInventory(),this);
        getServer().getPluginManager().registerEvents(new Recap(),this);
        getServer().getPluginManager().registerEvents(new SettingsInventory(),this);
        getServer().getPluginManager().registerEvents(new KitEditor(),this);

        // Commands
        getCommand("test").setExecutor(new Commands());
        getCommand("arena").setExecutor(new Commands());
        getCommand("testgeneration").setExecutor(new Commands());
        getCommand("t").setExecutor(new Commands());
        getCommand("party").setExecutor(new Commands());
        getCommand("e").setExecutor(new Commands());
        getCommand("previewinv").setExecutor(new Commands());
        getCommand("reroll").setExecutor(new Commands());
        getCommand("recap").setExecutor(new Commands());
        getCommand("spectate").setExecutor(new Commands());
        getCommand("duel").setExecutor(new Commands());
        getCommand("duels-kit").setExecutor(new Commands());
        getCommand("duels-kit").setTabCompleter(new TabCompleter());
        getCommand("arena").setTabCompleter(new TabCompleter());
        getCommand("party").setTabCompleter(new TabCompleter());
        getCommand("duel").setTabCompleter(new TabCompleter());

        if (Bukkit.getOnlinePlayers() != null) {

            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }

            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                kitSlot.put(onlinePlayers.getPlayer(),new HashMap<>());
                for (String type : types) {
                    kitSlot.get(onlinePlayers.getPlayer()).put(type,(int) Userdata.get().get(onlinePlayers.getPlayer().getUniqueId() + ".selected-slot." + type));
                }
            }
        }
        Userdata.save();


        // Database
        DuelsDB.connect();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (Bukkit.getOnlinePlayers() != null) {
            List<String> types = new ArrayList<>();
            for (String key : Queue.queue.keySet()) {
                String[] keys = key.split("-");
                types.add(keys[0].toLowerCase());
            }

            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                for (String type : types) {
                    Userdata.get().set(onlinePlayers.getPlayer().getUniqueId()+".selected-slot." +type,kitSlot.get(onlinePlayers).get(type));
                }
            }
        }
        Userdata.save();
    }
}
