package dev.imanity.brew;

import io.fairyproject.container.Autowired;
import io.fairyproject.container.PreInitialize;
import io.fairyproject.container.Service;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Service
@Getter
public class Brew {

    @Autowired
    private static Brew INSTANCE;

    public static Brew get() {
        return INSTANCE;
    }

    protected static boolean UNIT_TEST = false;
    protected static Plugin UNIT_TEST_PLUGIN;
    private Plugin plugin;

    @PreInitialize
    public void onPreInitialize() {
        this.plugin = UNIT_TEST ? UNIT_TEST_PLUGIN : JavaPlugin.getProvidingPlugin(this.getClass());
    }

}