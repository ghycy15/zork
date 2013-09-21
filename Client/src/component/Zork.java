package component;

import java.io.File;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.util.*;

public class Zork {
	Scanner source = new Scanner(System.in);
	public String userInput="";
	
	/*Hashmaps to store the instance of the game*/
	public HashMap<String,ZorkRoom> Rooms = new HashMap<String,ZorkRoom>();
	public HashMap<String,ZorkItem> Items = new HashMap<String,ZorkItem>();
	public HashMap<String,ZorkContainer> Containers = new HashMap<String,ZorkContainer>();
	public HashMap<String,ZorkObject> Objects = new HashMap<String,ZorkObject>();
	public HashMap<String,ZorkCreature> Creatures = new HashMap<String,ZorkCreature>();
	public HashMap<String,String> Inventory = new HashMap<String,String>();
	public HashMap<String,String> ObjectLookup = new HashMap<String,String>();
	public String output;
	public File file;
	public String currentRoom;
	public String welcome;
	
		public Zork(String filename) throws Exception
		  {
		    int i,j,k,l,x,y,z;

		    file = new File(filename);
		    if (!file.canRead())
		    {
		      //System.out.println("Error opening file. Exiting...");
		       welcome = "Error opening file. Exiting...";
		    }

		    try
		    {
		      /* Open the xml file*/
		      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		      Document doc = builder.parse(file);

		      Element rootElement = doc.getDocumentElement();

		      /* Every single first generation child is a room, container, creature, or item. So load them in*/
		      NodeList nodes = rootElement.getChildNodes();
		      for (k=0;k<nodes.getLength();k++)
		      {
		        Node node = nodes.item(k);
		        Element element;
		        if (node instanceof Element)
		        {
		          element = (Element)node;
		          String tagType = element.getTagName();
		  
		          /* If it's a room ... */
		          if (tagType.equals("room"))
		          {
		            ZorkRoom tempRoom = new ZorkRoom();

		            /*Get all possible Room attributes*/

		            NodeList name = element.getElementsByTagName("name");
		            tempRoom.name = getString((Element)name.item(0));
		            NodeList type = element.getElementsByTagName("type");
		            if (type.getLength() > 0)
		            {
		              tempRoom.type = getString((Element)type.item(0));
		            }
		            else
		            {
		              tempRoom.type = "regular";
		            }
		            NodeList status = element.getElementsByTagName("status");
		            if (status.getLength() > 0)
		            {
		              tempRoom.status = getString((Element)type.item(0));
		            }
		            else
		            {
		              tempRoom.status = "";
		            }
		            NodeList description = element.getElementsByTagName("description");
		            tempRoom.description = getString((Element)description.item(0));
		            
		            NodeList items = element.getElementsByTagName("item");
		            for (j=0;j<items.getLength();j++)
		            {
		              Element item = (Element)items.item(j);
		              String itemName = getString(item);
		              tempRoom.item.put(itemName,itemName);
		            }

		            NodeList creatures = element.getElementsByTagName("creature");
		            for (j=0;j<creatures.getLength();j++)
		            {
		              Element creature = (Element)creatures.item(j);
		              String creatureName = getString(creature);
		              tempRoom.creature.put(creatureName,creatureName);
		            }
		            
		            NodeList triggers = element.getElementsByTagName("trigger");
		            for (j=0;j<triggers.getLength();j++)
		            {
		              ZorkTrigger tempTrigger = new ZorkTrigger();
		              Element trigger = (Element)triggers.item(j);
		              NodeList commands = trigger.getElementsByTagName("command");
		              for (l=0;l<commands.getLength();l++)
		              {
		                Element command = (Element)commands.item(l);
		                ZorkCommand tempCommand = new ZorkCommand();
		                tempCommand.command = getString(command);
		                tempTrigger.conditions.add(tempCommand);
		                tempTrigger.hasCommand = true;
		              }
		              NodeList conditions = trigger.getElementsByTagName("condition");
		              for (l=0;l<conditions.getLength();l++)
		              {
		                Element condition = (Element)conditions.item(l);
		                NodeList object = condition.getElementsByTagName("object");
		                NodeList has = condition.getElementsByTagName("has");
		                if (has.getLength() > 0)
		                {
		                  ZorkConditionHas tempConditionHas = new ZorkConditionHas();
		                  tempConditionHas.has = getString((Element)has.item(0));
		                  tempConditionHas.object = getString((Element)object.item(0));
		                  NodeList owner = condition.getElementsByTagName("owner");
		                  tempConditionHas.owner = getString((Element)owner.item(0));
		                  tempTrigger.conditions.add(tempConditionHas);

		                }
		                else
		                {
		                  ZorkConditionStatus tempConditionStatus = new ZorkConditionStatus();
		                  tempConditionStatus.object = getString((Element)object.item(0));
		                  NodeList sstatus = condition.getElementsByTagName("status");
		                  tempConditionStatus.status = getString((Element)sstatus.item(0));
		                  tempTrigger.conditions.add(tempConditionStatus);
		                }
		                
		              }
		              NodeList ttype = element.getElementsByTagName("type");
		              if (ttype.getLength() > 0)
		              {
		                tempTrigger.type = getString((Element)ttype.item(0));
		              }
		              else
		              {
		                tempTrigger.type = "single";
		              }
		              NodeList prints = trigger.getElementsByTagName("print");
		              for (l=0;l<prints.getLength();l++)
		              {
		                Element print = (Element)prints.item(l);
		                tempTrigger.print.add(getString(print));
		              }
		              NodeList actions = trigger.getElementsByTagName("action");
		              for (l=0;l<actions.getLength();l++)
		              {
		                Element action = (Element)actions.item(l);
		                tempTrigger.action.add(getString(action));
		              }

		              tempRoom.trigger.add(tempTrigger);
		            }
		 
		            NodeList containers = element.getElementsByTagName("container");
		            for (j=0;j<containers.getLength();j++)
		            {
		              Element container = (Element)containers.item(j);
		              String containerName = getString(container);
		              tempRoom.container.put(containerName,containerName);
		            }

		            NodeList borders = element.getElementsByTagName("border");
		            for (j=0;j<borders.getLength();j++)
		            {
		              Element border = (Element)borders.item(j);
		              String borderdirection = getString((Element)border.getElementsByTagName("direction").item(0));
		              String bordername = getString((Element)border.getElementsByTagName("name").item(0));
		              tempRoom.border.put(borderdirection,bordername);
		            }
		            
		            /*Add this room to the rooms hashmap, put it in the generic objects hashmap, and store it's type in the objectlookup hashmap*/
		            Rooms.put(tempRoom.name,tempRoom);
		            Objects.put(tempRoom.name,(ZorkObject)tempRoom);
		            ObjectLookup.put(tempRoom.name,"room");
		          }

		          /* If it's an item... */
		          else if (tagType.equals("item"))
		          {
		            ZorkItem tempItem = new ZorkItem();

		            /* Get all possible item attributes*/
		            NodeList name = element.getElementsByTagName("name");
		            if (name.getLength() > 0)
		              tempItem.name = getString((Element)name.item(0));

		            NodeList status = element.getElementsByTagName("status");
		            if (status.getLength() > 0)
		            {
		              tempItem.status = getString((Element)status.item(0));
		            }
		            else
		            {
		              tempItem.status = "";
		            }

		            NodeList description = element.getElementsByTagName("description");
		            if (description.getLength()>0)
		              tempItem.description = getString((Element)description.item(0));

		            NodeList writing = element.getElementsByTagName("writing");
		            if (writing.getLength()>0)
		              tempItem.writing = getString((Element)writing.item(0));

		            NodeList turnon = element.getElementsByTagName("turnon");
		            if (turnon.getLength()>0)
		            {
		              NodeList prints = element.getElementsByTagName("print");
		              for(j=0;j<prints.getLength();j++)
		              {
		                tempItem.turnOnPrint.add(getString((Element)prints.item(j)));
		              }
		              NodeList actions = element.getElementsByTagName("action");
		              for(j=0;j<actions.getLength();j++)
		              {
		                tempItem.turnOnAction.add(getString((Element)actions.item(j)));
		              }
		            }

		            NodeList triggers = element.getElementsByTagName("trigger");
		            for (j=0;j<triggers.getLength();j++)
		            {
		              ZorkTrigger tempTrigger = new ZorkTrigger();
		              Element trigger = (Element)triggers.item(j);
		              NodeList commands = trigger.getElementsByTagName("command");
		              for (l=0;l<commands.getLength();l++)
		              {
		                Element command = (Element)commands.item(l);
		                ZorkCommand tempCommand = new ZorkCommand();
		                tempCommand.command = getString(command);
		                tempTrigger.conditions.add(tempCommand);
		                tempTrigger.hasCommand = true;
		              }
		              NodeList conditions = trigger.getElementsByTagName("condition");
		              for (l=0;l<conditions.getLength();l++)
		              {
		                Element condition = (Element)conditions.item(l);
		                NodeList object = condition.getElementsByTagName("object");
		                NodeList has = condition.getElementsByTagName("has");
		                if (has.getLength() > 0)
		                {
		                  ZorkConditionHas tempConditionHas = new ZorkConditionHas();
		                  tempConditionHas.has = getString((Element)has.item(0));
		                  tempConditionHas.object = getString((Element)object.item(0));
		                  NodeList owner = condition.getElementsByTagName("owner");
		                  tempConditionHas.owner = getString((Element)owner.item(0));
		                  tempTrigger.conditions.add(tempConditionHas);

		                }
		                else
		                {
		                  ZorkConditionStatus tempConditionStatus = new ZorkConditionStatus();
		                  tempConditionStatus.object = getString((Element)object.item(0));
		                  NodeList sstatus = condition.getElementsByTagName("status");
		                  tempConditionStatus.status = getString((Element)sstatus.item(0));
		                  tempTrigger.conditions.add(tempConditionStatus);
		                }
		                
		              }
		              NodeList ttype = element.getElementsByTagName("type");
		              if (ttype.getLength() > 0)
		              {
		                tempTrigger.type = getString((Element)ttype.item(0));
		              }
		              else
		              {
		                tempTrigger.type = "single";
		              }
		              NodeList prints = trigger.getElementsByTagName("print");
		              for (l=0;l<prints.getLength();l++)
		              {
		                Element print = (Element)prints.item(l);
		                tempTrigger.print.add(getString(print));
		              }
		              NodeList actions = trigger.getElementsByTagName("action");
		              for (l=0;l<actions.getLength();l++)
		              {
		                Element action = (Element)actions.item(l);
		                tempTrigger.action.add(getString(action));
		              }

		              tempItem.trigger.add(tempTrigger);
		            }
		          
		            /* Put each item in the items hashmap, the generic objects hashmap, and store its type in objectlookup*/
		            Items.put(tempItem.name,tempItem);
		            Objects.put(tempItem.name,(ZorkObject)tempItem);
		            ObjectLookup.put(tempItem.name,"item");
		          
		          }

		          /* If it's a container... */
		          else if (tagType.equals("container"))
		          {
		            ZorkContainer tempCont = new ZorkContainer();

		            /*Get all possible container attributes*/

		            NodeList name = element.getElementsByTagName("name");
		            if (name.getLength()>0)
		              tempCont.name = getString((Element)name.item(0));

		            NodeList status = element.getElementsByTagName("status");
		            if (status.getLength()>0)
		              tempCont.status = getString((Element)status.item(0));
		            
		            /*Initially assume a closed container*/
		            tempCont.isOpen = false;
		            NodeList description = element.getElementsByTagName("description");
		            if (description.getLength()>0)
		              tempCont.description = getString((Element)description.item(0));

		            NodeList accepts = element.getElementsByTagName("accept");
		            for(j=0;j<accepts.getLength();j++)
		            {
		              /* If container has an accepts attribute, then it is always open*/
		              tempCont.isOpen = true;
		              tempCont.accept.add(getString((Element)accepts.item(j)));
		            }
		            
		            NodeList citems = element.getElementsByTagName("item");
		            for (j=0;j<citems.getLength();j++)
		            {
		              Element item = (Element)citems.item(j);
		              String itemName = getString(item);
		              tempCont.item.put(itemName,itemName);
		            }

		            NodeList triggers = element.getElementsByTagName("trigger");
		            for (j=0;j<triggers.getLength();j++)
		            {
		              ZorkTrigger tempTrigger = new ZorkTrigger();
		              Element trigger = (Element)triggers.item(j);
		              NodeList commands = trigger.getElementsByTagName("command");
		              for (l=0;l<commands.getLength();l++)
		              {
		                Element command = (Element)commands.item(l);
		                ZorkCommand tempCommand = new ZorkCommand();
		                tempCommand.command = getString(command);
		                tempTrigger.conditions.add(tempCommand);
		                tempTrigger.hasCommand = true;
		              }
		              NodeList conditions = trigger.getElementsByTagName("condition");
		              for (l=0;l<conditions.getLength();l++)
		              {
		                Element condition = (Element)conditions.item(l);
		                NodeList object = condition.getElementsByTagName("object");
		                NodeList has = condition.getElementsByTagName("has");
		                if (has.getLength() > 0)
		                {
		                  ZorkConditionHas tempConditionHas = new ZorkConditionHas();
		                  tempConditionHas.has = getString((Element)has.item(0));
		                  tempConditionHas.object = getString((Element)object.item(0));
		                  NodeList owner = condition.getElementsByTagName("owner");
		                  tempConditionHas.owner = getString((Element)owner.item(0));
		                  tempTrigger.conditions.add(tempConditionHas);

		                }
		                else
		                {
		                  ZorkConditionStatus tempConditionStatus = new ZorkConditionStatus();
		                  tempConditionStatus.object = getString((Element)object.item(0));
		                  NodeList sstatus = condition.getElementsByTagName("status");
		                  tempConditionStatus.status = getString((Element)sstatus.item(0));
		                  tempTrigger.conditions.add(tempConditionStatus);
		                }
		                
		              }
		              NodeList ttype = element.getElementsByTagName("type");
		              if (ttype.getLength() > 0)
		              {
		                tempTrigger.type = getString((Element)ttype.item(0));
		              }
		              else
		              {
		                tempTrigger.type = "single";
		              }
		              NodeList prints = trigger.getElementsByTagName("print");
		              for (l=0;l<prints.getLength();l++)
		              {
		                Element print = (Element)prints.item(l);
		                tempTrigger.print.add(getString(print));
		              }
		              NodeList actions = trigger.getElementsByTagName("action");
		              for (l=0;l<actions.getLength();l++)
		              {
		                Element action = (Element)actions.item(l);
		                tempTrigger.action.add(getString(action));
		              }

		              tempCont.trigger.add(tempTrigger);
		            }

		            /* Put each container in the containers hashmap, the generic object hashmap, and the objectlookup hashmap*/
		            Containers.put(tempCont.name,tempCont);
		            Objects.put(tempCont.name,(ZorkObject)tempCont);
		            ObjectLookup.put(tempCont.name,"container");
		          }

		          /* And finally, if it's a creature...*/
		          else if (tagType.equals("creature"))
		          {
		            ZorkCreature tempCreature = new ZorkCreature();

		            /* Get all possible creature attributes*/

		            NodeList name = element.getElementsByTagName("name");
		            if (name.getLength()>0)
		              tempCreature.name = getString((Element)name.item(0));

		            NodeList status = element.getElementsByTagName("status");
		            if (status.getLength()>0)
		              tempCreature.status = getString((Element)status.item(0));

		            NodeList description = element.getElementsByTagName("description");
		            if (description.getLength()>0)
		              tempCreature.description = getString((Element)description.item(0));

		            NodeList vulns = element.getElementsByTagName("vulnerability");
		            for(j=0;j<vulns.getLength();j++)
		            {
		              String vulnString = getString((Element)vulns.item(j));
		              tempCreature.vulnerability.put(vulnString,vulnString);
		            }

		            NodeList attacks = element.getElementsByTagName("attack");
		            for (j=0;j<attacks.getLength();j++)
		            {
		              Element attack = (Element)attacks.item(j);
		              NodeList conditions = attack.getElementsByTagName("condition");
		              for (l=0;l<conditions.getLength();l++)
		              {
		                Element condition = (Element)conditions.item(l);
		                NodeList object = condition.getElementsByTagName("object");
		                NodeList has = condition.getElementsByTagName("has");
		                if (has.getLength() > 0)
		                {
		                  ZorkConditionHas tempConditionHas = new ZorkConditionHas();
		                  tempConditionHas.has = getString((Element)has.item(0));
		                  tempConditionHas.object = getString((Element)object.item(0));
		                  NodeList owner = condition.getElementsByTagName("owner");
		                  tempConditionHas.owner = getString((Element)owner.item(0));
		                  tempCreature.conditions.add(tempConditionHas);

		                }
		                else
		                {
		                  ZorkConditionStatus tempConditionStatus = new ZorkConditionStatus();
		                  tempConditionStatus.object = getString((Element)object.item(0));
		                  NodeList sstatus = condition.getElementsByTagName("status");
		                  tempConditionStatus.status = getString((Element)sstatus.item(0));
		                  tempCreature.conditions.add(tempConditionStatus);
		                }
		                
		              }
		              NodeList prints = attack.getElementsByTagName("print");
		              for (l=0;l<prints.getLength();l++)
		              {
		                Element print = (Element)prints.item(l);
		                tempCreature.print.add(getString(print));
		              }
		              NodeList actions = attack.getElementsByTagName("action");
		              for (l=0;l<actions.getLength();l++)
		              {
		                Element action = (Element)actions.item(l);
		                tempCreature.action.add(getString(action));
		              }

		            }

		            NodeList triggers = element.getElementsByTagName("trigger");
		            for (j=0;j<triggers.getLength();j++)
		            {
		              ZorkTrigger tempTrigger = new ZorkTrigger();
		              Element trigger = (Element)triggers.item(j);
		              NodeList commands = trigger.getElementsByTagName("command");
		              for (l=0;l<commands.getLength();l++)
		              {
		                Element command = (Element)commands.item(l);
		                ZorkCommand tempCommand = new ZorkCommand();
		                tempCommand.command = getString(command);
		                tempTrigger.conditions.add(tempCommand);
		                tempTrigger.hasCommand = true;
		              }
		              NodeList conditions = trigger.getElementsByTagName("condition");
		              for (l=0;l<conditions.getLength();l++)
		              {
		                Element condition = (Element)conditions.item(l);
		                NodeList object = condition.getElementsByTagName("object");
		                NodeList has = condition.getElementsByTagName("has");
		                if (has.getLength() > 0)
		                {
		                  ZorkConditionHas tempConditionHas = new ZorkConditionHas();
		                  tempConditionHas.has = getString((Element)has.item(0));
		                  tempConditionHas.object = getString((Element)object.item(0));
		                  NodeList owner = condition.getElementsByTagName("owner");
		                  tempConditionHas.owner = getString((Element)owner.item(0));
		                  tempTrigger.conditions.add(tempConditionHas);

		                }
		                else
		                {
		                  ZorkConditionStatus tempConditionStatus = new ZorkConditionStatus();
		                  tempConditionStatus.object = getString((Element)object.item(0));
		                  NodeList sstatus = condition.getElementsByTagName("status");
		                  tempConditionStatus.status = getString((Element)sstatus.item(0));
		                  tempTrigger.conditions.add(tempConditionStatus);
		                }
		                
		              }
		              NodeList ttype = element.getElementsByTagName("type");
		              if (ttype.getLength() > 0)
		              {
		                tempTrigger.type = getString((Element)ttype.item(0));
		              }
		              else
		              {
		                tempTrigger.type = "single";
		              }
		              NodeList prints = trigger.getElementsByTagName("print");
		              for (l=0;l<prints.getLength();l++)
		              {
		                Element print = (Element)prints.item(l);
		                tempTrigger.print.add(getString(print));
		              }
		              NodeList actions = trigger.getElementsByTagName("action");
		              for (l=0;l<actions.getLength();l++)
		              {
		                Element action = (Element)actions.item(l);
		                tempTrigger.action.add(getString(action));
		              }

		              tempCreature.trigger.add(tempTrigger);
		            }
		             
		            /* Put each creature in the creatures hashmap, the generic object hashmap, and the objectlookup hashmap*/
		            Creatures.put(tempCreature.name,tempCreature);
		            Objects.put(tempCreature.name,(ZorkObject)tempCreature);
		            ObjectLookup.put(tempCreature.name,"creature");
		          }
		            
		        }
		      }
		    }
		    catch (Exception e)
		    {
		      System.out.println("Invalid XML file, exiting");
		      throw new Exception(e);
		      //e.printStackTrace();
		      }

		    /* Some temporary initialization variables*/
		    String tempString;
		    ZorkContainer tempContainer;
		    ZorkTrigger tempTrigger;
		    currentRoom = "Entrance";
		    boolean skip = false;
		    
		    /* Print out the first entrance description, starting the game!*/
		    System.out.println(Rooms.get(currentRoom).description);
		    welcome =  Rooms.get(currentRoom).description;
		    /* There is no stopping in Zork, until we're done!!*/
		    /*while(true)
		    {
		      skip=false;
		      userInput = source.nextLine();*/

		      /*Now that we have the user command, check the input*/
		      //skip = checkAllTriggers();
		 
		      /*We only skip if a matched trigger overwrites the execution of a command*/
		     /* if (skip)
		      {
		        continue;
		      }*/

		      /* If we haven't skipped, perform the user action*/
		     // action(userInput);

		      /* Clear the user input, and check the triggers again (various states have changed, gnomes need to be found!*/
		     /* userInput = "";
		      checkAllTriggers();
		    }*/
		  }

		  /* Execute a user action or an action command from some <action> element that is not one of the "Special Commands"*/
		  public String action(String input)
		  {
			  userInput = input;
			 boolean skip = false;
		     skip = checkAllTriggers();
				 
		      /*We only skip if a matched trigger overwrites the execution of a command*/
		      if (skip)
		      {
		        return output;
		      }
		    int i,j,k,l,x,y,z;
		    String tempString;
		    ZorkContainer tempContainer;
		    String result = null;

		    /* Movement */
		    if (input.equals("n") || input.equals("s") || input.equals("e") || input.equals("w"))
		    {
		      result = move(input);
		      //return result;
		    }
		    /* Inventory */
		    else if (input.equals("i"))
		    {
		      result = inventory();
		      //return result;
		    }
		    /* Take */
		    else if (input.startsWith("take") && input.split(" ").length>=1)
		    {
		      tempString = input.split(" ")[1];
		      if ((Rooms.get(currentRoom)).item.get(tempString) != null)
		      {
		        Inventory.put(tempString,tempString);
		        ZorkRoom tempRoom = (Rooms.get(currentRoom));
		        tempRoom.item.remove(tempString);
		        Rooms.put(tempRoom.name,tempRoom);
		        //System.out.println("Item "+tempString+" added to inventory.");
		        result = "Item " + tempString + " added to inventory";
		        //return result;
		      }
		      else
		      {
		        /*Search all containers in the current room for the item!*/
		        boolean found=false;
		        for (String key : Rooms.get(currentRoom).container.keySet())
		        {
		          tempContainer = Containers.get(key);
		          if (tempContainer != null && tempContainer.isOpen && tempContainer.item.get(tempString) != null)
		          {
		            Inventory.put(tempString,tempString);
		            tempContainer.item.remove(tempString);
		            Containers.put(tempContainer.name,tempContainer);
		            //System.out.println("Item "+tempString+" added to inventory.");
		            result = "Item " + tempString + " added to inventory";
		            found=true;
		            break;
		          }
		        }
		        if (!found)
		          //System.out.println("Error");
		        	result = "Cannot take the item";
		      }
		    }
		    
		    /* Open Exit (you should be so lucky)*/
		    else if (input.equals("open exit"))
		    {
		      if (Rooms.get(currentRoom).type.equals("exit"))
		      {
		        //System.out.println("Game Over");
		    	result = "Game Over\n You win!";
		        //System.exit(0);
		      }
		      else
		      {
		        //System.out.println("Error");
		    	  result = "You cannot open the exit now";
		      }
		    }
		    /* Open a container */
		    else if (input.startsWith("open") && input.split(" ").length>=1)
		    {
		      tempString = input.split(" ")[1];
		      String found = Rooms.get(currentRoom).container.get(tempString);
		      if (found != null)
		      {
		        tempContainer = Containers.get(tempString);
		        tempContainer.isOpen = true;
		        result = containerInventory(tempContainer.item,tempString);
		        //result = "you opened the " + tempString + 
		      }
		      else
		      {
		    	  result = "Cannot open " + tempString;
		        //System.out.println("Error");
		      }
		    }
		    /* Read an object */
		    else if (input.startsWith("read") && input.split(" ").length>=1)
		    {
		      tempString = input.split(" ")[1];
		      ZorkItem tempItem;
		      if (Inventory.get(tempString) != null)
		      {
		        tempItem = Items.get(tempString);
		        if (tempItem.writing != null && tempItem.writing != "")
		        {
		          //System.out.println(tempItem.writing);
		        	result = tempItem.writing;
		        }
		        else
		        {
		          result = "Nothing written";
		          //System.out.println("Nothing written.");
		        }
		      }
		      else
		      {
		    	result = "You cannot read this";  
		       // System.out.println("Error");
		      }
		    }
		    /* Drop an item*/
		    else if (input.startsWith("drop") && input.split(" ").length>=1)
		    {
		      tempString = input.split(" ")[1];
		      if (Inventory.get(tempString) != null)
		      {
		        ZorkRoom tempRoom = Rooms.get(currentRoom);
		        tempRoom.item.put(tempString,tempString);
		        Rooms.put(tempRoom.name,tempRoom);
		        Inventory.remove(tempString);
		        //System.out.println(tempString+" dropped.");
		        result = tempString + " dropped";
		      }
		      else
		      {
		        //System.out.println("Error");
		    	  result = "You cannot drop the item or you don't have it";
		      }
		    }
		    /* Put an item somewhere */
		    else if (input.startsWith("put") && input.split(" ").length>=4)
		    {
		      tempString = input.split(" ")[1];
		      String destination = input.split(" ")[3];
		      if (Rooms.get(currentRoom).container.get(destination) != null && Containers.get(destination).isOpen && Inventory.get(tempString) != null)
		      {
		        tempContainer = Containers.get(destination);
		        tempContainer.item.put(tempString,tempString);
		        Inventory.remove(tempString);
		        //System.out.println("Item "+tempString+" added to "+destination+".");
		        result = "Item "+tempString+" added to "+destination+".";
		      }
		      else
		      {
		        //System.out.println("Error");
		    	  result = "You cannot put this object here";
		      }
		    }
		    /* Turn on an item*/
		    else if (input.startsWith("turn on") && input.split(" ").length>=3)
		    {
		      tempString = input.split(" ")[2];
		      ZorkItem tempItem;
		      if (Inventory.get(tempString) != null)
		      {
		        tempItem = Items.get(tempString);
		        //System.out.println("You activate the "+tempString+".");
		        result = "You activate the "+tempString+".\n";
		        if (tempItem != null)
		        {
		          for (y=0;y<tempItem.turnOnPrint.size();y++)
		          {
		            result += (tempItem.turnOnPrint.get(y));
		          }
		          for (y=0;y<tempItem.turnOnAction.size();y++)
		          {
		            performAction(tempItem.turnOnAction.get(y));
		          }
		        }
		        else
		        {
		          //System.out.println("Error");
		        	result = "This item cannot be turned on";
		        }
		      }
		      else
		      {
		        //System.out.println("Error");
		        	result = "This item cannot be turned on";
		      }
		    }
		    
		    /* Attempt an attack, you feeling lucky?*/
		    else if (input.startsWith("attack") && input.split(" ").length>=4)
		    {
		      tempString = input.split(" ")[1];
		      ZorkCreature tempCreature;
		      String weapon = input.split(" ")[3];
		      if (Rooms.get(currentRoom).creature.get(tempString) != null)
		      {
		        tempCreature = Creatures.get(tempString);
		        if (tempCreature != null && Inventory.get(weapon)!= null)
		        {
		          if (tempCreature.attack(this,weapon))
		          {
		            //System.out.println("You assault the "+tempString+" with the "+weapon+".");
		        	  result = "You assault the "+tempString+" with the "+weapon+".";
		            for (y=0;y<tempCreature.print.size();y++)
		            {
		              System.out.println(tempCreature.print.get(y));
		              result = result + "\n" + tempCreature.print.get(y);
		            }
		            for (y=0;y<tempCreature.action.size();y++)
		            {
		              performAction(tempCreature.action.get(y));
		            }
		          }
		          else
		          {
		            //System.out.println("Error");
		        	  result = "You cannot attack it";
		          }
		        }
		        else
		        {
		          //System.out.println("Error");
	        	  result = "You cannot attack it";

		        }
		      }
		      else
		          //System.out.println("Error");
	        	  result = "You cannot attack it";

		    }
		    /* Invalid command*/
		    else
		    {
		      //System.out.println("Error");
	        	  result = "Run command";

		    }
		    userInput = "";
		    checkAllTriggers();
		    if(!output.equals("")){
		    	result = result + "\n" + output;
		    }
		    System.out.println("\noutput:"+output +"output");
		    return result;
		  }

		
		  
		  
		  
		  
		  
		  
		  
		  /* Check triggers */
		  public boolean checkAllTriggers()
		  {
			  output = "";
		    /*Variable initialization*/
		    boolean skip=false;
		    int i,j,k,l,x,y,z;
		    ZorkTrigger tempTrigger;
		    ZorkContainer tempContainer;
		    /*Check Room triggers*/
		    for(x=0;x<Rooms.get(currentRoom).trigger.size();x++)
		    {
		      tempTrigger=Rooms.get(currentRoom).trigger.get(x);
		      if (tempTrigger.evaluate(this))
		      {
		        for (y=0;y<tempTrigger.print.size();y++)
		        {
		          //System.out.println(tempTrigger.print.get(y));
		          output += tempTrigger.print.get(y);
		        }
		        for (y=0;y<tempTrigger.action.size();y++)
		        {
		          performAction(tempTrigger.action.get(y));
		        }
		        if (tempTrigger.hasCommand)
		        {
		          skip = true;
		        }
		        if (tempTrigger.type.equals("single"))
		        {
		          Rooms.get(currentRoom).trigger.remove(x);
		        }
		      }
		    }

		    /* Check items in the containers in the room */
		    for(String key: Rooms.get(currentRoom).container.keySet())
		    {
		      tempContainer = Containers.get(key);
		      for(String key2: tempContainer.item.keySet())
		      {
		        ZorkItem tempItem = Items.get(key2);
		        for(x=0;x<tempItem.trigger.size();x++)
		        {
		          tempTrigger = tempItem.trigger.get(x);
		          if (tempTrigger.evaluate(this))
		          {
		            for (y=0;y<tempTrigger.print.size();y++)
		            {
		              //System.out.println(tempTrigger.print.get(y));
		              output += tempTrigger.print.get(y);
		            }
		            for (y=0;y<tempTrigger.action.size();y++)
		            {
		              performAction(tempTrigger.action.get(y));
		            }
		            if (tempTrigger.hasCommand)
		            {
		              skip = true;
		            }
		            if (tempTrigger.type.equals("single"))
		            {
		              tempItem.trigger.remove(x);
		            }
		          }
		        }
		      }
		       
		      /* Check all containers in the room*/
		      for(x=0;x<tempContainer.trigger.size();x++)
		      {
		        tempTrigger = tempContainer.trigger.get(x);
		        if (tempTrigger.evaluate(this))
		        {
		          for (y=0;y<tempTrigger.print.size();y++)
		          {
		            //System.out.println(tempTrigger.print.get(y));
		            output += tempTrigger.print.get(y);
		          }
		          for (y=0;y<tempTrigger.action.size();y++)
		          {
		            performAction(tempTrigger.action.get(y));
		          }
		          if (tempTrigger.hasCommand)
		          {
		            skip = true;
		          }
		          if (tempTrigger.type.equals("single"))
		          {
		            tempContainer.trigger.remove(x);
		          }
		        }
		      }
		    }

		    /* Check all creatures in the room */
		    for(String key: Rooms.get(currentRoom).creature.keySet())
		    {
		      ZorkCreature tempCreature = Creatures.get(key);
		      for(x=0;x<tempCreature.trigger.size();x++)
		      {
		        tempTrigger = tempCreature.trigger.get(x);
		        if (tempTrigger.evaluate(this))
		        {
		          for (y=0;y<tempTrigger.print.size();y++)
		          {
		            //System.out.println(tempTrigger.print.get(y));
		            output += tempTrigger.print.get(y);
		          }
		          for (y=0;y<tempTrigger.action.size();y++)
		          {
		            performAction(tempTrigger.action.get(y));
		          }
		          if (tempTrigger.hasCommand)
		          {
		            skip = true;
		          }
		          if (tempTrigger.type.equals("single"))
		          {
		            tempCreature.trigger.remove(x);
		          }
		        }
		      }
		    }

		    /* Check items in inventory */
		    for(String key: Inventory.keySet())
		    {
		      ZorkItem tempItem = Items.get(key);
		      for(x=0;x<tempItem.trigger.size();x++)
		      {
		        tempTrigger = tempItem.trigger.get(x);
		        if (tempTrigger.evaluate(this))
		        {
		          for (y=0;y<tempTrigger.print.size();y++)
		          {
		            //System.out.println(tempTrigger.print.get(y));
		            output += tempTrigger.print.get(y);
		          }
		          for (y=0;y<tempTrigger.action.size();y++)
		          {
		            performAction(tempTrigger.action.get(y));
		          }
		          if (tempTrigger.hasCommand)
		          {
		            skip = true;
		          }
		          if (tempTrigger.type.equals("single"))
		          {
		            tempItem.trigger.remove(x);
		          }
		        }
		      }
		    }

		    /* Check items in room */
		    for(String key: Rooms.get(currentRoom).item.keySet())
		    {
		      ZorkItem tempItem = Items.get(key);
		      for(x=0;x<tempItem.trigger.size();x++)
		      {
		        tempTrigger = tempItem.trigger.get(x);
		        if (tempTrigger.evaluate(this))
		        {
		          for (y=0;y<tempTrigger.print.size();y++)
		          {
		            //System.out.println(tempTrigger.print.get(y));
		            output += tempTrigger.print.get(y);
		          }
		          for (y=0;y<tempTrigger.action.size();y++)
		          {
		            performAction(tempTrigger.action.get(y));
		          }
		          if (tempTrigger.hasCommand)
		          {
		            skip = true;
		          }
		          if (tempTrigger.type.equals("single"))
		          {
		            tempItem.trigger.remove(x);
		          }
		        }
		      }
		    }
		    return skip;
		  }

		  /*Basic movement function */
		  public String move(String direction)
		  {
		    String fullDirection = "";
		    String destination="";
		    if (direction.equals("n"))
		    {
		      fullDirection="north";
		    }
		    else if (direction.equals("s"))
		    {
		      fullDirection="south";
		    }
		    else if (direction.equals("e"))
		    {
		      fullDirection="east";
		    }
		    else if (direction.equals("w"))
		    {
		      fullDirection="west";
		    }
		     
		    destination = (Rooms.get(currentRoom)).border.get(fullDirection);
		    if (destination != null)
		    {
		      currentRoom = destination;
		      return Rooms.get(currentRoom).description;
		      
		    }
		    else
		    {
		      //System.out.println("Can't go that way.");
		    	String str = "Can't go that way";
		    	return str;
		    }
		  }

		  /* This is used to perform the "Special Actions"*/
		  public void performAction(String action)
		  {
		    String object;
		    String objectType;
		    /* Update: figure out what type of item it is, and then change it's status*/
		    if (action.startsWith("Update"))
		    {
		      object = action.split(" ")[1];
		      String newStatus = action.split(" ")[3];

		      objectType = ObjectLookup.get(object);
		      if (objectType.equals("room"))
		      {
		        ZorkRoom tempRoom = Rooms.get(object);
		        tempRoom.status = newStatus;
		        Rooms.put(tempRoom.name,tempRoom);
		      }
		      else if (objectType.equals("container"))
		      {
		        ZorkContainer tempContainer = Containers.get(object);
		        tempContainer.status = newStatus;
		        Containers.put(tempContainer.name,tempContainer);

		      }
		      else if (objectType.equals("creature"))
		      {
		        ZorkCreature tempCreature = Creatures.get(object);
		        tempCreature.status = newStatus;
		        Creatures.put(tempCreature.name,tempCreature);

		      }
		      else if (objectType.equals("item"))
		      {
		        ZorkItem tempItem = Items.get(object);
		        tempItem.status = newStatus;
		        Items.put(tempItem.name,tempItem);

		      }
		      
		    }
		    /*Game Over: pretty straight forward*/
		    else if (action.equals("Game Over"))
		    {
		      System.out.println("Victory!");
		      //System.exit(0);
		    }

		    /* Add: figure out what type the destination is, then what type the object is. Then add object to destination if it makes sense */
		    else if (action.startsWith("Add"))
		    {
		      String destination = action.split(" ")[3];
		      object = action.split(" ")[1];
		      objectType = ObjectLookup.get(object);
		      String destinationType = ObjectLookup.get(destination);
		      if (destinationType.equals("room"))
		      {
		        ZorkRoom tempRoom = Rooms.get(destination);
		        if (objectType.equals("item"))
		          tempRoom.item.put(object,object);
		        else if (objectType.equals("creature"))
		          tempRoom.creature.put(object,object);
		        else if (objectType.equals("container"))
		          tempRoom.container.put(object,object);
		        else
		          System.out.println("Error");
		        Rooms.put(tempRoom.name,tempRoom);
		      }
		      else if (destinationType.equals("container"))
		      {
		        ZorkContainer tempContainer = Containers.get(destination);
		        if (objectType.equals("item"))
		          tempContainer.item.put(object,object);
		        else
		          System.out.println("Error");
		        Containers.put(tempContainer.name,tempContainer);
		      }
		      else
		      {
		        System.out.println("Error");
		      }
		    }

		    /* Delete: figure out what object it is and delete it accordingly. Rooms are especially tricky */
		    else if (action.startsWith("Delete"))
		    {
		      object = action.split(" ")[1];
		      objectType = ObjectLookup.get(object);
		      Objects.remove(object);
		      if (objectType.equals("room"))
		      {
		        ZorkRoom tempRoom;
		        for (String key : Rooms.keySet())
		        {
		          tempRoom = Rooms.get(key);
		          for (String key2 : tempRoom.border.keySet())
		          {
		            if (tempRoom.border.get(key2).equals(object))
		            {
		              tempRoom.border.remove(key2);
		            }
		          }
		          Rooms.put(tempRoom.name,tempRoom);
		        }
		      }
		      else if (objectType.equals("item"))
		      {
		        ZorkRoom tempRoom;
		        for (String key : Rooms.keySet())
		        {
		          tempRoom = Rooms.get(key);
		          if (tempRoom.item.get(object) != null)
		          {
		            tempRoom.item.remove(object);
		            Rooms.put(tempRoom.name,tempRoom);
		          }
		        }
		        ZorkContainer tempContainer;
		        for (String key : Containers.keySet())
		        {
		          tempContainer = Containers.get(key);
		          if (tempContainer.item.get(object) != null)
		          {
		            tempContainer.item.remove(object);
		            Containers.put(tempContainer.name,tempContainer);
		          }
		        }
		      }
		      else if (objectType.equals("container"))
		      {
		        ZorkRoom tempRoom;
		        for (String key : Rooms.keySet())
		        {
		          tempRoom = Rooms.get(key);
		          if (tempRoom.container.get(object) != null)
		          {
		            tempRoom.container.remove(object);
		            Rooms.put(tempRoom.name,tempRoom);
		          }
		        }
		      }
		      else if (objectType.equals("creature"))
		      {
		        ZorkRoom tempRoom;
		        for (String key : Rooms.keySet())
		        {
		          tempRoom = Rooms.get(key);
		          if (tempRoom.creature.get(object) != null)
		          {
		            tempRoom.creature.remove(object);
		            Rooms.put(tempRoom.name,tempRoom);
		          }
		        }
		      }
		    }
		    else
		    {
		      /*If it's not a "Special Action", just treat it normally */
		      userInput = action;
		      action(action);
		    }
		  }

		  /* Print out the what's in a container when it's been opened*/
		  public String containerInventory(HashMap<String,String> Container, String Name)
		  {
		    String output = "";
		    if (Container.isEmpty())
		    { 
		      String str = Name + " is empty";
		      //System.out.println(Name+" is empty");
		      return str;
		    }
		    else
		    {
		    	String str = Name+" contains";
		      //System.out.print(Name+" contains ");
		      for (String key : Container.keySet())
		      {
		        output += key+", ";
		      }
		      output = output.substring(0,output.length()-2);
		      str = str + " " + output + ".";
		      //System.out.println(output+".");
		      return str;
		    }
		  }

		  /* Print out the inventory when user types i */
		  public String inventory()
		  {
		    String output = "Inventory: ";
		    if (Inventory.isEmpty())
		    {
		      String str = "Inventory: empty";
		     // System.out.println("Inventory: empty");
		      return str;
		    }
		    else
		    {
		      for (String key : Inventory.keySet())
		      {
		        output += key+", ";
		      }
		      output = output.substring(0,output.length()-2);
		      //System.out.println(output);
		      return output;
		    }
		  }
		 
		  //implement here....., return save data to UI
		  public String saveGame(String name){
			  String result = name + ":;:" + currentRoom;
			  
			  return null;
		  }
		  
		/* Get a string from an element (XML parsing stuff)*/
		  public static String getString(Element e)
		  {
		     Node child = e.getFirstChild();
		     if (child instanceof CharacterData)
		     {
		       CharacterData cd = (CharacterData) child;
		       return cd.getData();
		     }
		     return "?";
		  }
		  

}