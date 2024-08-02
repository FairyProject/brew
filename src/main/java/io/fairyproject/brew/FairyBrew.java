package io.fairyproject.brew;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.team.Team;
import io.fairyproject.bukkit.events.BukkitEventFilter;
import io.fairyproject.bukkit.events.BukkitEventNode;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PostInitialize;
import io.fairyproject.container.PreDestroy;
import io.fairyproject.event.EventNode;
import io.fairyproject.metadata.MetadataKey;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@InjectableComponent
@RequiredArgsConstructor
public class FairyBrew {

    public static final MetadataKey<Game> GAME_KEY = MetadataKey.create("brew:game", Game.class);
    public static final MetadataKey<Team> TEAM_KEY = MetadataKey.create("brew:team", Team.class);
    public static final MetadataKey<Integer> MINIMUM_PLAYERS_KEY = MetadataKey.createIntegerKey("brew:min-players");
    public static final MetadataKey<Integer> MAXIMUM_PLAYERS_KEY = MetadataKey.createIntegerKey("brew:max-players");

    private final EventNode<Event> eventNode = EventNode.create("brew", BukkitEventFilter.ALL, null);
    private final BukkitEventNode bukkitEventNode;

    @PostInitialize
    public void onPostInitialize() {
        this.bukkitEventNode.addChild(this.eventNode);
    }

    @PreDestroy
    public void onPreDestroy() {
        this.bukkitEventNode.removeChild(this.eventNode);
    }

    public Game createGame() {
        return new Game(this.eventNode);
    }

    public EventNode<Event> getEventNode() {
        return this.eventNode;
    }

    public @Nullable Game getGameByPlayer(@NotNull Player player) {
        return Metadata.getForPlayer(player)
                .flatMap(metadata -> metadata.get(GAME_KEY))
                .orElse(null);
    }

    public @Nullable Team getTeamByPlayer(@NotNull Player player) {
        return Metadata.getForPlayer(player)
                .flatMap(metadata -> metadata.get(TEAM_KEY))
                .orElse(null);
    }

    public void setGame(@NotNull Player player, @NotNull Game game) {
        Metadata.provideForPlayer(player)
                .put(GAME_KEY, game);
    }

    public void setTeam(@NotNull Player player, @NotNull Team team) {
        Metadata.provideForPlayer(player)
                .put(TEAM_KEY, team);
    }

    public int getMinimumPlayers(@NotNull Game game) {
        return game.getMetadataMap()
                .get(MINIMUM_PLAYERS_KEY)
                .orElse(-1);
    }

    public int getMaximumPlayers(@NotNull Game game) {
        return game.getMetadataMap()
                .get(MAXIMUM_PLAYERS_KEY)
                .orElse(-1);
    }

    public void setMinimumPlayers(@NotNull Game game, int minimumPlayers) {
        game.getMetadataMap()
                .put(MINIMUM_PLAYERS_KEY, minimumPlayers);
    }

    public void setMaximumPlayers(@NotNull Game game, int maximumPlayers) {
        game.getMetadataMap()
                .put(MAXIMUM_PLAYERS_KEY, maximumPlayers);
    }

}
