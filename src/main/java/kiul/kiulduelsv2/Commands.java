package kiul.kiulduelsv2;

import kiul.kiulduelsv2.config.Arenadata;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
        Player p = (Player) commandSender;
        switch (label) {
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
                        switch (args[2]) {
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
                                Arenadata.get().set("arenas." + args[1] + ".size", args[3]);
                                p.sendMessage("arena: " + args[1] + " size from center set to: " + args[3]);
                                break;
                        }
                        break;
                }
        }
    return false;
    }
}
