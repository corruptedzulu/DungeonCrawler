
package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;

public class MoveDownAction extends MoveAction 
{

	public MoveDownAction(ICamera c, float s)
	{
		super(c, s);
		moveAmount = moveAmount * -1;
	}
	
	public void performAction(float time, Event event)
	{
		timeSinceLastMoveMS += time;
		
		if(timeSinceLastMoveMS > 25)
		{
			timeSinceLastMoveMS = 0;
			
			Vector3D viewDir = camera.getUpAxis().normalize();
			Vector3D curLocVector = new Vector3D(camera.getLocation());
			Vector3D newLocVec = curLocVector.add(viewDir.mult(moveAmount));
			double newX = newLocVec.getX();
			double newY = newLocVec.getY();
			double newZ = newLocVec.getZ();
			Point3D newLoc = new Point3D(newX, newY, newZ);
			camera.setLocation(newLoc);
		}

		
	}
}
