package org.imanity.brew.game;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import io.fairyproject.bukkit.listener.events.EventSubscribeBuilder;
import io.fairyproject.bukkit.listener.events.Events;
import io.fairyproject.bukkit.player.PlayerEventRecognizer;
import io.fairyproject.util.terminable.TerminableConsumer;
import org.imanity.brew.Brew;
import org.imanity.brew.game.event.GameEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GameListener extends TerminableConsumer {

    Game getGame();

    default <T extends Event & Cancellable> void cancelPlayer(Class<T> type) {
        this.cancelPlayer(type, EventPriority.NORMAL, Collections.emptyList());
    }

    default <T extends Event & Cancellable> void cancelPlayer(Class<T> type, EventPriority priority) {
        this.cancelPlayer(type, priority, null, Collections.emptyList());
    }

    default <T extends Event & Cancellable> void cancelPlayer(Class<T> type, EventPriority priority, Predicate<Player> playerPredicate) {
        this.listenPlayer(type, priority, true, playerPredicate, Collections.emptyList())
                .listen(event -> event.setCancelled(true))
                .build();
    }

    default <U extends Event, T extends U> void cancelPlayer(Class<T> type, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        this.cancelPlayer(type, EventPriority.NORMAL, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> void cancelPlayer(Class<T> type, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        this.cancelPlayer(type, EventPriority.NORMAL, attributes);
    }

    default <U extends Event, T extends U> void cancelPlayer(Class<T> type, EventPriority priority, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        this.cancelPlayer(type, priority, null, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> void cancelPlayer(Class<T> type, EventPriority priority, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        this.cancelPlayer(type, priority, null, attributes);
    }

    default <U extends Event, T extends U> void cancelPlayer(Class<T> type, EventPriority priority, Predicate<Player> playerPredicate, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        Preconditions.checkArgument(Cancellable.class.isAssignableFrom(type), "The event class wasn't extends on Cancellable");
        this.listenPlayer(type, priority, true, playerPredicate, ImmutableList.copyOf(attributes))
                .listen(event -> ((Cancellable) event).setCancelled(true))
                .build();
    }

    default <U extends Event, T extends U> void cancelPlayer(Class<T> type, EventPriority priority, Predicate<Player> playerPredicate, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        Preconditions.checkArgument(Cancellable.class.isAssignableFrom(type), "The event class wasn't extends on Cancellable");
        this.listenPlayer(type, priority, true, playerPredicate, attributes)
                .listen(event -> ((Cancellable) event).setCancelled(true))
                .build();
    }

    default <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type) {
        return this.listenPlayer(type, EventPriority.NORMAL);
    }

    default <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority) {
        return this.listenPlayer(type, priority, false);
    }

    default <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, boolean ignoreCancelled) {
        return this.listenPlayer(type, EventPriority.NORMAL, ignoreCancelled);
    }

    default <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled) {
        return this.listenPlayer(type, priority, ignoreCancelled, (Predicate<Player>)null);
    }

    default <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, Predicate<Player> playerPredicate) {
        return this.listenPlayer(type, priority, ignoreCancelled, playerPredicate, Collections.emptyList());
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        return this.listenPlayer(type, EventPriority.NORMAL, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        return this.listenPlayer(type, EventPriority.NORMAL, attributes);
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        return this.listenPlayer(type, priority, false, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        return this.listenPlayer(type, priority, false, attributes);
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, boolean ignoreCancelled, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        return this.listenPlayer(type, EventPriority.NORMAL, ignoreCancelled, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, boolean ignoreCancelled, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        return this.listenPlayer(type, EventPriority.NORMAL, ignoreCancelled, attributes);
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        return this.listenPlayer(type, priority, ignoreCancelled, null, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        return this.listenPlayer(type, priority, ignoreCancelled, null, attributes);
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, Predicate<Player> playerPredicate, Class<? extends PlayerEventRecognizer.Attribute<U>>... attributes) {
        return this.listenPlayer(type, priority, ignoreCancelled, playerPredicate, ImmutableList.copyOf(attributes));
    }

    default <U extends Event, T extends U> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, Predicate<Player> playerPredicate, List<Class<? extends PlayerEventRecognizer.Attribute<U>>> attributes) {
        if (!PlayerEventRecognizer.isTypePossible(type) && attributes.size() == 0) {
            throw new UnsupportedOperationException("Impossible to get Player from event type " + type.getSimpleName());
        }
        final EventSubscribeBuilder<T> builder = Events
                .subscribe(type)
                .priority(priority);
        if (ignoreCancelled && Cancellable.class.isAssignableFrom(type)) {
            builder.filter(event -> !((Cancellable) event).isCancelled());
        }
        return builder
                .filter(event -> {
                    final Player player = PlayerEventRecognizer.tryRecognize(event, attributes.toArray(new Class[0]));
                    if (player == null)
                        return false;
                    return this.getGame().isPlayer(player) && (playerPredicate == null || playerPredicate.test(player));
                })
                .plugin(Brew.INSTANCE.getPlugin())
                .bindWith(this);
    }

    default <T extends GameEvent> void listenGame(Class<T> type, Consumer<T> consumer) {
        this.listenGame(type, EventPriority.NORMAL, consumer);
    }

    default <T extends GameEvent> void listenGame(Class<T> type, EventPriority priority, Consumer<T> consumer) {
        Events.subscribe(type)
                .priority(priority)
                .filter(event -> this.getGame() == event.getGame())
                .listen(consumer)
                .build(Brew.INSTANCE.getPlugin())
                .bindWith(this);
    }

}
