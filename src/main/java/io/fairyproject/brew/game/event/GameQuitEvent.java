package io.fairyproject.brew.game.event;

import io.fairyproject.brew.game.Game;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.listener.events.PlayerCallableEvent;

public class GameQuitEvent extends PlayerCallableEvent implements GameEvent {
    private final Game game;

    public GameQuitEvent(Player who, Game game) {
        super(who);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
