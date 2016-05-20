
package myGameEngine;

import dcfinal.DungeonCrawler3D;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class QuitGameAction extends AbstractInputAction 
{
	private DungeonCrawler3D dg;
	
	public QuitGameAction(DungeonCrawler3D dungeonCrawlerSoon3D)
	{
		super();
		dg = dungeonCrawlerSoon3D;
	}

	@Override
	public void performAction(float time, Event event) 
	{
		dg.setGameOver(true);
	}

}
