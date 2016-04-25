package World.Room;

public class Quarters extends Room 
{
	private static int maxNumberInWorld = 10;
	private static Quarters[] rooms = new Quarters[maxNumberInWorld];
	
	public Quarters()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Quarters create()
	{
		Quarters quarters = null;
		
		if(maxNumberInWorld > 0)
		{
			quarters = new Quarters();
			
			rooms[maxNumberInWorld-1] = quarters; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return quarters;
		
	}
}
