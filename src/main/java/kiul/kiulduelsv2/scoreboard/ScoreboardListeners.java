package kiul.kiulduelsv2.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardListeners implements Listener {

    @EventHandler
    public void applyScoreboardOnLogin (PlayerJoinEvent e) {
        ScoreboardMethods.startLobbyScoreboardTask(e.getPlayer());
    }
}
