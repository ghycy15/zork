package component;


public class Item {
	String name;
	boolean available;//if true, item is ok for being taken
	public String description;
	boolean turnon;
	public int vulnerability;
	
	public Item(String name, boolean avai,String des,int vul){
		this.name = name;
		this.available = avai;
		this.description = des;
		this.turnon = false;
		this.vulnerability = vul;
	}
	
}
