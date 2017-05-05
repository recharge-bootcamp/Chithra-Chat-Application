package com.paypal.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Chithra This class is the server for the chat application
 *
 */
public class ServerEngineImpl implements ServerEngine {
	ServerSocket server;
	Socket connection;
	String dataInput;
	private PrintWriter output;
	private BufferedReader input;
	private BufferedReader userInput;
	int numberOfClients;
	String chosenUser;
	private HashMap<String, Socket> allConnections;

	@Override
	public void createAndConnect() {
		try {
			System.out.println("Server started");
			server = new ServerSocket(3000);
			allConnections = new HashMap<String, Socket>();

			// Establish Connection
			while (true) {

				connection = server.accept();
				input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				if ((dataInput = input.readLine()) != null)
					// Parsing data for username and adding it to the map
					if ("#".equals(String.valueOf((dataInput.charAt(0))))) {
					allConnections.put(dataInput.substring(1), connection);
					if ("Abhi".equals(dataInput.substring(1))) {
					chosenUser = "Chit";
					}else{
						chosenUser="Abhi";
					}

					}
				System.out.println("Connection accepted, Server up and running");

				ServerEngineThread newThread = new ServerEngineThread(connection, allConnections, chosenUser);

				numberOfClients++;
				newThread.start();
			}

		} catch (UnknownHostException e) {
			System.out.println("Host not resolved..");
			e.printStackTrace();

		} catch (IOException e) {
			System.out.println("I/o error occured..");
			e.printStackTrace();
		}

	}

	public static void main(String args[]) {

		ServerEngine chatServer = new ServerEngineImpl();
		chatServer.createAndConnect();

	}

}
