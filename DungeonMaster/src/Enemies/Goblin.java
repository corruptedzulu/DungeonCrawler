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
		
		
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		abilities.add(new Ability("Strength", 8));
		abilities.add(new Ability("Dexterity", 14));
		abilities.add(new Ability("Constitution", 10));
		abilities.add(new Ability("Intelligence", 10));
		abilities.add(new Ability("Wisdom", 8));
		abilities.add(new Ability("Charisma", 8));
		this.setAbilities(abilities);
		
		
		ArrayList<Skill> skills = Skill.getAllSkillsAtDefaultModifiers(8, 14, 10, 10, 8, 8);
		
		//reset Stealth to be +6 (+2 for DEX and +4 for Prof and exp?)
		skills.set(16, new Skill("Stealth", 14, true, 4, false ));
				
				
		this.setSkills(skills);
		
		
		this.challengeRating = 1/4;
		
		
		ArrayList<Weapon> weapons = new ArrayList<Weapon>();
		
		
		
		
		this.setWeapons(weapons);
		
	
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
	
	
	
}
