package dungeonmaster;
import java.util.Random;

import World.GameWorld;
import World.Room.Room;
import World.WorldObjects.WorldObject;

public abstract class WorldEntity implements Comparable
{

	protected static int worldEntityID = 0;
	
	protected int myWorldEntityID;
	
	//zero indexed
	protected int xCoor;
	protected int yCoor;
	
	protected Random rand;
	
	protected GameWorld gw;
	
	protected Room containingRoom;
	
	
	protected boolean shouldRemoveSelfFromGame;
	
	
	protected WorldEntity()
	{
		myWorldEntityID = worldEntityID;
		worldEntityID++;
		
		shouldRemoveSelfFromGame = false;
		
		xCoor = 0;
		yCoor = 0;
		
		rand = new Random();
	}

	public void resetTurnSpecificValues()
	{
		
	}
	
	public int rollInitiative()
	{
		return 0;
	}
	
	public boolean getShouldRemoveSelfFromGame() {
		return shouldRemoveSelfFromGame;
	}


	protected void setShouldRemoveSelfFromGame(boolean shouldRemoveSelfFromGame) {
		this.shouldRemoveSelfFromGame = shouldRemoveSelfFromGame;
	}


	public int getMyWorldEntityID() {
		return myWorldEntityID;
	}
	
	
	public Room getContainingRoom() {
		return containingRoom;
	}

	
	public String toString()
	{
		String s = null;
		
		s = "WorldEntity" + myWorldEntityID + ":" + xCoor + "," + yCoor + ";";
		
		return s;
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
	
	
	public boolean isAdjacentTo(WorldEntity e)
	{
		boolean result = false;
		
		int x1 = this.xCoor + 1;
		int x0 = this.xCoor;
		int xn1 = this.xCoor - 1;
		
		int y1 = this.yCoor + 1;
		int y0 = this.yCoor;
		int yn1 = this.yCoor - 1;
		
		
		int eX = e.getxCoor();
		int eY = e.getyCoor();
		
		
		if(eX == x1) //if we're aligned with the column one square to the right
		{
			if(eY == y1 || eY == y0 || eY == yn1) //if we're aligned with any of the three rows
			{
				result = true;
			}
		}
		else if(eX == x0)
		{
			if(eY == y1 || eY == yn1)
			{
				result = true;
			}
		}
		else if(eX == xn1)
		{
			if(eY == y1 || eY == y0 || eY == yn1)
			{
				result = true;
			}
		}
		else
		{
			result = false;
		}
		
		return result;
	}
	
	public boolean isAdjacentTo(WorldObject e)
	{
		boolean result = false;
		
		int x1 = this.xCoor + 1;
		int x0 = this.xCoor;
		int xn1 = this.xCoor - 1;
		
		int y1 = this.yCoor + 1;
		int y0 = this.yCoor;
		int yn1 = this.yCoor - 1;
		
		
		int eX = e.getxCoor();
		int eY = e.getyCoor();
		
		
		if(eX == x1) //if we're aligned with the column one square to the right
		{
			if(eY == y1 || eY == y0 || eY == yn1) //if we're aligned with any of the three rows
			{
				result = true;
			}
		}
		else if(eX == x0)
		{
			if(eY == y1 || eY == yn1)
			{
				result = true;
			}
		}
		else if(eX == xn1)
		{
			if(eY == y1 || eY == y0 || eY == yn1)
			{
				result = true;
			}
		}
		else
		{
			result = false;
		}
		
		return result;
	}
	
}
