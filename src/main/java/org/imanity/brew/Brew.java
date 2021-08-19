package org.imanity.brew;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.fairy.bean.*;
import org.fairy.metadata.AbstractMetadataRegistry;
import org.fairy.metadata.MetadataMap;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.scene.SceneProvider;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.GameConfigurer;
import org.imanity.brew.game.GameProvider;
import org.imanity.brew.scene.SceneType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(name = "brew")
@ClasspathScan("org.imanity.brew")
@Getter
public class Brew {

    @Autowired
    public static Brew INSTANCE;

    private Map<SceneType, SceneProvider> sceneProviders;

    private BrewAdapter brewAdapter;
    private GameProvider gameProvider;
    private GameConfigurer gameConfigurer;
    private Plugin plugin;

    private GameMetadataRegistry gameMetadataRegistry;

    @PreInitialize
    public void onPreInitialize() {
        ComponentRegistry.registerComponentHolder(new ComponentHolder() {
            @Override
            public Class<?>[] type() {
                return new Class[] {BrewAdapter.class};
            }

            @Override
            public void onEnable(Object instance) {
                brewAdapter = (BrewAdapter) instance;
            }
        });
    }

    @PostInitialize
    public void onPostInitialize() {
        Preconditions.checkNotNull(this.brewAdapter, "No Brew Adapter been registered.");
        this.sceneProviders = new ConcurrentHashMap<>(2);
        this.sceneProviders.put(SceneType.LOBBY, this.brewAdapter.createLobbySceneProvider());
        this.gameProvider = this.brewAdapter.createGameProvider();
        this.gameConfigurer = this.brewAdapter.createGameConfigurer();

        this.sceneProviders.values().forEach(Beans::inject);
        Beans.inject(this.gameProvider);
        Beans.inject(this.gameConfigurer);

        this.sceneProviders.values().forEach(SceneProvider::load);
        this.gameProvider.init();
        this.gameConfigurer.init();

        this.plugin = JavaPlugin.getProvidingPlugin(this.getClass());
        this.gameMetadataRegistry = new GameMetadataRegistry();
    }

    public Scene findSceneByType(SceneType type, Game game) {
        return this.sceneProviders
                .get(type)
                .find(game);
    }

    public boolean joinRandomGame(Player player) {
        final Game game = this.gameProvider.find(player);

        if (game == null) {
            return false;
        }

        game.addPlayer(player);
        return true;
    }

    public static MetadataMap metadataOf(Game game) {
        return INSTANCE.gameMetadataRegistry.provide(game);
    }

    public static void clear(Game game) {
        INSTANCE.gameMetadataRegistry.remove(game);
    }

    private static final class GameMetadataRegistry extends AbstractMetadataRegistry<Integer> {

        public MetadataMap provide(Game game) {
            return provide(game.getId());
        }

        public void remove(Game game) {
            this.remove(game.getId());
        }

    }

}