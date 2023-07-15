package io.fairyproject.brew.game;

import io.fairyproject.brew.player.PlayerListener;
import io.fairyproject.brew.Brew;
import io.fairyproject.bukkit.util.JavaPluginUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import io.fairyproject.bukkit.listener.events.Events;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.brew.game.event.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@Deprecated
public interface GameListener extends PlayerListener, TerminableConsumer {

    Game getGame();

    @Override
    default boolean isPlayer(@NotNull Player player) {
        return this.getGame().isPlayer(player);
    }

    default <T extends Event & GameEvent> void listenGame(Class<T> type, Consumer<T> consumer) {
        this.listenGame(type, EventPriority.NORMAL, consumer);
    }

    default <T extends Event & GameEvent> void listenGame(Class<T> type, EventPriority priority, Consumer<T> consumer) {
        Events.subscribe(type)
                .priority(priority)
                .filter(event -> this.getGame() == event.getGame())
                .listen(consumer)
                .build(JavaPluginUtil.getProvidingPlugin(this.getClass()))
                .bindWith(this);
    }

}
