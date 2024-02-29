package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.Material;
import org.bukkit.block.Block;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaMethods {

    public static ArrayList<String> arenasInUse = new ArrayList<>();

    public static ArrayList<Block> liquidFreeze = new ArrayList<>();

    public static Set<String> getArenas () {
        Set<String> keys = Arenadata.get().getConfigurationSection("arenas").getKeys(false);
        return keys;}

    public static String getSuitableArena () {
        Set<String> arenaList = getArenas();

        for (String arena : arenaList) {
            if (DuelMethods.playersInMap.get(arena) == null || DuelMethods.playersInMap.get(arena).size() == 0) {
                if (!arenasInUse.contains(arena)) {
                    return arena;
                }
            }
        }
    return null;}



    public static String findPlayerArena (Player p) {
        Set<String> arenas = getArenas();
        for (String arenaName : arenas) {
            if (LocationIsInsideArena(p.getLocation(),arenaName)) {
                return arenaName;
            }
        }
        return null;
    }

    public static void regenerateArena (String arenaName) {
            Location center = Arenadata.get().getLocation("arenas." + arenaName + ".center");
            TerrainArena.generateTerrain(center.getWorld(),center,4,null,null);
    }

    public static boolean LocationIsInsideArena (Location loc,String arenaName) {
        Region arenaRegion = (Region) Arenadata.get().get("arenas." + arenaName + ".region");
        if (arenaRegion.contains(loc)) {
            return true;
        }
        return false;
    }
}
