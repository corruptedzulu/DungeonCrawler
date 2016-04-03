
package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;

public class PitchUpAction extends RollAction 
{

	public PitchUpAction(ICamera c, float rotRateDeg) 
	{
		super(c, rotRateDeg);
	}

	public void performAction(float time, Event event)
	{
		
		timeSinceLastRollMS += time;
		
		if(timeSinceLastRollMS > 25)
		{
			//System.out.println("move right: " + event.getComponent().getName());
			//if we got this from the Y Axis, make sure we're applying a sanity check to the value
			if(event.getComponent().getName().contains("Y Rotation"))// && event.getValue() > 0.2)
			{
				if( (event.getValue() < -0.2) )
				{
					timeSinceLastRollMS = 0;
					//moveAmount = (float) 0.1 ; 
				//}
					Vector3D viewDir = camera.getViewDirection().normalize();
					Vector3D rightDir = camera.getRightAxis().normalize();
					Vector3D upDir = camera.getUpAxis().normalize();
					Matrix3D matrix = new Matrix3D();
					matrix.rotate(rotationRateDeg, rightDir);
					//matrix.rotateX(rotationRateDeg);
					//matrix.rotateY(10);
					
					viewDir = viewDir.mult(matrix);
					rightDir.mult(matrix);
					upDir = upDir.mult(matrix);
					
					camera.setRightAxis(rightDir);
					camera.setUpAxis(upDir);
					camera.setViewDirection(viewDir);
					
					return;
				}
				else if( (event.getValue() > 0.2) )
				{
					timeSinceLastRollMS = 0;
					//moveAmount = (float) 0.1 ; 
				//}
					Vector3D viewDir = camera.getViewDirection().normalize();
					Vector3D rightDir = camera.getRightAxis().normalize();
					Vector3D upDir = camera.getUpAxis().normalize();
					Matrix3D matrix = new Matrix3D();
					matrix.rotate(-rotationRateDeg, rightDir);
					//matrix.rotateX(rotationRateDeg);
					//matrix.rotateY(10);
					
					viewDir = viewDir.mult(matrix);
					rightDir.mult(matrix);
					upDir = upDir.mult(matrix);
					
					camera.setRightAxis(rightDir);
					camera.setUpAxis(upDir);
					camera.setViewDirection(viewDir);
					
					return;
				}
				else
				{
					return;
				}
			}
			
			timeSinceLastRollMS = 0;
			//moveAmount = (float) 0.1 ; 
		//}
			Vector3D viewDir = camera.getViewDirection().normalize();
			Vector3D rightDir = camera.getRightAxis().normalize();
			Vector3D upDir = camera.getUpAxis().normalize();
			Matrix3D matrix = new Matrix3D();
			matrix.rotate(rotationRateDeg, rightDir);
			//matrix.rotateX(rotationRateDeg);
			//matrix.rotateY(10);
			
			viewDir = viewDir.mult(matrix);
			rightDir.mult(matrix);
			upDir = upDir.mult(matrix);
			
			camera.setRightAxis(rightDir);
			camera.setUpAxis(upDir);
			camera.setViewDirection(viewDir);
					
			
		}
		
	}
}
