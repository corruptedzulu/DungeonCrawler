package World.Room;

public class Barracks extends Room 
{
	private static int maxNumberInWorld = 10;
	private static Barracks[] rooms = new Barracks[maxNumberInWorld];
	
	public Barracks()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Barracks create()
	{
		Barracks barracks = null;
		
		if(maxNumberInWorld > 0)
		{
			barracks = new Barracks();
			
			rooms[maxNumberInWorld-1] = barracks; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return barracks;
		
	}
}
