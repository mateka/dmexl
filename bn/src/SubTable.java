package bn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Information subsystem with nominal attributes, induced by subset of attributes of another information system - object FullTable.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class SubTable implements Table
{
	/**
	 * Array containing table values, possible values of each attribute are always consecutive integers starting from 0. First dimension is about rows, second - about columns.
	 * It contains whole table - that is with all attributes, not only with attributes from considered here subset.
	 */
	private int[][] t;
	
	/**
	 * Vector containing number of possible values of each attribute.
	 * Again, it contains information about all attributes, not only these from the subset.
	 */
	private int[] v;
	
	/**
	 * Number of rows of the table.
	 */
	private int m;
	
	/**
	 * Number of columns of the table.
	 * Here, by number of columns we mean number of attributes only in the subset.
	 */
	private int n;
	
	/**
	 * Vector containing sorted indexes of attributes from the subset.
	 */
	private int[] s;

	/**
	 * Constructor, creates empty subtable.
	 */
	public SubTable()
	{
		t = null;
		v = null;
		m = 0;
		n = 0;
		s = null;
	}

	/**
	 * Constructor, creates subtable from given table (which might be already subtable itself), induced by random subset of attributes.
	 * @param tab	Given table.
	 * @param x	Size of random subset of attributes.
	 */
	public SubTable(Table tab, int x)
	{
		t = tab.getT();
		v = tab.getV();
		m = tab.getM();
		n = Math.max(x, 1);
		s = Generator.sample(n, tab.getN());
		Arrays.sort(s);
		
		if (tab instanceof SubTable)
		{
			SubTable stab = (SubTable) tab;
			for (int i = 0; i < s.length; i++)
			{
				s[i] = stab.getS(s[i]);
			}
		}
	}
	
	/**
	 * Constructor, creates subtable from given table (which might be already subtable itself), induced by random subset of attributes, including some specified by its index attribute.
	 * @param tab	Given table.
	 * @param x	Size of random subset of attributes.
	 * @param c	The attribute included in the subset (its index, which in the case of tab being FullTable is the same as value of the attribute).
	 */
	public SubTable(Table tab, int x, int c)
	{
		t = tab.getT();
		v = tab.getV();
		m = tab.getM();
		n = Math.max(x, 1);
		s = Generator.sample(n, tab.getN(), c);
		Arrays.sort(s);
		
		if (tab instanceof SubTable)
		{
			SubTable stab = (SubTable) tab;
			for (int i = 0; i < s.length; i++)
			{
				s[i] = stab.getS(s[i]);
			}
		}
	}
	
	/**
	 * Gives access to the array containing table values, possible values of each attribute are always consecutive integers starting from 0. First dimension is about rows, second - about columns.
	 * @param i	Row index.
	 * @param j	Column index.
	 * @return	Value of the s[j]-th attribute for the i-th row.
	 */
	@Override public int getT(int i, int j) { return t[i][s[j]]; }
	
	/**
	 * Gives access to memory address of the array containing table values.
	 * @return	Array representing whole table - including all attributes, not only these from the subset.
	 */
	@Override public int[][] getT() { return t; }
	
	/**
	 * Gives access to the vector of attributes description.
	 * @param i	Index.
	 * @return	Number of possible values of the s[i]-th attribute.
	 */
	@Override public int getV(int i) { return v[s[i]]; }
	
	/**
	 * Gives access to the vector of attributes from the subset.
	 * @param i	Index.
	 * @return	s[i].
	 */
	public int getS(int i) { return s[i]; }
	
	/**
	 * Gives access to memory address of the vector of attributes description.
	 * @return	Array representing specified vector - including all attributes, not only these from the subset.
	 */
	@Override public int[] getV() { return v; }
	
	/**
	 * @return	Number of rows.
	 */
	@Override public int getM() { return m; }
	
	/**
	 * @return	Number of columns.
	 */
	@Override public int getN() { return n; }
	
	/**
	 * Sets value of the t array element specified in parameters.
	 * @param i	Row index of the element.
	 * @param j	Column subset index of the element.
	 * @param x	New value of the element.
	 */
	@Override public void setT(int i, int j, int x) { t[i][s[j]] = x; }
	
	/**
	 * Sets value of the v array element specified in parameters.
	 * @param i	Subset index of the element.
	 * @param x	New value of the element.
	 */
	@Override public void setV(int i, int x) { v[s[i]] = x; }
	
	/**
	 * Merges two given in parameters subtables, in the sense of merging their subsets of attributes.
	 * @param a	First subtable to merge.
	 * @param b	Second subtable to merge.
	 * @return	New object of type TableMergeReturn, containing new SubTable - result of merging, together with some technical parameters.
	 */
	public static TableMergeReturn merge(SubTable a, SubTable b)
	{
		SubTable c = new SubTable();
		List<Integer> mrg = new ArrayList<Integer>();
		int[] x = a.s;
		int[] y = b.s;
		int[] nia = new int [a.s.length];
		int[] nib = new int [b.s.length];
		int[] ria = new int [a.v.length];
		int[] rib = new int [a.v.length];
		int i, j, k;
		Set<Integer> inters = new HashSet<Integer>();
		
		c.m = a.m;
		c.t = a.t;
		c.v = a.v;
		
		i = 0;
		j = 0;
		k = 0;
		
		while (true)
		{
			while (i < x.length && x[i] < y[j])
			{
				mrg.add(x[i]);
				nia[i] = k;
				i++;
				k++;
			}
			if (i == x.length)
			{
				while (j < y.length)
				{
					mrg.add(y[j]);
					nib[j] = k;
					j++;
					k++;
				}
				break;
			}
			while (j < y.length && x[i] > y[j])
			{
				mrg.add(y[j]);
				nib[j] = k;
				j++;
				k++;
			}
			if (j == y.length)
			{
				while (i < x.length)
				{
					mrg.add(x[i]);
					nia[i] = k;
					i++;
					k++;
				}
				break;
			}
			if (x[i] == y[j])
			{
				mrg.add(x[i]);
				inters.add(x[i]);
				nia[i] = k;
				nib[j] = k;
				ria[x[i]] = i;
				rib[x[i]] = j;
				i++;
				j++;
				k++;
			}
			if (i == x.length)
			{
				while (j < y.length)
				{
					mrg.add(y[j]);
					nib[j] = k;
					j++;
					k++;
				}
				break;
			}
			if (j == y.length)
			{
				while (i < x.length)
				{
					mrg.add(x[i]);
					nia[i] = k;
					i++;
					k++;
				}
				break;
			}
		}
		c.s = new int [mrg.size()];
		for (i = 0; i < c.s.length; i++) { c.s[i] = mrg.get(i); }
		c.n = c.s.length;
		
		return new TableMergeReturn(c, inters, nia, nib, ria, rib);
	}
	
	/**
	 * Returns accuracy of prediction of indicated attribute.
	 * @param cl	Vector of proposed classification.
	 * @param i	Attribute (index from the subset) for which classification was done.
	 * @return	Accuracy of prediction.
	 */
	@Override public double checkAccuracy(int[] cl, int i)
	{
		int sum = 0;
		
		for (int j = 0; j < cl.length; j++)
		{
			if (cl[j] == getT(j, i))
			{
				sum++;
			}
		}
		
		return (double) sum / (double) cl.length;
	}
}
