

package com.seikkailupeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Object extends Seikkailupeli {

    protected Texture itemTexture;
    protected String itemName;

    protected int objectCoordinateX;
    protected int objectCoordinateY;

    public Object() {

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

class PickableItem extends Object {


    public PickableItem() {

    }

    public PickableItem(String name, Texture texture, int itemCoordinateX, int itemCoordinateY) {

        itemName = name;
        itemTexture = texture;
        objectCoordinateX = itemCoordinateX;
        objectCoordinateY = itemCoordinateY;
    }

}

class RandomSpawnPickableItem extends PickableItem {


    public RandomSpawnPickableItem(String name, Texture texture) {
        itemName = name;
        itemTexture = texture;
        randomizeCoordinates();
    }

    public void randomizeCoordinates() {
        objectCoordinateX = (int) (1 + Math.random() * 50) * 128;
        objectCoordinateY = (int) (1 + Math.random() * 50) * 128;
        //return new int [] {objectCoordinateX, objectCoordinateY};
    }

}