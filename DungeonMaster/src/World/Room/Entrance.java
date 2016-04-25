package World.Room;

public class Entrance extends Room 
{
	private static int maxNumberInWorld = 3;
	private static Entrance[] rooms = new Entrance[maxNumberInWorld];
	
	public Entrance()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Entrance create()
	{
		Entrance entrance = null;
		
		if(maxNumberInWorld > 0)
		{
			entrance = new Entrance();
			
			rooms[maxNumberInWorld-1] = entrance; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return entrance;
		
	}
}
