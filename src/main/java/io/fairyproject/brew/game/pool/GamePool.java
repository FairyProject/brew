package io.fairyproject.brew.game.pool;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.util.distribution.GameDistribution;
import net.kyori.adventure.audience.ForwardingAudience;
import io.fairyproject.util.Either;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public interface GamePool extends Iterable<Game>, ForwardingAudience {

    static GamePool create() {
        return new GamePoolImpl();
    }

    Set<Game> all();

    Stream<Game> stream();

    @Contract("_ -> this")
    GamePool withFilter(@NotNull BiPredicate<Player, Game> filter);

    @Contract("_ -> this")
    GamePool withFilter(@NotNull Predicate<Game> filter);

    void add(@NotNull Game game);

    void remove(@NotNull Game game);

    @NotNull
    default Either<Game, ?> find(@NotNull Player player, @NotNull GameDistribution distribution) {
        return distribution.find(this.findAll(player), game -> true);
    }

    @NotNull
    default Either<Game, ?> find(@NotNull Player player, @NotNull GameDistribution distribution, Predicate<Game> gamePredicate) {
        return distribution.find(this.findAll(player), gamePredicate);
    }

    @NotNull
    Collection<Game> findAll(@NotNull Player player);

}
