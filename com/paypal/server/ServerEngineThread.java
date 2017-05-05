package com.paypal.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
/**
 * 
 * @author Chithra
 * Creating new thread for each client
 *
 */
public class ServerEngineThread extends Thread {
	
	private Socket connection;
	private PrintWriter output;
	private BufferedReader input;
	private BufferedReader userInput;
	String dataInput;
	String chosenOne;
	Map<String,Socket> usersMap;
	
	public ServerEngineThread(Socket socket, HashMap<String,Socket> map, String chosenUser){
		this.connection = socket;
		usersMap=map;
		chosenOne = chosenUser;
	}
	
	@Override
	public void run(){
		try {
			
			
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			while(true){
				
				if((dataInput = input.readLine())!=null)
				System.out.println(dataInput);
				
				if(usersMap.get(chosenOne)!=null){
				Socket outSocket = usersMap.get(chosenOne);
				
				output = new PrintWriter(outSocket.getOutputStream(), true);
				}else{
					//only for testing
					output = new PrintWriter(connection.getOutputStream(), true);
				}
				
					output.println(dataInput);
					
					if(dataInput.equalsIgnoreCase("Exit")){
						break;
					}
				
			}

			input.close();
			output.flush();
			output.close();
			userInput.close();
			connection.close();
			
			
		} catch (IOException e) {
			System.out.println("Error sending/receiving messages");
			e.printStackTrace();
		}
		
		
		
	}

}
