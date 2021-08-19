package org.imanity.brew.util;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return this.createChunkData(world);
    }
}
