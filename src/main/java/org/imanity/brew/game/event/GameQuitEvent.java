package org.imanity.brew.game.event;

import org.bukkit.entity.Player;
import org.fairy.bukkit.listener.events.PlayerCallableEvent;

public class GameQuitEvent extends PlayerCallableEvent {
    public GameQuitEvent(Player who) {
        super(who);
    }
}
