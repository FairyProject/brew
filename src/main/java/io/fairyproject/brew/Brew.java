package io.fairyproject.brew;

import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.PreInitialize;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@InjectableComponent
@Getter
@Deprecated
public class Brew {

    protected static boolean UNIT_TEST = false;
    protected static Plugin UNIT_TEST_PLUGIN;
    private Plugin plugin;

    @PreInitialize
    public void onPreInitialize() {
        this.plugin = UNIT_TEST ? UNIT_TEST_PLUGIN : JavaPlugin.getProvidingPlugin(this.getClass());
    }

}