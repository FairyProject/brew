package org.imanity.brew.scene.json.impl;

import com.google.gson.JsonObject;
import io.fairyproject.bukkit.util.CustomLocation;
import org.imanity.brew.scene.impl.WorldScene;
import org.imanity.brew.scene.json.SceneSerializerBase;

public class WorldSceneSerializer extends SceneSerializerBase<WorldScene> {
    @Override
    public WorldScene deserialize(JsonObject jsonObject) {
        final String world = jsonObject.get("world").getAsString();
        final String location = jsonObject.get("location").getAsString();
        WorldScene worldScene = new WorldScene(world, CustomLocation.stringToLocation(location));
        this.deserializeBase(worldScene, jsonObject);
        return worldScene;
    }

    @Override
    public void serialize(WorldScene scene, JsonObject jsonObject) {
        super.serialize(scene, jsonObject);
        jsonObject.addProperty("world", scene.getWorldName());
        jsonObject.addProperty("location", scene.getSpawnLocation().toString());
    }
}
