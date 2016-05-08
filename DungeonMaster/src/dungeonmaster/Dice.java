package dungeonmaster;

import java.util.Random;

public class Dice 
{
	
	private int dNumber;
	private Random r;
	
	public Dice(int faces)
	{
		dNumber = faces;
		r = new Random();
	}
	
	public int roll(int numberToRoll)
	{
		int total = 0;
		
		for(int x = 0; x < numberToRoll; x++)
		{
			total += r.nextInt(dNumber) + 1;
		}
		
		return total;
	}
	

}
