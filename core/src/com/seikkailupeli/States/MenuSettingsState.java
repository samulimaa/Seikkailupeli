package com.seikkailupeli.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.seikkailupeli.Seikkailupeli;


public class MenuSettingsState extends  State {

    private Stage stage;
    private Texture backround;

    //Play button
    private Texture playButtonTexture;
    private TextureRegion playButtonTextureRegion;
    private TextureRegionDrawable playButtonTextureRegionDrawable;
    private ImageButton playButton;


    public MenuSettingsState(GameStateManager gsm) {
        super(gsm);
        backround = new Texture("menu/settingsvalikko.png");
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

        //Play button
        playButtonTexture = new Texture(Gdx.files.internal("actionButton.jpg"));
        playButtonTextureRegion = new TextureRegion(playButtonTexture);
        playButtonTextureRegionDrawable = new TextureRegionDrawable(playButtonTextureRegion);
        playButton = new ImageButton(playButtonTextureRegionDrawable);

        stage = new Stage(new ScreenViewport());

        stage.addActor(playButton);
        Gdx.input.setInputProcessor(stage);

        playButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                gsm.set(new MenuState(gsm));
                dispose();
                return false;
            }
        });

    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        sb.begin();
        sb.draw(backround,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        sb.end();

        playButton.setPosition(80,870);
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        //stage.draw();

    }

    @Override
    public void dispose() {

        backround.dispose();
    }
}

