package dev.imanity.brew.game;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import dev.imanity.brew.player.PlayerListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import io.fairyproject.bukkit.listener.events.EventSubscribeBuilder;
import io.fairyproject.bukkit.listener.events.Events;
import io.fairyproject.bukkit.player.PlayerEventRecognizer;
import io.fairyproject.util.terminable.TerminableConsumer;
import dev.imanity.brew.Brew;
import dev.imanity.brew.game.event.GameEvent;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface GameListener extends PlayerListener, TerminableConsumer {

    Game getGame();

    default <T extends GameEvent> void listenGame(Class<T> type, Consumer<T> consumer) {
        this.listenGame(type, EventPriority.NORMAL, consumer);
    }

    default <T extends GameEvent> void listenGame(Class<T> type, EventPriority priority, Consumer<T> consumer) {
        Events.subscribe(type)
                .priority(priority)
                .filter(event -> this.getGame() == event.getGame())
                .listen(consumer)
                .build(Brew.get().getPlugin())
                .bindWith(this);
    }

}
