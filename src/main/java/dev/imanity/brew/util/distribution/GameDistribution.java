package dev.imanity.brew.util.distribution;

import dev.imanity.brew.game.Game;
import io.fairyproject.util.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public interface GameDistribution {
    Either<Game, ?> find(@NotNull Collection<Game> games, Predicate<Game> gamePredicate);

    GameDistribution EQUALLY = (games, gamePredicate) -> {
        int bestCount = 0;
        Game bestGame = null;
        for (Game game : games) {
            if (gamePredicate.test(game) && (bestGame == null || game.getPlayerCount() < bestCount)) {
                bestGame = game;
                bestCount = game.getPlayerCount();
            }
        }

        if (bestGame == null)
            return Either.right(null);
        return Either.left(bestGame);
    };

    GameDistribution FILLING = (games, gamePredicate) -> {
        int bestCount = 0;
        Game bestGame = null;
        for (Game game : games) {
            if (gamePredicate.test(game) && (bestGame == null || game.getPlayerCount() > bestCount)) {
                bestGame = game;
                bestCount = game.getPlayerCount();
            }
        }

        if (bestGame == null)
            return Either.right(null);
        return Either.left(bestGame);
    };
}