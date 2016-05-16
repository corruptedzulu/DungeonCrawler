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
	private BufferedReader bufferRead;
	
	
	public Client(String[] args)
	{
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		
		try 
		{
		    echoSocket = new Socket(hostName, portNumber);
		    out = new PrintWriter(echoSocket.getOutputStream(), true);
		    in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		    bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    
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
	
	public BufferedReader getConsoleIn()
	{
		return bufferRead;
	}
	
	public Socket getSocket()
	{
		return echoSocket;
	}
	
	public static void main(String args[])
	{
	
		String[] array = {"127.0.0.1", "7856"};
		Client c = new Client(array);
		
		
		String read = "";
		
		while(read != "quit")
		{
			try
			{
				if(c.getSocket().getInputStream().available() >= 2)
				{
					try
					{
						System.out.println(c.getIn().readLine());
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
			try
			{
				if(System.in.available() >= 2)
				{
					read = c.getConsoleIn().readLine();
					c.getOut().println(read);
				}
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			
			
			
		}
	
	}
}

