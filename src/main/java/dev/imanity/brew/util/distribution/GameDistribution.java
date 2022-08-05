package dev.imanity.brew.util.distribution;

import dev.imanity.brew.game.Game;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface GameDistribution {
    @Nullable
    Game find(@NotNull Collection<Game> games);

    GameDistribution EQUALLY = games -> {
        int bestCount = 0;
        Game bestGame = null;
        for (Game game : games) {
            if (game.isConnectable() && (bestGame == null || game.getPlayerCount() < bestCount)) {
                bestGame = game;
                bestCount = game.getPlayerCount();
            }
        }

        return bestGame;
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

        return bestGame;
    };
}