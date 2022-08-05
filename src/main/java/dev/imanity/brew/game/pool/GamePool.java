package dev.imanity.brew.game.pool;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.util.distribution.GameDistribution;
import io.fairyproject.libs.kyori.adventure.audience.ForwardingAudience;
import io.fairyproject.util.ConditionUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface GamePool extends Iterable<Game>, ForwardingAudience {

    static Builder builder() {
        return new Builder();
    }

    static GamePool create(int size, Function<@Nullable Player, Game> gameBuilder) {
        return new GamePoolImpl(size, gameBuilder);
    }

    Set<Game> all();

    Stream<Game> stream();

    @Contract("_ -> this")
    GamePool withFilter(@NotNull BiPredicate<Player, Game> filter);

    @Contract("_ -> this")
    GamePool withFilter(@NotNull Predicate<Game> filter);

    @NotNull
    default Optional<Game> find(@NotNull Player player, GameDistribution distribution) {
        return this.find(player, distribution, true);
    }

    @NotNull
    default Optional<Game> find(@NotNull Player player, GameDistribution distribution, boolean shouldCreate) {
        return Optional.ofNullable(distribution.find(this.findAll(player, shouldCreate)));
    }

    @NotNull
    default Collection<Game> findAll(@NotNull Player player) {
        return this.findAll(player, true);
    }

    /**
     * find all games that match filters.
     *
     * @param player the player.
     * @param shouldCreate should create new game if no games were found, it requires gameBuilder to be set.
     * @return games
     */
    @NotNull
    Collection<Game> findAll(@NotNull Player player, boolean shouldCreate);

    class Builder {

        private int size = Integer.MAX_VALUE;
        private Function<@Nullable Player, Game> gameBuilder;

        @Contract("_ -> this")
        public Builder size(int size) {
            this.size = size;
            return this;
        }

        @Contract("_ -> this")
        public Builder gameBuilder(@NotNull Function<@Nullable Player, Game> gameBuilder) {
            this.gameBuilder = gameBuilder;
            return this;
        }

        public GamePool build() {
            ConditionUtils.notNull(this.gameBuilder, "gameSupplier is not suppose to be null!");
            return new GamePoolImpl(this.size, this.gameBuilder);
        }

    }

}
