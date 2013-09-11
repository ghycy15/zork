package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ZorkClient {

	public static String hostname = "128.46.101.71";
	private static String REGISTER = "Resgister";
	private static String LOGIN = "Login";
	private static String SAVEDATA = "SaveData";
	private static String GETDATA = "GetData";
	private static String SEPARATER = ":;:";
	
	
	/**
	 * check if the user exist and the password is correct
	 * 
	 * @param userName
	 * @param password
	 * @return true if the server return true
	 */
	public static boolean login(String userName, String password) {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			kkSocket = new Socket(hostname, 8080);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}

		out.println(LOGIN + SEPARATER + userName + SEPARATER + password);

		String fromServer;
		boolean succFromServer = false;
		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				if (fromServer.equals("true")) {
					succFromServer = true;
				}

			}
			out.close();
			in.close();
			kkSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return succFromServer;
	}
	
	/**
	 * register a new user
	 * 
	 * @param userName
	 * @param password
	 * @return true if the server return true
	 */
	public static boolean register(String userName, String password) {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			kkSocket = new Socket(hostname, 8080);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}

		out.println(REGISTER + SEPARATER + userName + SEPARATER + password);

		String fromServer;
		boolean succFromServer = false;
		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				if (fromServer.equals("true")) {
					succFromServer = true;
				}

			}
			out.close();
			in.close();
			kkSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return succFromServer;
	}
	
	/**
	 * push the current game process to server
	 * 
	 * @param (int) data slot NO.
	 * @param (String) game process
	 * @return true if the server return true
	 */
	public static boolean saveData(int dataSlot, String gameProcess) {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			kkSocket = new Socket(hostname, 8080);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}

		out.println(SAVEDATA + SEPARATER + dataSlot + SEPARATER + gameProcess);

		String fromServer;
		boolean succFromServer = false;
		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				if (fromServer.equals("true")) {
					succFromServer = true;
				}

			}
			out.close();
			in.close();
			kkSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return succFromServer;
	}
	
	/**
	 * request the user information
	 * 
	 * @param (String) user name
	 * @return the map of data slot and saved data, null if unexpected value returned from server
	 */
	public static Map<Integer,String> getData(String userName) {

		Socket kkSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			kkSocket = new Socket(hostname, 8080);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}

		out.println(GETDATA + SEPARATER + userName);

		String fromServer;
		Map<Integer, String> savedData = new HashMap<Integer, String>();
		try {
			while ((fromServer = in.readLine()) != null) {
				System.out.println(fromServer);
				String[] dataList = fromServer.split(SEPARATER);
				if(!dataList[0].equals("GetData")){
					return null;
				}
				savedData.put(Integer.valueOf(dataList[1]), dataList[2]);
				
			}
			out.close();
			in.close();
			kkSocket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return savedData;
	}
}
