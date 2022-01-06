package org.imanity.brew;

import io.fairyproject.container.Component;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.GameConfigurer;
import org.imanity.brew.game.GameProvider;
import org.imanity.brew.game.impl.ConstantGameProvider;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.scene.SceneProvider;
import org.imanity.brew.scene.impl.ConstantSceneProvider;

@Component
public class BrewTestAdapter implements BrewAdapter {

    @Override
    public SceneProvider createLobbySceneProvider() {
        return new ConstantSceneProvider();
    }

    @Override
    public SceneProvider createGameSceneProvider() {
        return new ConstantSceneProvider();
    }

    @Override
    public GameProvider createGameProvider() {
        return new ConstantGameProvider(ConstantGameProvider.Distribution.EQUALLY, 0);
    }

    @Override
    public GameConfigurer createGameConfigurer() {
        return new GameConfigurer() {
            @Override
            public void init() {

            }

            @Override
            public Game buildGame() {
                return null;
            }
        };
    }

}
