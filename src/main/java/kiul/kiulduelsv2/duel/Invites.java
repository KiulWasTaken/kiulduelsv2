package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.gui.layout.KitEnum;
import kiul.kiulduelsv2.gui.layout.KitInventory;
import kiul.kiulduelsv2.party.Party;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Invites {


    public static HashMap<Player,HashMap<Player,String>> duelInviteMap = new HashMap<>();

    public static void sendDuelMessage (Player recipient, String message, Component clickComponent) {



        recipient.sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#ebbc3d:#b59a4e><strikethrough>                                                                "));
        recipient.sendMessage(C.t("&7&o"+message));
        if (clickComponent != null) {
        recipient.sendMessage(clickComponent);
        }
        recipient.sendMessage(MiniMessage.miniMessage().deserialize("<gradient:#ebbc3d:#b59a4e><strikethrough>                                                                "));
    }

    public static void duelInviteSend (Player sentFrom, Player sentTo, boolean isPartyFight, boolean statsEnabled, String type) {
        String stats = statsEnabled ? C.t(C.GREEN+"&lENABLED") :  C.t(C.RED+"&lDISABLED");
        String mode = C.t("&f&lNULL");

        switch (type) {
            case "smp":
                mode = KitEnum.SMP.getDisplayName();
                break;
            case "crystal":
                mode = KitEnum.CRYSTAL.getDisplayName();
                break;
            case "shield":
                mode = KitEnum.SHIELD.getDisplayName();
                break;
            case "cart":
                mode = KitEnum.CART.getDisplayName();
                break;
        }

        String sentFromTarget = isPartyFight ? sentTo.getName()+"'s party" : sentTo.getName();
        String sentFromMessage = C.t("&7you invited &f"+sentFromTarget + "&7 to a duel:\n \n" +
                "&7MODE &8→&r " + mode + "\n" +
                "&7STATS &8→&r " + stats);

        String sentToTarget = isPartyFight ? sentFrom.getName()+"'s party" : sentFrom.getName();
        String target = isPartyFight ? "your party" : "you" ;
        String sentToMessage = C.t("&f"+sentToTarget + "&7 has invited &f" + target + "&7 to a duel:\n \n" +
                "&7MODE &8→&r " + mode + "\n" +
                "&7STATS &8→&r " + stats + "\n ");

        String statsEnabledStateString = String.valueOf(statsEnabled);

        if (isPartyFight) {
            Party partyFrom = C.partyManager.findPartyForMember(sentFrom.getUniqueId());
            Party partyTo = C.partyManager.findPartyForMember(sentTo.getUniqueId());
            if (partyTo == null || partyFrom == null) {
                sentFrom.sendMessage(C.failPrefix + "one of the parties involved does not exist");
                return;
            }


            for (UUID partyUUIDs : partyFrom.getMembersInclusive()) {
                Player partyMember = Bukkit.getPlayer(partyUUIDs);
                if (partyMember != null) {
                    sendDuelMessage(partyMember,sentFromMessage,null);
                }
            }

            Component clickComponent = Component.empty().append(MiniMessage.miniMessage().deserialize("<click:run_command:/duel accept " + sentFrom.getName() + " " + statsEnabledStateString + "><#27a33a><b>[✔]</click>")).append(Component.text("         ").append(MiniMessage.miniMessage().deserialize("<click:run_command:/duel reject " + sentFrom.getName() + "><#e33630><b>[❌]</click>")));
            for (UUID partyUUIDs : partyTo.getMembersInclusive()) {
                Player partyMember = Bukkit.getPlayer(partyUUIDs);
                if (partyMember != null) {
                    if (partyTo.isLeader(partyUUIDs)) {
                        sendDuelMessage(partyMember,sentToMessage,clickComponent);
                    } else {
                        sendDuelMessage(partyMember, sentToMessage, null);
                    }
                }
            }

        } else {
            Component clickComponent = Component.empty().append(MiniMessage.miniMessage().deserialize("<click:run_command:/duel accept " + sentFrom.getName() + " " + statsEnabledStateString + "><#27a33a><b>[✔]</click>")).append(Component.text("         ").append(MiniMessage.miniMessage().deserialize("<click:run_command:/duel reject " + sentFrom.getName() + "><#e33630><b>[❌]</click>")));
            sendDuelMessage(sentTo,sentToMessage,clickComponent);
            sendDuelMessage(sentFrom,sentFromMessage,null);
        }

        duelInviteMap.put(sentTo,new HashMap<>());
        duelInviteMap.get(sentTo).put(sentFrom,type);
    }

    public static void duelInviteAccept (Player inviteSend, Player inviteAccept, boolean isPartyFight,boolean isStatsEnabled) {

        if (duelInviteMap.containsKey(inviteAccept)) {
            String type = duelInviteMap.get(inviteAccept).get(inviteSend);
            if (isPartyFight) {
                Party sendParty = C.partyManager.findPartyForMember(inviteSend.getUniqueId());
                Party acceptParty = C.partyManager.findPartyForMember(inviteAccept.getUniqueId());
                if (sendParty == null || acceptParty == null) {
                    inviteAccept.sendMessage(C.failPrefix + "one of the parties involved does not exist");
                    return;
                }

                // add all online members from the accepting party to acceptPartyParticipants
                List<UUID> acceptPartyParticipants = new ArrayList<>();
                for (UUID partyUUID : acceptParty.getMembersInclusive()) {
                    Player partyMember = Bukkit.getPlayer(partyUUID);
                    if (partyMember != null) {
                        acceptPartyParticipants.add(partyUUID);
                    }
                }

                // add all online members from the sending party to sendPartyParticipants
                List<UUID> sendPartyParticipants = new ArrayList<>();
                for (UUID partyUUID : sendParty.getMembersInclusive()) {
                    Player partyMember = Bukkit.getPlayer(partyUUID);
                    if (partyMember != null) {
                        sendPartyParticipants.add(partyUUID);
                    }
                }

                // add all members from both parties into a combined list for use in startPartyDuel
                List<UUID> participatingPlayers = new ArrayList<>();
                participatingPlayers.addAll(acceptPartyParticipants);
                participatingPlayers.addAll(sendPartyParticipants);

                if (ArenaMethods.getSuitableArena() != null) {
                    DuelMethods.startPartyDuel(ArenaMethods.getSuitableArena(),participatingPlayers,sendPartyParticipants,acceptPartyParticipants,false,type);
                } else {
                    inviteSend.sendMessage(C.failPrefix + "the other party accepted your request but there are no arenas available to play on");
                    inviteAccept.sendMessage(C.failPrefix + "there are no arenas available to play on");
                }

            } else {
                // add the player that accepted to a team list
                List<UUID> acceptParticipants = new ArrayList<>();
                acceptParticipants.add(inviteAccept.getUniqueId());

                // add the player that sent the invite to a team list
                List<UUID> sendParticipants = new ArrayList<>();
                sendParticipants.add(inviteSend.getUniqueId());

                // add all members from both team lists into a combined list for use in startPartyDuel
                List<UUID> participatingPlayers = new ArrayList<>();
                participatingPlayers.addAll(acceptParticipants);
                participatingPlayers.addAll(sendParticipants);

                if (ArenaMethods.getSuitableArena() != null) {
                    DuelMethods.startPartyDuel(ArenaMethods.getSuitableArena(),participatingPlayers,sendParticipants,acceptParticipants,false,type);
                } else {
                    inviteSend.sendMessage(C.failPrefix + "the other player accepted your request but there are no arenas available to play on");
                    inviteAccept.sendMessage(C.failPrefix + "there are no arenas available to play on");
                }
            }
            duelInviteMap.remove(inviteAccept);
        } else {
          inviteAccept.sendMessage(C.failPrefix + "you are not invited to a duel with this player or their party!");
        }
    }
}
