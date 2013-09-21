package component;

import java.util.ArrayList;
import java.util.HashMap;

public class ZorkCreature extends ZorkObject {
	public String name;
	  public String description;
	  public HashMap<String,String> vulnerability = new HashMap<String,String>();
	  public ArrayList<ZorkCondition> conditions = new ArrayList<ZorkCondition>();
	  public ArrayList<String> print = new ArrayList<String>();
	  public ArrayList<String> action = new ArrayList<String>();
	  public ZorkCreature()
	  {
	  }
	  /* Evaluate the success of an attack*/
	  public boolean attack(Zork zork,String weapon)
	  {
	    if (vulnerability.get(weapon) == null)
	    {
	      return false;
	    }
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
