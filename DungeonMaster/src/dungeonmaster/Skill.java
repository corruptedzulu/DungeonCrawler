package dungeonmaster;

import java.util.ArrayList;

public class Skill 
{

	private String name;
	private String ability;
	private int modifier;
	
	private boolean proficient;
	private boolean expertise;
	
	public static String[] allSkills = { "Acrobatics", "Animal Handling", "Arcana", 
										  "Athletics", "Deception", "History",
										  "Insight", "Intimidation", "Investigation", 
										  "Medicine", "Nature", "Perception", 
										  "Performance", "Persuasion", "Religion", 
										  "Sleight of Hand", "Stealth", "Survival"};
	
	public static String[] allSkillApplicableAbilities = {"Dexterity", "Wisdom", "Intelligence", 
														   "Strength", "Charisma", "Intelligence", 
														   "Wisdom", "Charisma", "Intelligence", 
														   "Wisdom", "Intelligence", "Wisdom", 
														   "Charisma", "Charisma", "Intelligence", 
														   "Dexterity", "Dexterity", "Wisdom"};
	
	
	public static ArrayList<Skill> getAllSkillsAtDefaultModifiers(int str, int dex, int con, int intel, int wis, int cha)
	{
		ArrayList<Skill> skills = new ArrayList<Skill>();
		
		Skill sk;
		
		sk = new Skill(allSkills[0], dex , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[1], wis , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[2], intel , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[3], str , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[4], cha , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[5], intel , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[6], wis , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[7], cha , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[8], intel , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[9], wis , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[10], intel , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[11], wis , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[12], cha , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[13], cha , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[14], intel , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[15], dex , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[16], dex , false, 0, false);
		skills.add(sk);
		sk = new Skill(allSkills[17], wis , false, 0, false);
		skills.add(sk);
		
		return skills;
		
	}
	
	
	public Skill()
	{
		
	}
	
	public Skill(String n, int m, boolean prof, int profBonus, boolean exp)
	{
		name = n;
		modifier = m;
		proficient = prof;
		
		if(proficient)
		{
			modifier = modifier + profBonus;
		}
		
		expertise = exp;
	}
	
	public int getModifier()
	{
		return modifier;
	}

	public boolean getProficient()
	{
		return proficient;
	}
	
	public boolean getExpertise()
	{
		return expertise;
	}
	
	public String getName() {
		return name;
	}
	
	
}
