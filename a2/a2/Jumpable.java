package a2;

public abstract class Jumpable 
{

	private double verticalJumpVelocity;
	private double currentVerticalVelocity;
	private double startAltitude;
	private double currentAltitude;
	
	private double gravitationalAcceleration;
	
	
	public Jumpable()
	{
		
	}
	
	public Jumpable(double vel, double alt)
	{
		this();
		verticalJumpVelocity = vel;
		currentAltitude = alt;
	}
	
	public void update()
	{
		
	}
	
}
