package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.gui.queue.QueueInventory;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.party.Party;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static kiul.kiulduelsv2.gui.ClickMethods.inEditor;
import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.loadGlobalKit;

public class Queue implements Listener {

    public final static HashMap<String,ArrayList<Player>> queue = new HashMap<>() {{
        put("SMP-RATED",new ArrayList<>());
        put("CART-RATED",new ArrayList<>());
        put("CRYSTAL-RATED",new ArrayList<>());
        put("SHIELD-RATED",new ArrayList<>());
        put("MAP-QUEUE",new ArrayList<>());
    }};
    public static List<String> queueTypesLowercase() {
        List<String> types = new ArrayList<>();
        for (String key : Queue.queue.keySet()) {
            String[] keys = key.split("-");
            types.add(keys[0].toLowerCase());
        }
        return types;
    }

    public static ArrayList<Player> findPlayerQueue(Player p) {
        for (ArrayList<Player> queues : queue.values()) {
            if (queues.contains(p)) {
                return queues;
            }
        }
        return null;
    }

    @EventHandler
    public void queueGuiClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) {return;}
        if (e.getView().getTitle().equalsIgnoreCase("queue")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING)) {
                String type = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING);
                if (!e.getCurrentItem().getType().equals(Material.NETHER_STAR)) {
                    String strings[] = e.getCurrentItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin, "local"), PersistentDataType.STRING).split("-");
                    String kitType = strings[0].toLowerCase();
                    if (Userdata.get().get("kits." + p.getUniqueId() + "." + kitType + ".kit-slot-" + kitSlot.get(p).get(kitType)) != null) {

                        boolean rated = false;
                        if (type.toLowerCase().contains("rated")) {
                            rated = true;
                        }
                        p.closeInventory();
                        if (p.getUniqueId() == null) {return;}
                        Party party = C.partyManager.findPartyForMember(p.getUniqueId());
                        if (party != null) {
                            for (UUID partyMember : party.getMembers()) {
                                if (Bukkit.getPlayer(partyMember) != null) {
                                    Player pm = Bukkit.getPlayer(partyMember);
                                    if (inEditor.containsKey(pm)) {
                                        p.sendMessage(C.t("&c&oCannot enter queue whilst party member is editing their kit!"));
                                        return;
                                    }
                                }
                            }
                            for (UUID partyMember : party.getMembers()) {
                                if (Bukkit.getPlayer(partyMember) != null) {
                                    Player pm = Bukkit.getPlayer(partyMember);
                                    Party.sendPartyMessage(C.t("your party has joined queue for &d" + type.toUpperCase()),pm);
                                    try {
                                        loadGlobalKit(pm, "queue");
                                    } catch (IOException err) {
                                        err.printStackTrace();
                                    }
                                }
                            }
                        }

                        joinQueue(p, type, rated);
                        p.playSound(p, Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1f, 0.4f);

                        try {
                            loadGlobalKit(p, "queue");
                        } catch (IOException err) {
                            err.printStackTrace();
                        }

                    } else {
                        p.closeInventory();
                        p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Selected kit slot (" + KitMethods.kitSlot.get(p).get(kitType) + ") is empty!");
                    }
                } else {
                    try {
                        loadGlobalKit(p, "queue");
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                    p.closeInventory();
                    for (String queueType : queue.keySet()) {
                        if (queueType.equals("MAP-QUEUE")) {continue;}
                        boolean rated = false;
                        if (queueType.toLowerCase().contains("rated")) {
                            rated = true;
                        }
                        joinQueue(p, queueType, rated);
                    }
                }
            }
        }
    }

    @EventHandler
    public void interactItem (PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) {return;}
        if (e.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(C.plugin,"local"),PersistentDataType.STRING)) {
            if (e.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(C.plugin,"local"),PersistentDataType.STRING).equalsIgnoreCase("queue")) {
                QueueInventory.queueInventory(p);
            }
        }
    }

    public void joinQueue (Player p, String type,boolean rated) {
        getQueue(type).add(p);
        String strings[] = type.split("-");
        String kitType = strings[0].toLowerCase();
        long sinceJoined = System.currentTimeMillis();
         int pElo = (int) DuelsDB.readPlayer(p.getUniqueId(),"stat_elo_"+kitType);
        List<Player> players = new ArrayList<>();
        Party party = C.partyManager.findPartyForMember(p.getUniqueId());
        if (party != null) {
            for (UUID memberUUIDs : party.getMembers()) {
                if (Bukkit.getPlayer(memberUUIDs) != null) {
                    players.add(Bukkit.getPlayer(memberUUIDs));
                }
            }
        } else {
            players.add(p);
        }

        String finalType = type;
        new BukkitRunnable() {



            @Override
            public void run() {
                if (getQueue(finalType).contains(p) && p.isOnline()) {

                    int[] times = C.splitTimestampSince(sinceJoined);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%02d:%02d:%02d", times[0], times[1], times[2])));
                    if (party != null) {
                        for (UUID partyMember : party.getMembers()) {
                            if (Bukkit.getPlayer(partyMember) != null) {
                                Player pm = Bukkit.getPlayer(partyMember);
                                pm.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%02d:%02d:%02d", times[0], times[1], times[2])));
                            }
                        }
                    }

                    if (rated) {
                        for (Player playersInQueue : getQueue(finalType)) {
                            if (playersInQueue == p) {continue;}
                            int eElo = (int) DuelsDB.readPlayer(playersInQueue.getUniqueId(), "stat_elo_"+kitType);
                            double difference = pElo - eElo;
                            if (difference < 0) {
                                difference *= -1;
                            }
                            if (difference < ((double) (System.currentTimeMillis() - sinceJoined) / 1000) * 10) {
                                Party eParty = C.partyManager.findPartyForMember(playersInQueue.getUniqueId());
                                if (eParty != null) {
                                    for (UUID memberUUIDs : eParty.getMembers()) {
                                        if (Bukkit.getPlayer(memberUUIDs) != null) {
                                            if (!players.contains(Bukkit.getPlayer(memberUUIDs))) {
                                                players.add(Bukkit.getPlayer(memberUUIDs));
                                            }
                                        }
                                    }
                                } else {
                                    if (!players.contains(playersInQueue)) {
                                        players.add(playersInQueue);
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                    } else {

                        players.clear();
                        Party party1 = C.partyManager.findPartyForMember(getQueue(finalType).get(0).getUniqueId());
                        Party party2 = C.partyManager.findPartyForMember(getQueue(finalType).get(1).getUniqueId());
                        if (party1 != null && party2 != null) {
                            for (UUID memberUUIDs : party1.getMembers()) {
                                if (Bukkit.getPlayer(memberUUIDs) != null) {
                                    players.add(Bukkit.getPlayer(memberUUIDs));
                                }
                            }
                            for (UUID memberUUIDs : party2.getMembers()) {
                                if (Bukkit.getPlayer(memberUUIDs) != null) {
                                    players.add(Bukkit.getPlayer(memberUUIDs));
                                }
                            }
                        } else {
                            players.add(getQueue(finalType).get(0));
                            players.add(getQueue(finalType).get(1));
                        }
                    }


                    if (players.size() > 1) {
                        String arena = ArenaMethods.getSuitableArena();
                        for (Player duelMembers : players) {
                            for (String type : queue.keySet()) {
                                if (getQueue(type).contains(duelMembers)) {
                                    getQueue(type).remove(duelMembers);
                                }
                            }
                        }
                        if (arena != null) {

                            DuelMethods.startRealisticDuel(players, arena, false, rated,kitType);
                            cancel();
                        } else {
                            for (Player duelMembers : players) {
                                for (String type : queue.keySet()) {
                                    if (getQueue(type).contains(duelMembers)) {
                                        getQueue(type).remove(duelMembers);
                                    }
                                    Queue.queue.get("MAP-QUEUE").add(duelMembers);
                                }
                            }

                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    boolean next = false;
                                    for (Player duelMembers : players) {
                                        int[] times = C.splitTimestampSince(sinceJoined);
                                        duelMembers.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + String.format("%02d:%02d:%02d", times[0], times[1], times[2])));
                                        if (!duelMembers.isOnline()) {
                                            Queue.queue.get("MAP-QUEUE").remove(duelMembers);
                                        }
                                        if (!Queue.queue.get("MAP-QUEUE").contains(duelMembers)) {
                                            for (Player online : players) {
                                                Queue.queue.get("MAP-QUEUE").remove(online);
                                                if (online.isOnline()) {
                                                    try {
                                                        KitMethods.lobbyKit(online);
                                                    } catch (IOException err) {
                                                        err.printStackTrace();
                                                    }
                                                }
                                            }
                                            players.clear();
                                            cancel();
                                            return;
                                        }
                                        if (Queue.queue.get("MAP-QUEUE").get(0) == duelMembers) {
                                            next = true;
                                        }
                                    }

                                    String arena = ArenaMethods.getSuitableArena();
                                    if (arena != null && next) {
                                        DuelMethods.startRealisticDuel(players, arena, false, rated,kitType);
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(C.plugin, 0, 20);
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(C.plugin,0,20);
    }



    public ArrayList<Player> getQueue (String type) {
        return queue.get(type);
    }
}
