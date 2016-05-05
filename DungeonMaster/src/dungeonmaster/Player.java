package dungeonmaster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import PlayerCharacters.PlayerCharacter;

public class Player 
{
	
	private PlayerCharacter myCharacter;
	
	private Socket mySocket;
	
	private PrintWriter out;
	private BufferedReader in;
	
	public Player()
	{
		
	}

	public Socket getMySocket() 
	{
		return mySocket;
	}

	public void setMySocket(Socket mySocket) 
	{
		this.mySocket = mySocket;
		setUpReadAndWrite();
	}

	public PlayerCharacter getMyCharacter() 
	{
		return myCharacter;
	}

	public void setMyCharacter(PlayerCharacter myCharacter) 
	{
		this.myCharacter = myCharacter;
	}
	
	

	
	
	private void setUpReadAndWrite()
	{
		try 
		{
			out = new PrintWriter(mySocket.getOutputStream(), true);
		} 
		catch (IOException e2) 
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		try 
		{
			in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		} 
		catch (IOException e2) 
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	}

	public PrintWriter getOut() {
		return out;
	}


	public BufferedReader getIn() {
		return in;
	}
	

}
