package org.imanity.brew.game.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.fairyproject.bukkit.listener.events.CallableEvent;
import org.imanity.brew.game.Game;

@RequiredArgsConstructor
@Getter
public class GameEvent extends CallableEvent {
    private final Game game;

}
