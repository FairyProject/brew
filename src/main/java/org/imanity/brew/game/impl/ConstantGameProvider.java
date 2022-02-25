package org.imanity.brew.game.impl;

import io.fairyproject.container.Autowired;
import io.fairyproject.task.Task;
import org.bukkit.entity.Player;
import org.imanity.brew.Brew;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.GameProvider;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstantGameProvider implements GameProvider {

    private final Distribution distribution;
    private final List<Game> games;
    private final int size;

    @Autowired
    private Brew brew;

    public ConstantGameProvider(Distribution distribution, int size) {
        this.distribution = distribution;
        this.games = new ArrayList<>(size);
        this.size = size;
    }

    @Override
    public void init() {
        for (int i = 0; i < this.size; i++) {
            final Game game = this.brew.getGameConfigurer().buildGame();
            game.start().thenRunAsync(() -> this.games.add(game), Task.main());
        }
    }

    @Override
    public Game find(Player player) {
        return this.distribution.findGame(this.games);
    }

    @Override
    public List<Game> getActiveScenes() {
        return Collections.unmodifiableList(this.games);
    }

    public interface Distribution {
        @Nullable
        Game findGame(List<Game> games);

        Distribution EQUALLY = new Distribution() {
            @Override
            public @Nullable Game findGame(List<Game> games) {
                int bestCount = 0;
                Game bestGame = null;
                for (Game game : games) {
                    if (game.isConnectable() && (bestGame == null || game.getPlayerCount() < bestCount)) {
                        bestGame = game;
                        bestCount = game.getPlayerCount();
                    }
                }

                return bestGame;
            }
        };

        Distribution FILLING  = new Distribution() {
            @Override
            public @Nullable Game findGame(List<Game> games) {
                int bestCount = 0;
                Game bestGame = null;
                for (Game game : games) {
                    if (game.isConnectable() && (bestGame == null || game.getPlayerCount() > bestCount)) {
                        bestGame = game;
                        bestCount = game.getPlayerCount();
                    }
                }

                return bestGame;
            }
        };
    }


}
