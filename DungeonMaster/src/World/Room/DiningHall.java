package World.Room;

public class DiningHall extends Room 
{
	private static int maxNumberInWorld = 10;
	private static DiningHall[] rooms = new DiningHall[maxNumberInWorld];
	
	public DiningHall()
	{
		//maxNumberInWorld--;
	}
	
	
	public static DiningHall create()
	{
		DiningHall diningHall = null;
		
		if(maxNumberInWorld > 0)
		{
			diningHall = new DiningHall();
			
			rooms[maxNumberInWorld-1] = diningHall; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return diningHall;
		
	}
}
