package dev.imanity.brew.game.state;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.game.GameListener;
import io.fairyproject.state.StateHandler;
import io.fairyproject.state.StateMachine;
import io.fairyproject.state.trigger.Trigger;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GameStateHandler<S, T> implements StateHandler<S, T>, GameListener, TerminableConsumer {

    private final CompositeTerminable compositeTerminable = CompositeTerminable.create();
    protected final Game game;

    public GameStateHandler(Game game) {
        this.game = game;
    }

    @Override
    public final void onStart(@NotNull StateMachine<S, T> stateMachine, @NotNull S s, @Nullable Trigger<T> trigger) {
        this.start(stateMachine, s, trigger);
    }

    protected void start(StateMachine<S, T> stateMachine, S s, Trigger<T> trigger) {

    }

    @Override
    public final void onTick(@NotNull StateMachine<S, T> stateMachine, @NotNull S s) {
        this.tick(stateMachine, s);
    }

    protected void tick(StateMachine<S, T> stateMachine, S s) {

    }

    @Override
    public final void onStop(@NotNull StateMachine<S, T> stateMachine, @NotNull S s, @Nullable Trigger<T> trigger) {
        this.stop(stateMachine, s, trigger);

        this.compositeTerminable.closeAndReportException();
        this.compositeTerminable.cleanup();
    }

    protected void stop(StateMachine<S, T> stateMachine, S s, Trigger<T> trigger) {
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
