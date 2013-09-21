package component;
import java.util.ArrayList;
import java.util.HashMap;

class ZorkRoom extends ZorkObject{ public String name;
	public String type="regular";
	public String description;
	public HashMap<String,String> border = new HashMap<String,String>();
	public HashMap<String,String> container = new HashMap<String,String>();
	public HashMap<String,String> item = new HashMap<String,String>();	
	public HashMap<String,String> creature = new HashMap<String,String>();
	public ZorkRoom()
	{
	}
}
