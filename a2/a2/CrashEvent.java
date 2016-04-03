
package a2;

import sage.event.AbstractGameEvent;

public class CrashEvent extends AbstractGameEvent 
{

	private int crashNum = 0;
	
	public CrashEvent(int crashNumber)
	{
		crashNum = crashNumber;
	}
	
	
	
	
}
