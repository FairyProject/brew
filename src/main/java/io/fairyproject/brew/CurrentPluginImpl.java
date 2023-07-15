package io.fairyproject.brew;

import io.fairyproject.bukkit.util.JavaPluginUtil;
import org.bukkit.plugin.Plugin;

public class CurrentPluginImpl implements CurrentPlugin {

    private Plugin plugin;

    @Override
    public Plugin getBukkitPlugin() {
        if (plugin == null)
            plugin = JavaPluginUtil.getProvidingPlugin(CurrentPluginImpl.class);

        return plugin;
    }
}
