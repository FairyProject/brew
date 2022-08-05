package dev.imanity.brew.scene.pool;

import dev.imanity.brew.game.Game;
import dev.imanity.brew.scene.Scene;
import io.fairyproject.util.terminable.Terminable;
import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public interface ScenePool extends Terminable {

    static ScenePool create(Scene... scenes) {
        return new ScenePoolImpl(scenes);
    }

    BiPredicate<Game, Scene> NOT_RUNNING = (game, scene) -> scene.used();

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
