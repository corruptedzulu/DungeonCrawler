package dcfinal;

import dcfinal.assets.*;

import myGameEngine.Camera3PController;
import myGameEngine.MoveBackAction;
import myGameEngine.MoveForwardAction;
import myGameEngine.MoveLeftAction;
import myGameEngine.MoveRightAction;
import myGameEngine.MyDisplaySystem;
import myGameEngine.YawLeftAction;
import myGameEngine.YawRightAction;

import net.java.games.input.Component;
import net.java.games.input.Event;
import sage.app.BaseGame;
import sage.audio.*;
import sage.display.*;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.physics.IPhysicsEngine;
import sage.physics.PhysicsEngineFactory;
import sage.scene.*;
import sage.scene.shape.*;
import sage.input.*;
import sage.input.action.*;
import sage.renderer.IRenderer;
import sage.camera.*;
import sage.scene.state.BlendState;
import sage.scene.state.TextureState;
import sage.terrain.TerrainBlock;
import sage.terrain.AbstractHeightMap;
import sage.terrain.HillHeightMap;
import sage.texture.Texture;
import sage.texture.TextureManager;
import sage.scene.state.RenderState;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Quaternion;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


import javax.script.*;

public class DungeonCrawler3D extends BaseGame
{
    private final double TILE_SIZE = 2.0;
    private final double TILE_HALF_SIZE = TILE_SIZE * .5f;
    private final Point3D TILE_SCALE = new Point3D(1.32, 1.32, 1.32); // 1.32

    private final Quaternion ROT_E = new Quaternion(); // default model orientation
    private final Quaternion ROT_NE = new Quaternion(1, new double[]{0, 45, 0});
    private final Quaternion ROT_N = new Quaternion(1, new double[]{0, 90, 0});
    private final Quaternion ROT_NW = new Quaternion(1, new double[]{0, 135, 0});
    private final Quaternion ROT_W = new Quaternion(1, new double[]{0, 180, 0});
    private final Quaternion ROT_SW = new Quaternion(1, new double[]{0, 225, 0});
    private final Quaternion ROT_S = new Quaternion(1, new double[]{0, 270, 0});
    private final Quaternion ROT_SE = new Quaternion(1, new double[]{0, 315, 0});
    private final Quaternion COLUMN_ROT_NE = ROT_E;
    private final Quaternion COLUMN_ROT_NW = ROT_N;
    private final Quaternion COLUMN_ROT_SW = ROT_W;
    private final Quaternion COLUMN_ROT_SE = ROT_S;

    private final Vector3D DISTANCE_N = new Vector3D(TILE_SIZE, 0, 0);
    private final Vector3D DISTANCE_NE = new Vector3D(TILE_SIZE, 0, TILE_SIZE);
    private final Vector3D DISTANCE_E = new Vector3D(0, 0, TILE_SIZE);
    private final Vector3D DISTANCE_SE = new Vector3D(-TILE_SIZE, 0, TILE_SIZE);
    private final Vector3D DISTANCE_S = new Vector3D(-TILE_SIZE, 0, 0);
    private final Vector3D DISTANCE_SW = new Vector3D(-TILE_SIZE, 0, -TILE_SIZE);
    private final Vector3D DISTANCE_W = new Vector3D(0, 0, -TILE_SIZE);
    private final Vector3D DISTANCE_NW = new Vector3D(TILE_SIZE, 0, -TILE_SIZE);

    private final int TERRAIN_SEED = 22348;
    private final int TERRAIN_SIZE = 50;
    private final int TERRAIN_ITERATIONS = 2000;
    private final float TERRAIN_HEIGHT = 0.01f; // .5f
    private final float TERRAIN_HILL_MIN_RADIUS = 5f;
    private final float TERRAIN_HILL_MAX_RADIUS = 20f;
    private final double TERRAIN_SIZE_MINUS_TILE = TERRAIN_SIZE - TILE_SIZE * 1.5f;

    //"C:\\Users\\Zagak\\Google Drive\\CSC165\\DungeonCrawler\\assets\\";

    private String googleDrivePath =  System.getProperty("user.dir") + "\\assets\\";
    private AssetInfo assetInfo;
    private TileAsset tileAsset;
    private WallAsset wallAsset;
    private WallAsset columnAsset;
    private WallAsset doorAsset;
    private WallAsset woodDoorAsset;
    private NPCEnemyAsset goblinAsset;
    private NPCEnemyAsset gobSword;
    private ObjectInteractableAsset coinAsset;
    private ObjectNonInteractableAsset barrelAsset;
    private ObjectInteractableAsset chestAsset;
    private String terrainTexture =
            googleDrivePath + "SkyBoxes\\_countrypaths_1\\down.jpg"; // http://www.farmpeeps.com/fp_skyboxes.html
    private String soundsPath = googleDrivePath + "Sounds\\";

    private BlendState rsBlendTransparent;

    private SceneNode avatarOne;
    private Camera3PController camController;
    private IDisplaySystem display;
    private IRenderer renderer;
    private ICamera camera;
    private IInputManager im;
    private IEventManager eventManager;
    private IAudioManager audioManager;
    private Random random;
    private SkyBox skyBox;
    private TerrainBlock theTerrain;
    private ScriptEngine scriptEngine;
    private File scriptFile;
    private ParticleSystemEngine particleSystemEngine;


    private AudioResource gruntAudioResource;
    private AudioResource fireballAudioResource;
    private AudioResource footstepsAudioResource;
    private AudioResource swordclashAudioResource;
    private AudioResource doorAudioResource;
    private Sound doorSound;
    private Sound fireballSound;
    private Sound gruntSound;
    private Sound footstepsSound;
    private Sound swordClashSound;

    private HUDString playerClassHUD;

    // pretend network validation
    private boolean isDemoDoRegardless = true;

    // network state
    private String hasPickedCharacter = "";
    private boolean connected;
    private boolean isReady = false;
    private boolean isYourTurn = false;
    private String send;
    private String options[] = {"yourMove", "Move", "StandardAction", "End Turn"};
    private boolean isMoveQued;
    private Vector3D moveQue;
    private Quaternion rotQue;


    // may not need all or any
    private ArrayList<SceneNode> gameWorld;
    private Group room1;
    private Group goblinsSound;
    private Group player;
    private Group animateGroup;
    private Group chestGroup;
    private ParticleSystemEngine.ParticleSystem coinsPS;

    private String kbName, gpName;
    private MoveForwardAction mvFwdActionPOne;
    private MoveLeftAction mvLeftActionPOne;
    private MoveRightAction mvRightActionPOne;
    private MoveBackAction mvBackActionPOne;
    private YawRightAction yawRightActionPOne;
    private YawLeftAction yawLeftActionPOne;


    public DungeonCrawler3D()
    {
        super();
    }

    protected void initSystem()
    {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println(googleDrivePath);
        //display = createDisplaySystem();
        //setDisplaySystem(display);
        display = new MyDisplaySystem(1092, 768, 24, 20, false, "sage.renderer.jogl.JOGLRenderer");
        display.setTitle("DungeonCrawler");
        setDisplaySystem(display);

        random = new Random();

        im = new InputManager();
        setInputManager(im);


        gameWorld = new ArrayList<>();
        setGameWorld(gameWorld);
        //super.initSystem();
    }

    protected void initGame()
    {
        renderer = display.getRenderer();
        eventManager = EventManager.getInstance();
        assetInfo = new AssetInfo(googleDrivePath);

        String engine = "sage.physics.JBullet.JBulletPhysicsEngine";
        IPhysicsEngine physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEngine.initSystem();
        float[] gravity = {0, -1f, 0};
        physicsEngine.setGravity(gravity);
        particleSystemEngine = new ParticleSystemEngine(assetInfo, physicsEngine, gameWorld,random);
        coinsPS = particleSystemEngine.getSystem("coins", 10, 2, 10000);

        initWorldEnvironment();
        try
        {
            addObjectsToWorld();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        initScriptEngines();

        associateDefaultKeyAndControllerBindings();
        camController = new Camera3PController(camera, avatarOne, im, kbName, "K");
        initAudio();
        super.update(0f);
    }


    private void addObjectsToWorld() throws Exception
    {

        // MISC OBJECTS FOR SAKE OF DEMO
        ObjectInteractableAsset testBoxAsset = assetInfo.objectInteractables.get("transtest");
        SceneNode transparentBox = testBoxAsset
                .make(new Point3D(6, 0, 6), new Point3D(2, 2, 2), new Quaternion(1, new double[]{0, 0, 0}));
        enableTransparency(transparentBox);
        addGameWorldObject(transparentBox);
        barrelAsset = assetInfo.objectNonInteractables.get("barrel");
        addGameWorldObject(barrelAsset.make(new Point3D(2, 0, 4), new Point3D(2, .2, 2), new Quaternion(1, new double[]{0, 0, 0})));

        // animation objects
        animateGroup = new Group("animateGroup");
        ObjectInteractableAsset chest = assetInfo.objectInteractables.get("chest");
        chestGroup = chest
                .makeAni(new Point3D(4, -.2, 2), new Point3D(.03, .03, .03), new Quaternion(1, new double[]{0, 0, 0}));
        animateGroup.addChild(chestGroup);
        addGameWorldObject(animateGroup);

        // coin for physics
        coinAsset = assetInfo.objectInteractables.get("coin");
        for (int i = 0; i < 5;i++)
            addGameWorldObject(coinAsset.make(new Point3D(1.5+(i*.2), 1+(i*.1), 1.5), new Point3D(.8,.8, .8),
                                          new Quaternion(1, new double[]{90, 0, 0})));


        // HARDCODED - SUPPOSE TO BE PARSED AND TRANSLATE FROM THE SERVER FIRST
        room1 = constructRoom(0, 8, 8, 0, 0);
        addDoorToRoom(room1, 0, 5, 7);
        addEnemyToRoom(room1, 0, 0, 5);
        addEnemyToRoom(room1, 1, 2, 5);
        addEnemyToRoom(room1, 2, 4, 5);
        addGameWorldObject(room1);
        // MISC OBJECTS FOR SAKE OF DEMO END ONLY



        // Trees
        ObjectNonInteractableAsset treeAsset = assetInfo.objectNonInteractables.get("tree");
        for (int i = 0 ; i < 20 ; i++)
            makeTreeAtRandomLocation(treeAsset);

        // initial character before it picks a class and connects to server
        avatarOne = assetInfo.playables.get("man")
                .make(new Point3D(0, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        camera = new JOGLCamera(renderer);
        camera.setPerspectiveFrustum(60, 2, .2, 1000);
        camera.setViewport(0.0, 1.0, 0.0, 1);
        camera.setLocation(new Point3D(0, 0, 0));
        addGameWorldObject(avatarOne);

        createPlayerHUD();
    }

    private void createPlayerHUD()
    {
        playerClassHUD = new HUDString("Pick A Class: 1-Cleric, 2-Wizard, 3-Rogue, 4-Fighter");
        playerClassHUD.setName("Class");
        playerClassHUD.setLocation(0.01, 0.01);
        playerClassHUD.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
        playerClassHUD.setColor(Color.BLACK);
        playerClassHUD.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
        camera.addToHUD(playerClassHUD);
    }

    private void setPlayerClass(String classChoice, int posX, int posY)
    {
        boolean valid = true;
        switch (classChoice)
        {
            case "Fighter":
                ((TriMesh) avatarOne).setTexture(assetInfo.playables.get("fighter").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.RED);
                break;
            case "Wizard":
                ((TriMesh) avatarOne).setTexture(assetInfo.playables.get("wizard").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.BLUE);
                break;
            case "Cleric":
                ((TriMesh) avatarOne).setTexture(assetInfo.playables.get("cleric").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.YELLOW);
                break;
            case "Rogue":
                ((TriMesh) avatarOne).setTexture(assetInfo.playables.get("rogue").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.gray);
                break;
            default:
                valid = false;

        }
        if (!valid)
        {
            System.out.println(classChoice + " isn't a valid class choice");
            return;
        }

        Vector3D cur = avatarOne.getLocalTranslation().getCol(3);
        float tx = (float) (posY * TILE_SIZE - cur.getX());
        float ty = (float) (posX * TILE_SIZE - cur.getZ());
        avatarOne.translate(tx, 0f, ty);

        player = new Group(classChoice);
        player.setParent(avatarOne);
        PlayableAsset hpAsset = assetInfo.playables.get("hp");
        PlayableAsset hpBorder = assetInfo.playables.get("hpborder");
        SceneNode snhp = hpAsset.make(new Point3D(0, 0, 0), TILE_SCALE, ROT_E);
        SceneNode snhpBorder = hpBorder.make(new Point3D(0, 0, 0), TILE_SCALE, ROT_E);
        player.addChild(snhp);
        player.addChild(snhpBorder);
        addGameWorldObject(player);
    }

    private void initWorldEnvironment()
    {
        skyBox = assetInfo.skyBoxes.get("countrypaths").make(200, 200, 200, false);
        addGameWorldObject(skyBox);

        theTerrain = initTerrain();
        addGameWorldObject(theTerrain);

        //initWorldAxes();
    }

    private TerrainBlock initTerrain()
    {
        HillHeightMap myHillHeightMap = new HillHeightMap(TERRAIN_SIZE, TERRAIN_ITERATIONS, TERRAIN_HILL_MIN_RADIUS,
                                                          TERRAIN_HILL_MAX_RADIUS, (byte) 2, TERRAIN_SEED);
        myHillHeightMap.setHeightScale(0.1f);
        TerrainBlock hillTerrain = createTerBlock(myHillHeightMap);
        Texture groundTexture = TextureManager.loadTexture2D(terrainTexture);
        groundTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
        TextureState groundState = (TextureState) display.getRenderer().createRenderState(
                RenderState.RenderStateType.Texture);
        groundState.setTexture(groundTexture, 0);
        groundState.setEnabled(true);
        hillTerrain.setRenderState(groundState);
        return hillTerrain;
    }

    private TerrainBlock createTerBlock(AbstractHeightMap heightMap)
    {
        Vector3D terrainScale = new Vector3D(1, TERRAIN_HEIGHT, 1);
        int terrainSize = heightMap.getSize();
        float cornerHeight = heightMap.getTrueHeightAtPoint(0, 0) * TERRAIN_HEIGHT;
        Point3D terrainOrigin = new Point3D(0, -cornerHeight, 0);
        String name = "Terrain:" + heightMap.getClass().getSimpleName();
        return new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);
    }

    private void initAudio()
    {
        audioManager = AudioManagerFactory.createAudioManager("sage.audio.joal.JOALAudioManager");
        if (!audioManager.initialize())
        {
            System.out.println("Audio Manager failed to initialize!");
            return;
        }

        doorAudioResource = audioManager.createAudioResource(soundsPath + "dooropen.wav",
                                                             AudioResourceType.AUDIO_SAMPLE);
        gruntAudioResource = audioManager.createAudioResource(soundsPath + "grunt.wav", AudioResourceType.AUDIO_SAMPLE);
        fireballAudioResource = audioManager.createAudioResource(soundsPath + "fireball.wav",
                                                                 AudioResourceType.AUDIO_SAMPLE);
        footstepsAudioResource = audioManager.createAudioResource(soundsPath + "footsteps1.wav",
                                                                  AudioResourceType.AUDIO_SAMPLE);
        swordclashAudioResource = audioManager.createAudioResource(soundsPath + "swordclash.wav",
                                                                   AudioResourceType.AUDIO_SAMPLE);
        doorSound = new Sound(doorAudioResource, SoundType.SOUND_EFFECT, 20, false);
        gruntSound = new Sound(gruntAudioResource, SoundType.SOUND_EFFECT, 20, false);
        fireballSound = new Sound(fireballAudioResource, SoundType.SOUND_EFFECT, 20, false);
        footstepsSound = new Sound(footstepsAudioResource, SoundType.SOUND_EFFECT, 20, false);
        swordClashSound = new Sound(swordclashAudioResource, SoundType.SOUND_EFFECT, 20, false);

        doorSound.initialize(audioManager);
        doorSound.setMaxDistance(50f);
        doorSound.setMinDistance(0.1f);
        doorSound.setRollOff(3f);

        gruntSound.initialize(audioManager);
        gruntSound.setMaxDistance(50f);
        gruntSound.setMinDistance(0.1f);
        gruntSound.setRollOff(3f);

        fireballSound.initialize(audioManager);
        fireballSound.setMaxDistance(50f);
        fireballSound.setMinDistance(0.1f);
        fireballSound.setRollOff(3f);

        footstepsSound.initialize(audioManager);
        footstepsSound.setMaxDistance(50f);
        footstepsSound.setMinDistance(0.1f);
        footstepsSound.setRollOff(3f);

        swordClashSound.initialize(audioManager);
        swordClashSound.setMaxDistance(50f);
        swordClashSound.setMinDistance(0.1f);
        swordClashSound.setRollOff(3f);

        updateEar();

    }

    private void initWorldAxes()
    {
        Point3D origin = new Point3D(0, 0, 0);
        Point3D xEnd = new Point3D(TERRAIN_SIZE_MINUS_TILE, 0, 0);
        Point3D yEnd = new Point3D(0, TERRAIN_SIZE_MINUS_TILE, 0);
        Point3D zEnd = new Point3D(0, 0, TERRAIN_SIZE_MINUS_TILE);
        Line xAxis = new Line(origin, xEnd, Color.red, 2);
        Line yAxis = new Line(origin, yEnd, Color.green, 2);
        Line zAxis = new Line(origin, zEnd, Color.blue, 2);
        addGameWorldObject(xAxis);
        addGameWorldObject(yAxis);
        addGameWorldObject(zAxis);
    }

    private void associateDefaultKeyAndControllerBindings()
    {
        im = getInputManager();
        gpName = im.getFirstGamepadName();

        if (gpName == null && im.getControllers().size() > 2)
        {
            gpName = im.getControllers().get(2).getName();
            //gpName = im.getGamepadController(2).getName();
        }
        kbName = im.getKeyboardName();
        initPlayerOneControlsPreConnection();
    }


    private void initPlayerOneControlsPreConnection()
    {
        mvFwdActionPOne = new MoveForwardAction(camera, (float) 0.1);
        mvLeftActionPOne = new MoveLeftAction(camera, (float) 0.1);
        mvRightActionPOne = new MoveRightAction(camera, (float) 0.1);
        mvBackActionPOne = new MoveBackAction(camera, (float) 0.1);
        mvFwdActionPOne.setTerrain(theTerrain);
        mvBackActionPOne.setTerrain(theTerrain);
        mvLeftActionPOne.setTerrain(theTerrain);
        mvRightActionPOne.setTerrain(theTerrain);
        mvFwdActionPOne.setTerrainFollow(true);
        mvBackActionPOne.setTerrainFollow(true);
        mvLeftActionPOne.setTerrainFollow(true);
        mvRightActionPOne.setTerrainFollow(true);

        yawRightActionPOne = new YawRightAction(camera, (float) 0.1);
        yawLeftActionPOne = new YawLeftAction(camera, (float) 0.1);

        mvFwdActionPOne.setAvatar(avatarOne);
        mvBackActionPOne.setAvatar(avatarOne);
        mvLeftActionPOne.setAvatar(avatarOne);
        mvRightActionPOne.setAvatar(avatarOne);
        yawLeftActionPOne.setAvatar(avatarOne);
        yawRightActionPOne.setAvatar(avatarOne);

        IInputManager.INPUT_ACTION_TYPE ATpressOnly = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;
        IInputManager.INPUT_ACTION_TYPE ATrepeat = IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN;

        // pickCharacter > Cleric | Wizard | Rogue | Fighter
        im.associateAction(kbName, Component.Identifier.Key._1, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   pickCharacter("Cleric");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, Component.Identifier.Key._2, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   pickCharacter("Wizard");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, Component.Identifier.Key._3, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   pickCharacter("Rogue");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, Component.Identifier.Key._4, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   pickCharacter("Fighter");
                               }
                           }
                , ATpressOnly);

        //QUIT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE,
                           (v, event) -> {
                               setGameOver(true);

                               doorSound.release(audioManager);
                               fireballSound.release(audioManager);
                               footstepsSound.release(audioManager);
                               gruntSound.release(audioManager);
                               swordClashSound.release(audioManager);

                               doorAudioResource.unload();
                               fireballAudioResource.unload();
                               footstepsAudioResource.unload();
                               gruntAudioResource.unload();
                               swordclashAudioResource.unload();

                               shutdown();
                               audioManager.shutdown();
                           }, ATpressOnly);
        //MOVE FORWARD
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W,
                           mvFwdActionPOne, ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.W,
                           mvFwdActionPOne, ATrepeat);
        //MOVE BACKWARD
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S,
                           mvBackActionPOne,
                           ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.S,
                           mvBackActionPOne, ATrepeat);
        //MOVE LEFT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A,
                           mvLeftActionPOne, ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A,
                           mvLeftActionPOne, ATrepeat);
        //MOVE RIGHT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D,
                           mvRightActionPOne, ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.D,
                           mvRightActionPOne, ATrepeat);
        //YAW
        //ROTATE LEFT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.Q,
                           yawLeftActionPOne, ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.Q,
                           yawLeftActionPOne, ATrepeat);
        //ROTATE RIGHT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.E,
                           yawRightActionPOne, ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.E,
                           yawRightActionPOne, ATrepeat);


        // OPEN CHEST/COINS trigger testing AND PHYSICS SPAM FOR COINS
        //IInputManager.INPUT_ACTION_TYPE ATpressOnly = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.G,
                           new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   // physics
                                   coinsPS.start(coinAsset,
                                                 new Point3D(2, 2, 2),
                                                 new Point3D(.8, .8, .8),
                                                 new Quaternion(1, new double[]{90, 0, 0}));

                                   Iterator<SceneNode> itr = chestGroup.getChildren();
                                   while (itr.hasNext())
                                   {
                                       Model3DTriMesh mesh = ((Model3DTriMesh) itr.next());
                                       if (mesh.hasAnimations())
                                       {
                                           mesh.startAnimation("my_animation");
                                       }
                                   }

                               }
                           },
                           ATpressOnly);

        // Script trigger testing only works once per adjustment of script
        //IInputManager.INPUT_ACTION_TYPE ATpressOnly = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.T,
                           new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   Invocable invocableEngine = (Invocable) scriptEngine;
                                   String newTexture = "";
                                   try
                                   {
                                       newTexture = (String) invocableEngine.invokeFunction("getWallTexture");
                                   }
                                   catch (ScriptException e)
                                   {
                                       e.printStackTrace();
                                   }
                                   catch (NoSuchMethodException e)
                                   {
                                       e.printStackTrace();
                                   }


                                   Iterator<SceneNode> children = room1.getChildren();
                                   while (children.hasNext())
                                   {
                                       SceneNode obj = children.next();

                                       if (obj.getName().contains("Wall"))
                                       {
                                           if (newTexture != "random")
                                           {
                                               Texture t = tileAsset.giveMeTexture(newTexture);
                                               if (t != null)
                                                   ((TriMesh) obj).setTexture(t);
                                               else
                                                   ((TriMesh) obj).setTexture(tileAsset.giveMeTexture());
                                           } else
                                               ((TriMesh) obj).setTexture(tileAsset.giveMeTexture());
                                       }
                                   }

                               }
                           },
                           ATpressOnly);
    }

    private void initPlayerOneControlsPostConnection()
    {
        mvFwdActionPOne.setEnabled(false);
        mvLeftActionPOne.setEnabled(false);
        mvRightActionPOne.setEnabled(false);
        mvBackActionPOne.setEnabled(false);
        yawLeftActionPOne.setEnabled(false);
        yawRightActionPOne.setEnabled(false);

        IInputManager.INPUT_ACTION_TYPE ATpressOnly = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;


        // yourMove+End Turn > [end]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestEndTurn();
                               }
                           }
                , ATpressOnly);

        // yourMove+StandardAction > [standard]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestStandardAction();
                               }
                           }
                , ATpressOnly);

        // StandardActionType > [attack]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestStandardType();
                               }
                           }
                , ATpressOnly);

        // attackType > [range]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestAttack();
                               }
                           }
                , ATpressOnly);

        // yourMove+Move > [move]
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD5, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestMove();
                               }
                           }
                , ATpressOnly);

        // moveDirection > N | NE | E | SE | S | SW | W | NW
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD7, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("NW");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD8, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("N");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD9, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("NE");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD4, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("W");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD6, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("E");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD1, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("SW");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD2, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("S");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD3, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("SE");
                               }
                           }
                , ATpressOnly);

        // moveDirection > N | NE | E | SE | S | SW | W | NW
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.I, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("NW");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.O, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("N");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.P, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("NE");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.J, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("W");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.L, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("E");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.N, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("SW");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.M, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("S");
                               }
                           }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.COMMA, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   tryMoveNumpad("SE");
                               }
                           }
                , ATpressOnly);

        // yourMove+End Turn > [end]
        im.associateAction(kbName, Component.Identifier.Key.END, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestEndTurn();
                               }
                           }
                , ATpressOnly);

        // yourMove+StandardAction > [standard]
        im.associateAction(kbName, Component.Identifier.Key.H, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestStandardAction();
                               }
                           }
                , ATpressOnly);

        // StandardActionType > [attack]
        im.associateAction(kbName, Component.Identifier.Key.F, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestStandardType();
                               }
                           }
                , ATpressOnly);

        // attackType > [range]
        im.associateAction(kbName, Component.Identifier.Key.G, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestAttack();
                               }
                           }
                , ATpressOnly);

        // yourMove+Move > [move]
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.K, new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   requestMove();
                               }
                           }
                , ATpressOnly);

    }

    private void pickCharacter(String classChoice)
    {
        if (!Arrays.asList(options).contains(classChoice) && !isDemoDoRegardless)
            return;
        hasPickedCharacter = classChoice;
        send = classChoice;
        if (isDemoDoRegardless)
        {
            setPlayerClass(classChoice, 3, 5);
            initPlayerOneControlsPostConnection();
        }
    }

    private void requestStandardAction()
    {
        if (!isYourTurn && send != "")
            return;
        if (Arrays.asList(options).contains("StandardAction"))
            send = "standard";
    }

    private void requestEndTurn()
    {
        if (!isYourTurn && send != "")
            return;
        if (Arrays.asList(options).contains("End Turn"))
            send = "end";
    }

    private void requestAttack()
    {
        if (isDemoDoRegardless)
        {
            int select = random.nextInt(goblinsSound.getNumberOfChildren());
            SceneNode gob = ((Group) goblinsSound.getChild(0 + "Goblin" + select)).getChild("g" + select);
            if (gob != null)
            {
                Vector3D pos = gob.getLocalTranslation().getCol(3);
                gruntSound.setLocation(new Point3D(pos.getX(), pos.getY(), pos.getZ()));
                gruntSound.play();
                System.out.println("attacking #" + select + " goblin at " + pos);
            }
        }

        if (send != "")
            return;
        if (Arrays.asList(options).contains("attack"))
            send = "ranged";
    }

    private void requestStandardType()
    {
        if (send != "")
            return;
        if (Arrays.asList(options).contains("StandardActionType"))
            send = "attack";
    }

    private void requestMove()
    {
        if (!isYourTurn && send != "")
            return;
        if (Arrays.asList(options).contains("Move"))
            send = "move";
    }

    private void tryMoveNumpad(String direction)
    {
        if (isDemoDoRegardless)
        {

            //if (!isYourTurn) // faint rot in dir
            //    return;
            //if (Arrays.asList(options).contains("moveDirection")) // Arrays.asList(options).contains("EnemyList")
            //{
            switch (direction)
            {
                case "N":
                    moveQue = DISTANCE_N;
                    rotQue = ROT_N;
                    break;
                case "NE":
                    moveQue = DISTANCE_NE;
                    rotQue = ROT_NE;
                    break;
                case "E":
                    moveQue = DISTANCE_E;
                    rotQue = ROT_E;
                    break;
                case "SE":
                    moveQue = DISTANCE_SE;
                    rotQue = ROT_SE;
                    break;
                case "S":
                    moveQue = DISTANCE_S;
                    rotQue = ROT_S;
                    break;
                case "SW":
                    moveQue = DISTANCE_SW;
                    rotQue = ROT_SW;
                    break;
                case "W":
                    moveQue = DISTANCE_W;
                    rotQue = ROT_W;
                    break;
                case "NW":
                    moveQue = DISTANCE_NW;
                    rotQue = ROT_NW;
                    break;
                default:
                    return;
            }
            send = direction;
            isMoveQued = true;

            System.out.println("direction:" + direction);
            Vector3D pos = avatarOne.getLocalTranslation().getCol(3);
            footstepsSound.setLocation(new Point3D(pos.getX(), pos.getY(), pos.getZ()));
            footstepsSound.play();

            avatarOne.translate((float) moveQue.getX(), 0f, (float) moveQue.getZ());

            Matrix3D rot = new Matrix3D();
            rot.rotate(rotQue.getAngleAxis()[1], rotQue.getAngleAxis()[2], rotQue.getAngleAxis()[3]);
            avatarOne.setLocalRotation(rot);
        }
    }


    private void updateSkybox()
    {
        Point3D camLoc1 = camera.getLocation();
        Matrix3D camTrans = new Matrix3D();
        camTrans.translate(camLoc1.getX(), camLoc1.getY(), camLoc1.getZ());
        skyBox.setLocalTranslation(camTrans);
    }

    private void updateEar()
    {
        Matrix3D avDir = (Matrix3D) (avatarOne.getWorldRotation().clone());
        float camAz = camController.getCameraAzimuth();
        avDir.rotateY(180.0f - camAz);
        Vector3D dir = new Vector3D(0, 0, 1);
        dir = dir.mult(avDir);
        audioManager.getEar().setLocation(camera.getLocation());
        audioManager.getEar().setOrientation(dir, new Vector3D(0, 1, 0));
    }

    public void update(float elapsedTimeMS)
    {
        coinsPS.update(elapsedTimeMS);
        updateAnimations(elapsedTimeMS);

        // TODO PARSE SERVER(DUNGEON MASTER) SEE  outputLog.txt in DungeonMasert/src
        //if charactersAvailable:Cleric;Fighter;Rogue;Wizard;$$ and pickCharacter
        //      initPlayerOneControlsPostConnection();

        // if (PlayerCharacter12:XY 0,0;HPMaxHP 44,44;$$)
        //      setPlayerClass(hasPickedCharacter,0,0); // hp starts 100 //snhp1.getLocalScale().setElementAt(0,0,0); figure out later // for hp

        // if (reportReadyToProceed)
        //      send

        // if (GW:.............)
        //Group room = constructRoom(0, 8, 12, 0, 0);
        //addDoorToRoom(room,0,5,11);
        //addEnemyToRoom(room,0,0,5);
        //addEnemyToRoom(room,1,2,5);
        //addEnemyToRoom(room,2,4,5);
        //addGameWorldObject(room);
        // then do without this part loop


        mapBoundaryFix(avatarOne); // probably dont need this after setPlayerClass() has been called
        updateSkybox();
        updateEar();
        camController.update(elapsedTimeMS);
        super.update(elapsedTimeMS);
    }

    private void updateAnimations(float elapsedTimeMS)
    {
        Iterator<SceneNode> groups = animateGroup.getChildren();
        while (groups.hasNext())
        {
            Iterator<SceneNode> children = ((Group) groups.next()).getChildren();
            while (children.hasNext())
            {
                Model3DTriMesh submesh = (Model3DTriMesh) children.next();
                if (submesh.isAnimating())
                {
                    if (submesh.getCurrentAnimationTime() < 3.2f)   // unique to chest
                        submesh.updateAnimation(elapsedTimeMS);
                    else
                        submesh.stopAnimation();
                }
            }
        }
    }

    protected void render()
    {
        renderer.setCamera(camera);
        super.render();
    }

    public void addGameWorldObject(SceneNode s)
    {
        super.addGameWorldObject(s);
    }

    private void enableTransparency(SceneNode target)
    {
        if (rsBlendTransparent == null)
            rsBlendTransparent = (BlendState) renderer.createRenderState(RenderState.RenderStateType.Blend);
        rsBlendTransparent.setBlendEnabled(false);
        rsBlendTransparent.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        rsBlendTransparent.setDestinationFunction(BlendState.DestinationFunction.DestinationAlpha);
        rsBlendTransparent.setTestEnabled(true);
        rsBlendTransparent.setTestFunction(BlendState.TestFunction.GreaterThan);
        rsBlendTransparent.setEnabled(true);
        target.setRenderMode(SceneNode.RENDER_MODE.TRANSPARENT);
        target.setRenderState(rsBlendTransparent);
        target.updateRenderStates();
    }

    private void mapBoundaryFix(SceneNode node)
    {
        Vector3D pos = node.getLocalTranslation().getCol(3);
        if (pos.getX() < TILE_HALF_SIZE)
            node.getLocalTranslation().setElementAt(0, 3, TILE_HALF_SIZE);
        if (pos.getZ() < TILE_HALF_SIZE)
            node.getLocalTranslation().setElementAt(2, 3, TILE_HALF_SIZE);
        if (pos.getX() > TERRAIN_SIZE_MINUS_TILE)
            node.getLocalTranslation().setElementAt(0, 3, TERRAIN_SIZE_MINUS_TILE);
        if (pos.getZ() > TERRAIN_SIZE_MINUS_TILE)
            node.getLocalTranslation().setElementAt(2, 3, TERRAIN_SIZE_MINUS_TILE);
    }

    private void makeTreeAtRandomLocation(Asset asset)
    {
        double scaleMax = 2.5;
        double scaleMin = 2.0;
        double skew = 0.2;
        double scaleX = scaleMin + (scaleMax - scaleMin) * random.nextDouble();
        double scaleY = scaleX + ((0.5 - random.nextFloat()) * 2.0) * skew;
        double scaleZ = scaleX + ((0.5 - random.nextFloat()) * 2.0) * skew;

        double posX = random.nextDouble() * (TERRAIN_SIZE_MINUS_TILE - TILE_HALF_SIZE) + TILE_HALF_SIZE;
        double posZ = random.nextDouble() * (TERRAIN_SIZE_MINUS_TILE - TILE_HALF_SIZE) + TILE_HALF_SIZE;
        double posY = theTerrain.getHeight((float) posX, (float) posZ) - (.5f * scaleY);

        double rotMax = 4;
        double rotY = 360.0 * random.nextDouble();
        double rotX = ((0.5 - random.nextDouble()) * 2.0) * rotMax;
        double rotZ = ((0.5 - random.nextDouble()) * 2.0) * rotMax;

        SceneNode tree = asset.make(new Point3D(posX, posY, posZ), new Point3D(scaleX, scaleY, scaleZ),
                                    new Quaternion(1, new double[]{rotX, rotY, rotZ}));
        enableTransparency(tree);
        addGameWorldObject(tree);
    }

    public void initScriptEngines()
    {
        ScriptEngineManager factory = new ScriptEngineManager();
        scriptEngine = factory.getEngineByName("js");
        scriptFile = new File(googleDrivePath + "textureScript.js");
        try
        {
            FileReader fileReader = new FileReader(scriptFile);
            scriptEngine.eval(fileReader);
            fileReader.close();
        }
        catch (FileNotFoundException e1)
        {
            System.out.println(scriptFile + " not found " + e1);
        }
        catch (IOException e2)
        {
            System.out.println("IO problem with " + scriptFile + e2);
        }
        catch (ScriptException e3)
        {
            System.out.println("ScriptException in " + scriptFile + e3);
        }
        catch (NullPointerException e4)
        {
            System.out.println("Null ptr exception reading " + scriptFile + e4);
        }
    }

    private void executeScript(ScriptEngine engine, String scriptFileName)
    {
        try
        {
            FileReader fileReader = new FileReader(scriptFileName);
            engine.eval(fileReader); //execute the script statements in the file
            fileReader.close();
        }
        catch (FileNotFoundException e1)
        {
            System.out.println(scriptFileName + " not found " + e1);
        }
        catch (IOException e2)
        {
            System.out.println("IO problem with " + scriptFileName + e2);
        }
        catch (ScriptException e3)
        {
            System.out.println("ScriptException in " + scriptFileName + e3);
        }
        catch (NullPointerException e4)
        {
            System.out.println("Null ptr exception in " + scriptFileName + e4);
        }
    }

    private Group constructRoom(int number, int sizeX, int sizeY, int posX, int posY)
    {
        Group room = new Group("Room " + number);

        if (tileAsset == null)
        {
            tileAsset = assetInfo.tiles.get("tile");
            tileAsset.setRandomTexture(true);
        }
        if (wallAsset == null)
            wallAsset = assetInfo.walls.get("wall");
        if (columnAsset == null)
            columnAsset = assetInfo.walls.get("column");


        // tiles
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                room.addChild(tileAsset.make(number + " Tile " + x + "x" + y,
                                             new Point3D((y + posY) * TILE_SIZE, 0, (x + posX) * TILE_SIZE), TILE_SCALE,
                                             getRandomTileRotation()));


        // walls
        for (int x = 0; x < sizeX; x++) // north
            room.addChild(wallAsset.make(number + " Wall North " + x,
                                         new Point3D((posY + sizeY - 1) * TILE_SIZE, 0, (x + posX) * TILE_SIZE),
                                         TILE_SCALE, ROT_N));

        for (int x = 0; x < sizeX; x++) // south
            room.addChild(wallAsset.make(number + " Wall South " + x,
                                         new Point3D(posY * TILE_SIZE, 0, (x + posX) * TILE_SIZE), TILE_SCALE, ROT_S));

        for (int y = 0; y < sizeY; y++) // west
            room.addChild(
                    wallAsset.make(number + " Wall West " + y, new Point3D((y + posY) * TILE_SIZE, 0, posX * TILE_SIZE),
                                   TILE_SCALE, ROT_W));

        for (int y = 0; y < sizeY; y++) // east
            room.addChild(wallAsset.make(number + " Wall East " + y,
                                         new Point3D((y + posY) * TILE_SIZE, 0, (posX + sizeX - 1) * TILE_SIZE),
                                         TILE_SCALE, ROT_E));


        // columns
        for (int x = 0; x < sizeX; x++) // north
            room.addChild(columnAsset.make(number + " Column North " + x,
                                           new Point3D((posY + sizeY - 1) * TILE_SIZE, 0, (x + posX) * TILE_SIZE),
                                           TILE_SCALE, COLUMN_ROT_NW));
        for (int x = 0; x < sizeX; x++) // south
            room.addChild(columnAsset.make(number + " Column South " + x,
                                           new Point3D(posY * TILE_SIZE, 0, (x + posX) * TILE_SIZE), TILE_SCALE,
                                           COLUMN_ROT_SE));
        for (int y = 0; y < sizeY; y++) // west
            room.addChild(columnAsset.make(number + " Column West " + y,
                                           new Point3D((y + posY) * TILE_SIZE, 0, posX * TILE_SIZE), TILE_SCALE,
                                           COLUMN_ROT_SW));
        for (int y = 0; y < sizeY; y++) // east
            room.addChild(columnAsset.make(number + " Column East " + y,
                                           new Point3D((y + posY) * TILE_SIZE, 0, (posX + sizeX) * TILE_SIZE),
                                           TILE_SCALE, COLUMN_ROT_NW));

        return room;
    }

    private void addDoorToRoom(Group room, int number, int posX, int posY)
    {
        if (doorAsset == null)
            doorAsset = assetInfo.walls.get("door");
        if (woodDoorAsset == null)
            woodDoorAsset = assetInfo.walls.get("doorwood");

        String target = "";
        Matrix3D tarRot = null;
        Iterator<SceneNode> iterator = room.iterator();
        int tarZ = (int) (posX * TILE_SIZE);
        int tarX = (int) (posY * TILE_SIZE);
        while (iterator.hasNext())
        {
            SceneNode node = iterator.next();
            Vector3D pos = node.getLocalTranslation().getCol(3);
            if (tarZ == pos.getZ() && tarX == pos.getX() && node.getName().contains("Wall"))
            {
                target = node.getName();
                tarRot = node.getLocalRotation();
                break;
            }
        }
        if (tarRot != null)
        {
            String roomNumber = room.getName().split(" ")[1];
            room.removeChild(room.getChild(target));
            SceneNode door = doorAsset.make(roomNumber + " Door " + number, new Point3D(tarX, 0, tarZ), TILE_SCALE,
                                            new Quaternion());
            SceneNode woodDoor = woodDoorAsset.make(roomNumber + " WoodDoor " + number, new Point3D(tarX, 0, tarZ),
                                                    TILE_SCALE, new Quaternion());
            door.setLocalRotation(tarRot);
            woodDoor.setLocalRotation(tarRot);
            room.addChild(door);
            room.addChild(woodDoor);
        }
    }

    private void addEnemyToRoom(Group room, int number, int posX, int posY)
    {
        if (goblinAsset == null)
            goblinAsset = assetInfo.npcEnemies.get("goblin");
        if (gobSword == null)
            gobSword = assetInfo.npcEnemies.get("swordgoblin");


        String roomNumber = room.getName().split(" ")[1];
        Group goblin = new Group(roomNumber + "Goblin" + number);
        goblin.addChild(
                goblinAsset.make("g" + number, new Point3D(posY * TILE_SIZE, 0, posX * TILE_SIZE), TILE_SCALE, ROT_E));
        goblin.addChild(
                gobSword.make("s" + number, new Point3D(posY * TILE_SIZE, 0, posX * TILE_SIZE), TILE_SCALE, ROT_E));
        room.addChild(goblin);

        if (goblinsSound == null)
            goblinsSound = new Group("Goblin" + number);
        goblinsSound.addChild(goblin);
    }

    private Quaternion getRandomTileRotation()
    {
        int select = random.nextInt(100) % 4;
        if (select == 0)
            return ROT_N;
        if (select == 1)
            return ROT_E;
        if (select == 2)
            return ROT_S;
        return ROT_W;
    }
}