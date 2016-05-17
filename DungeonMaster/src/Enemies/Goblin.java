package Enemies;

import java.util.ArrayList;

import dungeonmaster.*;

public class Goblin extends Enemy
{

	
	
	
	
	public Goblin()
	{
		super();
		
		this.setAlignment("Lawful Evil");
		
		ArmorClass ac = new ArmorClass();
		ac.setAC(15);
		ac.setModifier(0);
		this.setArmorClass(ac);
		
		this.setMaxHP(7);
		
		this.setMovementInSquares(6);
		this.resetTurnSpecificValues();
		
		
		charisma = new Ability("Charisma", 8);
		dexterity = new Ability("Dexterity", 14);
		strength = new Ability("Strength", 8);
		intelligence = new Ability("Intelligence", 10);
		wisdom = new Ability("Wisdom", 8);
		constitution = new Ability("Constitution", 10);	
		
		
		ArrayList<Skill> skills = Skill.getAllSkillsAtDefaultModifiers(8, 14, 10, 10, 8, 8);
		
		//reset Stealth to be +6 (+2 for DEX and +4 for Prof and exp?)
		skills.set(16, new Skill("Stealth", 14, true, 4, false ));
				
				
		this.setSkills(skills);
		
		
		this.challengeRating = 1/4;
		
		
		
		//TODO weapons
		
		meleeWeapon = new Weapon();
		rangedWeapon = new Weapon();
		
		//Scimitar		
		this.meleeWeapon = new Weapon();
		this.meleeWeapon.setName("Scimitar");
		this.meleeWeapon.setDamageType("Slashing");
		this.meleeWeapon.setWeaponType("melee");
		this.meleeWeapon.setDistance(1);
		this.meleeWeapon.setDamageDiceNumber(6);
		this.meleeWeapon.setNumberOfDamageDice(1);
		this.meleeWeapon.setDamageModifier(2);
		this.meleeWeapon.setAttackModifier(4);
		
		//Shortbow
		this.rangedWeapon = new Weapon();
		this.rangedWeapon.setName("Shortbow");
		this.rangedWeapon.setDamageType("Piercing");
		this.rangedWeapon.setWeaponType("ranged");
		this.rangedWeapon.setDistance(20);
		this.rangedWeapon.setDamageDiceNumber(6);
		this.rangedWeapon.setNumberOfDamageDice(1);
		this.rangedWeapon.setDamageModifier(2);
		this.rangedWeapon.setAttackModifier(4);
		
		
	
	}
	
	
	
	
	public Goblin(String string)
	{
		// TODO Auto-generated constructor stub
		
		this();
		
		
		this.armorClass.setAC(17);
		this.setMaxHP(27);
		this.setMovementInSquares(7);
		
		
		
		
		
		
	}




	public void takeTurn()
	{
		
		ArrayList<Square> playerLocations = gw.getPlayerCharacterLocations();
		
		//find enemy locations
		//if enemy is within six squares
		//try to move there
		//and attack melee (Melee Standard Action)
		
		
		//if no enemies are within six squares
		//find nearest
		//attack ranged (Ranged Standard Action)
		
		
	}
	
	public void move()
	{
		
		
		
	}
	
	
	public void attack()
	{
		
		
		
	}
	
	
	//TODO fill these methods for the Goblins to attack with
	
	public int makeAttackRoll(String string) 
	{
	
		if(string.equals("melee"))
		{
			return meleeWeapon.rollToAttack();
		}
		
		if(string.equals("ranged"))
		{
			return rangedWeapon.rollToAttack();
		}
		
		return 0;
	}


	public int makeDamageRoll(String string) 
	{		
		
		if(string.equals("melee"))
		{
			return meleeWeapon.rollDamage();
		}
		
		if(string.equals("ranged"))
		{
			return rangedWeapon.rollDamage();
		}
		
		return 0;
	}


	public int getRangedAttackDistance() 
	{
		return rangedWeapon.getDistance();
	}
	
	
	
}
