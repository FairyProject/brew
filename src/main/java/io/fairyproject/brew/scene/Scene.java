package io.fairyproject.brew.scene;

import io.fairyproject.brew.game.Game;
import io.fairyproject.metadata.MetadataMap;
import org.bukkit.entity.Player;
import io.fairyproject.util.terminable.Terminable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public interface Scene extends Terminable {

    String getName();

    void setName(@NotNull String name);

    boolean isUsed();

    @Nullable Game getCurrentGame();

    void setCurrentGame(@Nullable Game game);

    CompletableFuture<?> init(Game game);

    void teleport(Player player, Game game);

    MetadataMap getMetadataMap();

}
