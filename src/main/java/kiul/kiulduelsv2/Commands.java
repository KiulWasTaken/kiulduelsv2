package kiul.kiulduelsv2;

import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                        saveInventorytoSelectedKitSlot(p);
                        break;
                    case "load":
                        try {loadSelectedKitSlot(p);}
                        catch (IOException error) {error.printStackTrace();}
                        break;
                    case "delete":
                        if (args[1].equalsIgnoreCase("confirm")) {
                            Userdata.get().set("kits." + p.getUniqueId() + ".kit-slot-" + kitSlot.get(p), null);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Are you sure? Type &6/kit delete confirm &7to permanently delete the contents of your selected kit slot"));
                        }
                        break;
                    case "slot":
                        kitSlot.put(p,Integer.parseInt(args[1]));
                        break;
                }
                break;
            case "arena":
                switch (args[0]) {
                    case "create":
                        Arenadata.get().set("arenas." + args[1] + ".center", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".team1", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".team2", p.getLocation());
                        Arenadata.get().set("arenas." + args[1] + ".size", 40);
                        break;
                    case "delete":
                        Arenadata.get().set("arenas." + args[1], null);
                        p.sendMessage(ChatColor.GRAY + "Arena: " + ChatColor.GOLD + args[1] + ChatColor.RED + " Deleted " + ChatColor.GRAY + " successfully!");
                        break;
                    case "edit":
                        switch (args[1]) {
                            case "team_one":
                                Arenadata.get().set("arenas." + args[1] + ".team1", p.getLocation());
                                p.sendMessage("team one spawn location set to: " + p.getLocation() + " for arena: " + args[1]);
                                break;
                            case "team_two":
                                Arenadata.get().set("arenas." + args[1] + ".team2", p.getLocation());
                                p.sendMessage("team two spawn location set to: " + p.getLocation() + " for arena: " + args[1]);
                                break;
                            case "center":
                                Arenadata.get().set("arenas." + args[1] + ".center", p.getLocation());
                                p.sendMessage("team three spawn/center location set to: " + p.getLocation() + " for arena: " + args[1]);
                                break;
                            case "size":
                                Arenadata.get().set("arenas." + args[1] + ".size", args[2]);
                                p.sendMessage("arena: " + args[1] + " size from center set to: " + args[2]);
                                break;
                            case "icon":
                                Arenadata.get().set("arenas." + args[1] + ".size", args[2]);
                                p.sendMessage(ChatColor.GRAY + "icon set to: " + ChatColor.GOLD + args[2] + ChatColor.GRAY + " for arena: " + ChatColor.GOLD + args[1]);
                                break;
                        }
                }
                break;
            case "testgeneration":
                TerrainArena.generateTerrain(p.getWorld(),new Location(p.getWorld(),Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])),new Location(p.getWorld(),Integer.parseInt(args[3]),Integer.parseInt(args[4]),Integer.parseInt(args[5])),5);
        }
    return false;
    }
}
