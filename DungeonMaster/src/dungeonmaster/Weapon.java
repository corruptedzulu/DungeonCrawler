package dungeonmaster;

public class Weapon 
{

	private String name;
	private String damageType;
	private String weaponType;
	private int distance;
	private int damageDiceNumber;
	private int numberOfDamageDice;
	private int damageModifier;
	
	private int attackModifier;
	
	private boolean criticalHit;
	

	public Weapon()
	{
		name = "";
		setDamageType("");
		weaponType = "";
		distance = 0;
		damageDiceNumber = 0;
		numberOfDamageDice = 0;
		damageModifier = 0;
		setAttackModifier(0);
		
		criticalHit = false;
	}
	
	public Weapon(String n, String dType, String wType, int dist, int dNumber, int diceNumber, int dmgMod, int attMod)
	{
		name = n;
		setDamageType(dType);
		weaponType = wType;
		distance = dist;
		damageDiceNumber = dNumber;
		numberOfDamageDice = diceNumber;
		damageModifier = dmgMod;
		setAttackModifier(attMod);
		
		criticalHit = false;
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
	
	public int getDistance()
	{
		return distance;
	}

	public String getDamageType()
	{
		return damageType;
	}

	public void setDamageType(String damageType)
	{
		this.damageType = damageType;
	}

	public int getAttackModifier()
	{
		return attackModifier;
	}

	public void setAttackModifier(int attackModifier)
	{
		this.attackModifier = attackModifier;
	}

	public int getDamageDiceNumber()
	{
		return damageDiceNumber;
	}

	public void setDamageDiceNumber(int damageDiceNumber)
	{
		this.damageDiceNumber = damageDiceNumber;
	}

	public int getNumberOfDamageDice()
	{
		return numberOfDamageDice;
	}

	public void setNumberOfDamageDice(int numberOfDamageDice)
	{
		this.numberOfDamageDice = numberOfDamageDice;
	}

	public int getDamageModifier()
	{
		return damageModifier;
	}

	public void setDamageModifier(int damageModifier)
	{
		this.damageModifier = damageModifier;
	}

	public void setWeaponType(String weaponType)
	{
		this.weaponType = weaponType;
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}
	
}
