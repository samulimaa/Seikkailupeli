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

	private int playerSpawnPosX = 768;
	private int playerSpawnPosY = 512;

	private int playerPosX;
	private int playerPosY;

	private float playerAnimationSpeed = 0.025f;
	private float playerAnimationStateTime;
	private float playerAnimationTime;

	private boolean playerAnimationRunning = false;

	private boolean drawObjectEnabled = true;
	private boolean tilemapEnabled = true;
	private boolean uiButtonsEnabled = true;
	private boolean movementEnabled = true;

	private Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	private SpriteBatch spriteBatch;
	private SpriteBatch batch;

	private BitmapFont font;
	private Toast toast;

	private Texture button;
	private Texture actionButton;
	private Texture animationSheet;
	private Texture greenObject;
	private Texture actionObject;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;

	private OrthographicCamera camera;

	public PickableItem pickableItem;

	private List<PickableItem> pickableItemList = new ArrayList<PickableItem>();
	private List<String> inventoryList = new ArrayList<String>();

	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		System.out.println("CREATE");

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		playerPosX = w / 2 + 30;
		playerPosY = h / 2 - 30;

		System.out.println("CREATE 2");

		tiledMap = new TmxMapLoader().load("map1.tmx");

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 4);

		System.out.println("CREATE 3");

		camera = new OrthographicCamera(w, h);
		camera.position.set(playerSpawnPosX, playerSpawnPosY, 0);
		camera.update();

		batch = new SpriteBatch();

		button = new Texture(Gdx.files.internal("button.jpg"));
		actionButton = new Texture(Gdx.files.internal("actionButton.jpg"));

		System.out.println("CREATE 4");

		greenObject = new Texture(Gdx.files.internal("greenObject.jpg"));
		actionObject = new Texture(Gdx.files.internal("actionObject.jpg"));

		animationSheet = new Texture(Gdx.files.internal("animation_sheet.png"));

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


		System.out.println("CREATE 5");

		placeItems();
	}

	private void placeItems() { //maaritellaan poimittavat tavarat yms

		pickableItemList.add (new PickableItem("tavara1", actionObject,128, 128));
		pickableItemList.add (new PickableItem("tavara2", actionObject,512, 128));
		pickableItemList.add (new PickableItem("tavara3", actionObject,1024, 1024));
		pickableItemList.add (new PickableItem("tavara4", actionObject,2048, 1024));
		pickableItemList.add (new PickableItem("tavara5", actionObject, 1536, 1536));
		pickableItemList.add (new PickableItem("tavara6", actionObject,4096, 768));

		//pickableItem = new PickableItem("tavara3", actionObject, 256, 256);
	}

	@Override
	public void render () {

		//System.out.println("RENDER");

		batch.setProjectionMatrix(camera.combined);
		camera.update();

		//System.out.println(playerPosX);

		Gdx.gl.glClearColor(0.398f, 1, 1, 0);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );



		if (drawObjectEnabled) {
			drawTextures();
		}

		if (uiButtonsEnabled) {
			drawButtons();
		}

		if (toast != null)
		{
			toast.render(Gdx.graphics.getDeltaTime());
		}

		if (playerAnimationRunning) {
			playerAnimationStateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		}

		playerAnimationTime += Gdx.graphics.getDeltaTime();

		if (playerAnimationTime > 0.8f) {
			playerAnimationRunning = false;
			playerAnimationStateTime = 0.025f;
		}

	}

	private void drawTextures() {
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

		/*if (actionPointsEnabled) {
			for (int i = 0; i < actionCoordinatesX.size(); i++) {
				batch.draw(actionObject, actionCoordinatesX.get(i), actionCoordinatesY.get(i));
			}
		}*/

		for (int i = 0; i < pickableItemList.size(); i++) {
			batch.draw(pickableItemList.get(i).getItemTexture(), pickableItemList.get(i).getItemCoordinateX(), pickableItemList.get(i).getItemCoordinateY());
		}


		//batch.draw(pickableItem.getItemTexture(), pickableItem.getItemCoordinateX(), pickableItem.getItemCoordinateY());



		batch.end();

		TextureRegion currentFrame = animation.getKeyFrame(playerAnimationStateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, playerPosX - 50, playerPosY - 40);
		spriteBatch.end();
	}


	private void drawButtons() {
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

	private void moveLeft(int pixels) {
		//playerPosX -= pixels;
		camera.translate(-pixels, 0, 0);
		System.out.println("MOVE LEFT");
	}

	private void moveRight(int pixels) {
		//playerPosX += pixels;
		camera.translate(+pixels, 0, 0);
		System.out.println("MOVE RIGHT");
	}

	private void moveUp(int pixels) {
		//playerPosY += pixels;
		camera.translate(0, +pixels);
		System.out.println("MOVE UP");
	}

	private void moveDown(int pixels) {
		//playerPosY -= pixels;
		camera.translate(0, -pixels);
		System.out.println("MOVE DOWN");
	}

	private void playPlayerAnimation() {

		if (!playerAnimationRunning) {
		}

		playerAnimationRunning = true;
		playerAnimationTime = 0;
	}

	private void checkAction() 		//tarkista onko pelaajan lahella toimintoja
	{
		System.out.println("CHECK ACTION! x = " + Float.toString(camera.position.x) +"  y = "+ Float.toString(camera.position.y));

		int distanceToObject = 128;

		for (int i = 0; i < pickableItemList.size(); i++) {
			if (camera.position.x - pickableItemList.get(i).getItemCoordinateX() <= distanceToObject && camera.position.x - pickableItemList.get(i).getItemCoordinateX() >= -distanceToObject
					&& camera.position.y - pickableItemList.get(i).getItemCoordinateY() <= distanceToObject && camera.position.y - pickableItemList.get(i).getItemCoordinateY() >= -distanceToObject)
			{
				System.out.println("NEAR POINT:  " + Integer.toString(i));
				pickUpItem(i);
			}
		}
	}

	private void pickUpItem(int i) {
		System.out.println("PICKUP " + Integer.toString(i));
		showToast("PICKED UP: " + pickableItemList.get(i).getItemName());
		inventoryList.add(pickableItemList.get(i).getItemName());
		pickableItemList.remove(i);
	}

	private void showInventory() {
		for (int i = 0; i < inventoryList.size(); i++)
		{
			showToast("YOU HAVE: " + inventoryList.get(i) + " , ");
			System.out.println(inventoryList.get(i));
		}

	}


	private void showToast(String text) {
		font = new BitmapFont();
		font.getData().setScale(3);
		Toast.ToastFactory toastFactory = new Toast.ToastFactory.Builder().font(font).build();
		toast = toastFactory.create(text, Toast.Length.SHORT);
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

		if (uiButtonsEnabled) {
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

