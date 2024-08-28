package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.arena.TerrainArena;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.database.DuelsDB;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.scoreboard.ScoreboardMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;


public class DuelMethods {

    public static HashMap<String,List<Player>> allowedToReRoll = new HashMap<>();

    public static HashMap<String,List<Boolean>> reRollYes = new HashMap<>();
    public static HashMap<String,List<Boolean>> reRollNo = new HashMap<>();


    public static HashMap<Player,String> inventoryPreview = new HashMap<>();
    public static HashMap<Player,String> armourPreview = new HashMap<>();

    public static ArrayList<Player> preDuel = new ArrayList<>();


    public static void beginDuel (String arenaName,List<Player> players,boolean rated,String kitType) {
        for (Player play : players) {
            play.setGameMode(GameMode.SURVIVAL);
            String rating = rated ? "Rated" : "Casual";
            ScoreboardMethods.startDuelSidebar(play,players,rating,System.currentTimeMillis());
        }

        Location center = Arenadata.get().getLocation("arenas."+arenaName+".center");
        int size = Arenadata.get().getInt("arenas."+arenaName+".size");
        World world = center.getWorld();

        Chunk SEChunk = world.getChunkAt(center.getChunk().getX()+size,center.getChunk().getZ()+size);
        Chunk NWChunk = world.getChunkAt(center.getChunk().getX()-size,center.getChunk().getZ()-size);


        Location teamOneSpawn = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 64, SEChunk.getZ() << 4).add(-56, 0, -56);
        Location teamTwoSpawn = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 64, NWChunk.getZ() << 4).add(56, 0, 56);
//        playersInMap.put(arenaName,players);
        ArrayList<UUID> teamOne = new ArrayList<>();
        ArrayList<UUID> teamTwo = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {
            if (i >= players.size()/2) {
                teamTwo.add(players.get(i).getUniqueId());
            } else {
                teamOne.add(players.get(i).getUniqueId());
            }
        }
        C.duelManager.createDuel(teamOne,teamTwo,rated,false,arenaName);


//        List<List<Player>> arenaTeams = new ArrayList<>() {{
//            add(teamOne);
//            add(teamTwo);
//        }};
//        mapTeams.put(arenaName,arenaTeams);
        for (UUID p : teamOne) {
            Bukkit.getPlayer(p).teleport(getHighestBlockBelow(199,teamOneSpawn).getLocation().add(0.5,1,0.5));
        }
        for (UUID p : teamTwo) {
            Bukkit.getPlayer(p).teleport(getHighestBlockBelow(199,teamTwoSpawn).getLocation().add(0.5,1,0.5));
        }
        for (Player p : players) {
            try {
                KitMethods.loadSelectedKitSlot(p,kitType);
                preDuelCountdown(p);
                    p.setSaturation(5);
                    p.setFoodLevel(20);
                    p.setHealth(20);
                    p.setGameMode(GameMode.SURVIVAL);
                    for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                        p.removePotionEffect(potionEffect.getType());
                    }
            } catch (IOException err) {
                err.printStackTrace();
            }

        }

    }

    public static void preDuelCountdown (Player p) {
        preDuel.add(p);
         new BukkitRunnable() {
            int time = 10; //or any other number you want to start countdown from
            @Override
            public void run() {
                if (this.time >= 0 && C.duelManager.findDuelForMember(p.getUniqueId()) != null) {
                    p.sendTitle(ChatColor.DARK_AQUA + "" + time, "Ready", 0, 15, 5);
                    if (this.time == 1) {
                        p.sendTitle(ChatColor.DARK_AQUA + "" + time, "Fight!", 0, 15, 5);
                    }
                    this.time--;
                } else {
                    preDuel.remove(p);
                    cancel();
                }

            }
        }.runTaskTimer(C.plugin,10,20);
    }

    public static void startRealisticDuel (List<Player> players, String arenaName,boolean reRolled, boolean rated,String kitType) {
        ArenaMethods.arenasInUse.add(arenaName);
        int size = players.size();

        Location duelCentre = Arenadata.get().getLocation("arenas." + arenaName + ".center");
        Location teleportTo = new Location(duelCentre.getWorld(),duelCentre.getX(),150,duelCentre.getZ());
        UUID duelUUID = UUID.randomUUID();
        for (Player p : players) {
            p.teleport(teleportTo);
            p.setGameMode(GameMode.SPECTATOR);
            p.setAllowFlight(true);
            p.setFlying(true);
            DuelListeners.duelStatistics.put(p.getUniqueId(),DuelListeners.createStatsArraylist());
            DuelListeners.duelStatistics.get(p.getUniqueId()).put("uuid",duelUUID);
            DuelListeners.duelStatistics.get(p.getUniqueId()).put("type",kitType.toLowerCase());
            p.sendMessage(DuelListeners.duelStatistics.get(p.getUniqueId()).get("uuid").toString());
        }


        if (ArenaMethods.getSuitableArena() != null && !reRolled) {
            String backupArena = ArenaMethods.getSuitableArena();
            ArenaMethods.arenasInUse.add(backupArena);
            // Create a clickable message with two components
            ComponentBuilder message = new ComponentBuilder("Re-Roll this randomly generated map? ")
                    .color(net.md_5.bungee.api.ChatColor.GRAY).color(net.md_5.bungee.api.ChatColor.ITALIC);

            // Add the first clickable component
            message.append("[✓]")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .bold(true)
                    .underlined(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reroll yes"));

            // Add a separator
            message.append(" ");

            // Add the second clickable component
            message.append("[❌]")
                    .color(net.md_5.bungee.api.ChatColor.RED)
                    .bold(true)
                    .underlined(true)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reroll no"));

            // Send the message
            allowedToReRoll.put(arenaName, new ArrayList<>(players));
            reRollYes.put(arenaName, new ArrayList<>());
            reRollNo.put(arenaName, new ArrayList<>());
            for (Player p : players) {
                p.setAllowFlight(true);
                p.setFlying(true);
                p.spigot().sendMessage(message.create());
            }
            new BukkitRunnable() {
                int time = 15; //or any other number you want to start countdown from

                @Override
                public void run() {
                    for (Player p : players) {
                        String message = "§7"+time;
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                    }
                    for (Player p : players) {
                        if (!Bukkit.getOnlinePlayers().contains(p)) {
                            players.remove(p);
                        }
                    }
                    if (players.size() != size) {
                        for (Player p : players) {
                            UtilMethods.teleportLobby(p);
                        }
                        ArenaMethods.arenasInUse.remove(arenaName);
                        ArenaMethods.arenasInUse.remove(backupArena);
                        cancel();
                        return;
                    }

                    if (this.time <= 0) {
                        cancel();
                        if (reRollYes.get(arenaName).size() >= reRollNo.get(arenaName).size()) {
                            for (Player p : players) {
                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Map Re-Rolling!");
                            }
                            reRollNo.remove(arenaName);
                            reRollYes.remove(arenaName);
                            startRealisticDuel(players,backupArena,true,rated,kitType);
                            ArenaMethods.regenerateArena(arenaName);
                            return;

                        } else {
                            for (Player p : players) {
                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Game Starting!");
                                p.setFlying(false);
                                p.setAllowFlight(false);
                            }
                            reRollNo.remove(arenaName);
                            reRollYes.remove(arenaName);
                            beginDuel(arenaName,players,rated,kitType);
                            ArenaMethods.arenasInUse.remove(backupArena);
                            return;
                        }
                    }

                    if (reRollYes.get(arenaName).size() >= reRollNo.get(arenaName).size() && allowedToReRoll.get(arenaName).isEmpty()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Map Re-Rolling!");
                            p.setFlying(false);
                            p.setAllowFlight(false);
                            p.teleport(teleportTo);
                            preDuel.add(p);
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        allowedToReRoll.remove(arenaName);
                        TerrainArena.generateTerrainPerformant(duelCentre,4);
                        startRealisticDuel(players,backupArena,true,rated,kitType);
                        cancel();
                        return;
                    } else if (reRollYes.get(arenaName).size() < reRollNo.get(arenaName).size() && allowedToReRoll.get(arenaName).isEmpty()) {
                        for (Player p : players) {
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Voting Complete! Game Starting!");
                            p.setFlying(false);
                            p.setAllowFlight(false);
                        }
                        reRollNo.remove(arenaName);
                        reRollYes.remove(arenaName);
                        allowedToReRoll.remove(arenaName);
                        beginDuel(arenaName, players,rated,kitType);
                        ArenaMethods.arenasInUse.remove(backupArena);

                        cancel();
                        return;
                    }
                    time--;
                }
            }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 20L);
        } else {
            for (Player p : players) {
                p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"No backup maps available, game starting in 10 seconds.");
                p.setAllowFlight(true);
                p.setFlying(true);
            }
            new BukkitRunnable() {
                boolean allPlayersPresent = true;
                @Override
                public void run() {
                    for (Player p : players) {
                        if (!p.isOnline()) {
                            allPlayersPresent = false;
                        }
                     }
                    if (allPlayersPresent) {
                        beginDuel(arenaName, players,rated,kitType);
                    } else {
                        for (Player p : players) {
                            UtilMethods.teleportLobby(p);
                            ArenaMethods.arenasInUse.remove(arenaName);
                        }
                    }
                }
            }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class),200);
        }
    }


    public static void startPartyDuel (String arenaName,List<UUID> players,List<UUID> teamOne,List<UUID> teamTwo, boolean ffa,String kitType) {
        Map<String,Object> duelStatistics = DuelListeners.createStatsArraylist();
        for (UUID player : players) {
            Player play = Bukkit.getPlayer(player);
            if (preDuel.contains(play)) {
                preDuel.remove(play);
            }
            play.setGameMode(GameMode.SURVIVAL);
            DuelListeners.duelStatistics.put(play.getUniqueId(),duelStatistics);
        }
        UUID duelUUID = UUID.randomUUID();
        for (UUID pUUIDs : players) {
            Player p = Bukkit.getPlayer(pUUIDs);
            DuelListeners.duelStatistics.put(p.getUniqueId(),DuelListeners.createStatsArraylist());
            DuelListeners.duelStatistics.get(p.getUniqueId()).put("uuid",duelUUID);
            DuelListeners.duelStatistics.get(p.getUniqueId()).put("type",kitType.toLowerCase());
            p.sendMessage(DuelListeners.duelStatistics.get(p.getUniqueId()).get("uuid").toString());
        }
        Location teamOneSpawn = Arenadata.get().getLocation("arenas."+arenaName+".southeast");
        Location teamTwoSpawn = Arenadata.get().getLocation("arenas."+arenaName+".northwest");
        if (!ffa) {

            C.duelManager.createDuel(teamOne,teamTwo,false,false,arenaName);


                for (UUID p : teamOne) {
                    Bukkit.getPlayer(p).teleport(getHighestBlockBelow(199,teamOneSpawn).getLocation().add(0.5,1,0.5));
                }
                for (UUID p : teamTwo) {
                    Bukkit.getPlayer(p).teleport(getHighestBlockBelow(199,teamTwoSpawn).getLocation().add(0.5,1,0.5));
                }

        } else {
            C.duelManager.createDuel(teamOne,teamTwo,false,true,arenaName);
            Location spawn = Arenadata.get().getLocation("arenas."+arenaName+".center");
            for (UUID p : players) {
                Bukkit.getPlayer(p).teleport(getHighestBlockBelow(199,spawn).getLocation().add(0.5,1,0.5));
            }
        }
        for (UUID uuid : players) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                try {
                    KitMethods.loadSelectedKitSlot(p,kitType);
                    preDuel.add(p);
                    preDuelCountdown(p);
                    p.setSaturation(5);
                    p.setFoodLevel(20);
                    p.setHealth(20);
                    p.setGameMode(GameMode.SURVIVAL);
                    for (PotionEffect potionEffect : p.getActivePotionEffects()) {
                        p.removePotionEffect(potionEffect.getType());
                    }
                } catch (IOException err) {
                    err.printStackTrace();
                }
            }

        }

    }

    public static Block getHighestBlockBelow (int y, Location location) {

        for (int i = y; i > -63; i--) {
            Location newLoc = new Location(location.getWorld(), location.getX(), i, location.getZ());
            if (newLoc.getBlock().getType().toString() != "AIR") {
            return newLoc.getBlock();
            }
        }
        return location.getBlock();
    }




    public static void updateElo(List<UUID> losingTeam, List<UUID> winningTeam) {
        Map<UUID, Integer> loserScoreboard = new HashMap<>();
        Map<UUID, Integer> winnerScoreboard = new HashMap<>();
        Map<UUID, Integer> eloChange = new HashMap<>();
        String eloType = (String) DuelListeners.duelStatistics.get(losingTeam.get(0)).get("type");

        // Setup
        for (UUID uuid : losingTeam) {
            int damageDealt = (int) DuelListeners.duelStatistics.get(uuid).get("damage_dealt");
            loserScoreboard.put(uuid, damageDealt);
            eloChange.put(uuid, 0);
        }
        for (UUID uuid : winningTeam) {
            int damageDealt = (int) DuelListeners.duelStatistics.get(uuid).get("damage_dealt");
            winnerScoreboard.put(uuid, damageDealt);
            eloChange.put(uuid, 0);
        }

        // Calculate Elo change for the winning team
        for (UUID uuid : winningTeam) {
            int elo = (int) DuelsDB.readPlayer(uuid, "stat_elo_"+eloType);
            int damageDealt = (int) DuelListeners.duelStatistics.get(uuid).get("damage_dealt");

            for (UUID playedAgainst : winnerScoreboard.keySet()) {
                if (playedAgainst.equals(uuid)) continue;

                int Eelo = (int) DuelsDB.readPlayer(playedAgainst, "stat_elo_"+eloType);
                double expected = 1.0 / (1.0 + Math.pow(10.0, (Eelo - elo) / 400.0));
                int outcome = damageDealt > winnerScoreboard.get(playedAgainst) ? 1 : 0;
                int change = (int) (C.K * (outcome - expected));
                eloChange.put(uuid, eloChange.get(uuid) + change);
            }

            for (UUID playedAgainst : losingTeam) {
                int Eelo = (int) DuelsDB.readPlayer(playedAgainst, "stat_elo_"+eloType);
                double expected = 1.0 / (1.0 + Math.pow(10.0, (Eelo - elo) / 400.0));
                int change = (int) (C.K * (1 - expected));
                eloChange.put(uuid, eloChange.get(uuid) + change);
            }

            int newElo = elo + eloChange.get(uuid);
            if (newElo < 100) {
                eloChange.put(uuid, 100 - elo);
            }

            DuelListeners.duelStatistics.get(uuid).put("elo", eloChange.get(uuid));
            DuelsDB.writePlayer(uuid, "stat_elo_"+eloType, newElo);
        }

        // Calculate Elo change for the losing team
        for (UUID uuid : losingTeam) {
            int elo = (int) DuelsDB.readPlayer(uuid, "stat_elo_"+eloType);
            int damageDealt = (int) DuelListeners.duelStatistics.get(uuid).get("damage_dealt");

            for (UUID playedAgainst : loserScoreboard.keySet()) {
                if (playedAgainst.equals(uuid)) continue;

                int Eelo = (int) DuelsDB.readPlayer(playedAgainst, "stat_elo_"+eloType);
                double expected = 1.0 / (1.0 + Math.pow(10.0, (Eelo - elo) / 400.0));
                int outcome = damageDealt > loserScoreboard.get(playedAgainst) ? 1 : 0;
                int change = (int) (C.K * (outcome - expected));
                eloChange.put(uuid, eloChange.get(uuid) + change);
            }

            for (UUID playedAgainst : winningTeam) {
                int Eelo = (int) DuelsDB.readPlayer(playedAgainst, "stat_elo_"+eloType);
                double expected = 1.0 / (1.0 + Math.pow(10.0, (Eelo - elo) / 400.0));
                int change = (int) (C.K * -expected);
                eloChange.put(uuid, eloChange.get(uuid) + change);
            }

            int newElo = elo + eloChange.get(uuid);
            if (newElo < 100) {
                eloChange.put(uuid, 100 - elo);
            }

            DuelListeners.duelStatistics.get(uuid).put("elo", eloChange.get(uuid));
            DuelsDB.writePlayer(uuid, "stat_elo_"+eloType, newElo);
        }
    }

    public static void updateCareer (List<UUID> losingTeam, List<UUID> winningTeam,boolean rated) {
        String losingTeamMembers = "";
        String winningTeamMembers = "";
        String comma = ", ";
        String type = (String) DuelListeners.duelStatistics.get(losingTeam.get(0)).get("type");
        String typeText = "";
        String victory = "&a&lVICTORY &7vs. ";
        String defeat = "&c&lDEFEAT &7vs. ";
//        [🗡] [🪓] [🔱]
        switch (type) {
            case "smp":
                typeText = "&#FBE108&l[&#FBE108&l\uD83D\uDDE1&#F8FB08&l] ";
                break;
            case "shield":
                typeText = "&#2623FA&l[&#425EFF&l\uD83E\uDE93&#425EFF&l] ";
                break;
            case "crystal":
                typeText = "&#FF40F2&l[&#E95FFF&l\uD83D\uDD31O&#E95FFF&l] ";
                break;
        }
        for (int i = 0; i < losingTeam.size(); i++) {
            if (i == losingTeam.size()-1) {
                comma = "";
            }
            if (i > 2) {
                comma = "..";
                losingTeamMembers += Bukkit.getPlayer(losingTeam.get(i)).getName() + comma;
                break;
            }
            losingTeamMembers += Bukkit.getPlayer(losingTeam.get(i)).getName() + comma;
        }
        comma = ", ";
        for (int i = 0; i < winningTeam.size(); i++) {
            if (i == losingTeam.size()-1) {
                comma = "";
            }
            if (i > 2) {
                comma = "..";
                losingTeamMembers += Bukkit.getPlayer(losingTeam.get(i)).getName() + comma;
                break;
            }
            winningTeamMembers += Bukkit.getPlayer(winningTeam.get(i)).getName() + comma;
        }

        for (UUID uuid : losingTeam) {
            String playerElo = "";
            if (rated) {
                int eloChange = (int) DuelListeners.duelStatistics.get(uuid).get("elo");
                    playerElo = ChatColor.WHITE + " (" + ChatColor.GREEN + "+" + eloChange + ChatColor.WHITE + ")";
                if (eloChange < 0) {
                    playerElo = ChatColor.WHITE + " (" + ChatColor.RED + eloChange + ChatColor.WHITE + ")";
                }
            }
            ArrayList<String> career;
            career = new ArrayList<String>();
            if (Userdata.get().get(uuid+".career") != null) {
                career = (ArrayList<String>) Userdata.get().get(uuid+".career");
            }
            career.add(C.t(  typeText + defeat + winningTeamMembers + playerElo));
            if (career.size() > 10) {
                career.remove(0);
            }
            Userdata.get().set(uuid+".career",career);
        }
        for (UUID uuid : winningTeam) {
            String playerElo = "";
            if (rated) {
                int eloChange = (int) DuelListeners.duelStatistics.get(uuid).get("elo");
                    playerElo = ChatColor.WHITE + " (" + ChatColor.GREEN + "+" + eloChange + ChatColor.WHITE + ")";
                if (eloChange < 0) {
                    playerElo = ChatColor.WHITE + " (" + ChatColor.RED + eloChange + ChatColor.WHITE + ")";
                }
            }
            ArrayList<String> career;
            career = new ArrayList<String>();
            if (Userdata.get().get(uuid+".career") != null) {
                career = (ArrayList<String>) Userdata.get().get(uuid+".career");
            }
            career.add(C.t( typeText + victory + losingTeamMembers + playerElo));
            if (career.size() > 10) {
                career.remove(0);
            }
            Userdata.get().set(uuid+".career",career);
        }

    }
}
