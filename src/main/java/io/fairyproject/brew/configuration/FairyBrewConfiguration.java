package io.fairyproject.brew.configuration;

import io.fairyproject.brew.CurrentPlugin;
import io.fairyproject.brew.CurrentPluginImpl;
import io.fairyproject.container.InjectableComponent;
import io.fairyproject.container.configuration.Configuration;

@Configuration
public class FairyBrewConfiguration {

    @InjectableComponent
    public CurrentPlugin currentPlugin() {
        return new CurrentPluginImpl();
    }

}
