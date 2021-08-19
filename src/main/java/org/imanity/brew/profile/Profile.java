package org.imanity.brew.profile;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Profile {

    private final UUID uuid;
    private final String name;

    public Profile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }



}
