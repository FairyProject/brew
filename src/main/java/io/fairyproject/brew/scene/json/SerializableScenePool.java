package io.fairyproject.brew.scene.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.scene.pool.AbstractScenePool;
import org.bukkit.Bukkit;
import io.fairyproject.brew.scene.Scene;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SerializableScenePool extends AbstractScenePool {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Set<Scene> scenes;

    public SerializableScenePool(File folder, SceneSerializer<?> sceneSerializer) {
        if (!folder.isDirectory()) {
            throw new UnsupportedOperationException("SerializableArenaProvider(File) requires to be a Directory");
        }

        this.scenes = Sets.newConcurrentHashSet();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            try {
                final String s = file.getName().substring(0, file.getName().lastIndexOf(".") - 1);
                final JsonObject jsonObject = GSON.fromJson(s, JsonObject.class);

                final Scene scene = sceneSerializer.deserialize(jsonObject);
                this.scenes.add(scene);

                Bukkit.getLogger().info("Loaded scene " + scene.getName());
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        this.scenes.clear();
    }

    @Override
    public Scene find(Game game) {
        return this.streamWithFilters(game)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Scene> all() {
        return ImmutableList.copyOf(this.scenes);
    }

    @Override
    public List<Scene> findAll(Game game) {
        return this.streamWithFilters(game).collect(Collectors.toList());
    }
}
