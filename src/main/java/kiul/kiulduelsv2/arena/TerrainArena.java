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
        }

        scheduler.runTaskAsynchronously(Kiulduelsv2.getPlugin(Kiulduelsv2.class), () -> {
            double Rx = random.nextDouble(0, 1);
            double Rz = random.nextDouble(0, 1);
            double Lx = (Rx - 0.5) * 8000;
            double Lz = (Rz - 0.5) * 8000;

            scheduler.runTask(Kiulduelsv2.getPlugin(Kiulduelsv2.class), () -> {
                Location center = new Location(world, Lx, 0, Lz);
                Chunk ch = center.getChunk();
                Location retrieveCenter = new Location(ch.getWorld(), ch.getX() << 4, 64, ch.getZ() << 4).add(8, 0, 8);

                Chunk SEChunk = world.getChunkAt(center.getChunk().getX() + size, center.getChunk().getZ() + size);
                Chunk NWChunk = world.getChunkAt(center.getChunk().getX() - (size - 1), center.getChunk().getZ() - (size - 1));

                Location SECorner = new Location(SEChunk.getWorld(), SEChunk.getX() << 4, 0, SEChunk.getZ() << 4).add(15, 0, 15);
                Location NWCorner = new Location(NWChunk.getWorld(), NWChunk.getX() << 4, 0, NWChunk.getZ() << 4).add(-16, 199, -16);
                if (!isBiomeAllowed(world, SECorner, NWCorner, size)) {
                    generateTerrainPerformant(targetLocation, size); // Recursively retry
                    return;
                }

                scheduler.runTaskAsynchronously(Kiulduelsv2.getPlugin(Kiulduelsv2.class), () -> {



                    CuboidRegion region = new CuboidRegion(BlockVector3.at(SECorner.getX(), SECorner.getY(), SECorner.getZ()), BlockVector3.at(NWCorner.getX(), NWCorner.getY(), NWCorner.getZ()));
                    com.sk89q.worldedit.world.World faweWorld = BukkitAdapter.adapt(world);
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
                                if (Arenadata.get().getLocation("arenas." + arenaName + ".center") == targetLocation) {
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
    }

    private static boolean isBiomeAllowed(World world, Location SECorner, Location NWCorner, int size) {
        Set<Biome> disallowedBiomes = TerrainArena.disallowedBiomes; // Add disallowed biomes here
        int disallowedCount = 0;
        int totalChunks = size * size;

        for (int x = SECorner.getChunk().getX(); x <= NWCorner.getChunk().getX(); x++) {
            for (int z = SECorner.getChunk().getZ(); z <= NWCorner.getChunk().getZ(); z++) {
                Chunk chunk = world.getChunkAt(x, z);
                Biome biome = chunk.getBlock(0, 0, 0).getBiome(); // Checking one block per chunk

                if (disallowedBiomes.contains(biome)) {
                    disallowedCount++;
                }
            }
        }

        double disallowedPercentage = (double) disallowedCount / totalChunks;
        return disallowedPercentage <= 0.2;
    }
}

