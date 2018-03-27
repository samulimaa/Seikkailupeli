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
	private int currentLevel = 1;
	private int randomMaxItems;
	private int randomItemsOnMap = 0;

	private float playerAnimationSpeed = 0.025f;
	private float playerAnimationStateTime;
	private float playerAnimationTime;
	private float randomSpawnTime;
	private float randomSpawnInterval;

	private boolean playerAnimationRunning = false;

	private boolean drawObjectEnabled = true;
	private boolean tilemapEnabled = true;
	private boolean uiButtonsEnabled = true;
	private boolean movementEnabled = true;
	private boolean drawPickableItems = true;
	private boolean drawInventory = false;
	private boolean enableRandomSpawns;

	private Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	private SpriteBatch spriteBatch;
	private SpriteBatch batch;

	private BitmapFont font;
	private Toast toast;

	private Texture button;
	private Texture actionButton;
	private Texture animationSheet;
	private Texture greenObject;
	private Texture item1;
	private Texture item2;
	private Texture randomItem1;
	private Texture inventoryBackground;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;

	private OrthographicCamera camera;

	private List<PickableItem> pickableItemList = new ArrayList<PickableItem>();
	//private List<RandomSpawnPickableItem> randomSpawnPickableItemList = new ArrayList<RandomSpawnPickableItem>();
	private List<Inventory> inventory = new ArrayList<Inventory>();

	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		System.out.println("CREATE");

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		playerPosX = w / 2 + 30;
		playerPosY = h / 2 - 30;

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
		item1 = new Texture(Gdx.files.internal("item1.jpg"));
		item2 = new Texture(Gdx.files.internal("item2.jpg"));
		randomItem1 = new Texture(Gdx.files.internal("randomItem1.jpg"));

		inventoryBackground = new Texture(Gdx.files.internal("inventory.jpg"));

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


		if (currentLevel == 1) {
			pickableItemList.add (new PickableItem("tavara1", item1,128, 128));
			pickableItemList.add (new PickableItem("tavara2", item1,512, 128));
			pickableItemList.add (new PickableItem("tavara3", item1,1024, 1024));
			pickableItemList.add (new PickableItem("tavara4", item1,2048, 1024));
			pickableItemList.add (new PickableItem("tavara5", item1, 1536, 1536));
			pickableItemList.add (new PickableItem("tavara6", item1,4096, 768));
			pickableItemList.add (new PickableItem("tavara7", item1, 1664, 1664));
			pickableItemList.add (new PickableItem("tavara8", item1,4096, 768));

			pickableItemList.add(new PickableItem("avain1", item2, 800, 1792));
			pickableItemList.add(new PickableItem("avain2", item2, 2048, 1792));
			pickableItemList.add(new PickableItem("avain3", item2, 1536, 128));
			pickableItemList.add(new PickableItem("avain4", item2, 1280, 1280));
			pickableItemList.add(new PickableItem("avain5", item2, 640, 640));

			enableRandomSpawns = true;
			randomMaxItems = 20;
			randomSpawnInterval = 2;
		}

	}

	@Override
	public void render () {

		//System.out.println("RENDER");

		batch.setProjectionMatrix(camera.combined);
		camera.update();

		Gdx.gl.glClearColor(0.398f, 1, 1, 0);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

		if (drawObjectEnabled) {
			drawTextures();
		}

		if (uiButtonsEnabled) {
			drawButtons();
		}

		if (drawInventory) {
			drawInventory();
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

		if (enableRandomSpawns) {
			if (randomMaxItems > randomItemsOnMap) {
				randomSpawnTime += Gdx.graphics.getDeltaTime();
					if (randomSpawnTime >= randomSpawnInterval) {
						pickableItemList.add(new RandomSpawnPickableItem("random", randomItem1));
						randomItemsOnMap++;
						randomSpawnTime = 0;
					}
			}
		}
	}

	private void drawTextures() {
		if (tilemapEnabled) {
			tiledMapRenderer.setView(camera);
			tiledMapRenderer.render();
		}

		batch.begin();

		for (int i = 0; i <= 5; i++) {
			batch.draw(greenObject, -300 + i * 200, -300);
			batch.draw(greenObject, -300, -300 + i * 200);
		}


		if (drawPickableItems) {
			for (int i = 0; i < pickableItemList.size(); i++) {
				batch.draw(pickableItemList.get(i).getItemTexture(), pickableItemList.get(i).getItemCoordinateX(), pickableItemList.get(i).getItemCoordinateY());
			}
		}

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

	private void drawInventory() {

		batch.begin();

		//System.out.println("DRAW INVENTORY()");

		float inventoryCoordinateX = camera.position.x - 550;
		float inventoryCoordinateY = camera.position.y - 200;

		
		batch.draw(inventoryBackground, inventoryCoordinateX, inventoryCoordinateY);

		Inventory.goFirstRow();

		Inventory.setItemRowNumber(0);
		for (int i = 0; i < inventory.size(); i++) {
			if (Inventory.getItemRowNumber() >= Inventory.getItemsPerRow()) {
				Inventory.goNextRow();
				Inventory.setItemRowNumber(0);
			}
			//System.out.println(inventory.size());
			batch.draw(inventory.get(i).getTexture(), inventoryCoordinateX + 50 + Inventory.getDistanceBetweenObjectsX() * Inventory.getItemRowNumber(),
					inventoryCoordinateY + inventoryBackground.getHeight() - Inventory.getDistanceBetweenObjectsY() - Inventory.getDistanceBetweenObjectsY()*Inventory.getRow());

			if (!inventory.get(i).isPositionSaved()) {
				inventory.get(i).setDrawnPosX(440 + Inventory.getDistanceBetweenObjectsX() * Inventory.getItemRowNumber()+1 );
				inventory.get(i).setDrawnPosY(Inventory.getDistanceBetweenObjectsY() * (Inventory.getRow()+1) + 165);
			inventory.get(i).setPositionSaved();
			}

			Inventory.nextItemRowNumber();
		}

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

		int distanceToObject = 100;

		showInventory();

		for (int i = 0; i < pickableItemList.size(); i++) {
			if (camera.position.x - pickableItemList.get(i).getItemCoordinateX() <= distanceToObject && camera.position.x - pickableItemList.get(i).getItemCoordinateX() >= -distanceToObject
					&& camera.position.y - pickableItemList.get(i).getItemCoordinateY() <= distanceToObject + 50 && camera.position.y - pickableItemList.get(i).getItemCoordinateY() >= -distanceToObject)
			{
				System.out.println("NEAR POINT:  " + Integer.toString(i));
				pickUpItem(i);
			}
		}
	}

	private void pickUpItem(int i) {
		if (inventory.size() >= Inventory.getMaxRows() * Inventory.getItemsPerRow()) {
			Inventory.setFull(true);
		} else {
			Inventory.setFull(false);
		}

		if (!Inventory.checkIsFull()) {
			System.out.println("PICKUP " + Integer.toString(i));
			showToast("PICKED UP: " + pickableItemList.get(i).getItemName());
			inventory.add(new Inventory(pickableItemList.get(i).getItemName(), pickableItemList.get(i).getItemTexture()));
			if (pickableItemList.get(i).getItemName().equals("random")) {
				randomItemsOnMap--;
			}
			pickableItemList.remove(i);
		} else {
			showToast("INVENTORY IS FULL!");
		}
	}

	private void showInventory() {

		List<String> items = new ArrayList<String>();
		for (int i = 0; i < inventory.size(); i++)
		{
			items.add(inventory.get(i).getItemName());
		}

		for (int i = 0; i < items.size(); i++) {
			System.out.println(items.get(i));

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

		if (movementEnabled && uiButtonsEnabled) { //liikkuminen
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

		if (uiButtonsEnabled) { //toimintanappi
			if (screenX > 1670 && screenX < 1910 && screenY > 850 && screenY < 1050) {
				checkAction();
            }
		}

		if (uiButtonsEnabled && !drawInventory) { //inventory on
			if (screenY < 200) {
				drawInventory = true;
			}
		}

		if (uiButtonsEnabled && drawInventory){ //inventory off
			if (screenY > 800 || screenX < 300 || screenX > 1600 ) {
				drawInventory = false;
			}
		}

		if (drawInventory) { //tavaran valinta inventorysta
			for (int i = 0; i < inventory.size(); i++) {
				//System.out.println(Float.toString(inventory.get(i).getDrawnPosX()));
				//System.out.println(Float.toString(inventory.get(i).getDrawnPosX() + inventory.get(i).getTexture().getWidth()));
				if (screenX > inventory.get(i).getDrawnPosX() && screenX < inventory.get(i).getDrawnPosX() + Inventory.getDistanceBetweenObjectsX() - 30
				&& screenY > inventory.get(i).getDrawnPosY() && screenY < inventory.get(i).getDrawnPosY() + Inventory.getDistanceBetweenObjectsY() - 30) {
					showToast(inventory.get(i).getItemName());
					System.out.println("ITEM HERE");
				}
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

