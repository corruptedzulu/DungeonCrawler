package dungeonmaster;

public class Spell 
{

	private String name;
	private String associatedClass;
	private String school;
	
	private int level;
	private boolean requiresSave;
	private SavingThrow saveType;
	private int numberDamageDice;
	private int damageDiceFaceValue;
	
	
	public Spell()
	{
		name = "";
		associatedClass = "";
		school = "";
		
		level = 0;
		requiresSave = false;
		numberDamageDice = 0;
		damageDiceFaceValue = 0;
	}
	
	
	
}
