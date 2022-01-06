package org.imanity.brew.game.state;

import io.fairyproject.state.State;
import org.imanity.brew.game.Game;

public interface GameState extends State {

    Game getGame();

    boolean isConnectable();

}
