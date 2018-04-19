package com.seikkailupeli.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.seikkailupeli.Seikkailupeli;


public class PlayState extends State {

    private Seikkailupeli game;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        game = new Seikkailupeli(gsm);
        game.create();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {


    }

    @Override
    public void render(SpriteBatch sb) {

        game.render();
    }

    @Override
    public void dispose() {

        game.dispose();
    }
}
