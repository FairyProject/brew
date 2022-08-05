package dev.imanity.brew.game.state;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.game.GameListener;
import lombok.Getter;
import io.fairyproject.state.StateBase;
import io.fairyproject.task.Task;

public abstract class GameStateBase extends StateBase implements GameState, GameListener {

    @Getter
    protected final Game game;

    public GameStateBase(Game game) {
        this.game = game;
    }

    @Override
    protected void onUpdate() {
        this.game.update();
    }

    public void scheduleUpdates(int delay, int ticks) {
        this.bind(Task.mainRepeated(task -> this.update(), delay, ticks));
    }

}
