package com.seikkailupeli;

import com.badlogic.gdx.graphics.Texture;

public class Character extends Seikkailupeli {

    private String characterName;
    private Texture characterTexture;

    private int characterCoordinateX;
    private int characterCoordinateY;

    private String characterDialog;

    public Character(String name, Texture texture, int cX, int cY) {

        characterName = name;
        characterTexture = texture;
        characterCoordinateX = cX;
        characterCoordinateY = cY;
    }

    Character(String name, Texture texture, int cX, int cY, String dialog) {

        characterName = name;
        characterTexture = texture;
        characterCoordinateX = cX;
        characterCoordinateY = cY;
        characterDialog = dialog;
    }

    String getCharacterName() {
        return characterName;
    }

    Texture getCharacterTexture() {
        return characterTexture;
    }

    String getCharacterDialog() {
        return characterDialog;
    }

    int getCharacterCoordinateX() {
        return characterCoordinateX;
    }

    int getCharacterCoordinateY() {
        return characterCoordinateY;
    }
}