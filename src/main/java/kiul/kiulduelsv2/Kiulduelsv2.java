package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.ArenaListeners;
import kiul.kiulduelsv2.config.ConfigListeners;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.inventory.GlobalKits;
import kiul.kiulduelsv2.inventory.InventoryListeners;
import org.bukkit.plugin.java.JavaPlugin;

public final class Kiulduelsv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (Userdata.get().get("kits.global") == null) {
            GlobalKits.instantiate();
        }
        getServer().getPluginManager().registerEvents(new ArenaListeners(),this);
        getServer().getPluginManager().registerEvents(new ConfigListeners(), this);
        getServer().getPluginManager().registerEvents(new InventoryListeners(),this);
        getCommand("kit").setExecutor(new Commands());
        getCommand("test").setExecutor(new Commands());
        getCommand("arena").setExecutor(new Commands());
        getCommand("testgeneration").setExecutor(new Commands());
        getCommand("t").setExecutor(new Commands());
        getCommand("e").setExecutor(new Commands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
