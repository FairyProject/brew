package io.fairyproject.brew.team.event;

import io.fairyproject.brew.game.Game;
import io.fairyproject.brew.game.event.GameEvent;
import io.fairyproject.brew.team.Team;

public interface TeamEvent extends GameEvent {

    Team getTeam();

    @Override
    default Game getGame() {
        return this.getTeam().getGame();
    }

}
