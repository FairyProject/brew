package dev.imanity.brew.game.state;

import org.bukkit.entity.Player;
import io.fairyproject.bukkit.timer.TimerUnhandled;
import dev.imanity.brew.game.Game;

import java.util.Collection;

public abstract class GameStateTimer extends TimerUnhandled {

    protected final GameState gameState;

    public GameStateTimer(GameState gameState, long startTime, long duration) {
        super(startTime, duration);
        this.gameState = gameState;
    }

    public GameStateTimer(GameState gameState, long duration) {
        super(duration);
        this.gameState = gameState;
    }

    @Override
    protected void onStart() {
        this.gameState.bind(this);
    }

    public Game getGame() {
        return this.gameState.getGame();
    }

    @Override
    public Collection<? extends Player> getReceivers() {
        return this.getGame().getPlayers();
    }
}
