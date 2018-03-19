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

	int x = 1920;
	int y = 1080;

	int animationPosX = 960;
	int animationPosY = 540;
	float animationSpeed = 0.025f;
	float stateTime;
	float animationTime;

	boolean animationRunning = false;

	// Objects used
	Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	SpriteBatch spriteBatch;
	SpriteBatch batch;
	SpriteBatch cameraBatch;
	//SpriteBatch mapbatch;

	Texture button;
	Texture animationSheet;
	Texture map;

	OrthographicCamera camera;


	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		System.out.println("CREATE");

		System.out.println("CREATE 2");

		//tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);


		System.out.println("CREATE 3");

		//float w = Gdx.graphics.getWidth();
		//float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(1920, 1080);
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();


		batch = new SpriteBatch();
		button = new Texture(Gdx.files.internal("button.jpg"));
		//cameraBatch = new SpriteBatch();
		//cameraBatch.setProjectionMatrix();

		System.out.println("CREATE 4");
		map = new Texture(Gdx.files.internal("map.jpg"));

		System.out.println("CREATE 5");




		animationSheet = new Texture(Gdx.files.internal("animation_sheet.png"));
		//map = new Texture("map.png");

		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(animationSheet,
				animationSheet.getWidth() / FRAME_COLS,
				animationSheet.getHeight() / FRAME_ROWS);

		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}

		// Initialize the Animation with the frame interval and array of frames
		animation = new Animation<TextureRegion>(animationSpeed, walkFrames);

		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void render () {

		//System.out.println("RENDER");

		batch.setProjectionMatrix(camera.combined);
		//camera.position.set(((float) animationPosX), (float) animationPosY,0);
		camera.update();

		//System.out.println(animationPosX);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		batch.begin();
		batch.draw(button, 200, 200);
		batch.draw(map, 960, 540);
		//batch.draw(button, camera.position.x, camera.position.y);
		batch.end();

		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, animationPosX, animationPosY);
		spriteBatch.end();

		if (animationRunning) {
			stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		}

		animationTime += Gdx.graphics.getDeltaTime();

		if (animationTime > 1.0f) {
			animationRunning = false;
			stateTime = 0.025f;
		}

		System.out.println(animationPosX);



	}

	@Override
	public void dispose () {
		batch.dispose();
		button.dispose();
		spriteBatch.dispose();
	}

	public void moveLeft(int pixels) {
		//animationPosX -= pixels;
		camera.translate(-pixels, 0, 0);
		System.out.println("MOVE LEFT");
	}

	public void moveRight(int pixels) {
		//animationPosX += pixels;
		camera.translate(+pixels, 0, 0);

		System.out.println("MOVE RIGHT");
	}

	public void moveUp(int pixels) {
		//animationPosY += pixels;
		camera.translate(0, +pixels);
		System.out.println("MOVE UP");
	}

	public void moveDown(int pixels) {
		//animationPosY -= pixels;
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
		System.out.println("TOUCHDOWN! " + "X: "+ String.valueOf(screenX) + "  Y:  " + String.valueOf(screenY));

		if (screenX < 300) {
			moveLeft(32);
			playAnimation();
		}

		if (screenX > 1600) {
			moveRight(32);
			playAnimation();
		}

		if (screenY < 200) {
			moveUp(32);
			playAnimation();
		}

		if (screenY > 900) {
			moveDown(32);
			playAnimation();
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

