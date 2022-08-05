package dev.imanity.brew.scene.json;

import com.google.gson.JsonObject;
import dev.imanity.brew.scene.Scene;

public interface SceneSerializer<T extends Scene> {

    void serialize(T scene, JsonObject jsonObject);

    Scene deserialize(JsonObject jsonObject);

}
