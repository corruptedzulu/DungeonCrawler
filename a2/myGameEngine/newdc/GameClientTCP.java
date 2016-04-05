package myGameEngine.newdc;

import a2.DungeonCrawler3DSoonTM;
import a2.newdc.GhostAvatar;
import graphicslib3D.Point3D;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import sage.networking.client.GameConnectionClient;

public class GameClientTCP extends GameConnectionClient 
{ 
	private DungeonCrawler3DSoonTM game;
	private UUID id; 
	private GhostAvatar ghost;
 
	public GameClientTCP(InetAddress remAddr, int remPort, ProtocolType pType, DungeonCrawler3DSoonTM game) throws IOException
	{ 
		super(remAddr, remPort, pType); 
		this.game = game; 
		this.id = UUID.randomUUID(); 
		System.out.println(id.toString());
	} 
	
	protected void processPacket(Object msg) // override 
	{ 
		// extract incoming message into substrings. Then process: 
		String message = (String) msg; 
		String[] msgTokens = message.split(","); 
	 
		if(msgTokens.length > 0) 
		{ 		
			if(msgTokens[0].compareTo("join") == 0) 
			{
				// receive �gjoin�h 
				// format: join, success or join, failure 			
				if(msgTokens[1].compareTo("success") == 0) 
				{ 
					game.setConnected(true); 
					sendCreateMessage(game.getPlayerPosition()); 
				} 
				else if(msgTokens[1].compareTo("failure") == 0) 
					game.setConnected(false); 
			} 
			else if(msgTokens[0].compareTo("bye") == 0) // receive �gbye�h 
			{ 
				// format: bye, remoteId 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				removeGhostAvatar(ghostID); 
			} 
			else if(msgTokens[0].compareTo("create") == 0) // receive �gcreate�c�h 
			{  
				// format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				Point3D ghostPosition = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4])); 
				if (ghost==null){
					createGhostAvatar(ghostID, ghostPosition);
				}
			} 
			else if(msgTokens[0].compareTo("move") == 0) // receive �gmove�h 
			{ 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				Point3D ghostPosition = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				moveGhostAvatar(ghostID, ghostPosition);
			} 
			else if(msgTokens[0].compareTo("fire") == 0) // receive �gmove�h 
			{

				System.out.println("BANG!");
				//UUID ghostID = UUID.fromString(msgTokens[1]);
				//fireGhostBullet(ghostID);
			} 
			else if (msgTokens[0].compareTo("dsfr") == 0) // receive �gdetails for�h 
			{
				// format: dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]);
				Point3D location = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				
				if (ghost==null){
					createGhostAvatar(ghostID, location);
					sendReadyMessage();
				}
				
			} 
			else if(msgTokens[0].compareTo("wsds") == 0) // receive �gwants details�h 
			{ 
				Point3D pos = game.getPlayerPosition();
				UUID remID = UUID.fromString(msgTokens[1]);
				sendDetailsForMessage(remID, pos);
			} 
			else if(msgTokens[0].compareTo("scroll") == 0) // receive �gscroll�h 
			{ 
				game.startScrolling();
			} 
		}
	} 

	private void sendReadyMessage() {
		try 
		{ 
			String message = new String("ready," + id.toString()); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}

	private void createGhostAvatar(UUID ghostID, Point3D ghostPosition) {
		ghost = new GhostAvatar(ghostID, ghostPosition);
		ghost.scale(.30f,.30f,.30f);
		// ghost.rotate(-90, new Vector3D(1,0,0));
		game.textureObj(ghost, "ghostfighter.png");
		game.addGameWorldObject(ghost);
	}

	private void removeGhostAvatar(UUID ghostID) {
		game.removeGameWorldObject(ghost);
		ghost = null;		
	}
	
	private void moveGhostAvatar(UUID ghostID, Point3D ghostPosition) {	
		if (ghost!=null) 
			ghost.move(ghostPosition);
	}

	/* // uh no
	private void fireGhostBullet(UUID ghostID) {	
		if (ghost!=null) 
		{
			Bullet[] b = ghost.fire();
			for(int i = 0; i < b.length; i++)
			{
				game.addBullet(b[i]);
			}
		}
	}
	*/

	public void sendCreateMessage(Point3D pos) 
	{	
		// format: (create, localId, x,y,z) 
		try 
		{ 
			String message = new String("create," + id.toString()); 
			message += "," + pos.getX()+"," + pos.getY() + "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	 
	public void sendJoinMessage() 
	{
		// format: join, localId 
		try 
		{ 
			sendPacket(new String("join," + id.toString()));
		}
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	 
	
	public void sendByeMessage() 
	{  
		try 
		{ 
			String message = new String("bye," + id.toString()); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	
	public void sendFireMessage()
	{
		try 
		{ 
			String message = new String("fire," + id.toString()); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	
	public void sendDetailsForMessage(UUID remId, Point3D pos) 
	{
		try 
		{ 
			String message = new String("dsfr," + id.toString() + "," + remId.toString());
			message += "," + pos.getX(); 
			message += "," + pos.getY(); 
			message += "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	
	public void sendMoveMessage(Point3D pos) 
	{
		try 
		{ 
			String message = new String("move," + id.toString()); 
			message += "," + pos.getX(); 
			message += "," + pos.getY(); 
			message += "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
}