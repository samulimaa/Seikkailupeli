package com.seikkailupeli;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Seikkailupeli extends ApplicationAdapter implements InputProcessor{

	// Constant rows and columns of the sprite sheet
	private static final int FRAME_COLS = 6, FRAME_ROWS = 5;


	int playerPosX;
	int playerPosY;

	float animationSpeed = 0.025f;
	float stateTime;
	float animationTime;

	boolean animationRunning = false;
	boolean drawObjectEnabled = true;
	boolean uiButtonsEnabled = true;
	boolean movementEnabled = true;


	// Objects used
	Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	SpriteBatch spriteBatch;
	SpriteBatch batch;
	//SpriteBatch cameraBatch;

	Texture button;
	Texture animationSheet;
	Texture map;
	Texture greenObject;

	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	OrthographicCamera camera;


	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		System.out.println("CREATE");

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();


		playerPosX = w / 2;
		playerPosY = h / 2;

		//tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		tiledMap = new TmxMapLoader().load("testmap.tmx");

		System.out.println("CREATE 2");

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		System.out.println("CREATE 3");

		camera = new OrthographicCamera(w, h);
		camera.update();


		batch = new SpriteBatch();
		button = new Texture(Gdx.files.internal("button.jpg"));

		System.out.println("CREATE 4");
		map = new Texture(Gdx.files.internal("map.jpg"));

		System.out.println("CREATE 5");

		greenObject = new Texture(Gdx.files.internal("greenObject.jpg"));


		animationSheet = new Texture(Gdx.files.internal("animation_sheet.png"));
		//map = new Texture("map.png");

		TextureRegion[][] tmp = TextureRegion.split(animationSheet,
				animationSheet.getWidth() / FRAME_COLS,
				animationSheet.getHeight() / FRAME_ROWS);


		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		// Initialize the Animation with the frame interval and array of frames
		animation = new Animation<TextureRegion>(animationSpeed, walkFrames);

		spriteBatch = new SpriteBatch();
		stateTime = 0f;

	}

	@Override
	public void render () {

		//System.out.println("RENDER");

		batch.setProjectionMatrix(camera.combined);
		camera.update();

		//System.out.println(playerPosX);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		if (drawObjectEnabled) {
			drawObjects();
		}

		if (uiButtonsEnabled) {
			drawButtons();
		}

		if (animationRunning) {
			stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		}

		animationTime += Gdx.graphics.getDeltaTime();

		if (animationTime > 1.0f) {
			animationRunning = false;
			stateTime = 0.025f;
		}

	}

	public void drawObjects() {
		batch.begin();
		//batch.draw(button, 500, 500);
		//batch.draw(map, 0, 0);

		for (int i = 0; i <= 5; i++) {
			batch.draw(greenObject, -300 + i * 200, -300);
			batch.draw(greenObject, -300, -300 + i * 200);
		}

		//batch.draw(button, camera.position.x, camera.position.y); //debug kameran paikka
		batch.end();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();


		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, playerPosX - 50, playerPosY - 40);
		spriteBatch.end();
	}

	public void drawButtons() {
		batch.begin();

		float cX = camera.position.x;
		float cY = camera.position.y;

		//left
		batch.draw(button, cX - 900, cY - 350);

		//right
		batch.draw(button, cX - 600, cY - 350);

		//up
		batch.draw(button, cX - 750, cY - 200);

		//down
		batch.draw(button, cX - 750, cY - 500);

		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		button.dispose();
		spriteBatch.dispose();
		greenObject.dispose();
		animationSheet.dispose();
	}

	public void moveLeft(int pixels) {
		//playerPosX -= pixels;
		camera.translate(-pixels, 0, 0);
		System.out.println("MOVE LEFT");
	}

	public void moveRight(int pixels) {
		//playerPosX += pixels;
		camera.translate(+pixels, 0, 0);

		System.out.println("MOVE RIGHT");
	}

	public void moveUp(int pixels) {
		//playerPosY += pixels;
		camera.translate(0, +pixels);
		System.out.println("MOVE UP");
	}

	public void moveDown(int pixels) {
		//playerPosY -= pixels;
		camera.translate(0, -pixels);
		System.out.println("MOVE DOWN");
	}

	public void playAnimation() {

		if (!animationRunning) {
		}

		animationRunning = true;
		animationTime = 0;
	}



	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("TOUCHDOWN! " + "X: " + String.valueOf(screenX) + "  Y:  " + String.valueOf(screenY));

		int moveAmount = 50;

		if (movementEnabled && uiButtonsEnabled) {
			if (screenX > 30 && screenX < 220 && screenY > 700 && screenY < 900) {
				moveLeft(moveAmount);
				playAnimation();
			}

			if (screenX > 340 && screenX < 500 && screenY > 700 && screenY < 900) {
				moveRight(moveAmount);
				playAnimation();
			}

			if (screenX > 200 && screenX < 360 && screenY > 600 && screenY < 740) {
				moveUp(moveAmount);
				playAnimation();
			}

			if (screenX > 200 && screenX < 360 && screenY > 900 && screenY < 1064) {
				moveDown(moveAmount);
				playAnimation();
			}
		}


		if (movementEnabled && !uiButtonsEnabled) {

			if (screenX < 300) {
				moveLeft(moveAmount);
				playAnimation();
			}

			if (screenX > 1600) {
				moveRight(moveAmount);
				playAnimation();
			}

			if (screenY < 200) {
				moveUp(moveAmount);
				playAnimation();
			}

			if (screenY > 900) {
				moveDown(moveAmount);
				playAnimation();
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
}

