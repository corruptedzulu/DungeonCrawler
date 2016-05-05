package dungeonmaster;

public class Ability 
{

	private String name;
	private int score;
	private int modifier;
	
	
	public Ability()
	{
		
	}
	
	public Ability(String n, int s)
	{
		name = n;
		score = s;
		modifier = determineModifier(s);
				
	}
	
	
	private int determineModifier(int s)
	{
		
		if(s == 1)
		{
			return -5;
		}
		else if(s == 2 || s == 3)
		{
			return -4;
		}
		else if(s == 4 || s == 5)
		{
			return -3;
		}
		else if(s == 6 || s == 7)
		{
			return -2;
		}
		else if(s == 8 || s == 9)
		{
			return -1;
		}
		else if(s == 10 || s == 11)
		{
			return 0;
		}
		else if(s == 12 || s == 13)
		{
			return 1;
		}
		else if(s == 14 || s == 15)
		{
			return 2;
		}
		else if(s == 16 || s == 17)
		{
			return 3;
		}
		else if(s == 18 || s == 19)
		{
			return 4;
		}
		
		
		return 5;
		
	}
	
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getModifier() {
		return modifier;
	}
	public void setModifier(int modifier) {
		this.modifier = modifier;
	}
	
	
}
