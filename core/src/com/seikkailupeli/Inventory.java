package com.seikkailupeli;

import com.badlogic.gdx.graphics.Texture;

public class Inventory extends Seikkailupeli {

    private String itemName;
    private Texture itemTexture;

    private static int row = 1;

    private static int itemsPerRow = 6;

    private static int distanceBetweenObjectsX = 150;
    private static int distanceBetweenObjectsY = 150;

    private static int objectsDrawn;

    public Inventory(String name, Texture texture) {
        itemName = name;
        itemTexture = texture;
    }

    public String getItemName() {
        return itemName;
    }

    public Texture getTexture() {
        return itemTexture;
    }

    public static int getDistanceBetweenObjectsX(){
        return distanceBetweenObjectsX;
    }

    public static int getDistanceBetweenObjectsY() {
        return distanceBetweenObjectsY;
    }

    public static int getItemsPerRow() {
        return itemsPerRow;
    }

    public static int getRow() {
        return row;
    }

    public static void goNextRow() {
        row++;
    }
    public static void goFirstRow() {
        row = 0;
    }

    public static int getObjectsDrawn() {
        return objectsDrawn;
    }
    public static void setObjectsDrawn(int amount) {
        objectsDrawn = amount;
    }



}