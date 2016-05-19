package Networking;

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
		else if(s.regionMatches(0, "GameWorld", 0, 9))//GameWorld
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
		//objects
		//doors
		//entrance/not entrance
	}
	
	public void parseGameWorld(String s)
	{
		//rooms basically
	}
	
	public void parseWorldEntity(String s)
	{
		//enemy
	}
	
	public void parseWorldObject(String s)
	{
		//chest and door
		
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