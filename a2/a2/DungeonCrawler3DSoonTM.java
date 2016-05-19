package a2;

import a2.assets.*;

import myGameEngine.GameClientTCP;
import myGameEngine.GameServerTCP;
import myGameEngine.Camera3PController;
import myGameEngine.MoveBackAction;
import myGameEngine.MoveForwardAction;
import myGameEngine.MoveLeftAction;
import myGameEngine.MoveRightAction;
import myGameEngine.MyDisplaySystem;
import myGameEngine.PitchDownAction;
import myGameEngine.PitchUpAction;
import myGameEngine.YawLeftAction;
import myGameEngine.YawRightAction;

import net.java.games.input.Event;
import sage.app.BaseGame;
import sage.audio.AudioResource;
import sage.audio.IAudioManager;
import sage.audio.Sound;
import sage.display.*;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.networking.IGameConnection;
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
import java.net.InetAddress;
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

    private float time;
    private HUDString player1ID;
    private HUDString timeStringPOne;
    private Random random;
    private SceneNode avatarOne;
    private String kbName, gpName;

    private SkyBox skyBox;
    private TerrainBlock theTerrain;
    private Group background;

    private Group animateGroup;
    private Group chestGroup;

    private AssetInfo assetInfo;
    private String zagDrivePath = "C:\\Users\\Zagak\\Google Drive\\CSC165\\DungeonCrawler\\assets\\";
    private String zuluDrivePath = "your path here";
    private String tjDrivePath = "your path here";
    private String googleDrivePath = zagDrivePath;

    // http://www.farmpeeps.com/fp_skyboxes.html
    private String terrainTexture = googleDrivePath + "SkyBoxes\\_countrypaths_1\\down.jpg";

    private BlendState rsBlendTransparent;

    //private IDisplaySystem display;
    private IDisplaySystem display;
    private IRenderer renderer;
    private ICamera cameraOne;
    private IInputManager im;
    private IEventManager eventManager;

    private IAudioManager audioManager;
    private Sound sound;
    private AudioResource resource1;

    private Camera3PController camOne;

    private GameServerTCP hostedServer;
    private GameClientTCP client;
    private boolean connected;


    private TileAsset tileAsset;
    private WallAsset wallAsset;
    private WallAsset columnAsset;
    private WallAsset doorAsset;
    private NPCEnemyAsset goblinAsset;
    private NPCEnemyAsset gobSword;

    public DungeonCrawler3DSoonTM()
    {
        super();
    }

    protected void initSystem()
    {
        display = new MyDisplaySystem(1920, 1080, 24, 20, false, "sage.renderer.jogl.JOGLRenderer");
        display.setTitle("DungeonCrawler");
        setDisplaySystem(display);

        //display = createDisplaySystem();
        //setDisplaySystem(display);
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

        //createPlayerHUDs();

        //initScriptEngines();

        associateDefaultKeyAndControllerBindings();
        camOne = new Camera3PController(cameraOne, avatarOne, im, kbName, "K");


        super.update(0f);
    }


    private void addObjectsToWorld() throws Exception
    {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

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

        // testing transparency
        ObjectInteractableAsset testBoxAsset = assetInfo.objectInteractables.get("transtest");
        SceneNode transparentBox = testBoxAsset
                .make(new Point3D(0, 0, 1), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        enableTransparency(transparentBox);
        addGameWorldObject(transparentBox);

        // testing transparency
        ObjectNonInteractableAsset treeAsset = assetInfo.objectNonInteractables.get("tree");
        for (int i = 0 ; i < 20 ; i++)
            makeTreeAtRandomLocation(treeAsset);





        Group room = constructRoom(0, 8, 12, 0, 0);
        addDoorToRoom(room,0,5,11);
        addEnemyToRoom(room,0,0,5);
        addEnemyToRoom(room,1,2,5);
        addEnemyToRoom(room,2,4,5);
        addGameWorldObject(room);


        // client parse
        // todo the below items

        // HP
        PlayableAsset hpAsset = assetInfo.playables.get("hp");
        PlayableAsset hpBar = assetInfo.playables.get("hpborder");
        Group hp1 = new Group("hp1");
        hp1.addChild(hpAsset.make(new Point3D(0,0,0), TILE_SCALE, ROT_E));
        SceneNode snhp1 = hpBar.make(new Point3D(0,0,0), TILE_SCALE, ROT_E);
        hp1.addChild(snhp1);
        addGameWorldObject(hp1);
        //snhp1.getLocalScale().setElementAt(0,0,0); figure out later


        // PLAYERS
        SceneNode cleric = assetInfo.playables.get("cleric")
                .make(new Point3D(0, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        addGameWorldObject(cleric);
        SceneNode wizard = assetInfo.playables.get("wizard")
                .make(new Point3D(2, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        addGameWorldObject(wizard);
        SceneNode rogue = assetInfo.playables.get("rogue")
                .make(new Point3D(4, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        addGameWorldObject(rogue);
        SceneNode fighter = assetInfo.playables.get("fighter")
                .make(new Point3D(6, 0, 0), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        addGameWorldObject(fighter);




        // camera
        avatarOne = cleric;
        cameraOne = new JOGLCamera(renderer);
        cameraOne.setPerspectiveFrustum(60, 2, .2, 1000);
        cameraOne.setViewport(0.0, 1.0, 0.0, 1);
        cameraOne.setLocation(new Point3D(0, 0, 0));
    }

    private void createPlayerHUDs()
    {
        timeStringPOne = new HUDString("Time = ");
        timeStringPOne.setLocation(0, 0.05);
        cameraOne.addToHUD(timeStringPOne);

        player1ID = new HUDString("Player 1: ");
        player1ID.setName("Player1ID");
        player1ID.setLocation(0, 0.10);
        player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
        player1ID.setColor(Color.WHITE);

        player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
        cameraOne.addToHUD(player1ID);
    }

    private void initWorldEnvironment()
    {
        skyBox = assetInfo.skyBoxes.get("countrypaths").make(200, 200, 200, false);
        addGameWorldObject(skyBox);

        theTerrain = initTerrain();
        background = new Group();
        background.addChild(theTerrain);
        addGameWorldObject(background);
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
        initPlayerOneControls();
    }


    private void initPlayerOneControls()
    {
        IAction quitActionPOne = (v, event) -> {
            if (hostedServer != null)
                try
                {
                    hostedServer.shutdown();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            setGameOver(true);
        };

        MoveForwardAction mvFwdActionPOne = new MoveForwardAction(cameraOne, (float) 0.1);
        MoveLeftAction mvLeftActionPOne = new MoveLeftAction(cameraOne, (float) 0.1);
        MoveRightAction mvRightActionPOne = new MoveRightAction(cameraOne, (float) 0.1);
        MoveBackAction mvBackActionPOne = new MoveBackAction(cameraOne, (float) 0.1);
        mvFwdActionPOne.setTerrain(theTerrain);
        mvBackActionPOne.setTerrain(theTerrain);
        mvLeftActionPOne.setTerrain(theTerrain);
        mvRightActionPOne.setTerrain(theTerrain);
        mvFwdActionPOne.setTerrainFollow(true);
        mvBackActionPOne.setTerrainFollow(true);
        mvLeftActionPOne.setTerrainFollow(true);
        mvRightActionPOne.setTerrainFollow(true);

        PitchUpAction pitchUpActionPOne = new PitchUpAction(cameraOne, (float) 0.1);
        PitchDownAction pitchDownActionPOne = new PitchDownAction(cameraOne, (float) 0.1);
        RollRightAction rollRightActionPOne = new RollRightAction(cameraOne, (float) 10);
        RollLeftAction rollLeftActionPOne = new RollLeftAction(cameraOne, (float) 10);
        YawRightAction yawRightActionPOne = new YawRightAction(cameraOne, (float) 0.1);
        YawLeftAction yawLeftActionPOne = new YawLeftAction(cameraOne, (float) 0.1);

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
                           quitActionPOne, ATpressOnly);

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


        //TODO: Note - Roll should be mapped to Left/Right and Yaw should be Q/E to match cockpit controls

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

        //PITCH -- TODO: Pitch commands should be switched to match actual cockpit controls
        //ROTATE UP
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.UP,
                           pitchUpActionPOne,ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.UP,
                           pitchUpActionPOne,ATrepeat);

        //ROTATE DOWN
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.DOWN,
                           pitchDownActionPOne, ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.DOWN,
                           pitchDownActionPOne,ATrepeat);

        //ROLL
        //ROTATE COUNTERCLOCKWISE
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.LEFT,
                           rollRightActionPOne,ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.LEFT,
                           rollRightActionPOne,ATrepeat);

        //ROTATE CLOCKWISE
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.RIGHT,
                           rollLeftActionPOne, ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.RIGHT,
                           rollLeftActionPOne,ATrepeat);

        //ZOOM IN
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.Z,
                           rollLeftActionPOne,ATpressOnly);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.Z,
                           rollLeftActionPOne,ATrepeat);

        //ZOOM OUT
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.C,
                           rollLeftActionPOne,ATpressOnly);
        im.associateAction(kbName,net.java.games.input.Component.Identifier.Key.C,
                           rollLeftActionPOne,ATrepeat);

        // OPEN CHEST
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

    private void updateSkybox()
    {
        Point3D camLoc1 = cameraOne.getLocation();
        Matrix3D camTrans = new Matrix3D();
        camTrans.translate(camLoc1.getX(), camLoc1.getY(), camLoc1.getZ());
        skyBox.setLocalTranslation(camTrans);
    }

    public void update(float elapsedTimeMS)
    {
        //scoreString.setText("Score = " + score);
        time += elapsedTimeMS;
        //DecimalFormat df = new DecimalFormat("0.0");
        //timeStringPOne.setText("Time = " + df.format(time / 1000));

        updateAnimations(elapsedTimeMS);


        mapBoundaryFix(avatarOne); //

        updateSkybox();
        camOne.update(elapsedTimeMS);
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
        renderer.setCamera(cameraOne);
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


    // ### TJ Networking
    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    public Point3D getPlayerPosition()
    {
        // TODO validate and test
        return new Point3D(avatarOne.getLocalTranslation().getCol(3));
    }

    public void startScrolling()
    {
        // TODO random stub method
    }

    public void removeGameWorldObject(MoveToDoghouseEvent.GhostAvatar ghost)
    {
        // TODO list of ghostAvatars
    }

    public void textureObj(MoveToDoghouseEvent.GhostAvatar ghost, String s)
    {
        // TODO
    }
    // ### TJ

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
}