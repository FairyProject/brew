package org.imanity.brew.scene;

import org.bukkit.entity.Player;
import io.fairyproject.util.terminable.Terminable;
import org.imanity.brew.game.Game;

public interface Scene extends Terminable {

    String getName();

    void setName(String name);

    boolean isRunning();

    void setRunningBy(Game game);

    Game getRunningGame();

    void init(Game game);

    /**
     * set up the Player in Lobby
     *
     * @param player the Player
     * @param game the Game
     */
    void teleport(Player player, Game game);

}
