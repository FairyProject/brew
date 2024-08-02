package io.fairyproject.brew.listener;

import io.fairyproject.brew.BrewEx;
import org.bukkit.plugin.Plugin;
import io.fairyproject.bukkit.listener.FilteredEventList;
import io.fairyproject.bukkit.listener.FilteredListener;
import io.fairyproject.bukkit.metadata.Metadata;

/**
 * The Listener that handles events while player is not in game
 *
 * @param <T> the Plugin
 */
public abstract class NoGameListener<T extends Plugin> extends FilteredListener<T> {

    public NoGameListener(T plugin) {
        this.initial(plugin, FilteredEventList.builder()
                .filter((player, event) -> !Metadata.provideForPlayer(player).has(BrewEx.GAME))
                .build());
    }

}
