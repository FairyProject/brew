package dev.imanity.brew.scene.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.imanity.brew.game.Game;
import org.bukkit.Bukkit;
import dev.imanity.brew.scene.Scene;
import dev.imanity.brew.scene.pool.ScenePoolBase;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SerializableScenePool extends ScenePoolBase {

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

                Bukkit.getLogger().info("Loaded scene " + scene.name());
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
