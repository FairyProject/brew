package org.imanity.brew.listener;

import org.bukkit.plugin.Plugin;
import io.fairyproject.bukkit.listener.FilteredEventList;
import io.fairyproject.bukkit.listener.FilteredListener;
import io.fairyproject.bukkit.metadata.Metadata;
import org.imanity.brew.constant.PlayerConstants;

/**
 * The Listener that handles events while player is not in game
 *
 * @param <T> the Plugin
 */
public abstract class NoGameListener<T extends Plugin> extends FilteredListener<T> {

    public NoGameListener(T plugin) {
        this.initial(plugin, FilteredEventList.builder()
                .filter((player, event) -> !Metadata.provideForPlayer(player).has(PlayerConstants.GAME))
                .build());
    }

}
