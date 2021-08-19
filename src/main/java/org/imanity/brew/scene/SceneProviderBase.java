package org.imanity.brew.scene;

import java.util.Collection;
import java.util.stream.Stream;

public abstract class SceneProviderBase implements SceneProvider {

    protected final Stream<Scene> searchAvailable(Collection<Scene> scenes) {
        return scenes.stream().filter(scene -> !scene.isRunning());
    }

}
