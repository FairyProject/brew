package org.imanity.brew.constant;

import lombok.experimental.UtilityClass;
import io.fairyproject.metadata.MetadataKey;
import org.imanity.brew.game.Game;

@UtilityClass
public class PlayerConstants {

    public final MetadataKey<Game> GAME = MetadataKey.create("brew:scene", Game.class);

    static {
        GAME.setRemoveOnNonExists(true);
    }

}
