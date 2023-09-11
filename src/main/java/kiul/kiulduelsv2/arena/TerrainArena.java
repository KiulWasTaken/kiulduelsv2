package kiul.kiulduelsv2.arena;

import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.storage.IChunkLoader;
import net.minecraft.world.level.levelgen.ChunkGeneratorAbstract;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class TerrainArena extends ChunkGenerator {

    public void test (World targetWorld) {
        Random random = new Random();
        WorldCreator wc = new WorldCreator("world name");

        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);

        World world = wc.createWorld();


    }
}
