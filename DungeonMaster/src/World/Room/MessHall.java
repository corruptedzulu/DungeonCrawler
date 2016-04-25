package World.Room;

public class MessHall extends Room 
{
	private static int maxNumberInWorld = 5;
	private static MessHall[] rooms = new MessHall[maxNumberInWorld];
	
	public MessHall()
	{
		//maxNumberInWorld--;
	}
	
	
	public static MessHall create()
	{
		MessHall messHall = null;
		
		if(maxNumberInWorld > 0)
		{
			messHall = new MessHall();
			
			rooms[maxNumberInWorld-1] = messHall; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return messHall;
		
	}
}
