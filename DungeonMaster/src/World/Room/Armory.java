package World.Room;

public class Armory extends Room 
{
	private static int maxNumberInWorld = 10;
	private static Armory[] rooms = new Armory[maxNumberInWorld];
	
	public Armory()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Armory create()
	{
		Armory armory = null;
		
		if(maxNumberInWorld > 0)
		{
			armory = new Armory();
			
			rooms[maxNumberInWorld-1] = armory; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return armory;
		
	}
}
