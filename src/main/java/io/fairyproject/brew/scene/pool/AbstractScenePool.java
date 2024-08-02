package io.fairyproject.brew.scene.pool;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public abstract class AbstractScenePool implements ScenePool {

    protected final List<BiPredicate<Game, Scene>> filters = new ArrayList<>();

    @Override
    public ScenePool withFilter(BiPredicate<Game, Scene> gamePredicate) {
        this.filters.add(gamePredicate);
        return this;
    }

    public Stream<Scene> streamWithFilters(Game game) {
        return this.all()
                .stream()
                .filter(scene -> {
                    for (BiPredicate<Game, Scene> filter : this.filters) {
                        if (!filter.test(game, scene)) {
                            return false;
                        }
                    }
                    return true;
                });
    }
}
