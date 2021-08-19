package org.imanity.brew.game.state;

import org.fairy.state.State;
import org.imanity.brew.game.Game;

public interface GameState extends State {

    Game getGame();

    boolean isConnectable();

}
