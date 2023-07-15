package io.fairyproject.brew.game.state;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.game.GameEx;
import io.fairyproject.brew.listener.ListenerPresets;
import io.fairyproject.brew.util.countdown.Countdown;
import io.fairyproject.state.Signal;
import io.fairyproject.state.State;
import io.fairyproject.state.StateMachine;
import io.fairyproject.util.ConditionUtils;

public class PregameStateHandler extends GameStateHandler {

    private final Countdown countdown;
    private final Signal signal;

    public PregameStateHandler(Game game, Countdown countdown, Signal signal) {
        super(game);

        this.countdown = countdown;
        this.signal = signal;
    }

    @Override
    protected void start(StateMachine stateMachine, State state, Signal signal) {
        ConditionUtils.is(this.game.has(GameEx.MINIMUM_PLAYERS), "GameEx.MINIMUM_PLAYERS is not set");
        ConditionUtils.is(this.game.has(GameEx.MAXIMUM_PLAYERS), "GameEx.MAXIMUM_PLAYERS is not set");

        ListenerPresets.disallowBlockModification(this);
        ListenerPresets.disallowDamage(this);
        ListenerPresets.disallowItems(this);
        ListenerPresets.disallowHungers(this);
        ListenerPresets.disallowInteract(this);
    }

    @Override
    protected void tick(StateMachine stateMachine, State s) {
        // check if the game has enough players to start
        if (this.game.getPlayerCount() >= this.game.getOrThrow(GameEx.MINIMUM_PLAYERS) && !this.countdown.isStarted()) {
            // start the timer
            this.countdown.start();
        }

        // check if the game doesn't have enough players
        if (game.getPlayerCount() < game.getOrThrow(GameEx.MINIMUM_PLAYERS) && this.countdown.isStarted()) {
            // reset the timer
            this.countdown.reset();
        }

        // tick the timer if it's started
        if (this.countdown.isStarted()) {
            this.countdown.tick();
            // if the timer is finished, fire the end trigger
            if (this.countdown.isEnded()) {
                stateMachine.signal(signal);
            }
        }
    }

    @Override
    protected void stop(StateMachine stateMachine, State state, Signal signal) {
        // reset the countdown
        this.countdown.reset();
    }
}
