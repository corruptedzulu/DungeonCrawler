package dungeonmaster;
import World.*;
import World.Room.Room;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.Invocable;

import Enemies.Enemy;
import Interfaces.*;
import Race.*;
import Networking.*;
import PlayerCharacters.*;

public class DungeonMaster 
{

	private ScriptEngine engine;
	private String scriptName = "script.js";
	private File scriptFile;
	
	
	private GameWorld gw;
	private ArrayList<PlayerCharacter> characters;
	private ArrayList<Player> players; 
	
	private ArrayList<PlayerCharacter> availableCharacters;
	
	
	
	private List<WorldEntity> allEntities;
	
	private ArrayList<WorldEntity> initiative;
	//private Room currentRoom;
	
	private IDungeonMasterInput input;
	private IDungeonMasterOutput output;
	
	private boolean gameInProgress;
	private boolean singlePlayerGame;
	
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
		
		allEntities = new ArrayList<WorldEntity>();
		
		clientsConnected = 0;
		
		gameInProgress = true;
		singlePlayerGame = false;
		
	}
	
	public void initialize(String[] args)
	{
		if(args != null)
		{
			if(args[0].equals("singleplayer"))
			{
				singlePlayerGame = true;
			}
		}
		
		
		ScriptEngineManager factory = new ScriptEngineManager();
		List<ScriptEngineFactory> list = factory.getEngineFactories();
		engine = factory.getEngineByName("js");
		scriptFile = new File(scriptName);
		
		boolean scriptsAvailable = false;
		
		
		if(scriptFile.exists())
		{
			scriptsAvailable = true;
		}
		
		
		
		//check if any script files are being provided to build the world with
		
		//create world (rooms, items, treasures, monsters)
		//create characters
		
		
		if(scriptsAvailable)
		{
			 try
			 { 
				 FileReader fileReader = new FileReader(scriptFile);
				 engine.eval(fileReader);
				 fileReader.close();
			 }
			 catch (FileNotFoundException e1)
			 { 
				 System.out.println(scriptFile + " not found " + e1);
			 }
			 catch (IOException e2)
			 { 
				 System.out.println("IO problem with " + scriptFile + e2);
			 }
			 catch (ScriptException e3) 
			 { 
				 System.out.println("ScriptException in " + scriptFile + e3);
			 }
			 catch (NullPointerException e4)
			 { 
				 System.out.println ("Null ptr exception reading " + scriptFile + e4); 
			 }
			 
			 
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
			
			//gw = new GameWorld();
			gw = new GameWorld("simple");
			gw.setDM(this);
			allEntities = gw.getAllEnemies();
			gw.setPCArrayList(characters);
			
			//TODO: uncomment
			
			
			Cleric cleric = new Cleric();
			Fighter fighter = new Fighter();
			Rogue rogue = new Rogue();
			Wizard wizard = new Wizard();
			
			
			characters.add(cleric);
			characters.add(fighter);
			characters.add(rogue);
			characters.add(wizard);
		
			
			cleric.setxCoor(0);
			cleric.setyCoor(0);
			fighter.setxCoor(0);
			fighter.setyCoor(1);
			rogue.setxCoor(0);
			rogue.setyCoor(2);
			wizard.setxCoor(0);
			wizard.setyCoor(3);
			
		}
		
		

		
		//start networking
		
		try
		{
			serverSocket = new ServerSocket(7856);

		}
		catch (IOException e)
		{
			
		}
		
/*		
		String s = "GW:WorldSize 1000,1000;Room0:StandardRoom;WidthLength 8,12;WorldXYofSWCorner 0,0;WorldEntity0:XY 0,5;$$WorldEntity1:XY 2,5;$$WorldEntity2:XY 4,5;$$PlayerCharacter12:XY 0,0;HPMaxHP 44,44;$$;Door0:ThisRoomXY 5,11;OtherRoomXY 5,0;$$;$$Room1:StandardRoom;WidthLength 15,20;WorldXYofSWCorner 0,12;WorldEntity3:XY 4,10;$$WorldEntity4:XY 6,10;$$WorldEntity5:XY 8,10;$$WorldEntity6:XY 10,10;$$WorldEntity7:XY 12,10;$$WorldEntity8:XY 14,10;$$;Door1:ThisRoomXY 5,0;OtherRoomXY 5,11;$$Door2:ThisRoomXY 5,19;OtherRoomXY 5,0;$$;$$Room2:StandardRoom;WidthLength 10,30;WorldXYofSWCorner 0,33;WorldEntity9:XY 0,15;$$WorldEntity10:XY 2,15;$$WorldEntity11:XY 5,20;$$;WorldObject4:5,25;$$;Door3:ThisRoomXY 5,0;OtherRoomXY 5,19;$$;$$$$";
		Parser p = new Parser();
		
		p.parseString(s);*/
		
	}
	
	public void start()
	{
		//wait for all four players to connect
		//transmit initial state to each one
		while(clientsConnected < 4)
		{
			
			//create a new serversocket and listen for a client
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
					
						
			//make a new player to represent that socket
			//store the socket there and add the player to the list
			Player p = new Player();
			p.setMySocket(socket);
			players.add(p);
			
			
			
			
			
			
			//listen for their name
			/*p.getOut().println("requestName");
			String playerName = "";
			try 
			{
				playerName = p.getIn().readLine();
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
			
			
			//tell them the classes that are available			
			String sending = "charactersAvailable:";		
			for(PlayerCharacter pc : characters)
			{
				sending += pc.getPlayerClass().getThisInstanceClass() + ";";
			}
			sending += "$$";
			p.getOut().println(sending);
			
			
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//ask them to pick which class they want
			p.getOut().println("pickCharacter");
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
			
			
			//match that string with the PC
			for(PlayerCharacter pc : characters)
			{
				if(pc.getPlayerClass().getThisInstanceClass().equals(playerCharacterChoice))
				{
					//pc.setPlayerName(playerName);
					p.setMyCharacter(pc);
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					p.getOut().println(p.getMyCharacter().toString());
				}
			}
			
			
			
			int index = 0;
			allEntities.add(p.getMyCharacter());
			
			//if this is a single player game, do the loop once, then break
			if(singlePlayerGame == true)
			{
				characters.clear();
				characters.add(p.getMyCharacter());

				break;
			}
			
			
			
			clientsConnected++;
			
			
		}
		
		
		//tell all connected players about each of the other players.
		for(Player p : players)//for each player
		{
			for(Player player : players)//loop through the players
			{
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//report to each player what all of the players currently are
				p.getOut().println(player.getMyCharacter().toString());
			}
			
			//p.getOut().println("nextReportWhenReadyToProceed");//tell the client that the next communcation from the server will be asking them to report they're ready
			
		}
		
		
		
		//wait for all players (in succession) to send the ready signal (ie, they stepped on the teleporter)
		int clientsHaveReportedReady = 0;
		
		for(Player p : players)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			p.getOut().println("reportReadyToProceed");
			String readyToProceed = "";
			while( !readyToProceed.equals("ready"))
			{
				try
				{
					readyToProceed = p.getIn().readLine();
				}
				catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			clientsHaveReportedReady += 1;
			
			gw.getRoomOne().addEntity(p.getMyCharacter());
			p.getMyCharacter().setContainingRoom(gw.getRoomOne());
			
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//tell the player about the whole game world
			p.getOut().println(gw.toString());
			
			
			
		}
		
		
		

		
		
		//establish initiative order
		for(WorldEntity e : allEntities)
		{
			if(e instanceof PlayerCharacter)
			{
				((PlayerCharacter) e).rollInitiative();
			}
			
			if(e instanceof NonPlayerCharacter)
			{
				((NonPlayerCharacter) e).rollInitiative();
			}
		}
		
		//sort them in ascending order
		Collections.sort(allEntities);
		
		//now, we want them in descending order for the initiative list
		//so just keep inserting the next one at the beginning
		for(WorldEntity e : allEntities)
		{
			initiative.add(0, e);
		}
		

		
		
		
		for(Player p : players)
		{
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			p.getOut().println("readyToProceed");//the game will begin, change display to the internal map, away from the outdoors area
		}
		
		
		
		gameLoop();
	}


	private void gameLoop()
	{
		
		
		//check if the players can see enemies or vice versa
		//if yes, bring the enemies into the initiative
		
		//go through the list of players
		//offer each player their turn
		//receive their choice and update
		//move to next player
	
		//if we come to an enemy or npc
		//call takeTurn()
		
		while(gameInProgress == true)
		{
			
			for(WorldEntity e : initiative)
			{
				boolean hasMoved = false;
				boolean hasActioned = false;
				//boolean hasMinorActioned = false;
				
				String move = "Move";
				String action = "StandardAction";
				//String minor = "MinorAction";
				String ender = "End turn";
				
				e.resetTurnSpecificValues();
				
				
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
			
					
					if( ((PlayerCharacter) e).isDying() )
					{
						((PlayerCharacter) e).saveVsDeath();
						//TODO inform player of success or failure
						
						
						if( ((PlayerCharacter)e).isDead() )
						{
							
							for(Player player : players)
							{
								if(player != p)
								{
									try
									{
										Thread.sleep(100);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									player.getOut().println(p.getMyCharacter().toString());
								}
							}
								
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							p.getOut().println("dead");
			
						}
						
						if( ((PlayerCharacter)e).isDying() )
						{
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							p.getOut().println("dying");
							
							
							
						}
						
						
							
						//TODO player is dead
						//inform all players that the player died	
						continue; //skip to the next WorldEntity
					}
					
					
					if( ((PlayerCharacter) e).isDead())
					{					
						continue;//skip to the next player, but keep us in the initiative order
					}
					
					
					//while the player still has turn options
					while( hasMoved == false || hasActioned == false)// || hasMinorActioned == false)
					{
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//tell the player that it is their turn
						//ask the player what they would like to do
						p.getOut().println("yourMove");
						
						
						//these are the things the player can do, dependent of what they've already done
						if(hasMoved == false)
						{
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							p.getOut().println(move);
						}
						if(hasActioned == false)
						{
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							p.getOut().println(action);
						}
						/*if(hasMinorActioned == false)
						{
							//p.getOut().println(minor);
						}*/
						
						try
						{
							Thread.sleep(100);
						}
						catch (InterruptedException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						p.getOut().println(ender);
						
						/*p.getMyCharacter().setxCoor(5);
						p.getMyCharacter().setyCoor(10);*/
						
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
						
						
						
						
						if(playerMove.equals("move") && hasMoved == false)
						{
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							p.getOut().println("moveDirection");
							
							
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
								((PlayerCharacter) e).move(moveInDirection);
							
								//decrement the player's remaining movement
								((PlayerCharacter) e).moveOneSquare();
								
								if(((PlayerCharacter) e).getMovementRemaining() == 0)
								{
									hasMoved = true;
								}
								
							}
							
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//send the new X and Y coordinates (in squares coordinate space)
							p.getOut().println( ((PlayerCharacter) e).toString() );
							
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
									try
									{
										Thread.sleep(100);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									player.getOut().println(((PlayerCharacter) e).toString());
								}
							}
							
						}
						
						
						
						if(playerMove.equals("standard") && hasActioned == false)
						{
							
							//check with the world to see what actions are available
							//see what is adjacent to the character.
							//ask each of those 
							//	objects: what their interactions are
							//	entites: if they are enemy, ally, or PCs
							
							
							
							//get a list of all of the things that the player could do from this square
							//attack and interact are the two over arching things
							
							try
							{
								Thread.sleep(100);
							}
							catch (InterruptedException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							p.getOut().println("standardActionType");
							
							
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
							
							
							
							
							
							if(standardActionType.equals("attack"))
							{
								try
								{
									Thread.sleep(100);
								}
								catch (InterruptedException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								p.getOut().println("attackType");
								
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
								
								
								
								
								
								if(attackType.equals("melee"))
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
										
										
										
										//NOTE: this if() if() could be replaced with a call to e.isAdjacentTo(s);
										
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
									
									
									String enemiesList = "EnemyList:";
									for(Enemy en : enemiesAttackable)
									{
										enemiesList += en.toString();
										
									}
									enemiesList += "$$";
									
									try
									{
										Thread.sleep(100);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									//send the list of enemies to the client
									p.getOut().println(enemiesList);
									
									try
									{
										Thread.sleep(100);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									p.getOut().println("selectEnemyWorldEntityID");
									
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
											
											try
											{
												Thread.sleep(100);
											}
											catch (InterruptedException e1)
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
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
													try
													{
														Thread.sleep(100);
													}
													catch (InterruptedException e1)
													{
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
													player.getOut().println(attackedEnemy.toString());
												}
											}
											
										}
										
									}
									
									
									
									
									
								}
								
								
								if(attackType.equals("ranged"))
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
									
									
									
									//now i have an arraylist of within-range enemies
									
									
									String enemiesList = "EnemyList";
									
									for(Enemy en : enemiesAttackable)
									{
										enemiesList += en.toString();
										
									}
									
									enemiesList += "$$";
																	
									try
									{
										Thread.sleep(100);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									//send the list of enemies to the client
									p.getOut().println(enemiesList);
									
									try
									{
										Thread.sleep(100);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									p.getOut().println("selectEnemyWorldEntityID");
									
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
									int attackRoll = ((PlayerCharacter) e).makeAttackRoll("ranged");
									
									
									//if it beats the enemy's AC
									if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									{
										//make the damage roll
										int damage = ((PlayerCharacter) e).makeDamageRoll("ranged");
										
										
										//tell the enemy to take the damage
										attackedEnemy.takeDamage(damage);
										
										
										//if that kills the enemy
										if(attackedEnemy.getShouldRemoveSelfFromGame())
										{
											//then tell the Room to delete the enemy
											e.getContainingRoom().removeEntity(attackedEnemy);
											
											try
											{
												Thread.sleep(100);
											}
											catch (InterruptedException e1)
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
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
													try
													{
														Thread.sleep(100);
													}
													catch (InterruptedException e1)
													{
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
													player.getOut().println(attackedEnemy.toString());
												}
											}//for(Player player : players)
											
										}//if(attackedEnemy.getShouldRemoveSelfFromGame())
										
									}//if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									
								}//if(attackType == "ranged")					
								
							}//if(standardActionType == "attack")
							
							
							if(standardActionType.equals("interactWithObject"))
							{
								//get all of the adjacent objects
								ArrayList<WorldObject> objects = gw.getWorldObjectsAdjacentToMe(e);
								WorldObject interactingWith = null;
								boolean isInteracting = false;
								
								String interactionDirection = null;
								
								try
								{
									Thread.sleep(100);
								}
								catch (InterruptedException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								p.getOut().println("interactionDirection");
								
								
								//ask player for direction of object to interact with								
								//get the interaction direction that the player would like to make
								try 
								{
									interactionDirection = p.getIn().readLine();
								} 
								catch (IOException e1) 
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								
								for(WorldObject wo : objects)
								{
									//check direction relative to e
									//check if that direction matches the interaction direction
									//set the object as interactingWith
									//if nothing found, break
									
									
									String directionOfObject = this.determineDirectionTowardsObject(e, wo);
									
									
									if(directionOfObject.equals(interactionDirection))
									{
										interactingWith = wo;
										
										isInteracting = true;
									}
									
									
									
								}
								
								if(!isInteracting)
								{
									continue; //turns out we're not interacting with anything, so skip to the next go around of our turn-loop
								}
								
								
								//call object.getApplicableInteractionSkill()
								//call PlayerCharacter.rollSkill(skill);
								//call object.rollMeetsDC(pc's roll);
								//if yes, object.interact();
								
								Room movingFrom = e.getContainingRoom(); //save this in case we need to rereport it
								String skill = interactingWith.getApplicableInteractionSkill();
								int roll = ((PlayerCharacter) e).useSkill(skill);
								
								if(interactingWith.meetsInteractionDC(roll))
								{
									if(interactingWith instanceof Door)
									{
										//door-specific interactions code (move character to the next room)
										
										
										//get the room this door connects to
										Room roomMovingTo = ((Door) interactingWith).getRoomIConnectTo();
										
										//get all of the Doors in the new room
										ArrayList<Door> doorsInNewRoom = roomMovingTo.getDoors();
										
										
										//check each door to see what it connects with
										for(Door d : doorsInNewRoom)
										{
											//if the door connects to the room we're coming out of
											//then get it's location and put the playercharacter one sqaure in
											if(d.getRoomIConnectTo() == e.getContainingRoom())
											{
												//move the character to the new room
												e.setContainingRoom(roomMovingTo);
												movingFrom.getEntities().remove(e);
												roomMovingTo.getEntities().add(e);
												
												//set the coordinates in that new room
												
												int doorX = d.getxRoomOneCoor();
												int doorY = d.getyRoomOneCoor();
												
												if(doorX == 0)//SOUTH WALL
												{
													e.setxCoor(doorX + 1);
													e.setyCoor(doorY);
												}
												else if(doorX == d.getMyRoom().getLengthSquares())//NORTH WALL
												{
													e.setxCoor(doorX - 1);
													e.setyCoor(doorY);
												}
												else if(doorY == 0)//WEST WALL
												{
													e.setxCoor(doorX);
													e.setyCoor(doorY + 1);
												}
												else if(doorX == d.getMyRoom().getWidthSquares())//EAST WALL
												{
													e.setxCoor(doorX);
													e.setyCoor(doorY - 1);
												}
																						
												
												
											}
										}
										
										
										try
										{
											Thread.sleep(100);
										}
										catch (InterruptedException e1)
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										//tell the player about it's character (specifically that it moved)
										p.getOut().println( ((PlayerCharacter) e).toString() );
										//TODO tell the player about the new room it' in, but only if we're not saving the whole world map on the client-side on startup
										
										
										//now tell the other players about the change in rooms
										
										for(Player player : players)
										{
											if(p != player)
											{
												try
												{
													Thread.sleep(100);
												}
												catch (InterruptedException e1)
												{
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
												player.getOut().println( ((PlayerCharacter) e).toString() );
											}
										}				
										
										
									}
									else
									{
										interactingWith.interact(e);
										
										for(Player player : players)
										{
											try
											{
												Thread.sleep(100);
											}
											catch (InterruptedException e1)
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											player.getOut().println(interactingWith.toString());
										}
										//TODO some sort of way to report what happened with an interaction
									}
								
									
								}
				
							}
							
				
							hasActioned = true;
							
						}
						
						/*if(playerMove == "minor")
						{
						
		
							String minorActionType = null;
							
							//get the action type that the player would like to make
							try 
							{
								minorActionType = p.getIn().readLine();
							} 
							catch (IOException e1) 
							{
								e1.printStackTrace();
							}
							
							
							
							
							hasMinorActioned = true;
							
						}*/
						
						
						
						//player is done with turn, either by declaration or by exhaustion of options
						if(playerMove.equals("end") || (hasActioned == true && hasMoved == true)) //hasMinorActioned == true &&
						{
							
							
							
							break;
						}
						
						
						
					}
					
					
					
					
					
				}
				else if(e instanceof NonPlayerCharacter)
				{
					if(e instanceof Enemy)
					{
						//if the enemy is dead, just skip it
						if(e.shouldRemoveSelfFromGame)
						{
							continue;
						}
						
						
						
						
						//check if we've attacked
						//if no, check for melee attack
					
						ArrayList<PlayerCharacter> playerCharactersInRoom = new ArrayList<PlayerCharacter>();
						
						ArrayList<WorldEntity> myRoomEntities = e.getContainingRoom().getEntities();
						
						ArrayList<PlayerCharacter> playerCharactersAttackable = new ArrayList<PlayerCharacter>();
						
						
						//get the locations of all of the playercharacters in this room
						for(WorldEntity we : myRoomEntities)
						{
							if(we instanceof PlayerCharacter)
							{
								if(((PlayerCharacter) we).isDying())
								{
									//if the player is dying, then ignore them (no need to kill them if they're down)
								}
								else
								{
									playerCharactersInRoom.add((PlayerCharacter) we);
								}
							}
						}
						
						
						//find out which ones are within my immediate melee range
						for(PlayerCharacter s : playerCharactersInRoom)
						{			
							
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
									
									playerCharactersAttackable.add(s);
									
								}
								
								
							}
						}
					
					
						//if there are characters adjacent to me, attack one
						if(playerCharactersAttackable.size() != 0)
						{
						
							for(Player player : players)
							{
								//if this enemy is in the same room as any of the players, then sleep for a second in between taking actions
								if(e.getContainingRoom() == player.getMyCharacter().getContainingRoom())
								{
									try
									{
										Thread.sleep(1000);
									}
									catch (InterruptedException e1)
									{
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
							
							
							Random random = new Random();
							
							int attackIndex = random.nextInt(playerCharactersAttackable.size());
														
							
							PlayerCharacter attackedEnemy = playerCharactersAttackable.get(attackIndex);
							
							
							//make the attack roll
							int attackRoll = ((Enemy) e).makeAttackRoll("melee");
							
							
							//if it beats the enemy's AC
							if(attackRoll >= attackedEnemy.getArmorClass().getAC())
							{
								//make the damage roll
								int damage = ((Enemy) e).makeDamageRoll("melee");
								
								
								//tell the enemy to take the damage
								attackedEnemy.takeDamage(damage);
								
								
								//if that took the player to 0 or fewer HP, then on their turn they will start making death saving throws	
								
								for(Player p : players)
								{
									if(p.getMyCharacter() == attackedEnemy)
									{
										try
										{
											Thread.sleep(100);
										}
										catch (InterruptedException e1)
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										p.getOut().println(attackedEnemy.toString());
									}
								}
							}
							
							hasActioned = true;
							continue;
												
						}
						else//otherwise, move to the nearest enemy
						{
							
							
							//check if there is another enemy within move range
							//if yes, move to enemy and attack
							//if no, check if we've attacked
							//if no, check for ranged attack
							
							
					
							//find out which players are within movement range
							ArrayList<PlayerCharacter> reachableCharacters = new ArrayList<PlayerCharacter>();
							
							for(PlayerCharacter s : playerCharactersInRoom)
							{								
								
								int squareX = s.getxCoor();
								int squareY = s.getyCoor();
								int myX = e.getxCoor();
								int myY = e.getyCoor();
								
								int maxDistance = ((Enemy) e).getMovementInSquares();
								
								
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
										reachableCharacters.add(s);
									}
								}
		
								
							}
							
							
							//if i can move to another character, do so
							if(reachableCharacters.size() != 0)
							{
								Random random = new Random();
								
								int moveTowards = random.nextInt(reachableCharacters.size());
								
								//TODO make up to MOVEMENT number of moves towards the selected character
								//then, once we're adjacent, make a melee attack
								
								
								
								while(hasMoved == false)
								{
									
									for(Player player : players)
									{
										//if this enemy is in the same room as any of the players, then sleep for a second in between taking actions
										if(e.getContainingRoom() == player.getMyCharacter().getContainingRoom())
										{
											try
											{
												Thread.sleep(1000);
											}
											catch (InterruptedException e1)
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									}
									
									
									String moveInDirection = determineDirectionTowardsEntity(e, reachableCharacters.get(moveTowards));

									
									//check if the direction the enemy wants to move is valid
									if( gw.entityCanMoveInDirection(e, moveInDirection) )
									{
										//if it is a valid location, tell the player to make the move
										((Enemy) e).move(moveInDirection);
									
										//decrement the player's remaining movement
										((Enemy) e).moveOneSquare();
										
										if(((Enemy) e).getMovementRemaining() == 0)
										{
											hasMoved = true;
										}
										
									}
									else
									{
										switch (moveInDirection)
										{
										case "N":
											
											if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "S":
											
											if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "E":
											
											if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "W":
											
											if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "NW":
											
											if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "NE":
											
											if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "SW":
											
											if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else if(gw.entityCanMoveInDirection(e, "SE"))
											{
												moveInDirection = "SE";
											}
											else if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
											
										case "SE":
											
											if(gw.entityCanMoveInDirection(e, "E"))
											{
												moveInDirection = "E";
											}
											else if(gw.entityCanMoveInDirection(e, "S"))
											{
												moveInDirection = "S";
											}
											else if(gw.entityCanMoveInDirection(e, "NE"))
											{
												moveInDirection = "NE";
											}
											else if(gw.entityCanMoveInDirection(e, "SW"))
											{
												moveInDirection = "SW";
											}
											else if(gw.entityCanMoveInDirection(e, "N"))
											{
												moveInDirection = "N";
											}
											else if(gw.entityCanMoveInDirection(e, "W"))
											{
												moveInDirection = "W";
											}
											else if(gw.entityCanMoveInDirection(e, "NW"))
											{
												moveInDirection = "NW";
											}
											else
											{
												//if we can't make any of those moves, then we're stuck
												//so just cancel out of attempting to move
												//this is really only possible if we're surrounded
												//we wouldve attacked something first
												hasMoved = true;
												break;
											}
											
											
											
											//if it is a valid location, tell the player to make the move
											((Enemy) e).move(moveInDirection);
										
											//decrement the player's remaining movement
											((Enemy) e).moveOneSquare();
											
											if(((Enemy) e).getMovementRemaining() == 0)
											{
												hasMoved = true;
											}
											
											
											break;
										
										
										
										}
									}
																		
									for(Player player : players)
									{
										try
										{
											Thread.sleep(100);
										}
										catch (InterruptedException e1)
										{
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										player.getOut().println( ((Enemy)e).toString());
									}
								}
								
								
								//we've now exhausted our movement
								//check if we can attack
								//if we can check if we have anyone next to us
								

								//find out which ones are within my immediate melee range
								for(PlayerCharacter s : playerCharactersInRoom)
								{			
									
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
											playerCharactersAttackable.add(s);										
										}
										
										
									}
								}
							
							
								//if there are characters adjacent to me after i completed my movement, attack one
								if(playerCharactersAttackable.size() != 0 && hasActioned == false)
								{
									for(Player player : players)
									{
										//if this enemy is in the same room as any of the players, then sleep for a second in between taking actions
										if(e.getContainingRoom() == player.getMyCharacter().getContainingRoom())
										{
											try
											{
												Thread.sleep(1000);
											}
											catch (InterruptedException e1)
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									}
									
									int attackIndex = random.nextInt(playerCharactersAttackable.size());
									
									//TODO fill in the attack for adjacent enemy (picked from the list based on attackIndex)
									
									PlayerCharacter attackedEnemy = playerCharactersAttackable.get(attackIndex);
									
									//TODO MELEE attack
									
									//make the attack roll
									int attackRoll = ((Enemy) e).makeAttackRoll("melee");
									
									
									//if it beats the enemy's AC
									if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									{
										//make the damage roll
										int damage = ((Enemy) e).makeDamageRoll("melee");
										
										
										//tell the enemy to take the damage
										attackedEnemy.takeDamage(damage);
										
										for(Player p : players)
										{
											if(p.getMyCharacter() == attackedEnemy)
											{
												try
												{
													Thread.sleep(100);
												}
												catch (InterruptedException e1)
												{
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
												p.getOut().println(attackedEnemy.toString());
											}
										}
																				
									}
																		
									hasActioned = true;
									continue;
									
									
								}
								
								
								//TODO send notificaiton to the players
								
								
								hasMoved = true;
								
								continue;
							}
							else//ranged attack
							{
							
								
								//find out which players are within ranged weapon range
								for(PlayerCharacter s : playerCharactersInRoom)
								{								
									
									int squareX = s.getxCoor();
									int squareY = s.getyCoor();
									int myX = e.getxCoor();
									int myY = e.getyCoor();
									
									int maxDistance = ((Enemy) e).getRangedAttackDistance();
									
									
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
											playerCharactersAttackable.add(s);
										}
									}
				
								}
								
								
								if(playerCharactersAttackable.size() != 0)
								{
									
									for(Player player : players)
									{
										//if this enemy is in the same room as any of the players, then sleep for a second in between taking actions
										if(e.getContainingRoom() == player.getMyCharacter().getContainingRoom())
										{
											try
											{
												Thread.sleep(1000);
											}
											catch (InterruptedException e1)
											{
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
										}
									}
									
									Random random = new Random();
									
									int attackIndex = random.nextInt(playerCharactersAttackable.size());
									
									//TODO fill in the attack for ranged enemy (picked from the list based on attackIndex)
									
																		
									
									PlayerCharacter attackedEnemy = playerCharactersAttackable.get(attackIndex);
																		
									
									
									//make the attack roll
									int attackRoll = ((Enemy) e).makeAttackRoll("ranged");
									
									
									//if it beats the enemy's AC
									if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									{
										//make the damage roll
										int damage = ((Enemy) e).makeDamageRoll("ranged");
										
										
										//tell the enemy to take the damage
										attackedEnemy.takeDamage(damage);
										
										for(Player p : players)
										{
											if(p.getMyCharacter() == attackedEnemy)
											{
												try
												{
													Thread.sleep(100);
												}
												catch (InterruptedException e1)
												{
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
												p.getOut().println(attackedEnemy.toString());
											}
										}
										
										
									}
									
									
									//TODO send notification to the players
									
									
									hasActioned = true;
									continue;
									
									
								}
								else
								{
									//no enemies within ranged attack range, nor movement
									//so just quit our turn
									continue;
								}						
							}							
						}								
					}								
				}						
			}				
		}			
	}


	private String determineDirectionTowardsEntity(WorldEntity mover, WorldEntity moveTowards)
	{
		String direction = "";
		
		int moverX = mover.getxCoor();
		int moverY = mover.getyCoor();
		int moveToX = moveTowards.getxCoor();
		int moveToY = moveTowards.getyCoor();
		
		
		
		//compute angle from where i am to where i'm going and assign a direction (1/8th of a circle)
		//woohoo, trig
		int deltaX = moveToX - moverX;//adjacent
		int deltaY = moveToY - moverY;//opposite
		
		
		double angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		
		if(angle < 0)
		{
			angle += 360;
		}
		
		//0-359 degrees
		
		if(angle >= 22.5 && angle < 77.5)
		{
			direction = "NE";
		}
		else if(angle >= 77.5 && angle < 112.5)
		{
			direction = "N";
		}
		else if(angle >= 112.5 && angle < 157.5)
		{
			direction = "NW";
		}
		else if(angle >= 157.5 && angle < 202.5)
		{
			direction = "W";
		}
		else if(angle >= 202.5 && angle < 247.5)
		{
			direction = "SW";
		}
		else if(angle >= 247.5 && angle < 292.5)
		{
			direction = "S";
		}
		else if(angle >= 292.5 && angle < 337.5)
		{
			direction = "SE";
		}
		else if(angle >= 337.5 || angle < 22.5) //have to use the OR here, since it can be bigger han 337.5 or smaller than 22.5
		{
			direction = "E";
		}
		else
		{
			direction = "E";//use east as the fallback if nothing matches correctly
		}
		
		
		
		return direction;
	}
	
	private String determineDirectionTowardsObject(WorldEntity mover, WorldObject moveTowards)
	{
		String direction = "";
		
		int moverX = mover.getxCoor();
		int moverY = mover.getyCoor();
		int moveToX = moveTowards.getxCoor();
		int moveToY = moveTowards.getyCoor();
		
		
		
		//compute angle from where i am to where i'm going and assign a direction (1/8th of a circle)
		//woohoo, trig
		int deltaX = moveToX - moverX;//adjacent
		int deltaY = moveToY - moverY;//opposite
		
		
		double angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		
		if(angle < 0)
		{
			angle += 360;
		}
		
		//0-359 degrees
		
		if(angle >= 22.5 && angle < 77.5)
		{
			direction = "NE";
		}
		else if(angle >= 77.5 && angle < 112.5)
		{
			direction = "N";
		}
		else if(angle >= 112.5 && angle < 157.5)
		{
			direction = "NW";
		}
		else if(angle >= 157.5 && angle < 202.5)
		{
			direction = "W";
		}
		else if(angle >= 202.5 && angle < 247.5)
		{
			direction = "SW";
		}
		else if(angle >= 247.5 && angle < 292.5)
		{
			direction = "S";
		}
		else if(angle >= 292.5 && angle < 337.5)
		{
			direction = "SE";
		}
		else if(angle >= 337.5 || angle < 22.5) //have to use the OR here, since it can be bigger han 337.5 or smaller than 22.5
		{
			direction = "E";
		}
		else
		{
			direction = "E";//use east as the fallback if nothing matches correctly
		}
		
		
		
		return direction;
	}
	
}
