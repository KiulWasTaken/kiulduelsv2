package kiul.kiulduelsv2.gui;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.party.Party;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class QueueInventory {



    public static void queueInventory(Player p) {

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.2F, 0.5F);

        Inventory inventory = Bukkit.createInventory(p, 27, "Queue");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {

            if (i <= 14) {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
            } else if (i <= 17) {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
            } else {
                inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null,null));
            }
        }
        Party party = C.partyManager.findPartyForMember(p.getUniqueId());
        boolean partyExists = false;
        int partySize = party.getMembers().size();
        if (party != null) {
            partyExists = true;
        }

        for (QueueEnum item : QueueEnum.values()) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }

                if (partyExists) {

                    if (partySize > 2) {
                        lore.add(C.t("&7▸ SOLO"));
                        lore.add(C.t("&7▸ DUO (PARTY)"));
                        lore.add(C.t("&4❌ &cparty is too large"));
                    } else {
                        lore.add(C.t("&7▸ SOLO"));
                        lore.add(C.t("&#FF9000▸ &#FFB600D&#FFDB00U&#FFDE06O &#FFBE13(&#FF9E20P&#FFC02EA&#FFE23BR&#FFE542T&#FFC942Y&#FFAD42)"));
                    }

                } else {
                    lore.add(C.t("&#FF9000▸ &#FFD700S&#FFC610O&#FFB329L&#FFF342O"));
                    lore.add(C.t("&7▸ DUO (PARTY)"));
                }
                String itemName = ItemStackMethods.translateHexColorCodes("&#","",item.getDisplayName());


                inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(C.t(itemName), item.getMaterial(), 1, lore, null, null,item.getlocalName()));
        }

        p.openInventory(inventory);

    }

}
