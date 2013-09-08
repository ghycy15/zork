import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* Server
 * Multithreaded server that listens to port 8080
 * waiting connection of client
 *  
 * @author Jeanne Deng
 */

public class Server {
	
	public static void main(String[] agrs) {
		try {
			int i = 1;
			ServerSocket socket = new ServerSocket(8080);
			while(true) {
				System.out.println("Spawning " + i);
				Runnable run = new RequestHandler(socket.accept());
				Thread thread = new Thread(run);
				thread.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}