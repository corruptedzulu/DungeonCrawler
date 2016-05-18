
package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;

public class RollLeftAction extends RollAction 
{

	public RollLeftAction(ICamera c, float rotRateDeg) 
	{
		super(c, rotRateDeg);
		rotationRateDeg = rotRateDeg * -1;
	}

	public void performAction(float time, Event event)
	{
		
		timeSinceLastRollMS += time;
		
		if(timeSinceLastRollMS > 25)
		{
			timeSinceLastRollMS = 0;
			
			Vector3D viewDir = camera.getViewDirection().normalize();
			Vector3D rightDir = camera.getRightAxis().normalize();
			Vector3D upDir = camera.getUpAxis().normalize();
			Matrix3D matrix = new Matrix3D();
			matrix.rotate(rotationRateDeg, viewDir);
			//matrix.rotateY(10);
			
			viewDir = viewDir.mult(matrix);
			rightDir = rightDir.mult(matrix);
			upDir = upDir.mult(matrix);
			
			camera.setUpAxis(upDir);
			camera.setViewDirection(viewDir);
			camera.setRightAxis(rightDir);
			
		}
		
	}
}
