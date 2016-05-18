package dungeonmaster;

import java.util.ArrayList;
import java.util.Random;

import PlayerCharacters.PlayerCharacter;
import Race.Race;

public class NonPlayerCharacter extends WorldEntity implements Comparable
{
	
	//protected RangedAttack ranged;
	//protected MeleeAttack melee;
	//protected SpellCast spellCast;

	protected String npcName;
	protected Race race;
	protected String alignment;
	protected EntityClass npcClass;
	
	protected int level;
	protected ArmorClass armorClass;
	protected int initiativeModifier;
	protected int currentInitiative;
	
	protected boolean dying;
	protected boolean dead;
	protected int deathSaveFailures;
	protected int deathSaveSuccesses;
	
	protected int maxHP;
	protected int currentHP;
	protected int maxHitDice;
	protected int currentHitDice;
	protected int hitDiceFaceValue;
	
	
	protected int movementInSquares;
	protected int movementRemaining;
	
	
	protected ArrayList<SavingThrow> savingThrows;
	protected ArrayList<Skill> skills;
	
	
	protected Ability strength;
	protected Ability dexterity;
	protected Ability constitution;
	protected Ability intelligence;
	protected Ability wisdom;
	protected Ability charisma;
	
	
	protected ArrayList<Treasure> treasures;
	protected ArrayList<Weapon> weapons;
	protected ArrayList<Item> inventory;
	
	protected Weapon meleeWeapon;
	protected Weapon rangedWeapon;
	
	
	protected ArrayList<Spell> spells;

	
	public NonPlayerCharacter()
	{
		super();
		
		dying = false;
		dead = false;
		npcName = "";
		
		level = 0;
		initiativeModifier = 0;
		currentInitiative = 0;
		deathSaveFailures = 0;
		deathSaveSuccesses = 0;
		maxHP = 0;
		currentHP = 0;
		maxHitDice = 0;
		currentHitDice = 0;
		hitDiceFaceValue = 0;
		
		movementInSquares = 0;
		movementRemaining = 0;
		
		rand = new Random();
		
		this.resetTurnSpecificValues();
		
		savingThrows = new ArrayList<SavingThrow>();
		skills = new ArrayList<Skill>();
		treasures = new ArrayList<Treasure>();
		weapons = new ArrayList<Weapon>();
		inventory = new ArrayList<Item>();
		spells = new ArrayList<Spell>();
		
		
	}
	
	public void resetTurnSpecificValues()
	{
		
		movementRemaining = movementInSquares;
		
	}
	
	
	public String toString()
	{
		String s = super.toString();
		
		//s += race + ";";
		
		if(shouldRemoveSelfFromGame)
		{
			s += "shouldRemoveSelfFromGame;";
		}
		
		s += "$$";
		
		return s;
		
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
	
	public void useSkill()
	{
		
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
		
	}
	
	public boolean isDead()
	{
		return dead;
	}
	
	public int rollInitiative()
	{
		currentInitiative = 0;
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


	public String getNpcName() {
		return npcName;
	}


	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}


	public Race getRace() {
		return race;
	}


	public void setRace(Race race) {
		this.race = race;
	}


	public EntityClass getNpcClass() {
		return npcClass;
	}


	public void setNpcClass(EntityClass npcClass) {
		this.npcClass = npcClass;
	}


	public int getInitiativeModifier() {
		return initiativeModifier;
	}


	public void setInitiativeModifier(int initiativeModifier) {
		this.initiativeModifier = initiativeModifier;
	}


	public int getDeathSaveFailures() {
		return deathSaveFailures;
	}


	public void setDeathSaveFailures(int deathSaveFailures) {
		this.deathSaveFailures = deathSaveFailures;
	}


	public int getDeathSaveSuccesses() {
		return deathSaveSuccesses;
	}


	public void setDeathSaveSuccesses(int deathSaveSuccesses) {
		this.deathSaveSuccesses = deathSaveSuccesses;
	}


	public int getMaxHP() {
		return maxHP;
	}


	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}


	public int getCurrentHP() {
		return currentHP;
	}


	public void setCurrentHP(int currentHP) {
		this.currentHP = currentHP;
	}


	public int getMaxHitDice() {
		return maxHitDice;
	}


	public void setMaxHitDice(int maxHitDice) {
		this.maxHitDice = maxHitDice;
	}


	public int getCurrentHitDice() {
		return currentHitDice;
	}


	public void setCurrentHitDice(int currentHitDice) {
		this.currentHitDice = currentHitDice;
	}


	public int getHitDiceFaceValue() {
		return hitDiceFaceValue;
	}


	public void setHitDiceFaceValue(int hitDiceFaceValue) {
		this.hitDiceFaceValue = hitDiceFaceValue;
	}


	public ArrayList<Spell> getSpells() {
		return spells;
	}


	public void setSpells(ArrayList<Spell> spells) {
		this.spells = spells;
	}


	public void setMovementRemaining(int movementRemaining) {
		this.movementRemaining = movementRemaining;
	}


	public void setTreasures(ArrayList<Treasure> treasures) {
		this.treasures = treasures;
	}


	public void setWeapons(ArrayList<Weapon> weapons) {
		this.weapons = weapons;
	}


	public void setInventory(ArrayList<Item> inventory) {
		this.inventory = inventory;
	}

	public int getCurrentInitiative()
	{
		return currentInitiative;
	}
	
	@Override
	public int compareTo(Object o)
	{	
		
		if(o instanceof NonPlayerCharacter)
		{
			return (this.currentInitiative - ((NonPlayerCharacter) o).currentInitiative);
		}
		
		if(o instanceof PlayerCharacter)
		{
			return (this.currentInitiative - ((PlayerCharacter) o).getCurrentInitiative());
		}
		
		
		return 0;
	}
	
	
	
}
