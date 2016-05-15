package PlayerCharacters;
import java.util.ArrayList;
import java.util.Random;

import Race.Race;
import World.WorldObjects.Door;
import dungeonmaster.Ability;
import dungeonmaster.ArmorClass;
import dungeonmaster.Dice;
import dungeonmaster.EntityClass;
import dungeonmaster.Item;
import dungeonmaster.SavingThrow;
import dungeonmaster.NonPlayerCharacter;
import dungeonmaster.Skill;
import dungeonmaster.Spell;
import dungeonmaster.Treasure;
import dungeonmaster.Weapon;
import dungeonmaster.WorldEntity;

public class PlayerCharacter extends WorldEntity implements Comparable
{
	
	protected String name;
	protected String playerName;
	protected Race playerRace;
	protected String alignment;
	protected EntityClass playerClass;
	
	protected int level;
	protected boolean inspiration;
	protected ArmorClass armorClass;
	protected int initiativeModifier;
	protected int currentInitiative;
	
	
	protected int maxHP;
	protected int currentHP;
	protected int maxHitDice;
	protected int currentHitDice;
	protected int hitDiceFaceValue;
	
	protected int deathSaveFailures;
	protected int deathSaveSuccesses;
	protected boolean dying;
	protected boolean dead;

	protected int movementInSquares;
	protected int movementRemaining;
	
	protected int visionDistanceSquares;
	
	
	protected ArrayList<SavingThrow> savingThrows;
	protected ArrayList<Skill> skills;
	protected ArrayList<Ability> abilities;
	
	
	protected ArrayList<Treasure> treasures;
	protected ArrayList<Weapon> weapons;
	protected Weapon meleeWeapon;
	protected Weapon rangedWeapon;
	protected ArrayList<Item> inventory;
	
	
	protected ArrayList<Spell> spells;

	
	public PlayerCharacter()
	{
		
		super();
		
		dying = false;
		dead = false;
		deathSaveFailures = 0;
		deathSaveSuccesses = 0;
		
		rand = new Random();
		
		this.resetTurnSpecificValues();
	}
	
	
	public void resetTurnSpecificValues()
	{
		
		movementRemaining = movementInSquares;
		
	}
	
	public String toString()
	{
		String s = null;
		
		s = "WorldEntity" + myWorldEntityID + ":" + xCoor + "," + yCoor + ";";
		
		
		if(dead)
		{
			s += "dead;";
		}
		if(dying)
		{
			s += "dying";
		}
		
		s += "$$";
		
		return s;
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
	
	public void move(String moveInDirection)
	{
		
		//TODO make move in direction
		
		switch (moveInDirection)
		{
		case "N":
			
			this.setxCoor(this.getxCoor());
			this.setyCoor(this.getyCoor() + 1);
			
			
			break;
			
		case "S":
			
			this.setxCoor(this.getxCoor());
			this.setyCoor(this.getyCoor() - 1);
			
			
			break;
			
		case "E":
			
			this.setxCoor(this.getxCoor() + 1);
			this.setyCoor(this.getyCoor());
			
			
			break;
			
		case "W":
			
			this.setxCoor(this.getxCoor() - 1);
			this.setyCoor(this.getyCoor());
			
			
			break;
			
		case "NE":
			
			this.setxCoor(this.getxCoor() + 1);
			this.setyCoor(this.getyCoor() + 1);
			
			
			break;
			
		case "SE":
			
			this.setxCoor(this.getxCoor() + 1);
			this.setyCoor(this.getyCoor() - 1);
			
			
			break;
			
		case "NW":
			
			this.setxCoor(this.getxCoor() - 1);
			this.setyCoor(this.getyCoor() + 1);
			
			
			break;
			
		case "SW":
			
			this.setxCoor(this.getxCoor() - 1);
			this.setyCoor(this.getyCoor() - 1);
			
			break;
			
	
		}
		
	}
	
	public void dash()
	{
		
	}
	
	public int useSkill(Skill skill)
	{
		//TODO fill in
		
		return 0;
	}
	
	public void makeSave()
	{
		
	}
	
	public void takeDamage(int damage)
	{
		this.currentHP = this.currentHP - damage;
		
		if(currentHP < 1)
		{
			dying = true;
		}
	}
	
	public void saveVsDeath()
	{
		Dice d = new Dice(20);
		
		
		if(d.roll(1) >= 10)
		{
			this.deathSaveSuccesses++;
		}
		else
		{
			this.deathSaveFailures++;
		}
		
		if(deathSaveFailures == 3)
		{
			this.dead = true;
			this.dying = false;//can't be dying if we're already dead
		}
		
		if(deathSaveSuccesses == 3)
		{
			this.dying = false;
		}
		
		
	}
	
	public int getDeathSaveFailures()
	{
		return deathSaveFailures;
	}

	public int getDeathSaveSuccesses()
	{
		return deathSaveSuccesses;
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public boolean isDying()
	{
		return dying;
	}

	public int rollInitiative()
	{
		currentInitiative = 0;
		int initiativeRoll = rand.nextInt(20) + 1;
		initiativeRoll += this.initiativeModifier;
		return initiativeRoll;
	}
	
	
	public void openDoor(Door door)
	{
		
	}
	
	public void lootWorldEntity(WorldEntity entity)
	{
		
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


	public void setMovementInSquares(int squares)
	{
		this.movementInSquares = squares;
	}
	
	public int getMovementInSquares()
	{
		return this.movementInSquares;
	}
	
	public int getMovementRemaining()
	{
		return this.movementRemaining;
	}
	
	public boolean moveOneSquare()
	{
		if(movementRemaining > 0)
		{
			movementRemaining = movementRemaining - 1;
			return true;
		}
		
		return false;
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


	public int makeAttackRoll(String string) 
	{
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
		
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
		// TODO Auto-generated method stub
		return rangedWeapon.getDistance();
	}


	public int getCurrentInitiative()
	{
		// TODO Auto-generated method stub
		return currentInitiative;
	}
	
	
	@Override
	public int compareTo(Object o)
	{
		// TODO Auto-generated method stub
		
		
		if(o instanceof NonPlayerCharacter)
		{
			return (this.currentInitiative - ((NonPlayerCharacter) o).getCurrentInitiative());
		}
		
		if(o instanceof PlayerCharacter)
		{
			return (this.currentInitiative - ((PlayerCharacter) o).getCurrentInitiative());
		}
		
		
		return 0;
	}

	
}
