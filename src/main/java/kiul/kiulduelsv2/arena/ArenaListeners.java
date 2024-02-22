package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.duel.DuelListeners;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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


}
