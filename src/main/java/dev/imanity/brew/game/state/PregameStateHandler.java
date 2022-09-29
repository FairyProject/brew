package dev.imanity.brew.game.state;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.game.GameEx;
import dev.imanity.brew.listener.ListenerPresets;
import dev.imanity.brew.util.countdown.Countdown;
import io.fairyproject.state.StateMachine;
import io.fairyproject.state.trigger.Trigger;
import io.fairyproject.util.ConditionUtils;

public class PregameStateHandler<S, T> extends GameStateHandler<S, T> {

    private final Countdown countdown;
    private final Trigger<T> trigger;

    public PregameStateHandler(Game game, Countdown countdown, Trigger<T> trigger) {
        super(game);

        this.countdown = countdown;
        this.trigger = trigger;
    }

    @Override
    protected void start(StateMachine<S, T> stateMachine, S s, Trigger<T> trigger) {
        ConditionUtils.is(this.game.has(GameEx.MINIMUM_PLAYERS), "GameEx.MINIMUM_PLAYERS is not set");
        ConditionUtils.is(this.game.has(GameEx.MAXIMUM_PLAYERS), "GameEx.MAXIMUM_PLAYERS is not set");

        ListenerPresets.disallowBlockModification(this);
        ListenerPresets.disallowDamage(this);
        ListenerPresets.disallowItems(this);
        ListenerPresets.disallowHungers(this);
        ListenerPresets.disallowInteract(this);
    }

    @Override
    protected void tick(StateMachine<S, T> stateMachine, S s) {
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
                stateMachine.fire(this.trigger);
            }
        }
    }
}
