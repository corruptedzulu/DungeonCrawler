package dungeonmaster;

public class Weapon 
{

	private String name;
	private String damageType;
	private String weaponType;
	private int damageDiceNumber;
	private int numberOfDamageDice;
	private int damageModifier;
	
	private int attackModifier;
	
	private boolean criticalHit;
	

	public Weapon()
	{
		
	}
	
	public Weapon(String n, String dType, String wType, int dNumber, int diceNumber, int dmgMod, int attMod)
	{
		name = n;
		damageType = dType;
		setWeaponType(wType);
		damageDiceNumber = dNumber;
		numberOfDamageDice = diceNumber;
		damageModifier = dmgMod;
		attackModifier = attMod;
		
	}
	
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
	
	
	public int rollToAttack()
	{
		criticalHit = false;
		
		//1 d20 is the attack roll
		Dice d = new Dice(20);
		
		int result = d.roll(1);
		
		//if a crit, hold that
		if(result == 20)
		{
			criticalHit = true;
		}
		
		
		return result;
	}

	public int rollDamage()
	{
		int damage = 0;
		
		Dice d = new Dice(damageDiceNumber);
		
		damage += d.roll(numberOfDamageDice);
		
		if(criticalHit)
		{
			//critical hit, you roll the damage dice a second time
			damage += d.roll(numberOfDamageDice);
		}
		
		damage += damageModifier;
		
		return damage;
		
	}

	public String getWeaponType() {
		return weaponType;
	}
	
}
