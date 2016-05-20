
package myGameEngine;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import net.java.games.input.Event;
import sage.camera.ICamera;
import sage.input.action.AbstractInputAction;
import sage.terrain.TerrainBlock;

public class MoveAction extends AbstractInputAction
{
	protected ICamera camera;
	protected float speed;

	protected float moveAmount = (float) 0.3;
	protected int timeSinceLastMoveMS;

	private TerrainBlock terrainBlock;
	private boolean isTerrainFollow;
	private boolean isEnabled;

	public MoveAction(ICamera c, float s)
	{
		camera = c;
		speed = s;
		timeSinceLastMoveMS = 0;
		isTerrainFollow = false;
		terrainBlock = null;
		isEnabled = true;
	}

	@Override
	public void performAction(float time, Event event)
	{
		//System.out.println(time);

		Vector3D newLocVector = new Vector3D();
		Vector3D viewDir = camera.getViewDirection().normalize();
		Vector3D curLocVector = new Vector3D(camera.getLocation());
		if (event.getValue() < -0.2)
		{
			newLocVector = curLocVector.add(viewDir.mult(speed * time));
		} else
		{
			if (event.getValue() > 0.2)
			{
				newLocVector = curLocVector.minus(viewDir.mult(speed * time));
			} else
			{
				newLocVector = curLocVector;
			}
		}
		//create a point for the new location
		double newX = newLocVector.getX();
		double newY = newLocVector.getY();
		double newZ = newLocVector.getZ();
		Point3D newLoc = new Point3D(newX, newY, newZ);
		camera.setLocation(newLoc);
	}


	public boolean isTerrainFollow()
	{
		return isTerrainFollow;
	}

	public void setTerrainFollow(boolean terrainFollow)
	{
		isTerrainFollow = terrainFollow;
	}

	public void setTerrain(TerrainBlock terrain)
	{
		terrainBlock = terrain;
	}

	public double getTerrainHeight(Vector3D position)
	{
		if (!isTerrainFollow || terrainBlock == null)
			return position.getY();

		float terHeight = terrainBlock.getHeight((float)position.getX(),(float)position.getZ());
		return terHeight + (float)terrainBlock.getOrigin().getY(); // plus height offset
	}

	public void setEnabled(boolean enabled)
	{
		isEnabled = enabled;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}
}
