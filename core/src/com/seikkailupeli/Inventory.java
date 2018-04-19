package com.seikkailupeli;

import com.badlogic.gdx.graphics.Texture;

class Inventory {

    private String itemName;
    private Texture itemTexture;
    private float drawnPosX, drawnPosY;
    private boolean positionSaved = false;

    private static boolean isFull = false;
    private static int row = 0;

    private static int itemsPerRow = 7;
    private static int maxRows = 3;

    private static int distanceBetweenObjectsX = 150;
    private static int distanceBetweenObjectsY = 150;

    private static int itemRowNumber = 0;

    Inventory(String name, Texture texture) {
        itemName = name;
        itemTexture = texture;
    }

    String getItemName() {
        return itemName;
    }

    Texture getTexture() {
        return itemTexture;
    }

    void setDrawnPosX(float x) {
        drawnPosX = x;
    }

    float getDrawnPosX() {
        return drawnPosX;
    }

    void setDrawnPosY(float y) {
        drawnPosY = y;
    }

    float getDrawnPosY() {
        return drawnPosY;
    }

    static int getDistanceBetweenObjectsX(){
        return distanceBetweenObjectsX;
    }

    static int getDistanceBetweenObjectsY() {
        return distanceBetweenObjectsY;
    }

    static int getItemsPerRow() {
        return itemsPerRow;
    }

    static int getRow() {
        return row;
    }

    static void goNextRow() {
        row++;

    }
    static void goFirstRow() {
        row = 0;
    }

    static int getItemRowNumber()
    {
        return itemRowNumber;
    }
    static void setItemRowNumber(int number) {
        itemRowNumber = number;
    }
    static void nextItemRowNumber() {
        itemRowNumber++;
    }

    void setPositionSaved() {
        positionSaved = true;
    }
    boolean isPositionSaved() {
        return positionSaved;
    }

    static int getMaxRows() {
        return maxRows;
    }

    static boolean checkIsFull() {
        return isFull;
    }

    static void setFull(boolean b) {
        isFull = b;
    }
}