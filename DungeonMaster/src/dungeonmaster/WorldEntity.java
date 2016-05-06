package dungeonmaster;
import java.util.Random;

import World.GameWorld;
import World.Room.Room;

public abstract class WorldEntity 
{

	//zero indexed
	protected int xCoor;
	protected int yCoor;
	
	protected Random rand;
	
	protected GameWorld gw;
	
	protected Room containingRoom;
	

	public Room getContainingRoom() {
		return containingRoom;
	}


	public void setContainingRoom(Room containingRoom) {
		this.containingRoom = containingRoom;
	}
	
	public void moveToNewRoom(Room newRoom)
	{
		this.containingRoom = newRoom;
	}


	public void setGameWorld(GameWorld gw)
	{
		this.gw = gw;
	}


	public int getxCoor() {
		return xCoor;
	}


	public void setxCoor(int xCoor) {
		this.xCoor = xCoor;
	}


	public int getyCoor() {
		return yCoor;
	}


	public void setyCoor(int yCoor) {
		this.yCoor = yCoor;
	}
	
}
