package com.seikkailupeli;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.seikkailupeli.sprites.SideCharacter;

public class Character {

    private String characterName;
    private Texture characterTexture;
    private SideCharacter sideCharacter;

    private int characterCoordinateX;
    private int characterCoordinateY;

    private int dialogStart;
    private int dialogEnd;
    private boolean dialogsEnabled;

    private boolean movementEnabled;
    private int movementMode = 0; //1 = linear movement, 2 = random movement
    private float movementSpeed;
    private int movementLimitX, movementLimitY;
    private int startX, startY;
    private int destinationX, destinationY;
    private int moveDirection = 0;
    private int distance;
    private boolean stayInsideMap;
    private float waitTime;
    private float timeWaited = 0;
    private boolean isWaiting = false;
    private static int maxDistance = 100;

    Character(String name, Texture texture, int cX, int cY) {

        characterName = name;
        characterTexture = texture;
        characterCoordinateX = cX;
        characterCoordinateY = cY;
        dialogsEnabled = false;
        movementEnabled = false;
    }

    Character(String name, Texture texture, int cX, int cY, int dialogStart, int dialogEnd) {
        characterName = name;
        characterTexture = texture;
        characterCoordinateX = cX;
        characterCoordinateY = cY;

        this.dialogStart = dialogStart;
        this.dialogEnd = dialogEnd;
        dialogsEnabled = true;

        movementEnabled = false;
    }

    Character(String name, SideCharacter sd, int cX, int cY, float speed, int limitX, int limitY, int dialogStart, int dialogEnd) { //linear movement

        characterName = name;
        sideCharacter = sd;
        characterCoordinateX = cX;
        characterCoordinateY = cY;
        movementSpeed = speed;
        movementLimitX = limitX;
        movementLimitY = limitY;
        startX = this.characterCoordinateX;
        startY = this.characterCoordinateY;
        destinationX = startX + limitX;
        destinationY = startY + limitY;
        waitTime = ((float) Math.random() * 4.5f) + 0.5f;
        movementMode = 1;

        this.dialogStart = dialogStart;
        this.dialogEnd = dialogEnd;

        dialogsEnabled = true;

        if (limitX > 0) {
            moveDirection = 2;
        } else if (limitX < 0) {
            moveDirection = 4;
        }

        if (limitY > 0) {
            moveDirection = 1;
        } else if (limitY < 0) {
            moveDirection = 3;
        }

        if (limitX == 0 && limitY == 0) {
            moveDirection = 0;
        }

        this.movementEnabled = !(speed == 0);
    }

    Character(String name, SideCharacter sd, int cX, int cY, boolean stayInMap, int dialogStart, int dialogEnd) { //random movement

        characterName = name;
        sideCharacter = sd;
        characterCoordinateX = cX;
        characterCoordinateY = cY;
        startX = cX;
        startY = cY;

        this.dialogStart = dialogStart;
        this.dialogEnd = dialogEnd;
        dialogsEnabled = true;

        randomizeMovement();
        stayInsideMap = stayInMap;
        movementEnabled = true;
        movementMode = 2;



    }



    void moveCharacter() {

        checkIsWaiting();

        if(isWaiting){
            sideCharacter.currentState = SideCharacter.State.STANDING;
        }


        if (movementEnabled) {
            if (stayInsideMap ) {
                checkIfOutOfMap();
            }

            if (moveDirection == 1) {
                moveUp();
            }

            if (moveDirection == 2) {
                moveRight();
            }

            if (moveDirection == 3) {
                moveDown();
            }

            if (moveDirection == 4) {
                moveLeft();
            }

            if (moveDirection == 0) {
                System.out.println("DIRECTION = 0");

            }
        }
    }

    private void moveUp() {
        if (!isWaiting) {
            sideCharacter.currentState = SideCharacter.State.WALKINGUP;
            this.setCharacterCoordinateY((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateY + (int) movementSpeed);
        }


        if (movementMode == 1) {
            if (this.characterCoordinateY > destinationY) {
                destinationY = startY - movementLimitY;
                moveDirection = 3;
                waitToMove();
            }
        }
        if (movementMode == 2) {
            if (this.characterCoordinateY > startY + distance) {
                randomizeMovement();
                startY = this.characterCoordinateY;
                waitToMove();
            }
        }
    }
    private void moveRight() {
        if (!isWaiting) {
            sideCharacter.currentState = SideCharacter.State.WALKINGRIGHT;
            this.setCharacterCoordinateX((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateX + (int) movementSpeed);
        }



        if (movementMode == 1) {
            if (this.characterCoordinateX > destinationX) {
                destinationX = startX - movementLimitX;
                moveDirection = 4;
                startX = this.characterCoordinateX;
                waitToMove();
            }
        }
        if (movementMode == 2) {
            if (this.characterCoordinateX > startX + distance) {
                randomizeMovement();
                startX = this.characterCoordinateX;
                waitToMove();
            }
        }
    }
    private void moveDown() {
        if (!isWaiting) {
            sideCharacter.currentState = SideCharacter.State.WALKINGDOWN;
            this.setCharacterCoordinateY((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateY - (int) movementSpeed);
        }



        if (movementMode == 1) {
            if (this.characterCoordinateY < destinationY) {
                destinationY = startY + movementLimitY;
                moveDirection = 1;
                waitToMove();
            }
        }
        if (movementMode == 2) {
            if (this.characterCoordinateY < startY - distance) {
                randomizeMovement();
                startY = this.characterCoordinateY;
                waitToMove();
            }
        }
    }
    private void moveLeft() {
        if (!isWaiting) {
            sideCharacter.currentState = SideCharacter.State.WALKINGLEFT;
            this.setCharacterCoordinateX((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateX - (int) movementSpeed);
        }



        if (movementMode == 1) {
            if (this.characterCoordinateX < destinationX) {
                destinationX = startX + movementLimitX;
                moveDirection = 2;
                waitToMove();
            }
        }
        if (movementMode == 2) {
            if (this.characterCoordinateX < startX - distance) {
                randomizeMovement();
                startX = this.characterCoordinateX;
                waitToMove();
            }
        }
    }

    private void randomizeMovement() {

        distance = (int) (Math.random() * 512) + 512;
        moveDirection = (int) (1 + Math.random() * 4);
        movementSpeed = (int) (1 + Math.random() * 4);
    }

    private void checkIfOutOfMap() {

        if (this.characterCoordinateX < 900) {
            this.setCharacterCoordinateX((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateX + (int) movementSpeed);
            moveDirection = 2; //Oikealle
            distance = 256;
            moveLeft();
            System.out.println("CHARACTER MAP BORDER -X");
        }

        if (this.characterCoordinateX > 4000) {
            this.setCharacterCoordinateX((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateX - (int) movementSpeed);
            moveDirection = 4; //Vasemmalle
            distance = 256;
            moveRight();
            System.out.println("CHARACTER MAP BORDER +X");
        }

        if (this.characterCoordinateY < 900) {
            this.setCharacterCoordinateY((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateY + (int) movementSpeed);
            moveDirection = 1;//Ylös
            distance = 256;
            moveUp();
            System.out.println("CHARACTER MAP BORDER -Y");
        }

        if (this.characterCoordinateY > 4000) {
            this.setCharacterCoordinateY((int) (Gdx.graphics.getDeltaTime() * movementSpeed) + characterCoordinateY - (int) movementSpeed);
            moveDirection = 3;//Alas
            distance = 256;
            moveDown();
            System.out.println("CHARACTER MAP BORDER +Y");
        }
    }

    private void waitToMove() {


        isWaiting = true;
        waitTime = (float) (0.5f + Math.random() * 5);
        timeWaited = 0;
    }

    private void checkIsWaiting() {
        if (isWaiting) {
            timeWaited += Gdx.graphics.getDeltaTime();
            if (timeWaited > waitTime) {
                isWaiting = false;
            }
        }
    }



    void pauseMovement() {
        movementEnabled = false;
    }

    void resumeMovement() {
        movementEnabled = true;
    }


    String getCharacterName() {
        return characterName;
    }

    public TextureRegion getCharacterTexture(float dt) {

        return sideCharacter.getFrame(dt);
    }

    int getCharacterCoordinateX() {
        return characterCoordinateX;
    }

    int getCharacterCoordinateY() {
        return characterCoordinateY;
    }

    boolean isDialogsEnabled() {
        return dialogsEnabled;
    }

    void setDialogsDisabled() {
        dialogsEnabled = false;
    }
    void setDialogsEnabled() { dialogsEnabled = true;}

    int getDialogStart() {
        return dialogStart;
    }
    int getDialogEnd() {
        return dialogEnd;
    }

    boolean isMovementEnabled() { return movementEnabled;}

    private void setCharacterCoordinateX(int x) {
        characterCoordinateX = x;
    }

    private void setCharacterCoordinateY(int y) {
        characterCoordinateY = y;
    }

    static int getMaxDistance() {
        return maxDistance;
    }
}
