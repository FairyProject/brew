package dev.imanity.brew.scene.pool;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public abstract class ScenePoolBase implements ScenePool {

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
