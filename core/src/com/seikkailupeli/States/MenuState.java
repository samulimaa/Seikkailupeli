package com.seikkailupeli.States;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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
import com.seikkailupeli.SoundManager;

public class MenuState extends State{

    private Stage stage;
    private Texture backround;

    //Play button
    private Texture playButtonTexture;
    private TextureRegion playButtonTextureRegion;
    private TextureRegionDrawable playButtonTextureRegionDrawable;
    private ImageButton playButton;

    //Exit button
    private Texture exitButtonTexture;
    private TextureRegion exitButtonTextureRegion;
    private TextureRegionDrawable exitButtonTextureRegionDrawable;
    private ImageButton exitButton;

    //Settings button
    private Texture settingsButtonTexture;
    private TextureRegion settingsButtonTextureRegion;
    private TextureRegionDrawable settingsButtonTextureRegionDrawable;
    private ImageButton settingsButton;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        backround = new Texture("menu/retrograd.png");

        SoundManager.create();
        SoundManager.music.setLooping(true);
        SoundManager.music.setVolume(1f);
        SoundManager.music.play();

    }

    @Override
    public void handleInput() {

        /*if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
            dispose();
        }*/
    }

    @Override
    public void update(float dt) {


        handleInput();

        //Play button
        playButtonTexture = new Texture(Gdx.files.internal("menu/playButton.png"));
        playButtonTextureRegion = new TextureRegion(playButtonTexture);
        playButtonTextureRegionDrawable = new TextureRegionDrawable(playButtonTextureRegion);
        playButton = new ImageButton(playButtonTextureRegionDrawable);

        //Exit button
        exitButtonTexture = new Texture(Gdx.files.internal("actionButton.jpg"));
        exitButtonTextureRegion = new TextureRegion(exitButtonTexture);
        exitButtonTextureRegionDrawable = new TextureRegionDrawable(exitButtonTextureRegion);
        exitButton = new ImageButton(exitButtonTextureRegionDrawable);

        //Settings button
        settingsButtonTexture = new Texture(Gdx.files.internal("actionButton.jpg"));
        settingsButtonTextureRegion = new TextureRegion(settingsButtonTexture);
        settingsButtonTextureRegionDrawable = new TextureRegionDrawable(settingsButtonTextureRegion);
        settingsButton = new ImageButton(settingsButtonTextureRegionDrawable);


        stage = new Stage(new ScreenViewport());

        stage.addActor(playButton);
        stage.addActor(exitButton);
        stage.addActor(settingsButton);
        Gdx.input.setInputProcessor(stage);

        playButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                gsm.set(new PlayState(gsm));
                dispose();
                return false;
            }
        });

        exitButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                Gdx.app.exit();
                dispose();
                return false;
            }
        });

        settingsButton.addListener(new EventListener()
        {
            @Override
            public boolean handle(Event event)
            {
                gsm.set(new MenuSettingsState(gsm));
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


        playButton.setPosition(680,350);
        exitButton.setPosition(0,900);
        settingsButton.setPosition(1750,900);
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        //stage.draw();

    }

    @Override
    public void dispose() {
        backround.dispose();
        SoundManager.dispose();
    }
}
