package com.codecool.enterprise.avatar.model;

public class AvatarService {

    private final String avatarURI;

    public AvatarService(String avatarURI) {
        this.avatarURI = avatarURI;
    }

    public String getAvatarURI() {
        return avatarURI;
    }
}
