package component;

import java.util.ArrayList;

public class ZorkItem extends ZorkObject {
	public String name;
	  public String description;
	  public String writing;
	  public ArrayList<String> turnOnPrint = new ArrayList<String>();
	  public ArrayList<String> turnOnAction = new ArrayList<String>();
	  public ZorkItem()
	  {
	  }
}
