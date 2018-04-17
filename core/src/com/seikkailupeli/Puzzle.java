package com.seikkailupeli;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.Arrays;

class Puzzle extends Object {

    private boolean isCompleted = false;

    private String question;

    private int[] correctValue;
    private int[] currentValue;
    private int[] originalValue;

    private int teleportCoordinateX1;
    private int teleportCoordinateY1;
    private int teleportCoordinateX2;
    private int teleportCoordinateY2;

    private int backgroundOffsetX = -550;
    private int backgroundOffsetY = -330;

    private int questionOffsetX = -300;
    private int questionOffsetY = 550;

    private int numbersOffsetX = -440;
    private int numbersOffsetY = 0;

    private int distanceToNextButton = 250;
    private int buttonSize = 100;

    private Texture backgroundTexture;
    private Texture teleportAreaTexture;
    private BitmapFont questionFont;
    private BitmapFont numbersFont;

    Puzzle(Texture texture, Texture background, int coordinateX, int coordinateY, String q, int[] startNumbers, int[] correctNumbers,
            Texture teleportTexture, int teleportX1, int teleportY1, int teleportX2, int teleportY2) {
        itemTexture = texture;
        backgroundTexture = background;
        teleportAreaTexture = teleportTexture;
        objectCoordinateX = coordinateX;
        objectCoordinateY = coordinateY;
        question = q;
        currentValue = startNumbers;
        originalValue = currentValue;
        correctValue = correctNumbers;

        teleportCoordinateX1 = teleportX1;
        teleportCoordinateY1 = teleportY1;
        teleportCoordinateX2 = teleportX2;
        teleportCoordinateY2 = teleportY2;

        questionFont = new BitmapFont();
        questionFont.getData().setScale(7);

        numbersFont = new BitmapFont();
        numbersFont.getData().setScale(15);
    }

    String getQuestion() {
        return question;
    }

    int[] getCorrectValue() {
        return correctValue;
    }

    Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    void setCurrentValue(int button) {

        switch (button) {
            case 0:
                currentValue[0]++;
                break;
            case 1:
                currentValue[1]++;
                break;
            case 2:
                currentValue[2]++;
                break;
            case 3:
                currentValue[3]++;
                break;
            case 4:
                currentValue[0]--;
                break;
            case 5:
                currentValue[1]--;
                break;
            case 6:
                currentValue[2]--;
                break;
            case 7:
                currentValue[3]--;
                break;
        }

        for (int i = 0; i < 4; i++) {
            if (currentValue[i] > 9) {
                currentValue[i] = 0;
                break;
            }
            if (currentValue[i] < 0) {
                currentValue[i] = 9;
                break;
            }
        }

        checkIsCorrect();
    }

    void setValueToOriginal() {
        currentValue = originalValue;
    }

    int[] getCurrentValue() {
        return currentValue;
    }

    boolean checkIsCorrect() {
        isCompleted = Arrays.equals(currentValue, correctValue);
        return isCompleted;
    }

    int[] getTeleport1Coordinates() {
        return new int[] {teleportCoordinateX1, teleportCoordinateY1};
    }

    int[] getTeleport2Coordinates() {
        return new int[] {teleportCoordinateX2, teleportCoordinateY2};
    }

    Texture getTeleportAreaTexture() {
        return teleportAreaTexture;
    }

    int getBackgroundOffsetX() {
        return backgroundOffsetX;
    }
    int getBackgroundOffsetY() {
        return backgroundOffsetY;
    }
    int getQuestionOffsetX() {
        return questionOffsetX;
    }
    int getQuestionOffsetY() {
        return questionOffsetY;
    }
    int getNumbersOffsetX() {
        return numbersOffsetX;
    }
    int getNumbersOffsetY() {
        return numbersOffsetY;
    }
    int getDistanceToNextButton() {
        return distanceToNextButton;
    }
    int getButtonSize() {
        return buttonSize;
    }

    BitmapFont getQuestionFont() {
        return questionFont;
    }

    BitmapFont getNumbersFont() {
        return numbersFont;
    }
}
