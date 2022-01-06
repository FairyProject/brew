package org.imanity.brew;

import com.google.common.base.Preconditions;
import io.fairyproject.container.*;
import io.fairyproject.container.controller.AutowiredContainerController;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.imanity.brew.game.Game;
import org.imanity.brew.game.GameConfigurer;
import org.imanity.brew.game.GameProvider;
import org.imanity.brew.scene.Scene;
import org.imanity.brew.scene.SceneProvider;
import org.imanity.brew.scene.SceneType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class Brew {

    @Autowired
    public static Brew INSTANCE;

    protected static boolean UNIT_TEST = false;
    protected static Plugin UNIT_TEST_PLUGIN;

    private Map<SceneType, SceneProvider> sceneProviders;

    private BrewAdapter brewAdapter;
    private GameProvider gameProvider;
    private GameConfigurer gameConfigurer;
    private Plugin plugin;

    @PreInitialize
    public void onPreInitialize() {
        ComponentRegistry.registerComponentHolder(ComponentHolder.builder()
                .type(BrewAdapter.class)
                .onEnable(obj -> this.brewAdapter = (BrewAdapter) obj)
                .build());
    }

    @PostInitialize
    public void onPostInitialize() {
        Preconditions.checkNotNull(this.brewAdapter, "No Brew Adapter been registered.");
        this.plugin = UNIT_TEST ? UNIT_TEST_PLUGIN : JavaPlugin.getProvidingPlugin(this.getClass());

        this.sceneProviders = new ConcurrentHashMap<>(2);
        final SceneProvider lobbySceneProvider = this.brewAdapter.createLobbySceneProvider();
        if (lobbySceneProvider != null)
            this.sceneProviders.put(SceneType.LOBBY, lobbySceneProvider);
        final SceneProvider gameSceneProvider = this.brewAdapter.createGameSceneProvider();
        if (gameSceneProvider != null)
            this.sceneProviders.put(SceneType.ARENA, gameSceneProvider);
        this.gameProvider = this.brewAdapter.createGameProvider();
        this.gameConfigurer = this.brewAdapter.createGameConfigurer();

        for (SceneProvider sceneProvider : this.sceneProviders.values()) {
            try {
                AutowiredContainerController.INSTANCE.applyObject(sceneProvider);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        try {
            AutowiredContainerController.INSTANCE.applyObject(this.gameProvider);
            AutowiredContainerController.INSTANCE.applyObject(this.gameConfigurer);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        this.sceneProviders.values().forEach(SceneProvider::load);
        this.gameProvider.init();
        this.gameConfigurer.init();
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

}