package io.fairyproject.brew.scene.json.impl;

import com.google.gson.JsonObject;
import io.fairyproject.brew.scene.impl.WorldScene;
import io.fairyproject.brew.scene.json.NamedSceneSerializer;
import io.fairyproject.mc.util.Position;

public class WorldSceneSerializer implements NamedSceneSerializer<WorldScene> {
    @Override
    public WorldScene deserialize(JsonObject jsonObject) {
        final String world = jsonObject.get("world").getAsString();
        final String location = jsonObject.get("location").getAsString();
        WorldScene worldScene = new WorldScene(world, Position.fromString(location));
        this.doDeserialize(worldScene, jsonObject);
        return worldScene;
    }

    @Override
    public void serialize(WorldScene scene, JsonObject jsonObject) {
        NamedSceneSerializer.super.serialize(scene, jsonObject);

        jsonObject.addProperty("world", scene.getWorldName());
        jsonObject.addProperty("location", scene.getPosition().toString());
    }
}
