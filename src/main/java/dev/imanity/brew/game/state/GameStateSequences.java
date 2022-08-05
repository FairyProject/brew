package dev.imanity.brew.game.state;

import com.google.common.collect.Lists;
import dev.imanity.brew.game.Game;
import lombok.Getter;
import io.fairyproject.state.State;
import io.fairyproject.state.StateSequences;
import io.fairyproject.task.Task;

import java.util.List;

public class GameStateSequences extends StateSequences implements GameState {

    @Getter
    private final Game game;
    private final int ticks;

    public GameStateSequences(Game game, int ticks, List<State> states) {
        super(states);
        this.game = game;
        this.ticks = ticks;
    }

    public GameStateSequences(Game game, int ticks, State... states) {
        super(Lists.newArrayList(states));
        this.game = game;
        this.ticks = ticks;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ticks != -1) {
            this.scheduleUpdate(this.ticks, this.ticks);
        }
    }

    protected void scheduleUpdate(int delay, int ticks) {
        this.bind(Task.mainRepeated(task -> this.update(), delay, ticks));
    }

    public boolean isConnectable() {
        final State currentState = this.getCurrentState();
        if (currentState instanceof GameState) {
            return ((GameState) currentState).isConnectable();
        }
        return false;
    }

}
