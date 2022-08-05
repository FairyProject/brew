package dev.imanity.brew.scene;

import dev.imanity.brew.game.Game;
import io.fairyproject.metadata.MetadataMap;
import io.fairyproject.metadata.MetadataMapProxy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class SceneBase implements Scene, MetadataMapProxy {

    @Getter
    @Setter
    private String name;
    private Game game;
    private final MetadataMap metadataMap = MetadataMap.create();

    @Override
    public boolean used() {
        return this.game != null;
    }

    @Override
    public Game currentGame() {
        return this.game;
    }

    @Override
    public void currentGame(Game game) {
        this.game = game;
    }

    @Override
    public MetadataMap getMetadataMap() {
        return this.metadataMap;
    }
}
