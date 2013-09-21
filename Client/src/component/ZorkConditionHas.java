package component;

public class ZorkConditionHas extends ZorkCondition{
	String has;
	  String owner;
	  public boolean evaluate(Zork zork)
	  {
	    /*Inventory is a special case as it isn't the name of any object in the game, check for it specifically*/
	    if (owner.equals("inventory"))
	    {
	      if (zork.Inventory.get(object) != null && has.equals("yes") || zork.Inventory.get(object) == null && has.equals("no"))
	      {
	        return true;
	      }
	      else
	      {
	        return false;
	      }
	    }
	    else
	    {
	      /* is it a room?*/
	      ZorkRoom roomObject = zork.Rooms.get(owner);
	      if ( roomObject != null )
	      {
	        if ((roomObject).item.get(object) != null && has.equals("yes") || (roomObject).item.get(object) == null && has.equals("no"))
	        {
	          return true;
	        }
	        else
	        {
	          return false;
	        }
	      }
	      /* is it a container?*/
	      else
	      {
	        ZorkContainer containerObject = zork.Containers.get(owner);
	        if (containerObject != null )
	        {
	          if ((containerObject).item.get(object) != null && has.equals("yes") || (containerObject).item.get(object) == null && has.equals("no"))
	          {
	            return true;
	          }
	          else
	          {
	            return false;
	          }

	        }
	      }
	    }
	    
	    return false;
	  }
}
