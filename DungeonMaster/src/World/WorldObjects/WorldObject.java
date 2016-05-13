package World.WorldObjects;

import dungeonmaster.Skill;
import dungeonmaster.WorldEntity;

public class WorldObject 
{

	protected int width;
	protected int length;
	
	protected int xCoor;
	protected int yCoor;
	
	
	public static int worldObjectID = 0;
	protected int myWorldObjectID;
	
	
	
	public WorldObject()
	{
		myWorldObjectID = worldObjectID;
		worldObjectID++;
		
	}
	
	
	
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
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
	
	
	public String toString()
	{
		String result = "";
		
		result += "WorldObject" + myWorldObjectID + ":" + xCoor + "," + yCoor + ";" + "$$";
		
		
		
		return result;
	}



	public Skill getApplicableInteractionSkill()
	{
		// TODO Auto-generated method stub
		return null;
	}



	public boolean meetsInteractionDC(int roll)
	{
		// TODO Auto-generated method stub
		return false;
	}



	public void interact(WorldEntity e)
	{
		// TODO Auto-generated method stub
		
	}
	
	
}
