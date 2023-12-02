package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.duel.DuelMethods;
import kiul.kiulduelsv2.gui.EnchantInventory;
import kiul.kiulduelsv2.gui.ItemEnum;
import kiul.kiulduelsv2.gui.ItemInventory;
import kiul.kiulduelsv2.gui.QueueInventory;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

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
                            ItemStack[] kitContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global." + args[1] + ".inventory")).getContents();
                            ItemStack[] armourContents = InventoryToBase64.fromBase64((String) Userdata.get().get("kits.global." + args[1] + ".armour")).getContents();
                            p.getInventory().setContents(kitContents);
                            p.getInventory().setArmorContents(armourContents);
                        } catch (IOException error) {error.printStackTrace();}
                        break;
                }
                break;
            case "arena":
                switch (args[0]) {
                    case "create":
                        Arenadata.get().set("arenas." + args[1] + ".center", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".team1", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".type", "DEFAULT");
                        Arenadata.get().set("arenas." + args[1] + ".team2", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".corner1", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".corner2", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".size", 40);
                        Arenadata.save();
                        p.sendMessage("Created map: " + args[1]);
                        break;
                    case "delete":
                        Arenadata.get().set("arenas." + args[1], null);
                        p.sendMessage(ChatColor.GRAY + "Arena: " + ChatColor.GOLD + args[1] + ChatColor.RED + " Deleted " + ChatColor.GRAY + " successfully!");
                        Arenadata.save();
                        break;
                    case "info":

                        break;
                    case "edit":
                        switch (args[2]) {
                            case "team_one":
                                Arenadata.get().set("arenas." + args[1] + ".team1", p.getLocation());
                                p.sendMessage("team one spawn location set to: " + p.getLocation() + " for arena: " + args[1]);
                                Arenadata.save();
                                break;
                            case "team_two":
                                Arenadata.get().set("arenas." + args[1] + ".team2", p.getLocation());
                                p.sendMessage("team two spawn location set to: " + p.getLocation() + " for arena: " + args[1]);
                                Arenadata.save();
                                break;
                            case "corner2":
                                Arenadata.get().set("arenas." + args[1] + ".corner2", p.getLocation());
                                p.sendMessage("corner two location set to: " + p.getLocation() + " for arena: " + args[1]);
                                Arenadata.save();
                                break;
                            case "corner1":
                                Arenadata.get().set("arenas." + args[1] + ".corner1", p.getLocation());
                                p.sendMessage("corner one location set to: " + p.getLocation() + " for arena: " + args[1]);
                                Arenadata.save();
                                break;
                            case "center":
                                double pX = p.getLocation().getX();
                                double pZ = p.getLocation().getZ();
                                Location center = new Location(p.getWorld(),pX,0,pZ);
                                Arenadata.get().set("arenas." + args[1] + ".center", center);
                                p.sendMessage("center location set to: " + pX + "," + pZ + " for arena: " + args[1]);
                                Arenadata.save();
                                break;
                            case "size":
                                Arenadata.get().set("arenas." + args[1] + ".size", args[3]);
                                p.sendMessage("arena: " + args[1] + " size from center set to: " + args[3]);
                                Arenadata.save();
                                break;
                            case "icon":
                                Arenadata.get().set("arenas." + args[1] + ".icon", args[2]);
                                p.sendMessage(ChatColor.GRAY + "icon set to: " + ChatColor.GOLD + args[2] + ChatColor.GRAY + " for arena: " + ChatColor.GOLD + args[1]);
                                Arenadata.save();
                                break;
                            case "type":
                                if (ArenaMethods.validMapTypes.toString().contains(args[3])) {
                                    Arenadata.get().set("arenas." + args[1] + ".type", args[3]);
                                    p.sendMessage(ChatColor.GRAY + "map type set to: " + ChatColor.GOLD + args[3] + ChatColor.GRAY + " for arena: " + ChatColor.GOLD + args[1]);
                                    Arenadata.save();
                                } else {
                                    p.sendMessage(ChatColor.RED + "Please enter a valid map type: " + ChatColor.YELLOW + ArenaMethods.validMapTypes.toString());
                                }
                                break;
                        }
                }
                break;
            case "testgeneration":
                TerrainArena.generateTerrain(p.getWorld(),new Location(p.getWorld(),Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])),new Location(p.getWorld(),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5])),5);
                break;
            case "reroll":
                if (args[0].equalsIgnoreCase("yes")) {
                    DuelMethods.reRollYes.get(ArenaMethods.findPlayerArena(p)).add(true);
                    DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).remove(p);
                } else {
                    DuelMethods.reRollNo.get(ArenaMethods.findPlayerArena(p)).add(false);
                    DuelMethods.allowedToReRoll.get(ArenaMethods.findPlayerArena(p)).remove(p);
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
        }
    return false;
    }
}
