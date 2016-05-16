package a2;

import a2.newdc.GhostAvatar;
import a2.newdc.assets.*;

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
import java.text.DecimalFormat;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class DungeonCrawler3DSoonTM extends BaseGame //implements MouseListener
{
    private final double TILE_SIZE = 2.0;
    private final double TS = TILE_SIZE;
    private final double TILE_HALF_SIZE = TILE_SIZE * .5f;
    private final Point3D TILE_SCALE = new Point3D(1.2,1.2,1.2); // 1.32
    private final Quaternion TILE_ROT = new Quaternion();

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

    private Camera3PController camOne;

    private GameServerTCP hostedServer;
    private GameClientTCP client;
    private boolean connected;
    ;

    public DungeonCrawler3DSoonTM()
    {
        super();
        hostedServer = null;
    }

    public DungeonCrawler3DSoonTM(InetAddress address, int port) throws IOException
    {
        super();
        client = new GameClientTCP(address, port, IGameConnection.ProtocolType.TCP, this);

    }

    public DungeonCrawler3DSoonTM(InetAddress address, int port, GameServerTCP server) throws IOException
    {
        super();
        hostedServer = server;
        client = new GameClientTCP(address, port, IGameConnection.ProtocolType.TCP, this);
        client.sendJoinMessage();
    }

    protected void initSystem()
    {
        display = createDisplaySystem();
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
        display.setTitle("DungeonCrawler");

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
        client.sendJoinMessage();

        super.update((float) 0.0);
    }



    private IDisplaySystem createDisplaySystem()
    {
        IDisplaySystem display = new MyDisplaySystem(1280, 720, 24, 20, false, "sage.renderer.jogl.JOGLRenderer");
        System.out.print("\nWaiting for display creation...");
        int count = 0;
        // wait until display creation completes or a timeout occurs
        while (!display.isCreated())
        {
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException("Display creation interrupted");
            }

            count++;
            System.out.print("+");

            if (count % 80 == 0)
            {
                System.out.println();
            }
            if (count > 2000) // 20 seconds (approx.)
            {
                throw new RuntimeException("Unable to create display");
            }
        }
        System.out.println();
        return display;
    }


    private void addObjectsToWorld() throws Exception
    {
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));

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
        SceneNode tree = treeAsset
                .make(new Point3D(0, 0, 1), new Point3D(1, 1, 1), new Quaternion(1, new double[]{0, 0, 0}));
        enableTransparency(tree);
        addGameWorldObject(tree);

        for (int i = 0 ; i < 20 ; i++)
            makeTreeAtRandomLocation(treeAsset);


        TileAsset tile = assetInfo.tiles.get("tile");
        tile.setRandomTexture(true);
        addGameWorldObject(tile.make(new Point3D(0, 0, 0), TILE_SCALE, TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(0, 0, TS), TILE_SCALE, TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(0, 0, TS*2), TILE_SCALE, TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(0, 0, TS*3), TILE_SCALE,TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(TS, 0, 0), TILE_SCALE, TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(TS*2, 0, 0), TILE_SCALE, TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(TS*3, 0, 0), TILE_SCALE, TILE_ROT));
        addGameWorldObject(tile.make(new Point3D(TS*3, 0, TS*2), TILE_SCALE, TILE_ROT));

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



        //MoveUpAction mvUpActionPOne = new MoveUpAction(cameraOne, (float) 0.1);
        //MoveDownAction mvDownActionPOne = new MoveDownAction(cameraOne, (float) 0.1);

        PitchUpAction pitchUpActionPOne = new PitchUpAction(cameraOne, (float) 0.1);
        PitchDownAction pitchDownActionPOne = new PitchDownAction(cameraOne, (float) 0.1);
        RollRightAction rollRightActionPOne = new RollRightAction(cameraOne, (float) 0.1);
        RollLeftAction rollLeftActionPOne = new RollLeftAction(cameraOne, (float) 0.1);
        YawRightAction yawRightActionPOne = new YawRightAction(cameraOne, (float) 0.1);
        YawLeftAction yawLeftActionPOne = new YawLeftAction(cameraOne, (float) 0.1);


        mvFwdActionPOne.setAvatar(avatarOne);
        mvBackActionPOne.setAvatar(avatarOne);
        mvLeftActionPOne.setAvatar(avatarOne);
        mvRightActionPOne.setAvatar(avatarOne);

        yawLeftActionPOne.setAvatar(avatarOne);
        yawRightActionPOne.setAvatar(avatarOne);

        //TODO: Switch all of the actions over to the correct objects

        //QUIT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.ESCAPE,
                           quitActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

        //MOVE FORWARD
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.W,
                           mvFwdActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.W,
                           mvFwdActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //MOVE BACKWARD
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.S,
                           mvBackActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.S,
                           mvBackActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);



        //MOVE LEFT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.A,
                           mvLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.A,
                           mvLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


        //MOVE RIGHT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.D,
                           mvRightActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.D,
                           mvRightActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);




        //TODO: Note - Roll should be mapped to Left/Right and Yaw should be Q/E to match cockpit controls

        //YAW
        //ROTATE LEFT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.Q,
                           yawLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.Q,
                           yawLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROTATE RIGHT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.E,
                           yawRightActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.E,
                           yawRightActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //PITCH -- TODO: Pitch commands should be switched to match actual cockpit controls
        //ROTATE UP
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.UP,
                           pitchUpActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.UP,
                           pitchUpActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROTATE DOWN
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.DOWN,
                           pitchDownActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.DOWN,
                           pitchDownActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROLL
        //ROTATE COUNTERCLOCKWISE
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.LEFT,
                           rollRightActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.LEFT,
                           rollRightActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROTATE CLOCKWISE
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.RIGHT,
                           rollLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.RIGHT,
                           rollLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


        //ZOOM IN
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.Z,
                           rollLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.Z,
                           rollLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


        //ZOOM OUT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.C,
                           rollLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.C,
                           rollLeftActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        // OPEN CHEST
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.G,
                           new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
                                   Iterator<SceneNode> itr = chestGroup.getChildren();
                                   while (itr.hasNext())
                                   {
                                       Model3DTriMesh mesh = ((Model3DTriMesh)itr.next());
                                       if (mesh.hasAnimations())
                                       {
                                           mesh.startAnimation("my_animation");
                                       }
                                   }
                               }
                           },
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);



    }

    private void updateSkybox()
    {
        Point3D camLoc1 = cameraOne.getLocation();
        Matrix3D camTrans = new Matrix3D();
        camTrans.translate(camLoc1.getX(), camLoc1.getY(), camLoc1.getZ());
        skyBox.setLocalTranslation(camTrans);
        // not 2
    }

    public void update(float elapsedTimeMS)
    {
        //scoreString.setText("Score = " + score);
        time += elapsedTimeMS;
        //DecimalFormat df = new DecimalFormat("0.0");
        //timeStringPOne.setText("Time = " + df.format(time / 1000));

        updateAnimations(elapsedTimeMS);

        //
        mapBoundaryFix(avatarOne);

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

    public void removeGameWorldObject(GhostAvatar ghost)
    {
        // TODO list of ghostAvatars
    }

    public void textureObj(GhostAvatar ghost, String s)
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
}
