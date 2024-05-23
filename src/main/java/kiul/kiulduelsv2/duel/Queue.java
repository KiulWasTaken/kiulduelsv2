package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.StatDB;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.party.Party;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static kiul.kiulduelsv2.inventory.KitMethods.kitSlot;
import static kiul.kiulduelsv2.inventory.KitMethods.loadGlobalKit;

public class Queue implements Listener {

    public static HashMap<String,ArrayList<Player>> queue = new HashMap<>() {{
        put("SMP-CASUAL",new ArrayList<>());
        put("SMP-RATED",new ArrayList<>());
    }};

    @EventHandler
    public void queueGuiClick (InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase("queue")) {
            e.setCancelled(true);
            if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p)) != null) {
                // if (KitMethods.kitMatchesCriteria(String kit,int playerKitSlot,Player p) {
                // proceed
                // } else {
                // "you dickhead!"
                // }

                // FORMAT: Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p))


                if (e.getCurrentItem().getItemMeta().getLocalizedName() != null) {
                    String type = e.getCurrentItem().getItemMeta().getLocalizedName();
                    boolean rated = false;
                    if (type.toLowerCase().contains("rated")) {rated = true;}
                    joinQueue(p,type,rated);
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.4f);
                    try {
                        loadGlobalKit(p, "queue");
                    } catch (IOException err) {
                        err.printStackTrace();
                    }

                } else {
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 0.3f, 0.4f);
                }
            } else {
                p.closeInventory();
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Selected kit slot (" + KitMethods.kitSlot.get(p) + ") is empty!");
            }
        }
    }

    @EventHandler
    public void interactItem (PlayerInteractEvent e) {
        Player p = e.getPlayer();


        if (Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p)) != null) {
            // if (KitMethods.kitMatchesCriteria(String kit,int playerKitSlot,Player p) {
            // proceed
            // } else {
            // "you dickhead!"
            // }

            // FORMAT: Userdata.get().get("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p))


            if (p.getInventory().getItemInMainHand().getItemMeta() != null) {
                if (p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName() != null) {
                    String type = p.getInventory().getItemInMainHand().getItemMeta().getLocalizedName();
                    boolean rated = false;
                    if (type.toLowerCase().contains("rated")) {rated = true;}
                    joinQueue(p,type,rated);

                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.4f);
                    try {
                        loadGlobalKit(p, "queue");
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                } else {
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, 0.3f, 0.4f);
                }
            }
        } else {
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "Selected kit slot (" + KitMethods.kitSlot.get(p) + ") is empty!");
        }
    }


    public void joinQueue (Player p, String type,boolean rated) {
        getQueue(type).add(p);
        long sinceJoined = System.currentTimeMillis();
        int pElo = (int) StatDB.readPlayer(p.getUniqueId(),"stat_elo");
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
        new BukkitRunnable() {



            @Override
            public void run() {
                if (getQueue(type).contains(p)) {

                    int[] times = C.splitTimestampSince(sinceJoined);
                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%02d:%02d:%02d", times[0], times[1], times[2])));

                    if (rated) {
                        for (Player playersInQueue : getQueue(type)) {
                            int eElo = (int) StatDB.readPlayer(playersInQueue.getUniqueId(),"stat_elo");
                            double difference = pElo - eElo;
                            if(difference < 0) {difference *= -1;}
                            if (difference < ((System.currentTimeMillis()-sinceJoined)/1000)*10) {
                                Party eParty = C.partyManager.findPartyForMember(playersInQueue.getUniqueId());
                                if (eParty != null) {
                                    for (UUID memberUUIDs : eParty.getMembers()) {
                                        if (Bukkit.getPlayer(memberUUIDs) != null) {
                                            players.add(Bukkit.getPlayer(memberUUIDs));
                                        }
                                    }
                                } else {
                                    players.add(playersInQueue);
                                }
                                break;
                            }
                        }
                    } else {
                        if (getQueue(type).size() > 1) {
                            players.clear();
                            Party party1 = C.partyManager.findPartyForMember(getQueue(type).get(0).getUniqueId());
                            Party party2 = C.partyManager.findPartyForMember(getQueue(type).get(1).getUniqueId());
                            if (party1 != null) {
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
                                players.add(getQueue(type).get(0));
                                players.add(getQueue(type).get(1));
                            }
                        }
                    }

                    String arena = ArenaMethods.getSuitableArena();
                    if (arena != null) {
                        for (Player duelMembers : players) {
                            getQueue(type).remove(duelMembers);
                        }
                        DuelMethods.startRealisticDuel(players,arena,false);
                        cancel();
                    } else {
                        if (players.size() > 1) {
                            for (Player duelMembers : players) {
                                getQueue(type).remove(duelMembers);
                            }
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    for (Player duelMembers : players) {
                                        if (!duelMembers.isOnline()) {
                                            for (Player online : players) {
                                                if (online.isOnline()) {
                                                    try {KitMethods.lobbyKit(online);} catch (IOException err) {err.printStackTrace();}
                                                }
                                            }
                                            cancel();
                                        }
                                    }
                                    int[] times = C.splitTimestampSince(sinceJoined);
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED+String.format("%02d:%02d:%02d", times[0], times[1], times[2])));
                                    String arena = ArenaMethods.getSuitableArena();
                                    if (arena != null) {
                                        DuelMethods.startRealisticDuel(players,arena,false);
                                    }
                                }
                            }.runTaskTimer(C.plugin,0,20);
                            cancel();

                        }
                    }

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(C.plugin,0,20);
    }

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
                    String arena = ArenaMethods.getSuitableArena();
                    if (arena == null) {
                        for (Player playersInQueue : players) {
                            playersInQueue.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "No arenas available!");
                            try {
                                KitMethods.loadGlobalKit(playersInQueue, "lobby");
                            }catch (IOException r) {
                                r.printStackTrace();
                            }
                        }
                        return;
                    }
                    DuelMethods.startRealisticDuel(players, arena,false);
                    // startRealisticDuel(map,type,players);
                }

            }
        }
    }

    public ArrayList<Player> getQueue (String type) {
        return queue.get(type);
    }
}
