package org.imanity.brew.scene.impl;

import com.google.common.collect.Lists;
import org.imanity.brew.game.Game;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.scene.SceneProviderBase;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConstantSceneProvider extends SceneProviderBase {

    private final List<Scene> scenes;

    public ConstantSceneProvider(Scene... scenes) {
        this.scenes = Lists.newArrayList(scenes);
    }

    @Override
    public void load() {

    }

    @Override
    public Scene find(Game game) {
        return this.searchAvailable(this.scenes).findFirst().orElse(null);
    }

    @Override
    public List<Scene> getActiveArenas() {
        return Collections.unmodifiableList(this.scenes);
    }

    @Override
    public List<Scene> getAvailableArenas(Game game) {
        return this.searchAvailable(this.scenes).collect(Collectors.toList());
    }

    @Override
    public void close() {
        this.scenes.clear();
    }
}
