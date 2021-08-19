package org.imanity.brew.scene;

import org.fairy.util.terminable.Terminable;
import org.imanity.brew.game.Game;

import java.util.List;

public interface SceneProvider extends Terminable {

    void load();

    Scene find(Game game);

    List<Scene> getActiveArenas();

    List<Scene> getAvailableArenas(Game game);

}
