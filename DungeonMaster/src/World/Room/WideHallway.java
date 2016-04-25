package World.Room;

public class WideHallway extends Hallway 
{
	//Hallways have no number limits and are created to connect
	public WideHallway()
	{
		super();
		
		super.setDungeonEntrance(false);
		super.setRoomName("Wide Hallway");
		super.setRoomType("Wide Hallway");
		
		super.setWidthSquares(3);
				
	}
}
