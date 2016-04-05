
package myGameEngine;

import a2.DungeonCrawler3DSoonTM;
import net.java.games.input.Event;
import sage.input.action.AbstractInputAction;

public class QuitGameAction extends AbstractInputAction 
{
	private DungeonCrawler3DSoonTM dg;
	
	public QuitGameAction(DungeonCrawler3DSoonTM dungeonCrawlerSoon3D)
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
