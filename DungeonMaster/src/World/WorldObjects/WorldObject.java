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
	
	private String interactionSkill;
	private int interactionDC;
	
	
	public WorldObject()
	{
		myWorldObjectID = worldObjectID;
		worldObjectID++;
		
		
		width = 0;
		length = 0;
		
		xCoor = 0;
		yCoor = 0;
		
		interactionDC = 0;
		
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
	public String getInteractionSkill()
	{
		return interactionSkill;
	}
	public void setInteractionSkill(String interactionSkill)
	{
		this.interactionSkill = interactionSkill;
	}
	public int getInteractionDC()
	{
		return interactionDC;
	}
	public void setInteractionDC(int interactionDC)
	{
		this.interactionDC = interactionDC;
	}
	
	
	public String toString()
	{
		String result = "";
		
		result += "WorldObject" + myWorldObjectID + ":" + xCoor + "," + yCoor + ";";
		
		
		
		return result;
	}



	public String getApplicableInteractionSkill()
	{
		return getInteractionSkill();
	}



	public boolean meetsInteractionDC(int roll)
	{
		if(roll >= this.interactionDC)
		{
			return true;
		}
		
		return false;
	}



	public void interact(WorldEntity e)
	{
		// TODO Auto-generated method stub
		
	}
	
	
	public boolean isAdjacentTo(WorldObject e)
	{
		boolean result = false;
		
		int x1 = this.xCoor + 1;
		int x0 = this.xCoor;
		int xn1 = this.xCoor - 1;
		
		int y1 = this.yCoor + 1;
		int y0 = this.yCoor;
		int yn1 = this.yCoor - 1;
		
		
		int eX = e.getxCoor();
		int eY = e.getyCoor();
		
		
		if(eX == x1) //if we're aligned with the column one square to the right
		{
			if(eY == y1 || eY == y0 || eY == yn1) //if we're aligned with any of the three rows
			{
				result = true;
			}
		}
		else if(eX == x0)
		{
			if(eY == y1 || eY == yn1)
			{
				result = true;
			}
		}
		else if(eX == xn1)
		{
			if(eY == y1 || eY == y0 || eY == yn1)
			{
				result = true;
			}
		}
		else
		{
			result = false;
		}
		
		return result;
	}
	
	public boolean isAdjacentTo(WorldEntity e)
	{
		boolean result = false;
		
		int x1 = this.xCoor + 1;
		int x0 = this.xCoor;
		int xn1 = this.xCoor - 1;
		
		int y1 = this.yCoor + 1;
		int y0 = this.yCoor;
		int yn1 = this.yCoor - 1;
		
		
		int eX = e.getxCoor();
		int eY = e.getyCoor();
		
		
		if(eX == x1) //if we're aligned with the column one square to the right
		{
			if(eY == y1 || eY == y0 || eY == yn1) //if we're aligned with any of the three rows
			{
				result = true;
			}
		}
		else if(eX == x0)
		{
			if(eY == y1 || eY == yn1)
			{
				result = true;
			}
		}
		else if(eX == xn1)
		{
			if(eY == y1 || eY == y0 || eY == yn1)
			{
				result = true;
			}
		}
		else
		{
			result = false;
		}
		
		return result;
	}








	
}
