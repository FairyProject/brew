package org.imanity.brew.game;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.fairy.bukkit.listener.events.EventSubscribeBuilder;
import org.fairy.bukkit.listener.events.Events;
import org.fairy.bukkit.metadata.Metadata;
import org.fairy.bukkit.util.Players;
import org.fairy.metadata.MetadataKey;
import org.fairy.metadata.MetadataMap;
import org.fairy.util.terminable.Terminable;
import org.fairy.util.terminable.TerminableConsumer;
import org.fairy.util.terminable.composite.CompositeTerminable;
import org.imanity.brew.game.event.GameJoinEvent;
import org.imanity.brew.game.event.GameQuitEvent;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.constant.PlayerConstants;
import org.imanity.brew.game.state.GameState;
import org.imanity.brew.team.Team;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Game implements Terminable, TerminableConsumer, Iterable<Player> {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger();

    @Getter
    private Scene lobbyScene;
    @Getter
    private GameState state;
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
        Metadata.provideForPlayer(player).put(PlayerConstants.GAME, this);
        new GameJoinEvent(player).call();
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
        Metadata.provideForPlayer(player).remove(PlayerConstants.GAME);
        new GameQuitEvent(player).call();
    }

    public Team createTeam() {
        return this.addTeam(new Team(this, this.teamCounter.getAndIncrement()));
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

    protected final <T extends PlayerEvent> EventSubscribeBuilder<T> listen(Class<T> type) {
        return Events.subscribe(type)
                .filter(event -> this.isPlayer(event.getPlayer()))
                .bindWith(this);
    }

    protected Predicate<Player> filterPlayer() {
        return this::isPlayer;
    }

    public boolean isPlayer(Player player) {
        return this.players.contains(player.getUniqueId());
    }

    public void start() {
        this.state.start();
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

    public <T> boolean has(MetadataKey<T> metadataKey) {
        return metadataMap.has(metadataKey);
    }

    public <T> T get(MetadataKey<T> metadataKey) {
        return metadataMap.getOrNull(metadataKey);
    }

    public <T> T getOrPut(MetadataKey<T> metadataKey, Supplier<T> supplier) {
        return metadataMap.getOrPut(metadataKey, supplier);
    }

    public <T> void put(MetadataKey<T> metadataKey, T t) {
        metadataMap.put(metadataKey, t);
    }

    public <T> boolean remove(MetadataKey<T> metadataKey) {
        return metadataMap.remove(metadataKey);
    }

    @NotNull
    @Override
    public Iterator<Player> iterator() {
        return this.getPlayers().iterator();
    }
}
