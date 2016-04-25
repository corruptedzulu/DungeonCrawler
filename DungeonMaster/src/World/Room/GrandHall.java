package World.Room;

public class GrandHall extends Room 
{
	private static int maxNumberInWorld = 2;
	private static GrandHall[] rooms = new GrandHall[maxNumberInWorld];
	
	public GrandHall()
	{
		//maxNumberInWorld--;
	}
	
	
	public static GrandHall create()
	{
		GrandHall grandHall = null;
		
		if(maxNumberInWorld > 0)
		{
			grandHall = new GrandHall();
			
			rooms[maxNumberInWorld-1] = grandHall; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return grandHall;
		
	}
}
