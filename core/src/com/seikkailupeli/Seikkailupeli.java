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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.seikkailupeli.States.GameStateManager;
import com.seikkailupeli.States.MenuState;
import com.seikkailupeli.States.State;
import com.seikkailupeli.sprites.Player;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Seikkailupeli extends State implements InputProcessor{

	// Constant rows and columns of the player sprite sheet
	private static final int FRAME_COLS = 6, FRAME_ROWS = 5;

	private int playerSpawnPosX = 1536;
	private int playerSpawnPosY = 1024;
	private int playerPosX;
	private int playerPosY;
	private int playerSpeed = 8;
	private int currentLevel = 1;
	private int randomMaxItems;
	private int randomItemsOnMap = 0;
	private int currentPuzzleId;

	private float playerAnimationSpeed = 0.025f;
	private float playerAnimationStateTime;
	private float playerAnimationTime;
	private float randomSpawnTime;
	private float randomSpawnInterval;
	private float dialogTime = 0;

	private boolean playerAnimationRunning = false;

	private boolean drawObjectEnabled = true;
	private boolean tilemapEnabled = true;
	private boolean uiButtonsEnabled = true;
	private boolean movementEnabled = true;
	private boolean drawPickableItems = true;
	private boolean drawInventory = false;
	private boolean drawCharacters = true;
	private boolean drawPuzzleObjects = true;
	private boolean enableRandomSpawns;
	private boolean inputControlsEnabled = true;
	private boolean actionButtonDown;
	private boolean characterMovementEnabled = true;
	private boolean puzzleRunning = false;
	private boolean dialogEnabled = false;

	private Animation<TextureRegion> animation; // Must declare frame type (TextureRegion)
	private SpriteBatch spriteBatch;
	private SpriteBatch batch;

	private BitmapFont font;
	private Toast toast;

	private Touchpad touchpad;

	private Drawable touchBackground;
	private Drawable touchKnob;

	private Texture button;
	private Texture actionButtonTexture;
	private Texture animationSheet;
	private Texture greenObject;
	private Texture item1;
	private Texture item2;
	private Texture randomItem1;
	private Texture inventoryBackground;
	private Texture character1;
	private Texture settingsButtonTexture;
	private Texture puzzleBackgroundTexture;
	private Texture puzzleButtonTexture;
	private Texture puzzleObjectTexture;
	private Texture teleportTexture;
	private Texture dialogTexture;

	private ImageButton actionImageButton;
	private ImageButton inventoryImageButton;
	private ImageButton settingsImageButton;

	private Dialog dialog;

	private TiledMap tiledMap;
	private TiledMapRenderer tiledMapRenderer;
	private OrthogonalTiledMapRenderer tiledMapLayerRenderer;


	private OrthographicCamera camera;
	private Stage stage;

	private World world;
	private Player player;

	private List<PickableItem> pickableItemList = new ArrayList<PickableItem>();
	private List<Character> characterList = new ArrayList<Character>();
	private List<Inventory> inventory = new ArrayList<Inventory>();
	private List<Puzzle> puzzleList = new ArrayList<Puzzle>();

	private Box2DDebugRenderer b2dr;

	public Seikkailupeli(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void handleInput() {

	}

	@Override
	public void render(SpriteBatch sb) {

	}

	public void create () {

		Gdx.gl.glClearColor(0, 0, 0, 0);

		Gdx.input.setInputProcessor(this);

		world = new World(new Vector2(0,0),false);
		//b2dr = new Box2DDebugRenderer();

		player = new Player(playerSpawnPosX,playerSpawnPosY,world);

		System.out.println("CREATE");

		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();

		playerPosX = w / 2;
		playerPosY = h / 2 - 30;

		tiledMap = new TmxMapLoader().load("map2.tmx");

		tiledMapLayerRenderer = new OrthogonalTiledMapRenderer(tiledMap,4);

		TiledObjectUtil.parseTiledObjectLayer(world,tiledMap.getLayers().get("Collision").getObjects());

		System.out.println("CREATE 3");

		camera = new OrthographicCamera(w, h);
		camera.position.set(playerSpawnPosX, playerSpawnPosY, 0);
		camera.update();

		batch = new SpriteBatch();

		button = new Texture(Gdx.files.internal("button.jpg"));
		actionButtonTexture = new Texture(Gdx.files.internal("action_nappi.png"));
		Texture inventoryButtonTexture = new Texture(Gdx.files.internal("laukkuikoni.png"));
		settingsButtonTexture = new Texture(Gdx.files.internal("asetukset_ikoni.png"));
		puzzleObjectTexture = new Texture(Gdx.files.internal("puzzleObject.png"));
		puzzleButtonTexture = new Texture(Gdx.files.internal("puzzleButton.png"));


		System.out.println("CREATE 4");

		TextureRegion actionButtonTextureRegion = new TextureRegion(actionButtonTexture);
		TextureRegionDrawable actionButtonTextureRegionDrawable = new TextureRegionDrawable(actionButtonTextureRegion);
		actionImageButton = new ImageButton(actionButtonTextureRegionDrawable);

		TextureRegion inventoryButtonTextureRegion = new TextureRegion(inventoryButtonTexture);
		TextureRegionDrawable inventoryButtonTextureRegionDrawable = new TextureRegionDrawable(inventoryButtonTextureRegion);
		inventoryImageButton = new ImageButton(inventoryButtonTextureRegionDrawable);

		TextureRegion settingsButtonTextureRegion = new TextureRegion(settingsButtonTexture);
		TextureRegionDrawable settingsButtonTextureRegionDrawable = new TextureRegionDrawable(settingsButtonTextureRegion);
		settingsImageButton = new ImageButton(settingsButtonTextureRegionDrawable);

		dialogTexture = new Texture(Gdx.files.internal("puhekupla.png"));

		//Skin dialogSkin = new Skin();
		//dialogSkin.add("skin", dialogTexture);
		//dialog = new TextButton("TEKSTIA", dialogSkin);

		//dialog = new Drawable(Gdx.files.internal("puhekupla.png"));
		//dialog = new TextButton(dialogStyle);

		greenObject = new Texture(Gdx.files.internal("greenObject.jpg"));
		item1 = new Texture(Gdx.files.internal("item1.jpg"));
		item2 = new Texture(Gdx.files.internal("item2.jpg"));
		randomItem1 = new Texture(Gdx.files.internal("randomItem1.jpg"));

		character1 = new Texture(Gdx.files.internal("character1.png"));
		inventoryBackground = new Texture(Gdx.files.internal("inventory.jpg"));
		puzzleBackgroundTexture = new Texture(Gdx.files.internal("Puzzlebackground.png"));
		teleportTexture = new Texture(Gdx.files.internal("teleport.png"));

		
		spriteBatch = new SpriteBatch();
		playerAnimationStateTime = 0f;

		//animationSheet = new Texture(Gdx.files.internal("animation_sheet.png"));

		/*TextureRegion[][] tmp = TextureRegion.split(animationSheet,
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
		playerAnimationStateTime = 0f;*/

		System.out.println("CREATE 5");
		
		initializeInputControls();
		placeItemsAndCharacters();

		b2dr = new Box2DDebugRenderer();
	}

	private void initializeInputControls() {

		Skin touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", new Texture("ohjaustatti_tausta.png"));
		touchpadSkin.add("touchKnob", new Texture("ohjaustatti.png"));
		Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();

		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		touchpad = new Touchpad(10, touchpadStyle);
		touchpad.setBounds(20, 20, 350, 350);
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

		settingsImageButton.addListener(new ClickListener() { //asetukset nappi
			@Override
			public void clicked(InputEvent event, float x, float y) { System.out.println("SETTINGS");}
		});

		stage = new Stage();
		stage.addActor(touchpad);
		stage.addActor(actionImageButton);
		stage.addActor(inventoryImageButton);
		stage.addActor(settingsImageButton);
		//stage.addActor(dialog);

	}


	public void render () {

		if (!puzzleRunning) {

			Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

			update(Gdx.graphics.getDeltaTime());

			//System.out.println("RENDER");

			batch.setProjectionMatrix(camera.combined);
			camera.update();

			//Gdx.gl.glClearColor(0.398f, 1, 1, 0);

			if (drawObjectEnabled) {
				drawTextures(batch);
			}


			if (uiButtonsEnabled) {
				drawButtons();
			}


			if (drawInventory && !dialogEnabled) {
				drawInventory();
			} else {
				Gdx.input.setInputProcessor(stage);
			}

			if (toast != null)
			{
				toast.render(Gdx.graphics.getDeltaTime());
			}


			if (dialogEnabled) {
				drawDialog();
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

		if (puzzleRunning) {
			//camera.translate(puzzleList.get(currentPuzzleId).getItemCoordinateX(), puzzleList.get(currentPuzzleId).getItemCoordinateY());
			//camera.position.set(0, 0, 0);
			drawPuzzle();
		}

		if (!puzzleRunning) {
			b2dr.render(world,camera.combined);
		}

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

			//characterList.add(new Character("hahmo1", character1, 2944, 1152, 2, 0, 400 , "HELLO!"));
			//characterList.add(new Character("hahmo2", character1, 1792, 1280, 2, 200, 0, "HELLO HELLO HELLO!"));
			//characterList.add(new Character("hahmo3", character1, 3520, 1152, 0, 0, 0,"HEY HEY!"));
			characterList.add(new Character("hahmo4", character1, 256, 896, 2, 300, 0, 0, 5));
			characterList.add(new Character("hahmo5", character1, 1536, 1536, true, 0, 6));
			characterList.add(new Character("hahmo6", character1, 1408, 1152, true, 0, 3));
			
			puzzleList.add(new Puzzle(puzzleObjectTexture, puzzleBackgroundTexture, 1500, 1200, "KYSYMYS", new int[] {0,0,0,0}, new int[] {1,1,1,1},
					teleportTexture, 1800, 1200, 2800, 1200));
			//puzzleList.add(new Puzzle(puzzleObjectTexture, puzzleBackgroundTexture, 2500, 800 , "KYSYMYS", new int[] {0,0,0,0}, new int[] {1,2,3,4}));
		}
	}

		private void drawTextures(SpriteBatch batch) {
		tiledMapLayerRenderer.getBatch().begin();

		tiledMapLayerRenderer.setView(camera);
		tiledMapLayerRenderer.renderTileLayer((TiledMapTileLayer) tiledMap.getLayers().get("Backround"));

		//batch.begin();

		for (int i = 0; i <= 5; i++) {
			tiledMapLayerRenderer.getBatch().draw(greenObject, -300 + i * 200, -300);
			tiledMapLayerRenderer.getBatch().draw(greenObject, -300, -300 + i * 200);
		}


		if (drawPickableItems) {
			for (int i = 0; i < pickableItemList.size(); i++) {
				tiledMapLayerRenderer.getBatch().draw(pickableItemList.get(i).getItemTexture(), pickableItemList.get(i).getItemCoordinateX(), pickableItemList.get(i).getItemCoordinateY());
			}
		}

		if (drawPuzzleObjects) {
			for (int i = 0; i < puzzleList.size(); i++) {
				if (!puzzleList.get(i).checkIsCorrect()) {
					tiledMapLayerRenderer.getBatch().draw(puzzleList.get(i).getItemTexture(), puzzleList.get(i).getItemCoordinateX(), puzzleList.get(i).getItemCoordinateY());
				}
				tiledMapLayerRenderer.getBatch().draw(puzzleList.get(i).getTeleportAreaTexture(), puzzleList.get(i).getTeleport1Coordinates()[0], puzzleList.get(i).getTeleport1Coordinates()[1]);
				tiledMapLayerRenderer.getBatch().draw(puzzleList.get(i).getTeleportAreaTexture(), puzzleList.get(i).getTeleport2Coordinates()[0], puzzleList.get(i).getTeleport2Coordinates()[1]);
			}
		}


		if (drawCharacters) {
			for (int i = 0; i < characterList.size(); i++) {
				if (characterList.get(i).isMovementEnabled()) {
					characterList.get(i).moveCharacter();
				}
				tiledMapLayerRenderer.getBatch().draw(characterList.get(i).getCharacterTexture(), characterList.get(i).getCharacterCoordinateX(), characterList.get(i).getCharacterCoordinateY());
			}
		}

		tiledMapLayerRenderer.getBatch().draw(player.getPlayerTexture(Gdx.graphics.getDeltaTime()),player.getPosition().x-40,player.getPosition().y-30,Gdx.graphics.getWidth()/8/3,Gdx.graphics.getHeight()/8);

		tiledMapLayerRenderer.renderTileLayer((TiledMapTileLayer) tiledMap.getLayers().get("Fog"));

		tiledMapLayerRenderer.getBatch().end();

		//batch.end();

		/*TextureRegion currentFrame = animation.getKeyFrame(playerAnimationStateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, playerPosX - 50, playerPosY - 40);
		spriteBatch.end();*/
	}

	public void update(float dt) {
		world.step(1/60f,6,2);
		Vector3 position = camera.position;
		position.x = player.b2body.getPosition().x;
		position.y = player.b2body.getPosition().y;

		player.position = new Vector2(player.b2body.getPosition().x, player.b2body.getPosition().y);
		player.update(dt);

		camera.position.set(position);
		camera.update();
	}


	private void drawButtons() {

		batch.begin();

		touchpad.setPosition(70, 70);
		actionImageButton.setPosition(1610, 70);
		inventoryImageButton.setPosition(1620,800);
		settingsImageButton.setPosition(40, 880);
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

	private void drawPuzzle() {

		int[] correctValue = puzzleList.get(currentPuzzleId).getCorrectValue();
		int[] currentValue = puzzleList.get(currentPuzzleId).getCurrentValue();

		int backgroundOffsetX = puzzleList.get(currentPuzzleId).getBackgroundOffsetX();
		int backgroundOffsetY = puzzleList.get(currentPuzzleId).getBackgroundOffsetY();
		int questionOffsetX = puzzleList.get(currentPuzzleId).getQuestionOffsetX();
		int questionOffsetY = puzzleList.get(currentPuzzleId).getQuestionOffsetY();
		int numbersOffsetX = puzzleList.get(currentPuzzleId).getNumbersOffsetX();
		int numbersOffsetY = puzzleList.get(currentPuzzleId).getNumbersOffsetY();
		int distanceToNextNumber = puzzleList.get(currentPuzzleId).getDistanceToNextButton();

		Gdx.input.setInputProcessor(this);

		//camera.position.set(puzzleList.get(currentPuzzleId).getItemCoordinateX(), puzzleList.get(currentPuzzleId).getItemCoordinateY(), 0);

		Gdx.gl.glClearColor(1, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//System.out.println("PUZZLE " + Integer.toString(currentPuzzleId));

		batch.begin();

		batch.draw(puzzleList.get(currentPuzzleId).getBackgroundTexture(), puzzleList.get(currentPuzzleId).getItemCoordinateX() + backgroundOffsetX,
				puzzleList.get(currentPuzzleId).getItemCoordinateY() + backgroundOffsetY);

		puzzleList.get(currentPuzzleId).getQuestionFont().draw(batch, puzzleList.get(currentPuzzleId).getQuestion(),
				puzzleList.get(currentPuzzleId).getItemCoordinateX() + questionOffsetX, puzzleList.get(currentPuzzleId).getItemCoordinateY() + questionOffsetY);


		for (int i = 0; i < 4; i++) {
			puzzleList.get(currentPuzzleId).getNumbersFont().draw(batch, Integer.toString(currentValue[i]), (puzzleList.get(currentPuzzleId).getItemCoordinateX() + numbersOffsetX) + i * distanceToNextNumber,
					puzzleList.get(currentPuzzleId).getItemCoordinateY() + numbersOffsetY);
		}

		int firstX = numbersOffsetX + 10;
		for (int i = 0; i < 4; i++) { //ylhaalla olevat napit
			batch.draw(puzzleButtonTexture, puzzleList.get(currentPuzzleId).getItemCoordinateX() + firstX, puzzleList.get(currentPuzzleId).getItemCoordinateY() + 30);
			firstX += distanceToNextNumber;
		}

		firstX = numbersOffsetX + 20;
		for (int i = 0; i < 4; i++) { //alhaalla olevat napit
			batch.draw(puzzleButtonTexture, puzzleList.get(currentPuzzleId).getItemCoordinateX() + firstX, puzzleList.get(currentPuzzleId).getItemCoordinateY() - 290);
			firstX += distanceToNextNumber;
		}

		//paluu nappi
		batch.draw(button, puzzleList.get(currentPuzzleId).getItemCoordinateX() - 850, puzzleList.get(currentPuzzleId).getItemCoordinateY() - 380);

		batch.end();

		if (puzzleList.get(currentPuzzleId).checkIsCorrect()) {
			puzzleList.get(currentPuzzleId).setValueToOriginal();
			puzzleRunning = false;
			Gdx.input.setInputProcessor(stage);
			showToast("PUZZLE COMPLETED!", 3, Toast.Length.SHORT);
		}
	}

	private void drawDialog() {

		float dialogMaxTime = dialog.getDialogMaxTime();

		inputControlsEnabled = false;
		uiButtonsEnabled = false;
		drawPickableItems = false;
		drawPuzzleObjects = false;
		characterList.get(dialog.getCharacter_id()).pauseMovement();

		if (dialogTime > dialogMaxTime) {
			if (dialog.getDialog_id() >= dialog.getDialogEnd()) {
				dialogEnabled = false;
				dialogTime = 0;
				characterList.get(dialog.getCharacter_id()).resumeMovement();
				inputControlsEnabled = true;
				uiButtonsEnabled = true;
				drawPickableItems = true;
				drawPuzzleObjects = true;
			} else {
				dialog.goNextDialog();
				dialog.switchSpeaker();
				dialogTime = 0;
			}
		}


		if (dialog.getCurrentDialog() == null) {
			System.out.println("DIALOG NULL!");
			dialogEnabled = false;
		}

		BitmapFont dialogFont = new BitmapFont();
		dialogFont.getData().setScale(3);

		//batch.begin();
		tiledMapLayerRenderer.getBatch().begin();


		if (dialog.isPlayerSpeaking()) {
			tiledMapLayerRenderer.getBatch().draw(dialogTexture, camera.position.x - 945, camera.position.y - 300);
			dialogFont.draw(tiledMapLayerRenderer.getBatch(), dialog.getCurrentDialog(), camera.position.x - 830, camera.position.y - 100);
		} else {
			tiledMapLayerRenderer.getBatch().draw(dialogTexture, characterList.get(dialog.getCharacter_id()).getCharacterCoordinateX() - 900, characterList.get(dialog.getCharacter_id()).getCharacterCoordinateY() - 300);
			dialogFont.draw(tiledMapLayerRenderer.getBatch(), dialog.getCurrentDialog(), characterList.get(dialog.getCharacter_id()).getCharacterCoordinateX() - 830, characterList.get(dialog.getCharacter_id()).getCharacterCoordinateY() - 100);
		}
		//batch.end();
		tiledMapLayerRenderer.getBatch().end();


		dialogTime += Gdx.graphics.getDeltaTime();
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
			player.b2body.setLinearVelocity(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
			player.b2body.setTransform((player.b2body.getLinearVelocity().x * playerSpeed) + player.b2body.getPosition().x, (player.b2body.getLinearVelocity().y * playerSpeed) + player.b2body.getPosition().y, 0);
				if (touchpad.getKnobPercentX() != 0 && touchpad.getKnobPercentY() != 0) {
				//playPlayerAnimation();
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
		if (inputControlsEnabled) {

			System.out.println("CHECK ACTION! x = " + Float.toString(camera.position.x) + "  y = " + Float.toString(camera.position.y));

			int maxDistanceToObject = 90;
			int maxDistanceToCharacter = 100;
			int maxDistanceToPuzzle = 90;
			int maxDistanceToTeleport = 140;

			showInventory();

			for (int i = 0; i < pickableItemList.size(); i++) { //tarkista onko tavaroita lahella
				if (camera.position.x - pickableItemList.get(i).getItemCoordinateX() <= maxDistanceToObject && camera.position.x - pickableItemList.get(i).getItemCoordinateX() >= -maxDistanceToObject
						&& camera.position.y - pickableItemList.get(i).getItemCoordinateY() <= maxDistanceToObject + 50 && camera.position.y - pickableItemList.get(i).getItemCoordinateY() >= -maxDistanceToObject) {
					System.out.println("NEAR POINT:  " + Integer.toString(i));
					pickUpItem(i);
					break;
				}
			}

			for (int i = 0; i < puzzleList.size(); i++) { //tarkista onko puzzleja lahella
				if (!puzzleList.get(i).checkIsCorrect()) {
					if (camera.position.x - puzzleList.get(i).getItemCoordinateX() <= maxDistanceToPuzzle && camera.position.x - puzzleList.get(i).getItemCoordinateX()  >= -maxDistanceToPuzzle
							&& camera.position.y - puzzleList.get(i).getItemCoordinateY()  <= maxDistanceToPuzzle && camera.position.y - puzzleList.get(i).getItemCoordinateY()  >= -maxDistanceToPuzzle) {
						System.out.println("NEAR PUZZLE:  " + Integer.toString(i));
						showPuzzle(i);
						break;
					}
				}
			}

			for (int i = 0; i < puzzleList.size(); i++) { //tarkista onko puzzlejen teleportteja lahella
				if (camera.position.x - puzzleList.get(i).getTeleport1Coordinates()[0] <= maxDistanceToTeleport + 80 && camera.position.x - puzzleList.get(i).getTeleport1Coordinates()[0]  >= -maxDistanceToTeleport + 80
						&& camera.position.y - puzzleList.get(i).getTeleport1Coordinates()[1] <= maxDistanceToTeleport + 120 && camera.position.y - puzzleList.get(i).getTeleport1Coordinates()[1] >= -maxDistanceToTeleport + 120) {
					System.out.println("NEAR TELEPORT1:  " + Integer.toString(i));
					if (puzzleList.get(i).checkIsCorrect()) {
						player.b2body.setTransform(puzzleList.get(i).getTeleport2Coordinates()[0] + teleportTexture.getWidth() / 2, puzzleList.get(i).getTeleport1Coordinates()[1] + teleportTexture.getHeight() / 2,
								player.b2body.getAngle());

					} else {
						showToast("YOU NEED TO SOLVE PUZZLE FIRST!",3, Toast.Length.SHORT);
					}
					break;
				}
				if (camera.position.x - puzzleList.get(i).getTeleport2Coordinates()[0] <= maxDistanceToTeleport + 80 && camera.position.x - puzzleList.get(i).getTeleport2Coordinates()[0] >= -maxDistanceToTeleport + 80
						&& camera.position.y - puzzleList.get(i).getTeleport2Coordinates()[1] <= maxDistanceToTeleport + 120 && camera.position.y - puzzleList.get(i).getTeleport2Coordinates()[1] >= -maxDistanceToTeleport + 120) {
					System.out.println("NEAR TELEPORT2:  " + Integer.toString(i));
					if (puzzleList.get(i).checkIsCorrect()) {
						player.b2body.setTransform(puzzleList.get(i).getTeleport1Coordinates()[0] + teleportTexture.getWidth() / 2, puzzleList.get(i).getTeleport1Coordinates()[1] + teleportTexture.getHeight() / 2,
								player.b2body.getAngle());
					} else {
						showToast("YOU NEED TO SOLVE PUZZLE FIRST!", 3, Toast.Length.SHORT);
					}
					break;
				}
			}

			for (int i = 0; i < characterList.size(); i++) { //tarkista onko hahmoja lahella
				if (camera.position.x - characterList.get(i).getCharacterCoordinateX() <= maxDistanceToCharacter && camera.position.x - characterList.get(i).getCharacterCoordinateX() >= -maxDistanceToCharacter
						&& camera.position.y - characterList.get(i).getCharacterCoordinateY() <= maxDistanceToCharacter + 50 && camera.position.y - characterList.get(i).getCharacterCoordinateY() >= -maxDistanceToCharacter) {
					if (characterList.get(i).isDialogsEnabled()) {
						//showToast(characterList.get(i).getCharacterDialog(), 3, Toast.Length.SHORT);
						dialogEnabled = true;
						dialog = new Dialog(i, characterList.get(i).getDialogStart(), characterList.get(i).getDialogEnd(), true, characterList.get(i).getCharacterCoordinateX(), characterList.get(i).getCharacterCoordinateY());
						//drawDialog(characterList.get(i).getDialogStart(), characterList.get(i).getDialogEnd(), characterList.get(i).getCharacterCoordinateX(), characterList.get(i).getCharacterCoordinateY());
					}
					break;
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
			showToast("PICKED UP: " + pickableItemList.get(i).getItemName(), 3, Toast.Length.SHORT);
			inventory.add(new Inventory(pickableItemList.get(i).getItemName(), pickableItemList.get(i).getItemTexture()));
			if (pickableItemList.get(i).getItemName().equals("random")) {
				randomItemsOnMap--;
			}
			pickableItemList.remove(i);
		} else {
			showToast("INVENTORY IS FULL!", 3,Toast.Length.SHORT);
		}
	}
	
	private void showPuzzle(int puzzleId) {

		currentPuzzleId = puzzleId;
		player.b2body.setTransform(puzzleList.get(currentPuzzleId).getItemCoordinateX() + 40, puzzleList.get(currentPuzzleId).getItemCoordinateY() + 40, 0);
		render();

		if (puzzleList.get(currentPuzzleId).checkIsCorrect()) {
			puzzleList.get(currentPuzzleId).setValueToOriginal();
		}

		puzzleRunning = true;

	}

	private void showInventory() {

		List<String> items = new ArrayList<String>();
		for (int i = 0; i < inventory.size(); i++)
		{
			items.add(inventory.get(i).getItemName());
		}

		for (int i = 0; i < items.size(); i++) {
			//System.out.println(items.get(i));

		}
	}

	private void showToast(String text, int size, Toast.Length time) {
		font = new BitmapFont();
		font.getData().setScale(size);
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
					showToast(inventory.get(i).getItemName(), 3, Toast.Length.SHORT);
					System.out.println("ITEM HERE");
				}
			}
		}

		if (puzzleRunning) { //puzzle napit

			int buttonPositionX = 450;
			int buttonPositionY = 450;
			int touchOffset = 50;
			int distanceToNextButton = puzzleList.get(currentPuzzleId).getDistanceToNextButton();
			int buttonSize = puzzleList.get(currentPuzzleId).getButtonSize();

			for (int i = 0; i < 8; i++) {
				if (screenX > buttonPositionX - touchOffset && screenX < buttonPositionX + buttonSize + touchOffset
						&& screenY > buttonPositionY - touchOffset && screenY < buttonPositionY + buttonSize + touchOffset) {
					System.out.println("NAPPI " + Integer.toString(i));
					puzzleList.get(currentPuzzleId).setCurrentValue(i);
					break;
				} else {
					buttonPositionX += distanceToNextButton;
					if (i == 3) {
						buttonPositionX = 500;
						buttonPositionY += 320;
					}
				}
			}

			if (screenX > 50 && screenX < 260 && screenY < 1040 && screenY > 840) {
				puzzleRunning = false;
				Gdx.input.setInputProcessor(stage);
			}
		}
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

