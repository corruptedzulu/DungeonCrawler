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

public class Rogue extends PlayerCharacter 
{
	
	
	public Rogue()
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
		
		
		
		this.name = "Rogue";
		//this.playerRace = new Race();
		this.alignment = "True Neutral";
		
		this.playerClass = new EntityClass();
		this.playerClass.setThisInstanceClass(9);//cleric
		
		this.level = 6;
		this.inspiration = false;
		this.proficiencyBonus = 3;
		this.movementInSquares = 6;
		this.movementRemaining = 6;
		
		this.createAbilites();
		this.createSkills();
		this.createSavingThrows();
				
		this.armorClass = new ArmorClass();
		this.armorClass.setAC(17);
		
		
		this.initiativeModifier = this.dexterity.getModifier();
		
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
		//studded leather armor
	}

	private void createInitialWeapons()
	{
		// TODO Auto-generated method stub
	
		
		//mace		
		this.meleeWeapon = new Weapon();
		this.meleeWeapon.setName("Rapier");
		this.meleeWeapon.setDamageType("Slashing");
		this.meleeWeapon.setWeaponType("melee");
		this.meleeWeapon.setDistance(1);
		this.meleeWeapon.setDamageDiceNumber(8);
		this.meleeWeapon.setNumberOfDamageDice(1);
		this.meleeWeapon.setDamageModifier(this.dexterity.getModifier());
		this.meleeWeapon.setAttackModifier(this.proficiencyBonus + this.dexterity.getModifier());
		
		//light crossbow
		this.rangedWeapon = new Weapon();
		this.rangedWeapon.setName("Light crossbow");
		this.rangedWeapon.setDamageType("Piercing");
		this.rangedWeapon.setWeaponType("ranged");
		this.rangedWeapon.setDistance(20);
		this.rangedWeapon.setDamageDiceNumber(8);
		this.rangedWeapon.setNumberOfDamageDice(1);
		this.rangedWeapon.setDamageModifier(this.strength.getModifier());
		this.rangedWeapon.setAttackModifier(this.proficiencyBonus + this.strength.getModifier());
		
		
	}

	private void createInitialTreasures()
	{
		// TODO Auto-generated method stub
		
	}

	private void computeMaxHP()
	{
		// TODO Auto-generated method stub
		maxHP = 35;
	}

	private void createSavingThrows()
	{
		// TODO Auto-generated method stub
		
	}

	private void createSkills()
	{
		// TODO make skill-specific for rogue
		
		skills = Skill.getAllSkillsAtDefaultModifiers(this.strength.getModifier(), this.dexterity.getModifier(), this.constitution.getModifier(), this.intelligence.getModifier(), this.wisdom.getModifier(), this.charisma.getModifier());
		
		for(Skill s : skills)
		{
			if(s.getName().equals("Deception"))
			{
				s = new Skill("Deception", 1, true, this.proficiencyBonus, false);
			}
			if(s.getName().equals("Stealth"))
			{
				s = new Skill("Stealth", 5, true, this.proficiencyBonus, false);
			}
		}
		
	}

	private void createAbilites()
	{
		// TODO change scores to be the rogue's
		
		charisma = new Ability("Charisma", 13);
		dexterity = new Ability("Dexterity", 20);
		strength = new Ability("Strength", 15);
		intelligence = new Ability("Intelligence", 11);
		wisdom = new Ability("Wisdom", 12);
		constitution = new Ability("Constitution", 12);	
		
	}

}
