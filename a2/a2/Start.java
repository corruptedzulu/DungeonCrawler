package a2;

import myGameEngine.newdc.GameServerTCP;

import java.io.IOException;
import java.net.InetAddress;

public class Start
{
    public static void main(String [] args) throws IOException
    {
        // examples
        // -Dsun.java2d.d3d=false server 57909  // only server
        // -Dsun.java2d.d3d=false client 57909  // 1 client
        // -Dsun.java2d.d3d=false sf1 57909     // server and 1 client
        // -Dsun.java2d.d3d=false offline       // no network

        if (args.length < 2)
            return;

        if (args[1].contains("server"))
        {
            int port = Integer.parseInt(args[2]);
            System.out.println("starting server using port " + port);
            new GameServerTCP(port);
        }
        else if (args[1].contains("client"))
        {
            InetAddress address = InetAddress.getByName(args[2]);
            int port = Integer.parseInt(args[3]);
            System.out.println("starting client with connection " + address.getHostAddress() + ":" + port);
            new DungeonCrawler3DSoonTM(address, port).start();
        }
        else if (args[1].contains("sf1"))
        {
            int port = Integer.parseInt(args[2]);
            InetAddress address = InetAddress.getByName("localhost");

            System.out.println("starting server using port " + port);
            GameServerTCP server = new GameServerTCP(port);

            System.out.println("starting client with connection " + address.getHostAddress() + ":" + port);
            new DungeonCrawler3DSoonTM(address, port, server).start();
        }
        else if (args[1].contains("offline"))
        {
            System.out.println("starting offline client");
            new DungeonCrawler3DSoonTM().start();
        }
        else
            System.out.println("bad args");
    }
}
