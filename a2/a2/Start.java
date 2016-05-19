package a2;

import myGameEngine.GameServerTCP;

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

        new DungeonCrawler3DSoonTM().start();
    }
}
