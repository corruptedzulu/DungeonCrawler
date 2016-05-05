package Networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client 
{

	private Socket echoSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	
	public Client(String[] args)
	{
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		
		try 
		{
		    echoSocket = new Socket(hostName, portNumber);
		    out = new PrintWriter(echoSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		    
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
		}
	}


	public PrintWriter getOut() 
	{
		return out;
	}


	public BufferedReader getIn() 
	{
		return in;
	}
	
	
}
