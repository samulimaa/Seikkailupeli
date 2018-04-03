

package com.seikkailupeli;

import com.badlogic.gdx.graphics.Texture;

public class Object extends Seikkailupeli {

    Texture itemTexture;
    String itemName;

    int objectCoordinateX;
    int objectCoordinateY;

    public Object() {

    }


    String getItemName() {
        return itemName;
    }

    Texture getItemTexture() {
        return itemTexture;
    }

    int getItemCoordinateX() {
        return objectCoordinateX;
    }

    int getItemCoordinateY() {
        return objectCoordinateY;
    }
}

class PickableItem extends Object {


    PickableItem() {

    }

    PickableItem(String name, Texture texture, int itemCoordinateX, int itemCoordinateY) {

        itemName = name;
        itemTexture = texture;
        objectCoordinateX = itemCoordinateX;
        objectCoordinateY = itemCoordinateY;
    }

}

class RandomSpawnPickableItem extends PickableItem {

    RandomSpawnPickableItem(String name, Texture texture) {
        itemName = name;
        itemTexture = texture;
        randomizeCoordinates();
    }

    private void randomizeCoordinates() {
        objectCoordinateX = (int) (1 + Math.random() * 50) * 128;
        objectCoordinateY = (int) (1 + Math.random() * 50) * 128;
    }

}