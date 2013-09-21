package component;

class ZorkConditionStatus extends ZorkCondition {
	String status;
	  public boolean evaluate(Zork zork)
	  {
	    ZorkObject tested = zork.Objects.get(object);
	    if (tested != null && tested.status.equals(status))
	      return true;
	    else
	      return false;
	  }
}

