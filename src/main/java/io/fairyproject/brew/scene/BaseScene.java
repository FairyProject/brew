package io.fairyproject.brew.scene;

import io.fairyproject.brew.game.Game;
import io.fairyproject.metadata.MetadataMap;
import org.jetbrains.annotations.NotNull;

public abstract class BaseScene implements Scene {

    private String name;
    private Game game;
    private final MetadataMap metadataMap = MetadataMap.create();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean isUsed() {
        return this.game != null;
    }

    @Override
    public Game getCurrentGame() {
        return this.game;
    }

    @Override
    public void setCurrentGame(Game game) {
        this.game = game;
    }

    @Override
    public MetadataMap getMetadataMap() {
        return this.metadataMap;
    }

}
