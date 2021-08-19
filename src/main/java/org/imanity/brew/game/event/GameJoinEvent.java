package org.imanity.brew.game.event;

import org.bukkit.entity.Player;
import org.fairy.bukkit.listener.events.PlayerCallableEvent;

public class GameJoinEvent extends PlayerCallableEvent {
    public GameJoinEvent(Player who) {
        super(who);
    }
}
