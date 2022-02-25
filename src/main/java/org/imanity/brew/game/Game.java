package org.imanity.brew.game;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import io.fairyproject.bukkit.FairyBukkitPlatform;
import io.fairyproject.mc.metadata.PlayerOnlineValue;
import io.fairyproject.task.Task;
import lombok.Getter;
import lombok.Setter;
import io.fairyproject.libs.kyori.adventure.audience.Audience;
import io.fairyproject.libs.kyori.adventure.audience.ForwardingAudience;
import org.bukkit.entity.Player;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.bukkit.util.Players;
import io.fairyproject.metadata.MetadataMap;
import io.fairyproject.metadata.MetadataMapProxy;
import io.fairyproject.util.terminable.Terminable;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import org.imanity.brew.game.event.GameJoinEvent;
import org.imanity.brew.game.event.GameQuitEvent;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.constant.PlayerConstants;
import org.imanity.brew.game.state.GameState;
import org.imanity.brew.team.Team;
import org.imanity.brew.team.TeamEx;
import org.imanity.brew.team.event.TeamQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Game implements Terminable, TerminableConsumer, ForwardingAudience, Iterable<Player>, MetadataMapProxy, GameListener {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger();

    @Getter
    @Nullable
    private Scene lobbyScene;
    @Getter
    private GameState state;
    @Getter
    @Setter
    private int maxPlayerPerTeam = 1;
    @Getter
    private final int id;
    private final Set<UUID> players = Sets.newConcurrentHashSet();
    private final Map<Integer, Team> teams = new ConcurrentHashMap<>();
    private final AtomicInteger teamCounter = new AtomicInteger();
    private final CompositeTerminable compositeTerminable = CompositeTerminable.create();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final MetadataMap metadataMap = MetadataMap.create();

    public Game() {
        this.id = ID_COUNTER.getAndIncrement();
    }

    public void setLobbyScene(Scene scene) {
        this.lobbyScene = scene;
        this.lobbyScene.setRunningBy(this);
        this.bind(scene);
    }

    public void setState(GameState state) {
        this.state = state;
        this.bind(state);
    }

    @NotNull
    @Override
    public <T extends AutoCloseable> T bind(@NotNull T t) {
        return this.compositeTerminable.bind(t);
    }

    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
        Metadata.provideForPlayer(player).put(PlayerConstants.GAME, PlayerOnlineValue.create(this, player.getUniqueId()));

        new GameJoinEvent(player, this).call();
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
        Metadata.provideForPlayer(player).remove(PlayerConstants.GAME);

        // remove player from their team
        Team team = TeamEx.getTeamByPlayer(player);
        if (team != null) {
            team.removePlayer(player, TeamQuitEvent.Reason.DISCONNECTED);
        }

        new GameQuitEvent(player, this).call();
    }

    public Team getTeam(int id) {
        return this.teams.get(id);
    }

    public Team createTeam() {
        return this.createTeam(this.teamCounter.getAndIncrement());
    }

    public Team createTeam(int id) {
        Preconditions.checkArgument(!this.hasTeam(id), "create team while team with id " + id + " already exists.");
        return this.addTeam(new Team(this, id));
    }

    public Team addTeam(Team team) {
        this.teams.put(team.getId(), team);
        return team;
    }

    public Team removeTeam(int id) {
        return this.teams.remove(id);
    }

    public Team removeTeam(Team team) {
        return this.teams.remove(team.getId());
    }

    public Collection<Team> getTeams() {
        return Collections.unmodifiableCollection(this.teams.values());
    }

    public boolean hasTeam(Team team) {
        return this.hasTeam(team.getId());
    }

    public boolean hasTeam(int id) {
        return this.teams.containsKey(id);
    }

    protected Predicate<Player> filterPlayer() {
        return this::isPlayer;
    }

    public boolean isPlayer(Player player) {
        return this.players.contains(player.getUniqueId());
    }

    public CompletableFuture<?> start() {
        CompletableFuture<?> completableFuture = CompletableFuture.completedFuture(null);

        // Initialize basic stuff
        if (this.lobbyScene != null) {
            completableFuture = this.lobbyScene.load(this);
        }

        return completableFuture.thenRunAsync(this.state::start, Task.main());
    }

    public void update() {
        this.state.update();
    }

    public boolean isConnectable() {
        return this.state.isConnectable();
    }

    @Override
    public void close() throws Exception {
        if (this.closed.compareAndSet(false, true)) {
            return;
        }
        this.compositeTerminable.close();
        this.metadataMap.clear();
        if (this.lobbyScene != null)
            this.lobbyScene.setRunningBy(null);
    }

    @Override
    public boolean isClosed() {
        return this.closed.get();
    }

    public List<Player> getPlayers() {
        return Players.transformUuids(this.players);
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    @Override
    public MetadataMap getMetadataMap() {
        return this.metadataMap;
    }

    @NotNull
    @Override
    public Iterator<Player> iterator() {
        return this.getPlayers().iterator();
    }

    @Override
    public final Game getGame() {
        return this;
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return Players.streamUuids(this.players)
                .map(FairyBukkitPlatform.AUDIENCES::player)
                .collect(Collectors.toList());
    }
}
