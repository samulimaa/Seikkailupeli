package com.seikkailupeli;

import com.badlogic.gdx.graphics.Texture;

public class Inventory extends Seikkailupeli {


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

    public void setDrawnPosX(float x) {
        drawnPosX = x;
    }

    public float getDrawnPosX() {
        return drawnPosX;
    }

    public void setDrawnPosY(float y) {
        drawnPosY = y;
    }

    public float getDrawnPosY() {
        return drawnPosY;
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

    public static int getItemRowNumber()
    {
        return itemRowNumber;
    }
    public static void setItemRowNumber(int number) {
        itemRowNumber = number;
    }
    public static void nextItemRowNumber() {
        itemRowNumber++;
    }

    public void setPositionSaved() {
        positionSaved = true;
    }
    public boolean isPositionSaved() {
        return positionSaved;
    }

    public static int getMaxRows() {
        return maxRows;
    }

    public static boolean checkIsFull() {
        return isFull;
    }

    public static void setFull(boolean b) {
        isFull = b;
    }
}