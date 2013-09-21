package component;

abstract class ZorkCondition {
	String object;
	public abstract boolean evaluate(Zork zork);
}
