package dungeonmaster;

public class ArmorClass 
{

	private int ac;
	private int modifier;
	
	
	public ArmorClass()
	{
		ac = 0;
		modifier = 0;
	}


	public int getAC() {
		return ac;
	}


	public void setAC(int ac) {
		this.ac = ac;
	}


	public int getModifier() {
		return modifier;
	}


	public void setModifier(int modifier) {
		this.modifier = modifier;
	}
	
	
}
