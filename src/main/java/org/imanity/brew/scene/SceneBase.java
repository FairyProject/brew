package org.imanity.brew.scene;

import lombok.Getter;
import lombok.Setter;
import org.imanity.brew.game.Game;

public abstract class SceneBase implements Scene {

    @Getter
    @Setter
    private String name;
    private Game game;

    @Override
    public boolean isRunning() {
        return this.game != null;
    }

    @Override
    public Game getRunningGame() {
        return this.game;
    }

    @Override
    public void setRunningBy(Game game) {
        this.game = game;
    }
}
