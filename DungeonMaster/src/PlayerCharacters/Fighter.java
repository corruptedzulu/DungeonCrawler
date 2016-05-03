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

public class Fighter extends PlayerCharacter 
{

	
	
	public Fighter()
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
	}
}
