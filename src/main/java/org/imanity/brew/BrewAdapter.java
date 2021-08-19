package org.imanity.brew;

import org.imanity.brew.scene.SceneProvider;
import org.imanity.brew.game.GameConfigurer;
import org.imanity.brew.game.GameProvider;

public interface BrewAdapter {

    SceneProvider createLobbySceneProvider();

    SceneProvider createGameSceneProvider();

    GameProvider createGameProvider();

    GameConfigurer createGameConfigurer();

}
