package io.fairyproject.brew.game;

import com.google.common.base.Preconditions;
import io.fairyproject.brew.BrewEx;
import io.fairyproject.brew.game.event.GameEvent;
import io.fairyproject.brew.game.event.GameJoinEvent;
import io.fairyproject.brew.game.event.GameQuitEvent;
import io.fairyproject.brew.team.Team;
import io.fairyproject.brew.team.TeamEx;
import io.fairyproject.brew.team.event.TeamQuitEvent;
import io.fairyproject.brew.util.task.IntervalTask;
import io.fairyproject.bukkit.FairyBukkitPlatform;
import io.fairyproject.bukkit.events.BukkitEventFilter;
import io.fairyproject.bukkit.metadata.Metadata;
import io.fairyproject.bukkit.player.PlayerEventRecognizer;
import io.fairyproject.bukkit.util.Players;
import io.fairyproject.event.EventNode;
import io.fairyproject.mc.metadata.PlayerOnlineValue;
import io.fairyproject.metadata.MetadataMap;
import io.fairyproject.metadata.MetadataMapProxy;
import io.fairyproject.util.terminable.Terminable;
import io.fairyproject.util.terminable.TerminableConsumer;
import io.fairyproject.util.terminable.composite.CompositeTerminable;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Game implements Terminable, TerminableConsumer, ForwardingAudience, Iterable<Player>, MetadataMapProxy, GameListener {

    private static final AtomicInteger ID_COUNTER = new AtomicInteger();

    @Getter
    @Setter
    private int maxPlayerPerTeam;
    @Getter
    private final int id;
    @Getter
    private final EventNode<Event> eventNode;
    private final Set<UUID> players;
    private final Map<Integer, Team> teams;
    private final AtomicInteger teamCounter;
    private final CompositeTerminable compositeTerminable;
    private final AtomicBoolean closed;
    private final MetadataMap metadataMap;
    private final IntervalTask cleanup;

    public Game(EventNode<Event> parentEventNode) {
        this.id = ID_COUNTER.getAndIncrement();
        this.eventNode = EventNode.event("brew:game:" + id, BukkitEventFilter.ALL, this::eventFilter);
        this.maxPlayerPerTeam = 1;
        this.players = new HashSet<>();
        this.teams = new HashMap<>();
        this.teamCounter = new AtomicInteger();
        this.compositeTerminable = CompositeTerminable.create();
        this.closed = new AtomicBoolean();
        this.metadataMap = MetadataMap.create();
        this.cleanup = IntervalTask.create(this.metadataMap::cleanup, 20 * 60);

        parentEventNode.addChild(this.eventNode);
        this.eventNode.bindWith(this);
    }

    private boolean eventFilter(Event event) {
        if (event instanceof GameEvent) {
            GameEvent gameEvent = (GameEvent) event;
            return gameEvent.getGame() == this;
        }

        Player player = PlayerEventRecognizer.tryRecognize(event);
        if (player == null)
            return true;

        return this.isPlayer(player);
    }

    @NotNull
    @Override
    public <T extends Terminable> T bind(@NotNull T t) {
        return this.compositeTerminable.bind(t);
    }

    public void addPlayer(Player player) {
        this.players.add(player.getUniqueId());
        Metadata.provideForPlayer(player).put(BrewEx.GAME, PlayerOnlineValue.create(this, player.getUniqueId()));

        new GameJoinEvent(player, this).call();
    }

    public void removePlayer(Player player) {
        new GameQuitEvent(player, this).call();

        this.players.remove(player.getUniqueId());
        Metadata.provideForPlayer(player).remove(BrewEx.GAME);

        // remove player from their team
        Team team = TeamEx.getTeamByPlayer(player);
        if (team != null)
            team.removePlayer(player, TeamQuitEvent.Reason.DISCONNECTED);
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
    public boolean isPlayer(@NotNull Player player) {
        return this.players.contains(player.getUniqueId());
    }

    public void update() {
        this.cleanup.update();

        for (Team team : this.teams.values())
            team.update();
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
                .map(BukkitAudiences.create(FairyBukkitPlatform.PLUGIN)::player)
                .collect(Collectors.toList());
    }
}
