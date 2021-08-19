package org.imanity.brew.game.impl;

import org.bukkit.entity.Player;
import org.fairy.bean.Autowired;
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
            this.games.add(game);
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

    public enum Distribution {

        EQUALLY {
            @Override
            @Nullable
            protected Game findGame(List<Game> games) {
                int bestCount = 0;
                Game bestGame = null;
                for (Game game : games) {
                    if (game.isConnectable() && game.getPlayerCount() < bestCount) {
                        bestGame = game;
                        bestCount = game.getPlayerCount();
                    }
                }

                return bestGame;
            }
        },
        FILLING {
            @Override
            protected @Nullable Game findGame(List<Game> games) {
                int bestCount = 0;
                Game bestGame = null;
                for (Game game : games) {
                    if (game.isConnectable() && game.getPlayerCount() > bestCount) {
                        bestGame = game;
                        bestCount = game.getPlayerCount();
                    }
                }

                return bestGame;
            }
        };

        @Nullable
        protected Game findGame(List<Game> games) {
            throw new UnsupportedOperationException();
        }

    }

}
