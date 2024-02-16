package kiul.kiulduelsv2.gui.clickevents;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickMethods {

    public static ArrayList<Player> inEditor = new ArrayList<>();


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
        inEditor.add(p);
        ItemInventory.itemInventory(p);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200000,1,false,false));
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            onlinePlayers.hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),p);
        }
        p.getInventory().clear();
        if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + KitMethods.kitSlot.get(p)) != null) {
            try {
                KitMethods.loadSelectedKitSlot(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        p.sendMessage(ChatColor.GRAY + " " + ChatColor.ITALIC + "Left-Click to open the item menu / Right-Click to open the enchant menu");
    }
}
