package World.WorldObjects;

public class Door 
{
	
	private String doorType;
	
	private int width;
	
	
	
	//these are the square locations of the door
	//only one axis would be used (the other is -1) since the doors are on an axis (1D, not 2D)
	//there are two sets, so that a single door object can define its location in the rooms on either side of it
	private int xRoomOneCoor;
	private int yRoomOneCoor;

	private int xRoomTwoCoor;
	private int yRoomTwoCoor;
	
	
	
	
	public Door()
	{
		xRoomOneCoor = -1;
		yRoomOneCoor = -1;
		
		xRoomTwoCoor = -1;
		yRoomTwoCoor = -1;
	}
	
	
	public void setDoorType(String dt)
	{
		doorType = dt;
	}
	
	public String getDoorType()
	{
		return doorType;
	}
	
	
	public void setDoorWidth(int w)
	{
		width = w;
	}
	
	public int getDoorWidth()
	{
		return width;
	}
	
	
	
	
	
}
