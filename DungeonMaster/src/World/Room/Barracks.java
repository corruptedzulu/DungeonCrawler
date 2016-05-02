package World.Room;

import java.util.ArrayList;

import World.Floor;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;
import dungeonmaster.WorldEntity;

public class Barracks extends Room 
{
	private static int maxNumberInWorld = 10;
	private static Barracks[] rooms = new Barracks[maxNumberInWorld];
	
	
	public Barracks()
	{
		//maxNumberInWorld--;
		
		this.maxLengthSquares = 10;
		this.maxWidthSquares  = 10;
		this.minLengthSquares = 4;
		this.minWidthSquares  = 4;
		this.maxDoors = 4;
		this.minSquaresPerEnemy = 4; //mininum number of squares per enemy, ie you must have at least this many squares per enemy in the room 
		this.maxEnemies = this.maxLengthSquares * this.maxWidthSquares / this.minSquaresPerEnemy;
		this.oddsThatDoorIsSecret = 0.05;
		
		
		this.roomName = "Barracks";
		this.roomType = "Barracks";
		
		
		this.contents = new ArrayList<WorldObject>();
		this.entities = new ArrayList<WorldEntity>();
		this.doors = new ArrayList<Door>();
		
		this.roomFloor = new Floor();
		
		this.dungeonEntrance = false;
		
		
		/*
		 *  protected BoundingBox bb;
			
			protected int widthSquares;
			protected int lengthSquares;
			
			//the (x,y) coordinates of the square with the smallest x and y values (aka, lower left corner)
			//these are in the World Coordinates (from the DM's perspective)
			//this means that they are counted in 5ft increments (squares)
			protected int xWorldCoor;
			protected int yWorldCoor;

		 */
	}
	
	
	public static Barracks create()
	{
		Barracks barracks = null;
		
		if(maxNumberInWorld > 0)
		{
			barracks = new Barracks();
			
			rooms[maxNumberInWorld-1] = barracks; //minus 1. we enter this loop only if maxNumberInWorld is 1 or bigger, but we want it to go into index 0 when it's 1
			
			maxNumberInWorld--;
		}
		
		return barracks;
		
	}
	
	
	
	public void defineRoomSize()
	{
		//take the range of values (max-min) and use that as our nextInt span.
		//add one to that so that the max is included in the range
		//then, add the min back to the answer to shift it back into the range
		
		//eg, max=20 min=10.
		//should be 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20
		//so, this is actually 11 items (20-10+1)
		
		//let it pick rands from 0 (inclusive) to 11 (exclusive)
		//possibles are 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
		
		//add the minimum to those to push them back into the range.
		
		
		this.widthSquares = r.nextInt(maxWidthSquares - minWidthSquares + 1) + minWidthSquares;
		this.lengthSquares = r.nextInt(maxLengthSquares - minLengthSquares + 1) + minLengthSquares;
		
		
		
	}
	
	public void placeDoors()
	{
		
		int numberOfDoors = r.nextInt(maxDoors) + 1;
		int offsetFromZero = 0;//how many spaces from the first square in the lower left corner
		int wallToUseNSEW = 0;//north = 1, south = 2, east = 3, west = 4
		
		for(int x = 0; x < numberOfDoors; x++)
		{
			Door d = new Door();
			
			wallToUseNSEW = r.nextInt(4) + 1; //we use 1,2,3,4 so we add one to make it not zero-indexed
			
			if(wallToUseNSEW <= 2)//north south
			{
				offsetFromZero = r.nextInt(widthSquares); //we're on an x-axis parallel
			}
			else //east west
			{
				offsetFromZero = r.nextInt(lengthSquares); //no need for offset since the lower corner is (0,0)
			}
			
			
			//figure out if the door is a secret door
			if( r.nextDouble() >= 1.0 - this.oddsThatDoorIsSecret)
			{
				d.setDoorType("Secret");
			}
			else
			{
				d.setDoorType("Normal");
			}
			
			
			switch(wallToUseNSEW)
			{
			case 1:
				//north
				
				d.setxRoomOneCoor(offsetFromZero);
				d.setyRoomOneCoor(this.lengthSquares);
				
				break;
			case 2:
				//south
				
				d.setxRoomOneCoor(offsetFromZero);
				d.setyRoomOneCoor(0);
				
				break;
			case 3:
				//east
				
				d.setxRoomOneCoor(this.widthSquares);
				d.setyRoomOneCoor(offsetFromZero);
				
				break;
			case 4:
				//west
				
				d.setxRoomOneCoor(0);
				d.setyRoomOneCoor(offsetFromZero);
				
				break;
			}
			
			
			this.doors.add(d);
			
			
		}
		
	}

	
	public void populateWithContents()
	{
		
	}
	
	
	public void populateWithEnemies()
	{
		
		
		//this.entities.add(); 
	}
	
	
	public void populateWithTreasures()
	{
		
	}
	
	
}
