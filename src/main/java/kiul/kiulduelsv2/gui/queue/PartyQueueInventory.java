package kiul.kiulduelsv2.gui.queue;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.CustomKitData;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.Invites;
import kiul.kiulduelsv2.gui.ItemStackMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.party.Party;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;

public class PartyQueueInventory implements Listener {



    public static void main(Player p) {

        Inventory inventory = Bukkit.createInventory(p, 54, "Party Games");
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackMethods.createItemStack("", Material.BLACK_STAINED_GLASS_PANE, 1, emptylore, null, null, null));
        }
    // Fill specific slots with gray stained glass pane
        int tick = 0;
        List<Party> parties = C.partyManager.getParties();
        int[] slots = {11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33};
        for (int slot : slots) {
            inventory.setItem(slot, ItemStackMethods.createItemStack("", Material.GRAY_STAINED_GLASS_PANE, 1, emptylore, null, null, null));
            if (parties.size() > tick) {
                Party party = parties.get(tick);
                Player leader = Bukkit.getPlayer(party.getLeader());
                if (leader != null) {
                String itemName = C.t("&e"+leader.getName()+"'s Party");
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY+"Click this item to send " + leader.getName() + " an");
                lore.add(ChatColor.GRAY+"invitation to a party vs. party duel.");
                lore.add("");
                lore.add(ChatColor.GRAY+"Party Members:");
                for (UUID memberUUID : party.getMembersInclusive()) {
                    if (Bukkit.getPlayer(memberUUID) != null) {
                        lore.add(C.t("&8- &7"+Bukkit.getPlayer(memberUUID).getName()));
                    }
                }


                    inventory.setItem(slot, ItemStackMethods.createSkullItem(itemName,leader,lore,"versus"));
                }
            }
            tick++;
        }

        for (PartyQueueEnum item : PartyQueueEnum.values()) {
            List<String> lore = new ArrayList<>();
            for (String itemLore : item.getLore()) {
                lore.add((itemLore));
            }

            String itemName = ItemStackMethods.translateHexColorCodes("&#", "", item.getDisplayName());
            inventory.setItem(item.getInventorySlot(), ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, item.getlocalName()));

        }
        p.openInventory(inventory);

    }

    public static void selectMode(Player p,String type) {

        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 0.2F, 0.5F);
        int invSize = 27;
        Inventory inventory = Bukkit.createInventory(p, invSize, "Party | " + type.toUpperCase());
        List<String> emptylore = new ArrayList<>();
        emptylore.add("");

        for (int i = 1; i <= 9; i++) {
            inventory.setItem(invSize - i, ItemStackMethods.createItemStack(" ", Material.BLACK_STAINED_GLASS_PANE, 1, List.of(new String[]{""}), null, null,null));
        }


        for (QueueEnum item : QueueEnum.values()) {
            if (item.getlocalName().contains("RATED")) {
                List<String> lore = new ArrayList<>();
                for (String itemLore : item.getLore()) {
                    lore.add((itemLore));
                }
                String itemName = C.t(item.getDisplayName());
                String[] strings = item.getlocalName().split("-");
                String newLocalName = strings[0].toLowerCase();

                inventory.addItem(ItemStackMethods.createItemStack(itemName, item.getMaterial(), 1, lore, null, null, newLocalName));
            }
        }

        p.openInventory(inventory);

    }


    @EventHandler
    public void onGUIClick (InventoryClickEvent e) {
        Player p = (Player)e.getView().getPlayer();
        if (e.getView().getTitle().contains("Party")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                Party party = C.partyManager.findPartyForMember(p.getUniqueId());
                String localName = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING);
                switch (localName) {
                    case "split", "ffa":
                        selectMode(p,localName);
                        break;
                    case "versus":
                        String rawItemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        String[] splitName = rawItemName.split("'");
                        if (Bukkit.getPlayer(splitName[0]) == p) {return;}
                        selectMode(p,localName+" → " + splitName[0]);
                        break;
                    case "smp","shield","crystal":

                        if (party == null) {return;}
                        for (UUID uuid : party.getMembersInclusive()) {
                            if (CustomKitData.get().get(uuid + "." + localName + ".kit-slot-" + kitSlot.get(p).get(localName)) == null) {
                                p.closeInventory();
                                p.sendMessage(C.failPrefix + Bukkit.getPlayer(uuid).getName() +"'s selected kit slot (" + KitMethods.kitSlot.get(p).get(localName) + ") for " + localName + " is empty!");
                                return;
                            }
                        }

                        if (ArenaMethods.getSuitableArena() != null) {
                        String[] strings = e.getView().getTitle().split("\\|");
                        String partyFightType = strings[1].toLowerCase().trim();
                        if (partyFightType.contains("versus")) {
                            String[] split = partyFightType.split("→");
                            p.sendMessage(Bukkit.getPlayer(split[1].trim()).getName());
                            Invites.duelInviteSend(p,Bukkit.getPlayer(split[1].trim()),true,false,localName);
                            return;
                        }
                        boolean ffa = partyFightType.equalsIgnoreCase("ffa");



                            DuelMethods.startPartyDuel(ArenaMethods.getSuitableArena(), new ArrayList<>(party.getMembersInclusive()), new ArrayList<>(party.teamOne()), new ArrayList<>(party.teamTwo()), ffa, localName);
                        } else {
                            p.sendMessage(C.t("&c&oNo arenas available"));
                        }
                        p.closeInventory();
                    default:
                        if (e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)) {
                            // send invite to owner of head
                        }
                        break;
                }
            }
        }
    }
}
