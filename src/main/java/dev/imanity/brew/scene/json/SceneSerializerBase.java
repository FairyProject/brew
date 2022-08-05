package dev.imanity.brew.scene.json;

import com.google.gson.JsonObject;
import dev.imanity.brew.scene.Scene;

public abstract class SceneSerializerBase<T extends Scene> implements SceneSerializer<T> {
    @Override
    public void serialize(T scene, JsonObject jsonObject) {
        jsonObject.addProperty("name", scene.name());
    }

    protected final void deserializeBase(T scene, JsonObject jsonObject) {
        scene.name(jsonObject.get("name").getAsString());
    }
}
