package dev.imanity.brew.player.strategy;

import dev.imanity.brew.player.PlayerState;
import io.fairyproject.state.State;
import io.fairyproject.state.strategy.StateStrategyBase;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerStateStrategy extends StateStrategyBase {
    public PlayerStateStrategy(int priority) {
        super(priority);
    }

    @Override
    public @NotNull PlayerState parent() {
        return (PlayerState) super.parent();
    }

    public @NotNull Player player() {
        return this.parent().player();
    }

    @Override
    public void setParent(@NotNull State state) {
        if (state instanceof PlayerState) {
            super.setParent(state);
            return;
        }
        throw new IllegalArgumentException("PlayerStateStrategy can only be added to PlayerState...");
    }
}
