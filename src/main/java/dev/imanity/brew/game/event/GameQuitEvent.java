package dev.imanity.brew.game.event;

import dev.imanity.brew.game.Game;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.listener.events.PlayerCallableEvent;

public class GameQuitEvent extends PlayerCallableEvent {
    private final Game game;

    public GameQuitEvent(Player who, Game game) {
        super(who);
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
