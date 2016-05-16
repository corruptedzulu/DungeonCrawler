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

	public int getRangedAttackDistance()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int makeAttackRoll(String string)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public int makeDamageRoll(String string)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
}
