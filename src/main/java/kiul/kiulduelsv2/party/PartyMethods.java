package kiul.kiulduelsv2.party;

import kiul.kiulduelsv2.C;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PartyMethods {

    public static void partyInvitePlayer (UUID partyMember, UUID invited) {
        Player inviter = Bukkit.getPlayer(partyMember);
        Player player = Bukkit.getPlayer(invited);

        player.sendMessage(C.t("&d&m                                                                "));
        player.sendMessage(ChatColor.GRAY+ "" + ChatColor.ITALIC+ inviter.getDisplayName() + " has invited you to their party!");
        ComponentBuilder message = new ComponentBuilder("click this message to accept the invitation!");
        message.color(net.md_5.bungee.api.ChatColor.YELLOW).italic(true);

        // Add the first clickable component
        message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + inviter.getDisplayName()));
        player.spigot().sendMessage(message.create());
        player.sendMessage(C.t("&d&m                                                                "));

    }
}
