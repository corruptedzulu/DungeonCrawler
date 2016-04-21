package World;

import java.util.ArrayList;
import java.util.Random;

import World.Room.Room;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;

public class GameWorld 
{
	
	private int worldWidth;
	private int worldLength;
	
	private int roomMaxDimension;
	
	private Random rand;
	
	private ArrayList<Room> rooms;
	private ArrayList<String> roomTypes;
	

	
	public GameWorld()
	{
		worldWidth = 1000;
		worldLength = 1000;
		
		rand = new Random();
		roomMaxDimension = 25;
		
		fillArrays();
	}
	
	public GameWorld(int w, int l)
	{
		worldWidth = w;
		worldLength = l;
		
		rand = new Random();
		roomMaxDimension = 25;
		
		fillArrays();
	}
	
	
	public void initializeWorld()
	{
		
	}
	
	private void fillArrays()
	{
		roomTypes = new ArrayList<String>();
		roomTypes.add("Hallway");
		roomTypes.add("Barracks");
		roomTypes.add("Quarters");
		roomTypes.add("StorageCold");
		roomTypes.add("StorageAmbient");
		roomTypes.add("MessHall");
		roomTypes.add("DiningHall");
		roomTypes.add("Chapel");
		roomTypes.add("Temple");
		roomTypes.add("Armory");
		roomTypes.add("Entrance");
		roomTypes.add("GrandHall");
		roomTypes.add("Open");
		roomTypes.add("NarrowHallway");
		roomTypes.add("StandardHallway");
		roomTypes.add("WideHallway");
		roomTypes.add("ExpansiveHallway");
	}
	
	
	public void addRoom(Room r)
	{
		rooms.add(r);
	}
	
	
	public Room constructRoom()
	{
		Room room = new Room();
		
		//private String roomName;
		//private String roomType;
		//private int widthSquares;
		//private int lengthSquares;
		//the (x,y) coordinates of the square with the smallest x and y values (aka, lower left corner)
		//these are in the World Coordinates (from the DM's perspective)
		//this means that they are counted in 5ft increments (squares)
		//private int xWorldCoor;
		//private int yWorldCoor;
		//private ArrayList<WorldObject> contents;
		//private ArrayList<Door> doors;
		//private Floor roomFloor;
		//private boolean dungeonEntrance;
		
		
		room.setLengthSquares(rand.nextInt(this.roomMaxDimension) + 2);
		room.setWidthSquares(rand.nextInt(this.roomMaxDimension) + 2);
		
		
		
		return room;
	}
	
	
	public Room constructHallway()
	{
		Room room = new Room();
		
		
		return room;
	}
	
	public Room constructEntrance()
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
