package org.imanity.brew.game.state.impl;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.state.GameStateBase;
import org.imanity.brew.game.state.GameStateTimer;

public abstract class TimedGameState extends GameStateBase {

    @Getter
    protected final Timer timer;

    public TimedGameState(Game game, long duration) {
        this(game, System.currentTimeMillis(), duration);
    }

    public TimedGameState(Game game, long startTime, long duration) {
        super(game);
        this.timer = new Timer(startTime, duration);
        this.bind(this.timer);
    }

    @Override
    protected void onStart() {
        this.timer.start();
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

    protected String getAnnounceMessage(Player player, int seconds) {
        return "Time Remaining: <seconds>";
    }

    protected String getSidebarText(Player player) {
        return "&fTimer: &e" + this.timer.getSecondsRemaining() + "s";
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
        public String getAnnounceMessage(Player player, int seconds) {
            return TimedGameState.this.getAnnounceMessage(player, seconds);
        }

        @Override
        public String getScoreboardText(Player player) {
            return TimedGameState.this.getSidebarText(player);
        }

        @Override
        protected void onElapsed() {
            TimedGameState.this.end();
        }
    }
}
