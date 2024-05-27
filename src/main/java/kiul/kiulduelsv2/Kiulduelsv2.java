package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.ArenaListeners;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.ConfigListeners;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelListeners;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.clickevents.KitClickEvent;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.inventory.GlobalKits;
import kiul.kiulduelsv2.inventory.InteractListeners;
import kiul.kiulduelsv2.inventory.InventoryListeners;
import kiul.kiulduelsv2.scoreboard.ScoreboardListeners;
import kiul.kiulduelsv2.scoreboard.ScoreboardMethods;
import kiul.kiulduelsv2.util.TabCompleter;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;

public final class Kiulduelsv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Userdata.setup();
        Arenadata.setup();
        GlobalKits.instantiate();
        getServer().getPluginManager().registerEvents(new ConfigListeners(), this);
        getServer().getPluginManager().registerEvents(new InventoryListeners(),this);
        getServer().getPluginManager().registerEvents(new Queue(),this);
        getServer().getPluginManager().registerEvents(new KitClickEvent(),this);
        getServer().getPluginManager().registerEvents(new InteractListeners(),this);
        getServer().getPluginManager().registerEvents(new DuelListeners(),this);
        getServer().getPluginManager().registerEvents(new ArenaListeners(),this);
        getServer().getPluginManager().registerEvents(new ScoreboardListeners(),this);
        getCommand("kit").setExecutor(new Commands());
        getCommand("test").setExecutor(new Commands());
        getCommand("arena").setExecutor(new Commands());
        getCommand("testgeneration").setExecutor(new Commands());
        getCommand("t").setExecutor(new Commands());
        getCommand("party").setExecutor(new Commands());
        getCommand("e").setExecutor(new Commands());
        getCommand("cancel").setExecutor(new Commands());
        getCommand("previewinv").setExecutor(new Commands());
        getCommand("reroll").setExecutor(new Commands());
        getCommand("recap").setExecutor(new Commands());
        getCommand("save").setExecutor(new Commands());
        getCommand("kit").setTabCompleter(new TabCompleter());
        getCommand("arena").setTabCompleter(new TabCompleter());
        getCommand("party").setTabCompleter(new TabCompleter());
        if (Bukkit.getOnlinePlayers() != null) {
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                kitSlot.put(onlinePlayers.getPlayer(), (int) Userdata.get().get("selected-slot." + onlinePlayers.getPlayer().getUniqueId()));
            }
        }
        Userdata.save();

        try {
            C.scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this);
        } catch (NoPacketAdapterAvailableException e) {
            // If no packet adapter was found, you can fall back to the no-op implementation:
            C.scoreboardLibrary = new NoopScoreboardLibrary();
            this.getLogger().warning("No scoreboard packet adapter available!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (Bukkit.getOnlinePlayers() != null) {
            for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                Userdata.get().set("selected-slot." + onlinePlayers.getPlayer().getUniqueId(), kitSlot.get(onlinePlayers.getPlayer()));
            }
        }
        Userdata.save();
        C.scoreboardLibrary.close();
    }
}
