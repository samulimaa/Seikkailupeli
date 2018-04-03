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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;

public class Seikkailupeli extends ApplicationAdapter implements InputProcessor{

	// Constant rows and columns of the player sprite sheet
	private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

	private int playerSpawnPosX = 1536;
	private int playerSpawnPosY = 1024;
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
	private boolean drawCharacters = true;
	private boolean enableRandomSpawns;
	private boolean inputControlsEnabled = true;
	private boolean touchInput = true;

	private Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	private SpriteBatch spriteBatch;
	private SpriteBatch batch;

	private BitmapFont font;
	private Toast toast;

	private Touchpad touchpad;
	private Touchpad.TouchpadStyle touchpadStyle;

	private Skin touchpadSkin;
	private Drawable touchBackground;
	private Drawable touchKnob;

	private Texture button;
	private Texture actionButtonTexture;
	private Texture animationSheet;
	private Texture greenObject;
	private Texture item1;
	private Texture item2;
	private Texture randomItem1;
	private Texture inventoryButtonTexture;
	private Texture inventoryBackground;
	private Texture character1;

	private ImageButton actionImageButton;
	private ImageButton inventoryImageButton;
	private TextureRegion actionButtonTextureRegion;
	private TextureRegion inventoryButtonTextureRegion;
	private TextureRegionDrawable actionButtonTextureRegionDrawable;
	private TextureRegionDrawable inventoryButtonTextureRegionDrawable;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;

	private OrthographicCamera camera;
	private Stage stage;

	private Box2DDebugRenderer b2dr;
	private World world;
	private Body player;

	private List<PickableItem> pickableItemList = new ArrayList<PickableItem>();
	private List<Character> characterList = new ArrayList<Character>();
	private List<Inventory> inventory = new ArrayList<Inventory>();

	private boolean actionButtonDown;


	@Override
	public void create () {

		Gdx.input.setInputProcessor(this);

		world = new World(new Vector2(0,0),false);
		b2dr = new Box2DDebugRenderer();

		player = createBox(playerSpawnPosX,playerSpawnPosY,32*4,32*4,false);

		System.out.println("CREATE");

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		playerPosX = w / 2;
		playerPosY = h / 2 - 30;

		tiledMap = new TmxMapLoader().load("map2.tmx");

		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 4);

		TiledObjectUtil.parseTiledObjectLayer(world,tiledMap.getLayers().get(2).getObjects());

		System.out.println("CREATE 3");

		camera = new OrthographicCamera(w, h);
		camera.position.set(playerSpawnPosX, playerSpawnPosY, 0);
		camera.update();

		batch = new SpriteBatch();

		button = new Texture(Gdx.files.internal("button.jpg"));
		actionButtonTexture = new Texture(Gdx.files.internal("actionButton.jpg"));
		inventoryButtonTexture = new Texture(Gdx.files.internal("inventoryButton.png"));

		System.out.println("CREATE 4");

		actionButtonTextureRegion = new TextureRegion(actionButtonTexture);
		actionButtonTextureRegionDrawable = new TextureRegionDrawable(actionButtonTextureRegion);
		actionImageButton = new ImageButton(actionButtonTextureRegionDrawable);

		inventoryButtonTextureRegion = new TextureRegion(inventoryButtonTexture);
		inventoryButtonTextureRegionDrawable = new TextureRegionDrawable(inventoryButtonTextureRegion);
		inventoryImageButton = new ImageButton(inventoryButtonTextureRegionDrawable);

		greenObject = new Texture(Gdx.files.internal("greenObject.jpg"));
		item1 = new Texture(Gdx.files.internal("item1.jpg"));
		item2 = new Texture(Gdx.files.internal("item2.jpg"));
		randomItem1 = new Texture(Gdx.files.internal("randomItem1.jpg"));

		character1 = new Texture(Gdx.files.internal("character1.png"));

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

		initializeInputControls();

		placeItemsAndCharacters();

	}

	private void initializeInputControls() {

		touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
		touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
		touchpadStyle = new Touchpad.TouchpadStyle();

		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		touchpad = new Touchpad(10, touchpadStyle);
		touchpad.setBounds(20, 20, 300, 300);
		touchpad.setResetOnTouchUp(true);

		actionImageButton.addListener(new ClickListener() { //toimintonappi
			@Override
			public void clicked(InputEvent event, float x, float y) {
				checkAction();
			}
		});

		inventoryImageButton.addListener(new ClickListener() { //tavaraluettelo
			@Override
			public void clicked(InputEvent event, float x, float y) {
				drawInventory = true;
			}
		});

		stage = new Stage();
		stage.addActor(touchpad);
		stage.addActor(actionImageButton);
		stage.addActor(inventoryImageButton);

	}


	private void placeItemsAndCharacters() { //maaritellaan poimittavat tavarat yms


		if (currentLevel == 1) {
			pickableItemList.add (new PickableItem("tavara1", item1,128, 128));
			pickableItemList.add (new PickableItem("tavara2", item1,512, 128));
			pickableItemList.add (new PickableItem("tavara3", item1, 896, 896));
			pickableItemList.add (new PickableItem("tavara4", item1, 1664, 1024));
			pickableItemList.add (new PickableItem("tavara5", item1, 1536, 1536));
			pickableItemList.add (new PickableItem("tavara6", item1,4096, 768));
			pickableItemList.add (new PickableItem("tavara7", item1,1664, 1664));
			pickableItemList.add (new PickableItem("tavara8", item1,4096, 768));

			pickableItemList.add(new PickableItem("avain1", item2, 800, 1792));
			pickableItemList.add(new PickableItem("avain2", item2, 2048, 1792));
			pickableItemList.add(new PickableItem("avain3", item2, 1536, 128));
			pickableItemList.add(new PickableItem("avain4", item2, 1280, 1280));
			pickableItemList.add(new PickableItem("avain5", item2, 640, 640));

			enableRandomSpawns = true;
			randomMaxItems = 20;
			randomSpawnInterval = 2;

			characterList.add(new Character("hahmo1", character1, 1792, 1152, "HELLO!"));
			characterList.add(new Character("hahmo2", character1, 1792, 256, "HELLO HELLO HELLO!"));
			characterList.add(new Character("hahmo3", character1, 3520, 1152, "HEY HEY!"));
			characterList.add(new Character("hahmo4", character1, 1024, 1152));

		}
	}

	@Override
	public void render () {
		update();

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
		} else {
			Gdx.input.setInputProcessor(stage);
		}

		if (toast != null)
		{
			toast.render(Gdx.graphics.getDeltaTime());
		}

		if (playerAnimationRunning) {
			playerAnimationStateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		}

		playerAnimationTime += Gdx.graphics.getDeltaTime();

		if (playerAnimationTime > 0.2f) {
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

		if (inputControlsEnabled) {
			handleTouchpad();
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

		if (drawCharacters) {
			for (int i = 0; i < characterList.size(); i++) {
				batch.draw(characterList.get(i).getCharacterTexture(), characterList.get(i).getCharacterCoordinateX(), characterList.get(i).getCharacterCoordinateY());
			}
		}

		batch.end();

		TextureRegion currentFrame = animation.getKeyFrame(playerAnimationStateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, playerPosX - 50, playerPosY - 40);
		spriteBatch.end();
	}


	private void update() {
		world.step(1/60f,6,2);
		Vector3 position = camera.position;
		position.x = player.getPosition().x;
		position.y = player.getPosition().y;
		camera.position.set(position);
		camera.update();
	}


	private void drawButtons() {

		batch.begin();

		touchpad.setPosition(50, 50 );
		actionImageButton.setPosition(1650, 50);
		inventoryImageButton.setPosition(0,780);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

		batch.end();
	}

	private void drawInventory() {

		Gdx.input.setInputProcessor(this);

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

	public Body createBox(int x, int y, int width, int height, boolean isStatic){

		Body pBody;
		BodyDef def = new BodyDef();

		if(isStatic)
			def.type = BodyDef.BodyType.StaticBody;
		else
			def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x,y);
		def.fixedRotation = true;
		pBody = world.createBody(def);

		CircleShape shape = new CircleShape();
		shape.setRadius(width / 2);

		pBody.createFixture(shape,1f);
		shape.dispose();

		return pBody;
	}

	@Override
	public void dispose () {
		batch.dispose();
		button.dispose();
		spriteBatch.dispose();
		greenObject.dispose();
		actionButtonTexture.dispose();
		animationSheet.dispose();
		tiledMap.dispose();
	}

	
	private void handleTouchpad() {
		if (inputControlsEnabled) {

			//Vector2 velocity = player.getLinearVelocity();
			player.setLinearVelocity(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
			player.setTransform((player.getLinearVelocity().x * 7) + player.getPosition().x, (player.getLinearVelocity().y * 7) + player.getPosition().y, 0);
			if (touchpad.getKnobPercentX() != 0 && touchpad.getKnobPercentY() != 0) {
				playPlayerAnimation();
				drawInventory = false;
			}
		}
		
	}

	private void playPlayerAnimation() {

		if (!playerAnimationRunning) {
		}

		playerAnimationRunning = true;
		playerAnimationTime = 0;
	}

	private void checkAction() 		//tarkista onko pelaajan lahella toimintoja
	{
		System.out.println("CHECK ACTION! x = " + Float.toString(camera.position.x) + "  y = " + Float.toString(camera.position.y));

		int maxDistanceToObject = 90;
		int maxDistanceToCharacter = 160;

		showInventory();

		for (int i = 0; i < pickableItemList.size(); i++) { //tarkista onko tavaroita lahella
			if (camera.position.x - pickableItemList.get(i).getItemCoordinateX() <= maxDistanceToObject && camera.position.x - pickableItemList.get(i).getItemCoordinateX() >= -maxDistanceToObject
					&& camera.position.y - pickableItemList.get(i).getItemCoordinateY() <= maxDistanceToObject + 50 && camera.position.y - pickableItemList.get(i).getItemCoordinateY() >= -maxDistanceToObject) {
				System.out.println("NEAR POINT:  " + Integer.toString(i));
				pickUpItem(i);
			}
		}

		for (int i = 0; i < characterList.size(); i++) { //tarkista onko hahmoja lahella
			if (camera.position.x - characterList.get(i).getCharacterCoordinateX() <= maxDistanceToCharacter && camera.position.x - characterList.get(i).getCharacterCoordinateX() >= -maxDistanceToCharacter
					&& camera.position.y - characterList.get(i).getCharacterCoordinateY() <= maxDistanceToCharacter + 50 && camera.position.y - characterList.get(i).getCharacterCoordinateY() >= -maxDistanceToCharacter) {
				if (characterList.get(i).getCharacterDialog() != null) {
					showToast(characterList.get(i).getCharacterDialog(), Toast.Length.SHORT);
				}
			}
		}
	}
	private void pickUpItem(int i) { //tavaroiden poiminta maasta
		if (inventory.size() >= Inventory.getMaxRows() * Inventory.getItemsPerRow()) {
			Inventory.setFull(true);
		} else {
			Inventory.setFull(false);
		}

		if (!Inventory.checkIsFull()) {
			System.out.println("PICKUP " + Integer.toString(i));
			showToast("PICKED UP: " + pickableItemList.get(i).getItemName(), Toast.Length.SHORT);
			inventory.add(new Inventory(pickableItemList.get(i).getItemName(), pickableItemList.get(i).getItemTexture()));
			if (pickableItemList.get(i).getItemName().equals("random")) {
				randomItemsOnMap--;
			}
			pickableItemList.remove(i);
		} else {
			showToast("INVENTORY IS FULL!", Toast.Length.SHORT);
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


	private void showToast(String text, Toast.Length time) {
		font = new BitmapFont();
		font.getData().setScale(3);
		Toast.ToastFactory toastFactory = new Toast.ToastFactory.Builder().font(font).build();
		toast = toastFactory.create(text, time);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("TOUCHDOWN! " + "X: " + String.valueOf(screenX) + "  Y:  " + String.valueOf(screenY));
		

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
					showToast(inventory.get(i).getItemName(), Toast.Length.SHORT);
					System.out.println("ITEM HERE");
				}
			}
		}
		
		/*if (movementEnabled && !uiButtonsEnabled) { //ruudun reunoilta liikkuminen (ei kayteta)

			if (screenX < 300) {
				//moveLeft(moveAmount);
				playPlayerAnimation();
			}

			if (screenX > 1600) {
				//moveRight(moveAmount);
				playPlayerAnimation();
			}

			if (screenY < 200) {
				//moveUp(moveAmount);
				playPlayerAnimation();
			}

			if (screenY > 900) {
				//moveDown(moveAmount);
				playPlayerAnimation();
			}
		}*/

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		if (!actionButtonDown) {
		}

		if (actionButtonDown) {
			actionButtonDown = false;
		}
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

