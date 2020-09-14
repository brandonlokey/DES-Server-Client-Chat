package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    public Server(int port) {
        try {
            // Initialize server
            server = new ServerSocket(port);
            System.out.println("Server started.");

            System.out.println("Waiting for client");

            // Client connected
            socket = server.accept();
            System.out.println("Client accepted.\n");

            // Input stream
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            String decLine = "";

            while (!decLine.equals("Over")) {
                try {
                    int length = in.readInt();

                    if(length > 0) {
                        byte[] message = new byte[length];
                        in.readFully(message, 0, message.length);

                        System.out.println("********************");
                        System.out.println("Encrypted (bytes): " + message);
                        System.out.println("Encrypted: " + new String(message));
                        System.out.println("Decrypted: " + decLine);
                        System.out.println("********************");
                    }
                }
                catch(IOException i) {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection.");

            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
	// write your code here
        Server server = new Server(5000);
    }
}
