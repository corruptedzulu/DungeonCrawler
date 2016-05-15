package World.Room;

import java.util.ArrayList;
import java.util.Random;

import Enemies.Enemy;
import World.Floor;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;
import dungeonmaster.WorldEntity; 


public class Room 
{
	protected Random r;
	
	
	public static int roomID = 0;
	
	protected int myRoomID;
	
	
	protected BoundingBox bb;
	protected String roomName;
	
	protected String roomType;
	
	protected int widthSquares;
	protected int lengthSquares;
	
	protected int minWidthSquares;
	protected int maxWidthSquares;
	protected int minLengthSquares;
	protected int maxLengthSquares;
	protected int maxDoors;
	protected double oddsThatDoorIsSecret;
	protected int maxEnemies;
	protected int minSquaresPerEnemy;
	protected int maxTreasures;
	
	
	//the (x,y) coordinates of the square with the smallest x and y values (aka, lower left corner)
	//these are in the World Coordinates (from the DM's perspective)
	//this means that they are counted in 5ft increments (squares)
	//the lowest left corner square is defined as (0,0)
	protected int xWorldCoor;


	protected int yWorldCoor;
	
	protected ArrayList<WorldObject> contents;
	protected ArrayList<WorldEntity> entities;
	
	protected ArrayList<Door> doors;
	
	protected Floor roomFloor;
	
	
	protected boolean dungeonEntrance;
	
	
	public Room()
	{
		//room is constructed by whatever is creating an instance of it. no self-constructing
		contents = new ArrayList<WorldObject>();
		entities = new ArrayList<WorldEntity>();
		doors = new ArrayList<Door>();
		
		myRoomID = roomID;
		roomID++;
		
		
		r = new Random();		
		
		roomName = "";
		
		roomType = "";
		
		widthSquares = 0;
		lengthSquares = 0;
		
		minWidthSquares = 0;
		maxWidthSquares = 0;
		minLengthSquares = 0;
		maxLengthSquares = 0;
		maxDoors = 0;
		oddsThatDoorIsSecret = 0.0;
		maxEnemies = 0;
		minSquaresPerEnemy = 0;
		maxTreasures = 0;

		xWorldCoor = 0;
		yWorldCoor = 0;
		
		dungeonEntrance = false;
		
	}
	
	public Room(String scriptedConstruction)
	{
		
	}
	
	
	public void buildSelf()
	{
		
	}
	
	
	public void addDoor(Door d)
	{
		
	}


	public String getRoomName() {
		return roomName;
	}


	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}


	public String getRoomType() {
		return roomType;
	}


	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}


	public int getWidthSquares() {
		return widthSquares;
	}


	public void setWidthSquares(int widthSquares) {
		this.widthSquares = widthSquares;
	}


	public int getLengthSquares() {
		return lengthSquares;
	}


	public void setLengthSquares(int lengthSquares) {
		this.lengthSquares = lengthSquares;
	}


	public int getxWorldCoor() {
		return xWorldCoor;
	}


	public void setxWorldCoor(int xWorldCoor) {
		this.xWorldCoor = xWorldCoor;
	}


	public int getyWorldCoor() {
		return yWorldCoor;
	}


	public void setyWorldCoor(int yWorldCoor) {
		this.yWorldCoor = yWorldCoor;
	}


	public ArrayList<WorldObject> getContents() {
		return contents;
	}


	public void setContents(ArrayList<WorldObject> contents) {
		this.contents = contents;
	}

	public ArrayList<Door> getDoors() {
		return doors;
	}


	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}


	public Floor getRoomFloor() {
		return roomFloor;
	}


	public void setRoomFloor(Floor roomFloor) {
		this.roomFloor = roomFloor;
	}


	public boolean isDungeonEntrance() {
		return dungeonEntrance;
	}


	public void setDungeonEntrance(boolean dungeonEntrance) {
		this.dungeonEntrance = dungeonEntrance;
	}

	public void defineRoomSize() {
		// TODO Auto-generated method stub
		
	}

	public void placeDoors() {
		// TODO Auto-generated method stub
		
	}

	public void populateWithContents() {
		// TODO Auto-generated method stub
		
	}

	public void populateWithEnemies() {
		// TODO Auto-generated method stub
		
	}

	public void populateWithTreasures() {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<WorldEntity> getEntities() {
		return entities;
	}

	public void setEntities(ArrayList<WorldEntity> entities) {
		this.entities = entities;
	}

	public void removeEntity(Enemy attackedEnemy) 
	{
		// TODO Auto-generated method stub
		
		entities.remove(attackedEnemy);	
		
	}
	
	
	public String toString()
	{
		String result = "";
		
		
		result += "Room" + myRoomID + ":" + roomType + ";";
		result += widthSquares + "," + lengthSquares + ";";
		result += xWorldCoor + "," + yWorldCoor + ";";
		
		
		for(WorldEntity we : entities)
		{
			result += we.toString();
			
		}
		result += ";";
		
		
		for(WorldObject wo : contents)
		{
			result += wo.toString();
		}
		result += ";";
		
		
		for(Door d : doors)
		{
			result += d.toString();
		}
		result += ";";
		
		
		if(dungeonEntrance)
		{
			result += "true;";
		}
		else
		{
			result += "false;";
		}
		 
		
		
		result += "$$";
		
		return result;
	}
	
	
}
