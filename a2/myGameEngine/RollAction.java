
package myGameEngine;

import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;

public class RollAction extends AbstractInputAction 
{
	protected ICamera camera;
	protected float rotationRateDeg;
	protected int timeSinceLastRollMS;
	private boolean isEnabled;
	
	public RollAction(ICamera c, float rotRateDeg)
	{
		camera = c;
		rotationRateDeg = rotRateDeg;
		rotationRateDeg = (float) 20;
		
		timeSinceLastRollMS = 0;
		isEnabled = true;
	}
	
	
	@Override
	public void performAction(float arg0, Event arg1) 
	{

	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void setEnabled(boolean enabled)
	{
		isEnabled = enabled;
	}
}
