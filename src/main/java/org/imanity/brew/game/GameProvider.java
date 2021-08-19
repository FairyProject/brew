package org.imanity.brew.game;

import org.bukkit.entity.Player;

import java.util.List;

public interface GameProvider {

    void init();

    Game find(Player player);

    List<Game> getActiveScenes();

}
