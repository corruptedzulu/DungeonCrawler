package World.Room;

public class Storage extends Room
{
	private static int maxNumberInWorld = 30;
	private static Storage[] rooms = new Storage[maxNumberInWorld];
	
	public Storage()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Storage create()
	{
		Storage storage = null;
		
		if(maxNumberInWorld > 0)
		{
			storage = new Storage();
			
			rooms[maxNumberInWorld-1] = storage; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return storage;
		
	}
}
