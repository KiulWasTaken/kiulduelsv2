package kiul.kiulduelsv2.gui.clickevents;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickMethods {



    public static void queueAddCheck (ArrayList<Player> queue,Player p,String type) {
        p.closeInventory();
        try {
            KitMethods.loadGlobalKit(p, "queue");
        } catch (IOException e) {e.printStackTrace();}

        if (!queue.contains(p)) {
            queue.add(p);
            if (queue.size() >= 2) {
                List<Player> players = new ArrayList<>() {{
                    add(queue.get(0));
                    add(queue.get(1));
                }};

                for (Player playersInQueue : players) {
                    queue.remove(playersInQueue);
                }

                if (type.contains("CLASSIC")) {
                    String[] strings = type.split("-");
                    String kit = strings[0];
                    // startArcadeDuel(map,kit,players);


                } else {
                    DuelMethods.startRealisticDuel(players, ArenaMethods.getSuitableArena());
                    // startRealisticDuel(map,type,players);
                }

            }
        }
    }

    public static void enterKitEditor (Player p) {

    }
}
