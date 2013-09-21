package component;

import java.util.ArrayList;
import java.util.HashMap;

class ZorkContainer extends ZorkObject {
	public String name;
	  public HashMap<String,String> item = new HashMap<String,String>();
	  public String description;
	  public ArrayList<String> accept = new ArrayList<String>();
	  boolean isOpen;
	  public ZorkContainer()
	  {
	  }
}
