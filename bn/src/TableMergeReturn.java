package bn;

import java.util.Set;

/**
 * Object of this type is returned by merge method of the Subtable class.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class TableMergeReturn
{
	/**
	 * Result of merging two subtables: lets name them A and B.
	 */
	private SubTable subtable;
	
	/**
	 * The set of common Table indexes for subtables A and B - that is common elements of A.s and B.s.
	 */
	private Set<Integer> intersection;
	
	/**
	 * For the given index of subtable A subset of attributes s this array returns index of this attribute in the new subtable - result of merging A and B.
	 */
	private int[] newIndexA;
	
	/**
	 * For the given index of subtable B subset of attributes s this array returns index of this attribute in the new subtable - result of merging A and B.
	 */
	private int[] newIndexB;
	
	/**
	 * For the given element of s array in subtable - result of merging (element indicated by its value, not index) the array returns index of this element in the s array for Subtable A.
	 * This array is filled only on positions corresponding to elements of intersection set.
	 */
	private int[] revIndexA;
	
	/**
	 * For the given element of s array in subtable - result of merging (element indicated by its value, not index) the array returns index of this element in the s array for Subtable B.
	 * This array is filled only on positions corresponding to elements of intersection set.
	 */
	private int[] revIndexB;

	/**
	 * Constructor, creates the object and sets value of all members as indicated in parameters.
	 * @param s	Value for member subtable.
	 * @param i	Value for member intersection.
	 * @param na	Value for member newIndexA.
	 * @param nb	Value for member newIndexB.
	 * @param ra	Value for member revIndexA.
	 * @param rb	Value for member revIndexB.
	 */
	public TableMergeReturn(SubTable s, Set<Integer> i, int[] na, int[] nb, int[] ra, int[] rb)
	{
		subtable = s;
		intersection = i;
		newIndexA = na;
		newIndexB = nb;
		revIndexA = ra;
		revIndexB = rb;
	}
	
	/**
	 * Returns appropriate member.
	 * @return	Value of member subtable.
	 */
	public SubTable getSubtable()
	{
		return subtable;
	}
	
	/**
	 * Returns appropriate member.
	 * @return	Value of member intersection.
	 */
	public Set<Integer> getIntersection()
	{
		return intersection;
	}
	
	/**
	 * Returns appropriate member.
	 * @return	Value of member newIndexA.
	 */
	public int[] getNewIndexA()
	{
		return newIndexA;
	}
	
	/**
	 * Returns appropriate member.
	 * @return	Value of member newIndexB.
	 */
	public int[] getNewIndexB()
	{
		return newIndexB;
	}
	
	/**
	 * Returns appropriate member.
	 * @return	Value of member revIndexA.
	 */
	public int[] getRevIndexA()
	{
		return revIndexA;
	}
	
	/**
	 * Returns appropriate member.
	 * @return	Value of member revIndexB.
	 */
	public int[] getRevIndexB()
	{
		return revIndexB;
	}
}
