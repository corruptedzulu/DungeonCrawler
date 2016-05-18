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
		else if(s.contains("pickCharacter"))
		{
			//the client needs to reply with which character it wants
			
			
			//TODO ask the user which of the characters they want to use
			//reply to the server with that string
			
			
		}
		else if(s.contains("reportReadyToProceed"))
		{
			//TODO the initialization is completed. the client needs to reply with "ready" once it has moved to the teleporter
			
			
			
		}
		else if(s.contains("readyToProceed"))
		{
			//TODO move the player to the tile-world and start listening for updates
			//the game begins here
		}
		else if(s.contains("dead"))
		{
			//TODO the player has died and will no longer be able to make movements. it will still get the world updates
		}		
		else if(s.contains("dying"))
		{
			//TODO the player is dying and will not be able to make movements. it will still get the world updates
		}		
		else if(s.contains("yourMove"))
		{
			//TODO it is now the client's turn. the next three will list the options the player has. not all will be sent
		}		
		else if(s.contains("Move"))
		{
			
		}
		else if(s.contains("Standard Action"))
		{
			
		}
		else if(s.contains("End Turn"))
		{
			
		}
		
		else if(s.contains("moveDirection"))
		{
			//TODO reply with the direction you want to move
		}		
		else if(s.contains("standardActionType"))
		{
			//TODO reply with attack or interactWithObject
		}		
		else if(s.contains("attackType"))
		{
			//TODO reply with melee or ranged
		}		
		else if(s.contains("EnemyList:"))
		{
			//this is a player report
		}		
		else if(s.contains("selectEnemyWorldEntityID"))
		{
			//TODO reply with the enemy ID
		}		
		else if(s.contains("interactionDirection"))
		{
			//TODO reply with the direction to interact in
		}		
		/*else if()//PlayerCharacter
		{
			
		}
		else if()//Door
		{
			
		}
		else if()//GameWorld
		{
			
		}
		else if()//world entity
		{
			
		}
		else if()//world object (chest)
		{
			
		}*/
		
		
	}
	
	
	
	public void parseRoom(String s)
	{
		//objects
		//entities
		//doors
		//entrance/not entrance
	}
	
	public void parseGameWorld(String s)
	{
		//rooms basically
	}
	
	public void parseWorldEntity(String s)
	{
		//enemy and player character
	}
	
	public void parseWorldObject(String s)
	{
		//chest and door
		
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