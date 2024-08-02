package io.fairyproject.brew.game;

import org.bukkit.entity.Player;
import io.fairyproject.bukkit.timer.TimerBase;
import io.fairyproject.bukkit.timer.TimerList;

import java.util.Collection;

public abstract class GameTimer extends TimerBase {

    private final Game game;

    public GameTimer(Game game, long startTime, long duration, TimerList timerList) {
        super(startTime, duration, timerList);
        this.game = game;
    }

    public GameTimer(Game game, long startTime, long duration) {
        super(startTime, duration);
        this.game = game;
    }

    public GameTimer(Game game, long duration, TimerList timerList) {
        super(duration, timerList);
        this.game = game;
    }

    public GameTimer(Game game, long duration) {
        super(duration);
        this.game = game;
    }

    @Override
    public Collection<? extends Player> getReceivers() {
        return this.game.getPlayers();
    }
}
