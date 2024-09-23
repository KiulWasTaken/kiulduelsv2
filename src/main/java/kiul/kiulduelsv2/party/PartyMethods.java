package kiul.kiulduelsv2.party;

import kiul.kiulduelsv2.C;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.UUID;

public class PartyMethods {

    public static void partyInvitePlayer (UUID partyMember, UUID invited) {
        Player inviter = Bukkit.getPlayer(partyMember);
        Player player = Bukkit.getPlayer(invited);

        player.sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#c73e8b:#6d2b94><strikethrough>                                                                "));
        player.sendMessage(C.t(C.PINK + inviter.getName() + "&7 has invited you to their party!"));
        player.sendMessage("");
        net.kyori.adventure.text.Component clickComponent = net.kyori.adventure.text.Component.empty().append(MiniMessage.miniMessage().deserialize("<click:run_command:/party accept " + inviter.getName() +"><#27a33a><b>[✔]</click>")).append(Component.text("         ").append(MiniMessage.miniMessage().deserialize("<click:run_command:/party reject " + inviter.getName() + "><#e33630><b>[❌]</click>")));
        player.sendMessage(clickComponent);
        player.sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#c73e8b:#6d2b94><strikethrough>                                                                "));

    }
}
