package component;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private List<Item> items;
	private Room room;//current room
	private boolean exit;//1 means game over
	
	public Player(){
		items = new ArrayList<Item>();
		room = new Room();
		exit = false;		
	}
	
	public void move(String direction){//n,s,e,w
		
		if(room.getBorder(direction)==null){
			System.out.println("Can't go that way");
		}else{
			room = room.getBorder(direction);
		}
	}
	
	public List<Item> getItem() {//i
		return items;
	}
	
	public void takeItem(Item item){//take item
		items.remove(item);
		return;
	}
	
	
	
	
	
	

}
