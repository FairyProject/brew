package org.imanity.brew;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.fairyproject.bukkit.reflection.minecraft.MinecraftVersion;
import io.fairyproject.container.ContainerContext;
import io.fairyproject.tests.TestingHandle;
import io.fairyproject.tests.bukkit.FairyBukkitTestingPlatform;
import org.jetbrains.annotations.Nullable;

public class BrewTestingHandle implements TestingHandle {

    private final BrewPlugin PLUGIN = new BrewPlugin();

    @Override
    public io.fairyproject.plugin.Plugin plugin() {
        return PLUGIN;
    }

    @Override
    public io.fairyproject.FairyPlatform platform() {
        ContainerContext.SHOW_LOGS = true;

        Brew.UNIT_TEST = true;
        Brew.UNIT_TEST_PLUGIN = MockBukkit.createMockPlugin();

        return new FairyBukkitTestingPlatform() {
            @Override
            public MinecraftVersion version() {
                return new MinecraftVersion("v1_16_R3", 11603);
            }
        };
    }

    @Override
    public @Nullable String scanPath() {
        return "org.imanity.brew";
    }
}
