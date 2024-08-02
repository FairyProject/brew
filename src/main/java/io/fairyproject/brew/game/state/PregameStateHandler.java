package io.fairyproject.brew.game.state;

import io.fairyproject.brew.FairyBrew;
import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.listener.EventNodePresets;
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
        ConditionUtils.is(this.game.has(FairyBrew.MINIMUM_PLAYERS_KEY), "FairyBrew.MINIMUM_PLAYERS_KEY is not set");
        ConditionUtils.is(this.game.has(FairyBrew.MAXIMUM_PLAYERS_KEY), "FairyBrew.MAXIMUM_PLAYERS_KEY is not set");

        this.eventNode.addChild(EventNodePresets.getCancelBlockModification());
        this.eventNode.addChild(EventNodePresets.getCancelDamage());
        this.eventNode.addChild(EventNodePresets.getCancelItemInteraction());
        this.eventNode.addChild(EventNodePresets.getCancelHungerLevel());
        this.eventNode.addChild(EventNodePresets.getCancelWorldInteraction());
    }

    @Override
    protected void tick(StateMachine stateMachine, State s) {
        // check if the game has enough players to start
        if (this.game.getPlayerCount() >= this.game.getOrThrow(FairyBrew.MINIMUM_PLAYERS_KEY) && !this.countdown.isStarted()) {
            // start the timer
            this.countdown.start();
        }

        // check if the game doesn't have enough players
        if (game.getPlayerCount() < game.getOrThrow(FairyBrew.MINIMUM_PLAYERS_KEY) && this.countdown.isStarted()) {
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
