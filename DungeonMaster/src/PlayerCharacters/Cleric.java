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

public class Cleric extends PlayerCharacter 
{
	
	
	public Cleric()
	{
	
		super();
		
		
		
		this.name = "Cleric";
		this.playerRace = new Human();
		this.alignment = "Lawful Good";
		
		this.playerClass = new EntityClass();
		this.playerClass.setThisInstanceClass(3);//cleric
		
		this.level = 6;
		this.inspiration = false;
		this.proficiencyBonus = 3;
		this.movementInSquares = 6;
		this.movementRemaining = 6;
		
		this.createAbilites();
		this.createSkills();
		this.createSavingThrows();
				
		this.armorClass = new ArmorClass();
		this.armorClass.setAC(14 + this.dexterity.getModifier());
		
		
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
		
		//scale mail
		
		
	}

	private void createInitialWeapons()
	{
		// TODO Auto-generated method stub
		
		//mace		
		this.meleeWeapon = new Weapon();
		this.meleeWeapon.setName("Mace");
		this.meleeWeapon.setDamageType("Bludgeoning");
		this.meleeWeapon.setWeaponType("melee");
		this.meleeWeapon.setDistance(1);
		this.meleeWeapon.setDamageDiceNumber(6);
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
		
		maxHP = 44;
	
		
	}

	private void createSavingThrows()
	{
		// TODO Auto-generated method stub
		
	}

	private void createSkills()
	{
		// TODO Auto-generated method stub
		
		skills = Skill.getAllSkillsAtDefaultModifiers(this.strength.getModifier(), this.dexterity.getModifier(), this.constitution.getModifier(), this.intelligence.getModifier(), this.wisdom.getModifier(), this.charisma.getModifier());
		
		for(Skill s : skills)
		{
			if(s.getName().equals("Arcana"))
			{
				s = new Skill("Arcana", 0, true, this.proficiencyBonus, false);
			}
			if(s.getName().equals("History"))
			{
				s = new Skill("History", 0, true, this.proficiencyBonus, false);
			}
		}
		
	}

	private void createAbilites()
	{
		// TODO Auto-generated method stub
		
		charisma = new Ability("Charisma", 11);
		dexterity = new Ability("Dexterity", 16);
		strength = new Ability("Strength", 16);
		intelligence = new Ability("Intelligence", 11);
		wisdom = new Ability("Wisdom", 18);
		constitution = new Ability("Constitution", 15);	
		
	}

}
