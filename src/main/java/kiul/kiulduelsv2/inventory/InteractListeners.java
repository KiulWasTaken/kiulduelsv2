package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.*;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kiul.kiulduelsv2.C.partyManager;

public class InteractListeners implements Listener {

    @EventHandler
    public void hotbarInteractListener (PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (ClickMethods.inEditor.contains(e.getPlayer())) {
            if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                e.setCancelled(true);
                ItemInventory.itemInventory(p);
                return;
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                e.setCancelled(true);
                EnchantInventory.itemEnchantInventory(p);
                return;
            }
        }
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                        switch (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                            case "partysplit":
                                e.getPlayer().getInventory().getItemInMainHand().getItemMeta().setDisplayName(ChatColor.LIGHT_PURPLE + "Party FFA");
                                e.getPlayer().getInventory().getItemInMainHand().getItemMeta().setLocalizedName("partyffa");
                                e.getPlayer().getInventory().getItemInMainHand().setType(Material.PINK_DYE);
                                break;
                            case "partyffa":
                                e.getPlayer().getInventory().getItemInMainHand().getItemMeta().setDisplayName(ChatColor.LIGHT_PURPLE + "Party Split");
                                e.getPlayer().getInventory().getItemInMainHand().getItemMeta().setLocalizedName("partysplit");
                                e.getPlayer().getInventory().getItemInMainHand().setType(Material.MAGENTA_DYE);
                                break;
                        }
                    }
                }
            }
        }
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null) {
                    if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                        Party party = partyManager.findPartyForMember(p.getUniqueId());
                        UUID uuid = p.getUniqueId();
                        switch (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"), PersistentDataType.STRING)) {
                            case "queue":
                                e.setCancelled(true);
                                break;
                            case "leavegame":
                                UtilMethods.becomeNotSpectator(p);
                                UtilMethods.teleportLobby(p);
                                break;
                            case "kiteditor":
                                e.setCancelled(true);
                                KitInventory.kitInventory(e.getPlayer());
                                break;
                            case "partysplit":
                                DuelMethods.startPartyDuel(ArenaMethods.getSuitableArena(), party.getMembers(), party.teamOne(), party.teamTwo(), false);
                                break;
                            case "partyffa":
                                DuelMethods.startPartyDuel(ArenaMethods.getSuitableArena(), party.getMembers(), null, null, true);
                                break;
                            case "partyteam":
                                e.setCancelled(true);
                                party.changeTeam(p.getUniqueId());
                                List<String> lore = new ArrayList<>();
                                lore.add(ChatColor.GRAY + "Right-Click to change party team");
                                if (party.teamOne().contains(p)) {
                                    p.getInventory().setItemInMainHand(ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "RED", Material.RED_WOOL, 1, lore, null, null, "partyteam"));
                                } else {
                                    p.getInventory().setItemInMainHand(ItemStackMethods.createItemStack(ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE", Material.BLUE_WOOL, 1, lore, null, null, "partyteam"));
                                }
                                break;
                            case "leavequeue":
                                e.setCancelled(true);
                                try {
                                    KitMethods.lobbyKit(e.getPlayer());
                                    for (String types : Queue.queue.keySet()) {
                                        if (Queue.queue.get(types).contains(p)) {
                                            Queue.queue.get(types).remove(p);
                                        }
                                    }
                                } catch (IOException r) {
                                    r.printStackTrace();
                                }
                                break;
                            case "partydisband":
                                if (partyManager.findPartyForMember(uuid) != null) {
                                    if (partyManager.findPartyForMember(uuid).isLeader(uuid)) {
                                        ArrayList<Player> members = new ArrayList<>();
                                        for (UUID memberUUIDs : partyManager.findPartyForMember(uuid).getMembers()) {
                                            if (Bukkit.getPlayer(memberUUIDs) != null) {
                                                Player member = Bukkit.getPlayer(memberUUIDs);
                                                member.sendMessage(C.t("&#e04aac&o" + Bukkit.getPlayer(partyManager.findPartyForMember(uuid).getLeader()).getDisplayName() + "'s party has been disbanded"));
                                                members.add(member);
                                            }
                                        }
                                        members.add(Bukkit.getPlayer(partyManager.findPartyForMember(uuid).getLeader()));
                                        Bukkit.getPlayer(partyManager.findPartyForMember(uuid).getLeader()).sendMessage(C.t("&#e04aac&o" + "your party has been successfully disbanded"));
                                        partyManager.disbandParty(partyManager.findPartyForMember(uuid));
                                        for (Player member : members) {
                                            try {
                                                KitMethods.lobbyKit(member);
                                            } catch (IOException err) {
                                                err.printStackTrace();
                                            }
                                        }
                                    }
                                }
                                break;
                            case "leaveparty":
                                if (partyManager.findPartyForMember(uuid) != null) {
                                    if (!partyManager.findPartyForMember(uuid).isLeader(uuid)) {
                                        party.removeMember(uuid);
                                        try {
                                            KitMethods.lobbyKit(Bukkit.getPlayer(uuid));
                                        } catch (IOException err) {
                                            err.printStackTrace();
                                        }
                                    }
                                }
                                break;

                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        if (ClickMethods.inEditor.contains(p)) {
            event.getItemDrop().remove();
        }
    }
}
