package kiul.kiulduelsv2.util;

import kiul.kiulduelsv2.duel.Queue;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
        public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
            Player p = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("kit")) {
            if (!p.hasPermission("kiulduels.arena")) return Collections.emptyList();
            final List<String> oneArgList = new ArrayList<>();
            final List<String> completions = new ArrayList<>();

            oneArgList.add("save");
            oneArgList.add("load");
            oneArgList.add("delete");
            oneArgList.add("loadglobal");
            oneArgList.add("slot");

            if (args.length == 1){
                StringUtil.copyPartialMatches(args[0], oneArgList, completions);
            }
            if (args.length == 2){
                return null;
                // Returns null to get all online players
            }
            Collections.sort(completions);
            return completions;
        }
        if (command.getName().equalsIgnoreCase("arena")) {
            if (!p.hasPermission("kiulduels.arena")) return Collections.emptyList();
            final List<String> oneArgList = new ArrayList<>();
            final List<String> completions = new ArrayList<>();

            oneArgList.add("info");
            oneArgList.add("create");
            oneArgList.add("delete");

            if (args.length == 1){
                StringUtil.copyPartialMatches(args[0], oneArgList, completions);
            }
            if (args.length == 2){
                return null;
                // Returns null to get all online players
            }

            Collections.sort(completions);
            return completions;
        }
        if (command.getName().equalsIgnoreCase("party")) {
            final List<String> oneArgList = new ArrayList<>();
            final List<String> completions = new ArrayList<>();

            oneArgList.add("invite");
            oneArgList.add("accept");
            oneArgList.add("kick");
            oneArgList.add("disband");
            oneArgList.add("leave");

            if (args.length == 1){
                StringUtil.copyPartialMatches(args[0], oneArgList, completions);
            }
            if (args.length == 2){
                return null;
                // Returns null to get all online players
            }

            Collections.sort(completions);
            return completions;
        }
        if (command.getName().equalsIgnoreCase("duel")) {
            final List<String> oneArgList = new ArrayList<>();
            final List<String> threeArgList = new ArrayList<>();
            final List<String> completions = new ArrayList<>();

            oneArgList.add("invite");
            oneArgList.add("accept");
            oneArgList.add("reject");

            threeArgList.add("[ENABLE STATS?]");
            threeArgList.add("true");
            threeArgList.add("false");

            if (args.length == 1){
                StringUtil.copyPartialMatches(args[0], oneArgList, completions);
            }
            if (args.length == 2){
                return null;
                // Returns null to get all online players
            }
            if (args.length == 3) {
                StringUtil.copyPartialMatches(args[2], threeArgList, completions);
            }
            if (args.length == 4) {
                StringUtil.copyPartialMatches(args[3], Queue.queueTypesLowercase(), completions);
            }
            Collections.sort(completions);
            return completions;
        }
        return Collections.EMPTY_LIST;
    }
}
