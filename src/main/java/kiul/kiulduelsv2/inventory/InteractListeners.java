package kiul.kiulduelsv2.inventory;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.gui.*;
import kiul.kiulduelsv2.gui.KitEditor;
import kiul.kiulduelsv2.duel.Queue;
import kiul.kiulduelsv2.gui.layout.KitInventory;
import kiul.kiulduelsv2.gui.queue.PartyQueueInventory;
import kiul.kiulduelsv2.gui.settings.SettingsInventory;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.pattychips.pattyeventv2.PattyEventV2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kiul.kiulduelsv2.C.partyManager;

public class InteractListeners implements Listener {

    @EventHandler
    public void hotbarInteractListener (PlayerInteractEvent e) {
        Player p = e.getPlayer();
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
                                p.playSound(p, Sound.ENTITY_VILLAGER_WORK_LIBRARIAN,0.8f,1f);
                                KitInventory.selectKitToEdit(e.getPlayer());
                                break;
                            case "partyqueue":
                                e.setCancelled(true);
                                List<Player> playerList = new ArrayList<>();
                                for (UUID partyUUID : party.getMembersInclusive()) {
                                    Player partyMember = Bukkit.getPlayer(partyUUID);
                                    if (partyMember != null) {
                                        playerList.add(partyMember);
                                    }
                                }
                                if (playerList.size() < 2) {
                                    if (playerList.get(0) != null) {
                                        playerList.get(0).sendMessage(C.failPrefix+"cannot start party match with less than 2 players");
                                    }
                                    return;
                                }
                                PartyQueueInventory.main(p);
                                break;
                            case "partyteam":
                                e.setCancelled(true);
                                party.changeTeam(p.getUniqueId());
                                List<String> lore = new ArrayList<>();
                                lore.add(ChatColor.GRAY + "Right-Click to change party team");
                                if (party.teamOne().contains(p.getUniqueId())) {
                                    p.getInventory().setItemInMainHand(ItemStackMethods.createItemStack(ChatColor.RED + "" + ChatColor.BOLD + "RED", Material.RED_WOOL, 1, lore, null, null, "partyteam"));
                                } else {
                                    p.getInventory().setItemInMainHand(ItemStackMethods.createItemStack(ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE", Material.BLUE_WOOL, 1, lore, null, null, "partyteam"));
                                }
                                break;
                            case "leavequeue":
                                e.setCancelled(true);
                                if (party != null) {
                                        for (UUID memberUUIDs : party.getMembersInclusive()) {
                                            if (Bukkit.getPlayer(memberUUIDs) != null) {
                                                Player pm = Bukkit.getPlayer(memberUUIDs);
                                                Party.sendPartyMessage("party queue has been cancelled by " + ChatColor.LIGHT_PURPLE+ e.getPlayer().getName(),pm);
                                                try {
                                                    KitMethods.lobbyKit(pm);
                                                    for (String types : Queue.queue.keySet()) {
                                                        if (Queue.queue.get(types).contains(pm)) {
                                                            Queue.queue.get(types).remove(pm);
                                                        }
                                                    }
                                                } catch (IOException r) {
                                                    r.printStackTrace();
                                                }
                                            }
                                        }
                                        return;
                                }
                                try {
                                    KitMethods.lobbyKit(e.getPlayer());
                                    for (String types : Queue.queue.keySet()) {
                                        Queue.queue.get(types).remove(p);
                                    }
                                } catch (IOException r) {
                                    r.printStackTrace();
                                }
                                break;
                            case "partydisband":
                                p.performCommand("party disband");
                                break;
                            case "party":
                                if (C.PAT_MODE && !PattyEventV2.sittingOut.contains(p.getUniqueId())) {
                                    p.sendMessage(C.failPrefix+"you need to sit out of events before you can create a party");
                                    return;
                                }
                                e.setCancelled(true);
                                partyManager.createParty(p.getUniqueId());
                                try {KitMethods.lobbyKit(p);} catch (IOException err) {err.printStackTrace();}
                                Party.sendPartyMessage("&7New party has been created",p);
                                break;
                            case "shop":
                                p.performCommand("shop");
                                break;
                            case "partyinfo":
                                // print party member info (nicely)
                                break;
                            case "settings":
                                e.setCancelled(true);
                                SettingsInventory.settings(p,0,Material.WHITE_STAINED_GLASS_PANE);
                                break;
                            case "leaveparty":
                                p.performCommand("party leave");
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
        if (KitEditor.inEditor.containsKey(p)) {
            event.getItemDrop().remove();
        }
    }
}
