package World;

import java.util.ArrayList;

public class GameWorld 
{
	
	private int worldWidth;
	private int worldLength;
	
	
	private ArrayList<Room> rooms;
	

	
	public GameWorld()
	{
		
	}
	
	
	public void initializeWorld()
	{
		
	}
	
	
	public void addRoom(Room r)
	{
		rooms.add(r);
	}
	
	
	public Room constructRoom()
	{
		Room room = new Room();
		
		
		
		
		
		
		return room;
	}
	
	
	
	public void buildWorld()
	{
		//create rooms, put in random, non-overlapping locations
		//rooms have random (constrained) sizes
		//type is random, though there are constraints for number of a type in the world
		
		
		//populate rooms if needed
		
		
		
		//connect rooms with corridors
		//path ways prefer to be straight
		//can connect to each other
		
		
		//each doorway maybe selects a random another door in the world and pathfinds to it?
		
		
		
	}
	
	
	
}
