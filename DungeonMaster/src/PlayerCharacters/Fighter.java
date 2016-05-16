package PlayerCharacters;

import java.util.ArrayList;

import Race.Human;
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

public class Fighter extends PlayerCharacter 
{

	
	
	public Fighter()
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
		
		
		
		this.name = "Fighter";
		this.playerRace = new Human();
		this.alignment = "Chaotic Good";
		
		this.playerClass = new EntityClass();
		this.playerClass.setThisInstanceClass(5);//cleric
		
		this.level = 6;
		this.inspiration = false;
		this.proficiencyBonus = 3;
		this.movementInSquares = 6;
		this.movementRemaining = 6;
		
		
		this.createAbilites();
		this.createSkills();
		this.createSavingThrows();
				
		this.armorClass = new ArmorClass();
		this.armorClass.setAC(19);
		
		
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
		
		
		//splint mail
		//shield
		
		
	}

	private void createInitialWeapons()
	{
		// TODO Auto-generated method stub
	
		
		
		//mace		
		this.meleeWeapon = new Weapon();
		this.meleeWeapon.setName("Greatsword");
		this.meleeWeapon.setDamageType("Slashing");
		this.meleeWeapon.setWeaponType("melee");
		this.meleeWeapon.setDistance(1);
		this.meleeWeapon.setDamageDiceNumber(12);
		this.meleeWeapon.setNumberOfDamageDice(1);
		this.meleeWeapon.setDamageModifier(this.strength.getModifier());
		this.meleeWeapon.setAttackModifier(this.proficiencyBonus + this.strength.getModifier());
		
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
		maxHP = 49;
	}

	private void createSavingThrows()
	{
		// TODO Auto-generated method stub
		
	}

	private void createSkills()
	{
		// TODO make skill-specific for fighter
		
		skills = Skill.getAllSkillsAtDefaultModifiers(this.strength.getModifier(), this.dexterity.getModifier(), this.constitution.getModifier(), this.intelligence.getModifier(), this.wisdom.getModifier(), this.charisma.getModifier());
		
		for(Skill s : skills)
		{
			if(s.getName().equals("Animal Handling"))
			{
				s = new Skill("Animal Handling", 2, true, this.proficiencyBonus, false);
			}
			if(s.getName().equals("Survival"))
			{
				s = new Skill("Survival", 2, true, this.proficiencyBonus, false);
			}
		}
		
	}

	private void createAbilites()
	{
		// TODO change scores to be the fighter's
		
		charisma = new Ability("Charisma", 16);
		dexterity = new Ability("Dexterity", 8);
		strength = new Ability("Strength", 20);
		intelligence = new Ability("Intelligence", 17);
		wisdom = new Ability("Wisdom", 14);
		constitution = new Ability("Constitution", 17);	
		
	}
}
