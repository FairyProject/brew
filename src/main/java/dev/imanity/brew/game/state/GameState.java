package dev.imanity.brew.game.state;

import dev.imanity.brew.game.Game;
import io.fairyproject.state.State;

public interface GameState extends State {

    Game getGame();

    boolean isConnectable();

}
