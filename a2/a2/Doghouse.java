
package a2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
import java.util.ArrayList;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.SceneNode;
import sage.scene.TriMesh;

import com.jogamp.common.nio.Buffers;

import graphicslib3D.Matrix3D;

public class Doghouse extends TriMesh implements IEventListener 
{

	private ArrayList<SceneNode> contents;
	private float[] centers = new float[] {
										
										4,4,4, 4,4,3, 4,4,2, 4,4,1,
										3,3,4, 3,3,3, 3,3,2, 3,3,1,
										2,2,4, 2,2,3, 2,2,2, 2,2,1,
										1,1,4, 1,1,3, 1,1,2, 1,1,1,
										
										-2,2,2, -2,2,4, -2,4,1, -3,4,1,
										
										-1,3,4, -1,-2,3, 3,1,4, 4,1,4,
										2,2,-2, 1,1,-1, 3,-3,4, 4,2,1
			
										};
	
	private float timeRemainingMS = 2000; //internal "timer" for doghousing notification

	
	private static float[] vrts = new float[] { 
			5, -5, 5,  5,-5,-5, -5,-5,-5, -5,-5, 5, //Bottom surface
			5, -5, 5,  5,-5,-5,  5, 5,-5,  5, 5, 5, //Righthand Wall
		   -5, -5, 5, -5,-5,-5, -5, 5, 5, -5, 5,-5, //Left Wall
			5, -5,-5, -5,-5,-5, -5, 5,-5,  5, 5,-5,//,  //back wall
			
			5,-5,5, 5,5,5, -5,5,5, -5,-5,5, -3,-5,5, 3,-5,5, 3,3,5, 3,5,5, -3,5,5, -3,3,5,
			
			5,5,6, 5,5,-6, 0,9,-6, 0,9,6,
			-5,5,6, 0,9,6, 0,9,-6, -5,5,-6,
			
			0,9,5, 0,9,-5
			
		  };

	private static float[] cl = new float[] { 
				0,0,1,0, 0,0,1,0, 0,0,1,0, 0,0,1,0, 
				0,1,0,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 
				0,1,0,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 
				0,1,0,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 
				0,1,0,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 0,1,1,0, 0,1,1,0,	0,1,1,0, 0,1,1,0,
				
				0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0,
				0,1,0,0, 0,1,0,0, 0,1,0,0, 0,1,0,0,
				
				1,0,0,0, 1,0,0,0
				
			};
	
	private static float[] clAlt = new float[] { 
				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0, 
				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0,
				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0, 
				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0,
				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0,	1,1,1,0, 1,1,1,0,

				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0,
				1,1,1,0, 1,1,1,0, 1,1,1,0, 1,1,1,0,
				
				1,1,1,0, 1,1,1,0
				
			   };
	
	private static int[] triangles = new int[] {
				0,1,2, 0,2,3, //Bottom surface
				4,5,6, 4,6,7, //Righthand Wall
				8,9,10, 9,10,11, //Left Wall
				12,13,14, 12, 14, 15,
				16,17,21, 17,21,23, 18,19,20, 18,20,24, 22,23,25, 23,24,25,
				
				26,27,28, 26,28,29,
				30,31,32, 30,32,33,
				
				34,18,17, 35,14,15
				
				
			   };
	
	private FloatBuffer vertBuf, colorBuf, colorAltBuf;
	private IntBuffer triangleBuf;
	
	public Doghouse()
	{
		super();
		contents = new ArrayList<SceneNode>();
		
		vertBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		colorBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
		colorAltBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(clAlt);
		triangleBuf = com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		
		
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf);
	}
	
	@Override
	public boolean handleEvent(IGameEvent event) 
	{
		
		MoveToDoghouseEvent e = (MoveToDoghouseEvent)event;
		
		SceneNode node = e.getAffectedNode();
		
		if(contents.contains(node))
		{
			
		}
		else
		{
			//FloatBuffer cbuf = this.getColorBuffer();
			//cbuf.com
			//if we are currently the normal color, switch it
			if(this.getColorBuffer().compareTo(colorBuf) == 0)
			{
				FloatBuffer colorAltBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(clAlt);
				this.setColorBuffer(colorAltBuf);
			}
			else
			{
				timeRemainingMS = 2000; //a further hit was registered, so reset the countdown
			}
			
			
			
			
			//move to center
			node.getLocalTranslation().concatenate(node.getLocalTranslation().inverse());
			
			
			node.getLocalScale().scale(0.25, 0.25, 0.25);
			
			
			contents.add(node);
			//Matrix3D temp = node.getLocalTranslation();
			//temp = node.getLocalTranslation().inverse();
			
			//move to dog house
			//compute doghouse internal coordinates to move to...
			//use 1x1x1 voxels as storage space?
			//
			
			for(int x = 0; x < contents.size(); x++)
			{
				int y = x*3;
				if(centers[y] == 0)
				{
					//y += 2;
					continue;
				}
				else
				{
					float tempX, tempY, tempZ;
					tempX = centers[y];
					tempY = centers[y+1];
					tempZ = centers[y+2];
					
					centers[y] = (float)0.0;
					centers[y+1] = (float)0.0;
					centers[y+2] = (float)0.0;
					
					//x+=2;
					
					
					node.getLocalTranslation().translate(tempX, tempY, tempZ);
				}
				
			}
			
			node.updateWorldBound();
		}
		

		return false;
	}
	
	//update method to change color back
	public void updateGameWorldObject(float timeElapsedMS)
	{
		
		
	}
	
	public ArrayList<SceneNode> getContents()
	{
		return contents;
	}

}
