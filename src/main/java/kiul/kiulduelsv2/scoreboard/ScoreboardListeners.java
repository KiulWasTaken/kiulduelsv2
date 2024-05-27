package kiul.kiulduelsv2.scoreboard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ScoreboardListeners implements Listener {

    @EventHandler
    public void applyScoreboardOnLogin (PlayerJoinEvent e) {
        e.getPlayer().setScoreboard(ScoreboardMethods.lobbyScoreboard(e.getPlayer()));
    }
}
