package io.fairyproject.brew.game.pool;

import io.fairyproject.brew.game.Game;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamePoolImpl implements GamePool {

    private final Set<BiPredicate<Player, Game>> filters;
    private final Set<Game> games;

    public GamePoolImpl() {
        this.games = ConcurrentHashMap.newKeySet();
        this.filters = ConcurrentHashMap.newKeySet();
    }

    @Override
    public Set<Game> all() {
        return Collections.unmodifiableSet(this.games);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return this.games;
    }

    @NotNull
    @Override
    public Iterator<Game> iterator() {
        return this.games.iterator();
    }

    @Override
    public Stream<Game> stream() {
        return this.games.stream();
    }

    @Override
    public GamePool withFilter(@NotNull BiPredicate<Player, Game> filter) {
        this.filters.add(filter);
        return this;
    }

    @Override
    public GamePool withFilter(@NotNull Predicate<Game> filter) {
        return this.withFilter((player, game) -> filter.test(game));
    }

    @Override
    public void add(@NotNull Game game) {
        this.games.add(game);
    }

    @Override
    public void remove(@NotNull Game game) {
        this.games.remove(game);
    }

    @Override
    public @NotNull Collection<Game> findAll(@NotNull Player player) {
        return this.games.stream()
                .filter(game -> {
                    for (BiPredicate<Player, Game> filter : this.filters) {
                        if (!filter.test(player, game))
                            return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
