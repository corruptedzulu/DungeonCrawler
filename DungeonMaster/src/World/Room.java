package World;

import java.util.ArrayList; 

public class Room 
{
	
	private String roomName;
	
	private String roomType;
	
	private int widthSquares;
	private int lengthSquares;
	
	
	//the (x,y) coordinates of the square with the smallest x and y values (aka, lower left corner)
	//these are in the World Coordinates (from the DM's perspective)
	//this means that they are counted in 5ft increments (squares)
	private int xWorldCoor;
	private int yWorldCoor;
	
	private ArrayList<WorldObject> contents;
	
	private ArrayList<Door> doors;
	
	private Floor roomFloor;
	
	
	public Room()
	{
		
	}
	
	
	public void addDoor(Door d)
	{
		
	}
	
	
}
