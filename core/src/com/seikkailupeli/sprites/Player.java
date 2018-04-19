package com.seikkailupeli.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.seikkailupeli.SoundManager;

public class Player extends Sprite {

    public enum State{STANDING, WALKINGUP, WALKINGDOWN, WALKINGRIGHT, WALKINGLEFT}
    private State currentState;
    private State previousState;
    private float stateTimer;
    private boolean walkingup = false;
    private boolean walkingdown = false;
    private boolean walkingright = false;
    private boolean walkingleft = false;

    private Texture img;
    private Texture imgTest;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walkingUp;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walkingDown;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walkingRight;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> walkingLeft;
    private TextureRegion playerStanding;
    private TextureRegion playerStandingUp;
    private TextureRegion playerStandingDown;
    private TextureRegion playerStandingRight;
    private TextureRegion playerStandingLeft;
    private TextureRegion[] standFrames;
    private TextureRegion[] animationFrames;
    private TextureRegion[] upFrames;
    private TextureRegion[] downFrames;
    private TextureRegion[] rightFrames;
    private TextureRegion[] leftFrames;
    public World world;
    public Body b2body;
    float elapsedTime;
    public Vector2 position;

    public float animationSpeed = 0.25f;


    public Player(int x,int y, World world){

        this.world = world;

        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;


        imgTest = new Texture("nuoli.png");
        img = new Texture("test2.png");

        TextureRegion[][] tmp = TextureRegion.split(img,
                img.getWidth() / 3,
                img.getHeight() / 1);

        //Animaatio alaspäin käveltäessä
        TextureRegion[] walkFrames = new TextureRegion[3];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkingDown = new com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>(animationSpeed, walkFrames);


        playerCollision(x,y);
        setBounds(0,0,16, 16);

        playerStanding = new TextureRegion(img,0,0,img.getWidth()/3,img.getHeight());

        //testi 1 framella
        playerStandingUp = new TextureRegion(imgTest,30,0,imgTest.getWidth()/2,imgTest.getHeight()/2);
        playerStandingDown = new TextureRegion(imgTest,30,30,imgTest.getWidth()/2,imgTest.getHeight()/2);
        playerStandingRight = new TextureRegion(imgTest,0,0,imgTest.getWidth()/2,imgTest.getHeight()/2);
        playerStandingLeft = new TextureRegion(imgTest,0,30,imgTest.getWidth()/2,imgTest.getHeight()/2);


        setRegion(playerStanding);


        position = new Vector2(x-img.getWidth()/2,y-img.getHeight()/2);

    }

    public void update(float dt){

        //animation.update(dt);
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        elapsedTime += dt;
        setRegion(getFrame(dt));

    }

    private TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region = playerStanding;
        switch (currentState){
            case WALKINGDOWN:
                region = walkingDown.getKeyFrame(stateTimer, true);
                SoundManager.walkSound.play();
                break;
            case WALKINGUP:
                region = playerStandingUp;
                SoundManager.walkSound.play();
                break;
            case WALKINGRIGHT:
                region = playerStandingRight;
                SoundManager.walkSound.play();
                break;
            case WALKINGLEFT:
                region = playerStandingLeft;
                SoundManager.walkSound.play();
                break;
            /*case STANDING:
                switch (previousState){
                    case WALKINGDOWN:
                        region = walkingDownStop.getKeyFrame(stateTimer, false);
                        break;
                    case WALKINGUP:
                        region = walkingUpStop.getKeyFrame(stateTimer, false);
                        break;
                    case WALKINGRIGHT:
                        region = walkingRightStop.getKeyFrame(stateTimer, false);
                        break;
                    case WALKINGLEFT:
                        region = walkingLeftStop.getKeyFrame(stateTimer, false);
                        break;
                }*/
            default:
                region = playerStanding;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){

        // x- ja y-akseleiden itseisarvot
        float absoluteValueX = Math.abs(b2body.getLinearVelocity().x);
        float absoluteValueY = Math.abs(b2body.getLinearVelocity().y);

        //Määritellään milloin käytetään tiettyyn suuntaan menevää animaatiota
        if(b2body.getLinearVelocity().y<0 && absoluteValueY > absoluteValueX)
            return State.WALKINGDOWN;
        else if(b2body.getLinearVelocity().y>0 && absoluteValueY > absoluteValueX)
            return State.WALKINGUP;
        else if(b2body.getLinearVelocity().x>0 && absoluteValueX > absoluteValueY)
            return State.WALKINGRIGHT;
        else if(b2body.getLinearVelocity().x<0&& absoluteValueX > absoluteValueY)
            return State.WALKINGLEFT;
        else
            return State.STANDING;
    }

    //Pelaajan törmäysalue
    public void playerCollision(int x, int y){

        int radius = 36;
        BodyDef bdef = new BodyDef();
        bdef.position.set(x-radius/2,y-radius/2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }

    public Vector2 getPosition() {
        return position;
    }

    public TextureRegion getPlayerTexture(float dt) {
        return getFrame(dt);
    }

    public void dispose(){
        img.dispose();
    }


}
