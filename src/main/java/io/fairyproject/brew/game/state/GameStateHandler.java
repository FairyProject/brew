package io.fairyproject.brew.game.state;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.game.GameListener;
import io.fairyproject.bukkit.events.BukkitEventFilter;
import io.fairyproject.event.EventNode;
import io.fairyproject.state.Signal;
import io.fairyproject.state.State;
import io.fairyproject.state.StateHandler;
import io.fairyproject.state.StateMachine;
import io.fairyproject.util.terminable.Terminable;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GameStateHandler implements StateHandler, GameListener, TerminableConsumer {

    private final CompositeTerminable compositeTerminable;
    protected final EventNode<Event> eventNode;
    protected final Game game;

    public GameStateHandler(Game game) {
        this.game = game;
        this.compositeTerminable = CompositeTerminable.create();
        this.eventNode = EventNode.event("game-state:" + this.getClass().getName(), BukkitEventFilter.ALL, this::isEventFilter);
    }

    @Override
    public final void onStart(@NotNull StateMachine stateMachine, @NotNull State state, @Nullable Signal signal) {
        this.game.getEventNode().addChild(this.eventNode);
        this.eventNode.bindWith(this);

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

    protected boolean isEventFilter(Event event) {
        return true;
    }

    @Override
    public <T1 extends Terminable> @NotNull T1 bind(T1 t) {
        return this.compositeTerminable.bind(t);
    }

    @Override
    public Game getGame() {
        return this.game;
    }
}
