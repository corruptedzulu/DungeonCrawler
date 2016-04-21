
package a2.old;

import sage.event.AbstractGameEvent;

public class CrashEvent extends AbstractGameEvent 
{
	private int crashNum = 0;
	
	public CrashEvent(int crashNumber)
	{
		crashNum = crashNumber;
	}
}
