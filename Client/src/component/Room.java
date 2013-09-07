package component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Room {

	private String name;
	private String status;
	private String type;
	private String description;
	private Map<String, Room> border;
	private List<Container> container;
	private List<Item> item;
	private List<Creature> creature;
	private List<Trigger> trigger;

	public Room() {
		item = new ArrayList<Item>();
		creature = new ArrayList<Creature>();
		trigger = new ArrayList<Trigger>();
		border = new HashMap<String, Room>();
		setBorder("n", null);
		setBorder("e", null);
		setBorder("s", null);
		setBorder("w", null);
	}

	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}
	
	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}
	
	public Room getBorder(String direction) {
		return border.get(direction);
	}

	public List<Item> getItem() {
		return item;
	}
	
	public List<Creature> getCreature() {
		return creature;
	}
	
	public List<Trigger> getTrigger() {
		return trigger;
	}
	
	public List<Container> getContainer() {
		return container;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBorder(String direction, Room nextRoom) {
		border.put(direction, nextRoom);
	}

	public boolean setItem(Item newItem) {
		return item.add(newItem);
	}
	
	public boolean setCreature(Creature newCreature) {
		return creature.add(newCreature);
	}
	
	public boolean setTrigger(Trigger newTrigger) {
		return trigger.add(newTrigger);
	}

	public boolean setContainer(Container newContainer) {
		return container.add(newContainer);
	}
	
	public boolean deleteItem(Item del) {
		return item.remove(del);
	}
	
	public boolean deleteCreature(Creature del) {
		return creature.remove(del);
	}
	
	public boolean deleteTrigger(Trigger del) {
		return trigger.remove(del);
	}
	
	public boolean deleteContainer(Container del) {
		return container.remove(del);
	}
}
