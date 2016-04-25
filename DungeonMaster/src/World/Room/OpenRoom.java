package World.Room;

public class OpenRoom extends Room 
{
	private static int maxNumberInWorld = 10;
	private static OpenRoom[] rooms = new OpenRoom[maxNumberInWorld];
	
	public OpenRoom()
	{
		//maxNumberInWorld--;
	}
	
	
	public static OpenRoom create()
	{
		OpenRoom openRoom = null;
		
		if(maxNumberInWorld > 0)
		{
			openRoom = new OpenRoom();
			
			rooms[maxNumberInWorld-1] = openRoom; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return openRoom;
		
	}
}
