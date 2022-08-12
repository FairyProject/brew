package dev.imanity.brew.listener;

import dev.imanity.brew.player.PlayerListener;
import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.function.Predicate;

@UtilityClass
public class ListenerPresets {

    public void disallowBlockModification(PlayerListener gameListener) {
        disallowBlockModification(gameListener, null);
    }

    public void disallowBlockModification(PlayerListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(BlockBreakEvent.class, EventPriority.NORMAL, predicate);
        gameListener.cancelPlayer(BlockPlaceEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowItems(PlayerListener gameListener) {
        disallowItems(gameListener, null);
    }

    public void disallowItems(PlayerListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(PlayerDropItemEvent.class, EventPriority.NORMAL, predicate);
        try {
            final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
            gameListener.cancelPlayer(eventClass, EventPriority.NORMAL, predicate);
        } catch (ClassNotFoundException ex) {
            gameListener.cancelPlayer(PlayerPickupItemEvent.class, EventPriority.NORMAL, predicate);
        }
        gameListener.cancelPlayer(PlayerItemDamageEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowInteract(PlayerListener gameListener) {
        disallowInteract(gameListener, null);
    }

    public void disallowInteract(PlayerListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(PlayerInteractEvent.class, EventPriority.NORMAL, predicate);
        gameListener.cancelPlayer(PlayerInteractEntityEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowHungers(PlayerListener gameListener) {
        disallowHungers(gameListener, null);
    }

    public void disallowHungers(PlayerListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(FoodLevelChangeEvent.class, EventPriority.NORMAL, predicate);
        gameListener.cancelPlayer(PlayerItemConsumeEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowDamage(PlayerListener gameListener) {
        disallowDamage(gameListener, null);
    }

    public void disallowDamage(PlayerListener gameListener, Predicate<Player> predicate) {
        gameListener.cancelPlayer(PlayerDamageEvent.class, EventPriority.NORMAL, predicate);
    }

}
