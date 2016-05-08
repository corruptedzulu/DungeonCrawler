package a2;

import a2.newdc.GhostAvatar;
import a2.newdc.ParticleSystemEngine;
import a2.newdc.assets.AssetInfo;
import a2.newdc.assets.ObjectInteractableAsset;
import a2.newdc.assets.ObjectNonInteractableAsset;
import a2.newdc.assets.TileAsset;

import myGameEngine.GameClientTCP;
import myGameEngine.GameServerTCP;
import myGameEngine.Camera3PController;
import myGameEngine.MoveBackAction;
import myGameEngine.MoveForwardAction;
import myGameEngine.MoveLeftAction;
import myGameEngine.MoveRightAction;
import myGameEngine.MoveUpAction;
import myGameEngine.MyDisplaySystem;
import myGameEngine.PitchDownAction;
import myGameEngine.PitchUpAction;
import myGameEngine.YawLeftAction;
import myGameEngine.YawRightAction;

import net.java.games.input.*;
import net.java.games.input.Event;
import sage.app.BaseGame;
import sage.display.*;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.networking.IGameConnection;
<<<<<<< HEAD
import sage.scene.*;
=======
import sage.physics.IPhysicsEngine;
import sage.physics.PhysicsEngineFactory;
import sage.scene.SceneNode;
import sage.scene.SkyBox;
>>>>>>> refs/remotes/origin/master
import sage.scene.shape.*;
import sage.input.*;
import sage.input.action.*;
import sage.renderer.IRenderer;
import sage.camera.*;
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
    private float time;
    private HUDString player1ID;
    private HUDString timeStringPOne;
    private Random random;
    private SceneNode avatarOne;
    private String kbName, gpName;
    private ArrayList<SceneNode> gameworld;

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


    //private IDisplaySystem display;
    private IDisplaySystem display;
    private IRenderer renderer;
    private ICamera cameraOne;
    private IInputManager im;
    private IEventManager eventManager;

    private Camera3PController camOne;

    private GameServerTCP hostedServer;
    private GameClientTCP client;
<<<<<<< HEAD
    private boolean connected;
=======
    private ParticleSystemEngine particleSystemEngine;
    private ParticleSystemEngine.ParticleSystem ps;
>>>>>>> refs/remotes/origin/master

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

    protected void initGame()
    {
        display.setTitle("DungeonCrawler");

        renderer = display.getRenderer();

        assetInfo = new AssetInfo(googleDrivePath);

        try
        {
            createPlayers();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        createPlayerHUDs();
        initEnvironment();
        //initScriptEngines();
        initGameObjects();
        associateDefaultKeyAndControllerBindings();


        random = new Random();
        String engine = "sage.physics.JBullet.JBulletPhysicsEngine";
        IPhysicsEngine physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
        physicsEngine.initSystem();
        particleSystemEngine = new ParticleSystemEngine(assetInfo, physicsEngine,gameworld,random);


        ps = particleSystemEngine.getSystem("Test",5,1,10000);
        ps.start(new Point3D(0,50,0));

        camOne = new Camera3PController(cameraOne, avatarOne, im, kbName, "K");
        client.sendJoinMessage();

        super.update((float) 0.0);
    }

    protected void initSystem()
    {
        display = createDisplaySystem();
        setDisplaySystem(display);

        im = new InputManager();
        setInputManager(im);

        gameworld = new ArrayList<SceneNode>();
        setGameWorld(gameworld);

        //super.initSystem();
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


    private void createPlayers() throws Exception
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


<<<<<<< HEAD

        // animation objects
        animateGroup = new Group("animateGroup");

        ObjectInteractableAsset chest = assetInfo.objectInteractables.get("chest");
        chestGroup = chest
                .makeAni(new Point3D(0, 0, -2), new Point3D(.01, .01, .01), new Quaternion(1, new double[]{0, 0, 0}));
        animateGroup.addChild(chestGroup);


        addGameWorldObject(animateGroup);

=======
        if (false) // throw errors
        for (int i = 0; i < 40; i+=2)
            for (int j =0; j < 40; j+=2)
                if (true) // heap
                    addGameWorldObject(tile.make(new Point3D(i, 0, j), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
                else // GC space
                    addGameWorldObject(barrel.make(new Point3D(i, 0, j), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(0, 0, 2), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(0, 0, 4), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(0, 0, 6), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(2, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(4, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(6, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(6, 0, 2), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
>>>>>>> refs/remotes/origin/master


        TileAsset tile = assetInfo.tiles.get("tile");
        tile.setRandomTexture(true);
        addGameWorldObject(tile.make(new Point3D(0, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(0, 0, 2), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(0, 0, 4), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(0, 0, 6), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(2, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(4, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(6, 0, 0), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));
        addGameWorldObject(tile.make(new Point3D(6, 0, 2), new Point3D(.95, .95, .95), new Quaternion(1, new double[]{0, 0, 0})));

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

        player1ID = new HUDString("Player 1: ");// + scorePOne);
        player1ID.setName("Player1ID");
        player1ID.setLocation(0, 0.10);
        player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
        player1ID.setColor(Color.red);
        player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
        cameraOne.addToHUD(player1ID);
    }

    private void initEnvironment()
    {
        eventManager = EventManager.getInstance();
        random = new Random();
    }

    private void initGameObjects()
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
        HillHeightMap myHillHeightMap = new HillHeightMap(129, 2000, 5.0f, 20.0f, (byte) 2, 12345);
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

    private static TerrainBlock createTerBlock(AbstractHeightMap heightMap)
    {
        float heightScale = 0.05f;
        Vector3D terrainScale = new Vector3D(2, heightScale, 2);
        int terrainSize = heightMap.getSize();
        float cornerHeight = heightMap.getTrueHeightAtPoint(0, 0) * heightScale;
        Point3D terrainOrigin = new Point3D(0, -cornerHeight, 0);
        String name = "Terrain:" + heightMap.getClass().getSimpleName();
        return new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);
    }

    private void initWorldAxes()
    {
        Point3D origin = new Point3D(0, 0, 0);
        Point3D xEnd = new Point3D(100, 0, 0);
        Point3D yEnd = new Point3D(0, 100, 0);
        Point3D zEnd = new Point3D(0, 0, 100);
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

        //NOTE: This is attached to mvFwdAction.
        //The Fwd/Backward need to the on the same object when attached to an AXIS
        //The button ones should be too, but I'm not going worry about that yet

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

        //MOVE UP
        //im.associateAction(kbName,
        //                   net.java.games.input.Component.Identifier.Key.SPACE,
        //                   mvUpActionPOne,
        //                   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        //im.associateAction(kbName,
        //                   net.java.games.input.Component.Identifier.Key.SPACE,
        //                   mvUpActionPOne,
        //                   IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //		//MOVE DOWN
        //		im.associateAction(kbName,
        //				net.java.games.input.Component.Identifier.Key.F,
        //				mvDownAction,
        //				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        //		im.associateAction(kbName,
        //				net.java.games.input.Component.Identifier.Key.F,
        //				mvDownAction,
        //				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);


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

<<<<<<< HEAD
        // OPEN CHEST
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.G,
=======
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.P,
>>>>>>> refs/remotes/origin/master
                           new AbstractInputAction()
                           {
                               public void performAction(float v, Event event)
                               {
<<<<<<< HEAD
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



=======
                                   Vector3D v3 = avatarOne.getWorldTranslation().getCol(3);
                                   Point3D pos = new Point3D(v3.getX(),v3.getY(),v3.getZ());
                                   System.out.println(pos);
                                   ps.start(pos);
                               }
                           },
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
>>>>>>> refs/remotes/origin/master
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
        DecimalFormat df = new DecimalFormat("0.0");
        timeStringPOne.setText("Time = " + df.format(time / 1000));

        updateAnimations(elapsedTimeMS);

        updateSkybox();
        ps.update(elapsedTimeMS);
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
