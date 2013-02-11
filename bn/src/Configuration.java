package bn;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of values of variable parents.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class Configuration implements Comparable<Configuration>
{
	/**
	 * Values configuration.
	 */
	private List<Integer> c;

	/**
	 * Constructor, allocates memory for values configuration.
	 */
	public Configuration() { c = new ArrayList<Integer>(); }

	/**
	 * Clears configuration list.
	 */
	public void clear() { c.clear(); }

	/**
	 * Sets configuration to the vector of zeros.
	 * @param n	Length of the configuration.
	 */
	public void zero(int n)
	{
		c.clear();
		for (int i = 0; i < n; i++)
		{
			c.add(0);
		}
	}
	
	/**
	 * Converts configuration to the next one in lexicographical order.
	 * @param v	Element v[i] represents number of possible values (consecutive integers starting from 0) of the i-th element of the configuration.
	 * @return	True, if it the next configuration has been found, otherwise false.
	 */
	public boolean setNext(int[] v)
	{
		int k;
		
		for (int i = 0; i < c.size(); i++)
		{
			k = c.get(i);
			if (k == v[i] - 1)
			{
				c.set(i, 0);
			}
			else
			{
				c.set(i, k+1);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns new object of type Configuration which is the next one in lexicographical order.
	 * @param v	Element v[i] represents number of possible values (consecutive integers starting from 0) of the i-th element of the configuration.
	 * @return	New configuration if this next element exists, otherwise null.
	 */
	public Configuration next(int[] v)
	{
		Configuration cfg = new Configuration();
		int i, k;
		boolean ok = false;
		
		for (i = 0; i < c.size(); i++)
		{
			k = c.get(i);
			if (k == v[i] - 1)
			{
				cfg.add(0);
			}
			else
			{
				cfg.add(k+1);
				ok = true;
				break;
			}
		}
		
		if (ok)
		{
			for (k = i + 1; k < c.size(); k++)
			{
				cfg.add(c.get(k));
			}
			
			return cfg;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Puts element on the end of configuration list.
	 * @param i	New element.
	 */
	public void add(int i) { c.add(i); }
	
	/**
	 * Overrides compareTo function.
	 */
	@Override public int compareTo(Configuration conf)
	{
		for (int i = 0; i < c.size(); i++)
		{
			if (c.get(i) < conf.c.get(i)) { return -1; }
			if (conf.c.get(i) < c.get(i)) { return 1; }
		}
		
		return 0;
	}
	
	/**
	 * Overrides equals function.
	 */
	@Override public boolean equals(Object obj)
	{
		if (obj == null) { return false; }
		if (obj.getClass() != getClass()) { return false; }

		Configuration a = (Configuration) obj;
		if (c.size() != a.c.size()) { return false; }
		for (int i = 0; i < c.size(); i++) { if (c.get(i) != a.c.get(i)) { return false; } }
		
		return true;
	}

	/**
	 * Overrides hashCode function.
	 */
	@Override public int hashCode()
	{
		int sum = 0;
		
		for (int i : c) { sum += i; }
		
		return sum;
	}
	
	/**
	 * Returns new randomly generated object of type Configuration.
	 * @param n	Size of the configuration.
	 * @param v	Element v[i] represents number of possible values (consecutive integers starting from 0) of the i-th element of the configuration.
	 * @return	Random configuration.
	 */
	public static Configuration random(int n, int[] v)
	{
		Configuration cfg = new Configuration();
		
		for (int i = 0; i < n; i++)
		{
			cfg.add(Generator.random(v[i]));
		}
		
		return cfg;
	}
};