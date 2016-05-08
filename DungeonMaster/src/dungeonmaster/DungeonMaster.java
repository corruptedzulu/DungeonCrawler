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
	
	private ArrayList<PlayerCharacter> availableCharacters;
	
	
	private ArrayList<WorldEntity> initiative;
	//private Room currentRoom;
	
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
		characters = new ArrayList<PlayerCharacter>();
		players = new ArrayList<Player>();
		
		initiative = new ArrayList<WorldEntity>();
		
		availableCharacters = new ArrayList<PlayerCharacter>();
		
	}
	
	public void initialize(String[] args)
	{
		if(args != null)
		{
			if(args[0] == "singleplayer")
			{
				singlePlayerGame = true;
			}
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
			sendInitialGameState(p);
			sendAvailableCharacters(p);
			
			
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
			
			
			
			//TODO tell any other connected players about this new player.
			
		}
		
		
		
		
		for(WorldEntity e : characters)
		{
			
		}
		
		
		
		
		//establish initiative order
		
		
		gameLoop();
	}
	
	private void sendInitialGameState(Player p) 
	{
		// TODO Auto-generated method stub
		
		p.getOut().println(gw.toString());
		
	}

	private void sendAvailableCharacters(Player p) 
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
				String ender = "End turn";
				
				
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
						
						p.getOut().print(ender);
						
											
						
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
							p.getOut().print(e.getMyWorldEntityID() + ":" + e.xCoor + ", " + e.yCoor);
							
							for(Player player : players)
							{
								//if this player we're checking at is actually the one who is taking the turn
								//skip them
								//they're already getting the gameplay updates directly
								if(player == p)
								{
									
								}
								else
								{
									player.getOut().println(e.xCoor + ", " + e.yCoor);
								}
							}
							
						}
						
						
						
						if(playerMove == "standard")
						{
							
							//check with the world to see what actions are available
							//see what is adjacent to the character.
							//ask each of those 
							//	objects: what their interactions are
							//	entites: if they are enemy, ally, or PCs
							
							
							
							
							String standardActionType = null;
							
							//get the action type that the player would like to make
							try 
							{
								standardActionType = p.getIn().readLine();
							} 
							catch (IOException e1) 
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							
							
							
							if(standardActionType == "attack")
							{
								
								//ask if melee or ranged
								
								String attackType = null;
								
								//get the attack type that the player would like to make
								try 
								{
									attackType = p.getIn().readLine();
								} 
								catch (IOException e1) 
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								
								
								
								
								if(attackType == "melee")
								{
									
									//ask the gameworld which enemies are adjacent
									
									ArrayList<WorldEntity> enemiesInRoom = e.getContainingRoom().getEntities();
									
									ArrayList<Enemy> enemiesAttackable = new ArrayList<Enemy>();
									
									
									for(WorldEntity s : enemiesInRoom)
									{
										//check if this one is an enemy. if it is not, skip to the next one
										if( !(s instanceof Enemy) )
										{
											continue;
										}
										
										
										
										int squareX = s.getxCoor();
										int squareY = s.getyCoor();
										int myX = e.getxCoor();
										int myY = e.getyCoor();
										
										
										
										
										//if it is in x-alignment with me or one square either direction
										if(squareX == myX || squareX == myX - 1 || squareX == myX + 1)
										{
											
											//then check if it is on either side of me on the y axis or on my y axis
											if(squareY == myY + 1 || squareY == myY - 1 || squareY == myY)
											{
												
												
												//the enemy in question is adjacent to me
												
												enemiesAttackable.add((Enemy) s);
												
											}
											
											
										}
									}
									
									
									
									//now i have an arraylist of adjacent enemies
									
									
									String enemiesList = "";
									
									for(Enemy en : enemiesAttackable)
									{
										enemiesList += en.toString() + "$$";
										
									}
									
									
									//send the list of enemies to the client
									p.getOut().println(enemiesList);
									
									
									
									//ask which enemy the player would like to attack
									String attackEnemyID = null;
									
									//get the attack type that the player would like to make
									try 
									{
										attackEnemyID = p.getIn().readLine();
									} 
									catch (IOException e1) 
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									
									Enemy attackedEnemy = null;
									
									for(Enemy en : enemiesAttackable)
									{
										if(en.getMyWorldEntityID() ==  Integer.parseInt(attackEnemyID))
										{
											attackedEnemy = en;
										}
									}
									
									
									
									//make the attack roll
									int attackRoll = ((PlayerCharacter) e).makeAttackRoll("melee");
									
									
									//if it beats the enemy's AC
									if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									{
										//make the damage roll
										int damage = ((PlayerCharacter) e).makeDamageRoll("melee");
										
										
										//tell the enemy to take the damage
										attackedEnemy.takeDamage(damage);
										
										
										//if that kills the enemy
										if(attackedEnemy.getShouldRemoveSelfFromGame())
										{
											//then tell the Room to delete the enemy
											e.getContainingRoom().removeEntity(attackedEnemy);
											
											
											//and tell the player clients to remove it as well
											p.getOut().println(attackedEnemy.toString());
											
											for(Player player : players)
											{
												//if this player we're checking at is actually the one who is taking the turn
												//skip them
												//they're already getting the gameplay updates directly
												if(player == p)
												{
													
												}
												else
												{
													player.getOut().println(attackedEnemy.toString());
												}
											}
											
										}
										
									}
									
									
									
									
									
								}
								
								
								if(attackType == "ranged")
								{
									
																	
									//ask the gameworld which enemies are within range of ranged weapon
									
									ArrayList<WorldEntity> enemiesInRoom = e.getContainingRoom().getEntities();
									
									ArrayList<Enemy> enemiesAttackable = new ArrayList<Enemy>();
									
									
									for(WorldEntity s : enemiesInRoom)
									{
										//check if this one is an enemy. if it is not, skip to the next one
										if( !(s instanceof Enemy) )
										{
											continue;
										}
										
										
										
										int squareX = s.getxCoor();
										int squareY = s.getyCoor();
										int myX = e.getxCoor();
										int myY = e.getyCoor();
										
										int maxDistance = ((PlayerCharacter) e).getRangedAttackDistance();
										
										
										//compute range to each enemy and add it if it is in range
										//recall that DnD distances on diagonals do not follow the Pythagorean theorem
										//up two, over two, but on the diagonal is counted as two
										//thus, the distance in a large square from the center to each side is the same as to the diagonals
										//thus, we only need to check the sides in the following block										
										
										//if it is in between the x distances that my weapon could go
										if(squareX <= myX + maxDistance && squareX >= myX - maxDistance)
										{
											if(squareY <= myY + maxDistance && squareY >= myY - maxDistance)
											{
												enemiesAttackable.add((Enemy) s);
											}
										}
				
										
									}
									
									
									
									//now i have an arraylist of adjacent enemies
									
									
									String enemiesList = "";
									
									for(Enemy en : enemiesAttackable)
									{
										enemiesList += en.toString() + "$$";
										
									}
									
									
									//send the list of enemies to the client
									p.getOut().println(enemiesList);
									
									
									
									//ask which enemy the player would like to attack
									String attackEnemyID = null;
									
									//get the attack type that the player would like to make
									try 
									{
										attackEnemyID = p.getIn().readLine();
									} 
									catch (IOException e1) 
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									
									
									Enemy attackedEnemy = null;
									
									for(Enemy en : enemiesAttackable)
									{
										if(en.getMyWorldEntityID() ==  Integer.parseInt(attackEnemyID))
										{
											attackedEnemy = en;
										}
									}
									
									
									
									//make the attack roll
									int attackRoll = ((PlayerCharacter) e).makeAttackRoll("melee");
									
									
									//if it beats the enemy's AC
									if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									{
										//make the damage roll
										int damage = ((PlayerCharacter) e).makeDamageRoll("melee");
										
										
										//tell the enemy to take the damage
										attackedEnemy.takeDamage(damage);
										
										
										//if that kills the enemy
										if(attackedEnemy.getShouldRemoveSelfFromGame())
										{
											//then tell the Room to delete the enemy
											e.getContainingRoom().removeEntity(attackedEnemy);
											
											
											//and tell the player clients to remove it as well
											p.getOut().println(attackedEnemy.toString());
											
											for(Player player : players)
											{
												//if this player we're checking at is actually the one who is taking the turn
												//skip them
												//they're already getting the gameplay updates directly
												if(player == p)
												{
													
												}
												else
												{
													player.getOut().println(attackedEnemy.toString());
												}
											}
											
										}
										
									}
									
									
									
									
								}
								
								
								
								
								
								
								
								
							}
							
							if(standardActionType == "interactWithObject")
							{
								
								
								//get all of the abilities that can be used
								//so... check what objects in the world we're near
								
								//this m
								
								
								String abilityType = null;
								
								//get the attack type that the player would like to make
								try 
								{
									abilityType = p.getIn().readLine();
								} 
								catch (IOException e1) 
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								
								
								
								
								
								
								
								
								
								
								
								
								
								
							}
							
							
							
							
							
							
							hasActioned = true;
							
						}
						
						if(playerMove == "minor")
						{
						
							
							
							
							
							
							String minorActionType = null;
							
							//get the action type that the player would like to make
							try 
							{
								minorActionType = p.getIn().readLine();
							} 
							catch (IOException e1) 
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
							
							
							hasMinorActioned = true;
							
						}
						
						
						
						//player is done with turn, either by declaration or by exhaustion of options
						if(playerMove == "end" || (hasActioned == true && hasMinorActioned == true && hasMoved == true))
						{
							
							
							
							break;
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
