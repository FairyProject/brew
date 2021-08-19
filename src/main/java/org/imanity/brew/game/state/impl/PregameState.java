package org.imanity.brew.game.state.impl;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.fairy.locale.Locales;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.GameAttributes;
import org.imanity.brew.game.event.GameJoinEvent;
import org.imanity.brew.game.event.GameQuitEvent;
import org.imanity.brew.listener.ListenerPresets;

public class PregameState extends TimedGameState {

    public PregameState(Game game, long duration) {
        super(game, duration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Preconditions.checkArgument(this.game.has(GameAttributes.MINIMUM_PLAYERS), "Require attribute Attributes.MINIMUM_PLAYERS");
        Preconditions.checkArgument(this.game.has(GameAttributes.MAXIMUM_PLAYERS), "Require attribute Attributes.MAXIMUM_PLAYERS");

        ListenerPresets.disallowBlockModification(this, null);
        ListenerPresets.disallowInteract(this, null);
        ListenerPresets.disallowItems(this, null);
        ListenerPresets.disallowDamage(this, null);

        listenPlayer(GameJoinEvent.class)
                .listen(event -> {
                    final Player player = event.getPlayer();
                    this.getLobbyScene().teleport(player, this.getGame());
                    this.update();
                }).build();

        listenPlayer(GameQuitEvent.class)
                .listen(event -> this.update());

        this.pause();
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        final int playerCount = this.game.getPlayerCount();
        if (playerCount < this.game.get(GameAttributes.MINIMUM_PLAYERS)) {
            if (this.isPaused()) {
                return;
            }

            this.pause();
        } else if (playerCount >= this.game.get(GameAttributes.MAXIMUM_PLAYERS)) {
            if (!this.isPaused()) {
                return;
            }

            this.unpause();
            this.timer.restart(System.currentTimeMillis());
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
    protected String getAnnounceMessage(Player player, int seconds) {
        return Locales.translate(player, "pregame.countdown");
    }
}
