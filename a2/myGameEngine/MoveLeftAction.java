
package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.scene.SceneNode;

public class MoveLeftAction extends MoveAction 
{

	private SceneNode avatar;
	private float speed = 0.01f; // it would be better to use axis value

	
	public MoveLeftAction(ICamera c, float s) 
	{
		super(c, s);
		moveAmount = moveAmount * -1;
	}
	
	public void performAction(float time, Event event)
	{
		if(true){
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(0,0,1);
			Matrix3D tempRot = new Matrix3D();
			tempRot.concatenate(rot);
			tempRot.rotate(0,90,0);
			dir = dir.mult(tempRot);
			dir.scale((double)(speed * time) * 1);
			avatar.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
			return;}
		
		timeSinceLastMoveMS += time;
		
		if(timeSinceLastMoveMS > 25)
		{
			timeSinceLastMoveMS = 0;
			
			
			Vector3D viewDir = camera.getRightAxis().normalize();
			Vector3D curLocVector = new Vector3D(camera.getLocation());
			Vector3D newLocVec = curLocVector.add(viewDir.mult(moveAmount));
			double newX = newLocVec.getX();
			double newY = newLocVec.getY();
			double newZ = newLocVec.getZ();
			Point3D newLoc = new Point3D(newX, newY, newZ);
			camera.setLocation(newLoc);
		}
	}

	public void setAvatar(SceneNode n) 
	{ 
		avatar = n; 
	}
}
