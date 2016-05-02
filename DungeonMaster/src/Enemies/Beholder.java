package Enemies;

import java.util.ArrayList;

import Race.Race;
import dungeonmaster.Ability;
import dungeonmaster.ArmorClass;
import dungeonmaster.EntityClass;
import dungeonmaster.Item;
import dungeonmaster.SavingThrow;
import dungeonmaster.Skill;
import dungeonmaster.Spell;
import dungeonmaster.Treasure;
import dungeonmaster.Weapon;

public class Beholder extends Enemy
{

	private ArmorClass armorClass;
	private int initiativeModifier;
	
	private int deathSaveFailures;
	private int deathSaveSuccesses;
	
	
	private ArrayList<SavingThrow> savingThrows;
	private ArrayList<Skill> skills;
	private ArrayList<Ability> abilities;
	
	
	private ArrayList<Treasure> treasures;
	private ArrayList<Weapon> weapons;
	private ArrayList<Item> inventory;
	
	
	private ArrayList<Spell> spells;
	
	
	
	public Beholder()
	{
		this.npcName = "Beholder";
		//this.race = 
		this.alignment = "Lawful Evil";
		//this.npcClass
		this.challengeRating = 13;
		this.armorClass = new ArmorClass();
		this.initiativeModifier = 2;
		
		this.maxHP = 180;
		this.currentHP = 180;
		

	}
	
	
}
