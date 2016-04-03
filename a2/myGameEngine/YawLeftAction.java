
package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.scene.SceneNode;

public class YawLeftAction extends RollAction 
{

	private SceneNode avatar;
	private float speed = 0.01f; // it would be better to use axis value

	public YawLeftAction(ICamera c, float rotRateDeg) 
	{
		super(c, rotRateDeg);
		//rotationRateDeg = rotationRateDeg * -1;
	}

	public void performAction(float time, Event event)
	{
		
		
		
		timeSinceLastRollMS += time;
		
		if(timeSinceLastRollMS > 25)
		{
			timeSinceLastRollMS = 0;
			
			if(true){
				Matrix3D rot = avatar.getLocalRotation();
				rot.rotate(0, 3, 0);
				//avatar.rotate(3, );
				//Vector3D dir = new Vector3D(0,0,1);
				//Matrix3D tempRot = new Matrix3D();
				//tempRot.concatenate(rot);
				//tempRot.rotate(0,90,0);
				//dir = dir.mult(tempRot);
				//dir.scale((double)(speed * time) * -1);
				//avatar.translate((float)dir.getX(),(float)dir.getY(),(float)dir.getZ());
				return;}
			
			Vector3D viewDir = camera.getViewDirection().normalize();
			Vector3D rightDir = camera.getRightAxis().normalize();
			Vector3D upDir = camera.getUpAxis().normalize();
			Matrix3D matrix = new Matrix3D();
			matrix.rotate(rotationRateDeg, upDir);
			//matrix.rotateY(10);
			
			viewDir = viewDir.mult(matrix);
			rightDir = rightDir.mult(matrix);
			upDir = upDir.mult(matrix);
			
			camera.setUpAxis(upDir);
			camera.setViewDirection(viewDir);
			camera.setRightAxis(rightDir);
			
		}
		
	}
	
	public void setAvatar(SceneNode n) 
	{ 
		avatar = n; 
	}
}
