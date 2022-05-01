package org.imanity.brew.listener;

import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.imanity.brew.game.GameListener;

import java.util.function.Predicate;

@UtilityClass
public class ListenerPresets {

    public void disallowBlockModification(GameListener gameListener) {
        disallowBlockModification(gameListener, null);
    }

    public void disallowBlockModification(GameListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(BlockBreakEvent.class, EventPriority.NORMAL, predicate);
        gameListener.cancelPlayer(BlockPlaceEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowItems(GameListener gameListener) {
        disallowItems(gameListener, null);
    }

    public void disallowItems(GameListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(PlayerDropItemEvent.class, EventPriority.NORMAL, predicate);
        try {
            final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
            gameListener.cancelPlayer(eventClass, EventPriority.NORMAL, predicate);
        } catch (ClassNotFoundException ex) {
            gameListener.cancelPlayer(PlayerPickupItemEvent.class, EventPriority.NORMAL, predicate);
        }
        gameListener.cancelPlayer(PlayerItemDamageEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowInteract(GameListener gameListener) {
        disallowInteract(gameListener, null);
    }

    public void disallowInteract(GameListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(PlayerInteractEvent.class, EventPriority.NORMAL, predicate);
        gameListener.cancelPlayer(PlayerInteractEntityEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowHungers(GameListener gameListener) {
        disallowHungers(gameListener, null);
    }

    public void disallowHungers(GameListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(FoodLevelChangeEvent.class, EventPriority.NORMAL, predicate);
        gameListener.cancelPlayer(PlayerItemConsumeEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowDamage(GameListener gameListener) {
        disallowDamage(gameListener, null);
    }

    public void disallowDamage(GameListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(PlayerDamageEvent.class, EventPriority.NORMAL, predicate);
    }

}
