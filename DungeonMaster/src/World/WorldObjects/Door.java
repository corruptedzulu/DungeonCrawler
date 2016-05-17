package World.WorldObjects;

import World.Room.Room;
import dungeonmaster.Skill;

public class Door extends WorldObject
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
	
	private int roomOneFace; //N=1, S=2, E=3, W=4
	private int roomTwoFace;
	
	private Room myRoom;
	private Room roomIConnectTo;
	
	public static int doorID = 0;
	protected int myDoorID;
	
	
	
	public Door()
	{
		setxRoomOneCoor(-1);
		setyRoomOneCoor(-1);
		
		setxRoomTwoCoor(-1);
		setyRoomTwoCoor(-1);
		
		this.setInteractionDC(13);
		String s = "Sleight of Hand";
		this.setInteractionSkill(s);
		
		
		myDoorID = doorID;
		doorID++;
		
	}
	
	private void updateDoorFaces()
	{
		
		if(xRoomOneCoor == 0)
		{
			roomOneFace = 4;//west
			roomTwoFace = 3;//east
		}
		
		if(yRoomOneCoor == 0)
		{
			roomOneFace = 2;//south
			roomTwoFace = 1;//north
		}
		
		if(xRoomOneCoor > 0)
		{
			roomOneFace = 3;//east
			roomTwoFace = 4;//west
		}
		
		if(yRoomOneCoor > 0)
		{
			roomOneFace = 1;//north
			roomTwoFace = 2;//south
		}
		
		
		
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


	public int getxRoomOneCoor() {
		return xRoomOneCoor;
	}


	public void setxRoomOneCoor(int xRoomOneCoor) {
		this.xRoomOneCoor = xRoomOneCoor;
		updateDoorFaces();
	}


	public int getyRoomOneCoor() {
		return yRoomOneCoor;
	}


	public void setyRoomOneCoor(int yRoomOneCoor) {
		this.yRoomOneCoor = yRoomOneCoor;
		updateDoorFaces();
	}


	public int getxRoomTwoCoor() {
		return xRoomTwoCoor;
	}


	public void setxRoomTwoCoor(int xRoomTwoCoor) {
		this.xRoomTwoCoor = xRoomTwoCoor;
		updateDoorFaces();
	}


	public int getyRoomTwoCoor() {
		return yRoomTwoCoor;
	}


	public void setyRoomTwoCoor(int yRoomTwoCoor) {
		this.yRoomTwoCoor = yRoomTwoCoor;
		updateDoorFaces();
	}
	
	
	
	public String toString()
	{
		String result = "";
		
		result += "Door" + myDoorID + ":" + xRoomOneCoor + "," + yRoomOneCoor + ";" + + xRoomTwoCoor + "," + yRoomTwoCoor + ";" + "$$";
		
		return result;
	}

	public Room getMyRoom()
	{
		return myRoom;
	}

	public void setMyRoom(Room myRoom)
	{
		this.myRoom = myRoom;
	}

	public Room getRoomIConnectTo()
	{
		return roomIConnectTo;
	}

	public void setRoomIConnectTo(Room roomIConnectTo)
	{
		this.roomIConnectTo = roomIConnectTo;
	}
	
	
}
