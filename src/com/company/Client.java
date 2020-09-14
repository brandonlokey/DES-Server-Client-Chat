package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Socket socket = null;
    private Scanner input = null;
    private DataOutputStream out = null;
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to the server.");

            input = new Scanner(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";
            while(!line.equals("Over")) {
                try {
                    System.out.print("\nEnter text: ");
                    line = input.nextLine();
                    System.out.println("********************");
                    System.out.println("Plaintext: " + line);
                    System.out.println("Key: ");
                    System.out.println("Encrypted: ");
                    System.out.println("********************");
                    out.writeUTF(line);
                }
                catch(IOException i) {
                    System.out.println(i);
                }
            }

            try
            {
                input.close();
                out.close();
                socket.close();
            }
            catch(IOException i)
            {
                System.out.println(i);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
}
