package dev.imanity.brew.game.pool;

import dev.imanity.brew.game.Game;
import io.fairyproject.libs.kyori.adventure.audience.Audience;
import io.fairyproject.util.ConditionUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GamePoolImpl implements GamePool {

    private final Set<BiPredicate<Player, Game>> filters;
    private final Set<Game> games;
    private final int size;
    private final Function<@Nullable Player, Game> gameBuilder;

    public GamePoolImpl(int size, Function<@Nullable Player, Game> gameBuilder) {
        this.games = ConcurrentHashMap.newKeySet();
        this.filters = ConcurrentHashMap.newKeySet();
        this.gameBuilder = gameBuilder;
        this.size = size;

        for (int i = 0; i < this.size; i++) {
            final Game game = gameBuilder.apply(null);
            game.start();

            this.games.add(game);
        }
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
    public @NotNull Collection<Game> findAll(@NotNull Player player, boolean shouldCreate) {
        final List<Game> retVal = this.games.stream()
                .filter(game -> {
                    for (BiPredicate<Player, Game> filter : this.filters) {
                        if (!filter.test(player, game))
                            return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());
        if (retVal.isEmpty() && shouldCreate) {
            ConditionUtils.notNull(this.gameBuilder, "Attempting to build a new game but gameBuilder wasn't being set!");
            ConditionUtils.is(this.games.size() < this.size, "Exceed the maximum size (%s) in current game pool", this.size);

            final Game game = this.gameBuilder.apply(player);
            game.start();
            this.games.add(game);

            return Collections.singleton(game);
        }
        return retVal;
    }
}
