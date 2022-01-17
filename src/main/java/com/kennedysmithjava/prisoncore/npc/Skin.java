package com.kennedysmithjava.prisoncore.npc;

public class Skin {

    String texture;
    String signature;

    public Skin(String texture, String signature){
        this.texture = texture;
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public String getTexture() {
        return texture;
    }
}
