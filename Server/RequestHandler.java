import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/* RequestHandler
 * Handles the client request to server.
 * 
 * @author Jeanne Deng
 */

public class RequestHandler implements Runnable {
	
	private Socket socket;
	
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	//@Override
	public void run() {
		try {
			try {
				Scanner in = new Scanner(socket.getInputStream());
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				
				while(in.hasNextLine()) {
					String line = in.nextLine();
					if(line.trim().equals("disconnect")) {
						break;
					}

					CommandHandler command = new CommandHandler(line);
					String output = command.getOutput();
					out.println(output);
					//System.out.println(output);
				}
			} finally {
				socket.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}