package World.WorldObjects;

import dungeonmaster.WorldEntity;

import java.util.ArrayList;

import PlayerCharacters.PlayerCharacter;

import dungeonmaster.Treasure;

public class Chest extends WorldObject 
{
	
	private ArrayList<Treasure> treasures; 
	private boolean isOpen;
	
	
	public Chest()
	{
		super();
		
		this.length = 1;
		this.width = 1;
		
		isOpen = false;
		
		treasures = new ArrayList<Treasure>();
		
	}
	
	public String toString()
	{
		String s = null;
		
		s = super.toString();
		
		if(isOpen)
		{
			s += "isOpen;";
		}
		
		
		s += "$$";
		
		return s;
	}
	
	public void interact(WorldEntity entity)
	{
		if(entity instanceof PlayerCharacter)
		{ 
			for(Treasure t : treasures)
			{
				((PlayerCharacter) entity).addTreasure(t);
			}			
		}
	}
	
	
	public void addTreasure(Treasure t)
	{
		treasures.add(t);
	}
	
	public ArrayList<Treasure> getTreasures()
	{
		return treasures;
	}
	
	public void clearTreasures()
	{
		treasures.clear();
	}

}
