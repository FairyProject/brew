package io.fairyproject.brew.scene.impl;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.scene.BaseScene;
import io.fairyproject.brew.util.EmptyChunkGenerator;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.mc.util.Position;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Getter
public class WorldScene extends BaseScene implements Serializable {

    private final String worldName;
    private final Position position;
    private final Function<WorldCreator, WorldCreator> worldConfigurer;
    private final boolean save;

    public WorldScene(String worldName, Position position, Function<WorldCreator, WorldCreator> worldConfigurer, boolean save) {
        this.worldName = worldName;
        this.position = position;
        this.worldConfigurer = worldConfigurer;
        this.save = save;
    }

    public WorldScene(String worldName, Position position) {
        this(worldName, position, worldCreator -> worldCreator
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
                    this.position.setWorld(worldName);
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
        this.position.teleport(MCPlayer.from(player), 3f, true);
    }

    @Override
    public void close() {
        final World world = Bukkit.getWorld(this.worldName);
        if (world == null) {
            return;
        }

        this.unloadWorld(world);
    }

    @Override
    public boolean isClosed() {
        return Bukkit.getWorld(this.worldName) == null;
    }

}
