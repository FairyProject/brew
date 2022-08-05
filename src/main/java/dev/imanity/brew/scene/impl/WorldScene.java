package dev.imanity.brew.scene.impl;

import dev.imanity.brew.game.Game;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.util.CustomLocation;
import dev.imanity.brew.scene.SceneBase;
import dev.imanity.brew.util.EmptyChunkGenerator;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Getter
public class WorldScene extends SceneBase implements Serializable {

    private final String worldName;
    private final CustomLocation spawnLocation;
    private final Function<WorldCreator, WorldCreator> worldConfigurer;
    private final boolean save;

    public WorldScene(String worldName, CustomLocation spawnLocation, Function<WorldCreator, WorldCreator> worldConfigurer, boolean save) {
        this.worldName = worldName;
        this.spawnLocation = spawnLocation;
        this.worldConfigurer = worldConfigurer;
        this.save = save;
    }

    public WorldScene(String worldName, CustomLocation spawnLocation) {
        this(worldName, spawnLocation, worldCreator -> worldCreator
                .environment(World.Environment.NORMAL)
                .type(WorldType.FLAT)
                .generator(new EmptyChunkGenerator()),
                false);
    }

    @Override
    public CompletableFuture<?> init(Game game) {
        if (Bukkit.getWorld(this.worldName) != null)
            return CompletableFuture.completedFuture(null);

        return this.loadWorld(this.worldConfigurer
                .apply(new WorldCreator(this.worldName)))
                .thenAccept(world -> {
                    world.setAutoSave(false);
                    this.spawnLocation.setWorld(worldName);
                    this.onInitialize();
                });
    }

    public CompletableFuture<World> loadWorld(WorldCreator worldCreator) {
        return CompletableFuture.completedFuture(worldCreator.createWorld());
    }

    public void unloadWorld(World world) {
        Bukkit.unloadWorld(world, this.save);
    }

    public void onInitialize() {

    }

    @Override
    public void teleport(Player player, Game game) {
        this.spawnLocation.teleport(player, 3f, true);
    }

    @Override
    public void close() {
        final World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            return;
        }

        this.unloadWorld(world);
    }

}
