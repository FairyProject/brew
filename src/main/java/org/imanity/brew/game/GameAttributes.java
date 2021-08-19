package org.imanity.brew.game;

import lombok.experimental.UtilityClass;
import org.fairy.metadata.MetadataKey;

@UtilityClass
public class GameAttributes {

    public final MetadataKey<Integer> MINIMUM_PLAYERS = MetadataKey.createIntegerKey("scene:minPlayers");
    public final MetadataKey<Integer> MAXIMUM_PLAYERS = MetadataKey.createIntegerKey("scene:maxPlayers");

}
