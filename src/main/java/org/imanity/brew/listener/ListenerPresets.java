package org.imanity.brew.listener;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.fairy.bukkit.events.player.PlayerDamageEvent;
import org.imanity.brew.game.state.GameStateBase;

import java.util.function.Predicate;

@UtilityClass
public class ListenerPresets {

    public void disallowBlockModification(GameStateBase gameState, Predicate<Player> predicate) {
        gameState.cancelPlayer(BlockBreakEvent.class, EventPriority.NORMAL, predicate);
        gameState.cancelPlayer(BlockPlaceEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowItems(GameStateBase gameState, Predicate<Player> predicate) {
        gameState.cancelPlayer(PlayerDropItemEvent.class, EventPriority.NORMAL, predicate);
        gameState.cancelPlayer(PlayerPickupItemEvent.class, EventPriority.NORMAL, predicate);
        gameState.cancelPlayer(PlayerItemDamageEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowInteract(GameStateBase gameState, Predicate<Player> predicate) {
        gameState.cancelPlayer(PlayerInteractEvent.class, EventPriority.NORMAL, predicate);
        gameState.cancelPlayer(PlayerInteractEntityEvent.class, EventPriority.NORMAL, predicate);
    }

    public void disallowDamage(GameStateBase gameState, Predicate<Player> predicate) {
        gameState.cancelPlayer(PlayerDamageEvent.class, EventPriority.NORMAL, predicate);
    }

}
