package org.imanity.brew.game.event;

import org.bukkit.entity.Player;
import io.fairyproject.bukkit.listener.events.PlayerCallableEvent;
import org.imanity.brew.game.Game;

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
