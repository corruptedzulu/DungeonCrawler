package dungeonmaster;
import java.util.Random;

import World.GameWorld;

public abstract class WorldEntity 
{

	protected int xCoor;
	protected int yCoor;
	
	protected Random rand;
	
	protected GameWorld gw;
	

	public void setGameWorld(GameWorld gw)
	{
		this.gw = gw;
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
	
}
