package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.*;
import org.bukkit.block.Block;

import org.bukkit.entity.Player;

import java.util.*;

public class ArenaMethods {

    public static HashSet<String> arenasInUse = new HashSet<>();

    public static ArrayList<Block> liquidFreeze = new ArrayList<>();

    public static Set<String> getArenas () {
        Set<String> keys = Arenadata.get().getConfigurationSection("arenas").getKeys(false);
        return keys;}

    public static String getSuitableArena () {
        Set<String> arenaList = getArenas();

        for (String arena : arenaList) {
            if (!arenasInUse.contains(arena)) {
                return arena;
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
            TerrainArena.generateTerrainPerformant(center,4);
    }

    public static boolean LocationIsInsideArena (Location loc,String arenaName) {
        Region arenaRegion = getArenaRegion(arenaName);
        if (arenaRegion.contains(loc)) {
            return true;
        }
        return false;
    }

    public static Region getArenaRegion (String arenaName) {

        Location center = Arenadata.get().getLocation("arenas." + arenaName + ".center");
        int size = Arenadata.get().getInt("arenas." + arenaName + ".size");
        World world = center.getWorld();

        Chunk SEChunk = world.getChunkAt(center.getChunk().getX()+size,center.getChunk().getZ()+size);
        Chunk NWChunk = world.getChunkAt(center.getChunk().getX()-size,center.getChunk().getZ()-size);

        Location SECorner = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 0, SEChunk.getZ() << 4).add(16, -64, 16);
        Location NWCorner = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 0, NWChunk.getZ() << 4).add(-16, 199, -16);
        Region arenaRegion = new Region(SECorner.toVector(),NWCorner.toVector());
        return arenaRegion;
    }
}
