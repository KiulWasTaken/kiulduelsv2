package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelListeners;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.duel.Invites;
import kiul.kiulduelsv2.duel.Recap;
import kiul.kiulduelsv2.gui.layout.ItemEditInventory;
import kiul.kiulduelsv2.gui.layout.ItemInventory;
import kiul.kiulduelsv2.gui.queue.QueueInventory;
import kiul.kiulduelsv2.gui.ClickMethods;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.party.PartyMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

import static kiul.kiulduelsv2.C.partyManager;
import static kiul.kiulduelsv2.inventory.KitMethods.*;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        Player p = (Player) commandSender;
        Party party = partyManager.findPartyForMember(p.getUniqueId());
        switch (label) {
            case "kit":
                switch (args[0]) {
                    /*case "save":
                        saveInventoryToSelectedKitSlot(p);
                        break;
                    case "load":
                        try {loadSelectedKitSlot(p);}
                        catch (IOException error) {error.printStackTrace();}
                        break;
                    case "delete":
                        if (args[1].equalsIgnoreCase("confirm")) {
                            CustomKitData.get().set(p.getUniqueId() + ".kit-slot-" + kitSlot.get(p), null);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cAre you sure? &7Type &6/kit delete confirm &7to permanently delete the contents of your selected kit slot"));
                        }
                        break;*/
                    case "loadglobal":
                        try {
                            ItemStack[] armourContents;
                            ItemStack[] kitContents = InventoryToBase64.fromBase64(Userdata.get().getString("kits.global." + args[1] + ".inventory")).getContents();

                            if (Userdata.get().getString("kits.global." + args[1] + ".armour") != null) {
                                armourContents = InventoryToBase64.fromBase64(Userdata.get().getString("kits.global." + args[1] + ".armour")).getContents();
                                p.getInventory().setArmorContents(armourContents);
                            }
                            p.getInventory().setContents(kitContents);

                        } catch (IOException error) {
                            error.printStackTrace();
                        }
                        p.sendMessage(ChatColor.GREEN + "global kit " + ChatColor.YELLOW + args[1] + ChatColor.GREEN + " load success");
                        break;
                }
                break;
            case "arena":
                switch (args[0]) {
                    case "create":
                        if (args[1] != null) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "creating arena..");
                            long timeMillis = System.currentTimeMillis();

                            int size = 4;
                            World world = p.getWorld();
                            Chunk c = p.getLocation().getChunk();
                            Location center = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(8, 0, 8);


                            Chunk SEChunk = world.getChunkAt(center.getChunk().getX() + size, center.getChunk().getZ() + size);
                            Chunk NWChunk = world.getChunkAt(center.getChunk().getX() - size, center.getChunk().getZ() - size);


                            Location southeast = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 64, SEChunk.getZ() << 4).add(8, 0, 8);
                            Location northwest = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 64, NWChunk.getZ() << 4).add(8, 0, 8);

                            // set data
                            Arenadata.get().set("arenas." + args[1] + ".center", center);
                            Arenadata.get().set("arenas." + args[1] + ".size", size);
                            Arenadata.get().set("arenas." + args[1] + ".southeast", southeast);
                            Arenadata.get().set("arenas." + args[1] + ".northwest", northwest);

                            Arenadata.save();
                            long finalTime = System.currentTimeMillis() - timeMillis;
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "arena " + "'" + ChatColor.WHITE + args[1] + ChatColor.GRAY + "'" + " created (" + finalTime + ")");
                        } else {
                            p.sendMessage(C.failPrefix + "arena requires a name!");
                        }
                        break;
                    case "delete":
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "deleting arena..");
                        long timeMillis = System.currentTimeMillis();

                        Arenadata.get().set("arenas." + args[1], null);
                        Arenadata.save();

                        long finalTime = System.currentTimeMillis() - timeMillis;
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "arena " + "'" + ChatColor.WHITE + args[1] + ChatColor.GRAY + "'" + " deleted (" + finalTime + ")");
                        break;
                    case "info":
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "printing debug info..");
                        long timeMillis2 = System.currentTimeMillis();

                        p.sendMessage("center location: " + Arenadata.get().getLocation("arenas." + args[1] + ".center").toString());
                        p.sendMessage("size: " + Arenadata.get().getInt("arenas." + args[1] + ".size"));
                        p.sendMessage("southeast corner: " + Arenadata.get().getLocation("arenas." + args[1] + ".southeast").toString());
                        p.sendMessage("northwest corner:" + Arenadata.get().getLocation("arenas." + args[1] + ".northwest").toString());

                        long finalTime2 = System.currentTimeMillis() - timeMillis2;
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "finished printing (" + finalTime2 + ")");

                        break;
                }
                break;
            case "testgeneration":
                TerrainArena.generateTerrainPerformant(p.getLocation(), 4);

//      TerrainArena.generateTerrain(p.getWorld(), new Location(p.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])), 4, p, null);

                break;
            case "recap":
                if (DuelListeners.duelStatistics.get(p) != null) {
                    ArrayList<Player> duelMembers = new ArrayList<>();
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {
                            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").toString().equalsIgnoreCase(args[0])) {
                                duelMembers.add(onlinePlayers);
                            }
                        }
                    }
                    Recap.openStatsGUI(duelMembers, p);
                } else {
                    p.sendMessage(C.failPrefix + "game recap has expired or does not exist");
                }
                break;
            case "previewinv":
                Player target = Bukkit.getPlayer(args[0]);
                String type = args[1];
                if (target != null) {
                    Recap.open(p, target, true, type);
                } else {
                    p.sendMessage(C.failPrefix + "player is offline or does not exist");
                }
                break;
            case "reroll":
                if (DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)) != null) {
                    if (DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).contains(p)) {
                        if (args[0].equalsIgnoreCase("yes")) {
                            DuelMethods.reRollYes.get(ArenaMethods.findPlayerArena(p)).add(true);
                            DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).remove(p);
                        } else {
                            DuelMethods.reRollNo.get(ArenaMethods.findPlayerArena(p)).add(false);
                            DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).remove(p);
                        }
                        List<Player> playersInMap = new ArrayList<>();
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()) != null) {
                                if (DuelListeners.duelStatistics.get(onlinePlayers.getUniqueId()).get("uuid").toString().equalsIgnoreCase(DuelListeners.duelStatistics.get(p.getUniqueId()).get("uuid").toString())) {
                                    playersInMap.add(onlinePlayers);
                                }
                            }
                        }
                        for (Player mapPlayers : playersInMap) {
                            String reRollYes = ChatColor.GREEN + "■";
                            String reRollNo = ChatColor.RED + "■";
                            String votes = ChatColor.GRAY + "■";
                            String arenaName = ArenaMethods.findPlayerArena(p);
                            mapPlayers.sendTitle("", reRollYes.repeat(DuelMethods.reRollYes.get(arenaName).size()) + reRollNo.repeat(DuelMethods.reRollNo.get(arenaName).size()) + votes.repeat(DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).size()));
                            mapPlayers.playSound(mapPlayers, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 0.5f, 1f);
                        }
                    } else {
                        p.sendMessage(C.failPrefix + "You cannot do this right now");
                    }
                } else {
                    p.sendMessage(C.failPrefix + "You cannot do this right now");
                }
                break;
            case "spectate":
                if (Bukkit.getPlayer(args[0]) != null) {
                    UtilMethods.spectatePlayer(Bukkit.getPlayer(args[0]), p);
                } else {
                    p.sendMessage(C.failPrefix + "player is offline or does not exist");
                }
                break;
            case "test":
                QueueInventory.queueInventory(p);
                //ItemInventory.itemInventory(p);
                break;
            case "t":
                ItemInventory.itemInventory(p);
                break;
            case "e":
                ItemEditInventory.itemEnchantInventory(p);
                break;
            case "cancel":
                if (ClickMethods.inEditor.containsKey(p)) {
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Exiting kit editor..");
                    ClickMethods.inEditor.remove(p);
                    ItemEditInventory.currentItem.remove(p);
                    p.getActivePotionEffects().clear();
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.showPlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), p);
                    }
                    if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                    try {
                        lobbyKit(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case "save":
                if (ClickMethods.inEditor.containsKey(p)) {
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Exiting kit editor..");
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Saving kit..");
                    long timeMillis = System.currentTimeMillis();
                    saveInventoryToSelectedKitSlot(p, ClickMethods.inEditor.get(p));
                    long timeFinal = System.currentTimeMillis() - timeMillis;
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Complete! (" + timeFinal + "ms)");
                    ClickMethods.inEditor.remove(p);
                    if (p.hasPotionEffect(PotionEffectType.BLINDNESS)) {
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }

                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.showPlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), p);
                    }
                    try {
                        lobbyKit(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "duel":
                if (args.length > 0 && args[0] != null && args[1] != null && Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]) != p) {
                    Player args1 = Bukkit.getPlayer(args[1]);
                    boolean statsEnabled = false;
                    if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("accept")) {
                        statsEnabled = args[2].equalsIgnoreCase("true");
                    }
                    boolean isPartyFight = partyManager.findPartyForMember(p.getUniqueId()) != null && partyManager.findPartyForMember(Bukkit.getPlayer(args[1]).getUniqueId()) != null && partyManager.findPartyForMember(p.getUniqueId()).isLeader(p.getUniqueId()) && partyManager.findPartyForMember(Bukkit.getPlayer(args[1]).getUniqueId()).isLeader(Bukkit.getPlayer(args[1]).getUniqueId());
                    switch (args[0]) {
                        case "invite":
                            if (Invites.duelInviteMap.get(Bukkit.getPlayer(args[1])) != null && Invites.duelInviteMap.get(Bukkit.getPlayer(args[1])).keySet().contains(p)) {
                                p.sendMessage(C.failPrefix+"you have already invited this player or their party to a duel.");
                                return false;
                            }
                            Invites.duelInviteSend(p, Bukkit.getPlayer(args[1]), isPartyFight, statsEnabled, args[3]);
                            break;
                        case "accept":
                            Invites.duelInviteAccept(args1,p,isPartyFight,statsEnabled);
                            break;
                        case "reject":
                            if (Invites.duelInviteMap.get(p) != null) {
                                Invites.duelInviteMap.get(p).keySet().stream().toList().get(0).sendMessage(C.failPrefix+p.getName()+" has rejected your duel request.");
                                p.sendMessage(C.successPrefix+"successfully rejected " + Bukkit.getPlayer(args[1]).getName() +"'s duel invitation");
                                Invites.duelInviteMap.remove(p);

                            } else {
                                p.sendMessage(C.failPrefix + "you are not invited to a duel with this player.");
                            }

                            break;
                    }
                } else {
                    p.sendMessage(C.failPrefix+"duel command is malformed, please try again with correct formatting.");
                }
                break;
            case "party":

                UUID uuid = p.getUniqueId();
                if (partyManager.findPartyForMember(p.getUniqueId()) == null && !args[0].equalsIgnoreCase("invite") && !args[0].equalsIgnoreCase("accept") && !args[0].equalsIgnoreCase("reject")) {
                    p.sendMessage(C.failPrefix + "You are not in a party!");
                } else if (args.length == 0 && partyManager.findPartyForMember(p.getUniqueId()) != null) {
                    List<String> partyMembers = new ArrayList<>();
                    partyMembers.add(Bukkit.getPlayer(party.getLeader()).getName());
                    if (party.getMembers() != null) {
                        for (UUID memberUUID : party.getMembers()) {
                            partyMembers.add(Bukkit.getPlayer(memberUUID).getName());
                        }
                    }
                    p.sendMessage(ChatColor.LIGHT_PURPLE+""+ChatColor.ITALIC+"Party Members");
                    p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+partyMembers);
                } else {
                    switch (args[0]) {
                        case "invite":
                            if (partyManager.findPartyForMember(uuid) != null) {
                                if (partyManager.findPartyForMember(uuid).isLeader(uuid)) {
                                    if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]) != p && !partyManager.findPartyForMember(uuid).getMembers().contains(Bukkit.getPlayer(args[1]))) {
                                        Player invited = Bukkit.getPlayer(args[1]);
                                        if (Party.invitedPlayer.get(Bukkit.getPlayer(args[1]).getUniqueId()) != uuid) {
                                            Party.invitedPlayer.put(Bukkit.getPlayer(args[1]).getUniqueId(), uuid);
                                            PartyMethods.partyInvitePlayer(uuid, Bukkit.getPlayer(args[1]).getUniqueId());
                                            for (UUID members : party.getMembersInclusive()) {
                                                if (Bukkit.getPlayer(members) != null) {
                                                    Party.sendPartyMessage(C.PINK+Bukkit.getPlayer(args[1]).getName() + " &7has been invited to the party", Bukkit.getPlayer(members));
                                                }
                                            }
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    Party.invitedPlayer.remove(invited.getUniqueId());
                                                    if (partyManager.findPartyForMember(invited.getUniqueId()) == null) {
                                                        Party.sendPartyMessage(C.t("&7party invite from "+C.PINK+p.getName()+"&7 has expired."),Bukkit.getPlayer(args[1]));
                                                    }
                                                }
                                            }.runTaskLater(C.plugin,600);
                                        } else {
                                            Party.sendPartyMessage(C.t("&d"+args[1]+" &7is already invited to this party"),p);
                                        }
                                    } else {
                                        Party.sendPartyMessage(args[1].equalsIgnoreCase(p.getName()) ?  "that's you! your name is &d"+C.PINK+ args[1] + "!" : C.PINK+args[1]+" &7does not exist or is not online",p);
                                    }
                                }
                            } else {
                                if (Bukkit.getPlayer(args[1]) != null && Bukkit.getPlayer(args[1]) != p) {
                                    Player invited = Bukkit.getPlayer(args[1]);
                                    if (Party.invitedPlayer.get(Bukkit.getPlayer(args[1]).getUniqueId()) != uuid) {
                                        Party.invitedPlayer.put(Bukkit.getPlayer(args[1]).getUniqueId(), uuid);
                                        PartyMethods.partyInvitePlayer(uuid, Bukkit.getPlayer(args[1]).getUniqueId());
                                        Party.sendPartyMessage(C.PINK+Bukkit.getPlayer(args[1]).getName() + " &7has been invited to the party", p);
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                Party.invitedPlayer.remove(invited.getUniqueId());
                                                if (partyManager.findPartyForMember(invited.getUniqueId()) == null) {
                                                    Party.sendPartyMessage(C.t("party invite from "+C.PINK+p.getName()+"&7 has expired."),Bukkit.getPlayer(args[1]));
                                                }
                                            }
                                        }.runTaskLater(C.plugin,600);
                                    } else {
                                        Party.sendPartyMessage(C.t(C.PINK+args[1]+" &7is already invited to this party"),p);
                                    }
                                } else {
                                    Party.sendPartyMessage(args[1].equalsIgnoreCase(p.getName()) ?  "that's you! your name is "+C.PINK+args[1] + "!" : C.PINK+args[1]+" &7does not exist or is not online",p);
                                }
                            }
                            break;
                        case "leave":
                            if (partyManager.findPartyForMember(uuid) != null) {
                                if (!partyManager.findPartyForMember(uuid).isLeader(uuid)) {

                                    for (UUID memberUUIDs : party.getMembersInclusive()) {
                                        if (Bukkit.getPlayer(memberUUIDs) != null) {
                                            Party.sendPartyMessage(C.PINK+Bukkit.getPlayer(uuid).getName() + " &7has left the party!", Bukkit.getPlayer(memberUUIDs));
                                        }
                                    }
                                    party.removeMember(uuid);
                                    try {
                                        KitMethods.lobbyKit(Bukkit.getPlayer(uuid));
                                    } catch (IOException err) {
                                        err.printStackTrace();
                                    }
                                }
                            }
                            break;
                        case "disband":
                            if (party != null) {
                                if (party.isLeader(uuid)) {
                                    ArrayList<Player> members = new ArrayList<>();
                                    for (UUID memberUUIDs : party.getMembers()) {
                                        if (Bukkit.getPlayer(memberUUIDs) != null) {
                                            Player member = Bukkit.getPlayer(memberUUIDs);
                                            Party.sendPartyMessage(C.PINK+Bukkit.getPlayer(party.getLeader()).getName()+"&7's party has been disbanded",member);
                                            members.add(member);
                                        }
                                    }
                                    members.add(Bukkit.getPlayer(party.getLeader()));
                                    Party.sendPartyMessage("your party has been successfully disbanded",Bukkit.getPlayer(party.getLeader()));
                                    partyManager.disbandParty(party);
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
                        case "kick":
                            if (partyManager.findPartyForMember(uuid) != null) {

                                if (Bukkit.getPlayer(args[1]) != null) {
                                    Player removed = Bukkit.getPlayer(args[1]);
                                    party.removeMember(removed.getUniqueId());
                                    Party.sendPartyMessage(C.PINK+removed.getName() + " &7has been kicked from the party!",p);
                                    Party.sendPartyMessage("You have been kicked from the party!",removed);
                                    try {
                                        KitMethods.lobbyKit(removed);
                                    } catch (IOException err) {
                                        err.printStackTrace();
                                    }
                                    for (UUID partyMembers : party.getMembers()) {
                                        if (Bukkit.getPlayer(partyMembers) != null) {
                                            Party.sendPartyMessage(C.PINK+removed.getName() + " &7has been kicked from the party.",Bukkit.getPlayer(partyMembers));
                                        }
                                    }
                                }
                            }
                            break;
                        case "accept":
                            if (partyManager.findPartyForMember(uuid) == null) {
                                if (Bukkit.getPlayer(args[1]) != null) {
                                    if (Party.invitedPlayer.get(uuid) == Bukkit.getPlayer(args[1]).getUniqueId()) {
                                        Party newParty;
                                        if (partyManager.findPartyForMember(Bukkit.getPlayer(args[1]).getUniqueId()) == null) {
                                            newParty = partyManager.createParty(Bukkit.getPlayer(args[1]).getUniqueId());
                                        } else {
                                            newParty = partyManager.findPartyForMember(Bukkit.getPlayer(args[1]).getUniqueId());
                                        }
                                        newParty.addMember(uuid);
                                        Party.invitedPlayer.remove(uuid);
                                        for (UUID memberUUIDs : newParty.getMembersInclusive()) {
                                            if (Bukkit.getPlayer(memberUUIDs) != null) {
                                                Player member = Bukkit.getPlayer(memberUUIDs);
                                                Party.sendPartyMessage(C.PINK+p.getName() + " &7has joined the party!",member);
                                            }
                                        }

                                        try {
                                            KitMethods.lobbyKit(Bukkit.getPlayer(newParty.getLeader()));
                                        } catch (IOException err) {
                                            err.printStackTrace();
                                        }
                                        try {
                                            KitMethods.lobbyKit(p);
                                        } catch (IOException err) {
                                            err.printStackTrace();
                                        }
                                    } else {
                                        p.sendMessage(C.failPrefix + "Party invite expired or does not exist");
                                    }
                                } else {
                                    p.sendMessage(C.failPrefix + "Player is offline or does not exist");
                                }
                            } else {
                                p.sendMessage(C.failPrefix + "Leave your current party before attempting to join another");
                            }
                            break;
                        case "reject":
                            if (Bukkit.getPlayer(args[1]) != null) {
                                if (Party.invitedPlayer.get(uuid) == Bukkit.getPlayer(args[1]).getUniqueId()) {

                                    p.sendMessage(C.successPrefix+"rejected " + Bukkit.getPlayer(args[1]).getName() +"'s party invitation");
                                    Bukkit.getPlayer(args[1]).sendMessage(C.failPrefix+p.getName()+" has rejected your party invitation.");
                                    Party.invitedPlayer.remove(uuid);
                                } else {
                                    p.sendMessage(C.failPrefix + "You are not invited to this player's party");
                                }
                            } else {
                                p.sendMessage(C.failPrefix + "The player specified is not real");
                            }
                            break;
                    }
                }
                break;
        }
    return false;
    }
}
