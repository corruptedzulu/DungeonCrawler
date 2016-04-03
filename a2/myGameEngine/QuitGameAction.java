
package myGameEngine;

import a2.DogCatcher3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class QuitGameAction extends AbstractInputAction 
{
	private DogCatcher3D dg;
	
	public QuitGameAction(DogCatcher3D dogCatcher3D)
	{
		super();
		dg = dogCatcher3D;
	}

	@Override
	public void performAction(float time, Event event) 
	{
		dg.setGameOver(true);
	}

}
