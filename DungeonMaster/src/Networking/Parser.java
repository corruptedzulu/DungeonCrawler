package Networking;

import java.util.ArrayList;

import World.Room.Room;
import World.WorldObjects.Door;
import World.WorldObjects.WorldObject;
import dungeonmaster.WorldEntity;

public class Parser
{

	
	
	
	
	
	public Parser()
	{
		
	}
	
	
	public void parseString(String s)
	{
		
		/*

		charactersAvailable:....
		pickCharacter (reply)
		reportReadyToProceed (reply)
		readyToProceed (no response needed. move to the actual gameworld)
		dead (no response needed)
		dying (no response needed)
		yourMove Move Standard Action End Turn (these are 4 separate lines. reply after END TURN)
		moveDirection (reply with N, S, E, W, SE, SW, NE, NW)
		standardActionType (reply with attack or interactWithObject)
		attackType (reply with melee or ranged)
		EnemyList:....
		selectEnemyWorldEntityID (reply with the ID of the enemy to attack)
		interactionDirection (reply with N, S, E, W, SE, SW, NE, NW)
 
		 */
		
		if(s.regionMatches(0, "charactersAvailable:", 0, 20))
		{
			//the next part of the string will include the characters available
			
			String[] characterNames = s.split(";");
			
			
			
			
			
		}
		else if(s.regionMatches(0, "pickCharacter", 0, 13))
		{
			//the client needs to reply with which character it wants
			
			
			//TODO ask the user which of the characters they want to use
			//reply to the server with that string
			
			
		}
		else if(s.regionMatches(0, "reportReadyToProceed", 0, 20))
		{
			//TODO the initialization is completed. the client needs to reply with "ready" once it has moved to the teleporter
			
			
			
		}
		else if(s.regionMatches(0, "readyToProceed", 0, 14))
		{
			//TODO move the player to the tile-world and start listening for updates
			//the game begins here
			
			
		}
		else if(s.regionMatches(0, "dead", 0, 4))
		{
			//TODO the player has died and will no longer be able to make movements. it will still get the world updates
			
			
		}		
		else if(s.regionMatches(0, "dying", 0, 5))
		{
			//TODO the player is dying and will not be able to make movements. it will still get the world updates
			
			
		}		
		else if(s.regionMatches(0, "yourMove", 0, 8))
		{
			//TODO it is now the client's turn. the next three will list the options the player has. not all will be sent
			
			
		}		
		else if(s.regionMatches(0, "Move", 0, 4))
		{
			
			
			
		}
		else if(s.regionMatches(0, "Standard Action", 0, 15))
		{
			
			
			
		}
		else if(s.regionMatches(0, "End Turn", 0, 8))
		{
			
			
			
		}
		
		else if(s.regionMatches(0, "moveDirection", 0, 13))
		{
			//TODO reply with the direction you want to move
			
			
		}		
		else if(s.regionMatches(0, "standardActionType", 0, 18))
		{
			//TODO reply with attack or interactWithObject
			
			
		}		
		else if(s.regionMatches(0, "attackType", 0, 11))
		{
			//TODO reply with melee or ranged
			
			
		}		
		else if(s.regionMatches(0, "EnemyList:", 0, 10))
		{
			//this is a player report
			
			
		}		
		else if(s.regionMatches(0, "selectEnemyWorldEntityID", 0, 24))
		{
			//TODO reply with the enemy ID
			
			
		}		
		else if(s.regionMatches(0, "interactionDirection", 0, 20))
		{
			//TODO reply with the direction to interact in
			
			
		}		
		else if(s.regionMatches(0, "PlayerCharacter", 0, 15))//PlayerCharacter
		{
			
			this.parsePlayerCharacter(s);
			
		}
		else if(s.regionMatches(0, "Door", 0, 4))//Door
		{
			this.parseWorldObject(s);
			
		}
		else if(s.regionMatches(0, "GW", 0, 2))//GameWorld
		{
			this.parseGameWorld(s);
			
		}
		else if(s.regionMatches(0, "WorldEntity", 0, 11))//world entity
		{
			this.parseWorldEntity(s);
			
		}
		else if(s.regionMatches(0, "WorldObject", 0, 11))//world object (chest)
		{
			this.parseWorldObject(s);
			
		}
		
		
	}
	
	
	
	public void parseRoom(String s)
	{
		//entities
		//PlayerCharacters
		//objects
		//doors
		
		/*
		 * Room0:StandardRoom;WidthLength 8,12;WorldXYofSWCorner 0,0;
		 * WorldEntity0:XY 0,5;$$
		 * WorldEntity1:XY 2,5;$$
		 * WorldEntity2:XY 4,5;$$
		 * PlayerCharacter12:XY 0,0;HPMaxHP 44,44;$$;
		 * Door0:ThisRoomXY 5,11;OtherRoomXY 5,0;$$;
		 * $$
		 * Room1:StandardRoom;WidthLength 15,20;WorldXYofSWCorner 0,12;
		 * WorldEntity3:XY 4,10;$$
		 * WorldEntity4:XY 6,10;$$
		 * WorldEntity5:XY 8,10;$$
		 * WorldEntity6:XY 10,10;$$
		 * WorldEntity7:XY 12,10;$$
		 * WorldEntity8:XY 14,10;$$;
		 * Door1:ThisRoomXY 5,0;OtherRoomXY 5,11;$$
		 * Door2:ThisRoomXY 5,19;OtherRoomXY 5,0;$$;
		 * $$
		 * Room2:StandardRoom;WidthLength 10,30;WorldXYofSWCorner 0,33;
		 * WorldEntity9:XY 0,15;$$
		 * WorldEntity10:XY 2,15;$$
		 * WorldEntity11:XY 5,20;$$;
		 * WorldObject4:5,25;$$;
		 * Door3:ThisRoomXY 5,0;OtherRoomXY 5,19;$$;
		 * $$
		 * 
		 */
		
		
		int roomID = 0;
		
		int roomLength = 0;
		int roomWidth = 0;
		int worldX = 0;
		int worldY = 0;
		ArrayList<String> worldEntityStrings = new ArrayList<String>();
		ArrayList<String> worldObjectStrings = new ArrayList<String>();
		ArrayList<String> playerStrings = new ArrayList<String>();
		ArrayList<String> doorStrings = new ArrayList<String>();
		
		
		for(int x = 0; x < s.length(); x++)
		{
			
			if(s.charAt(x) == 'W' && s.charAt(x + 10) == 'h')
			{
				if(s.charAt(x+13) == ',')
				{
					roomWidth = Integer.parseInt(s.substring(x+12, x+13));
					roomLength = Integer.parseInt(s.substring(x+14, x+16));
				}
				else
				{
					roomWidth = Integer.parseInt(s.substring(x+12, x+14));
					roomLength = Integer.parseInt(s.substring(x+15, x+17));
				}
			}
			
			if(s.charAt(x) == 'W' && s.charAt(x + 16) == 'r')
			{
				worldX = 0;
				if(s.charAt(x+21) == ';')
				{
					worldY = Integer.parseInt(s.substring(x+20, x+21));
				}
				else
				{
					worldY = Integer.parseInt(s.substring(x+20, x+22));
				}
			}
			
			
		}
		

		
		
		for(int x = 0; x < s.length(); x++)
		{
			
			if(s.charAt(x) == 'W' && s.charAt(x+10) == 'y')
			{
				String worldEntity = "";
				//world entity
				for(int y = x; y < s.length(); y++)
				{
					if(s.charAt(y) == '$')
					{
						worldEntity = s.substring(x, y + 2);
						worldEntityStrings.add(worldEntity);
						break;
					}
				}
			}
			
			
			if(s.charAt(x) == 'W' && s.charAt(x+10) == 't')
			{
				//world object
				String worldObject = "";
				//world entity
				for(int y = x; y < s.length(); y++)
				{
					if(s.charAt(y) == '$')
					{
						worldObject = s.substring(x, y + 2);
						worldObjectStrings.add(worldObject);
						break;
					}
				}
				
			}
			
			
			if(s.charAt(x) == 'P' && s.charAt(x+14) == 'r')
			{
				//player character
				
				String pc = "";
				//world entity
				for(int y = x; y < s.length(); y++)
				{
					if(s.charAt(y) == '$')
					{
						pc = s.substring(x, y + 3);
						playerStrings.add(pc);
						break;
					}
				}
				
			}
			
			
			if(s.charAt(x) == 'D' && s.charAt(x+4) == 'r')
			{
				//door
				String door = "";
				//world entity
				for(int y = x; y < s.length(); y++)
				{
					if(s.charAt(y) == '$')
					{
						door = s.substring(x, y + 2);
						doorStrings.add(door);
					}
				}
				
			}
			
			
			
			
		}
		
		
		
		
		for(String string : worldObjectStrings)
		{
			this.parseWorldObject(string);
		}
		for(String string : worldEntityStrings)
		{
			this.parseWorldEntity(string);
		}
		for(String string : doorStrings)
		{
			this.parseDoorObject(string);
		}
		for(String string : playerStrings)
		{
			this.parsePlayerCharacter(string);
		}
		
		
		
		
	}
	
	public void parseGameWorld(String s)
	{
		//rooms basically
		/*
		 * GW:WorldSize 1000,1000;
		 * Room0:StandardRoom;WidthLength 8,12;WorldXYofSWCorner 0,0;
		 * WorldEntity0:XY 0,5;$$
		 * WorldEntity1:XY 2,5;$$
		 * WorldEntity2:XY 4,5;$$
		 * PlayerCharacter12:XY 0,0;HPMaxHP 44,44;$$;
		 * Door0:ThisRoomXY 5,11;OtherRoomXY 5,0;$$;
		 * $$
		 * Room1:StandardRoom;WidthLength 15,20;WorldXYofSWCorner 0,12;
		 * WorldEntity3:XY 4,10;$$
		 * WorldEntity4:XY 6,10;$$
		 * WorldEntity5:XY 8,10;$$
		 * WorldEntity6:XY 10,10;$$
		 * WorldEntity7:XY 12,10;$$
		 * WorldEntity8:XY 14,10;$$;
		 * Door1:ThisRoomXY 5,0;OtherRoomXY 5,11;$$
		 * Door2:ThisRoomXY 5,19;OtherRoomXY 5,0;$$;
		 * $$
		 * Room2:StandardRoom;WidthLength 10,30;WorldXYofSWCorner 0,33;
		 * WorldEntity9:XY 0,15;$$
		 * WorldEntity10:XY 2,15;$$
		 * WorldEntity11:XY 5,20;$$;
		 * WorldObject4:5,25;$$;
		 * Door3:ThisRoomXY 5,0;OtherRoomXY 5,19;$$;
		 * $$
		 * $$
		 */
		
		
		String notGameWorld = s.substring(23, s.length() - 2);//remove the header and the final $$
		String room1string = "";
		String room2string = "";
		String room3string = "";
		
		
		for(int x = 0; x < s.length(); x++)
		{
			if(s.charAt(x) == 'R' && s.charAt(x+1) == 'o' && s.charAt(x+2) == 'o' && s.charAt(x+3) == 'm' && s.charAt(x+4) == '0')
			{
				int endIndex = 0;
				//this is the Room0 string
				//look for Room1 and go back to the $$ for the substring index
				
				for(int y = x; y < s.length(); y++)
				{
					if(s.charAt(y) == 'R' && s.charAt(y+1) == 'o' && s.charAt(y+2) == 'o' && s.charAt(y+3) == 'm' && s.charAt(y+4) == '1')
					{
						endIndex = y;//we can use this because substring's terminator is exclusive
						break;
					}
				}
				
				room1string = s.substring(x, endIndex);
				
			}
			
			if(s.charAt(x) == 'R' && s.charAt(x+1) == 'o' && s.charAt(x+2) == 'o' && s.charAt(x+3) == 'm' && s.charAt(x+4) == '1')
			{
				int endIndex = 0;
				//this is the Room0 string
				//look for Room1 and go back to the $$ for the substring index
				
				for(int y = x; y < s.length(); y++)
				{
					if(s.charAt(y) == 'R' && s.charAt(y+1) == 'o' && s.charAt(y+2) == 'o' && s.charAt(y+3) == 'm' && s.charAt(y+4) == '2')
					{
						endIndex = y;//we can use this because substring's terminator is exclusive
						break;
					}
				}
				
				room2string = s.substring(x, endIndex);
				
			}
			
			if(s.charAt(x) == 'R' && s.charAt(x+1) == 'o' && s.charAt(x+2) == 'o' && s.charAt(x+3) == 'm' && s.charAt(x+4) == '2')
			{
				int endIndex = 0;
				//this is the Room0 string
				//look for Room1 and go back to the $$ for the substring index
				
				//we're just going to the end of the string, so no need for a terminator here
				room3string = s.substring(x, s.length() - 2);
				
			}
			
		}
		
		
		
		parseRoom(room1string);
		parseRoom(room2string);
		parseRoom(room3string);
				
		
	}
	
	public void parseWorldEntity(String s)
	{
		//enemy
	}
	
	public void parseWorldObject(String s)
	{
		//chest
		
	}
	
	public void parseDoorObject(String s)
	{
		
		
	}

	public void parsePlayerCharacter(String s)
	{
		
		
	}

	
}


/*

charactersAvailable:....
pickCharacter (reply)
reportReadyToProceed (reply)
readyToProceed (no response needed. move to the actual gameworld)
dead (no response needed)
dying (no response needed)
yourMove Move Standard Action End Turn (these are 4 separate lines. reply after END TURN)
moveDirection (reply with N, S, E, W, SE, SW, NE, NW)
standardActionType (reply with attack or interactWithObject)
attackType (reply with melee or ranged)
EnemyList:....
selectEnemyWorldEntityID (reply with the ID of the enemy to attack)
interactionDirection (reply with N, S, E, W, SE, SW, NE, NW)


Player status




GameWorld status


		result += "GW:" + worldLength + "," + worldWidth + ";";
		
		
		for(Room r : rooms)
		{
			result += r.toString();
		}
		
		result += "$$";
		
		

WorldEntity status





WorldObject status 
		

		s = "WorldObject" + myWorldObjectID + ":" + xCoor + "," + yCoor + ";";
		
		if(isOpen)
		{
			s += "isOpen;";
		}
		
		
		s += "$$";
		
		return s;



Door status


		String result = "";
		
		result += "Door" + myDoorID + ":" + xRoomOneCoor + "," + yRoomOneCoor + ";" + + xRoomTwoCoor + "," + yRoomTwoCoor + ";" + "$$";
		
		return result;
		
		
		
Room status


		String result = "";
		
		
		result += "Room" + myRoomID + ":" + roomType + ";";
		result += widthSquares + "," + lengthSquares + ";";
		result += xWorldCoor + "," + yWorldCoor + ";";
		
		
		for(WorldEntity we : entities)
		{
			result += we.toString();
			
		}
		result += ";";
		
		
		for(WorldObject wo : contents)
		{
			result += wo.toString();
		}
		result += ";";
		
		
		for(Door d : doors)
		{
			result += d.toString();
		}
		result += ";";
		
		
		if(dungeonEntrance)
		{
			result += "true;";
		}
		else
		{
			result += "false;";
		}
		 
		
		
		result += "$$";
		
		return result;



*/