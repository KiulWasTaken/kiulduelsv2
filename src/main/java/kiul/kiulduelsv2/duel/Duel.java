package kiul.kiulduelsv2.duel;

import it.unimi.dsi.fastutil.Hash;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class Duel {

    private boolean rated;
    private boolean ffa;
    private String arena;
    private List<UUID> allContained;
    private List<UUID> players;
    private List<UUID> blueTeam;
    private List<UUID> redTeam;
    private List<UUID> blueTeamMembers;
    private List<UUID> redTeamMembers;
    private long startTime;
    private String kitType;
    private List<UUID> spectators;

    // stats
    private HashMap<String,Integer> hits;
    private HashMap<String,Integer> hitsTaken;
    private HashMap<String,String> killer;

    private HashMap<UUID,Integer> eloChange;
    private HashMap<String,Boolean> isDead;
    private HashMap<String,Double> damageTaken;
    private HashMap<String,Double> damageDealt;
    private HashMap<String,HashMap<String,Double>> damageDealtToPlayer;
    private HashMap<String,HashMap<String,Double>> damageTakenFromPlayer;
    private HashMap<String,HashMap<String,Double>> damageTypeDealt; // Player UUID (Owner), Damage Type String, Damage Amount
    private HashMap<String,HashMap<String,Double>> damageTypeTaken; // Player UUID (Owner), Damage Type String, Damage Amount
    private HashMap<String,HashMap<String,HashMap<String,Double>>> damageTypeDealtToPlayer; // Player UUID (Owner), Damage Type String, Damage Amount
    private HashMap<String,HashMap<String,HashMap<String,Double>>> damageTypeTakenFromPlayer;
    private HashMap<String,Double> durabilityDamageDealt;
    private HashMap<String,Double> durabilityDamageTaken;
    private HashMap<String,HashMap<String,Double>> durabilityDamageDealtToPlayer;
    private HashMap<String,HashMap<String,Double>> durabilityDamageTakenFromPlayer;

    private HashMap<Player,String> inventoryPreview;
    private HashMap<Player,String> enderchestPreview;

    public Duel(List<UUID> redTeam, List<UUID> blueTeam, boolean rated,boolean ffa,String arena, String kitType) {
        this.players = new ArrayList<>(redTeam);
        players.addAll(blueTeam);
        this.spectators = new ArrayList<>();
        this.rated = rated;
        this.arena = arena;
        this.redTeamMembers = new ArrayList<>(redTeam);
        this.blueTeamMembers = new ArrayList<>(blueTeam);
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.ffa = ffa;
        this.allContained = new ArrayList<>(players);
        this.startTime = System.currentTimeMillis();
        this.kitType = kitType;
        this.durabilityDamageDealt = new HashMap<>();
        this.durabilityDamageTaken = new HashMap<>();
        this.damageDealtToPlayer = new HashMap<>();
        this.damageTakenFromPlayer = new HashMap<>();
        this.durabilityDamageTakenFromPlayer = new HashMap<>();
        this.durabilityDamageDealtToPlayer = new HashMap<>();
        this.hits = new HashMap<>();
        this.hitsTaken = new HashMap<>();
        this.damageDealt = new HashMap<>();
        this.damageTaken = new HashMap<>();
        this.damageTypeTakenFromPlayer = new HashMap<>();
        this.damageTypeDealtToPlayer = new HashMap<>();
        this.damageTypeTaken = new HashMap<>();
        this.damageTypeDealt = new HashMap<>();
        this.inventoryPreview = new HashMap<>();
        this.enderchestPreview = new HashMap<>();
        this.isDead = new HashMap<>();
        this.eloChange = new HashMap<>();
//        for (UUID redTeamUUIDs : redTeamMembers) {
//            Bukkit.getPlayer(redTeamUUIDs).setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"[RED] "+ChatColor.RESET+Bukkit.getPlayer(redTeamUUIDs).getName());
//        }
//        for (UUID blueTeamUUIDs : blueTeamMembers) {
//            Bukkit.getPlayer(blueTeamUUIDs).setDisplayName(ChatColor.BLUE+""+ChatColor.BOLD+"[BLUE] "+ChatColor.RESET+Bukkit.getPlayer(blueTeamUUIDs).getName());
//        }
    }

    public List<UUID> getPlayers() {
        return players;
    }
    public boolean contains(UUID playerUUID) {
        if (players.contains(playerUUID)) {
            return true;
        }
        return false;
    }

    public List<UUID> getBlueTeam() {
        return blueTeam;
    }

    public List<UUID> getRedTeam() {
        return redTeam;
    }

    public List<UUID> getBlueTeamMembers() {
        return blueTeamMembers;
    }

    public List<UUID> getRedTeamMembers() {
        return redTeamMembers;
    }
    public List<Player> getAllBlueTeamPlayers() {
        List<Player> allBlueTeamPlayers = new ArrayList<>();
        for (UUID uuids : getBlueTeamMembers()) {
            if (Bukkit.getPlayer(uuids) != null) {
                allBlueTeamPlayers.add(Bukkit.getPlayer(uuids));
            }
        }
        return allBlueTeamPlayers;
    }

    public HashMap<String, Double> getDamageDealt() {
        return damageDealt;
    }

    public HashMap<String, Double> getDamageTaken() {
        return damageTaken;
    }

    public HashMap<String, HashMap<String, Double>> getDamageTypeDealt() {
        return damageTypeDealt;
    }

    public HashMap<String, HashMap<String, Double>> getDamageTypeTaken() {
        return damageTypeTaken;
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> getDamageTypeDealtToPlayer() {
        return damageTypeDealtToPlayer;
    }

    public HashMap<String, HashMap<String, HashMap<String, Double>>> getDamageTypeTakenFromPlayer() {
        return damageTypeTakenFromPlayer;
    }


    public List<Player> getAllRedTeamPlayers() {
        List<Player> allRedTeamPlayers = new ArrayList<>();
        for (UUID uuids : getRedTeamMembers()) {
            if (Bukkit.getPlayer(uuids) != null) {
                allRedTeamPlayers.add(Bukkit.getPlayer(uuids));
            }
        }
        return allRedTeamPlayers;
    }

    public void remove(UUID playerUUID) {
        if (blueTeam.contains(playerUUID)) {
            blueTeam.remove(playerUUID);
        }
        if (redTeam.contains(playerUUID)) {
            redTeam.remove(playerUUID);
        }
        if (players.contains(playerUUID)) {
            players.remove(playerUUID);
        }
        if (spectators.contains(playerUUID)) {
            spectators.remove(playerUUID);
        }
        if (allContained.contains(playerUUID)) {
            allContained.remove(playerUUID);
        }
//        Bukkit.getPlayer(playerUUID).setDisplayName(null);
    }
    public void killPlayer(UUID playerUUID) {

        Player p = Bukkit.getPlayer(playerUUID);
        if (p != null) {
            inventoryPreview.put(p, InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
            enderchestPreview.put(p, InventoryToBase64.itemStackArrayToBase64(p.getInventory().getArmorContents()));
        }

        if (blueTeam.contains(playerUUID)) {
            blueTeam.remove(playerUUID);
        }
        if (redTeam.contains(playerUUID)) {
            redTeam.remove(playerUUID);
        }
        players.remove(playerUUID);
        spectators.add(playerUUID);

        List<UUID> playersInDuel = getPlayers();
        for (UUID alivePlayerUUIDs : playersInDuel) {
            Bukkit.getPlayer(alivePlayerUUIDs).hidePlayer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), Bukkit.getPlayer(playerUUID));
        }

        try {
            KitMethods.spectatorKit(Bukkit.getPlayer(playerUUID));
        } catch (IOException er) {
            er.printStackTrace();
        }
    }

    public void addPlayer(UUID playerUUID) {
        players.add(playerUUID);
    }

    public List<UUID> getSpectators() {
        return spectators;
    }
    public void removeSpectator(UUID playerUUID) {
        spectators.remove(playerUUID);allContained.remove(playerUUID);
    }
    public void addSpectator(UUID playerUUID) {
        spectators.add(playerUUID);
        allContained.add(playerUUID);
    }



    public boolean isRated() {
        return rated;
    }
    public String getArena() {
        return arena;
    }

    public boolean isFfa() {
        return ffa;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getKitType() {
        return kitType;
    }

    public void increaseStat (HashMap<String, HashMap<String, HashMap<String,Double>>> statMap,Player primary,String secondary,String tertiary, double amount) {
        if (statMap.get(primary.getUniqueId().toString()) == null) {
            statMap.put(primary.getUniqueId().toString(),new HashMap<>());
        }
        if (statMap.get(primary.getUniqueId().toString()).get(secondary) == null) {
            statMap.get(primary.getUniqueId().toString()).put(secondary,new HashMap<>());
            return;
        }
        if (statMap.get(primary.getUniqueId().toString()).get(secondary).get(tertiary) == null) {
            statMap.get(primary.getUniqueId().toString()).get(secondary).put(tertiary,amount);
            return;
        }
        statMap.get(primary.getUniqueId().toString()).get(secondary).put(tertiary,statMap.get(primary.getUniqueId().toString()).get(secondary).get(tertiary)+amount);

    }
    public void increaseStat (HashMap<String, HashMap<String, Double>> statMap,Player primary,String secondary, double amount) {
        if (statMap.get(primary.getUniqueId().toString()) == null) {
            statMap.put(primary.getUniqueId().toString(),new HashMap<>());
        }
        if (statMap.get(primary.getUniqueId().toString()).get(secondary) == null) {
            statMap.get(primary.getUniqueId().toString()).put(secondary,amount);
            return;
        }
        statMap.get(primary.getUniqueId().toString()).put(secondary,statMap.get(primary.getUniqueId().toString()).get(secondary)+amount);

    }
    public void increaseStat (HashMap<String, Double> statMap,Player primary, double amount) {
        if (statMap.get(primary.getUniqueId().toString()) == null) {
            statMap.put(primary.getUniqueId().toString(),amount);
            return;
        }
        statMap.put(primary.getUniqueId().toString(),statMap.get(primary.getUniqueId().toString())+amount);
    }

    public List<UUID> getAllContained() {
        return allContained;
    }

    public HashMap<String, String> getKiller() {
        return killer;
    }

    public HashMap<String, Integer> getHits() {
        return hits;
    }

    public HashMap<String, Double> getDurabilityDamageDealt() {
        return durabilityDamageDealt;
    }

    public HashMap<String, Double> getDurabilityDamageTaken() {
        return durabilityDamageTaken;
    }

    public HashMap<String, HashMap<String, Double>> getDamageDealtToPlayer() {
        return damageDealtToPlayer;
    }

    public HashMap<String, HashMap<String, Double>> getDamageTakenFromPlayer() {
        return damageTakenFromPlayer;
    }


    public HashMap<String, HashMap<String, Double>> getDurabilityDamageDealtToPlayer() {
        return durabilityDamageDealtToPlayer;
    }

    public HashMap<String, HashMap<String, Double>> getDurabilityDamageTakenFromPlayer() {
        return durabilityDamageTakenFromPlayer;
    }


    public HashMap<UUID, Integer> getEloChange() {
        return eloChange;
    }

    public HashMap<String, Boolean> getIsDead() {
        return isDead;
    }

    public HashMap<String, Integer> getHitsTaken() {
        return hitsTaken;
    }
    
    public void endGame (List<UUID> losingTeam, List<UUID> winningTeam, Player lastDead) {
        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL_LARGE) // You can choose different types like STAR, CREEPER, etc.
                .withColor(Color.LIME)
                .withColor(Color.WHITE)
                .build();
        Firework firework = (Firework) lastDead.getWorld().spawnEntity(lastDead.getLocation(), EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(effect);

        // Set the power of the firework
        meta.setPower(0); // Power 0 means instant explosion

        // Apply the meta to the firework
        firework.setFireworkMeta(meta);
        firework.detonate();

        if (isRated()) {
            DuelMethods.updateElo(losingTeam, winningTeam, this);
        }
        DuelMethods.updateCareer(losingTeam, winningTeam, isRated(), this);
        if (!ffa) {
            // team 1 wins
            for (UUID winners : winningTeam) {
                if (Bukkit.getPlayer(winners) != null) {
                    Player winnerPlayer = Bukkit.getPlayer(winners);
                    winnerPlayer.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "VICTORY!", "");
                    if (!isDead.get(winners.toString())) {
                        inventoryPreview.put(winnerPlayer, InventoryToBase64.itemStackArrayToBase64(winnerPlayer.getInventory().getContents()));
                        enderchestPreview.put(winnerPlayer, InventoryToBase64.itemStackArrayToBase64(winnerPlayer.getEnderChest().getContents()));
                    }

                    int wins = Userdata.get().getInt(winners + ".stats.wins");
                    int streak = Userdata.get().getInt(winners + ".stats.streak");
                    Userdata.get().set(winners + ".stats.wins", wins + 1);
                    Userdata.get().set(winners + ".stats.streak", streak + 1);
                    winnerPlayer.playSound(winnerPlayer, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            remove(winnerPlayer.getUniqueId());
                            UtilMethods.teleportLobby(winnerPlayer);
                        }
                    }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 41);
                } else {
                    remove(winners);
                }
            }
            ArenaMethods.regenerateArena(arena);

            for (UUID allUUIDs : getAllContained()) {
                Player onlineDuelParticipants = Bukkit.getPlayer(allUUIDs);
                if (losingTeam.contains(allUUIDs)) {
                    onlineDuelParticipants.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Recap.sendMatchRecap(onlineDuelParticipants, winningTeam, Duel.this);
                            UtilMethods.becomeNotSpectator(onlineDuelParticipants);
                            UtilMethods.teleportLobby(onlineDuelParticipants);
                            remove(allUUIDs);
                        }
                    }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 40);
                }
            }
        } else {

        }
    }

    public HashMap<Player, String> getEnderchestPreview() {
        return enderchestPreview;
    }

    public HashMap<Player, String> getInventoryPreview() {
        return inventoryPreview;
    }

    public void updateStats () {}
//    double getDamageDealt = getDamageDealt().get(winners.toString());
//    double getDamageTaken = getDamageTaken().get(winners.toString());
//    int damageDelta = (int) (getDamageDealt - getDamageTaken);
//    ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(winners + ".stats.damagedelta");
//                    damageDeltaPerRound.add(0, damageDelta);
//                    if (damageDeltaPerRound.size() > 10) {
//        damageDeltaPerRound.remove(10);
//    }
//                    Userdata.get().set(winners + ".stats.damagedelta", damageDeltaPerRound);
//                    if (Userdata.get().getInt(winners + ".stats.streak") > Userdata.get().getInt(winners + ".stats.best_streak")) {
//        Userdata.get().set(winners + ".stats.best_streak", Userdata.get().getInt(winners + ".stats.streak"));



     /*   int losses = Userdata.get().getInt(allUUIDs + ".stats.losses");
        Userdata.get().set(allUUIDs + ".stats.losses", losses + 1);
        Userdata.get().set(allUUIDs + ".stats.streak", 0);
        double getDamageDealt = getDamageDealt().get(allUUIDs.toString());
        double getDamageTaken = getDamageTaken().get(allUUIDs.toString());
        int damageDelta = (int)(getDamageDealt - getDamageTaken);
        ArrayList<Integer> damageDeltaPerRound = (ArrayList<Integer>) Userdata.get().get(allUUIDs + ".stats.damagedelta");
        damageDeltaPerRound.add(0, damageDelta);
        if (damageDeltaPerRound.size() > 10) {
            damageDeltaPerRound.remove(10);
        }
        Userdata.get().set(allUUIDs + ".stats.damagedelta", damageDeltaPerRound);
        Userdata.save();
    }*/
}
