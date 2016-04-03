
package myGameEngine;

import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;

public class PitchDownAction extends RollAction 
{

	public PitchDownAction(ICamera c, float rotRateDeg) 
	{
		super(c, rotRateDeg);
		rotationRateDeg = rotationRateDeg * -1;
	}

	public void performAction(float time, Event event)
	{
		
		timeSinceLastRollMS += time;
		
		if(timeSinceLastRollMS > 25)
		{
			timeSinceLastRollMS = 0;
			
			Vector3D viewDir = camera.getViewDirection();//.normalize();
			Vector3D rightDir = camera.getRightAxis();//.normalize();
			Vector3D upDir = camera.getUpAxis();//.normalize();
			Matrix3D matrix = new Matrix3D();
			matrix.rotate(rotationRateDeg, rightDir);
			//matrix.rotateX(rotationRateDeg);
			//matrix.rotateY(rotationRateDeg);
			//matrix.rotateZ(rotationRateDeg);
			
			
			//viewDir = viewDir.mult(matrix);
			//rightDir.mult(matrix);
			//upDir = upDir.mult(matrix);
			
			//camera.setRightAxis(rightDir);
			//camera.setUpAxis(upDir);
			//camera.setViewDirection(viewDir);

						
			/*double x = rightDir.getX();
			double y = rightDir.getY();
			double z = rightDir.getZ();
			//x = 1;
			//y = 1; 
			//z = 1;
			
			double r = Math.sqrt(Math.pow(y,2) + Math.pow(z,2));
			double thetaX, thetaY;
			if(z == 0)
			{
				thetaX = 90;
			}
			else
			{
				thetaX = Math.toDegrees(Math.asin(y/z));//opposite/adjacent
			}
			
			matrix.rotateX(-thetaX);
			//matrix.rotateX(thetaX);
			
			if(z == 0)
			{
				thetaY = 0;
			}
			else
			{
				thetaY = Math.toDegrees(Math.asin(x/z));
			}
			matrix.rotateY(-thetaY);
			
			matrix.rotateZ(rotationRateDeg);
			
			matrix.rotateY(thetaY);
			
			matrix.rotateX(thetaX);
			*/
			
			/*
			double argument = z/r;
			double angle = Math.acos(argument);
			
					
			double[] transpose = {1.0, 0.0, 0.0, -x,
								  0.0, 1.0, 0.0, -y,
								  0.0, 0.0, 1.0, -z,
								  0.0, 0.0, 0.0, 1.0};
			
			double[] normal = {1.0, 0.0, 0.0, x,
							   0.0, 1.0, 0.0, y,
							   0.0, 0.0, 1.0, z,
							   0.0, 0.0, 0.0, 1.0};
					
			
			double[] valsX = {1.0, 0.0,     0.0,      0.0,
							  0.0, z/angle, -y/angle, 0.0,
							  0.0, y/angle, z/angle,  0.0,
							  0.0, 0.0,     0.0,      1.0};
			double[] valsXInverse = {1.0, 0.0,      0.0,      0.0,
									 0.0, z/angle,  y/angle,  0.0,
									 0.0, -y/angle, z/angle,  0.0,
									 0.0, 0.0,      0.0,      1.0};
			
			double[] valsY = {angle, 0.0, -x,    0.0,
							  0.0,   1.0, 0.0,   0.0,
							  x,     0.0, angle, 0.0,
							  0.0,   0.0, 0.0,   1.0};
			double[] valsYInverse = {angle, 0.0, x,     0.0,
									 0.0,   1.0, 0.0,   0.0,
									 -x,    0.0, angle, 0.0,
									 0.0,   0.0, 0.0,   1.0};
			
			double[] valsZ = {Math.cos(rotationRateDeg), -Math.sin(rotationRateDeg), 0.0, 0.0,
							  Math.sin(rotationRateDeg), Math.cos(rotationRateDeg),  0.0, 0.0,
							  0.0,                       0.0,                        1.0, 0.0,
							  0.0,                       0.0,                        0.0, 1.0};
			
			
			//matrix = new Matrix3D(valsX);
			
			Matrix3D xmatrix, xInv, ymatrix, yInv, zmatrix, t, tInv, input;
			
			xmatrix = new Matrix3D(valsX);
			xInv = new Matrix3D(valsXInverse);
			ymatrix = new Matrix3D(valsY);
			yInv = new Matrix3D(valsYInverse);
			zmatrix = new Matrix3D(valsZ);
			t = new Matrix3D(transpose);
			tInv = new Matrix3D(normal);
			
			double[] temp = {x, y, z, 1.0};
			input = new Matrix3D(temp);
			
			
			t.concatenate(input);
			xmatrix.concatenate(t);
			ymatrix.concatenate(xmatrix);
			zmatrix.concatenate(ymatrix);
			yInv.concatenate(zmatrix);
			xInv.concatenate(yInv);
			tInv.concatenate(xInv);
			
			
			matrix.rotateY(angle);
			Matrix3D yInverse = matrix;
			yInverse.inverse();
			
			matrix.rotateZ(rotationRateDeg);
*/
			viewDir = viewDir.mult(matrix);
			rightDir = rightDir.mult(matrix);
			upDir = upDir.mult(matrix);
			
			camera.setRightAxis(rightDir);
			camera.setUpAxis(upDir);
			camera.setViewDirection(viewDir);

			
			
			
			 			
		}
		
	}
}
