package World.Room;

public class Chapel extends Room 
{
	private static int maxNumberInWorld = 2;
	private static Chapel[] rooms = new Chapel[maxNumberInWorld];
	
	public Chapel()
	{
		//maxNumberInWorld--;
	}
	
	
	public static Chapel create()
	{
		Chapel chapel = null;
		
		if(maxNumberInWorld > 0)
		{
			chapel = new Chapel();
			
			rooms[maxNumberInWorld-1] = chapel; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return chapel;
		
	}
}
