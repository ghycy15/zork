/* CommandHandler
 * Handles the command request from client.
 * 
 * @author Jeanne Deng
 */

public class CommandHandler {
	
	String s[];

	public CommandHandler(String command) {
		this.s = command.split(":;:");
	}

	public String getReturn() {
		String result = s[0] + ":;:";
		DatabaseConnector c = new DatabaseConnector();

		//////////////////////User/////////////////////////
		if(s[0].equals("Register")) {
			result += c.addUser(s[1], s[2]) ? "success" : "fail";
		} else if(s[0].equals("Login")) {
			result += c.isValidLogin(s[1], s[2]) ? "success" : "fail";
		}
		//////////////////////Game/////////////////////////
		else if(s[0].equals("SaveData")) {
			result += c.updateUserData(s[1], s[2], s[3]) ? "success" : "fail";
		} else if(s[0].equals("GetData")) {
			result += c.getUserData(s[1]);
		} else {
			result = "Nothing is being requested.";
		}

		return result;
	}
}