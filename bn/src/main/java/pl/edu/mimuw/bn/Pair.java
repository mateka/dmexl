package pl.edu.mimuw.bn;

/**
 * Ordered pair of numbers.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class Pair implements Comparable<Pair>
{
	/**
	 * First element of the pair.
	 */
	private int x;
	
	/**
	 * Second element of the pair.
	 */
	private int y;

	/**
	 * Constructor, sets both values to 0.
	 */
	public Pair() { x = 0; y = 0; }
	
	/**
	 * Constructor, sets values as indicated by parameters.
	 * @param a	Value of the first element.
	 * @param b	Value of the second element.
	 */
	public Pair(int a, int b) { x = a; y = b; }
	
	/**
	 * Constructor, copies given object.
	 * @param a	Object to be copied.
	 */	
	public Pair(Pair a) { x = a.x; y = a.y; }
	
	/**
	 * Copies given object.
	 * @param a	Object to be copied.
	 */
	public Pair set(Pair a) { x = a.x; y = a.y; return this; }
	
	/**
	 * Checks if the object is the same as specified in parameter.
	 * Returns true when it is the same, otherwise false.
	 * @param a	Object to be compered.
	 * @return	This object.
	 */
	public boolean equals(Pair a) { return ( x == a.x && y == a.y); }
	
	/**
	 * Performs vector addition with the specified in parameter object.
	 * Returns new object of type Pair - result of the sum.
	 * @param a	Object to be added.
	 * @return	New object - sum result.
	 */
	public Pair plus(Pair a) { Pair p = new Pair(x + a.x, y + a.y); return p; }
	
	/**
	 * Performs vector subtraction with the specified in parameter object.
	 * Returns new object of type Pair - result of the subtraction.
	 * @param a	Object to be subtracted.
	 * @return	New object - subtraction result.
	 */
	public Pair minus(Pair a) { Pair p = new Pair(x - a.x, y - a.y); return p; }
	
	/**
	 * Returns first element of the pair.
	 * @return	First element of the pair.
	 */
	public int getX() { return x; }
	
	/**
	 * Returns second element of the pair.
	 * @return	Second element of the pair.
	 */
	public int getY() { return y; }
	
	/**
	 * Sets value of the first element of the pair.
	 * @param a	New value.	
	 */
	public void setX(int a) { x = a; }
	
	/**
	 * Sets value of the second element of the pair.
	 * @param a	New value.	
	 */
	public void setY(int a) { y = a; }
	
	/**
	 * Sets value of both elements of the pair.
	 * @param a	New value for first element.
	 * @param b	New value for second element.
	 */
	public void setXY(int a, int b) { x = a; y = b; }
	
	/**
	 * Compares object with the specified in parameter object, in the sense of lexicographical order.
	 * @param a	Object to be compared.
	 * @return	-1 - if object is less, 0 - if is equal, 1 - if is bigger than specified in parameter object.
	 */	
	@Override public int compareTo(Pair a)
	{
		if (x < a.x || (x == a.x && y < a.y)) { return -1; }
		else if (x == a.x && y == a.y) { return 0; }
		else { return 1; }
	}

	/**
	 * Overrides equals function.
	 */
	@Override public boolean equals(Object obj)
	{
		if (obj == null) { return false; }
		if (obj.getClass() != getClass()) { return false; }

		Pair a = (Pair) obj;
		return ( x == a.x && y == a.y);
	}

	/**
	 * Overrides hashCode function.
	 */
	@Override public int hashCode()
	{
		return 1013 * x ^ 1009 * y;

	}
}
