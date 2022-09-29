package dev.imanity.brew.player.strategy;

import io.fairyproject.state.StateHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerStateHandler<S, T> extends StateHandler<S, T> {

    @NotNull Player player();

}
