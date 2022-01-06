package org.imanity.brew.game.event;

import org.bukkit.entity.Player;
import io.fairyproject.bukkit.listener.events.PlayerCallableEvent;

public class GameJoinEvent extends PlayerCallableEvent {
    public GameJoinEvent(Player who) {
        super(who);
    }
}
