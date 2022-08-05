package dev.imanity.brew;

import dev.imanity.brew.game.Game;
import io.fairyproject.metadata.MetadataKey;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BrewEx {

    public final MetadataKey<Game> GAME = MetadataKey.create("brew:scene", Game.class);

}
