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
		
			//loadScripts();
			//check if they are character and/or world scripts
			//if(characterScripts)
			//{
			//	parseCharacterScripts();
			//}
			
			//if(worldScripts)
			//{
			//	parseWorldScripts();
			//}
			
			//gw = new GameWorld("scripted");
			
		}
		else
		{
			
			gw = new GameWorld();
			gw.setDM(this);
			
			//gw = new GameWorld("simple");
			
			
			
			Cleric cleric = new Cleric();
			Fighter figher = new Fighter();
			Rogue rogue = new Rogue();
			Wizard wizard = new Wizard();
			
			
			characters.add(cleric);
			characters.add(figher);
			characters.add(rogue);
			characters.add(wizard);
			
			gw.setPCArrayList(characters);
			
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
			Socket socket = new Socket();
			try 
			{
				socket = serverSocket.accept();
			} 
			catch (IOException e2) 
			{
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
					
						
			
			Player p = new Player();
			p.setMySocket(socket);
			players.add(p);
			
			
			//TODO: sendInitialGameState
			sendInitialGameState(socket);
			sendAvailableCharacters(socket);
			
			
			//TODO: listen for which character they want and their name
			String playerName = "";
			p.getOut().print("What is your name? ");
			try 
			{
				playerName = p.getIn().readLine();
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			p.getOut().print("Which character would you like? Cleric, Rogue, Wizard, or Fighter: ");
			
			
			String playerCharacterChoice = "";
			try 
			{
				playerCharacterChoice = p.getIn().readLine();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for(PlayerCharacter pc : characters)
			{
				if(pc.getPlayerClass().getThisInstanceClass() == playerCharacterChoice)
				{
					pc.setPlayerName(playerName);
					p.setMyCharacter(pc);
				}
			}
			
			
			
			
			//if this is a single player game, do the loop once, then break
			if(singlePlayerGame == true)
			{
				//go ahead and remove all of the characters that are not the single player
				for(PlayerCharacter pc : characters)
				{
					if(pc.getPlayerName() == "")
					{
						characters.remove(pc);
					}
				}
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
				boolean hasMoved = false;
				boolean hasActioned = false;
				boolean hasMinorActioned = false;
				
				String move = "Move";
				String action = "Standard Action";
				String minor = "Minor Action";
				
				
				if(e instanceof PlayerCharacter)
				{
					
					Player p = null;
					
					//figure out which player this playercharacter corresponds to.
					for(Player player : players)
					{
						if(player.getMyCharacter() == e)
						{
							p = player;
						}	
					}
					
					//while the player still has turn options
					while( hasMoved == false || hasActioned == false || hasMinorActioned == false)
					{
						//tell the player that it is their turn
						//ask the player what they would like to do
						p.getOut().print("It is your move. What would you like to do (or end)?");
						
						
						//these are the things the player can do, dependent of what they've already done
						if(hasMoved == false)
						{
							p.getOut().print(move);
						}
						if(hasActioned == false)
						{
							p.getOut().print(action);
						}
						if(hasMinorActioned == false)
						{
							p.getOut().print(minor);
						}
						
											
						
						String playerMove = null;
						//get the player's next move
						try 
						{
							playerMove = p.getIn().readLine();
						} 
						catch (IOException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
						
						
						if(playerMove == "move")
						{
							String moveInDirection = null;
							
							//get the direction the player would like to move in (N, NE, E, SE, S, SW, W, NW);
							try 
							{
								moveInDirection = p.getIn().readLine();
							} 
							catch (IOException e1) 
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							
							//check if the direction the player wants to move is valid
							if( gw.entityCanMoveInDirection(e, moveInDirection) )
							{
								//if it is a valid location, tell the player to make the move
								((PlayerCharacter) e).move();
							
								//decrement the player's remaining movement
								((PlayerCharacter) e).moveOneSquare();
								
								if(((PlayerCharacter) e).getMovementRemaining() == 0)
								{
									hasMoved = true;
								}
								
							}
							
							
							//send the new X and Y coordinates (in squares coordinate space)
							p.getOut().print(e.xCoor + ", " + e.yCoor);
							
							
						}
						
						
						
						if(playerMove == "standard")
						{
							
							
							
						}
						
						if(playerMove == "minor")
						{
							
						}
						
						
						
						
						
					}
					
					
					
					
					
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
