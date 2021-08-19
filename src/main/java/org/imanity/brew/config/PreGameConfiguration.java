package org.imanity.brew.config;

import org.fairy.config.yaml.YamlConfiguration;

import java.nio.file.Path;

public class PreGameConfiguration extends YamlConfiguration {

    public static boolean INITIALIZED = false;

    protected PreGameConfiguration(Path path) {
        super(path);
    }

    @Override
    protected void postLoad() {
        INITIALIZED = true;

    }
}
