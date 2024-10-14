package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpectatorListeners implements Listener {


    @EventHandler
    public void preventSpectatorHurtAlivePlayers (EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p && C.duelManager.findDuelForMember(p.getUniqueId()) != null) {
            Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
            if (duel.getSpectators().contains(p)) {
                e.setCancelled(true);
            }
        }
    }
}
