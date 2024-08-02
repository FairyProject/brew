package io.fairyproject.brew.game;

import lombok.experimental.UtilityClass;
import io.fairyproject.metadata.MetadataKey;

@UtilityClass
@Deprecated
public class GameEx {

    public final MetadataKey<Integer> MINIMUM_PLAYERS = MetadataKey.createIntegerKey("scene:minPlayers");
    public final MetadataKey<Integer> MAXIMUM_PLAYERS = MetadataKey.createIntegerKey("scene:maxPlayers");

}
