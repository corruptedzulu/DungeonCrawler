
package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.scene.SceneNode;

public class MoveForwardAction extends MoveAction 
{
	private SceneNode avatar;
	private float speed = 0.01f; // it would be better to use axis value
	
	 
	public MoveForwardAction(ICamera c, float s) 
	{
		super(c, s);
		
		
	}

	
	public void performAction(float time, Event event)
	{
		if(true)
		{
			Matrix3D rot = avatar.getLocalRotation();
			Vector3D dir = new Vector3D(0,0,1);
			dir = dir.mult(rot);
			if(event.getComponent().getName().contains("Y Axis"))
			{
				if( event.getValue() < -0.2)
				{
					dir.scale((double) (speed * time));
				}
				else if( event.getValue() > 0.2)
				{
					dir.scale((double) (speed * time * -1));
				}
				else
				{
					return;
				}
			}
			dir.scale((double)(speed * time));
			avatar.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
			return;
		}
		
		timeSinceLastMoveMS += time;

		if(timeSinceLastMoveMS > 25) 
		{
			//if we got this from the Y Axis, make sure we're applying a sanity check to the value
			if(event.getComponent().getName().contains("Y Axis"))// && event.getValue() > 0.2)
			{
				if( (event.getValue() < -0.2) )
				{
					timeSinceLastMoveMS = 0;
					//moveAmount = (float) 0.1 ; 
				//}
					Vector3D viewDir = camera.getViewDirection().normalize();
					Vector3D curLocVector = new Vector3D(camera.getLocation());
					Vector3D newLocVec = curLocVector.add(viewDir.mult(moveAmount));
					double newX = newLocVec.getX();
					double newY = newLocVec.getY();
					double newZ = newLocVec.getZ();
					Point3D newLoc = new Point3D(newX, newY, newZ);
					camera.setLocation(newLoc);
					
					return;
				}
				else if( (event.getValue() > 0.2) )
				{
					timeSinceLastMoveMS = 0;
					//moveAmount = (float) 0.1 ; 
				//}
					Vector3D viewDir = camera.getViewDirection().normalize();
					Vector3D curLocVector = new Vector3D(camera.getLocation());
					Vector3D newLocVec = curLocVector.add(viewDir.mult(-moveAmount));
					double newX = newLocVec.getX();
					double newY = newLocVec.getY();
					double newZ = newLocVec.getZ();
					Point3D newLoc = new Point3D(newX, newY, newZ);
					camera.setLocation(newLoc);
					
					return;
				}
				else
				{
					return;
				}
			}
			
			timeSinceLastMoveMS = 0;
			//moveAmount = (float) 0.1 ; 
		//}
			Vector3D viewDir = camera.getViewDirection().normalize();
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
