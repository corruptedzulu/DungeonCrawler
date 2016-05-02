package World.Room;



//BOUNDING BOX IN DEFINED IN THE WORLD COORDINATES
public class BoundingBox 
{

	private int x1, x2, y1, y2;
	
	public BoundingBox()
	{
		
		
	}
	
	public BoundingBox(int xOne, int xTwo, int yOne, int yTwo)
	{
		setXOne(xOne);
		setXTwo(xTwo);
		setYOne(yOne);
		setYTwo(yTwo);
	}
	
	
	public void setXOne(int x)
	{
		x1 = x;
	}
	public void setXTwo(int x)
	{
		x2 = x;
	}
	public void setYOne(int y)
	{
		y1 = y;
	}
	public void setYTwo(int y)
	{
		x2 = y;
	}
	
	public int getXOne()
	{
		return x1;
	}
	public int getXTwo()
	{
		return x2;
	}
	public int getYOne()
	{
		return y1;
	}
	public int getYTwo()
	{
		return y2;
	}
	
	
	public static boolean collides(BoundingBox b1, BoundingBox b2)
	{		
		if (b1.getXTwo() < b2.getXOne() || b1.getXOne() > b2.getXTwo() || b1.getYTwo() < b2.getYOne() || b1.getYOne() > b2.getYTwo())
		{
			return false;
		}

			return true;
	}
	
	
}
