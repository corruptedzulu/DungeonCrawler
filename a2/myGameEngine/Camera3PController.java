package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.IInputManager;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import sage.scene.SceneNode;
import sage.util.MathUtils;

public class Camera3PController
{
	private ICamera cam; //the camera being controlled
	private SceneNode target; //the target the camera looks at
	private float cameraAzimuth; //rotation of camera around target Y axis
	private float cameraElevation; //elevation of camera above target
	private float cameraDistanceFromTarget;
	private Point3D targetPos; // avatar?s position in the world
	private Vector3D worldUpVec;
	private String type;

	public Camera3PController(ICamera cam, SceneNode target, IInputManager inputMgr, String controllerName, String t)
	{
		this.cam = cam;
		this.target = target;
		worldUpVec = new Vector3D(0, 1, 0);
		cameraDistanceFromTarget = 5.0f;
		cameraAzimuth = 0; // start from BEHIND and ABOVE the target
		cameraElevation = 20.0f; // elevation is in degrees
		type = t;

		update(0.0f); // initialize camera state
		setupInput(inputMgr, controllerName);
	}

	public void setNewSceneNode(SceneNode target)
	{
		this.target = target;
	}

	public void update(float time)
	{
		updateTarget();
		updateCameraPosition();
		cam.lookAt(targetPos, worldUpVec); // SAGE built-in function
	}

	public float getCameraAzimuth()
	{
		return cameraAzimuth;
	}

	private void updateTarget()
	{
		targetPos = new Point3D(target.getWorldTranslation().getCol(3));
	}

	private void updateCameraPosition()
	{
		double theta = cameraAzimuth;
		double phi = cameraElevation;
		double r = cameraDistanceFromTarget;
		// calculate new camera position in Cartesian coords
		Point3D relativePosition = MathUtils.sphericalToCartesian(theta, phi, r);
		Point3D desiredCameraLoc = relativePosition.add(targetPos);
		cam.setLocation(desiredCameraLoc);
	}

	private void setupInput(IInputManager im, String cn)
	{
		if (type == "K")//KEYBOARD
		{
			IAction orbitActionCounterclockwise = new OrbitAroundAction(1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Key.LEFT, orbitActionCounterclockwise,
							   IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
			IAction orbitActionClockwise = new OrbitAroundAction(-1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Key.RIGHT, orbitActionClockwise,
							   IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

			IAction zoomInAction = new ZoomCameraAction(1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Key.Z, zoomInAction,
							   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

			IAction zoomOutAction = new ZoomCameraAction(-1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Key.X, zoomOutAction,
							   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		}

		if (type == "G")//GAMEPAD
		{

			IAction orbitActionCounterclockwise = new OrbitAroundAction(1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Button._3, orbitActionCounterclockwise,
							   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
			IAction orbitActionClockwise = new OrbitAroundAction(-1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Button._2, orbitActionClockwise,
							   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

			IAction zoomInAction = new ZoomCameraAction(1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Button._5, zoomInAction,
							   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

			IAction zoomOutAction = new ZoomCameraAction(-1.0);
			im.associateAction(cn, net.java.games.input.Component.Identifier.Button._6, zoomOutAction,
							   IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		}

	}

	private class ZoomCameraAction extends AbstractInputAction
	{
		private double value;

		public ZoomCameraAction(double action)
		{
			value = action;
		}

		@Override
		public void performAction(float time, Event evt)
		{
			// TODO Auto-generated method stub

			float zoomAmount;
			if (evt.getValue() < -0.2)
			{
				zoomAmount = -0.5f;
			} else
			{
				if (evt.getValue() > 0.2)
				{
					zoomAmount = 0.5f;
				} else
				{
					zoomAmount = 0.0f;
				}
			}

			cameraDistanceFromTarget += zoomAmount * value;

			if (cameraDistanceFromTarget < .1f)
			{
				cameraDistanceFromTarget = .1f;
			}
			if (cameraDistanceFromTarget > 200)
			{
				cameraDistanceFromTarget = 200;
			}
		}
	}

	private class OrbitAroundAction extends AbstractInputAction
	{
		double dir;

		public OrbitAroundAction(double d)
		{
			this.dir = d;
		}

		public void performAction(float time, Event evt)
		{
			float rotAmount;
			if (evt.getValue() < -0.2)
			{
				rotAmount = -0.4f;
			} else
			{
				if (evt.getValue() > 0.2)
				{
					rotAmount = 0.4f;
				} else
				{
					rotAmount = 0.0f;
				}

			}

			cameraAzimuth += rotAmount * dir;
			cameraAzimuth = cameraAzimuth % 360;
		}
	}
}