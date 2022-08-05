package dev.imanity.brew;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import io.fairyproject.bukkit.reflection.minecraft.OBCVersion;
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
        Brew.UNIT_TEST = true;
        Brew.UNIT_TEST_PLUGIN = MockBukkit.createMockPlugin();

        return new FairyBukkitTestingPlatform() {
            @Override
            public OBCVersion version() {
                return OBCVersion.v1_16_R3;
            }
        };
    }

    @Override
    public String scanPath() {
        return "dev.imanity.brew";
    }

    @Override
    public ServerMock createServerMock() {
        return new BukkitServerMockImpl();
    }
}
