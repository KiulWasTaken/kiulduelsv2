package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.Region;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemEnum;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.QueueInventory;
import kiul.kiulduelsv2.gui.clickevents.ClickMethods;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.party.Party;
import kiul.kiulduelsv2.party.PartyManager;
import kiul.kiulduelsv2.party.PartyMethods;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

import java.io.IOException;
import java.util.*;

import static kiul.kiulduelsv2.inventory.KitMethods.*;
import static kiul.kiulduelsv2.config.Userdata.*;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        Player p = (Player) commandSender;

        switch (label) {
            case "kit":
                switch (args[0]) {
                    case "save":
                        saveInventoryToSelectedKitSlot(p);
                        break;
                    case "load":
                        try {loadSelectedKitSlot(p);}
                        catch (IOException error) {error.printStackTrace();}
                        break;
                    case "delete":
                        if (args[1].equalsIgnoreCase("confirm")) {
                            Userdata.get().set("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p), null);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cAre you sure? &7Type &6/kit delete confirm &7to permanently delete the contents of your selected kit slot"));
                        }
                        break;
                    case "slot":
                        kitSlot.put(p,Integer.parseInt(args[1]));
                        break;
                    case "loadglobal":
                        try {
                            ItemStack[] armourContents;
                            ItemStack[] kitContents = InventoryToBase64.fromBase64(Userdata.get().getString("kits.global." + args[1] + ".inventory")).getContents();

                            if (Userdata.get().getString("kits.global." + args[1] + ".armour") != null) {
                                armourContents = InventoryToBase64.fromBase64(Userdata.get().getString("kits.global." + args[1] + ".armour")).getContents();
                                p.getInventory().setArmorContents(armourContents);
                            }
                            p.getInventory().setContents(kitContents);

                        } catch (IOException error) {error.printStackTrace();}
                        p.sendMessage(ChatColor.GREEN + "global kit " + ChatColor.YELLOW + args[1] + ChatColor.GREEN +   " load success");
                        break;
                }
                break;
            case "arena":
                switch (args[0]) {
                    case "create":
                        if (args[1] != null) {
                            p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC + "creating arena..");
                            long timeMillis = System.currentTimeMillis();

                            int size = 4;
                            World world = p.getWorld();
                            Chunk c = p.getLocation().getChunk();
                            Location center = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(8, 0, 8);


                            Chunk SEChunk = world.getChunkAt(center.getChunk().getX()+size,center.getChunk().getZ()+size);
                            Chunk NWChunk = world.getChunkAt(center.getChunk().getX()-size,center.getChunk().getZ()-size);


                            Location southeast = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 64, SEChunk.getZ() << 4).add(8, 0, 8);
                            Location northwest = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 64, NWChunk.getZ() << 4).add(8, 0, 8);

                            // set data
                            Arenadata.get().set("arenas." + args[1] + ".center", center);
                            Arenadata.get().set("arenas." + args[1] + ".size", size);
                            Arenadata.get().set("arenas." + args[1] + ".southeast", southeast);
                            Arenadata.get().set("arenas." + args[1] + ".northwest", northwest);

                            Arenadata.save();
                            long finalTime = System.currentTimeMillis()-timeMillis;
                            p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC + "arena " + "'" + ChatColor.WHITE + args[1] + ChatColor.GRAY +  "'" + " created (" + finalTime + ")");
                        } else {
                            p.sendMessage(ChatColor.RED + "arena requires a name!");
                        }
                        break;
                    case "delete":
                        p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC + "deleting arena..");
                        long timeMillis = System.currentTimeMillis();

                        Arenadata.get().set("arenas." + args[1], null);
                        Arenadata.save();

                        long finalTime = System.currentTimeMillis()-timeMillis;
                        p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC + "arena " + "'" + ChatColor.WHITE + args[1] + ChatColor.GRAY +  "'" + " deleted (" + finalTime + ")");
                        break;
                    case "info":
                        p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC + "printing debug info..");
                        long timeMillis2 = System.currentTimeMillis();

                        p.sendMessage("center location: " + Arenadata.get().getLocation("arenas." + args[1] + ".center").toString());
                        p.sendMessage("size: " + Arenadata.get().getInt("arenas." + args[1] + ".size"));
                        p.sendMessage("southeast corner: " + Arenadata.get().getLocation("arenas." + args[1] + ".southeast").toString());
                        p.sendMessage("northwest corner:" + Arenadata.get().getLocation("arenas." + args[1] + ".northwest").toString());

                        long finalTime2 = System.currentTimeMillis()-timeMillis2;
                        p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC + "finished printing (" + finalTime2 + ")");

                        break;
                }
                break;
            case "testgeneration":
                TerrainArena.generateTerrainPerformant(p.getLocation(),4);

//      TerrainArena.generateTerrain(p.getWorld(), new Location(p.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])), 4, p, null);

                break;
            case "reroll":
                if (DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).contains(p)) {
                    if (args[0].equalsIgnoreCase("yes")) {
                        DuelMethods.reRollYes.get(ArenaMethods.findPlayerArena(p)).add(true);
                        DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).remove(p);
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Vote added! (Y)");
                    } else {
                        DuelMethods.reRollNo.get(ArenaMethods.findPlayerArena(p)).add(false);
                        DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).remove(p);
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Vote added! (N)");
                    }
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
                EnchantInventory.itemEnchantInventory(p);
                break;
            case "cancel":
                if (ClickMethods.inEditor.contains(p)) {
                    p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Exiting kit editor..");
                    ClickMethods.inEditor.remove(p);
                    p.getActivePotionEffects().clear();
                    for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                        onlinePlayers.showPlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), p);
                    }
                    if(p.hasPotionEffect(PotionEffectType.BLINDNESS)){
                        p.removePotionEffect(PotionEffectType.BLINDNESS);
                    }
                    try {
                        lobbyKit(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            case "save":
                if (ClickMethods.inEditor.contains(p)) {
                    p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Exiting kit editor..");
                    p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Saving kit..");
                    long timeMillis = System.currentTimeMillis();
                    saveInventoryToSelectedKitSlot(p);
                    long timeFinal = System.currentTimeMillis()-timeMillis;
                    p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"Complete! (" + timeFinal + "ms)");
                    ClickMethods.inEditor.remove(p);
                    if(p.hasPotionEffect(PotionEffectType.BLINDNESS)){
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
            case "party":

                UUID uuid = p.getUniqueId();
                PartyManager partyManager = new PartyManager();
                if (partyManager.findPartyForMember(p.getUniqueId()) == null) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are not in a party!");
                } else {
                    Party party = partyManager.findPartyForMember(p.getUniqueId());
                    p.sendMessage(party.getMembers().toString());
                }
                switch (args[0]) {
                    case "invite":
                        if (partyManager.findPartyForMember(uuid) != null) {
                            if (partyManager.findPartyForMember(uuid).isLeader(uuid)) {
                                if (Bukkit.getPlayer(args[1]) != null) {
                                    Party.invitedPlayer.put(Bukkit.getPlayer(args[1]).getUniqueId(),partyManager.findPartyForMember(uuid));
                                    PartyMethods.partyInvitePlayer(uuid,Bukkit.getPlayer(args[1]).getUniqueId());
                                }
                            }
                        } else {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                partyManager.createParty(p.getUniqueId());
                                Party.invitedPlayer.put(Bukkit.getPlayer(args[1]).getUniqueId(), partyManager.findPartyForMember(uuid));
                                PartyMethods.partyInvitePlayer(uuid, Bukkit.getPlayer(args[1]).getUniqueId());
                                try {KitMethods.lobbyKit(p);} catch (IOException err) {err.printStackTrace();}
                            }
                        }
                        break;
                    case "leave":
                        if (partyManager.findPartyForMember(uuid) != null) {
                            if (!partyManager.findPartyForMember(uuid).isLeader(uuid)) {
                               Party party = partyManager.findPartyForMember(uuid);
                               party.removeMember(uuid);

                            }
                        }
                        break;
                    case "disband":
                        if (partyManager.findPartyForMember(uuid) != null) {
                            if (partyManager.findPartyForMember(uuid).isLeader(uuid)) {
                                for (UUID memberUUIDs : partyManager.findPartyForMember(uuid).getMembers()) {
                                    if (Bukkit.getPlayer(memberUUIDs) != null) {
                                        Player member = Bukkit.getPlayer(memberUUIDs);
                                        member.sendMessage(C.t("&#FF5263" + Bukkit.getPlayer(partyManager.findPartyForMember(uuid).getLeader()).getDisplayName() + "'s party has been disbanded"));
                                    }
                                }
                                partyManager.disbandParty(partyManager.findPartyForMember(uuid));
                                }
                            }
                        break;
                    case "kick":
                        if (partyManager.findPartyForMember(uuid) != null) {
                            Party party = partyManager.findPartyForMember(uuid);
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player removed = Bukkit.getPlayer(args[1]);
                                party.removeMember(removed.getUniqueId());
                                removed.sendMessage(C.t("&#FF5263" + "" + ChatColor.ITALIC + "You have been kicked from the party!"));
                                for (UUID partyMembers : party.getMembers()) {
                                    if (Bukkit.getPlayer(partyMembers) != null) {
                                        Bukkit.getPlayer(partyMembers).sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + removed.getDisplayName() + " has been kicked from the party.");
                                    }
                                }
                            }
                        }
                        break;
                    case "accept":
                        if (partyManager.findPartyForMember(uuid) == null) {
                            if (Bukkit.getPlayer(args[1])  != null) {
                                Party invitedTo = partyManager.findPartyForMember(Bukkit.getPlayer(args[1]).getUniqueId());
                                if (Party.invitedPlayer.get(uuid) == invitedTo) {
                                    Party party = partyManager.findPartyForMember(Bukkit.getPlayer(args[1]).getUniqueId());
                                    for (UUID memberUUIDs : party.getMembers()) {
                                        if (Bukkit.getPlayer(memberUUIDs) != null) {
                                            Player member = Bukkit.getPlayer(memberUUIDs);
                                            member.sendMessage(C.t("&#B755FF" + p.getDisplayName() + " has joined the party!"));
                                        }
                                    }
                                    party.addMember(uuid);
                                    try {KitMethods.lobbyKit(p);} catch (IOException err) {err.printStackTrace();}
                                }
                            }
                        }
                        break;
                }
                break;
        }
    return false;
    }
}
