package myGameEngine.newdc;

import graphicslib3D.Point3D;

import java.io.IOException; 
import java.net.InetAddress; 
import java.util.UUID;

import sage.networking.server.GameConnectionServer; 
import sage.networking.server.IClientInfo; 

public class GameServerTCP extends GameConnectionServer<UUID> 
{ 
	public GameServerTCP(int localPort) throws IOException 
	{ 
		super(localPort, ProtocolType.TCP);
	} 
	 
	public void acceptClient(IClientInfo ci, Object o) 
	{ 
		if (getClients().size()==2) { return; } //only 2 players allowed
		
		String message = (String)o; 
		String[] messageTokens = message.split(","); 
	 
		if(messageTokens.length > 0) 
		{ 
			if(messageTokens[0].compareTo("join") == 0) // received �gjoin�h 
			{ 
				// format: join,localid 
				UUID clientID = UUID.fromString(messageTokens[1]); 
				addClient(ci, clientID);  
				sendJoinedMessage(clientID, true);
			} 
		} 
	} 

	public void processPacket(Object o, InetAddress senderIP, int sndPort) 
	{ 
		String message = (String) o; 
		String[] msgTokens = message.split(","); 
	 
		if(msgTokens.length > 0) 
		{ 
			if(msgTokens[0].compareTo("bye") == 0) // receive �gbye�h 
			{ 	
				// format: bye,localid 
				UUID clientID = UUID.fromString(msgTokens[1]); 
				sendByeMessages(clientID); 
				removeClient(clientID); 
			} 
	 
			else if(msgTokens[0].compareTo("create") == 0) // receive �gcreate�h 
			{ // format: create,localid,x,y,z 
				System.out.println("create message received from " + msgTokens[1]);
				UUID clientID = UUID.fromString(msgTokens[1]); 
				Point3D pos = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4])); 
				sendCreateMessages(clientID, pos); 
				sendWantsDetailsMessages(clientID); 
			} 
		
			else if(msgTokens[0].compareTo("dsfr") == 0) // receive �gdetails for�h 
			{ 
				System.out.println("Server recieved details from " + msgTokens[1] + " for " + msgTokens[2]);
				UUID clientID = UUID.fromString(msgTokens[1]);
				UUID remID = UUID.fromString(msgTokens[2]); 
				Point3D pos = new Point3D(Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]), Double.parseDouble(msgTokens[5])); 
				sendDetailsMessage(clientID, remID, pos); 
			}
		
			else if(msgTokens[0].compareTo("move") == 0) // receive �gmove�h 
			{ 
				UUID clientID = UUID.fromString(msgTokens[1]);
				Point3D pos = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				sendMoveMessages(clientID, pos);
			}
			
			else if(msgTokens[0].compareTo("fire") == 0) // receive �gfire�h 
			{ 
				UUID clientID = UUID.fromString(msgTokens[1]);
				sendFireMessages(clientID);
			}
			
			else if(msgTokens[0].compareTo("ready") == 0) // receive �gready�h 
			{ 
				System.out.println("ready received to server from " + msgTokens[1]);
				sendStartScrollingMessages();
			}
		}
	}
		 
	public void sendJoinedMessage(UUID clientID, boolean success) 
	{ 
		// format: join, success or join, failure 
		try 
		{ 
			String message = new String("join,"); 
			if (success) message += "success"; 
			else message += "failure"; 
			sendPacket(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}
	} 
	 
	public void sendCreateMessages(UUID clientID, Point3D position) 
	{ 
		// format: create, remoteId, x, y, z 
		try 
		{ 
			System.out.println("create message sent to everyone except " + clientID.toString());
			String message = new String("create," + clientID.toString()); 
			message += "," + position.getX(); 
			message += "," + position.getY(); 
			message += "," + position.getZ(); 
			forwardPacketToAll(message, clientID); 
		} 
		catch (IOException e)                                                                                                                                                                                                                                                                                 
		{ 
			e.printStackTrace(); 
		} 
	} 
	 
	public void sendDetailsMessage(UUID clientID, UUID remoteId, Point3D position) 
	{ 
		try 
		{ 
			String message = new String("dsfr," + clientID.toString());
			message += "," + position.getX(); 
			message += "," + position.getY(); 
			message += "," + position.getZ(); 
			sendPacket(message, remoteId);
			System.out.println("Server sent details to " + remoteId.toString() + " for " + clientID.toString());
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	 
	public void sendWantsDetailsMessages(UUID clientID) 
	{  
		try 
		{ 
			String message = new String("wsds," + clientID.toString()); 
			forwardPacketToAll(message, clientID); 
			System.out.println("wants details message sent to everyone except " + clientID.toString());
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	 
	public void sendMoveMessages(UUID clientID, Point3D position) 
	{  
		try 
		{ 
			String message = new String("move," + clientID.toString()); 
			message += "," + position.getX(); 
			message += "," + position.getY(); 
			message += "," + position.getZ();
			forwardPacketToAll(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	
	public void sendFireMessages(UUID clientID) 
	{  
		try 
		{ 
			String message = new String("fire," + clientID.toString()); 
			forwardPacketToAll(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	  
	public void sendByeMessages(UUID clientID) 
	{  
		try 
		{ 
			String message = new String("bye," + clientID.toString()); 
			forwardPacketToAll(message, clientID); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}

	public void sendStartScrollingMessages() {
		try 
		{ 
			String message = new String("scroll"); 
			sendPacketToAll(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
}
