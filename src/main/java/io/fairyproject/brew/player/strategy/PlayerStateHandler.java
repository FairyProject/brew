package io.fairyproject.brew.player.strategy;

import io.fairyproject.state.StateHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerStateHandler extends StateHandler {

    @NotNull Player player();

}
