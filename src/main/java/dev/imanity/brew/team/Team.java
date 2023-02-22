package dev.imanity.brew.team;

import com.google.common.collect.Sets;
import dev.imanity.brew.game.Game;
import dev.imanity.brew.player.PlayerListener;
import dev.imanity.brew.util.task.IntervalTask;
import io.fairyproject.bukkit.FairyBukkitPlatform;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.bukkit.util.Players;
import io.fairyproject.metadata.MetadataMap;
import io.fairyproject.metadata.MetadataMapProxy;
import io.fairyproject.util.terminable.Terminable;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeClosingException;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import lombok.Getter;
import io.fairyproject.libs.kyori.adventure.audience.Audience;
import io.fairyproject.libs.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.entity.Player;
import dev.imanity.brew.team.event.TeamJoinEvent;
import dev.imanity.brew.team.event.TeamQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Team implements
        Iterable<Player>,
        ForwardingAudience,
        MetadataMapProxy,
        Terminable,
        TerminableConsumer,
        PlayerListener {

    private final Game game;
    private final int id;
    private final Set<UUID> playerUuids;
    private final MetadataMap metadataMap;
    private final IntervalTask cleanup;
    private final CompositeTerminable compositeTerminable;

    public Team(Game game, int id) {
        this.game = game;
        this.id = id;
        this.playerUuids = Sets.newConcurrentHashSet();
        this.metadataMap = MetadataMap.create();
        this.compositeTerminable = CompositeTerminable.create();
        this.cleanup = IntervalTask.create(this.metadataMap::cleanup, 20 * 60);
    }

    @Nullable
    public Player getFirstPlayer() {
        for (Player player : this) {
            if (player.isOnline()) {
                return player;
            }
        }
        return null;
    }

    public int getPlayerCount() {
        return this.playerUuids.size();
    }

    public boolean addPlayer(Player player) {
        return this.addPlayer(player, TeamJoinEvent.Reason.PLUGIN);
    }

    public boolean addPlayer(Player player, TeamJoinEvent.Reason reason) {
        if (this.playerUuids.contains(player.getUniqueId())) {
            return false;
        }

        Team previous = TeamEx.getTeamByPlayer(player);
        if (previous != null) {
            if (!previous.removePlayer(player, TeamQuitEvent.Reason.JOIN_ANOTHER_TEAM)) {
                return false;
            }
        }

        return new TeamJoinEvent(player, reason, this).supplyCancelled(cancel -> {
            if (!cancel) {
                this.playerUuids.add(player.getUniqueId());
                Metadata.provideForPlayer(player).put(TeamEx.TEAM, this);
                return true;
            }

            return false;
        });
    }

    public boolean removePlayer(Player player) {
        return this.removePlayer(player, TeamQuitEvent.Reason.PLUGIN);
    }

    public boolean removePlayer(Player player, TeamQuitEvent.Reason reason) {
        if (!playerUuids.contains(player.getUniqueId())) {
            return false;
        }

        return new TeamQuitEvent(player, reason, this).supplyCancelled(cancel -> {
            if (!cancel) {
                this.playerUuids.remove(player.getUniqueId());
                Metadata.provideForPlayer(player).remove(TeamEx.TEAM);
                return true;
            }
            return false;
        });
    }

    /**
     * update the team
     * it should be called every tick
     */
    public void update() {
        this.cleanup.update();
    }

    public List<Player> getPlayers() {
        return Players.transformUuids(this.playerUuids);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Players.streamUuids(this.playerUuids)
                .map(FairyBukkitPlatform.AUDIENCES::player)
                .collect(Collectors.toList());
    }

    public MetadataMap getMetadataMap() {
        return metadataMap;
    }

    @NotNull
    @Override
    public Iterator<Player> iterator() {
        return this.getPlayers().iterator();
    }

    @Override
    public String toString() {
        return "Team{" +
                "game=" + game +
                ", id=" + id +
                '}';
    }

    @Override
    public void close() throws CompositeClosingException {
        this.metadataMap.clear();
        this.compositeTerminable.close();
    }

    @Override
    public boolean isPlayer(@NotNull Player player) {
        return this.playerUuids.stream().anyMatch(uuid -> player.getUniqueId() == uuid);
    }

    @Override
    public <T extends Terminable> @NotNull T bind(@NotNull T t) {
        return this.compositeTerminable.bind(t);
    }
}
