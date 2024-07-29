package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.party.Party;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class QueueInventory {



    public static void queueInventory(Player p) {

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.2F, 0.5F);

        Inventory inventory = Bukkit.createInventory(p, 27, "Queue");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
        }
        Party party = C.partyManager.findPartyForMember(p.getUniqueId());
        boolean partyExists = false;
        int partySize = 1;
        if (party != null) {
            partyExists = true;
            partySize = party.getMembers().size();
        }

        for (QueueEnum item : QueueEnum.values()) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }
                int amount = 1;
            String c = ChatColor.getLastColors(C.t(item.getDisplayName()));
                if (item.getlocalName().contains("CASUAL") || item.getlocalName().contains("RATED") || item.getlocalName().contains("UNRATED")) {
                    lore.add("");

                    if (partyExists) {

                        if (partySize > 2) {
                            lore.add(C.t("&7⏵ SOLO"));
                            lore.add(C.t("&7⏵ DUO (PARTY)"));
                            lore.add(C.t("&4❌ &cparty is too large"));
                        } else {
                            lore.add(C.t("&7⏵ SOLO"));
                            lore.add(c+"⏵ DUO (PARTY)");
                        }

                    } else {
                        lore.add(c+"⏵ SOLO");
                        lore.add(C.t("&7⏵ DUO (PARTY)"));
                    }
                }
                if (item.getlocalName().contains("RATED")) {
                    lore.add("");
                    String strings[] =  item.getlocalName().split("-");
                    String kitType = strings[0].toLowerCase();
                    amount = Queue.queue.get(item.getlocalName()).size()+1;
                    Map<Integer, UUID> placements = DuelsDB.getPlacements("stat_elo_"+kitType);
                    int numEntries = 10;
                    if (placements.keySet().size() < 10) {
                        numEntries = placements.keySet().size();
                    }
                    for (int i = 0; i < numEntries; i++) {
                        lore.add(C.t("&7#"+(i+1)+"&8⏵ &f"+Bukkit.getOfflinePlayer(placements.get(i+1)).getName() + " &7["+c+ DuelsDB.readPlayer(placements.get(i+1),"stat_elo_"+kitType)+"&7]"));
                    }

                }
                if (item.getlocalName().equalsIgnoreCase("career")) {
                    ArrayList<String> career = new ArrayList<String>();
                    if (Userdata.get().get(p.getUniqueId()+".career") != null) {
                        career = (ArrayList<String>) Userdata.get().get(p.getUniqueId()+".career");
                    }
                    int numEntries = 10;
                    if (career.size() <= 10) {
                        numEntries = career.size();
                    }
                    for (int i = 0; i < numEntries; i++) {
                        lore.add(career.get(i));
                    }
                }
                String itemName = C.t(item.getDisplayName());
                String[] realLore = lore.toArray(new String[0]);
                inventory.setItem(item.getInventorySlot(), C.createItemStack(itemName, item.getMaterial(), amount, realLore, null, null,item.getlocalName(),null));
        }

        p.openInventory(inventory);

    }

}
