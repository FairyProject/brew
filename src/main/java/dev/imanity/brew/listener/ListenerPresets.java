package dev.imanity.brew.listener;

import dev.imanity.brew.player.PlayerListener;
import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import io.fairyproject.util.terminable.Terminable;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
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

    public Terminable disallowBlockModification(PlayerListener gameListener) {
        return disallowBlockModification(gameListener, null);
    }

    public Terminable disallowBlockModification(PlayerListener gameListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        gameListener.cancelPlayer(BlockBreakEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        gameListener.cancelPlayer(BlockPlaceEvent.class, EventPriority.NORMAL, predicate).bindWith(events);

        return events;
    }

    public Terminable disallowItems(PlayerListener gameListener) {
        return disallowItems(gameListener, null);
    }

    public Terminable disallowItems(PlayerListener gameListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        gameListener.cancelPlayer(PlayerDropItemEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        try {
            final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
            gameListener.cancelPlayer(eventClass, EventPriority.NORMAL, predicate).bindWith(events);
        } catch (ClassNotFoundException ex) {
            gameListener.cancelPlayer(PlayerPickupItemEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        }
        gameListener.cancelPlayer(PlayerItemDamageEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        return events;
    }

    public Terminable disallowInteract(PlayerListener gameListener) {
        return disallowInteract(gameListener, null);
    }

    public Terminable disallowInteract(PlayerListener gameListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        gameListener.cancelPlayer(PlayerInteractEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        gameListener.cancelPlayer(PlayerInteractEntityEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        return events;
    }

    public Terminable disallowHungers(PlayerListener gameListener) {
        return disallowHungers(gameListener, null);
    }

    public Terminable disallowHungers(PlayerListener gameListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        gameListener.cancelPlayer(FoodLevelChangeEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        gameListener.cancelPlayer(PlayerItemConsumeEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        return events;
    }

    public Terminable disallowDamage(PlayerListener gameListener) {
        return disallowDamage(gameListener, null);
    }

    public Terminable disallowDamage(PlayerListener gameListener, Predicate<Player> predicate) {
        return gameListener.cancelPlayer(PlayerDamageEvent.class, EventPriority.NORMAL, predicate);
    }

}
