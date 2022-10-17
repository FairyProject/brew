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

    public Terminable disallowBlockModification(PlayerListener playerListener) {
        return disallowBlockModification(playerListener, null);
    }

    public Terminable disallowBlockModification(PlayerListener playerListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        playerListener.cancelPlayer(BlockBreakEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        playerListener.cancelPlayer(BlockPlaceEvent.class, EventPriority.NORMAL, predicate).bindWith(events);

        return events;
    }

    public Terminable disallowItems(PlayerListener playerListener) {
        return disallowItems(playerListener, null);
    }

    public Terminable disallowItems(PlayerListener playerListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        playerListener.cancelPlayer(PlayerDropItemEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        try {
            final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName("org.bukkit.event.entity.EntityPickupItemEvent");
            playerListener.cancelPlayer(eventClass, EventPriority.NORMAL, predicate).bindWith(events);
        } catch (ClassNotFoundException ex) {
            playerListener.cancelPlayer(PlayerPickupItemEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        }
        playerListener.cancelPlayer(PlayerItemDamageEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        return events;
    }

    public Terminable disallowInteract(PlayerListener playerListener) {
        return disallowInteract(playerListener, null);
    }

    public Terminable disallowInteract(PlayerListener playerListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        playerListener.cancelPlayer(PlayerInteractEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        playerListener.cancelPlayer(PlayerInteractEntityEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        return events;
    }

    public Terminable disallowHungers(PlayerListener playerListener) {
        return disallowHungers(playerListener, null);
    }

    public Terminable disallowHungers(PlayerListener playerListener, Predicate<Player> predicate) {
        CompositeTerminable events = CompositeTerminable.create();
        playerListener.cancelPlayer(FoodLevelChangeEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        playerListener.cancelPlayer(PlayerItemConsumeEvent.class, EventPriority.NORMAL, predicate).bindWith(events);
        return events;
    }

    public Terminable disallowDamage(PlayerListener playerListener) {
        return disallowDamage(playerListener, null);
    }

    public Terminable disallowDamage(PlayerListener gameListener, Predicate<Player> predicate) {
        return gameListener.cancelPlayer(PlayerDamageEvent.class, EventPriority.NORMAL, predicate);
    }

}
