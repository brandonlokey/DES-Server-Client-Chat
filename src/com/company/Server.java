package com.company;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

            // Get key
            Path path = Paths.get("KeyFile.txt");
            byte[] key = Files.readAllBytes(path);
            SecretKey desKey = new SecretKeySpec(key, 0, key.length, "DES");

            // Initialize decrypter
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, desKey);

            String decLine = "";

            while (!decLine.equals("Over")) {
                try {
                    int length = in.readInt();

                    if(length > 0) {
                        // Get message bytes
                        byte[] message = new byte[length];
                        in.readFully(message, 0, message.length);

                        // Decrypt it
                        byte[] decBytes = cipher.doFinal(message);
                        decLine = new String(decBytes);

                        System.out.println("********************");
                        //System.out.println("Encrypted (bytes): " + message);
                        System.out.println("Encrypted: " + new String(message));
                        System.out.println("Key: " + new String(key));
                        System.out.println("Decrypted: " + decLine);
                        System.out.println("********************");
                    }
                }
                catch(IOException i) {
                    System.out.println(i);
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Closing connection.");

            socket.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
	// write your code here
        Server server = new Server(5000);
    }
}
