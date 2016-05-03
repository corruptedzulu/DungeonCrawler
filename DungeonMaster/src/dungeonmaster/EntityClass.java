package dungeonmaster;

public class EntityClass 
{
	public static String classes[] = {"Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Warlock", "Wizard"};
	public static int classesIndex[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
	
	private String thisClass;
	
	public EntityClass()
	{
		
	}
	
	public String[] getClasses()
	{
		return classes;
	}
	
	public void setThisInstanceClass(int index)
	{
		thisClass = classes[index];
	}
	
	public String getThisInstanceClass()
	{
		return thisClass;
	}
	
}
