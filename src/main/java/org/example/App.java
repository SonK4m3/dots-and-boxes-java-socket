package org.example;

import org.example.server.DotsAndBoxesServer;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        String ipAddress = "192.168.137.1";
//        String ipAddress = "localhost";
        int port = 8080; // Choose your desired port number
        DotsAndBoxesServer server = new DotsAndBoxesServer(new InetSocketAddress(ipAddress, port));
        server.start();
        System.out.println("Server is running on port " + port);
    }
}
