package World;

import java.util.ArrayList;
import java.util.Random;

import Enemies.Enemy;
import Enemies.Goblin;
import PlayerCharacters.PlayerCharacter;
import World.Room.*;
import World.WorldObjects.Chest;
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
		
		rooms = new ArrayList<Room>();
		characters = new ArrayList<PlayerCharacter>();
		roomTypes = new ArrayList<String>();
		
	}
	
	public GameWorld(int w, int l)
	{
		worldWidth = w;
		worldLength = l;
		
		rand = new Random();
		roomMaxDimension = 25;
		
		fillArrays();
		
		rooms = new ArrayList<Room>();
		characters = new ArrayList<PlayerCharacter>();
		roomTypes = new ArrayList<String>();
	}
	
	
	public GameWorld(String string)
	{	
		
		this();
		
		
		//create 3 rooms for default set up
		
		
		Room room1 = new Room();
		Room room2 = new Room();
		Room room3 = new Room();
		
		rooms.add(room1);
		rooms.add(room2);
		rooms.add(room3);
		
		
		room1.setWidthSquares(8);
		room1.setLengthSquares(12);
		room1.setxWorldCoor(0);
		room1.setyWorldCoor(0);
		
		room2.setWidthSquares(15);
		room2.setLengthSquares(20);
		room2.setxWorldCoor(0);
		room2.setyWorldCoor(12);
		
		room3.setWidthSquares(10);
		room3.setLengthSquares(30);
		room3.setxWorldCoor(0);
		room3.setyWorldCoor(33);
		
		Door door1to2 = new Door();
		Door door2to1 = new Door();
		Door door2to3 = new Door();
		Door door3to2 = new Door();
		
		
		door1to2.setMyRoom(room1);
		door1to2.setRoomIConnectTo(room2);
		door1to2.setxRoomOneCoor(5);
		door1to2.setxRoomTwoCoor(5);
		door1to2.setyRoomOneCoor(room1.getLengthSquares() - 1);
		door1to2.setyRoomTwoCoor(0);

		door2to1.setMyRoom(room2);
		door2to1.setRoomIConnectTo(room1);
		door2to1.setxRoomOneCoor(5);
		door2to1.setxRoomTwoCoor(5);
		door2to1.setyRoomOneCoor(room2.getLengthSquares() - 1);
		door2to1.setyRoomTwoCoor(0);
		
		door2to3.setMyRoom(room2);
		door2to3.setRoomIConnectTo(room3);
		door2to3.setxRoomOneCoor(5);
		door2to3.setxRoomTwoCoor(5);
		door2to3.setyRoomOneCoor(room2.getLengthSquares() - 1);
		door2to3.setyRoomTwoCoor(0);
		
		door3to2.setMyRoom(room3);
		door3to2.setRoomIConnectTo(room2);
		door3to2.setxRoomOneCoor(5);
		door3to2.setxRoomTwoCoor(5);
		door3to2.setyRoomOneCoor(room3.getLengthSquares() - 1);
		door3to2.setyRoomTwoCoor(0);
		
		
		room1.getDoors().add(door1to2);
		room2.getDoors().add(door2to1);
		room2.getDoors().add(door2to3);
		room3.getDoors().add(door3to2);
		
		
		
		//create enemies
		
		
		for(int x = 0; x < 3; x++)
		{
			Goblin g = new Goblin();
			g.setxCoor(x * 2);
			g.setyCoor(5);
			room1.getEntities().add(g);
			g.setContainingRoom(room1);
		}
		
		for(int x = 0; x < 6; x++)
		{
			Goblin g = new Goblin();
			g.setxCoor( (x * 2) + 4);
			g.setyCoor(10);
			room2.getEntities().add(g);
			g.setContainingRoom(room2);
		}
		
		for(int x = 0; x < 2; x++)
		{
			Goblin g = new Goblin();
			g.setxCoor(x * 2);
			g.setyCoor(15);
			room3.getEntities().add(g);
			g.setContainingRoom(room3);
		}
		
		
		Goblin g = new Goblin("big");
		g.setxCoor(5);
		g.setyCoor(20);
		room3.getEntities().add(g);
		g.setContainingRoom(room3);
		
		Chest c = new Chest();
		c.setxCoor(5);
		c.setyCoor(25);
		
		room3.getContents().add(c);
		
		
		
		//create objects
		
		
		
		/*	
		
		protected ArrayList<WorldObject> contents;
		protected ArrayList<WorldEntity> entities;
		*/
		
		
	}

	public Room getRoomOne()
	{
		return rooms.get(0);
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

			//check the walls of the room
			
			//if the current Y coordinate has 1 added to it (go North), does that new Y coordinate exceed the length of the room?
			//these are zero indexed, so subtract 1 from the Length (just like with subtracting 1 from the size of an array to find the correct index)			
			if(e.getxCoor() + 1 > e.getContainingRoom().getWidthSquares() - 1 || e.getyCoor() + 1 > e.getContainingRoom().getLengthSquares() - 1)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we'd be on the same Y value
				if(e.getyCoor() + 1 == we.getyCoor())
				{
					//if we would be on the same X value
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
				if(e.getyCoor() + 1 == wo.getyCoor())
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
			
		case "NW":
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 added to it (go North), does that new Y coordinate exceed the length of the room?
			//these are zero indexed, so subtract 1 from the Length (just like with subtracting 1 from the size of an array to find the correct index)			
			if(e.getxCoor() - 1 < 0 || e.getyCoor() + 1 > e.getContainingRoom().getLengthSquares() - 1)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we'd be on the same Y value
				if(e.getyCoor() + 1 == we.getyCoor())
				{
					//if we would be on the same X value
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
				if(e.getyCoor() + 1 == wo.getyCoor())
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
			
		case "SE":
			
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 added to it (go North), does that new Y coordinate exceed the length of the room?
			//these are zero indexed, so subtract 1 from the Length (just like with subtracting 1 from the size of an array to find the correct index)			
			if(e.getxCoor() + 1 > e.getContainingRoom().getWidthSquares() - 1 || e.getyCoor() - 1 < 0)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we'd be on the same Y value
				if(e.getyCoor() - 1 == we.getyCoor())
				{
					//if we would be on the same X value
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
				if(e.getyCoor() - 1 == wo.getyCoor())
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
			
			
		case "SW":
			
			//check the walls of the room
			
			//if the current Y coordinate has 1 added to it (go North), does that new Y coordinate exceed the length of the room?
			//these are zero indexed, so subtract 1 from the Length (just like with subtracting 1 from the size of an array to find the correct index)			
			if(e.getxCoor() - 1 < 0 || e.getyCoor() - 1 < 0)
			{
				return false;
			}
			
			
			//check the other entities in the room
			for(WorldEntity we : e.getContainingRoom().getEntities())
			{
				//if we'd be on the same Y value
				if(e.getyCoor() - 1 == we.getyCoor())
				{
					//if we would be on the same X value
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
				if(e.getyCoor() - 1 == wo.getyCoor())
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

	public ArrayList<WorldObject> getWorldObjectsAdjacentToMe(WorldEntity e)
	{
		// TODO Auto-generated method stub
		
		ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
		
		//int eX = e.getxCoor();
		//int eY = e.getyCoor();
		
		ArrayList<WorldObject> allRoomObjects = e.getContainingRoom().getContents();
		
		
		if(e instanceof PlayerCharacter)
		{	
			for(WorldObject wo : allRoomObjects)
			{
				if(e.isAdjacentTo(wo))
				{
					objects.add(wo);
				}
			}	
		}
		
		return objects;
		
	}
	
	
	
	
}
