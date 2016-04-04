package a2;

import a2.newdc.GhostAvatar;
import sage.app.BaseGame;

import sage.display.*;
import sage.event.EventManager;
import sage.event.IEventManager;
import sage.scene.SceneNode;
import sage.scene.SkyBox;
import sage.scene.bounding.BoundingBox;
import sage.scene.shape.*;
import sage.scene.Group;
import sage.scene.HUDString;

import sage.input.*;
import sage.input.action.*;
import sage.renderer.IRenderer;
import sage.camera.*;
import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import myGameEngine.Camera3PController;
import myGameEngine.MoveBackAction;
import myGameEngine.MoveForwardAction;
import myGameEngine.MoveLeftAction;
import myGameEngine.MoveRightAction;
import myGameEngine.MoveDownAction;
import myGameEngine.MoveUpAction;
import myGameEngine.MyDisplaySystem;
import myGameEngine.PitchDownAction;
import myGameEngine.PitchUpAction;
import myGameEngine.QuitGameAction;
import myGameEngine.YawLeftAction;
import myGameEngine.YawRightAction;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.Color;
import java.text.DecimalFormat;

import sage.scene.state.RenderState;
import sage.scene.state.TextureState;
import sage.terrain.TerrainBlock;
import sage.terrain.AbstractHeightMap;
import sage.terrain.HillHeightMap;
import sage.texture.Texture;
import sage.texture.TextureManager;

public class DungeonCrawler3DSoonTM extends BaseGame //implements MouseListener
{
    private int scorePOne = 0;
    private int scorePTwo = 0;
    private float time;
    private HUDString player1ID;
    private HUDString player2ID;
    private HUDString timeStringPOne;
    private HUDString timeStringPTwo;
    private int numCrashes = 0;
    private int numStandardShapes = 10;
    private Random random;
    private Doghouse doghouse;
    private MyPyramid avatarOne;
    private Cylinder avatarTwo;
    //private Sphere avatarOne;
    //private Sphere avatarTwo;
    private PyramidGroup pyramids;
    private RotationController rotational;
    //private IDisplaySystem display;

    private String kbName, gpName;

    private int maxXDistanceAbsolute = 10;
    private int maxYDistanceAbsolute = 10;
    private int maxZDistanceAbsolute = 10;

    // ### TJ
    private SkyBox sky;
    private TerrainBlock theTerrain;
    private Group background;
    private String texFolder;

    // TODO next to adjust path here else it doesn't run!
    // http://www.farmpeeps.com/fp_skyboxes.html
    private String groundTexturePath = "C:\\Users\\Zagak\\Google Drive\\CSC165\\DungeonCrawler\\assets\\Skyboxs\\farmpeeps_skybox_countrypaths\\skybox_country_paths_bottom.jpg";
    private String skyboxTexturePath = "C:\\Users\\Zagak\\Google Drive\\CSC165\\DungeonCrawler\\assets\\Skyboxs\\farmpeeps_skybox_countrypaths\\skybox_country_paths_top.jpg";
    //private String groundTexturePath = texFolder + File.separator + "redrock.jpg";
    //private String skyboxTexturePath = texFolder + File.separator + "space.jpg";

    private boolean connected;
    // ### TJ

    IDisplaySystem display;
    IRenderer renderer;
    ICamera cameraOne, cameraTwo;
    IInputManager im;
    IEventManager eventManager;

    Camera3PController camOne, camTwo;

    protected void initGame()
    {
        //test f = new test();
        //f.listControllers();

        //display = getDisplaySystem();
        display.setTitle("Dog Catcher!");

        renderer = display.getRenderer();

        createPlayers();
        createPlayerHUDs();
        initEnvironment();
        initGameObjects();
        associateDefaultKeyAndControllerBindings();

        camOne = new Camera3PController(cameraOne, avatarOne, im, kbName, "K");

        if (gpName != null)
        {
            camTwo = new Camera3PController(cameraTwo, avatarTwo, im, gpName, "G");
        }

        super.update((float) 0.0);
    }

    protected void initSystem()
    {
        //call a local method to create a DisplaySystem object
        display = createDisplaySystem();
        setDisplaySystem(display);
        //create an Input Manager
        im = new InputManager();
        setInputManager(im);
        //create an (empty) gameworld
        ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>();
        setGameWorld(gameWorld);
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


    private void createPlayers()
    {
        avatarOne = new MyPyramid("PLAYER1");
        //avatarOne = new Sphere();
        avatarOne.translate(0, 1, 50);
        avatarOne.rotate(180, new Vector3D(0, 1, 0));
        addGameWorldObject(avatarOne);
        cameraOne = new JOGLCamera(renderer);
        cameraOne.setPerspectiveFrustum(60, 2, 1, 1000);
        cameraOne.setViewport(0.0, 1.0, 0.0, 0.45);
        cameraOne.setLocation(new Point3D(0, 1, 50));

        avatarTwo = new Cylinder("PLAYER2");
        //avatarTwo = new Sphere();
        avatarTwo.translate(50, 1, 0);
        avatarTwo.rotate(-90, new Vector3D(0, 1, 0));
        addGameWorldObject(avatarTwo);
        cameraTwo = new JOGLCamera(renderer);
        cameraTwo.setPerspectiveFrustum(60, 2, 1, 1000);
        cameraTwo.setViewport(0.0, 1.0, 0.55, 1.0);
        cameraTwo.setLocation(new Point3D(50, 1, 0));

        createPlayerHUDs();
    }

    private void createPlayerHUDs()
    {
        timeStringPOne = new HUDString("Time = ");
        timeStringPOne.setLocation(0, 0.05);
        cameraOne.addToHUD(timeStringPOne);

        timeStringPTwo = new HUDString("Time = ");
        timeStringPTwo.setLocation(0, 0.05);
        cameraTwo.addToHUD(timeStringPTwo);

        //scoreStringOne = new HUDString("Score = " + score);
        //addGameWorldObject(scoreStringOne);

        //scoreStringOne = new HUDString("0");
        //scoreStringTwo = new HUDString("0");

        player1ID = new HUDString("Player 1: ");// + scorePOne);
        player1ID.setName("Player1ID");
        player1ID.setLocation(0, 0.10);
        player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
        player1ID.setColor(Color.red);
        player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
        cameraOne.addToHUD(player1ID);

        player2ID = new HUDString("Player 2: ");// + scorePTwo);
        player2ID.setName("Player2ID");
        player2ID.setLocation(0, 0.10);
        player2ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
        player2ID.setColor(Color.yellow);
        player2ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
        cameraTwo.addToHUD(player2ID);
    }

    private void initEnvironment()
    {
        eventManager = EventManager.getInstance();
        //display.addMouseListener(this);
        random = new Random();
    }

    private void initGameObjects()
    {
        // ### TJ skybox
        sky = new SkyBox();
        Texture skyBoxTexture = TextureManager.loadTexture2D(skyboxTexturePath);
        sky.setTexture(SkyBox.Face.Up, skyBoxTexture);
        sky.setTexture(SkyBox.Face.Down, skyBoxTexture);
        sky.setTexture(SkyBox.Face.North, skyBoxTexture);
        sky.setTexture(SkyBox.Face.South, skyBoxTexture);
        sky.setTexture(SkyBox.Face.East, skyBoxTexture);
        sky.setTexture(SkyBox.Face.West, skyBoxTexture);
        addGameWorldObject(sky);

        theTerrain = initTerrain();
        theTerrain.scale(2, 1, 2);
        background = new Group();
        background.addChild(theTerrain);

        addGameWorldObject(background);
        /// ### TJ end skybox

        Dog dg1 = new Dog();
        Matrix3D dg1M = dg1.getLocalTranslation();
        dg1M.translate(10, 0, 10);
        dg1.setLocalTranslation(dg1M);
        Point3D center = new Point3D(0, -0.5, 0.5);
        BoundingBox bb = new BoundingBox(center, (float) 4.0, (float) 7.25, (float) 9.0);
        dg1.setLocalBound(bb);

        Matrix3D scale = dg1.getLocalScale();
        int rand = random.nextInt(5);
        rand += 3;
        double scaleFactor = (double) rand / 10;
        //double scaleFactor = (random.nextInt(5) + 1) / 10;
        scale.scale(scaleFactor, scaleFactor, scaleFactor);
        //dg1.setLocalScale(scale);
        //dg1.setShowBound(true);

        addGameWorldObject(dg1);
        dg1.updateWorldBound();

        doghouse = new Doghouse();
        Matrix3D dg2M = doghouse.getLocalTranslation();
        dg2M.translate(0, 0, 0);
        doghouse.setLocalTranslation(dg2M);
        addGameWorldObject(doghouse);
        doghouse.updateWorldBound();

        DoghouseColorController colorController = new DoghouseColorController();
        colorController.addControlledNode(doghouse);
        doghouse.addController(colorController);

        //code goes here to insert other gameworld objects, axes
        //avatarOne = new MyPyramid();
        //Matrix3D pyrT = avatarOne.getLocalTranslation();
        //pyrT.translate(2,0,2);
        //avatarOne.setLocalTranslation(pyrT);
        //Matrix3D pyrR = new Matrix3D();
        //pyrR.rotateY(45.0); // rotation is the direction avatar faces
        //avatarOne.setLocalRotation(pyrR);
        //addGameWorldObject(avatarOne);


        initWorldBoundaries();
        initWorldAxes();
        initStandardShapes();

        eventManager.addListener(doghouse, MoveToDoghouseEvent.class);

        //eventManager.addListener(object, eventClass)
    }

    private void initWorldBoundaries()
    {
        // TODO Auto-generated method stub

        Rectangle floor = new Rectangle(200, 200);
        Matrix3D flr = new Matrix3D();
        flr.rotate(90, 0, 0);
        floor.setLocalRotation(flr);

        floor.translate(0, 0, 0);
        floor.setColor(Color.GRAY);
        addGameWorldObject(floor);
    }

    // ### TJ terrain
    private static TerrainBlock createTerBlock(AbstractHeightMap heightMap)
    {
        float heightScale = 0.05f;
        Vector3D terrainScale = new Vector3D(1, heightScale, 1);

        // use the size of the height map as the size of the terrain
        int terrainSize = heightMap.getSize();

        // specify terrain origin so heightmap (0,0) is at world origin
        float cornerHeight =
                heightMap.getTrueHeightAtPoint(0, 0) * heightScale;
        Point3D terrainOrigin = new Point3D(-64.5, -cornerHeight, -64.5);

        // create a terrain block using the height map
        String name = "Terrain:" + heightMap.getClass().getSimpleName();
        TerrainBlock tb = new TerrainBlock(name, terrainSize, terrainScale, heightMap.getHeightData(), terrainOrigin);

        return tb;
    }

    private TerrainBlock initTerrain()
    {
        // create height map and terrain block
        HillHeightMap myHillHeightMap =
                new HillHeightMap(129, 2000, 5.0f, 20.0f, (byte) 2, 12345);
        myHillHeightMap.setHeightScale(0.1f);
        TerrainBlock hillTerrain = createTerBlock(myHillHeightMap);

        // create texture and texture state to color the terrain
        TextureState groundState;
        Texture groundTexture = TextureManager.loadTexture2D(groundTexturePath);

        groundTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
        groundState = (TextureState) display.getRenderer().createRenderState(RenderState.RenderStateType.Texture);
        groundState.setTexture(groundTexture, 0);
        groundState.setEnabled(true);

        // apply the texture to the terrain
        hillTerrain.setRenderState(groundState);
        return hillTerrain;
    }
    // ### TJ end terrain

    private void initStandardShapes()
    {
        for (int x = 0; x < numStandardShapes; x++)
        {
            MySphere sq1 = new MySphere();
            Matrix3D sq1M = sq1.getLocalTranslation();

            int tempX, tempY, tempZ;

            //put an 8 spread away from the center. keeps them from overlapping the doghouse
            if (random.nextBoolean())
            {
                tempX = random.nextInt(maxXDistanceAbsolute + 15);
            } else
            {
                tempX = -(random.nextInt(maxXDistanceAbsolute + 15));
            }

            //tempY = 0;

            //if(random.nextBoolean())
            //{
            tempY = random.nextInt(2);
            //}
            //else
            //{
            //tempY = -(random.nextInt(maxXDistanceAbsolute + 15));
            //}

            if (random.nextBoolean())
            {
                tempZ = random.nextInt(maxXDistanceAbsolute + 15);
            } else
            {
                tempZ = -(random.nextInt(maxXDistanceAbsolute + 15));
            }

            sq1M.translate(tempX, tempY, tempZ);
            sq1.scale(random.nextFloat() + 1, random.nextFloat() + 1, random.nextFloat() + 1);
            sq1.setLocalTranslation(sq1M);
            addGameWorldObject(sq1);
            sq1.setColor(Color.WHITE);
            sq1.updateLocalBound();
            sq1.updateWorldBound();

            //eventManager.addListener(sq1, CrashEvent.class);
        }


        pyramids = new PyramidGroup();
        addGameWorldObject(pyramids);
        rotational = new RotationController();
        rotational.addControlledNode(pyramids);
        pyramids.addController(rotational);

        for (int x = 0; x < numStandardShapes; x++)
        {
            MyPyramid sq1 = new MyPyramid();
            Matrix3D sq1M = sq1.getLocalTranslation();

            int tempX, tempY, tempZ;


            //put an 8 spread away from the center. keeps them from overlapping the doghouse
            if (random.nextBoolean())
            {
                tempX = random.nextInt(maxXDistanceAbsolute + 8);
            } else
            {
                tempX = -(random.nextInt(maxXDistanceAbsolute + 8));
            }

            tempY = random.nextInt(2);
            //			if(random.nextBoolean())
            //			{
            //				tempY = random.nextInt(maxXDistanceAbsolute + 8);
            //			}
            //			else
            //			{
            //				tempY = -(random.nextInt(maxXDistanceAbsolute + 8));
            //			}

            if (random.nextBoolean())
            {
                tempZ = random.nextInt(maxXDistanceAbsolute + 8);
            } else
            {
                tempZ = -(random.nextInt(maxXDistanceAbsolute + 8));
            }


            sq1M.translate(tempX, tempY, tempZ);
            sq1.scale(random.nextFloat() + 1, random.nextFloat() + 1, random.nextFloat() + 1);
            sq1.setLocalTranslation(sq1M);
            pyramids.addChild(sq1);
            //addGameWorldObject(sq1);
            //float[] buff = new float[] {0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,
            //							0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1,
            //						0,0,0,1, 0,0,0,1, 0,0,0,1, 0,0,0,1};
            //FloatBuffer clr = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(buff);
            //sq1.setColorBuffer(clr);
            //sq1.setColor(Color.WHITE);
            sq1.updateLocalBound();
            sq1.updateWorldBound();

            //eventManager.addListener(sq1, CrashEvent.class);
        }

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

        //Controller c = new Controller();
        if (gpName == null && im.getControllers().size() > 2)
        {
            gpName = im.getControllers().get(2).getName();
            //gpName = im.getGamepadController(2).getName();
        }

        kbName = im.getKeyboardName();

        initPlayerOneControls();

        if (gpName != null)
        {
            initPlayerTwoControls();
        }
    }


    private void initPlayerOneControls()
    {
        QuitGameAction quitActionPOne = new QuitGameAction(this);

        MoveForwardAction mvFwdActionPOne = new MoveForwardAction(cameraOne, (float) 0.1);
        MoveLeftAction mvLeftActionPOne = new MoveLeftAction(cameraOne, (float) 0.1);
        MoveRightAction mvRightActionPOne = new MoveRightAction(cameraOne, (float) 0.1);
        MoveBackAction mvBackActionPOne = new MoveBackAction(cameraOne, (float) 0.1);
        MoveUpAction mvUpActionPOne = new MoveUpAction(cameraOne, (float) 0.1);
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
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.SPACE,
                           mvUpActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.SPACE,
                           mvUpActionPOne,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

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


        //		//ZOOM IN
        //		im.associateAction(kbName,
        //				net.java.games.input.Component.Identifier.Key.Z,
        //				rollLeftActionPOne,
        //				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        //		im.associateAction(kbName,
        //				net.java.games.input.Component.Identifier.Key.Z,
        //				rollLeftActionPOne,
        //				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        //
        //
        //		//ZOOM OUT
        //		im.associateAction(kbName,
        //				net.java.games.input.Component.Identifier.Key.C,
        //				rollLeftActionPOne,
        //				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
        //		im.associateAction(kbName,
        //				net.java.games.input.Component.Identifier.Key.C,
        //				rollLeftActionPOne,
        //				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

    }

    private void initPlayerTwoControls()
    {
        QuitGameAction quitActionPTwo = new QuitGameAction(this);

        MoveForwardAction mvFwdActionPTwo = new MoveForwardAction(cameraTwo, (float) 0.1);
        MoveLeftAction mvLeftActionPTwo = new MoveLeftAction(cameraTwo, (float) 0.1);
        MoveRightAction mvRightActionPTwo = new MoveRightAction(cameraTwo, (float) 0.1);
        MoveBackAction mvBackActionPTwo = new MoveBackAction(cameraTwo, (float) 0.1);
        //MoveUpAction mvUpActionPTwo = new MoveUpAction(cameraTwo, (float) 0.1);
        //MoveDownAction mvDownActionPTwo = new MoveDownAction(cameraTwo, (float) 0.1);

        PitchUpAction pitchUpActionPTwo = new PitchUpAction(cameraTwo, (float) 0.1);
        //PitchDownAction pitchDownActionPTwo = new PitchDownAction(cameraTwo, (float) 0.1);
        //RollRightAction rollRightActionPTwo = new RollRightAction(cameraTwo, (float) 0.1);
        //RollLeftAction rollLeftActionPTwo = new RollLeftAction(cameraTwo, (float) 0.1);
        YawRightAction yawRightActionPTwo = new YawRightAction(cameraTwo, (float) 0.1);
        YawLeftAction yawLeftActionPTwo = new YawLeftAction(cameraTwo, (float) 0.1);


        mvFwdActionPTwo.setAvatar(avatarTwo);
        mvBackActionPTwo.setAvatar(avatarTwo);
        mvLeftActionPTwo.setAvatar(avatarTwo);
        mvRightActionPTwo.setAvatar(avatarTwo);

        yawLeftActionPTwo.setAvatar(avatarTwo);
        yawRightActionPTwo.setAvatar(avatarTwo);

        //TODO: Switch all of the actions over to the correct objects


        //QUIT
        im.associateAction(kbName,
                           net.java.games.input.Component.Identifier.Key.ESCAPE,
                           quitActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

        //MOVE FORWARD
        im.associateAction(gpName,
                           net.java.games.input.Component.Identifier.Axis.Y,
                           mvFwdActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //MOVE BACKWARD
        //		im.associateAction(gpName,
        //			net.java.games.input.Component.Identifier.Axis.Y,
        //			mvFwdActionPTwo,
        //			IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //MOVE LEFT
        //		im.associateAction(gpName,
        //			net.java.games.input.Component.Identifier.Axis.X,
        //			mvRightActionPTwo,
        //			IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
        //
        //MOVE RIGHT
        im.associateAction(gpName,
                           net.java.games.input.Component.Identifier.Axis.X,
                           mvRightActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //MOVE UP

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
        im.associateAction(gpName,
                           net.java.games.input.Component.Identifier.Axis.RX,
                           yawRightActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROTATE RIGHT
        im.associateAction(gpName,
                           net.java.games.input.Component.Identifier.Axis.RX,
                           yawRightActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //PITCH -- TODO: Pitch commands should be switched to match actual cockpit controls
        //ROTATE UP
        im.associateAction(gpName,
                           net.java.games.input.Component.Identifier.Axis.RY,
                           pitchUpActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROTATE DOWN
        im.associateAction(gpName,
                           net.java.games.input.Component.Identifier.Axis.RY,
                           pitchUpActionPTwo,
                           IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

        //ROLL
        //ROTATE COUNTERCLOCKWISE

        //ROTATE CLOCKWISE

    }

    public void update(float elapsedTimeMS)
    {
        ArrayList<SceneNode> toRemove = new ArrayList<>();

        for (SceneNode s : getGameWorld())
        {

            if (s instanceof Dog || s instanceof MySquare || s instanceof MySphere)
            {
                //s.getWorldBound().intersects(avatarOne.getWorldBound())
                if (s.getWorldBound().contains(
                        cameraOne.getLocation()))// s.getWorldBound().intersects(avatarOne.getWorldBound()))
                {
                    if (s instanceof Dog)
                    {
                        if (((Dog) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((Dog) s).setRemoved(true);
                        }
                    }
                    if (s instanceof MySquare)
                    {
                        if (((MySquare) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((MySquare) s).setRemoved(true);
                        }
                    }
                    if (s instanceof MySphere)
                    {
                        if (((MySphere) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((MySphere) s).setRemoved(true);
                        }
                    }
                    // always false
                    if (s instanceof MyPyramid)
                    {
                        if (((MyPyramid) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((MyPyramid) s).setRemoved(true);
                        }
                    }

                    numCrashes++;
                    scorePOne++;
                    System.out.println("Player 1: " + scorePOne);
                    player1ID.setText("Player 1: " + scorePOne);
                    toRemove.add(s);
                    //crash
                    MoveToDoghouseEvent newMTDhE = new MoveToDoghouseEvent(s);
                    eventManager.triggerEvent(newMTDhE);
                }

                if (s.getWorldBound().contains(
                        cameraTwo.getLocation()))// s.getWorldBound().intersects(avatarTwo.getWorldBound()))
                {
                    System.out.println("Camera two intersection");
                    if (s instanceof Dog)
                    {
                        if (((Dog) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((Dog) s).setRemoved(true);
                        }
                    }
                    if (s instanceof MySquare)
                    {
                        if (((MySquare) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((MySquare) s).setRemoved(true);
                        }
                    }
                    if (s instanceof MySphere)
                    {
                        if (((MySphere) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((MySphere) s).setRemoved(true);
                        }
                    }
                    // always false
                    if (s instanceof MyPyramid)
                    {
                        if (((MyPyramid) s).getRemoved())
                        {
                            continue;
                        } else
                        {
                            ((MyPyramid) s).setRemoved(true);
                        }
                    }

                    numCrashes++;
                    scorePTwo++;
                    System.out.println("Player 2: " + scorePTwo);
                    player2ID.setText("Player 2: " + scorePTwo);
                    toRemove.add(s);
                    //crash
                    MoveToDoghouseEvent newMTDhE = new MoveToDoghouseEvent(s);
                    eventManager.triggerEvent(newMTDhE);
                }
            }

            if (s instanceof Doghouse)
            {
                ((Doghouse) s).updateGameWorldObject(elapsedTimeMS);
            }

            if (s instanceof PyramidGroup)
            {
                Iterator k = ((PyramidGroup) s).getChildren();
                while (k.hasNext())
                {
                    SceneNode n = (SceneNode) k.next();

                    if (n.getWorldBound().contains(cameraOne.getLocation()) ||
                            n.getWorldBound().contains(cameraTwo.getLocation()))
                    {
                        if (((MyPyramid) n).getRemoved() == true)
                        {
                            continue;
                        } else
                        {
                            ((MyPyramid) n).setRemoved(true);
                        }

                        numCrashes++;
                        //scorePOne++;
                        //System.out.println(scorePOne);

                        if (n.getWorldBound().contains(cameraOne.getLocation()))
                        {
                            scorePOne++;
                            System.out.println("Player 1: " + scorePOne);
                            player1ID.setText("Player 1: " + scorePOne);
                        } else if (n.getWorldBound().contains(cameraTwo.getLocation()))
                        {
                            scorePTwo++;
                            System.out.println("Player 2: " + scorePTwo);
                            player2ID.setText("Player 2: " + scorePTwo);
                        }

                        toRemove.add(n);

                        //crash
                        MoveToDoghouseEvent newMTDhE = new MoveToDoghouseEvent(n);
                        eventManager.triggerEvent(newMTDhE);
                    }
                }
            }
        }

        for (int x = 0; x < toRemove.size(); x++)
        {
            // removeGameWorldObject(toRemove.get(x));
        }

        toRemove.clear();

        //scoreString.setText("Score = " + score);
        time += elapsedTimeMS;
        DecimalFormat df = new DecimalFormat("0.0");
        timeStringPOne.setText("Time = " + df.format(time / 1000));
        timeStringPTwo.setText("Time = " + df.format(time / 1000));

        camOne.update(elapsedTimeMS);
        camTwo.update(elapsedTimeMS);
        super.update(elapsedTimeMS);
    }

    protected void render()
    {
        renderer.setCamera(cameraOne);
        super.render();

        renderer.setCamera(cameraTwo);
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
}
