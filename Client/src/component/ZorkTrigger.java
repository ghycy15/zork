package component;
import java.util.ArrayList;

public class ZorkTrigger {
	public ArrayList<ZorkCondition> conditions = new ArrayList<ZorkCondition>();
	  String type="single"; 
	  boolean hasCommand = false;
	  public ArrayList<String> print = new ArrayList<String>();
	  public ArrayList<String> action = new ArrayList<String>();

	  public boolean evaluate(Zork zork)
	  {
	    for(int i=0;i<conditions.size();i++)
	    {
	      if (!conditions.get(i).evaluate(zork))
	      {
	        return false;
	      }
	    }
	    return true;
	  }
}
