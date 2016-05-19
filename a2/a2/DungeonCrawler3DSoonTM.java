package a2;

import a2.assets.*;

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
import sage.audio.AudioResource;
import sage.audio.IAudioManager;
import sage.audio.Sound;
import sage.display.*;
import sage.event.EventManager;
import sage.event.IEventManager;
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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DungeonCrawler3DSoonTM extends BaseGame
{
    private final double TILE_SIZE = 2.0;
    private final double TS = TILE_SIZE;
    private final double TILE_HALF_SIZE = TILE_SIZE * .5f;
    private final Point3D TILE_SCALE = new Point3D(1.32,1.32,1.32); // 1.32

    private final Quaternion ROT_E = new Quaternion(); // default model orientation
    private final Quaternion ROT_NE = new Quaternion(1, new double[]{0,45,0});
    private final Quaternion ROT_N = new Quaternion(1, new double[]{0,90,0});
    private final Quaternion ROT_NW = new Quaternion(1, new double[]{0,135,0});
    private final Quaternion ROT_W = new Quaternion(1, new double[]{0,180,0});
    private final Quaternion ROT_SW = new Quaternion(1, new double[]{0,225,0});
    private final Quaternion ROT_S = new Quaternion(1, new double[]{0,270,0});
    private final Quaternion ROT_SE = new Quaternion(1, new double[]{0,315,0});
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

    private String googleDrivePath = "C:\\Users\\Zagak\\Google Drive\\CSC165\\DungeonCrawler\\assets\\"; //todo not hard code
    private AssetInfo assetInfo;
    private TileAsset tileAsset;
    private WallAsset wallAsset;
    private WallAsset columnAsset;
    private WallAsset doorAsset;
    private NPCEnemyAsset goblinAsset;
    private NPCEnemyAsset gobSword;
    private String terrainTexture = googleDrivePath + "SkyBoxes\\_countrypaths_1\\down.jpg"; // http://www.farmpeeps.com/fp_skyboxes.html

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

    private AudioResource resource1;
    private Sound sound;

    private HUDString playerClassHUD;

    // network state
    private String hasPickedCharacter = "";
    private boolean connected;
    private boolean isReady = false;
    private boolean isYourTurn = false;
    private String send;
    private String options[];
    private boolean isMoveQued;
    private Vector3D moveQue;


    private Group player;  // may not need
    private Group animateGroup;
    private Group chestGroup;

    private String kbName, gpName;
    private MoveForwardAction mvFwdActionPOne;
    private MoveLeftAction mvLeftActionPOne;
    private MoveRightAction mvRightActionPOne;
    private MoveBackAction mvBackActionPOne;
    private YawRightAction yawRightActionPOne;
    private YawLeftAction yawLeftActionPOne;
    ;

    public DungeonCrawler3DSoonTM()
    {
        super();
    }

    protected void initSystem()
    {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //display = createDisplaySystem();
        //setDisplaySystem(display);
        display = new MyDisplaySystem(1092, 768, 24, 20, false, "sage.renderer.jogl.JOGLRenderer");
        display.setTitle("DungeonCrawler");
        setDisplaySystem(display);

        random = new Random();

        im = new InputManager();
        setInputManager(im);

        ArrayList<SceneNode> gameWorld = new ArrayList<>();
        setGameWorld(gameWorld);
        //super.initSystem();
    }

    protected void initGame()
    {
        renderer = display.getRenderer();
        eventManager = EventManager.getInstance();
        assetInfo = new AssetInfo(googleDrivePath);

        initWorldEnvironment();
        try
        {
            addObjectsToWorld();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //initScriptEngines();

        associateDefaultKeyAndControllerBindings();
        camController = new Camera3PController(camera, avatarOne, im, kbName, "K");
        super.update(0f);
    }


    private void addObjectsToWorld() throws Exception
    {
        // testing transparency
        //ObjectNonInteractableAsset treeAsset = assetInfo.objectNonInteractables.get("tree");
        //for (int i = 0 ; i < 20 ; i++)
        //    makeTreeAtRandomLocation(treeAsset);

        // PLAYERS
        //SceneNode cleric = assetInfo.playables.get("cleric")
        //        .make(new Point3D(0, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        //addGameWorldObject(cleric);
        //SceneNode wizard = assetInfo.playables.get("wizard")
        //        .make(new Point3D(2, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        //addGameWorldObject(wizard);
        //SceneNode rogue = assetInfo.playables.get("rogue")
        //        .make(new Point3D(4, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        //addGameWorldObject(rogue);
        //SceneNode fighter = assetInfo.playables.get("fighter")
        //        .make(new Point3D(6, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        //addGameWorldObject(fighter);

        Group room = constructRoom(0, 8, 12, 0, 0);
        addDoorToRoom(room,0,5,11);
        addEnemyToRoom(room,0,0,5);
        addEnemyToRoom(room,1,2,5);
        addEnemyToRoom(room,2,4,5);
        addGameWorldObject(room);


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
        playerClassHUD = new HUDString("Pick A Class");
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
                ((TriMesh)avatarOne).setTexture(assetInfo.playables.get("fighter").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.RED);
                break;
            case "Wizard":
                ((TriMesh)avatarOne).setTexture(assetInfo.playables.get("wizard").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.BLUE);
                break;
            case "Cleric":
                ((TriMesh)avatarOne).setTexture(assetInfo.playables.get("cleric").giveMeTexture());
                playerClassHUD.setText(classChoice);
                playerClassHUD.setColor(Color.YELLOW);
                break;
            case "Rogue":
                ((TriMesh)avatarOne).setTexture(assetInfo.playables.get("rogue").giveMeTexture());
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
        float tx = (float) (posY*TILE_SIZE - cur.getX());
        float ty = (float) (posX*TILE_SIZE - cur.getZ());
        avatarOne.translate(tx,0f,ty);

        player = new Group(classChoice);
        player.setParent(avatarOne);
        PlayableAsset hpAsset = assetInfo.playables.get("hp");
        PlayableAsset hpBorder = assetInfo.playables.get("hpborder");
        SceneNode snhp = hpAsset.make(new Point3D(0,0,0), TILE_SCALE, ROT_E);
        SceneNode snhpBorder = hpBorder.make(new Point3D(0,0,0), TILE_SCALE, ROT_E);
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
        initWorldAxes();
    }

    private TerrainBlock initTerrain()
    {
        HillHeightMap myHillHeightMap = new HillHeightMap(TERRAIN_SIZE, TERRAIN_ITERATIONS, TERRAIN_HILL_MIN_RADIUS, TERRAIN_HILL_MAX_RADIUS, (byte) 2, TERRAIN_SEED);
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

        //QUIT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.ESCAPE,
                           (v, event) -> setGameOver(true), ATpressOnly);
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
                           mvBackActionPOne,ATrepeat);
        //MOVE LEFT
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.A,
                           mvLeftActionPOne,ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.A,
                           mvLeftActionPOne,ATrepeat);
        //MOVE RIGHT
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.D,
                           mvRightActionPOne,ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.D,
                           mvRightActionPOne,ATrepeat);
        //YAW
        //ROTATE LEFT
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.Q,
                           yawLeftActionPOne,ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.Q,
                           yawLeftActionPOne,ATrepeat);
        //ROTATE RIGHT
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.E,
                           yawRightActionPOne, ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.E,
                           yawRightActionPOne,ATrepeat);


        // OPEN CHEST trigger testing
        //IInputManager.INPUT_ACTION_TYPE ATpressOnly = IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY;
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.G,
                           new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
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

        // pickCharacter > Cleric | Wizard | Rogue | Fighter
        im.associateAction(kbName, Component.Identifier.Key._1, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { pickCharacter("Cleric");} }
                , ATpressOnly);
        im.associateAction(kbName, Component.Identifier.Key._2, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { pickCharacter("Wizard");} }
                , ATpressOnly);
        im.associateAction(kbName, Component.Identifier.Key._3, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { pickCharacter("Rogue");} }
                , ATpressOnly);
        im.associateAction(kbName, Component.Identifier.Key._4, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { pickCharacter("Fighter");} }
                , ATpressOnly);


        // yourMove+End Turn > [end]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { requestEndTurn();} }
                , ATpressOnly);

        // yourMove+StandardAction > [standard]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { requestStandardAction();} }
                , ATpressOnly);

        // StandardActionType > [attack]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { requestStandardType();} }
                , ATpressOnly);

        // attackType > [range]
        im.associateAction(kbName, Component.Identifier.Key.NUMPADENTER, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { requestAttack(); } }
                , ATpressOnly);

        // yourMove+Move > [move]
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD5, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { requestMove();} }
                , ATpressOnly);

        // moveDirection > N | NE | E | SE | S | SW | W | NW
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD7, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("NW");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD8, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("N");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD9, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("NE");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD4, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("W");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD6, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("E");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD1, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("SW");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD2, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("S");} }
                , ATpressOnly);
        im.associateAction(kbName, net.java.games.input.Component.Identifier.Key.NUMPAD3, new AbstractInputAction()
                           { public void performAction(float v, Event event)  { tryMoveNumpad("SE");} }
                , ATpressOnly);
    }

    private void pickCharacter(String classChoice)
    {
        if (!Arrays.asList(options).contains(classChoice))
            return;
        hasPickedCharacter = classChoice;
        send = classChoice;
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
        if(Arrays.asList(options).contains("Move"))
            send = "move";
    }

    private void tryMoveNumpad(String direction)
    {
        if (!isYourTurn) // faint rot in dir
            return;
        if (Arrays.asList(options).contains("moveDirection")) // Arrays.asList(options).contains("EnemyList")
        {
            switch (direction)
            {
                case "N":
                    moveQue = DISTANCE_N;
                    break;
                case "NE":
                    moveQue = DISTANCE_NE;
                    break;
                case "E":
                    moveQue = DISTANCE_E;
                    break;
                case "SE":
                    moveQue = DISTANCE_SE;
                    break;
                case "S":
                    moveQue = DISTANCE_S;
                    break;
                case "SW":
                    moveQue = DISTANCE_SW;
                    break;
                case "W":
                    moveQue = DISTANCE_W;
                    break;
                case "NW":
                    moveQue = DISTANCE_NW;
                    break;
                default:
                    return;
            }
            send = direction;
            isMoveQued = true;
        }
    }


    private void updateSkybox()
    {
        Point3D camLoc1 = camera.getLocation();
        Matrix3D camTrans = new Matrix3D();
        camTrans.translate(camLoc1.getX(), camLoc1.getY(), camLoc1.getZ());
        skyBox.setLocalTranslation(camTrans);
    }


    public void update(float elapsedTimeMS)
    {
        updateAnimations(elapsedTimeMS);

        // TODO STUFF BELOW + SOUND/SCRIPT + PHYSICS + FIX FSEM + IMPORT THEIR MODELS + ZIP + REPORT
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
        camController.update(elapsedTimeMS);
        super.update(elapsedTimeMS);
    }

    private void updateAnimations(float elapsedTimeMS)
    {
        Iterator<SceneNode> groups = animateGroup.getChildren();
        while (groups.hasNext())
        {
            Iterator<SceneNode> children = ((Group)groups.next()).getChildren();
            while (children.hasNext())
            {
                Model3DTriMesh submesh = (Model3DTriMesh) children.next();
                if (submesh.isAnimating())
                {
                    if (submesh.getCurrentAnimationTime() < 3.33f)   // unique to chest
                    {
                        submesh.updateAnimation(elapsedTimeMS);


                        // remove from animation group add to gw ?
                    }
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
            node.getLocalTranslation().setElementAt(0,3,TILE_HALF_SIZE);
        if (pos.getZ() < TILE_HALF_SIZE)
            node.getLocalTranslation().setElementAt(2,3,TILE_HALF_SIZE);
        if (pos.getX() > TERRAIN_SIZE_MINUS_TILE)
            node.getLocalTranslation().setElementAt(0,3,TERRAIN_SIZE_MINUS_TILE);
        if (pos.getZ() > TERRAIN_SIZE_MINUS_TILE)
            node.getLocalTranslation().setElementAt(2,3,TERRAIN_SIZE_MINUS_TILE);
    }

    private void makeTreeAtRandomLocation(Asset asset)
    {
        double scaleMax = 2.5;
        double scaleMin = 2.0;
        double skew = 0.2;
        double scaleX = scaleMin + (scaleMax-scaleMin)*random.nextDouble();
        double scaleY = scaleX + ((0.5 - random.nextFloat())*2.0)*skew;
        double scaleZ = scaleX + ((0.5 - random.nextFloat())*2.0)*skew;

        double posX = random.nextDouble()*(TERRAIN_SIZE_MINUS_TILE-TILE_HALF_SIZE) + TILE_HALF_SIZE;
        double posZ = random.nextDouble()*(TERRAIN_SIZE_MINUS_TILE-TILE_HALF_SIZE) + TILE_HALF_SIZE;
        double posY =  theTerrain.getHeight((float)posX,(float)posZ) - (.5f*scaleY);

        double rotMax = 4;
        double rotY = 360.0 * random.nextDouble();
        double rotX = ((0.5 - random.nextDouble())*2.0)*rotMax;
        double rotZ = ((0.5 - random.nextDouble())*2.0)*rotMax;

        SceneNode tree = asset.make(new Point3D(posX, posY, posZ), new Point3D(scaleX,scaleY,scaleZ), new Quaternion(1, new double[] {rotX,rotY,rotZ}));
        enableTransparency(tree);
        addGameWorldObject(tree);
    }

    public void initScriptEngines()
    {
        ScriptEngineManager factory = new ScriptEngineManager();
        String scriptFileName = googleDrivePath + "testscript1.js";

        java.util.List<ScriptEngineFactory> list = factory.getEngineFactories();

        ScriptEngine jsEngine = factory.getEngineByName("js");

        this.executeScript(jsEngine, scriptFileName);
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
                                             ROT_E));


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

        String target = "";
        Matrix3D tarRot = null;
        Iterator<SceneNode> iterator = room.iterator();
        int tarZ = (int)(posX*TILE_SIZE);
        int tarX = (int)(posY*TILE_SIZE);
        while ( iterator.hasNext())
        {
            SceneNode node = iterator.next();
            Vector3D pos = node.getLocalTranslation().getCol(3);
            if (tarZ == pos.getZ() &&  tarX== pos.getX() && node.getName().contains("Wall"))
            {
                target = node.getName();
                tarRot = node.getLocalRotation();
                System.out.println(target);
                break;
            }
        }
        if (tarRot != null)
        {
            String roomNumber = room.getName().split(" ")[1];
            room.removeChild(room.getChild(target));
            SceneNode door = doorAsset.make(roomNumber + " Door " + number, new Point3D(tarX,0,tarZ), TILE_SCALE, new Quaternion());
            door.setLocalRotation(tarRot);
            room.addChild(door);
        }
    }

    private void addEnemyToRoom(Group room, int number, int posX, int posY)
    {
        if (goblinAsset == null)
            goblinAsset = assetInfo.npcEnemies.get("goblin");
        if (gobSword == null)
            gobSword = assetInfo.npcEnemies.get("swordgoblin");

        String roomNumber = room.getName().split(" ")[1];
        Group goblin = new Group(roomNumber + "Goblin " + number);
        goblin.addChild(goblinAsset.make(new Point3D(posY*TILE_SIZE,0,posX*TILE_SIZE), TILE_SCALE, ROT_E));
        goblin.addChild(gobSword.make(new Point3D(posY*TILE_SIZE,0,posX*TILE_SIZE), TILE_SCALE, ROT_E));
        room.addChild(goblin);
    }

    public void unusedStuff() throws Exception
    {
        // testing transparency
        ObjectInteractableAsset testBoxAsset = assetInfo.objectInteractables.get("transtest");
        SceneNode transparentBox = testBoxAsset
                .make(new Point3D(0, 0, 1), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        enableTransparency(transparentBox);
        addGameWorldObject(transparentBox);

        // MISC
        ObjectNonInteractableAsset barrel = assetInfo.objectNonInteractables.get("barrel");
        addGameWorldObject(barrel.make(new Point3D(0, 0, 1.5), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));

        // animation objects
        animateGroup = new Group("animateGroup");

        ObjectInteractableAsset chest = assetInfo.objectInteractables.get("chest");
        chestGroup = chest
                .makeAni(new Point3D(0, 0, 0), new Point3D(.01, .01, .01), new Quaternion(1, new double[]{0, 0, 0}));
        animateGroup.addChild(chestGroup);
        addGameWorldObject(animateGroup);
    }
}