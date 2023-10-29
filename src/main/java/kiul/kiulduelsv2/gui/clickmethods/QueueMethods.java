package kiul.kiulduelsv2.gui.clickmethods;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueueMethods {



    public static void queueAddCheck (ArrayList<Player> queue,Player p,String type) {
        Random random = new Random();
        if (!queue.contains(p)) {
            queue.add(p);
            if (queue.size() >= 2) {
                List<Player> players = queue.subList(0,1);
                queue.remove(players);
                List<String> smpArcadeMaps = ArenaMethods.getArenasOfType(type);
                DuelMethods.startDuel(players,true,false,smpArcadeMaps.get(random.nextInt(0,smpArcadeMaps.size())));
            }
        }
    }
}
