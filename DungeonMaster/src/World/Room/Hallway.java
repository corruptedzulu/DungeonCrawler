package World.Room;

import java.util.ArrayList;

import World.Floor;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;

public class Hallway extends Room 
{
	public Hallway()
	{
		super();
		
		super.setDungeonEntrance(false);
		super.setRoomName("Hallway");
		super.setRoomType("Hallway");
		
		super.setWidthSquares(2);
				
	}
		
}
