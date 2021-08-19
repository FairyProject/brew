package org.imanity.brew.team;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.fairy.bukkit.util.Players;
import org.fairy.metadata.MetadataKey;
import org.fairy.metadata.MetadataMap;
import org.imanity.brew.game.Game;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
public class Team implements Iterable<Player> {

    private final Game game;
    private final int id;
    private final Set<UUID> playerUuids;
    private final MetadataMap metadataMap;

    public Team(Game game, int id) {
        this.game = game;
        this.id = id;
        this.playerUuids = Sets.newConcurrentHashSet();
        this.metadataMap = MetadataMap.create();
    }

    public List<Player> getPlayers() {
        return Players.transformUuids(this.playerUuids);
    }

    public <T> boolean has(MetadataKey<T> metadataKey) {
        return this.metadataMap.has(metadataKey);
    }

    public <T> T get(MetadataKey<T> metadataKey) {
        return this.metadataMap.getOrNull(metadataKey);
    }

    public <T> T getOrPut(MetadataKey<T> metadataKey, Supplier<T> supplier) {
        return this.metadataMap.getOrPut(metadataKey, supplier);
    }

    public <T> void put(MetadataKey<T> metadataKey, T t) {
        this.metadataMap.put(metadataKey, t);
    }

    public <T> boolean remove(MetadataKey<T> metadataKey) {
        return this.metadataMap.remove(metadataKey);
    }

    @NotNull
    @Override
    public Iterator<Player> iterator() {
        return this.getPlayers().iterator();
    }
}
