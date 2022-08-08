package dev.imanity.brew.player;

import dev.imanity.brew.Brew;
import io.fairyproject.Fairy;
import io.fairyproject.bukkit.listener.events.Events;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.mc.MCPlayer;
import io.fairyproject.metadata.MetadataKey;
import io.fairyproject.metadata.MetadataMap;
import io.fairyproject.state.StateBase;
import io.fairyproject.util.terminable.Terminable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerState extends StateBase implements PlayerListener {

    protected static final MetadataKey<PlayerState> STATE = MetadataKey.create("brew:player-state", PlayerState.class);

    static {
        Events.subscribe(PlayerQuitEvent.class)
                .listen(event -> PlayerState.remove(event.getPlayer()))
                .build(Brew.get().getPlugin());
    }

    private final Player player;

    public PlayerState(Player player) {
        this.player = player;
    }

    @Override
    public Player player() {
        return this.player;
    }

    protected void scheduleUpdate(long startDelay, long delay, boolean async) {
        Terminable terminable;
        if (async)
            terminable = Fairy.getTaskScheduler().runAsyncRepeated(this::update, startDelay, delay);
        else
            terminable = Fairy.getTaskScheduler().runRepeated(this::update, startDelay, delay);
        terminable.bindWith(this);
    }

    public static @Nullable PlayerState get(@NotNull Player player) {
        return Metadata.get(player)
                .map(metadataMap -> metadataMap.getOrNull(STATE))
                .orElse(null);
    }

    public static void set(@NotNull Player player, @NotNull PlayerState playerState) {
        MetadataMap metadataMap = Metadata.provide(player);
        // close the old one
        metadataMap.ifPresent(STATE, Terminable::closeAndReportException);
        metadataMap.put(STATE, new PlayerStateValue(MCPlayer.from(player), playerState));
    }

    public static void remove(@NotNull Player player) {
        Metadata.get(player).ifPresent(metadataMap -> metadataMap.ifPresent(STATE, playerState -> {
            playerState.closeAndReportException();
            metadataMap.remove(STATE);
        }));
    }
}
