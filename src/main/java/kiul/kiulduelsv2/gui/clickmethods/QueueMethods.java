package kiul.kiulduelsv2.gui.clickmethods;

import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.entity.Player;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueueMethods {



    public static void queueAddCheck (ArrayList<Player> queue,Player p,String type) {
        p.closeInventory();
        try {
            KitMethods.loadGlobalKit(p, "queue");
        } catch (IOException e) {e.printStackTrace();}

        if (!queue.contains(p)) {
            queue.add(p);
            if (queue.size() >= 2) {
                List<Player> players = queue.subList(0,1);

                for (Player playersInQueue : players) {
                    queue.remove(playersInQueue);
                }

                if (type.contains("ARCADE")) {
                    String[] strings = type.split("-");
                    String kit = strings[0];
                    // startArcadeDuel(map,kit,players);

                } else {

                    // startRealisticDuel(map,type,players);
                }

            }
        }
    }
}
