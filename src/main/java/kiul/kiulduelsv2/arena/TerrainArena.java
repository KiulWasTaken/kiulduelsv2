package kiul.kiulduelsv2.arena;

import com.fastasyncworldedit.core.Fawe;
import com.fastasyncworldedit.core.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import io.papermc.lib.PaperLib;
import kiul.kiulduelsv2.Kiulduelsv2;
import kiul.kiulduelsv2.config.Arenadata;
import kiul.kiulduelsv2.duel.DuelMethods;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getServer;

public class TerrainArena extends ChunkGenerator {
    private final static Set<Biome> disallowedBiomes = new HashSet<>() {{
        add(Biome.OCEAN);
        add(Biome.DEEP_OCEAN);
        add(Biome.COLD_OCEAN);
        add(Biome.DEEP_COLD_OCEAN);
        add(Biome.FROZEN_OCEAN);
        add(Biome.DEEP_FROZEN_OCEAN);
        add(Biome.LUKEWARM_OCEAN);
        add(Biome.DEEP_LUKEWARM_OCEAN);
        add(Biome.WARM_OCEAN);
        add(Biome.RIVER);
        add(Biome.FROZEN_RIVER);
    }};

    private static Random random = new Random();

    public static void generateTerrain(World targetWorld, Location targetLocation, int size, Player p, List<Player> waitingForArena) {
        long timeMillis = System.currentTimeMillis();

        String worldName = "arenaTerrain";

// Check if the world is already loaded
        if (p != null) {
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
        if (p != null) {
            long finalTime = System.currentTimeMillis() - timeMillis;
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
        }
        if (waitingForArena != null) {
            long finalTime = System.currentTimeMillis() - timeMillis;
            for (Player waiter : waitingForArena) {
                waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
            }
        }
        if (p != null) {
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "re-rolling biome..");
        }
        if (waitingForArena != null) {
            for (Player waiter : waitingForArena) {
                waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "re-rolling biome..");
            }
        }

        new BukkitRunnable() {

            @Override
            public void run() {


                if (p != null) {
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "rolling location..");
                }
                Location retrievalLocation = returnRetrievalLocation(world);
                if (p != null) {
                    long finalTime = System.currentTimeMillis() - timeMillis;
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                }
//
                Chunk c = retrievalLocation.getChunk();
                Location center = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(8, 0, 8);
//
//                Chunk SEChunk = world.getChunkAt(center.getChunk().getX()+size,center.getChunk().getZ()+size);
//                Chunk NWChunk = world.getChunkAt(center.getChunk().getX()-size,center.getChunk().getZ()-size);
//
//                Location southeast = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 64, SEChunk.getZ() << 4).add(8, 0, 8);
//                Location northwest = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 64, NWChunk.getZ() << 4).add(8, 0, 8);


//                        if (disallowedBiomes.contains(retrievalLocation.getBlock().getBiome()) || disallowedBiomes.contains(southeast.getBlock().getBiome()) || disallowedBiomes.contains(northwest.getBlock().getBiome())) {
//                            if (p != null) {
//                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "getting location..");
//                            }
//                            retrievalLocation = returnRetrievalLocation(world);
//                            if (p != null) {
//                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "..done!");
//                            }
//                            cTick++;
//                            if (p != null) {
//                                p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "attempting.. (" + cTick + ")");
//                            }
//                            if (waitingForArena != null) {
//                                for (Player waiter : waitingForArena) {
//                                    waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "attempting.. (" + cTick + ")");
//                                }
//                            }
//
//                        } else {

                if (p != null) {
                    long finalTime = System.currentTimeMillis() - timeMillis;
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                }
                if (waitingForArena != null) {
                    long finalTime = System.currentTimeMillis() - timeMillis;
                    for (Player waiter : waitingForArena) {
                        waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                    }
                }

                if (p != null) {
                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "adding chunks to list..");
                }
                if (waitingForArena != null) {
                    for (Player waiter : waitingForArena) {
                        waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "adding chunks to list..");
                    }
                }

                ArrayList<Chunk> targetChunks = getChunksAround(targetLocation.getChunk(), size);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ArrayList<ChunkSnapshot> retrieveChunks = getChunkSnapshotsAround(retrievalLocation.getChunk(), size);
                        if (p != null) {
                            long finalTime = System.currentTimeMillis() - timeMillis;
                            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                        }
                        if (waitingForArena != null) {
                            long finalTime = System.currentTimeMillis() - timeMillis;
                            for (Player waiter : waitingForArena) {
                                waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                            }
                        }

                        if (p != null) {
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

                                    if (p != null) {
                                        long finalTime = System.currentTimeMillis() - timeMillis;
                                        p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                                    }
                                    if (waitingForArena != null) {
                                        long finalTime = System.currentTimeMillis() - timeMillis;
                                        for (Player waiter : waitingForArena) {
                                            waiter.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "completed! (" + finalTime + "ms)");
                                        }
//                                        DuelMethods.beginDuel(ArenaMethods.findPlayerArena(waitingForArena.get(0)), waitingForArena);
                                    } else {
                                        for (String arenaName : ArenaMethods.getArenas()) {
                                            if (Arenadata.get().getLocation("arenas." + arenaName + ".center").getChunk() == center.getChunk()) {
                                                if (ArenaMethods.arenasInUse.contains(arenaName)) {
                                                    ArenaMethods.arenasInUse.remove(arenaName);
                                                }
                                            }
                                        }
                                    }

                                    return;
                                }
                                for (int x = 0; x < 16; ++x) {
                                    for (int z = 0; z < 16; ++z) {
                                        for (int y = 0; y < 199; ++y) {
                                            targetChunks.get(tick).getBlock(x, y, z).setType(retrieveChunks.get(tick).getBlockType(x, y, z));
//                                        if (ArenaMethods.liquidFreeze.contains(targetChunks.get(tick).getBlock(x,y,z))) {
//                                            ArenaMethods.liquidFreeze.remove(targetChunks.get(tick).getBlock(x,y,z));
//                                        }
                                        }
                                    }
                                }
                                tick++;
                                if (p != null) {
                                    p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "..");
                                }
                            }
                        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 300, 30L);
                    }
                }.runTaskLater(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 300);

//                        new BukkitRunnable() {
//                            int tick = 0;
//
//                            @Override
//                            public void run() {
//                                if (tick >= targetChunks.size() || tick >= retrieveChunks.size()) {
//                                    cancel();
//
//                                    return;
//                                }
//                                for (int x = 0; x < 16; ++x) {
//                                    for (int z = 0; z < 16; ++z) {
//                                        for (int y = 0; y < 199; ++y) {
//                                            if (targetChunks.get(tick).getBlock(x,y,z).getType().equals(Material.WATER) || targetChunks.get(tick).getBlock(x, y, z).getType().equals(Material.LAVA)) {
//                                                ArenaMethods.liquidFreeze.add(targetChunks.get(tick).getBlock(x,y,z));
//                                            }
//                                        }
//                                    }
//                                }
//                                tick++;
//                            }
//                        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 60, 30L);


                cancel();
//                        }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0L, 50L);
    }


    public static Location returnRetrievalLocation(World world) {
        double x = (Math.random() - 0.5) * 8000;
        double z = (Math.random() - 0.5) * 8000;
        int y = world.getHighestBlockAt((int) x, (int) z).getY();
        Location retrievalLocation = new Location(world, x, y, z);
        return retrievalLocation;
    }

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
                } else if (zTick < radius) {
                    xTick = -radius;
                    zTick++;

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0, 8);
        return chunks;
    }

    public static ArrayList<ChunkSnapshot> getChunkSnapshotsAround(Chunk origin, int radius) {
        World world = origin.getWorld();

        int length = (radius * 2) + 1;
        ArrayList<ChunkSnapshot> chunks = new ArrayList<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        new BukkitRunnable() {
            int xTick = -radius;
            int zTick = -radius;

            @Override
            public void run() {
                if (xTick <= radius) {
                    chunks.add(world.getChunkAt(cX + xTick, cZ + zTick).getChunkSnapshot());

                    xTick++;
                } else if (zTick < radius) {
                    xTick = -radius;
                    zTick++;

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0, 8);
        return chunks;
    }

    public static void loadChunksAround(Chunk origin, int radius) {
        World world = origin.getWorld();

        int length = (radius * 2) + 1;
        ArrayList<ChunkSnapshot> chunks = new ArrayList<>(length * length);

        int cX = origin.getX();
        int cZ = origin.getZ();

        new BukkitRunnable() {
            int xTick = -radius;
            int zTick = -radius;

            @Override
            public void run() {
                if (xTick <= radius) {
                    Chunk chunk = world.getChunkAt(cX + xTick, cZ + zTick);
                    chunk.load();

                    xTick++;
                } else if (zTick < radius) {
                    xTick = -radius;
                    zTick++;

                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Kiulduelsv2.getPlugin(Kiulduelsv2.class), 0, 16);
    }

    public static void generateTerrainPerformant(Location targetLocation, int size) {
        String worldName = "arenaTerrain";
        BukkitScheduler scheduler = getServer().getScheduler();
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator = worldCreator.environment(World.Environment.NORMAL);
            worldCreator = worldCreator.generateStructures(true);
            worldCreator.createWorld();
            world = Bukkit.getWorld(worldName); // Ensure world is loaded
        }

        World finalWorld = world;
        scheduler.runTaskAsynchronously(Kiulduelsv2.getPlugin(Kiulduelsv2.class), () -> {
            double Rx = random.nextDouble(0, 1);
            double Rz = random.nextDouble(0, 1);
            double Lx = (Rx - 0.5) * 8000;
            double Lz = (Rz - 0.5) * 8000;

            // Asynchronously get the center chunk
            finalWorld.getChunkAtAsync(new Location(finalWorld, Lx, 0, Lz)).thenAccept(centerChunk -> {
                Location retrieveCenter = new Location(centerChunk.getWorld(), centerChunk.getX() << 4, 64, centerChunk.getZ() << 4).add(8, 0, 8);

                // Asynchronously get SEChunk and NWChunk
                CompletableFuture<Chunk> SEChunkFuture = finalWorld.getChunkAtAsync(centerChunk.getX() + size, centerChunk.getZ() + size);
                CompletableFuture<Chunk> NWChunkFuture = finalWorld.getChunkAtAsync(centerChunk.getX() - (size - 1), centerChunk.getZ() - (size - 1));

                // When both chunks are loaded, proceed
                CompletableFuture.allOf(SEChunkFuture, NWChunkFuture).thenAccept(v -> {
                    Chunk SEChunk = SEChunkFuture.join();
                    Chunk NWChunk = NWChunkFuture.join();

                    Location SECorner = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 0, SEChunk.getZ() << 4).add(15, 0, 15);
                    Location NWCorner = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 0, NWChunk.getZ() << 4).add(-16, 199, -16);

                    // Check if the biome is allowed asynchronously
                    isBiomeAllowed(finalWorld, SECorner, NWCorner, size).thenAccept(allowed -> {
                        if (!allowed) {
                            // If the biome is not allowed, regenerate the terrain by calling the method recursively
                            generateTerrainPerformant(targetLocation, size);
                            return;
                        }

                        // Proceed with terrain generation
                        scheduler.runTaskAsynchronously(Kiulduelsv2.getPlugin(Kiulduelsv2.class), () -> {
                            CuboidRegion region = new CuboidRegion(
                                    BlockVector3.at(SECorner.getX(), SECorner.getY(), SECorner.getZ()),
                                    BlockVector3.at(NWCorner.getX(), NWCorner.getY(), NWCorner.getZ())
                            );
                            com.sk89q.worldedit.world.World faweWorld = BukkitAdapter.adapt(finalWorld);
                            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
                            clipboard.setOrigin(BlockVector3.at(retrieveCenter.getX(), retrieveCenter.getY(), retrieveCenter.getZ()));

                            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(faweWorld, region, clipboard, region.getMinimumPoint());
                            forwardExtentCopy.setCopyingEntities(false); // Disable copying entities
                            Operations.complete(forwardExtentCopy);

                            Chunk c = targetLocation.getChunk();
                            Location targetCenter = new Location(c.getWorld(), c.getX() << 4, 64, c.getZ() << 4).add(8, 0, 8);

                            com.sk89q.worldedit.world.World fawePasteWorld = BukkitAdapter.adapt(targetLocation.getWorld());
                            try (EditSession editSession = WorldEdit.getInstance().newEditSession(fawePasteWorld)) {
                                Operation operation = new ClipboardHolder(clipboard)
                                        .createPaste(editSession)
                                        .to(BlockVector3.at(targetCenter.getX(), targetCenter.getBlockY(), targetCenter.getZ()))
                                        .build();
                                Operations.complete(operation);

                                editSession.close();

                                scheduler.runTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), () -> {
                                    for (String arenaName : ArenaMethods.getArenas()) {
                                        if (Arenadata.get().getLocation("arenas." + arenaName + ".center").equals(targetLocation)) {
                                            if (ArenaMethods.arenasInUse.contains(arenaName)) {
                                                ArenaMethods.arenasInUse.remove(arenaName);
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    });
                });
            });
        });
    }
//
    private static CompletableFuture<Boolean> isBiomeAllowed(World world, Location SECorner, Location NWCorner, int size) {
        Set<Biome> disallowedBiomes = TerrainArena.disallowedBiomes; // Add disallowed biomes here
        int totalChunks = size * size;

        // Retrieve SECorner and NWCorner chunks asynchronously
        CompletableFuture<Chunk> SEChunkFuture = world.getChunkAtAsync(SECorner);
        CompletableFuture<Chunk> NWChunkFuture = world.getChunkAtAsync(NWCorner);

        // Once both chunks are retrieved, proceed with biome checking
        return CompletableFuture.allOf(SEChunkFuture, NWChunkFuture).thenCompose(v -> {
            Chunk SEChunk = SEChunkFuture.join();
            Chunk NWChunk = NWChunkFuture.join();

            List<CompletableFuture<Void>> futures = new ArrayList<>();
            AtomicInteger disallowedCount = new AtomicInteger(0);

            // Now that the corners' chunks are retrieved, proceed with the biome check
            for (int x = NWChunk.getX(); x <= SEChunk.getX(); x++) {
                for (int z = NWChunk.getZ(); z <= SEChunk.getZ(); z++) {
                    CompletableFuture<Void> future = world.getChunkAtAsync(x, z).thenAccept(chunk -> {
                        Biome biome = chunk.getBlock(0, 60, 0).getBiome(); // Checking one block per chunk
                        if (disallowedBiomes.contains(biome)) {
                            disallowedCount.incrementAndGet();
                        }
                    });
                    futures.add(future);
                }
            }

            // Wait for all futures to complete
            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                    .thenApply(v2 -> {
                        double disallowedPercentage = (double) disallowedCount.get() / totalChunks;
                        return disallowedPercentage <= 0.20;
                    });
        });
    }
}

