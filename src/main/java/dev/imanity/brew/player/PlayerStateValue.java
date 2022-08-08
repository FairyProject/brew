package dev.imanity.brew.player;

import io.fairyproject.mc.MCPlayer;
import io.fairyproject.metadata.TransientValue;
import org.jetbrains.annotations.NotNull;

public class PlayerStateValue implements TransientValue<PlayerState> {

    private final MCPlayer mcPlayer;
    private PlayerState playerState;

    protected PlayerStateValue(@NotNull MCPlayer mcPlayer, @NotNull PlayerState playerState) {
        this.mcPlayer = mcPlayer;
        this.playerState = playerState;
    }

    @Override
    public PlayerState getOrNull() {
        return this.shouldExpire() ? null : this.playerState;
    }

    @Override
    public boolean shouldExpire() {
        boolean shouldExpire = !this.mcPlayer.isOnline();
        if (shouldExpire) {
            this.playerState.closeAndReportException();
            this.playerState = null;
        }
        return shouldExpire;
    }
}
