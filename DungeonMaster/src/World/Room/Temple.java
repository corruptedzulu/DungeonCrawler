package World.Room;

public class Temple extends Room
{
	private static int maxNumberInWorld = 1;
	private static Temple[] rooms = new Temple[maxNumberInWorld];
	
	public Temple()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Temple create()
	{
		Temple temple = null;
		
		if(maxNumberInWorld > 0)
		{
			temple = new Temple();
			
			rooms[maxNumberInWorld-1] = temple; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return temple;
		
	}
}
