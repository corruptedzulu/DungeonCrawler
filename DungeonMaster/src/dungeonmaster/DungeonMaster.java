package dungeonmaster;
import World.*;

import java.util.ArrayList;

import Interfaces.*;
import Race.*;
import Networking.*;
import PlayerCharacters.*;

public class DungeonMaster 
{

	private GameWorld gw;
	private ArrayList<PlayerCharacter> characters;
	private ArrayList<PlayerClient> players; 
	
	private IDungeonMasterInput input;
	private IDungeonMasterOutput output;
	
	private boolean gameInProgress = true;
	
	
	
	public DungeonMaster()
	{
		
		
	}
	
	public void initialize()
	{
		boolean scriptsAvailable = false;
		
		//check if any script files are being provided to build the world with
		
		//create world (rooms, items, treasures, monsters)
		//create characters
		
		
		if(scriptsAvailable)
		{
		
			loadScripts();
			//check if they are character and/or world scripts
			if(characterScripts)
			{
				parseCharacterScripts();
			}
			
			if(worldScripts)
			{
				parseWorldScripts();
			}
			
			
		}
		else
		{
			
			gw = new GameWorld();
			
			
			Cleric cleric = new Cleric();
			Fighter figher = new Fighter();
			Rogue rogue = new Rogue();
			Wizard wizard = new Wizard();
			
			
			characters.add(cleric);
			characters.add(figher);
			characters.add(rogue);
			characters.add(wizard);
			
		}
		
		
	}
	
	public void start()
	{
		//wait for all four players to connect
		//transmit initial state to each one
		while(server.getClientsConnected() < 4)
		{
			//TODO: server.listen
			PlayerClient client = server.listen();
			
			//TODO: sendInitialGameState
			sendInitialGameState(client);
			sendAvailableCharacters(client);
			
			//TODO: listen for which character they want
			String playerCharacterChoice = server.listen();
			
			for(PlayerCharacter pc : characters)
			{
				if(pc.getPlayerClass().getThisInstanceClass() == playerCharacterChoice)
				{
					pc.setPlayerName(playerName);
				}
			}
			
			players.add(client);
			
		}
		
		
		
		//establish initiative order
		
		
		gameLoop();
	}
	
	private void gameLoop()
	{
		while(gameInProgress == true)
		{
			//check if the players can see enemies or vice versa
			//if yes, bring the enemies into the initiative
			
			//go through the list of players
			//offer each player their turn
			//receive their choice and update
			//move to next player
		
		
		
		}
		
		
	}
	
}
