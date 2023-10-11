package kiul.kiulduelsv2.arena;

import kiul.kiulduelsv2.Kiulduelsv2;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

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

    public static void generateTerrain (World targetWorld, Location corner1, Location corner2, int size) {
        String worldName = "arenaTerrain";

// Check if the world is already loaded
        World world = Bukkit.getWorld(worldName);

// If the world is not loaded, load it
        if (world == null) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator = worldCreator.environment(World.Environment.NORMAL); // Adjust the environment if needed
            worldCreator = worldCreator.generateStructures(true); // Enable or disable structures as needed
            world = worldCreator.createWorld();
        }
        new BukkitRunnable() {

            World world = Bukkit.getWorld(worldName);
            Location retrievalLocation = returnRetrievalLocation(world);

            @Override
            public void run() {
                if (disallowedBiomes.contains(retrievalLocation.getBlock().getBiome())) {
                    retrievalLocation = returnRetrievalLocation(world);
                } else {
                    Location retrievalCorner1 = retrievalLocation;
                    // Loop through the chunks within the square
                    ArrayList<Chunk> targetChunks = new ArrayList<>();
                    int minX = Math.min(corner1.getBlockX(), corner2.getBlockX()) >> 4;
                    int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX()) >> 4;
                    int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ()) >> 4;
                    int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ()) >> 4;

                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            Chunk chunk = targetWorld.getChunkAt(x, z);
                            targetChunks.add(chunk);
                        }
                    }

                    ArrayList<Chunk> retrieveChunks = new ArrayList<>();
                    int cornerX = retrievalCorner1.getBlockX();
                    int cornerZ = retrievalCorner1.getBlockZ();
                    for (int x = cornerX; x < cornerX + size; x++) {
                        for (int z = cornerZ; z < cornerZ + size; z++) {
                            Chunk chunk = world.getChunkAt(x, z);
                            retrieveChunks.add(chunk);
                        }
                    }


                    new BukkitRunnable() {
                        int tick = 0;

                        @Override
                        public void run() {
                            if (tick >= targetChunks.size() || tick >= retrieveChunks.size()) {
                                cancel();
                                return;
                            }
                            for (int x = 0; x < 16; ++x) {
                                for (int z = 0; z < 16; ++z) {
                                    for (int y = 0; y < 199; ++y) {
                                        targetChunks.get(tick).getBlock(x, y, z).setType(retrieveChunks.get(tick).getBlock(x, y, z).getType());
                                    }
                                }
                            }
                        }
                    }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 5L);
                }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 1L);
    }

    public static Location returnRetrievalLocation (World world) {
        Random random = new Random();
        int x = random.nextInt(-7900,7900);
        int z = random.nextInt(-7900,7900);
        int y = world.getHighestBlockYAt(x,z);
        Location retrievalLocation = new Location(world,x,y,z);
        return retrievalLocation;}
}
