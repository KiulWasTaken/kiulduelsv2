package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.duel.DuelListeners;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ArenaListeners implements Listener {


    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (ArenaMethods.liquidFreeze.contains(event.getBlock())) {
            Material liquid = event.getBlock().getType();
            if (liquid == Material.WATER || liquid == Material.LAVA) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void pearlDrop (ProjectileHitEvent e) {
        Projectile p = e.getEntity();
            if (e.getHitBlock() != null && e.getHitBlock().getType() == Material.BEDROCK) {
                e.setCancelled(true);
                double xLoc = p.getLocation().getX();
                double zLoc = p.getLocation().getZ();
                double yLoc = DuelMethods.getHighestBlockBelow(199,p.getLocation()).getY();
                Location newloc = new Location(e.getHitBlock().getLocation().getWorld(),xLoc,yLoc,zLoc);
                p.teleport(newloc);
            }
        }
    }
