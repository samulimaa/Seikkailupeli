package com.seikkailupeli;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.ArrayList;
import java.util.List;


public class Seikkailupeli extends ApplicationAdapter implements InputProcessor{


	// Constant rows and columns of the player sprite sheet
	private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

	int playerPosX;
	int playerPosY;

	float playerAnimationSpeed = 0.025f;
	float playerAnimationStateTime;
	float playerAnimatonTime;

	boolean playerAnimatonRunning = false;

	boolean drawObjectEnabled = true;
	boolean tilemapEnabled = true;
	boolean uiButtonsEnabled = true;
	boolean movementEnabled = true;
	boolean actionPointsEnabled = true;

	List<Integer> actionCoordinatesX = new ArrayList<Integer>();
	List<Integer> actionCoordinatesY = new ArrayList<Integer>();

	// Objects used
	Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	SpriteBatch spriteBatch;
	SpriteBatch batch;

	BitmapFont font;
	Toast toast;

	Texture button;
	Texture actionButton;
	Texture animationSheet;
	Texture map;
	Texture greenObject;
	Texture actionObject;

	TiledMap tiledMap;
	TiledMapRenderer tiledMapRenderer;

	OrthographicCamera camera;


	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		System.out.println("CREATE");

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		playerPosX = w / 2 + 50;
		playerPosY = h / 2 + 40;

		font = new BitmapFont();
		Toast.ToastFactory toastFactory = new Toast.ToastFactory.Builder().font(font).build();

		toast = toastFactory.create("TOAST TEST!!", Toast.Length.LONG);

		//tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		tiledMap = new TmxMapLoader().load("testmap.tmx");

		System.out.println("CREATE 2");

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 4);

		System.out.println("CREATE 3");

		camera = new OrthographicCamera(w, h);
		camera.update();

		batch = new SpriteBatch();

		button = new Texture(Gdx.files.internal("button.jpg"));
		actionButton = new Texture(Gdx.files.internal("actionButton.jpg"));

		System.out.println("CREATE 4");
		map = new Texture(Gdx.files.internal("map.jpg"));

		System.out.println("CREATE 5");

		greenObject = new Texture(Gdx.files.internal("greenObject.jpg"));
		actionObject = new Texture(Gdx.files.internal("actionObject.jpg"));

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

		// Initialize the player Animation with the frame interval and array of frames
		animation = new Animation<TextureRegion>(playerAnimationSpeed, walkFrames);

		spriteBatch = new SpriteBatch();
		playerAnimationStateTime = 0f;

		defineActionCoordinates();

	}

	public void defineActionCoordinates() { //maaritellaan action paikkojen koordinaatit
		actionCoordinatesX.add(-256);
		actionCoordinatesY.add(256);

		actionCoordinatesX.add(-512);
		actionCoordinatesY.add(-512);

		actionCoordinatesX.add(1000);
		actionCoordinatesY.add(1000);

		if (actionCoordinatesX.size() != actionCoordinatesY.size()) {
			System.out.println("CHECK COORDINATES!");
		}

	}

	@Override
	public void render () {

		//System.out.println("RENDER");

		batch.setProjectionMatrix(camera.combined);
		camera.update();

		//System.out.println(playerPosX);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		toast.render(Gdx.graphics.getDeltaTime());


		if (drawObjectEnabled) {
			drawTextures();
		}

		if (uiButtonsEnabled) {
			drawButtons();
		}

		if (playerAnimatonRunning) {
			playerAnimationStateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		}

		playerAnimatonTime += Gdx.graphics.getDeltaTime();

		if (playerAnimatonTime > 0.8f) {
			playerAnimatonRunning = false;
			playerAnimationStateTime = 0.025f;
		}

	}

	public void drawTextures() {
		if (tilemapEnabled) {
			tiledMapRenderer.setView(camera);
			tiledMapRenderer.render();
		}
		//batch.draw(button, 500, 500);
		//batch.draw(map, 0, 0);
		batch.begin();


		for (int i = 0; i <= 5; i++) {
			batch.draw(greenObject, -300 + i * 200, -300);
			batch.draw(greenObject, -300, -300 + i * 200);
		}

		//batch.draw(button, camera.position.x, camera.position.y); //debug kameran paikka

		if (actionPointsEnabled) {
			for (int i = 0; i < actionCoordinatesX.size(); i++) {
				batch.draw(actionObject, actionCoordinatesX.get(i), actionCoordinatesY.get(i));
			}
		}

		batch.end();

		TextureRegion currentFrame = animation.getKeyFrame(playerAnimationStateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, playerPosX - 50, playerPosY - 40);
		spriteBatch.end();
	}


	public void drawButtons() {
		batch.begin();

		float cX = camera.position.x;
		float cY = camera.position.y;

		//System.out.println(cX);

		//left
		batch.draw(button, cX - 900, cY - 350);

		//right
		batch.draw(button, cX - 600, cY - 350);

		//up
		batch.draw(button, cX - 750, cY - 200);

		//down
		batch.draw(button, cX - 750, cY - 500);

		//action button
		batch.draw(actionButton, cX + 750, cY - 500);


		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		button.dispose();
		spriteBatch.dispose();
		greenObject.dispose();
		actionButton.dispose();
		animationSheet.dispose();
		tiledMap.dispose();
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

	public void playPlayerAnimation() {

		if (!playerAnimatonRunning) {
		}

		playerAnimatonRunning = true;
		playerAnimatonTime = 0;
	}

	public void checkAction() 		//tarkista onko action pisteita lahella kun action buttonia painetaan
	{
		System.out.println("CHECK ACTION! x = " + Float.toString(camera.position.x) +"  y = "+ Float.toString(camera.position.y));

		int distanceToObject = 128;

		for (int i = 0; i < actionCoordinatesX.size(); i++) {
		if (camera.position.x - actionCoordinatesX.get(i) <= distanceToObject && camera.position.x - actionCoordinatesX.get(i) >= -distanceToObject
				&& camera.position.y - actionCoordinatesY.get(i) <= distanceToObject && camera.position.y - actionCoordinatesY.get(i) >= -distanceToObject)
		{
			System.out.println("NEAR POINT:  " + Integer.toString(i+1));
		}
	}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("TOUCHDOWN! " + "X: " + String.valueOf(screenX) + "  Y:  " + String.valueOf(screenY));

		int moveAmount = 128;

		if (movementEnabled && uiButtonsEnabled) {
			if (screenX > 30 && screenX < 220 && screenY > 700 && screenY < 900) {
				moveLeft(moveAmount);
				playPlayerAnimation();
			}

			if (screenX > 340 && screenX < 500 && screenY > 700 && screenY < 900) {
				moveRight(moveAmount);
				playPlayerAnimation();
			}

			if (screenX > 200 && screenX < 360 && screenY > 600 && screenY < 740) {
				moveUp(moveAmount);
				playPlayerAnimation();
			}

			if (screenX > 200 && screenX < 360 && screenY > 900 && screenY < 1064) {
				moveDown(moveAmount);
				playPlayerAnimation();
			}
		}

		if (uiButtonsEnabled && actionPointsEnabled) {
			if (screenX > 1670 && screenX < 1910 && screenY > 850 && screenY < 1050) {
				checkAction();
			}
		}


		if (movementEnabled && !uiButtonsEnabled) { //ruudun reunoilta liikkuminen (ei kayteta)

			if (screenX < 300) {
				moveLeft(moveAmount);
				playPlayerAnimation();
			}

			if (screenX > 1600) {
				moveRight(moveAmount);
				playPlayerAnimation();
			}

			if (screenY < 200) {
				moveUp(moveAmount);
				playPlayerAnimation();
			}

			if (screenY > 900) {
				moveDown(moveAmount);
				playPlayerAnimation();
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

