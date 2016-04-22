
package a2.old;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import sage.scene.shape.Pyramid;

public class MyPyramid extends Pyramid 
{
	private static float[] vrts = new float[] {0,1,0,-1,-1,1,1,-1,1,1,-1,-1,-1,-1,-1};
	private static float[] cl = new float[] {1,0,0,1,0,1,0,1,0,0,1,1,1,1,0,1,1,0,1,1};
	private static int[] triangles = new int[] {0,1,2,0,2,3,0,3,4,0,4,1,1,4,2,4,3,2};
	
	private boolean isRemoved = false;
	
	public MyPyramid()
	{
		FloatBuffer vertBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(vrts);
		FloatBuffer colorBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
		IntBuffer triangleBuf = com.jogamp.common.nio.Buffers.newDirectIntBuffer(triangles);
		this.setVertexBuffer(vertBuf);
		this.setColorBuffer(colorBuf);
		this.setIndexBuffer(triangleBuf); 
		
		//this.setShowBound(true);
	}
	
	public MyPyramid(String string) 
	{
		// TODO Auto-generated constructor stub
		super(string);
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
