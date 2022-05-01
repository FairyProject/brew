package org.imanity.brew.game.state.impl;

import lombok.Getter;
import io.fairyproject.libs.kyori.adventure.text.Component;
import io.fairyproject.libs.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.state.GameStateBase;
import org.imanity.brew.game.state.GameStateTimer;

import java.util.Collection;

@Getter
public abstract class TimedGameState extends GameStateBase {

    protected Timer timer;
    protected final long duration;

    public TimedGameState(Game game, long duration) {
        super(game);
        this.duration = duration;
    }

    @Override
    protected void onStart() {
        this.timer = new Timer(System.currentTimeMillis(), duration);
        this.timer.start();
        this.bind(this.timer);
    }

    @Override
    protected void onUpdate() {
        if (this.timer.isPaused()) {
            return;
        }
        this.timer.tick();
    }

    @Override
    protected void onEnded() {
        if (!this.timer.isClosed()) {
            this.timer.clear();
        }
    }

    @Override
    protected void onPause() {
        this.timer.pause();
    }

    @Override
    protected void onUnpause() {
        this.timer.unpause();
    }

    @Override
    protected boolean canEnd() {
        return this.timer.isElapsed() && !this.timer.isPaused();
    }

    public final void extend(long duration) {
        this.timer.extend(duration);
    }

    public final void duration(long duration) {
        this.timer.setDuration(duration);
    }

    protected boolean isAnnouncing(int time) {
        return false;
    }

    protected Component getAnnounceMessage(Player player, int seconds) {
        return Component.text("Time Remaining: ", NamedTextColor.YELLOW).append(Component.text(seconds));
    }

    public Collection<? extends Player> getReceivers() {
        return this.game.getPlayers();
    }

    public class Timer extends GameStateTimer {

        public Timer(long startTime, long duration) {
            super(TimedGameState.this, startTime, duration);
        }

        public Timer(long duration) {
            super(TimedGameState.this, duration);
        }

        @Override
        protected boolean isAnnouncing(int time) {
            return TimedGameState.this.isAnnouncing(time);
        }

        @Override
        public Component getAnnounceMessage(Player player, int seconds) {
            return TimedGameState.this.getAnnounceMessage(player, seconds);
        }

        @Override
        public Collection<? extends Player> getReceivers() {
            return TimedGameState.this.getReceivers();
        }

        @Override
        protected void onElapsed() {
            TimedGameState.this.end();
        }
    }
}
