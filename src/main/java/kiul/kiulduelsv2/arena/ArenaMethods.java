package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.config.Arenadata;
import org.bukkit.Material;
import org.bukkit.block.Block;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ArenaMethods {

    static HashMap<Block, Material> regenMarkedMaterial = new HashMap<>();
    static HashMap<Block,Location> regenMarkedLocation = new HashMap<>();

    public static Set<String> getArenas () {
        Set<String> keys = Arenadata.get().getConfigurationSection("arena").getKeys(false);
        return keys;}

    public static String findPlayerArena (Player p) {
        Set<String> arenas = getArenas();
        for (String arenaName : arenas) {
            if (p.getLocation().distance((Location) Arenadata.get().get("arena." + arenaName + ".center")) <= (double) Arenadata.get().get("arena." + arenaName + ".size")) {
                return arenaName;
            }
        }
        return null;
    }

    public static void regenerateArena (String arenaName) {
        for (int i = 0; i < regenMarkedMaterial.size(); i++) {
            if (regenMarkedLocation.get(i).distance((Location)Arenadata.get().get("arena." + arenaName + ".center")) < (double)Arenadata.get().get("arena." + arenaName + ".size")) {
            if (regenMarkedMaterial.get(i) != regenMarkedLocation.get(1).getBlock().getType()) {
                regenMarkedLocation.get(i).getBlock().setType(regenMarkedMaterial.get(i));
                regenMarkedLocation.remove(i);
                regenMarkedMaterial.remove(i);
            }
            }
        }

    }





    public static ArrayList<Location> getArenaLocations (String arenaName) {
        ArrayList<Location> arenaLocations = new ArrayList<>();
        arenaLocations.add((Location)Arenadata.get().get("arena." + arenaName + ".team1"));
        arenaLocations.add((Location)Arenadata.get().get("arena." + arenaName + ".team2"));
        arenaLocations.add((Location)Arenadata.get().get("arena." + arenaName + ".center"));
    return arenaLocations;}
}
