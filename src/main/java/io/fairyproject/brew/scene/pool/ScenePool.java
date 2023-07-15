package io.fairyproject.brew.scene.pool;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.scene.Scene;
import io.fairyproject.util.terminable.Terminable;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface ScenePool extends Terminable {

    static ScenePool create(Scene... scenes) {
        return new ScenePoolImpl(scenes);
    }

    BiPredicate<Game, Scene> NOT_RUNNING = (game, scene) -> scene.isUsed();

    List<Scene> all();

    Scene find(Game game);

    List<Scene> findAll(Game game);

    @Contract("_ -> this")
    default ScenePool withFilter(Predicate<Scene> scenePredicate) {
        return withFilter((game, scene) -> scenePredicate.test(scene));
    }

    @Contract("_ -> this")
    ScenePool withFilter(BiPredicate<Game, Scene> gamePredicate);

}
