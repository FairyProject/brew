package dev.imanity.brew.player.strategy;

import com.google.common.collect.ImmutableList;
import io.fairyproject.state.StateMachine;
import io.fairyproject.state.trigger.Trigger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

@Accessors(fluent = true)
@Getter
@Setter
public class CachePlayerStateHandler<S, T> implements PlayerStateHandler<S, T> {

    private double health;
    private int foodLevel;
    private ItemStack[] contents;
    private ItemStack[] armorContents;
    private List<PotionEffect> potionEffects;
    private float exp;
    private int totalExperience;
    private int fireTicks;
    private GameMode gameMode;
    private Location location;
    private final Player player;
    private final EnumSet<CacheFlag> cacheFlags;
    private final boolean resetOnStart;
    private final boolean restoreOnEnd;

    public CachePlayerStateHandler(Player player, boolean resetOnStart, boolean restoreOnEnd, CacheFlag... cacheFlags) {
        this(player, resetOnStart, restoreOnEnd, Arrays.asList(cacheFlags));
    }

    public CachePlayerStateHandler(Player player, boolean resetOnStart, boolean restoreOnEnd, Collection<CacheFlag> cacheFlags) {
        this.player = player;
        this.cacheFlags = EnumSet.copyOf(cacheFlags);
        this.resetOnStart = resetOnStart;
        this.restoreOnEnd = restoreOnEnd;
    }

    private void restore() {
        if (!this.restoreOnEnd)
            return;

        PlayerInventory inventory = this.player().getInventory();

        for (CacheFlag cacheFlag : this.cacheFlags) {
            switch (cacheFlag) {
                case HEALTH:
                    this.player().setHealth(this.health);
                    break;
                case FOOD_LEVEL:
                    this.player().setFoodLevel(this.foodLevel);
                    break;
                case INVENTORY:
                    inventory.setContents(this.contents);
                    inventory.setArmorContents(this.armorContents);
                    break;
                case POTION_EFFECTS:
                    this.player().getActivePotionEffects().stream().map(PotionEffect::getType).forEach(this.player()::removePotionEffect);
                    this.player().addPotionEffects(this.potionEffects);
                    break;
                case EXP:
                    this.player().setExp(this.exp);
                    this.player().setTotalExperience(this.totalExperience);
                    break;
                case FIRE_TICKS:
                    this.player().setFireTicks(this.fireTicks);
                    break;
                case GAME_MODE:
                    this.player().setGameMode(this.gameMode);
                    break;
                case LOCATION:
                    this.player().teleport(this.location);
                    break;
            }
        }
    }

    public static <S, T> Builder<S, T> builder() {
        return new Builder<>();
    }

    @Override
    public @NotNull Player player() {
        return this.player;
    }

    @Override
    public void onStart(@NotNull StateMachine stateMachine, @NotNull Object o, @Nullable Trigger trigger) {
        PlayerInventory inventory = this.player().getInventory();

        for (CacheFlag cacheFlag : this.cacheFlags) {
            switch (cacheFlag) {
                case HEALTH:
                    this.health = this.player().getHealth();
                    if (this.resetOnStart)
                        this.player().setHealth(this.player().getMaxHealth());
                    break;
                case FOOD_LEVEL:
                    this.foodLevel = this.player().getFoodLevel();
                    if (this.resetOnStart)
                        this.player().setFoodLevel(20);
                    break;
                case INVENTORY:
                    this.contents = inventory.getContents().clone();
                    this.armorContents = inventory.getContents().clone();
                    if (this.resetOnStart) {
                        inventory.clear();
                        inventory.setArmorContents(null);
                    }
                    break;
                case POTION_EFFECTS:
                    this.potionEffects = ImmutableList.copyOf(this.player().getActivePotionEffects());
                    if (resetOnStart)
                        this.player().getActivePotionEffects().stream().map(PotionEffect::getType).forEach(this.player()::removePotionEffect);
                    break;
                case EXP:
                    this.exp = player().getExp();
                    this.totalExperience = player().getTotalExperience();
                    if (resetOnStart) {
                        this.player().setExp(0.0F);
                        this.player().setTotalExperience(0);
                    }
                    break;
                case FIRE_TICKS:
                    this.fireTicks = player().getFireTicks();
                    if (resetOnStart)
                        this.player().setFireTicks(0);
                    break;
                case GAME_MODE:
                    this.gameMode = player().getGameMode();
                    if (resetOnStart)
                        this.player().setGameMode(GameMode.SURVIVAL);
                    break;
                case LOCATION:
                    this.location = player().getLocation();
                    break;
            }
        }
    }

    @Override
    public void onTick(@NotNull StateMachine stateMachine, @NotNull Object o) {

    }

    @Override
    public void onStop(@NotNull StateMachine stateMachine, @NotNull Object o, @Nullable Trigger trigger) {
        this.restore();
    }

    public enum CacheFlag {
        HEALTH, FOOD_LEVEL, INVENTORY, POTION_EFFECTS, EXP, FIRE_TICKS, GAME_MODE, LOCATION
    }

    public static class Builder<S, T> {

        private List<CacheFlag> cacheFlags;
        private boolean resetOnStart;
        private boolean restoreOnEnd;
        private Player player;

        public Builder<S, T> player(Player player) {
            this.player = player;
            return this;
        }

        public Builder<S, T> enableCaching(CacheFlag... cacheFlags) {
            return this.enableCaching(Arrays.asList(cacheFlags));
        }

        public Builder<S, T> enableCaching(Collection<CacheFlag> cacheFlags) {
            this.cacheFlags.addAll(cacheFlags);
            return this;
        }

        public Builder<S, T> resetOnStart(boolean resetOnStart) {
            this.resetOnStart = resetOnStart;
            return this;
        }

        public Builder<S, T> restoreOnEnd(boolean restoreOnEnd) {
            this.restoreOnEnd = restoreOnEnd;
            return this;
        }

        public CachePlayerStateHandler<S, T> build() {
            return new CachePlayerStateHandler<>(player, resetOnStart, restoreOnEnd, cacheFlags);
        }

    }
}
