package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class TerrainArena extends ChunkGenerator {

    static ArrayList<Biome> disallowedBiomes = new ArrayList<>() {{
        add(Biome.OCEAN);
        add(Biome.DEEP_OCEAN);
        add(Biome.COLD_OCEAN);
        add(Biome.DEEP_COLD_OCEAN);
        add(Biome.FROZEN_OCEAN);
        add(Biome.DEEP_FROZEN_OCEAN);
        add(Biome.LUKEWARM_OCEAN);
        add(Biome.DEEP_LUKEWARM_OCEAN);

    }};

    public static void generateTerrain (World targetWorld, Location targetLocation, int size,Player p,List<Player> waitingForArena) {
        long timeMillis = System.currentTimeMillis();

        String worldName = "arenaTerrain";

// Check if the world is already loaded
        if (p!=null) {
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "instantiating world object..");
        }
        if (waitingForArena != null) {
            for (Player waiter : waitingForArena) {
                waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "instantiating world object..");
            }
        }
        World world = Bukkit.getWorld(worldName);


// If the world is not loaded, load it
        if (world == null) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator = worldCreator.environment(World.Environment.NORMAL); // Adjust the environment if needed
            worldCreator = worldCreator.generateStructures(true); // Enable or disable structures as needed
            worldCreator.createWorld();
        }
        if (p!=null) {
            long finalTime = System.currentTimeMillis()-timeMillis;
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
        }
        if (waitingForArena != null) {
            long finalTime = System.currentTimeMillis()-timeMillis;
            for (Player waiter : waitingForArena) {
                waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
            }
        }
        if (p!=null) {
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "re-rolling biome..");
        }
        if (waitingForArena != null) {
            for (Player waiter : waitingForArena) {
                waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "re-rolling biome..");
            }
        }

        new BukkitRunnable() {
            int tick = 1;

            World world = Bukkit.getWorld(worldName);
            Location retrievalLocation = returnRetrievalLocation(world);

            int size = 4;
            Chunk c = p.getLocation().getChunk();
            Location center = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(8, 0, 8);

            Chunk SEChunk = world.getChunkAt(center.add(size*16,0,size*16));
            Chunk NWChunk = world.getChunkAt(center.add(-size*16,0,-size*16));

            Location southeast = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 64, SEChunk.getZ() << 4).add(8, 0, 8);
            Location northwest = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 64, NWChunk.getZ() << 4).add(8, 0, 8);

            @Override
            public void run() {
                if (disallowedBiomes.contains(retrievalLocation.getBlock().getBiome()) || disallowedBiomes.contains(southeast.getBlock().getBiome()) || disallowedBiomes.contains(northwest.getBlock().getBiome())) {
                    retrievalLocation = returnRetrievalLocation(world);

                    if (p!=null) {
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "attempting.. (" + tick + ")");
                    }
                    if (waitingForArena != null) {
                        for (Player waiter : waitingForArena) {
                            waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "attempting.. (" + tick + ")");
                        }
                    }

                } else {

                    if (p!=null) {
                        long finalTime = System.currentTimeMillis()-timeMillis;
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                    }
                    if (waitingForArena != null) {
                        long finalTime = System.currentTimeMillis()-timeMillis;
                        for (Player waiter : waitingForArena) {
                            waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                        }
                    }

                    if (p!=null) {
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "adding chunks to list..");
                    }
                    if (waitingForArena != null) {
                        for (Player waiter : waitingForArena) {
                            waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "adding chunks to list..");
                        }
                    }

                    ArrayList<Chunk> targetChunks = getChunksAround(targetLocation.getChunk(),4);
                    ArrayList<Chunk> retrieveChunks = getChunksAround(retrievalLocation.getChunk(),4);

                    if (p!=null) {
                        long finalTime = System.currentTimeMillis()-timeMillis;
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                    }
                    if (waitingForArena != null) {
                        long finalTime = System.currentTimeMillis()-timeMillis;
                        for (Player waiter : waitingForArena) {
                            waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                        }
                    }

                    if (p!=null) {
                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "pasting chunks..");
                    }
                    if (waitingForArena != null) {
                        for (Player waiter : waitingForArena) {
                            waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "pasting chunks..");
                        }
                    }

                    new BukkitRunnable() {
                        int tick = 0;

                        @Override
                        public void run() {
                            if (tick >= targetChunks.size() || tick >= retrieveChunks.size()) {
                                cancel();

                                if (p!=null) {
                                    long finalTime = System.currentTimeMillis()-timeMillis;
                                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                                }
                                if (waitingForArena != null) {
                                    long finalTime = System.currentTimeMillis()-timeMillis;
                                    for (Player waiter : waitingForArena) {
                                        waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                                    }
                                    DuelMethods.beginDuel(ArenaMethods.findPlayerArena(waitingForArena.get(0)),waitingForArena);
                                }

                                return;
                            }
                            for (int x = 0; x < 16; ++x) {
                                for (int z = 0; z < 16; ++z) {
                                    for (int y = 0; y < 199; ++y) {
                                        targetChunks.get(tick).getBlock(x, y, z).setType(retrieveChunks.get(tick).getBlock(x, y, z).getType());

                                    }
                                }
                            }
                            tick++;
                        }
                    }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 60, 30L);
                    cancel();
                }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 20L);

    }

    public static Location returnRetrievalLocation (World world) {
        Random random = new Random();
        int x = random.nextInt(-7900,7900);
        int z = random.nextInt(-7900,7900);
        int y = world.getHighestBlockAt(x,z).getY();
        Location retrievalLocation = new Location(world,x,y,z);
        return retrievalLocation;}

    public static ArrayList<Chunk> getChunksAround(Chunk origin, int radius) {
        World world = origin.getWorld();

        int length = (radius * 2) + 1;
        ArrayList<Chunk> chunks = new ArrayList<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        new BukkitRunnable() {
            int xTick = -radius;
            int zTick = -radius;

            @Override
            public void run() {
                if (xTick <= radius) {
                    chunks.add(world.getChunkAt(cX + xTick, cZ + zTick));
                xTick++;
                } else if (zTick <= radius) {
                    xTick = -radius;
                    zTick++;
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class),0,8);
        return chunks;
    }
}
