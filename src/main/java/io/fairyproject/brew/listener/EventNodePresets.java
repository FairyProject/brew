package io.fairyproject.brew.listener;

import io.fairyproject.bukkit.events.BukkitEventFilter;
import io.fairyproject.bukkit.events.player.PlayerDamageByPlayerEvent;
import io.fairyproject.bukkit.events.player.PlayerDamageEvent;
import io.fairyproject.event.EventNode;
import lombok.experimental.UtilityClass;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

@UtilityClass
public class EventNodePresets {

    public EventNode<BlockEvent> getCancelBlockModification() {
        EventNode<BlockEvent> node = EventNode.create("cancel_block_modification", BukkitEventFilter.BLOCK, null);

        node.addListener(BlockBreakEvent.class, event -> event.setCancelled(true));
        node.addListener(BlockPlaceEvent.class, event -> event.setCancelled(true));

        return node;
    }

    @SuppressWarnings("deprecation")
    public EventNode<Event> getCancelItemInteraction() {
        EventNode<Event> node = EventNode.create("cancel_item_interaction", BukkitEventFilter.ALL, null);

        node.addListener(PlayerDropItemEvent.class, event -> event.setCancelled(true));
        try {
            node.addListener(org.bukkit.event.entity.EntityPickupItemEvent.class, event -> event.setCancelled(true));
        } catch (NoClassDefFoundError ex) {
            node.addListener(PlayerPickupItemEvent.class, event -> event.setCancelled(true));
        }
        node.addListener(PlayerItemDamageEvent.class, event -> event.setCancelled(true));

        return node;
    }

    public EventNode<PlayerEvent> getCancelWorldInteraction() {
        EventNode<PlayerEvent> node = EventNode.create("cancel_world_interaction", BukkitEventFilter.PLAYER, null);

        node.addListener(PlayerInteractEvent.class, event -> event.setCancelled(true));
        node.addListener(PlayerInteractEntityEvent.class, event -> event.setCancelled(true));

        return node;
    }

    public EventNode<Event> getCancelHungerLevel() {
        EventNode<Event> node = EventNode.create("cancel_hunger_level", BukkitEventFilter.ALL, null);

        node.addListener(FoodLevelChangeEvent.class, event -> event.setCancelled(true));
        node.addListener(PlayerItemConsumeEvent.class, event -> event.setCancelled(true));

        return node;
    }

    public EventNode<Event> getCancelDamage() {
        EventNode<Event> node = EventNode.create("cancel_damage", BukkitEventFilter.ALL, null);

        node.addListener(EntityDamageEvent.class, event -> event.setCancelled(true));
        node.addListener(PlayerDamageEvent.class, event -> event.setCancelled(true));
        node.addListener(PlayerDamageByPlayerEvent.class, event -> event.setCancelled(true));

        return node;
    }

}
