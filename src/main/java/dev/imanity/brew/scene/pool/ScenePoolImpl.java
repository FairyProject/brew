package dev.imanity.brew.scene.pool;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.scene.Scene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScenePoolImpl extends ScenePoolBase {

    private final List<Scene> scenes = new ArrayList<>();

    public ScenePoolImpl(Scene... scenes) {
        this.scenes.addAll(Arrays.asList(scenes));
    }

    @Override
    public List<Scene> all() {
        return this.scenes;
    }

    @Override
    public Scene find(Game game) {
        return this.streamWithFilters(game)
                .unordered()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Scene> findAll(Game game) {
        return this.streamWithFilters(game)
                .collect(Collectors.toList());
    }

    @Override
    public void close() {
        this.scenes.clear();
    }
}
