package dev.imanity.brew.game.state.impl;

import com.google.common.base.Preconditions;
import dev.imanity.brew.game.Game;
import io.fairyproject.libs.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import dev.imanity.brew.game.GameEx;
import dev.imanity.brew.game.event.GameJoinEvent;
import dev.imanity.brew.game.event.GameQuitEvent;
import dev.imanity.brew.listener.ListenerPresets;

public class PregameState extends TimedGameState {

    private final long duration;
    public PregameState(Game game, long duration) {
        super(game, duration);
        this.duration = duration;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Preconditions.checkArgument(this.game.has(GameEx.MINIMUM_PLAYERS), "Require attribute Attributes.MINIMUM_PLAYERS");
        Preconditions.checkArgument(this.game.has(GameEx.MAXIMUM_PLAYERS), "Require attribute Attributes.MAXIMUM_PLAYERS");

        ListenerPresets.disallowBlockModification(this, null);
        ListenerPresets.disallowInteract(this, null);
        ListenerPresets.disallowItems(this, null);
        ListenerPresets.disallowDamage(this, null);
        ListenerPresets.disallowHungers(this, null);

        listenPlayer(GameJoinEvent.class)
                .listen(event -> this.update())
                .build();

        listenPlayer(GameQuitEvent.class)
                .listen(event -> this.update())
                .build();

        this.pause();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        final int playerCount = this.game.getPlayerCount();
        if (playerCount < this.game.getOrThrow(GameEx.MINIMUM_PLAYERS)) {
            if (this.isPaused()) {
                return;
            }

            this.pause();
        } else if (playerCount >= this.game.getOrThrow(GameEx.MINIMUM_PLAYERS)) {
            if (!this.isPaused()) {
                return;
            }

            this.unpause();
            this.timer.restart(System.currentTimeMillis());
            this.timer.setDuration(this.duration);
        }
    }

    @Override
    protected void onEnded() {
        super.onEnded();
    }

    @Override
    public boolean isConnectable() {
        return true;
    }

    @Override
    protected boolean isAnnouncing(int time) {
        return true;
    }

    @Override
    protected Component getAnnounceMessage(Player player, int seconds) {
        return Component.translatable()
                .key("pregame.countdown")
                .args(Component.text(seconds))
                .build();
    }
}
