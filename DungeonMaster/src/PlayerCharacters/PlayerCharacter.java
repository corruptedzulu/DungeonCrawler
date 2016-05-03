package PlayerCharacters;
import java.util.ArrayList;
import java.util.Random;

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
import dungeonmaster.WorldEntity;

public class PlayerCharacter extends WorldEntity
{
	
	private String name;
	private String playerName;
	private Race playerRace;
	private String alignment;
	private EntityClass playerClass;
	
	private int level;
	private boolean inspiration;
	private ArmorClass armorClass;
	private int initiativeModifier;
	
	
	private int maxHP;
	private int currentHP;
	private int maxHitDice;
	private int currentHitDice;
	private int hitDiceFaceValue;
	
	private int deathSaveFailures;
	private int deathSaveSuccesses;
	
	
	private ArrayList<SavingThrow> savingThrows;
	private ArrayList<Skill> skills;
	private ArrayList<Ability> abilities;
	
	
	private ArrayList<Treasure> treasures;
	private ArrayList<Weapon> weapons;
	private ArrayList<Item> inventory;
	
	
	private ArrayList<Spell> spells;

	
	public PlayerCharacter()
	{
		rand = new Random();
	}
	
	
	public void takeTurn()
	{
	
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
		return name;
	}


	public void setName(String name) 
	{
		this.name = name;
	}


	public String getPlayerName() 
	{
		return playerName;
	}


	public void setPlayerName(String playerName) 
	{
		this.playerName = playerName;
	}


	public Race getPlayerRace() 
	{
		return playerRace;
	}


	public void setPlayerRace(Race playerRace) 
	{
		this.playerRace = playerRace;
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
		return playerClass;
	}


	public void setPlayerClass(EntityClass playerClass) 
	{
		this.playerClass = playerClass;
	}


	public int getLevel() 
	{
		return level;
	}


	public void setLevel(int level) 
	{
		this.level = level;
	}


	public boolean isInspiration() 
	{
		return inspiration;
	}


	public void setInspiration(boolean inspiration) 
	{
		this.inspiration = inspiration;
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
