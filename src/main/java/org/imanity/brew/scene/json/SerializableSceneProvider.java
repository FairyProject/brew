package org.imanity.brew.scene.json;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imanity.brew.game.Game;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.scene.SceneProvider;
import org.imanity.brew.scene.SceneProviderBase;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SerializableSceneProvider extends SceneProviderBase {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final Logger LOGGER = LogManager.getLogger();

    private final File folder;
    private final SceneSerializer<?> sceneSerializer;
    private Set<Scene> scenes;

    public SerializableSceneProvider(File folder, SceneSerializer<?> sceneSerializer) {
        this.folder = folder;
        this.sceneSerializer = sceneSerializer;
        if (!this.folder.isDirectory()) {
            throw new UnsupportedOperationException("SerializableArenaProvider(File) requires to be a Directory");
        }
    }

    @Override
    public void load() {
        this.scenes = Sets.newConcurrentHashSet();
        for (File file : Objects.requireNonNull(this.folder.listFiles())) {
            try {
                final String s = FileUtils.readFileToString(file);
                final JsonObject jsonObject = GSON.fromJson(s, JsonObject.class);

                final Scene scene = this.sceneSerializer.deserialize(jsonObject);
                this.scenes.add(scene);

                LOGGER.info("Loaded scene " + scene.getName());
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.scenes.clear();
    }

    @Override
    public Scene find(Game game) {
        return this.searchAvailable(this.scenes)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Scene> getActiveArenas() {
        return ImmutableList.copyOf(this.scenes);
    }

    @Override
    public List<Scene> getAvailableArenas(Game game) {
        return this.searchAvailable(this.scenes).collect(Collectors.toList());
    }
}
