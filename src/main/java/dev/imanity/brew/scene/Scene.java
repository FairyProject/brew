package dev.imanity.brew.scene;

import dev.imanity.brew.game.Game;
import io.fairyproject.metadata.MetadataMap;
import org.bukkit.entity.Player;
import io.fairyproject.util.terminable.Terminable;

import java.util.concurrent.CompletableFuture;

public interface Scene extends Terminable, MetadataMap {

    String name();

    Scene name(String name);

    boolean used();

    void currentGame(Game game);

    Game currentGame();

    CompletableFuture<?> init(Game game);

    /**
     * set up the Player in Lobby
     *
     * @param player the Player
     * @param game the Game
     */
    void teleport(Player player, Game game);

}
