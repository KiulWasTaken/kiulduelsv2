package kiul.kiulduelsv2.gui.clickevents;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.clickmethods.QueueMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.loadGlobalKit;

public class QueueClickEvent implements Listener {

    public static ArrayList<Player> SMPArcadeQueue = new ArrayList<>();
    public static ArrayList<Player> SMPRealistic1v1Queue = new ArrayList<>();
    public static ArrayList<Player> SMPRealistic2v2Queue = new ArrayList<>();

    public static ArrayList<Player> CrystalArcadeQueue = new ArrayList<>();
    public static ArrayList<Player> CrystalRealistic1v1Queue = new ArrayList<>();
    public static ArrayList<Player> CrystalRealistic2v2Queue = new ArrayList<>();

    public static ArrayList<Player> AxeQueue = new ArrayList<>();
    public static ArrayList<Player> DiamondPotQueue = new ArrayList<>();
    public static ArrayList<Player> KoHiQueue = new ArrayList<>();

    @EventHandler
    public void queueGuiClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("queue")) {
            e.setCancelled(true);
            // if (KitMethods.kitMatchesCriteria(kit, playerKitSlot) {
            // proceed
            // } else {
            // "you dickhead!"
            // }

            // FORMAT: Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p))
                try {loadGlobalKit(p, "queue");
                } catch (IOException err) {

                }
                switch (e.getCurrentItem().getItemMeta().getLocalizedName()) {
                    case "arcadeSMP":
                        QueueMethods.queueAddCheck(SMPArcadeQueue,p,"SMP-CLASSIC",true,"smp");
                        break;
                    case "realSMP1":
                        QueueMethods.queueAddCheck(SMPRealistic1v1Queue,p,"SMP-REALISTIC",false,null);
                        break;
                    case "realSMP2":
                        QueueMethods.queueAddCheck(SMPRealistic2v2Queue,p,"SMP-REALISTIC",false,null);
                        break;
                    case "arcadeCrystal":
                        QueueMethods.queueAddCheck(CrystalArcadeQueue,p,"CRYSTAL-CLASSIC",true,"crystal");
                        break;
                    case "realCrystal1":
                        QueueMethods.queueAddCheck(CrystalRealistic1v1Queue,p,"CRYSTAL-REALISTIC",false,null);
                        break;
                    case "realCrystal2":
                        QueueMethods.queueAddCheck(CrystalRealistic2v2Queue,p,"CRYSTAL-REALISTIC",false,null);
                        break;
                    case "axe":
                        QueueMethods.queueAddCheck(AxeQueue,p,"DEFAULT",true,"axe");
                        break;
                    case "glass":
                    default:
                        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING,0.1f,0.7f);
            }
        }
    }
}
