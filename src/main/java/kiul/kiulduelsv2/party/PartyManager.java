package kiul.kiulduelsv2.party;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyManager {
    private List<Party> parties;

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
        }
        return null;  // Player is not in any party
    }
}
