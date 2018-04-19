package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.seikkailupeli.SoundManager;
import com.seikkailupeli.States.GameStateManager;
import com.seikkailupeli.States.MenuState;

public class MyGdxGame extends Game {
	private SpriteBatch batch;
	private GameStateManager gsm;

	@Override
	public void create () {
		batch = new SpriteBatch();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));

	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}