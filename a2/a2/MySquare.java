
package a2;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.shape.Sphere;
import sage.scene.shape.Square;

public class MySquare extends Square implements IEventListener 
{

	private boolean isRemoved = false;
	
	@Override
	public boolean handleEvent(IGameEvent event) 
	{
		//CrashEvent, aka the Camera hit us
		
		
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
