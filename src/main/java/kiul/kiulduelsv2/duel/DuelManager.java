package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.duel.Duel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DuelManager {

    private static List<Duel> duels;

    public DuelManager() {
        this.duels = new ArrayList<>();
    }

    public Duel createDuel(List<UUID> redTeam, List<UUID> blueTeam, boolean rated,boolean ffa,String arena,String kitType) {
        Duel duel = new Duel(redTeam,blueTeam,rated,ffa,arena,kitType);
        duels.add(duel);
        return duel;
    }

    public void disbandDuel(Duel duel) {
        duels.remove(duel);
    }

    public Duel findDuelForMember(UUID playerName) {
        for (Duel duel : duels) {
            if (duel.contains(playerName)) {
                return duel;
            }
        }
        return null;
    }
}
