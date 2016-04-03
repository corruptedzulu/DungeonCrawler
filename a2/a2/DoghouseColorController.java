package a2;

import java.nio.FloatBuffer;

import sage.scene.Controller;
import sage.scene.SceneNode;

public class DoghouseColorController extends Controller 
{

	private double timeRemainingMS;
	
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
	@Override
	public void update(double timeElapsedMS) 
	{
		// TODO Auto-generated method stub

		timeRemainingMS -= timeElapsedMS;
		//System.out.println(timeRemainingMS);
		
		if(timeRemainingMS < 0) //if we ran out of time, reset the countdown and put the color back
		{
			timeRemainingMS = 2000;
			FloatBuffer colorBuf = com.jogamp.common.nio.Buffers.newDirectFloatBuffer(cl);
			for(SceneNode node : controlledNodes)
			{
				((Doghouse)node).setColorBuffer(colorBuf);
			}
			
		}
		
		
		
		
	}

}
