

package a2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.TriMesh;

public class Dog extends TriMesh implements IEventListener 
{
	
	private boolean isRemoved = false;
	
	private static float[] vrts = new float[] 
	{
		 2, 2, 4,  2, 2, 3,  1, 2, 3,  1, 2, 4, (float) 1.5,-4,(float)  3.5, //right front leg, clockwise
		 2, 2,-3,  2, 2,-4,  1, 2,-4,  1, 2,-3, (float) 1.5,-4,(float) -3.5, //right back leg, clockwise
		-1, 2,-3, -1, 2,-4, -2, 2,-4, -2, 2,-3, (float)-1.5,-4,(float) -3.5, //left back leg, clockwise
		-1, 2, 4, -1, 2, 3, -2, 2, 3, -2, 2, 4, (float)-1.5,-4,(float)  3.5, //left front leg, clockwise
		
		1,2,4, 1,0,4, -1,0,4, -1,2,4, 0,1,-4, //torso
		
		(float) 1,3,4, (float) 1,2,4, (float) -1,2,4, (float) -1,3,4, 0,2,5 //head
	};
	
	/*private static float[] vrts = new float[] { 
												2,8,0, -2,8,0, 0,8,8, //torso
												2,8,0, 1,8,0, (float) 1.5,0,0, //FR Leg
												-2,8,0, -1,8,0, (float) -1.5,0,0, //FL Leg
												2,8,8, 1,8,8, (float) 1.5,0,8, //RR Leg
												-2,8,8, -1,8,8, (float) -1.5,0,8, //RL Leg
												(float)1.5,10,0, (float) -1.5,10,0, 0,7,(float)-0.5,  //head
												2,5,0, -2,5,0, 0,7,8 //torso lower
											  };
	*/
	private static float[] cl = new float[] { 
												1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 
												1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 
												1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, //RL Leg
												1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0, 1,0,0,0,
												
												1,1,0,0, 1,1,0,0, 1,1,0,0, 1,1,0,0,	1,1,0,0, 
												1,0,1,0, 1,0,1,0, 1,0,1,0, 1,0,1,0, 1,0,1,0

											};
	
	private static float[] clAlt = new float[] { 
												0,0,0,0, 0,0,0,0, 0,0,0,0, //torso
												0,0,0,0, 0,0,0,0, 0,0,0,0, //FR Leg
												0,0,0,0, 0,0,0,0, 0,0,0,0, //FL Leg
												0,0,0,0, 0,0,0,0, 0,0,0,0, //RR Leg
												0,0,0,0, 0,0,0,0, 0,0,0,0, //RL Leg
												0,0,0,0, 0,0,0,0, 0,0,0,0  //head
											   };
	
	private static int[] triangles = new int[] {
												0,1,4, 1,2,4, 2,3,4, 3,0,4, 0,1,2, 0,2,3,
												
												5,6,9, 6,7,9, 7,8,9, 8,5,9, 5,6,7, 5,6,8,
												
												10,11,14, 11,12,14, 12,13,14, 13,10,14, 10,11,12, 10,12,13,
												
												15,16,19, 16,17,19, 17,18,19, 18,15,19, 15,16,17, 15,17,18,
												
												20,21,24, 21,22,24, 22,23,24, 20,23,24, 20,21,22, 20,22,23,
												
												25,26,29, 26,27,29, 27,28,29, 25,28,29, 25,26,27, 25,27,28
												
											   };
	
	
	
	public Dog()
	{
		int i;
		
		FloatBuffer vertBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		FloatBuffer colorBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
		//FloatBuffer colorAltBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(clAlt);
		IntBuffer triangleBuf = com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf);
		
	}
	
	@Override
	public boolean handleEvent(IGameEvent event) 
	{
		
		return false;
	}

	public boolean getRemoved()
	{
		return isRemoved;
	}
	
	public void setRemoved(boolean r)
	{
		isRemoved = r;
	}
	
}
