package World;

import java.util.ArrayList;
import java.util.Random;

import Enemies.Enemy;
import PlayerCharacters.PlayerCharacter;
import World.Room.*;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;
import dungeonmaster.DungeonMaster;
import dungeonmaster.Square;
import dungeonmaster.WorldEntity;

public class GameWorld 
{
	
	private int worldWidth;
	private int worldLength;
	
	private int roomMaxDimension;
	
	private Random rand;
	
	
	//private Room currentRoom;
	
	private ArrayList<Room> rooms;
	private ArrayList<String> roomTypes;
	
	private DungeonMaster dm;
	
	private ArrayList<PlayerCharacter> characters;
	

	
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
	
	
	public GameWorld(String string)
	{
		// TODO Auto-generated constructor stub
		
		
		this();
		
		
		//create 3 rooms for default set up
		
		
		
		
		
	}

	public void initializeWorld()
	{
		
	}
	
	private void fillArrays()
	{
		roomTypes = new ArrayList<String>();
		//roomTypes.add("Hallway");
		roomTypes.add("Barracks");
		roomTypes.add("Quarters");
		roomTypes.add("StorageCold");
		roomTypes.add("StorageAmbient");
		roomTypes.add("MessHall");
		roomTypes.add("DiningHall");
		roomTypes.add("Chapel");
		roomTypes.add("Temple");
		roomTypes.add("Armory");
		//roomTypes.add("Entrance");
		roomTypes.add("GrandHall");
		roomTypes.add("Open");
		//roomTypes.add("NarrowHallway");
		//roomTypes.add("StandardHallway");
		//roomTypes.add("WideHallway");
		//roomTypes.add("ExpansiveHallway");
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
		
		
		//room.setLengthSquares(rand.nextInt(this.roomMaxDimension) + 2);
		//room.setWidthSquares(rand.nextInt(this.roomMaxDimension) + 2);
		
		
		
		//return room;
		
		Random r = new Random();
		
		int roomNumber = r.nextInt(roomTypes.size());
		
		
		/*
			//roomTypes.add("Hallway");
		roomTypes.add("Barracks");
		roomTypes.add("Quarters");
		roomTypes.add("StorageCold");
		roomTypes.add("StorageAmbient");
		roomTypes.add("MessHall");
		roomTypes.add("DiningHall");
		roomTypes.add("Chapel");
		roomTypes.add("Temple");
		roomTypes.add("Armory");
			//roomTypes.add("Entrance");
		roomTypes.add("GrandHall");
		roomTypes.add("Open");
			//roomTypes.add("NarrowHallway");
			//roomTypes.add("StandardHallway");
			//roomTypes.add("WideHallway");
			//roomTypes.add("ExpansiveHallway"); 
		 */
		
		switch(roomNumber)
		{
		case 0:
			
			room = Barracks.create();
			
			
			room.defineRoomSize();
			
			room.placeDoors();
			
			room.populateWithContents();
			
			room.populateWithEnemies();
			
			room.populateWithTreasures();
			
			
			
			
			
			
			//return room;
			
			
			break;
			
		case 1:
			
			break;
			
		case 2:
			
			break;
			
		case 3:
			
			break;
			
		case 4:
			
			break;
			
		case 5:
			
			break;
			
		case 6:
			
			break;
			
		case 7:
			
			break;
			
		case 8:
			
			break;
			
		case 9:
			
			break;
			
		case 10:
			
			break;
			
		case 11:
			
			break;
			
		case 12:
			
			break;
			
		case 13:
			
			break;
			
		case 14:
			
			break;
			
		case 15:
			
			break;
			
		case 16:
			
			break;
			
		case 17:
			
			break;
		}
		
		
		//Quarters q = Quarters.create();
		
		
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
			//create room
			//place room
			//check if overlap
			//move to new location and try to place
			//check if overlap
			//repeat until no overlap (if more than 15 (?) tries, delete the room)
		
		
		//populate rooms if needed
		
		
		
		//connect rooms with corridors
		//path ways prefer to be straight
		//can connect to each other
		
		
		//each doorway maybe selects a random another door in the world and pathfinds to it?
		
		
		
	}

	public void setDM(DungeonMaster dm)
	{
		this.dm = dm;
	}
	
	public void setPCArrayList(ArrayList<PlayerCharacter> pcs)
	{
		this.characters = pcs;
	}
	
	
	public boolean entityCanMoveInDirection(WorldEntity e, String moveInDirection) 
	{
		
		
		switch(moveInDirection)
		{
		case "N":
			
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 added to it (go North), does that new Y coordinate exceed the length of the room?
			//these are zero indexed, so subtract 1 from the Length (just like with subtracting 1 from the size of an array to find the correct index)			
			if(e.getyCoor() + 1 > e.getContainingRoom().getLengthSquares() - 1)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we're on the same X value
				if(e.getxCoor() == we.getxCoor())
				{
					//if we would be on the same Y value
					if(e.getyCoor() + 1 == we.getyCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			//check the world objects in the room
			for(WorldObject wo : e.getContainingRoom().getContents())
			{
				//if we're on the same X value
				if(e.getxCoor() == wo.getxCoor())
				{
					//if we would be on the same Y value
					if(e.getyCoor() + 1 == wo.getyCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			
			
			
			
			break;	
			
		case "S":
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 subtracted from it (go South), does that new Y coordinate become less than zero? Zero is the lowest Y coordinate
			//these are zero indexed
			if(e.getyCoor() - 1 < 0)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we're on the same X value
				if(e.getxCoor() == we.getxCoor())
				{
					//if we would be on the same Y value
					if(e.getyCoor() - 1 == we.getyCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			//check the world objects in the room
			for(WorldObject wo : e.getContainingRoom().getContents())
			{
				//if we're on the same X value
				if(e.getxCoor() == wo.getxCoor())
				{
					//if we would be on the same Y value
					if(e.getyCoor() - 1 == wo.getyCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			
			
			break;
			
		case "E":
			
			
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 added to it (go North), does that new Y coordinate exceed the length of the room?
			//these are zero indexed, so subtract 1 from the Length (just like with subtracting 1 from the size of an array to find the correct index)			
			if(e.getxCoor() + 1 > e.getContainingRoom().getWidthSquares() - 1)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we're on the same X value
				if(e.getyCoor() == we.getyCoor())
				{
					//if we would be on the same Y value
					if(e.getxCoor() + 1 == we.getxCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			//check the world objects in the room
			for(WorldObject wo : e.getContainingRoom().getContents())
			{
				//if we're on the same X value
				if(e.getyCoor() == wo.getyCoor())
				{
					//if we would be on the same Y value
					if(e.getxCoor() + 1 == wo.getxCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			
			
			break;
			
		case "W":
			
			
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 subtracted from it (go South), does that new Y coordinate become less than zero? Zero is the lowest Y coordinate
			//these are zero indexed
			if(e.getxCoor() - 1 < 0)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we're on the same X value
				if(e.getyCoor() == we.getyCoor())
				{
					//if we would be on the same Y value
					if(e.getxCoor() - 1 == we.getxCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			//check the world objects in the room
			for(WorldObject wo : e.getContainingRoom().getContents())
			{
				//if we're on the same X value
				if(e.getyCoor() == wo.getyCoor())
				{
					//if we would be on the same Y value
					if(e.getxCoor() - 1 == wo.getxCoor())
					{
						//we would collide, so disallow move
						return false;
					}
				}
			}
			
			
			
			break;
			
		case "NE":
			
			
			
			break;
			
		case "NW":
			
			
			
			break;
			
		case "SE":
			
			
			
			break;
			
		case "SW":
			
			
			
			break;
		
		
		
		}
		
		
		//no collisions were detected in that direction, so go ahead
		return true;
	}

	public ArrayList<Square> getPlayerCharacterLocations() 
	{
		// TODO Auto-generated method stub
		
		ArrayList<Square> locations = new ArrayList<Square>();
		
		
		for(PlayerCharacter p : characters)
		{
			Square s = new Square(p.getxCoor(), p.getyCoor());
			locations.add(s);
		}
			
		return locations;
	}
	
	
	public ArrayList<Square> getEntityLocations(Room room)
	{
		
		ArrayList<Square> locations = new ArrayList<Square>();
		
		
		for(WorldEntity e : room.getEntities())
		{
			Square s = new Square(e.getxCoor(), e.getyCoor());
			locations.add(s);
		}
			
		return locations;
		
	}

	public ArrayList<Square> getEnemyLocationsInRoom(Room room) 
	{
		// TODO Auto-generated method stub
		
		ArrayList<Square> locations = new ArrayList<Square>();
		
		
		for(WorldEntity e : room.getEntities())
		{
			
			if(e instanceof Enemy)
			{
				Square s = new Square(e.getxCoor(), e.getyCoor());
				locations.add(s);
			}
		}
			
		return locations;
		
	}
	
	
	
	
	public String toString()
	{
		String result = "";
		
		//report width, length of the world
		//report the rooms (call each room's toString)
		
		result += "GW:" + worldLength + "," + worldWidth + ";";
		
		
		for(Room r : rooms)
		{
			result += r.toString();
		}
		
		result += "$$";
		
		return result;
	}

	public ArrayList<WorldEntity> getAllEnemies()
	{
		// TODO Auto-generated method stub
		
		
		ArrayList<WorldEntity> result = new ArrayList<WorldEntity>();
		
		
		for(Room r : rooms)
		{
			result.addAll(r.getEntities());
		}
		
		
		
		return result;
	}
	
	
	
	
}
