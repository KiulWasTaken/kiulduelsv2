package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.config.Arenadata;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Set;

public class ArenaMethods {

    public static Set<String> getArenas () {
        Set<String> keys = Arenadata.get().getConfigurationSection("arenas").getKeys(false);
        return keys;}



    public static void regenerateArena (String arenaName) {

    }





    public static ArrayList<Location> getArenaLocations (String arenaName) {
        ArrayList<Location> arenaLocations = new ArrayList<>();
        arenaLocations.add((Location)Arenadata.get().get("arena." + arenaName + ".team1"));
        arenaLocations.add((Location)Arenadata.get().get("arena." + arenaName + ".team2"));
        arenaLocations.add((Location)Arenadata.get().get("arena." + arenaName + ".center"));
    return arenaLocations;}
}
