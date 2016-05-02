package dungeonmaster;
import World.*;

import java.util.ArrayList;

import Interfaces.*;
import Race.*;
import Networking.*;

public class DungeonMaster 
{

	private GameWorld gw;
	private ArrayList<PlayerCharacter> characters;
	
	private IDungeonMasterInput input;
	private IDungeonMasterOutput output;
	
	
	
	
	public DungeonMaster()
	{
		
		
	}
	
	public void initialize()
	{
		//create world (rooms, items, treasures, monsters)
		
	}
	
	public void start()
	{
		//wait for all four players to connect
		//transmit initial state to each one
		while(server.getClientsConnected() < 4)
		{
			server.listen();
		}
		
		
		
		//establish initiative order
		
	}
	
	private void gameLoop()
	{
		
		//check if the players can see enemies or vice versa
		//if yes, bring the enemies into the initiative
		
		//go through the list of players
		//offer each player their turn
		//receive their choice and update
		//move to next player
		
		
	}
	
}
