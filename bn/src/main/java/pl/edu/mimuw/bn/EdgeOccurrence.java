package pl.edu.mimuw.bn;

/**
 * Class storing edge and its occurrence of not appearing in the group of subgraphs, with mechanisms allowing ordering these edges by their occurrence.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class EdgeOccurrence implements Comparable<EdgeOccurrence>
{
	/**
	 * Index of the first vertex of the edge.
	 */
	private int x;
	
	/**
	 * Index of the second vertex of the edge.
	 */
	private int y;
	
	/**
	 * Occurrence of not appearing in the group of subgraphs.
	 */
	private int occurrence;
	
	/**
	 * Constructor, sets value of edge vertices as given in parameters, and sets occurrence counter to 0.
	 * @param a	Index of the first vertex of the edge.
	 * @param b	Index of the second vertex of the edge.
	 */
	public EdgeOccurrence(int a, int b)
	{
		x = a;
		y = b;
		occurrence = 0;
	}
	
	/**
	 * Increases occurrence counter by 1.
	 */
	public void next()
	{
		occurrence++;
	}
	
	/**
	 * Gives access to the index of the first vertex of the edge.
	 * @return	Index of the first vertex of the edge.
	 */
	public int getX() { return x; }
	
	/**
	 * Gives access to the index of the second vertex of the edge.
	 * @return	Index of the second vertex of the edge.
	 */
	public int getY() { return y; }
	
	/**
	 * Gives access to the occurrence variable.
	 * @return	Occurrence of not appearing of the edge in the group of subgraphs.
	 */
	public int getOccurrence() { return occurrence; }
	
	/**
	 * Sets occurrence variable.
	 * @param n	New value.
	 */
	public void setOccurrence(int n) { occurrence = n; }
	
	/**
	 * Overrides compareTo function.
	 */
	@Override public int compareTo(EdgeOccurrence eo)
	{
		return occurrence - eo.occurrence;
	}
}
