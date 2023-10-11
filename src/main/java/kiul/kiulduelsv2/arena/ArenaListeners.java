package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.config.Arenadata;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

import static kiul.kiulduelsv2.arena.ArenaMethods.*;

public class ArenaListeners implements Listener {


    @EventHandler
    public void blockLoggerPlace (BlockPlaceEvent e) {
        if (getArenas() != null) {
       Set<String> arenas = getArenas();
            double bX = e.getBlock().getX();
            double bZ = e.getBlock().getZ();
            Location blockLocation = new Location(e.getBlock().getWorld(),bX,0,bZ);
       for (String arenaName : arenas) {
           if (blockLocation.distance((Location) Arenadata.get().get("arena." + arenaName + ".center")) <= (double)Arenadata.get().get("arena." + arenaName + ".size")) {
               regenMarkedMaterial.put(e.getBlock(), e.getBlock().getType());
               regenMarkedLocation.put(e.getBlock(), e.getBlock().getLocation());
                }
           }
       }
    }
    @EventHandler
    public void blockLoggerExplode (BlockExplodeEvent e) {
        if (getArenas() != null) {
            Set<String> arenas = getArenas();
            double bX = e.getBlock().getX();
            double bZ = e.getBlock().getZ();
            Location blockLocation = new Location(e.getBlock().getWorld(),bX,0,bZ);
            for (String arenaName : arenas) {
                if (blockLocation.distance((Location) Arenadata.get().get("arena." + arenaName + ".center")) <= (double) Arenadata.get().get("arena." + arenaName + ".size")) {
                    regenMarkedMaterial.put(e.getBlock(), e.getBlock().getType());
                    regenMarkedLocation.put(e.getBlock(), e.getBlock().getLocation());
                }
            }
        }
    }

    @EventHandler
    public void blockLoggerBreak (BlockBreakEvent e){
            if (getArenas() != null) {
                Set<String> arenas = getArenas();
                double bX = e.getBlock().getX();
                double bZ = e.getBlock().getZ();
                Location blockLocation = new Location(e.getBlock().getWorld(),bX,0,bZ);
                for (String arenaName : arenas) {
                    if (blockLocation.distance((Location) Arenadata.get().get("arena." + arenaName + ".center")) <= (double) Arenadata.get().get("arena." + arenaName + ".size")) {
                        regenMarkedMaterial.put(e.getBlock(), e.getBlock().getType());
                        regenMarkedLocation.put(e.getBlock(), e.getBlock().getLocation());
                    }
                }
            }
        }
    }
