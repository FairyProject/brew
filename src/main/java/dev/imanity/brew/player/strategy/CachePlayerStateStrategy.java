package dev.imanity.brew.player.strategy;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

@Accessors(fluent = true)
@Getter
@Setter
public class CachePlayerStateStrategy extends PlayerStateStrategy {

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
    private final EnumSet<Caching> cachings;
    private final boolean resetOnStart;
    private final boolean restoreOnEnd;

    public CachePlayerStateStrategy(int priority, boolean resetOnStart, boolean restoreOnEnd, Caching... cachings) {
        this(priority, resetOnStart, restoreOnEnd, Arrays.asList(cachings));
    }

    public CachePlayerStateStrategy(int priority, boolean resetOnStart, boolean restoreOnEnd, Collection<Caching> cachings) {
        super(priority);
        this.cachings = EnumSet.copyOf(cachings);
        this.resetOnStart = resetOnStart;
        this.restoreOnEnd = restoreOnEnd;
    }

    @Override
    public void onStart() {
        PlayerInventory inventory = this.player().getInventory();

        for (Caching caching : this.cachings) {
            switch (caching) {
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
    public void onUpdate() {

    }

    @Override
    public void onSuspend() {
        this.restore();
    }

    @Override
    public void onEnded() {
        this.restore();
    }

    private void restore() {
        if (!this.restoreOnEnd)
            return;

        PlayerInventory inventory = this.player().getInventory();

        for (Caching caching : this.cachings) {
            switch (caching) {
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

    @Override
    public void onPause() {

    }

    @Override
    public void onUnpause() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public enum Caching {
        HEALTH, FOOD_LEVEL, INVENTORY, POTION_EFFECTS, EXP, FIRE_TICKS, GAME_MODE, LOCATION
    }

    public static class Builder {

        private List<Caching> cachings;
        private int priority;
        private boolean resetOnStart;
        private boolean restoreOnEnd;

        public Builder enableCaching(Caching... cachings) {
            return this.enableCaching(Arrays.asList(cachings));
        }

        public Builder enableCaching(Collection<Caching> cachings) {
            this.cachings.addAll(cachings);
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder resetOnStart(boolean resetOnStart) {
            this.resetOnStart = this.resetOnStart;
            return this;
        }

        public Builder restoreOnEnd(boolean restoreOnEnd) {
            this.restoreOnEnd = restoreOnEnd;
            return this;
        }

        public CachePlayerStateStrategy build() {
            return new CachePlayerStateStrategy(priority, resetOnStart, restoreOnEnd, cachings);
        }

    }
}
