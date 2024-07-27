package kiul.kiulduelsv2.party;

import kiul.kiulduelsv2.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Party {
    public static void sendPartyMessage(String message,Player recipient) {

        recipient.sendMessage(C.t("&d&m                                                                "));
        recipient.sendMessage(C.t("&7&o"+message));
        recipient.sendMessage(C.t("&d&m                                                                "));
    }
    private UUID leader;
    private List<UUID> members;
    private List<UUID> teamOne;
    private List<UUID> teamTwo;

    public static HashMap<UUID,UUID> invitedPlayer = new HashMap<>();

    public Party(UUID leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
        this.teamOne = new ArrayList<>();
        this.teamTwo = new ArrayList<>();
        teamOne.add(leader);
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }
    public List<UUID> getMembersInclusive() {
        List<UUID> membersInclusive = new ArrayList<>();
        membersInclusive.add(leader);
        membersInclusive.addAll(members);
        return membersInclusive;
    }

    public List<Player> teamOnePlayers() {
        List<Player> teamOnePlayers = new ArrayList<>();
        for (UUID memberUUID : teamOne) {
            if (Bukkit.getServer().getPlayer(memberUUID) != null) {
                teamOnePlayers.add(Bukkit.getServer().getPlayer(memberUUID));
            }
        }
        return teamOnePlayers;
    }
    public List<Player> teamTwoPlayers() {
        List<Player> teamTwoPlayers = new ArrayList<>();
        for (UUID memberUUID : teamTwo) {
            if (Bukkit.getServer().getPlayer(memberUUID) != null) {
                teamTwoPlayers.add(Bukkit.getServer().getPlayer(memberUUID));
            }
        }
        return teamTwoPlayers;
    }

    public List<UUID> teamOne() {
        return teamOne;
    }

    public List<UUID> teamTwo() {
        return teamTwo;
    }

    public void addMember(UUID member) {
        members.add(member);
        if (teamOne.size() >= teamTwo.size()) {
            teamTwo.add(member);
        } else {
            teamOne.add(member);
        }
    }

    public void changeTeam(UUID member) {
        if (teamOne.contains(member)) {
            if (teamOne.size() > 1) {
                teamOne.remove(member);
                teamTwo.add(member);
            } else {
                Bukkit.getPlayer(member).sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Your team is too small for you to swap teams");
            }
        } else {
            if (teamTwo.size() > 1) {
                teamTwo.remove(member);
                teamOne.add(member);
            } else {
                Bukkit.getPlayer(member).sendMessage(ChatColor.RED+""+ChatColor.ITALIC+"Your team is too small for you to swap teams");
            }
        }
    }

    public void removeMember(UUID member) {
        members.remove(member);
        if (teamOne.contains(member)) {
            teamOne.remove(member);
        } else {
            teamTwo.remove(member);
        }
    }

    public boolean isMember(UUID playerName) {
        return members.contains(playerName) || leader.equals(playerName);
    }

    public boolean isLeader(UUID playerName) {
        return leader.equals(playerName);
    }

}
