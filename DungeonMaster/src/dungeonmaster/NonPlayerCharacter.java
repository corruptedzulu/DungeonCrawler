package dungeonmaster;

import java.util.ArrayList;
import java.util.Random;

import Race.Race;

public class NonPlayerCharacter extends WorldEntity
{

	protected String npcName;
	protected Race race;
	protected String alignment;
	protected EntityClass npcClass;
	
	protected int level;
	protected ArmorClass armorClass;
	protected int initiativeModifier;
	
	protected int deathSaveFailures;
	protected int deathSaveSuccesses;
	
	protected int maxHP;
	protected int currentHP;
	protected int maxHitDice;
	protected int currentHitDice;
	protected int hitDiceFaceValue;
	
	protected ArrayList<SavingThrow> savingThrows;
	protected ArrayList<Skill> skills;
	protected ArrayList<Ability> abilities;
	
	
	protected ArrayList<Treasure> treasures;
	protected ArrayList<Weapon> weapons;
	protected ArrayList<Item> inventory;
	
	
	protected ArrayList<Spell> spells;

	
	public NonPlayerCharacter()
	{
		rand = new Random();
	}
	
	
	public void takeTurn()
	{
		//evaluate available actions
		//move
		//attack
		//spell
	
		//take action
		
		
	}
	
	
	public void attacked()
	{
		
	}
	
	public void attack()
	{
	
	}
	
	public void attackOfOpportunity()
	{
		
	}
	
	public void move()
	{
		
	}
	
	public void dash()
	{
		
	}
	
	public void useSkill()
	{
		
	}
	
	public void makeSave()
	{
		
	}
	
	public void takeDamage()
	{
		
	}
	
	public void saveVsDeath()
	{
		
	}
	
	public int rollInitiative()
	{
		int initiativeRoll = rand.nextInt(20) + 1;
		initiativeRoll += this.initiativeModifier;
		return initiativeRoll;
	}
	

	public String getName() 
	{
		return npcName;
	}


	public void setName(String name) 
	{
		this.npcName = name;
	}


	public Race getPlayerRace() 
	{
		return race;
	}


	public void setPlayerRace(Race playerRace) 
	{
		this.race = playerRace;
	}


	public String getAlignment() 
	{
		return alignment;
	}


	public void setAlignment(String alignment) 
	{
		this.alignment = alignment;
	}


	public EntityClass getPlayerClass() 
	{
		return npcClass;
	}


	public void setPlayerClass(EntityClass playerClass) 
	{
		this.npcClass = playerClass;
	}


	public int getLevel() 
	{
		return level;
	}


	public void setLevel(int level) 
	{
		this.level = level;
	}


	public ArmorClass getArmorClass() 
	{
		return armorClass;
	}


	public void setArmorClass(ArmorClass armorClass) 
	{
		this.armorClass = armorClass;
	}


	public ArrayList<SavingThrow> getSavingThrows() 
	{
		return savingThrows;
	}


	public void setSavingThrows(ArrayList<SavingThrow> savingThrows) 
	{
		this.savingThrows = savingThrows;
	}


	public ArrayList<Skill> getSkills() 
	{
		return skills;
	}


	public void setSkills(ArrayList<Skill> skills) 
	{
		this.skills = skills;
	}


	public ArrayList<Ability> getAbilities() 
	{
		return abilities;
	}


	public void setAbilities(ArrayList<Ability> abilities) 
	{
		this.abilities = abilities;
	}


	
	
	public ArrayList<Treasure> getTreasures() 
	{
		return treasures;
	}

	public void addTreasure(Treasure t) 
	{
		treasures.add(t);
	}

	public void removeTreasure(Treasure t)
	{
		
	}

	
	
	public ArrayList<Weapon> getWeapons() 
	{
		return weapons;
	}


	public void addWeapon(Weapon w) 
	{
		weapons.add(w);
	}
	
	public void removeWeapon(Weapon w)
	{
		
	}

	

	public ArrayList<Item> getInventory() 
	{
		return inventory;
	}

	public void addItem(Item i) 
	{
		inventory.add(i);
	}
	
	public void removeItem(Item i)
	{
		
	}
	
	
	
}
