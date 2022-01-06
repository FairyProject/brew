package org.imanity.brew.game.state;

import lombok.Getter;
import io.fairyproject.state.StateBase;
import io.fairyproject.task.Task;
import org.imanity.brew.game.GameListener;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.game.Game;
import org.jetbrains.annotations.Nullable;

public abstract class GameStateBase extends StateBase implements GameState, GameListener {

    @Getter
    protected final Game game;

    public GameStateBase(Game game) {
        this.game = game;
    }

    @Nullable
    protected Scene getLobbyScene() {
        return this.game.getLobbyScene();
    }

    public void scheduleUpdates(int delay, int ticks) {
        this.bind(Task.mainRepeated(task -> this.update(), delay, ticks));
    }

}
