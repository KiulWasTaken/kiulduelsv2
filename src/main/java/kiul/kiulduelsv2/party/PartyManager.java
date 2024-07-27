package kiul.kiulduelsv2.party;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyManager {
    private static List<Party> parties;

    public PartyManager() {
        this.parties = new ArrayList<>();
    }

    public Party createParty(UUID leader) {
        Party party = new Party(leader);
        parties.add(party);
        return party;
    }

    public void disbandParty(Party party) {
        parties.remove(party);
    }

    public Party findPartyForMember(UUID playerName) {
        for (Party party : parties) {
            if (party.isMember(playerName)) {
                return party;
            }
            if (party.isLeader(playerName)) {
                return party;
            }
        }
        return null;  // Player is not in any party
    }

    public List<Party> getParties() {
        return new ArrayList<>(parties);
    }
}
