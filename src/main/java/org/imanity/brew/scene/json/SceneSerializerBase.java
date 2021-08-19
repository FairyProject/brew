package org.imanity.brew.scene.json;

import com.google.gson.JsonObject;
import org.imanity.brew.scene.Scene;

public abstract class SceneSerializerBase<T extends Scene> implements SceneSerializer<T> {
    @Override
    public void serialize(T scene, JsonObject jsonObject) {
        jsonObject.addProperty("name", scene.getName());
    }

    protected final void deserializeBase(T scene, JsonObject jsonObject) {
        scene.setName(jsonObject.get("name").getAsString());
    }
}
