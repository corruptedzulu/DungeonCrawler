package dungeonmaster;
import World.*;
import World.Room.Room;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

import Enemies.Enemy;
import Interfaces.*;
import Race.*;
import Networking.*;
import PlayerCharacters.*;

public class DungeonMaster 
{

	private GameWorld gw;
	private ArrayList<PlayerCharacter> characters;
	private ArrayList<Player> players; 
	
	private ArrayList<WorldEntity> initiative;
	private Room currentRoom;
	
	private IDungeonMasterInput input;
	private IDungeonMasterOutput output;
	
	private boolean gameInProgress = true;
	private boolean singlePlayerGame = false;
	
	private Socket socket;
	private ServerSocket serverSocket;
	private BufferedReader in;
	private BufferedReader stdIn;
	private PrintWriter out;
	
	private int clientsConnected;
	
	
	
	public DungeonMaster()
	{
		
		
	}
	
	public void initialize(String[] args)
	{
		if(args[0] == "singleplayer")
		{
			singlePlayerGame = true;
		}
		
		
		
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
		
		
		
		//start networking
		
		try
		{
			serverSocket = new ServerSocket(7856);

		}
		catch (IOException e)
		{
			
		}
		
		
	}
	
	public void start()
	{
		//wait for all four players to connect
		//transmit initial state to each one
		while(clientsConnected < 4)
		{
			//TODO: server.listen
			Socket socket = serverSocket.accept();
					
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			playerConnections.add(socket);
			
			
			//TODO: sendInitialGameState
			sendInitialGameState(socket);
			sendAvailableCharacters(socket);
			
			//TODO: listen for which character they want
			String playerCharacterChoice = socket.;
			
			for(PlayerCharacter pc : characters)
			{
				if(pc.getPlayerClass().getThisInstanceClass() == playerCharacterChoice)
				{
					pc.setPlayerName(playerName);
				}
			}
			
			players.add(client);
			
			
			//if this is a single player game, do the loop once, then break
			if(singlePlayerGame == true)
			{
				break;
			}
			
			clientsConnected++;
			
		}
		
		
		
		//establish initiative order
		
		
		gameLoop();
	}
	
	private void sendInitialGameState(Socket socket2) 
	{
		// TODO Auto-generated method stub
		
		
	}

	private void sendAvailableCharacters(Socket socket2) 
	{
		// TODO Auto-generated method stub
		
		
	}

	private void gameLoop()
	{
		while(gameInProgress == true)
		{
			
			for(WorldEntity e : initiative)
			{
				if(e instanceof PlayerCharacter)
				{
					
				}
				else if(e instanceof NonPlayerCharacter)
				{
					if(e instanceof Enemy)
					{
						
						
					}
					else
					{
						
					}
					
				}
				
				
				
				//check if the players can see enemies or vice versa
				//if yes, bring the enemies into the initiative
				
				//go through the list of players
				//offer each player their turn
				//receive their choice and update
				//move to next player
			
				//if we come to an enemy or npc
				//call takeTurn()
				
			}
			
			
		
		
		}
		
		
	}
	
}
