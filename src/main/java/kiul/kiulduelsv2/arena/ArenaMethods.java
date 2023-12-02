package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.Material;
import org.bukkit.block.Block;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class ArenaMethods {

    static HashMap<Block, Material> regenMarkedMaterial = new HashMap<>();
    static HashMap<Block,Location> regenMarkedLocation = new HashMap<>();
    public static ArrayList<String> validMapTypes = new ArrayList<>() { {
        add("SMP");
        add("REALISTIC");
        add("CRYSTAL");
        add("DEFAULT");
    }};

    public static Set<String> getArenas () {
        Set<String> keys = Arenadata.get().getConfigurationSection("arenas").getKeys(false);
        return keys;}

    public static String getArenaOfType (String type) {
        Set<String> arenaList = getArenas();
        ArrayList<String> arenasOfType = new ArrayList<>();
        for (String arenas : arenaList) {
            if (Arenadata.get().getString("arenas." + arenas + ".type").equalsIgnoreCase(type)) {
                arenasOfType.add(arenas);
            }
        }
        for (String arena : arenaList) {
            if (DuelMethods.playersInMap.get(arena) == null) {
                return arena;
            } else if (DuelMethods.playersInMap.get(arena).size() == 0) {
                return arena;
            }
        }
    return null;}



    public static String findPlayerArena (Player p) {
        Set<String> arenas = getArenas();
        double pX = p.getLocation().getX();
        double pZ = p.getLocation().getZ();
        Location playerLocation = new Location(p.getWorld(),pX,0,pZ);
        for (String arenaName : arenas) {
            if (playerLocation.distance((Location) Arenadata.get().get("arenas." + arenaName + ".center")) <= (double) Arenadata.get().get("arenas." + arenaName + ".size")) {
                return arenaName;
            }
        }
        return null;
    }

    public static void regenerateArena (String arenaName) {
        if (Arenadata.get().getString("arenas." + arenaName + ".type").contains("REALISTIC")) {
            Location corner1 = (Location)Arenadata.get().get("arenas."+arenaName+".corner1");
            Location corner2 = (Location)Arenadata.get().get("arenas."+arenaName+".corner2");
            TerrainArena.generateTerrain(corner2.getWorld(),corner1,corner2,5);
        } else {
        for (int i = 0; i < regenMarkedMaterial.size(); i++) {
            if (regenMarkedLocation.get(i).distance((Location)Arenadata.get().get("arenas." + arenaName + ".center")) < (double)Arenadata.get().get("arenas." + arenaName + ".size")) {
                if (regenMarkedMaterial.get(i) != regenMarkedLocation.get(1).getBlock().getType()) {
                    regenMarkedLocation.get(i).getBlock().setType(regenMarkedMaterial.get(i));
                    regenMarkedLocation.remove(i);
                    regenMarkedMaterial.remove(i);
                }
            }
            }
        }

    }





    public static ArrayList<Location> getArenaLocations (String arenaName) {
        ArrayList<Location> arenaLocations = new ArrayList<>();
        arenaLocations.add((Location)Arenadata.get().get("arenas." + arenaName + ".team1"));
        arenaLocations.add((Location)Arenadata.get().get("arenas." + arenaName + ".team2"));
        arenaLocations.add((Location)Arenadata.get().get("arenas." + arenaName + ".center"));
    return arenaLocations;}
}
