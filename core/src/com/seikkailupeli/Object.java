

package com.seikkailupeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Object extends Seikkailupeli {

    public Texture itemTexture;
    public String itemName;

    protected int objectCoordinateX;
    protected int objectCoordinateY;

    public Object() {

    }
}

class PickableItem extends Object {

    public Texture itemTexture;
    public String itemName;

    public PickableItem(String name, Texture texture, int itemCoordinateX, int itemCoordinateY) {

        itemName = name;
        itemTexture = texture;
        objectCoordinateX = itemCoordinateX;
        objectCoordinateY = itemCoordinateY;
    }

    public String getItemName() {
        return itemName;

    }
    public Texture getItemTexture() {
        return itemTexture;
    }

    public int getItemCoordinateX() {
        return objectCoordinateX;
    }

    public int getItemCoordinateY() {
        return objectCoordinateY;
    }

}