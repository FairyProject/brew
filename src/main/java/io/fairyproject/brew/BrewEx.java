package io.fairyproject.brew;

import io.fairyproject.brew.game.Game;
import io.fairyproject.metadata.MetadataKey;
import lombok.experimental.UtilityClass;

@UtilityClass
@Deprecated
public class BrewEx {

    public final MetadataKey<Game> GAME = FairyBrew.GAME_KEY;

}
