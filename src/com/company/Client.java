package com.company;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Client {
    private Socket socket = null;
    private Scanner input = null;
    private DataOutputStream out = null;

    public Client(String address, int port) {
        try {
            // Connect to server
            socket = new Socket(address, port);
            System.out.println("Connected to the server.");

            // Generate key
            KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
            SecretKey myDesKey = keygenerator.generateKey();
            byte[] key = myDesKey.getEncoded();

            // Generate cipher
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, myDesKey);

            // Write key to file
            File keyfile = new File("KeyFile.txt");
            OutputStream fileStream = new FileOutputStream(keyfile);
            fileStream.write(key);
            fileStream.close();

            // Initialize input/output streams
            input = new Scanner(System.in);
            out = new DataOutputStream(socket.getOutputStream());

            String line = "";
            // Take messages until "Over" entered
            while(!line.equals("Over")) {
                try {
                    System.out.print("\nEnter text: ");

                    line = input.nextLine();
                    System.out.println("********************");
                    System.out.println("Plaintext: " + line);
                    System.out.println("Key: " + key);
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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
}
