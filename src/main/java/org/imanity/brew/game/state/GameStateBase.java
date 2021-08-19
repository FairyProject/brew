package org.imanity.brew.game.state;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.fairy.bukkit.listener.events.EventSubscribeBuilder;
import org.fairy.bukkit.listener.events.Events;
import org.fairy.bukkit.player.PlayerEventRecognizer;
import org.fairy.state.StateBase;
import org.fairy.task.Task;
import org.imanity.brew.Brew;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.event.GameEvent;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class GameStateBase extends StateBase implements GameState {

    @Getter
    protected final Game game;

    public GameStateBase(Game game) {
        this.game = game;
    }

    protected Scene getLobbyScene() {
        return this.game.getLobbyScene();
    }

    public void scheduleUpdates(int delay, int ticks) {
        this.bind(Task.mainRepeated(task -> this.update(), delay, ticks));
    }

    @SafeVarargs
    public final <T extends Event & Cancellable> void cancelPlayer(Class<T> type, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        this.cancelPlayer(type, EventPriority.NORMAL, attributes);
    }

    @SafeVarargs
    public final <T extends Event & Cancellable> void cancelPlayer(Class<T> type, EventPriority priority, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        this.cancelPlayer(type, priority, null, attributes);
    }

    @SafeVarargs
    public final <T extends Event & Cancellable> void cancelPlayer(Class<T> type, EventPriority priority, Predicate<Player> playerPredicate, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        this.listenPlayer(type, priority, true, playerPredicate, attributes)
                .listen(event -> event.setCancelled(true))
                .build();
    }

    @SafeVarargs
    public final <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        return this.listenPlayer(type, EventPriority.NORMAL, attributes);
    }

    @SafeVarargs
    public final <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        return this.listenPlayer(type, priority, false, attributes);
    }

    @SafeVarargs
    public final <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, boolean ignoreCancelled, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        return this.listenPlayer(type, EventPriority.NORMAL, ignoreCancelled, attributes);
    }

    @SafeVarargs
    public final <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        return this.listenPlayer(type, priority, ignoreCancelled, null, attributes);
    }

    @SafeVarargs
    public final <T extends Event> EventSubscribeBuilder<T> listenPlayer(Class<T> type, EventPriority priority, boolean ignoreCancelled, Predicate<Player> playerPredicate, Class<PlayerEventRecognizer.Attribute<T>>... attributes) {
        if (!PlayerEventRecognizer.isTypePossible(type) && attributes.length == 0) {
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
                    final Player player = PlayerEventRecognizer.tryRecognize(event, attributes);
                    if (player == null)
                        return false;
                    return this.game.isPlayer(player) && (playerPredicate == null || playerPredicate.test(player));
                })
                .plugin(Brew.INSTANCE.getPlugin())
                .bindWith(this);
    }

    public final <T extends GameEvent> void listenScene(Class<T> type, Consumer<T> consumer) {
        this.listenScene(type, EventPriority.NORMAL, consumer);
    }

    public final <T extends GameEvent> void listenScene(Class<T> type, EventPriority priority, Consumer<T> consumer) {
        Events.subscribe(type)
                .priority(priority)
                .filter(event -> this.game == event.getGame())
                .listen(consumer)
                .build(Brew.INSTANCE.getPlugin())
                .bindWith(this);
    }

}
