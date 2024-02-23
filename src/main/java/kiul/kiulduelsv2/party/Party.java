package kiul.kiulduelsv2.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Party {
    private UUID leader;
    private List<UUID> members;

    public static HashMap<UUID,Party> invitedPlayer = new HashMap<>();

    public Party(UUID leader) {
        this.leader = leader;
        this.members = new ArrayList<>();
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID member) {
        members.add(member);
    }

    public void removeMember(UUID member) {
        members.remove(member);
    }

    public boolean isMember(UUID playerName) {
        return members.contains(playerName) || leader.equals(playerName);
    }

    public boolean isLeader(UUID playerName) {
        return leader.equals(playerName);
    }

}
