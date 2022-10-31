package dev.imanity.brew.game.state;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.game.GameListener;
import io.fairyproject.state.Signal;
import io.fairyproject.state.State;
import io.fairyproject.state.StateHandler;
import io.fairyproject.state.StateMachine;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GameStateHandler implements StateHandler, GameListener, TerminableConsumer {

    private final CompositeTerminable compositeTerminable = CompositeTerminable.create();
    protected final Game game;

    public GameStateHandler(Game game) {
        this.game = game;
    }

    @Override
    public final void onStart(@NotNull StateMachine stateMachine, @NotNull State state, @Nullable Signal signal) {
        this.start(stateMachine, state, signal);
    }

    protected void start(StateMachine stateMachine, State state, Signal signal) {

    }

    @Override
    public final void onTick(@NotNull StateMachine stateMachine, @NotNull State state) {
        this.tick(stateMachine, state);
    }

    protected void tick(StateMachine stateMachine, State state) {

    }

    @Override
    public final void onStop(@NotNull StateMachine stateMachine, @NotNull State state, @Nullable Signal signal) {
        this.stop(stateMachine, state, signal);

        this.compositeTerminable.closeAndReportException();
        this.compositeTerminable.cleanup();
    }

    protected void stop(StateMachine stateMachine, State state, Signal signal) {
    }

    @Override
    public <T1 extends AutoCloseable> @NotNull T1 bind(T1 t) {
        return this.compositeTerminable.bind(t);
    }

    @Override
    public Game getGame() {
        return this.game;
    }
}
