package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private List<UUID> blueTeamMembers;
    private List<UUID> redTeamMembers;
    private long startTime;
    private String kitType;

    private List<UUID> spectators;
    public Duel(List<UUID> redTeam, List<UUID> blueTeam, boolean rated,boolean ffa,String arena, String kitType) {
        this.players = new ArrayList<>(redTeam);
        players.addAll(blueTeam);
        this.spectators = new ArrayList<>();
        this.rated = rated;
        this.arena = arena;
        this.redTeamMembers = new ArrayList<>(redTeam);
        this.blueTeamMembers = new ArrayList<>(blueTeam);
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.ffa = ffa;
        this.allContained = new ArrayList<>(players);
        this.startTime = System.currentTimeMillis();
        this.kitType = kitType;

//        for (UUID redTeamUUIDs : redTeamMembers) {
//            Bukkit.getPlayer(redTeamUUIDs).setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"[RED] "+ChatColor.RESET+Bukkit.getPlayer(redTeamUUIDs).getName());
//        }
//        for (UUID blueTeamUUIDs : blueTeamMembers) {
//            Bukkit.getPlayer(blueTeamUUIDs).setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"[BLUE] "+ChatColor.RESET+Bukkit.getPlayer(blueTeamUUIDs).getName());
//        }
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

    public List<UUID> getBlueTeamMembers() {
        return blueTeamMembers;
    }

    public List<UUID> getRedTeamMembers() {
        return redTeamMembers;
    }
    public List<Player> getAllBlueTeamPlayers() {
        List<Player> allBlueTeamPlayers = new ArrayList<>();
        for (UUID uuids : getBlueTeamMembers()) {
            if (Bukkit.getPlayer(uuids) != null) {
                allBlueTeamPlayers.add(Bukkit.getPlayer(uuids));
            }
        }
        return allBlueTeamPlayers;
    }
    public List<Player> getAllRedTeamPlayers() {
        List<Player> allRedTeamPlayers = new ArrayList<>();
        for (UUID uuids : getRedTeamMembers()) {
            if (Bukkit.getPlayer(uuids) != null) {
                allRedTeamPlayers.add(Bukkit.getPlayer(uuids));
            }
        }
        return allRedTeamPlayers;
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
//        Bukkit.getPlayer(playerUUID).setDisplayName(null);
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

        List<UUID> playersInDuel = getPlayers();
        for (UUID alivePlayerUUIDs : playersInDuel) {
            Bukkit.getPlayer(alivePlayerUUIDs).hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), Bukkit.getPlayer(playerUUID));
        }

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
        spectators.remove(playerUUID);allContained.remove(playerUUID);
    }
    public void addSpectator(UUID playerUUID) {
        spectators.add(playerUUID);
        allContained.add(playerUUID);
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

    public long getStartTime() {
        return startTime;
    }

    public String getKitType() {
        return kitType;
    }

    public List<UUID> getAllContained() {
        return allContained;
    }
}
