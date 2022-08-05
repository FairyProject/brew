package dev.imanity.brew.game.event;

import dev.imanity.brew.game.Game;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.fairyproject.bukkit.listener.events.CallableEvent;

@RequiredArgsConstructor
@Getter
public class GameEvent extends CallableEvent {
    private final Game game;

}
