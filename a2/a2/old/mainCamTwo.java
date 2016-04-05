package a2.old;
/*
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import myGameEngine.*;
import sage.camera.JOGLCamera;
import sage.input.IInputManager;
import sage.scene.HUDString;
import sage.scene.shape.Cylinder;

import java.awt.*;
import java.text.DecimalFormat;

public class mainCamTwo
{
    private int scorePTwo = 0;
    private HUDString player2ID;
    private HUDString timeStringPTwo;
    private Cylinder avatarTwo;
    //private Sphere avatarTwo;

    protected void initGame()
    {
        if (gpName != null)
        {
            camTwo = new Camera3PController(cameraTwo, avatarTwo, im, gpName, "G");
        }
    }

    private void createPlayers()
    {
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
        timeStringPTwo = new HUDString("Time = ");
        timeStringPTwo.setLocation(0, 0.05);
        cameraTwo.addToHUD(timeStringPTwo);

        player2ID = new HUDString("Player 2: ");// + scorePTwo);
        player2ID.setName("Player2ID");
        player2ID.setLocation(0, 0.10);
        player2ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
        player2ID.setColor(Color.yellow);
        player2ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
        cameraTwo.addToHUD(player2ID);

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
    private void associateDefaultKeyAndControllerBindings()
    {
        if (gpName != null)
        {
            initPlayerTwoControls();
        }
    }

    public void update(float elapsedTimeMS)
    {
        timeStringPTwo.setText("Time = " + df.format(time / 1000));

        camTwo.update(elapsedTimeMS);
    }

    protected void render()
    {
        renderer.setCamera(cameraTwo);
        super.render();
    }
}
*/