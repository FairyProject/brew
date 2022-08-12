package dev.imanity.brew.game;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import io.fairyproject.bukkit.FairyBukkitPlatform;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.bukkit.util.Players;
import io.fairyproject.libs.kyori.adventure.audience.Audience;
import io.fairyproject.libs.kyori.adventure.audience.ForwardingAudience;
import io.fairyproject.mc.metadata.PlayerOnlineValue;
import io.fairyproject.metadata.MetadataMap;
import io.fairyproject.metadata.MetadataMapProxy;
import io.fairyproject.util.terminable.Terminable;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import dev.imanity.brew.BrewEx;
import dev.imanity.brew.game.event.GameJoinEvent;
import dev.imanity.brew.game.event.GameQuitEvent;
import dev.imanity.brew.game.state.GameState;
import dev.imanity.brew.team.Team;
import dev.imanity.brew.team.TeamEx;
import dev.imanity.brew.team.event.TeamQuitEvent;
import dev.imanity.brew.util.task.IntervalTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Game implements Terminable, TerminableConsumer, ForwardingAudience, Iterable<Player>, MetadataMapProxy, GameListener {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger();

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
    private final AtomicBoolean closed = new AtomicBoolean();
    private final MetadataMap metadataMap = MetadataMap.create();
    private final IntervalTask cleanup = IntervalTask.create(this.metadataMap::cleanup, 20 * 60);

    public Game() {
        this.id = ID_COUNTER.getAndIncrement();
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
        Metadata.provideForPlayer(player).put(BrewEx.GAME, PlayerOnlineValue.create(this, player.getUniqueId()));

        new GameJoinEvent(player, this).call();
    }

    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
        Metadata.provideForPlayer(player).remove(BrewEx.GAME);

        // remove player from their team
        Team team = TeamEx.getTeamByPlayer(player);
        if (team != null)
            team.removePlayer(player, TeamQuitEvent.Reason.DISCONNECTED);

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

    public void removeTeam(Team team) {
        this.removeTeam(team.getId());
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

    @Override
    public boolean isPlayer(Player player) {
        return this.players.contains(player.getUniqueId());
    }

    public void start() {
        this.state.start();
    }

    public void update() {
        this.cleanup.update();

        for (Team team : this.teams.values())
            team.update();
    }

    public boolean isConnectable() {
        return this.state.isConnectable();
    }

    @Override
    public void close() throws Exception {
        if (!this.closed.compareAndSet(false, true))
            return;

        this.compositeTerminable.close();
        this.metadataMap.clear();

        for (Team team : this.teams.values())
            team.close();
    }

    @Override
    public boolean isClosed() {
        return this.closed.get();
    }

    public List<Player> getPlayers() {
        return Players.transformUuids(this.players);
    }

    public Set<UUID> getPlayerUuids() {
        return this.players;
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
