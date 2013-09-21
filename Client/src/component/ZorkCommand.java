package component;

public class ZorkCommand extends ZorkCondition {
	  String command;
	  public boolean evaluate(Zork zork)
	  {
	    if (command.equals(zork.userInput))
	      return true;
	    else
	      return false;
	  }
}
