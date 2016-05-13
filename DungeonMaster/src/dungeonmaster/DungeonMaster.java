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
		
		allEntities = new ArrayList<WorldEntity>();
		
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
			
			gw = new GameWorld();
			gw = new GameWorld("simple");
			gw.setDM(this);
			
			
			
			Cleric cleric = new Cleric();
			Fighter figher = new Fighter();
			Rogue rogue = new Rogue();
			Wizard wizard = new Wizard();
			
			
			characters.add(cleric);
			characters.add(figher);
			characters.add(rogue);
			characters.add(wizard);
			
		}
		
		
		gw.setPCArrayList(characters);
		
		allEntities = gw.getAllEnemies();
		
		
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
			//sendInitialGameState(p); //tells them about the world
			sendAvailableCharacters(p); //tells them who is available
			
			
			//TODO: listen for which character they want and their name
			String playerName = "";
			p.getOut().println("requestName");
			try 
			{
				playerName = p.getIn().readLine();
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
		
		//TODO tell all connected players about each of the other players.
		
		for(Player p : players)
		{
			for(Player player : players)
			{
				//report to each player what all of the players currently are
				if(player == p)
				{
					
				}
				else
				{
					p.getOut().println(player.getMyCharacter().toString());
				}

				
			}
		}
		
		
		
		//TODO wait for all players to send the ready signal (ie, they stepped on the teleporter)
		
		
		
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
		
		
		
		//TODO tell the players about the first room
		
		
		
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
		
		String sending = "";
		
		for(PlayerCharacter pc : characters)
		{
			sending += pc.getClass().getName() + ";";
		}
		
		sending += "$$";
		
		p.getOut().println(sending);
		
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
					
					
					if( ((PlayerCharacter) e).isDead())
					{
						continue; //this lets us keep the player in the initiative order, just we skip them
					}
					
					
					if( ((PlayerCharacter) e).isDying() )
					{
						((PlayerCharacter) e).saveVsDeath();
						//TODO inform player of success or failure
						continue; //skip to the next WorldEntity
					}
					
					
					if( ((PlayerCharacter) e).isDead())
					{
						//TODO player is dead
						//inform all players that the player died
						
						
						
						continue;//skip to the next player
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
						
						
						
						
						if(playerMove == "move" && hasMoved == false)
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
								((PlayerCharacter) e).move(moveInDirection);
							
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
						
						
						
						if(playerMove == "standard" && hasActioned == false)
						{
							
							//check with the world to see what actions are available
							//see what is adjacent to the character.
							//ask each of those 
							//	objects: what their interactions are
							//	entites: if they are enemy, ally, or PCs
							
							
							
							//TODO get a list of all of the things that the player could do from this square
							//attack and interact are the two over arching things
							
							
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
								//get all of the adjacent objects
								ArrayList<WorldObject> objects = gw.getWorldObjectsAdjacentToMe(e);
								WorldObject interactingWith = null;
								boolean isInteracting = false;
								
								String interactionDirection = null;
								
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
									
									
									if(directionOfObject == interactionDirection)
									{
										interactingWith = wo;
										
										isInteracting = true;
									}
									
									
									
								}
								
								if(!isInteracting)
								{
									break;
								}
								
								
								//call object.getApplicableInteractionSkill()
								//call PlayerCharacter.rollSkill(skill);
								//call object.rollMeetsDC(pc's roll);
								//if yes, object.interact();
								
								Skill skill = interactingWith.getApplicableInteractionSkill();
								int roll = ((PlayerCharacter) e).useSkill(skill);
								
								if(interactingWith.meetsInteractionDC(roll))
								{
									if(interactingWith instanceof Door)
									{
										//TODO door-specific interactions code (move character to the next room)
									}
									
									
									interactingWith.interact(e);
								}
								else
								{
									
								}
								
								
								
								//TODO some sort of way to report what happened
																			
								
								
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
						
							Random random = new Random();
							
							int attackIndex = random.nextInt(playerCharactersAttackable.size());
														
							
							PlayerCharacter attackedEnemy = playerCharactersAttackable.get(attackIndex);
							
							
							//make the attack roll
							int attackRoll = ((PlayerCharacter) e).makeAttackRoll("melee");
							
							
							//if it beats the enemy's AC
							if(attackRoll >= attackedEnemy.getArmorClass().getAC())
							{
								//make the damage roll
								int damage = ((PlayerCharacter) e).makeDamageRoll("melee");
								
								
								//tell the enemy to take the damage
								attackedEnemy.takeDamage(damage);
								
								
								//if that took the player to 0 or fewer HP, then on their turn they will start making death saving throws								
							}
							
							hasActioned = true;
							break;
												
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
										player.getOut().println(e.xCoor + ", " + e.yCoor);
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
									int attackIndex = random.nextInt(playerCharactersAttackable.size());
									
									//TODO fill in the attack for adjacent enemy (picked from the list based on attackIndex)
									
									PlayerCharacter attackedEnemy = playerCharactersAttackable.get(attackIndex);
									
									//TODO MELEE attack
									
									//make the attack roll
									int attackRoll = ((PlayerCharacter) e).makeAttackRoll("melee");
									
									
									//if it beats the enemy's AC
									if(attackRoll >= attackedEnemy.getArmorClass().getAC())
									{
										//make the damage roll
										int damage = ((PlayerCharacter) e).makeDamageRoll("melee");
										
										
										//tell the enemy to take the damage
										attackedEnemy.takeDamage(damage);
																				
									}
																		
									hasActioned = true;
									break;
									
									
								}
								
								
								//TODO send notificaiton to the players
								
								
								hasMoved = true;
								
								break;
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
										
									}
									
									
									//TODO send notification to the players
									
									
									hasActioned = true;
									break;
									
									
								}
								else
								{
									//no enemies within ranged attack range, nor movement
									//so just quit our turn
									break;
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
		int deltaX = moverX - moveToX;//adjacent
		int deltaY = moverY - moveToY;//opposite
		
		
		double angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		
		
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
		int deltaX = moverX - moveToX;//adjacent
		int deltaY = moverY - moveToY;//opposite
		
		
		double angle = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		
		
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
