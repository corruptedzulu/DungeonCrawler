package PlayerCharacters;

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

public class Cleric extends PlayerCharacter 
{
	
	
	public Cleric()
	{
		/*
		private String playerName;
		private Race playerRace;
		
		private int level;
		private ArmorClass armorClass;
		private int initiativeModifier;
		
		
		private int maxHP;
		private int currentHP;
		private int maxHitDice;
		private int currentHitDice;
		private int hitDiceFaceValue;
		
		
		private ArrayList<SavingThrow> savingThrows;
		private ArrayList<Skill> skills;
		private ArrayList<Ability> abilities;
		
		
		private ArrayList<Treasure> treasures;
		private ArrayList<Weapon> weapons;
		private ArrayList<Item> inventory;
		
		
		private ArrayList<Spell> spells;*/
		
		
		
		super();
		
		
		
		this.name = "Cleric";
		//this.playerRace = new Race();
		this.alignment = "Lawful Good";
		
		this.playerClass = new EntityClass();
		this.playerClass.setThisInstanceClass(3);//cleric
		
		this.level = 6;
		this.inspiration = false;
		
		
		this.createAbilites();
		this.createSkills();
		this.createSavingThrows();
				
		this.armorClass = new ArmorClass();
		//this.armorClass.setAC(ac);
		
		
		//this.initiativeModifier = s
		
		this.computeMaxHP();
		this.currentHP = maxHP;
		this.maxHitDice = this.level;
		this.currentHitDice = this.maxHitDice;
		this.hitDiceFaceValue = this.playerClass.getHitDiceFaceValue();
		
		
		this.createInitialTreasures();
		this.createInitialWeapons();
		this.createInitialItems();
		
		this.createSpells();
		
		
	}

	private void createSpells()
	{
		// TODO Auto-generated method stub
		
	}

	private void createInitialItems()
	{
		// TODO Auto-generated method stub
		
	}

	private void createInitialWeapons()
	{
		// TODO Auto-generated method stub
		
	}

	private void createInitialTreasures()
	{
		// TODO Auto-generated method stub
		
	}

	private void computeMaxHP()
	{
		// TODO Auto-generated method stub
		
	}

	private void createSavingThrows()
	{
		// TODO Auto-generated method stub
		
	}

	private void createSkills()
	{
		// TODO Auto-generated method stub
		
	}

	private void createAbilites()
	{
		// TODO Auto-generated method stub
		
	}

}
