
package a2.old;

import sage.event.IEventListener;
import sage.event.IGameEvent;
import sage.scene.shape.Sphere;

public class MySphere extends Sphere implements IEventListener
{
	
	private boolean isRemoved = false;

	@Override
	public boolean handleEvent(IGameEvent arg0) {
		// TODO Auto-generated method stub
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
