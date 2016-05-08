package Enemies;

import dungeonmaster.NonPlayerCharacter;

public class Enemy extends NonPlayerCharacter 
{
	protected float challengeRating;
	
	
	
	public void takeDamage(int damage)
	{
		super.takeDamage(damage);
		
		//enemies don't make death saves
		if(dying)
		{
			this.shouldRemoveSelfFromGame = true;
		}

	}
	
	public void saveVsDeath()
	{
		
	}
	
}
