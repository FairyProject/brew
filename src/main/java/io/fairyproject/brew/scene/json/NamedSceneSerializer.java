package io.fairyproject.brew.scene.json;

import com.google.gson.JsonObject;
import io.fairyproject.brew.scene.Scene;

public interface NamedSceneSerializer<T extends Scene> extends SceneSerializer<T> {
    @Override
    default void serialize(T scene, JsonObject jsonObject) {
        jsonObject.addProperty("name", scene.getName());
    }

    default void doDeserialize(T scene, JsonObject jsonObject) {
        scene.setName(jsonObject.get("name").getAsString());
    }
}
