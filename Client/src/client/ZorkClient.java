package client;
//seasonxcc__xu205

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ZorkClient {

	public static String hostname = "128.46.101.71";// "parhub.ecn.purdue.edu";//"localhost";//"sslab05.cs.purdue.edu";

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
			kkSocket = new Socket(hostname, 4444);
			out = new PrintWriter(kkSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					kkSocket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host");
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection");
		}

		out.println("userName" + userName + ",password" + password);

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
}
