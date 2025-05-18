package kiul.kiulduelsv2.duel;

import kiul.kiulduelsv2.C;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.ParseMethods;
import kiul.kiulduelsv2.arena.ArenaMethods;
import kiul.kiulduelsv2.config.Userdata;
import kiul.kiulduelsv2.gui.KitEditor;
import kiul.kiulduelsv2.inventory.InventoryToBase64;
import kiul.kiulduelsv2.inventory.KitMethods;
import kiul.kiulduelsv2.util.UtilMethods;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.*;
import java.util.function.UnaryOperator;

public class DuelListeners implements Listener {


    @EventHandler (priority = EventPriority.HIGHEST)
    public void deathUpdateDuel(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && ((Player) e.getEntity()).getHealth() <= e.getFinalDamage() && p.getInventory().getItemInMainHand().getType() != Material.TOTEM_OF_UNDYING && p.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING) {
            if (ArenaMethods.findPlayerArena(p) != null) {
                String arenaName = ArenaMethods.findPlayerArena(p);
                Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
                if (duel != null) {
                    e.setDamage(0);
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setSaturation(5);
                    duel.killPlayer(p.getUniqueId());

                    duel.getIsDead().put(p.getUniqueId().toString(),true);
                    if (p.getKiller() != null) {
                        Userdata.get().set(p.getKiller().getUniqueId() + ".stats.kills", Userdata.get().getInt(p.getKiller().getUniqueId() + ".stats.kills") + 1);
                    }
                    Userdata.get().set(p.getUniqueId() + ".stats.deaths", Userdata.get().getInt(p.getUniqueId() + ".stats.deaths") + 1);

                    if (!duel.isFfa()) {
                        List<UUID> team1 = duel.getRedTeam();
                        List<UUID> team2 = duel.getBlueTeam();

                        // if only one team has players, end the game.
                        if (team2.isEmpty()) {
                            duel.endGame(team2,team1,p);
                        } else if (team1.isEmpty()) {
                            duel.endGame(team1,team2,p);
                        }
                    } else {
                        try {
                            KitMethods.spectatorKit(p);
                        } catch (IOException er) {
                            er.printStackTrace();
                        }
                        p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                        int losses = Userdata.get().getInt(p.getUniqueId() + ".stats.losses");
                        Userdata.get().set(p.getUniqueId() + ".stats.losses", losses + 1);
                        Userdata.get().set(p.getUniqueId() + ".stats.streak", 0);
                        if (duel.getPlayers().size() <= 1) {
                            List<UUID> winners = duel.getPlayers();
                            List<UUID> losers = duel.getAllContained();
                            duel.endGame(winners,losers,p);

                        }
                    }
                }
            }
        }
    }




    @EventHandler
    public void playerQuitUpdateDuel (PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (ArenaMethods.findPlayerArena(p) != null) {
            String arenaName = ArenaMethods.findPlayerArena(p);
            Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
            if (duel != null) {
                duel.remove(p.getUniqueId());
                duel.getIsDead().put(p.getUniqueId().toString(),true);
                if (p.getLastDamageCause() != null) {
                    if (p.getLastDamageCause().getEntity() instanceof Player) {
                        Userdata.get().set(p.getLastDamageCause().getEntity().getUniqueId()+".stats.kills",Userdata.get().getInt((p.getLastDamageCause().getEntity().getUniqueId()+".stats.kills"))+1);
                    }
                }

                Userdata.get().set(p.getUniqueId()+".stats.deaths",Userdata.get().getInt(p.getUniqueId()+".stats.deaths")+1);
                duel.getInventoryPreview().put(p,InventoryToBase64.itemStackArrayToBase64(p.getInventory().getContents()));
                duel.getEnderchestPreview().put(p,InventoryToBase64.itemStackArrayToBase64(p.getEnderChest().getContents()));
                if (!duel.isFfa()) {
                    List<UUID> team1 = duel.getRedTeam();
                    List<UUID> team2 = duel.getBlueTeam();

                    // if only one team has players, end the game.
                    if (team2.isEmpty()) {
                        duel.endGame(team2,team1,p);
                    } else if (team1.isEmpty()) {
                        duel.endGame(team1,team2,p);
                    }
                } else {
                    try {
                        KitMethods.spectatorKit(p);
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    p.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", "");
                    int losses = Userdata.get().getInt(p.getUniqueId() + ".stats.losses");
                    Userdata.get().set(p.getUniqueId() + ".stats.losses", losses + 1);
                    Userdata.get().set(p.getUniqueId() + ".stats.streak", 0);
                    if (duel.getPlayers().size() <= 1) {
                        List<UUID> winners = duel.getPlayers();
                        List<UUID> losers = duel.getAllContained();
                        duel.endGame(winners,losers,p);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(DuelMethods.preDuel.contains(e.getPlayer()) || KitEditor.inEditor.containsKey(e.getPlayer())) {
            Location to = e.getFrom();
            to.setPitch(e.getTo().getPitch());
            to.setYaw(e.getTo().getYaw());
            e.setTo(to);
        }
    }

    List<String> allowedInteractions = new ArrayList<>() {{
        add("HELMET");
        add("CHESTPLATE");
        add("LEGGINGS");
        add("BOOTS");
    }};

    @EventHandler
    public void preventUseItemsInEditor(PlayerInteractEvent e) {
        if(KitEditor.inEditor.containsKey(e.getPlayer())) {
            for (String allowedTypes : allowedInteractions) {
                if (e.getPlayer().getInventory().getItemInMainHand().getType().name().contains(allowedTypes)) {
                    return;
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void preventTeleportLeave (PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
        if (duel != null && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),duel.getArena())) {
                p.sendMessage(C.failPrefix +""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void preventMoveLeave (PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
        if (duel != null && ArenaMethods.findPlayerArena(p) != null) {
            if (!ArenaMethods.LocationIsInsideArena(e.getTo(),duel.getArena())) {
                p.sendMessage(C.failPrefix +""+ChatColor.ITALIC+"Do not attempt to leave the arena!");
                p.teleport(e.getFrom());
            }
        }
    }

    /**
     * "preventHurtInInactiveArena"
     * When a player is damaged, if they are inside an arena but not in a duel or in a duel's living participants, cancel the event.
     **/
    @EventHandler
    public void preventHurtInInactiveArena (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p) {
            if (ArenaMethods.findPlayerArena(p) != null) {

                if (C.duelManager.findDuelForMember(p.getUniqueId()) == null) {
                    e.setCancelled(true);
                } else {
                    Duel duel = C.duelManager.findDuelForMember(p.getUniqueId());
                    if (!duel.getPlayers().contains(p.getUniqueId()) || duel.getPlayers().size() <= 1) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    /**
     * "announcePlayerDeathsAndResurrections"
     * checks for whenever a player is killed or resurrects using a totem and broadcasts it to all players in the duel.
     * also broadcasts the killer/popper's weapon and method used if applicable.
     **/
    @EventHandler (priority = EventPriority.LOWEST)
    public void announcePlayerDeathsAndResurrections (EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Duel duel = C.duelManager.findDuelForMember(((Player) e.getEntity()).getUniqueId());
            if (duel != null) {
                Player p1 = null;
                if (e.getDamager() instanceof ExplosiveMinecart cart) {
                    if (cart.getLastDamageCause() instanceof EntityDamageByEntityEvent lastDamageCause) {
                        if ((lastDamageCause.getDamager().getType() == EntityType.ARROW || lastDamageCause.getDamager().getType() == EntityType.SPECTRAL_ARROW || lastDamageCause.getDamager().getType() == EntityType.PLAYER)) {
                            Player damager = null;
                            if ((lastDamageCause.getDamager() instanceof Projectile arrow)) {
                                damager = (Player) arrow.getShooter();
                            }
                            if (lastDamageCause.getDamager() instanceof Player) {
                                damager = (Player) lastDamageCause.getDamager();
                            }
                            p1 = damager;
                        }
                    }
                }
                if (e.getDamager() instanceof AbstractArrow arrow) {
                    if (arrow.getShooter() instanceof Player) {
                        p1 = (Player) arrow.getShooter();
                    }
                }
                if (e.getDamager() instanceof Player) {
                    p1 = (Player) e.getDamager();
                }

                Player p2 = (Player) e.getEntity();
                if (p1 == null) {
                    return;
                }

                if (p2.getHealth() - e.getFinalDamage() <= 0 && p2.getNoDamageTicks() == 0) {
                    boolean wasResurrected = p2.getInventory().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING || p2.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING;
                    String outcome = wasResurrected ? "was popped" : "was ";
                    if (!wasResurrected) {
                        if (e.getEntity() instanceof AbstractArrow) {
                            outcome+= "shot by";
                        } else if (e.getEntity() instanceof ExplosiveMinecart) {
                            outcome+= "blown up";
                        } else if (e.getEntity() instanceof Player killer) {
                            outcome+= "slain";
                            switch (killer.getInventory().getItemInMainHand().getType()) {
                                case MACE:
                                    outcome = "was smashed";
                                    break;
                                case NETHERITE_AXE:
                                    outcome = "was beheaded";
                                    break;
                                case TRIDENT:
                                    outcome = "was impaled";
                                    break;
                            }
                        }
                    }

                    String killer;
                    if (e.getDamager() instanceof Player) {
                        killer = "";
                    } else {
                        String typeName = e.getDamager().getType().name();
                        String[] words = typeName.split("_");
                        for (int i = 0; i < words.length; i++) {
                            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
                        }
                        String entityName = words[0]+" "+words[1];
                        String prefix = "";
                        String suffix = "";
                        if (e.getEntity() instanceof AbstractArrow) {
                            suffix = " from ";
                        } else if (e.getEntity() instanceof ExplosiveMinecart) {
                            prefix = "a ";
                            suffix = " shot by ";
                        }

                        killer = prefix+entityName+suffix;
                    }
                    Component finalPrefixComponent = Component.empty();
                    Component finalItemComponent = Component.empty();
                    ItemStack item = p1.getInventory().getItemInMainHand();
                    if (item.hasItemMeta() && item.getType() != Material.AIR) {
                            ItemMeta itemMeta = item.getItemMeta();
                        String displayName = itemMeta.getDisplayName();
                        if (displayName.isEmpty()) {
                            if (item.getMaxStackSize() > 1) {
                                displayName = item.getItemMeta().getItemName() + " x" + item.getAmount();
                            } else {

                                displayName = item.getItemMeta().getItemName();
                                if (item.getItemMeta().getItemName().isEmpty()) {
                                    displayName = item.getType().name().toLowerCase();
                                    displayName.replaceAll("\\_"," ");
                                }
                            }
                        }
                        Component prefix = MiniMessage.miniMessage().deserialize("<#e33630>⚔ ");
                        if (wasResurrected) {
                             prefix = MiniMessage.miniMessage().deserialize("<#ebbc3d>⚔ ");
                        }
                        Component name = MiniMessage.miniMessage().deserialize(ParseMethods.parseLegacyHexToMiniMessage(displayName));
                        Component itemPreviewComponent = Component.empty().append(Component.text("using ")).append(Component.text("[").color(NamedTextColor.AQUA).append(name).append(Component.text("]").color(NamedTextColor.AQUA)));
                        finalItemComponent = Component.empty().append(itemPreviewComponent).hoverEvent(item.asHoverEvent(UnaryOperator.identity()));
                        finalPrefixComponent = prefix;
                    }


                    Component deathMessage = Component.empty().append(finalPrefixComponent).append(Component.text(p2.getName()+" "+outcome+" by " + killer + p1.getName() + " ").append(finalItemComponent));

                    // send the compiled death message to all participants
                    for (UUID allContainedUUIDs : duel.getAllContained()) {
                        Player duelMember = Bukkit.getPlayer(allContainedUUIDs);
                        if (duelMember != null) {
                            duelMember.sendMessage(deathMessage);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEnderPearlHit(ProjectileHitEvent event) {
        // Check if the projectile is an Ender Pearl
        if (event.getEntity() instanceof EnderPearl) {
            EnderPearl enderPearl = (EnderPearl) event.getEntity();

            // Check if the entity who threw the Ender Pearl is a player
            if (enderPearl.getShooter() instanceof Player) {
                Player p = (Player) enderPearl.getShooter();
                if (C.duelManager.findDuelForMember(p.getUniqueId()) != null) {
                    Vector center = ArenaMethods.getArenaRegion(C.duelManager.findDuelForMember(p.getUniqueId()).getArena()).getCenter();

                    Location hitLocation = event.getEntity().getLocation();
                    Location blockHitLocation = event.getHitBlock() != null ? event.getHitBlock().getLocation() : null;

                    // Check if it hit a barrier block
                    if (blockHitLocation != null && blockHitLocation.getBlock().getType() == Material.BARRIER) {
                        // Cancel the event so the pearl doesn't do the default behavior
                        event.setCancelled(true);

                        // Calculate the vector to move the pearl toward the center of the arena
                        // Move the Ender Pearl back a couple of blocks towards the center of the arena
                        double distanceToMove = 1.0; // Adjust this for how far you want to move it
                        Vector directionToCenter = center.subtract(hitLocation.toVector()).normalize().multiply(distanceToMove);

                        // Calculate the new teleport location
                        Location newLocation = hitLocation.add(directionToCenter);

                        // Ensure the Ender Pearl stays above the ground (optional, depending on how you want it to land)
                        if (newLocation.getBlock().getType() == Material.AIR) {
                            // If the location is air, move it to the nearest solid block beneath it
                            while (newLocation.getBlock().getType() == Material.AIR && newLocation.getBlockY() > 0) {
                                newLocation.subtract(0, 1, 0); // Move down one block at a time
                            }
                        }

                        // Teleport the Ender Pearl to the new location
                        enderPearl.teleport(newLocation.add(0,1,0));

                        // Optional: Adjust the velocity to make it fall more naturally instead of shooting downwards
                        enderPearl.setVelocity(enderPearl.getVelocity().multiply(0.8).setY(-0.5)); // Lower Y velocity, adjusted for falling
                    }
                }
            }
        }
    }

    private HashMap<Entity, Player> entityOwner = new HashMap<>();

    public void increaseStat (HashMap<String, HashMap<String, Double>> statMap,Player primary,Player secondary, double amount) {
        if (statMap.get(primary.getUniqueId().toString()) == null) {
            statMap.put(primary.getUniqueId().toString(),new HashMap<>());
        }
        if (statMap.get(primary.getUniqueId().toString()).get(secondary.getUniqueId().toString()) == null) {
            statMap.get(primary.getUniqueId().toString()).put(secondary.getUniqueId().toString(),amount);
            return;
        }
        statMap.get(primary.getUniqueId().toString()).put(secondary.getUniqueId().toString(),statMap.get(primary.getUniqueId().toString()).get(secondary.getUniqueId().toString())+amount);

    }
    public void increaseStat (HashMap<String, Double> statMap,Player primary, double amount) {
        if (statMap.get(primary.getUniqueId().toString()) == null) {
            statMap.put(primary.getUniqueId().toString(),amount);
            return;
        }
        statMap.put(primary.getUniqueId().toString(),statMap.get(primary.getUniqueId().toString())+amount);
    }

    @EventHandler
    public void playerPlaceEntity (EntityPlaceEvent e) {
        Player p = e.getPlayer();
        if (e.getEntity() instanceof ExplosiveMinecart cart) {
            entityOwner.put(cart,p);
        }
    }


    @EventHandler
    public void onEntityDamageByEntity(VehicleDamageEvent event) {
        // Check if the damaged entity is a minecart
        if (event.getVehicle() instanceof ExplosiveMinecart) {
            // Check if the damaging entity is a player
            Player attacker;
            ExplosiveMinecart minecart = (ExplosiveMinecart) event.getVehicle();
            if (event.getAttacker() instanceof Player p) {
                attacker = p;
            } else if (event.getAttacker() instanceof Projectile arrow) {
                if (arrow.getShooter() instanceof Player) {
                    attacker = (Player) arrow.getShooter();
                } else return;
            } else return;
            // Handle the event, for example, you can send a message when the player punches the minecart
            entityOwner.put(minecart, attacker);
        }
    }
    @EventHandler
    public void playerHitMinecart (EntityCombustByEntityEvent e) {
        if (e.getCombuster() instanceof Projectile arrow && e.getEntity() instanceof ExplosiveMinecart cart) {
            Player attacker;
            if (arrow.getShooter() instanceof Player) {
                attacker = (Player) arrow.getShooter();
            } else return;
            entityOwner.put(cart,attacker);
        }
    }
    @EventHandler
    public void incrementStatsInFight(EntityDamageByEntityEvent e) {
        boolean isMarkedDown = false;
        if (e.getEntity() instanceof Player p2) {
            Player p1 = null;
            Duel fight = C.duelManager.findDuelForMember(p2.getUniqueId());
            if (fight == null) return;
            if (e.getDamager() instanceof ExplosiveMinecart cart) {
                if (entityOwner.get(cart) != null) {
                    Player damager = entityOwner.get(cart);
                    p1 = damager;
                    fight.increaseStat(fight.getDamageTypeDealt(),p1,"explosive",e.getFinalDamage());
                    fight.increaseStat(fight.getDamageTypeDealtToPlayer(),p1,p2.getUniqueId().toString(),"explosive",e.getFinalDamage());
                    isMarkedDown = true;
                }
            }

            if (e.getDamager() instanceof Projectile arrow) {
                if (arrow.getShooter() instanceof Player) {
                    p1 = (Player) arrow.getShooter();
                    fight.increaseStat(fight.getDamageTypeDealt(),p1,"ranged",e.getFinalDamage());
                    fight.increaseStat(fight.getDamageTypeDealtToPlayer(),p1,p2.getUniqueId().toString(),"ranged",e.getFinalDamage());
                    isMarkedDown = true;
                }
            }
            if (e.getDamager() instanceof Player) {
                p1 = (Player) e.getDamager();
                if (p1.getInventory().getItemInMainHand().getType().equals(Material.MACE)) {
                    fight.increaseStat(fight.getDamageTypeDealt(),p1,"mace",e.getFinalDamage());
                    fight.increaseStat(fight.getDamageTypeDealtToPlayer(),p1,p2.getUniqueId().toString(),"mace",e.getFinalDamage());
                } else {
                    fight.increaseStat(fight.getDamageTypeDealt(),p1,"melee",e.getFinalDamage());
                    fight.increaseStat(fight.getDamageTypeDealtToPlayer(),p1,p2.getUniqueId().toString(),"melee",e.getFinalDamage());
                }
                isMarkedDown = true;
            }
            if (p1 != null) {
                fight.increaseStat(fight.getDamageDealt(), p1, e.getFinalDamage());
                fight.increaseStat(fight.getDamageDealtToPlayer(),p1,p2.getUniqueId().toString(),e.getFinalDamage());
                fight.increaseStat(fight.getDamageTakenFromPlayer(),p2,p1.getUniqueId().toString(),e.getFinalDamage());
            }
            fight.increaseStat(fight.getDamageTaken(),p2,e.getFinalDamage());


            if (!isMarkedDown) {
                //fight.increaseStat(fight.getDamageTypeDealt(),p1,"unknown",e.getFinalDamage());
                fight.increaseStat(fight.getDamageTypeDealtToPlayer(),p1,p2.getUniqueId().toString(),"unknown",e.getFinalDamage());
            }
            int p2preDurability = totalArmourDurability(p2);
            Player finalP = p1;
            Bukkit.getScheduler().scheduleSyncDelayedTask(C.plugin, new Runnable() {
                @Override
                public void run() {
                    int p2postDurability = totalArmourDurability(p2);
                    if (p2postDurability > p2preDurability) {
                        int difference = p2postDurability - p2preDurability;
                        // increase stats by difference
                        fight.increaseStat(fight.getDurabilityDamageDealtToPlayer(), finalP, p2.getUniqueId().toString(), difference);
                        fight.increaseStat(fight.getDurabilityDamageTakenFromPlayer(), p2, finalP.getUniqueId().toString(), difference);
                    }
                }
            }, 1);
        }

    }

    @EventHandler
    public void spectatorPreventHurt (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player spectator) {
            Duel duel = C.duelManager.findDuelForMember(spectator.getUniqueId());
            if (duel != null) {
                if (duel.getSpectators().contains(spectator.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    public static int totalArmourDurability(Player p) {

        int totalDamage = 0;

        for (ItemStack armour : p.getInventory().getArmorContents()) {
            if (armour != null) {
                totalDamage += armour.getDurability();
            }
        }

        return totalDamage;
    }
}
