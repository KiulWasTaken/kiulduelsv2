package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Duel {

    private boolean rated;
    private boolean ffa;
    private String arena;
    private List<UUID> allContained;
    private List<UUID> players;
    private List<UUID> blueTeam;
    private List<UUID> redTeam;

    private List<UUID> spectators;
    public Duel(List<UUID> redTeam, List<UUID> blueTeam, boolean rated,boolean ffa,String arena) {
        this.players = new ArrayList<>(redTeam);
        players.addAll(blueTeam);
        this.spectators = new ArrayList<>();
        this.rated = rated;
        this.arena = arena;
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.ffa = ffa;
        this.allContained = players;
    }

    public List<UUID> getPlayers() {
        return players;
    }
    public boolean contains(UUID playerUUID) {
        if (players.contains(playerUUID)) {
            return true;
        }
        return false;
    }

    public List<UUID> getBlueTeam() {
        return blueTeam;
    }

    public List<UUID> getRedTeam() {
        return redTeam;
    }
    public void remove(UUID playerUUID) {
        if (blueTeam.contains(playerUUID)) {
            blueTeam.remove(playerUUID);
        }
        if (redTeam.contains(playerUUID)) {
            redTeam.remove(playerUUID);
        }
        if (players.contains(playerUUID)) {
            players.remove(playerUUID);
        }
        if (spectators.contains(playerUUID)) {
            spectators.remove(playerUUID);
        }
        if (allContained.contains(playerUUID)) {
            allContained.remove(playerUUID);
        }
    }
    public void killPlayer(UUID playerUUID) {
        if (blueTeam.contains(playerUUID)) {
            blueTeam.remove(playerUUID);
        }
        if (redTeam.contains(playerUUID)) {
            redTeam.remove(playerUUID);
        }
        players.remove(playerUUID);
        spectators.add(playerUUID);

        try {
            KitMethods.spectatorKit(Bukkit.getPlayer(playerUUID));
        } catch (IOException er) {
            er.printStackTrace();
        }
    }

    public void addPlayer(UUID playerUUID) {
        players.add(playerUUID);
    }

    public List<UUID> getSpectators() {
        return spectators;
    }
    public void removeSpectator(UUID playerUUID) {
        spectators.remove(playerUUID);
    }
    public void addSpectator(UUID playerUUID) {
        spectators.add(playerUUID);
    }

    public boolean isRated() {
        return rated;
    }
    public String getArena() {
        return arena;
    }

    public boolean isFfa() {
        return ffa;
    }

    public List<UUID> getAllContained() {
        return allContained;
    }
}
