package dev.imanity.brew.util.distribution;

import dev.imanity.brew.game.Game;
import io.fairyproject.util.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface GameDistribution {
    Either<Game, ?> find(@NotNull Collection<Game> games);

    GameDistribution EQUALLY = games -> {
        int bestCount = 0;
        Game bestGame = null;
        for (Game game : games) {
            if (game.isConnectable() && (bestGame == null || game.getPlayerCount() < bestCount)) {
                bestGame = game;
                bestCount = game.getPlayerCount();
            }
        }

        if (bestGame == null)
            return Either.right(null);
        return Either.left(bestGame);
    };

    GameDistribution FILLING = games -> {
        int bestCount = 0;
        Game bestGame = null;
        for (Game game : games) {
            if (game.isConnectable() && (bestGame == null || game.getPlayerCount() > bestCount)) {
                bestGame = game;
                bestCount = game.getPlayerCount();
            }
        }

        if (bestGame == null)
            return Either.right(null);
        return Either.left(bestGame);
    };
}