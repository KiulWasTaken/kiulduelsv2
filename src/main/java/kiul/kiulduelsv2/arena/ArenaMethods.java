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
        double pX = p.getLocation().getX();
        double pZ = p.getLocation().getZ();
        Location playerLocation = new Location(p.getWorld(),pX,0,pZ);


        for (String arenaName : arenas) {
            double aX = Arenadata.get().getLocation("arenas." + arenaName + ".center").getX();
            double aZ = Arenadata.get().getLocation("arenas." + arenaName + ".center").getZ();
            Location arenaLocation = new Location(p.getWorld(),aX,0,aZ);
            double sideLength = Math.pow(((Arenadata.get().getDouble("arenas." + arenaName + ".size")*2-1)*16),2);
            double maxDistance = (Math.sqrt(sideLength*2)/2)+2;
            if (playerLocation.distance(arenaLocation) <= maxDistance) {
                return arenaName;
            }
        }
        return null;
    }

    public static void regenerateArena (String arenaName) {
            Location center = Arenadata.get().getLocation("arenas." + arenaName + ".center");
            TerrainArena.generateTerrain(center.getWorld(),center,4,null,null);



    }





    public static ArrayList<Location> getArenaLocations (String arenaName) {
        ArrayList<Location> arenaLocations = new ArrayList<>();
        arenaLocations.add((Location)Arenadata.get().get("arenas." + arenaName + ".team1"));
        arenaLocations.add((Location)Arenadata.get().get("arenas." + arenaName + ".team2"));
        arenaLocations.add((Location)Arenadata.get().get("arenas." + arenaName + ".center"));
    return arenaLocations;}
}
