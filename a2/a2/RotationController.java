package a2;

import java.util.Iterator;

import a2.old.PyramidGroup;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;
import sage.scene.Controller;
import sage.scene.SceneNode;

/*
 * This code is based, with minor modifications, on the code provided
 * by Professor Scott Gordon in slide 20 of the presentation "6a Scenegraphs".
 * 
 */

public class RotationController extends Controller
{ 
	private double rotationRate = 1 ; //degrees to rotate per second
	private Vector3D rotationAxis = new Vector3D(0,1,0) ; //default axis
	
	//Apply this controller's rotation to each SceneNode to which it is attached.
	public void update(double time)
	{ 
	
		double rotAmount = rotationRate/10.0 * time ;
		Matrix3D newRot = new Matrix3D(rotAmount, rotationAxis);
		
		Iterator<SceneNode> it = ((PyramidGroup)controlledNodes.get(0)).getChildren();
		
		while(it.hasNext())
		{	
			SceneNode node = (SceneNode) it.next();
			Matrix3D curRot = node.getLocalRotation();
			curRot.concatenate(newRot);
			node.setLocalRotation(curRot);
		}
	}
}
