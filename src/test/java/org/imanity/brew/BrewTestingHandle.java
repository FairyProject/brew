package org.imanity.brew;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.fairyproject.bukkit.reflection.minecraft.MinecraftVersion;
import io.fairyproject.container.ContainerContext;
import io.fairyproject.tests.bukkit.BukkitServerMockImpl;
import io.fairyproject.tests.bukkit.BukkitTestingHandle;
import io.fairyproject.tests.bukkit.FairyBukkitTestingPlatform;

public class BrewTestingHandle implements BukkitTestingHandle {

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
    public String scanPath() {
        return "org.imanity.brew";
    }

    @Override
    public ServerMock createServerMock() {
        return new BukkitServerMockImpl();
    }
}
