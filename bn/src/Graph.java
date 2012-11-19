package bn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Bayesian network.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class Graph
{
	/**
	 * Edge properties in Bayesian network.
	 * @author Pawel Betlinski
	 * @version 1.0
	 * @since 2012
	 */
	private class Edge
	{
		/**
		 * Connection type of the edge between some ordered pair of nodes A and B:
		 * <ul>
		 * <li>0 - no connection,
		 * <li>1 - connection from A to B,
		 * <li>2 - connection from B to A.
		 * </ul>
		 */
		private int state;
		
		/**
		 * Cover state of the edge between some ordered pair of nodes A and B:
		 * <ul>
		 * <li>0 - not covered,
		 * <li>1 - covered.
		 * </ul>
		 */
		private int cover;
		
		/**
		 * Position of the edge in the vector <code>e</code> in class Graph.
		 */
		private int t;
		
		/**
		 * If the object represents edge between some nodes A and B, then v[I] indicate whether what kind of v-form is present between edges A, I, and B:
		 * <ul>
		 * <li>0 - v-form not present,
		 * <li>1 - v-form present, with converging arrows,
		 * <li>2 - v-form present, but without converging arrows.
		 * </ul>
		 */
		private int[] v;
		
		/**
		 * Constructor, only allocates memory, for the specified in corresponding table size.
		 */
		public Edge()
		{
			v = new int [table.getN()];
		}
		
		/**
		 * Constructor, only allocates memory, but for the specified as parameter size.
		 * @param n	Size of the array v.
		 */
		public Edge(int n)
		{
			v = new int [n];
		}
		
		/**
		 * Constructor, copies given object.
		 * @param a	Object to be copied.
		 */
		public Edge(Edge a)
		{
			int i;
			state = a.state;
			cover = a.cover;
			t = a.t;
			v = new int [table.getN()];
			
			for (i = 0; i < table.getN(); i++) { v[i] = a.v[i]; }
		}
		
		/**
		 * Copies given object.
		 * @param a	Object to be copied.
		 */
		public Edge set(Edge a)
		{
			int i;
			state = a.state;
			cover = a.cover;
			t = a.t;
			
			for (i = 0; i < table.getN(); i++) { v[i] = a.v[i]; }
			
			return this;
		}

		/**
		 * Sets value of state variable.
		 * @param a	New value.
		 */
		public void setState(int a) { state = a; }
		
		/**
		 * Sets value of cover variable.
		 * @param a	New value.
		 */
		public void setCover(int a) { cover = a; }
		
		/**
		 * Sets value of t variable.
		 * @param a	New value.
		 */
		public void setT(int a) { t = a; }
		
		/**
		 * Sets value of v variable.
		 * @param a	New value.
		 */
		public void setV(int[] a) { v = a; }

		/**
		 * Sets value of the specified element of v array.
		 * @param i	Element index.
		 * @param a	New value.
		 */
		public void setV(int i, int a) { v[i] = a; }

		/**
		 * @return	Value of state variable.
		 */
		public int getState() { return state; }
		
		/**
		 * @return	Value of cover variable.
		 */
		public int getCover() { return cover; }
		
		/**
		 * @return	Value of t variable.
		 */
		public int getT() { return t; }
		
		/**
		 * @return	Value of v variable.
		 */
		public int[] getV() { return v; }
	}
	
	/**
	 * The table on the basis of which network can be learned.
	 */
	private Table table;
	
	/**
	 * Information about what currently object graph contains:
	 * <ul>
	 * <li>0 - nothing - lack of graph,
	 * <li>1 - network structure, that is just a graph without any parameters,
	 * <li>2 - Bayesian network, that is graph with parameters.
	 * </ul>
	 */
	private int state;

	/**
	 * Logarithm of Bayesian scoring criterion value for the graph in the context of represented table.
	 */
	private double score_b;
	
	/**
	 * This table stores separately Bayesian score (logarithm of it) for each vertex of the graph.
	 * Total Bayesian score represented by score_b is a sum of elements of this array.
	 */	
	private double[] val;
	
	/**
	 * Element fr[i] is the map which for every possible configuration of values of parents of variable i returns array of table.getV(i) real values corresponding to frequence of occurrence of all possible values of variable i in the table given considered configuration of values of its parents (sum of the frequencies in each list is equal to 1).
	 */
	private Map<Configuration, double[]>[] fr;

	/**
	 * Element matrix[i][j] represents in the form of object of type Edge information about ordered pair of vertices (i, j), especially about properties of possible edge between them.
	 */
	private Edge[][] matrix;
	
	/**
	 * List of all edges in the graph, each element - object of type Pair representing ordered pair of vertices (i, j), where i is less than j - means that in the graph there is present edge between vertices i and j.
	 */
	private List<Pair> e;
	
	/**
	 * Element parents[i] is the set of parents of vertex i in the graph.
	 */
	private Set<Integer>[] parents;
	
	/**
	 * Element children[i] is the set of children of vertex i in the graph.
	 */
	private Set<Integer>[] children;
	
	/**
	 * Set of all covered edges in the graph, each element - object of type Pair representing ordered pair of vertices (i, j) - means that in the graph there is present covered edge from i to j.
	 */
	private Set<Pair> cover;

	/**
	 * Array representing ancestral order of vertices in the network.
	 */
	private int[] order;

	/**
	 * Indicates if all roots of the graph created in the merging process satisfy the following sparseness condition : each vertex is not connected with at least one other vertex.
	 */
	private boolean root_sparseness;
	
	/**
	 * Empty constructor - sets every field to null or 0.
	 */
	public Graph()
	{
		table = null;
		state = 0;
		score_b = 0;
		val = null;
		fr = null;
		matrix = null;
		e = null;
		parents = null;
		children = null;
		cover = null;
		order = null;
		root_sparseness = true;
	}
	
	/**
	 * Sets table to the value specified as parameter, all remaining fields corresponding to the Bayesian network and its score sets to null or 0.
	 * @param t	New value of the table.
	 */
	public void setTable(Table t)
	{
		table = t;
		state = 0;
		score_b = 0;
		val = null;
		fr = null;
		matrix = null;
		e = null;
		parents = null;
		children = null;
		cover = null;
		order = null;
		root_sparseness = true;
	}
	
	/**
	 * Sets variable table and val to null, score_b to 0, that is what can only remain is graph.
	 */
	public void removeTable()
	{
		table = null;
		score_b = 0;
		val = null;
	}

	/**
	 * All fields corresponding to the Bayesian network and its score sets to null or 0.
	 */
	public void removeNetwork()
	{
		state = 0;
		score_b = 0;
		val = null;
		fr = null;
		matrix = null;
		e = null;
		parents = null;
		children = null;
		cover = null;
		order = null;
		root_sparseness = true;
	}
	
	/**
	 * Creates new empty Baysian network structure - that is just an empty graph, without any parameters and scores.
	 * Array order is not updated here - that is why value of state variable is set to 0.
	 * @param n	Number of vertices in the structure.
	 */
	public void emptyStructure(int n)
	{
		int i, j, k;
		
		state = 0;
		
		matrix = new Edge [n][n];
		parents = new Set [n];
		children = new Set [n];
		
		for (i = 0; i < n-1; i++) 
		{
			for (j = i+1; j < n; j++)
			{
				matrix[i][j] = new Edge(n);
				matrix[i][j].setState(0);
				matrix[i][j].setCover(0);
				for (k = 0; k < n; k++) { matrix[i][j].setV(k, 0); }
			}
		}
		for (i = 0; i < n; i++)
		{
			parents[i] = new HashSet<Integer>();
			children[i] = new HashSet<Integer>();
		}
		
		cover = new TreeSet<Pair>();
		e = new ArrayList<Pair>();
	}

	/**
	 * Inserts new edge specified in parameters to the network structure (that is just a graph without parameters and scores).
	 * Array order is not updated here.
	 * @param x	First vertex of the edge.
	 * @param y	Second vertex of the edge. This index must be bigger than x index.
	 * @param st	Direction of the inserted edge:
	 * <ul>
	 * <li>1 - from x to y,
	 * <li>2 - from y to x.
	 * </ul>
	 */
	public void addStructureEdge(int x, int y, int st)
	{
		int a, b;
		
		matrix[x][y].setT(e.size());
		matrix[x][y].setState(st);
		
		Pair p = new Pair(x, y);
		e.add(p);
		
		if (parents[x].equals(parents[y])) { matrix[x][y].setCover(1); p = new Pair(x, y); cover.add(p); }
		else { matrix[x][y].setCover(0); }
		
		if (st == 1)
		{
			children[x].add(y);
			parents[y].add(x);
		}
		else
		{
			children[y].add(x);
			parents[x].add(y);
		}
		
		if (st == 1)
		{
			for (int it : parents[y])
			{
				a = Math.min(y, it);
				b = Math.max(y, it);
				
				if (matrix[a][b].getCover() == 1) { matrix[a][b].setCover(0); p = new Pair(a, b); cover.remove(p); }
				else
				{
					parents[it].add(it);
					if (parents[it].equals(parents[y])) { matrix[a][b].setCover(1); p = new Pair(a, b); cover.add(p); }
					parents[it].remove(it);
				}
			}
			
			for (int it : children[y])
			{
				a = Math.min(y, it);
				b = Math.max(y, it);
				
				if (matrix[a][b].getCover() == 1) { matrix[a][b].setCover(0); p = new Pair(a, b); cover.remove(p); }
				else
				{
					parents[y].add(y);
					if (parents[it].equals(parents[y])) { matrix[a][b].setCover(1); p = new Pair(a, b); cover.add(p); }
					parents[y].remove(y);
				}
			}
		}
		else
		{
			for (int it : parents[x])
			{
				a = Math.min(x, it);
				b = Math.max(x, it);
				
				if (matrix[a][b].getCover() == 1) { matrix[a][b].setCover(0); p = new Pair(a, b); cover.remove(p); }
				else
				{
					parents[it].add(it);
					if (parents[it].equals(parents[x])) { matrix[a][b].setCover(1); p = new Pair(a, b); cover.add(p); }
					parents[it].remove(it);
				}
			}
			
			for (int it : children[x])
			{
				a = Math.min(x, it);
				b = Math.max(x, it);
				
				if (matrix[a][b].getCover() == 1) { matrix[a][b].setCover(0); p = new Pair(a, b); cover.remove(p); }
				else
				{
					parents[x].add(x);
					if (parents[it].equals(parents[x])) { matrix[a][b].setCover(1); p = new Pair(a, b); cover.add(p); }
					parents[x].remove(x);
				}
			}
		}
		
		if (st == 1)
		{
			for (int it : parents[x])
			{
				matrix[Math.min(y, it)][Math.max(y, it)].setV(x, 2);
			}
			for (int it : children[x])
			{
				if (it != y)
				{
					matrix[Math.min(y, it)][Math.max(y, it)].setV(x, 2);
				}
			}
			
			for (int it : parents[y])
			{
				if (it != x)
				{
					matrix[Math.min(x, it)][Math.max(x, it)].setV(y, 1);
				}
			}
			for (int it : children[y])
			{
				matrix[Math.min(x, it)][Math.max(x, it)].setV(y, 2);
			}
		}
		else
		{
			for (int it : parents[y])
			{
				matrix[Math.min(x, it)][Math.max(x, it)].setV(y, 2);
			}
			for (int it : children[y])
			{
				if (it != x)
				{
					matrix[Math.min(x, it)][Math.max(x, it)].setV(y, 2);
				}
			}
			
			for (int it : parents[x])
			{
				if (it != y)
				{
					matrix[Math.min(y, it)][Math.max(y, it)].setV(x, 1);
				}
			}
			for (int it : children[x])
			{
				matrix[Math.min(y, it)][Math.max(y, it)].setV(x, 2);
			}
		}
	}
	
	/**
	 * Runs removeTable() function, and then creates random Bayesian network, randomly generated are both structure and parameters of the network.
	 * @param n	Number of vertices in the network.
	 * @param v	Value v[i] represents number of possible values (consecutive integers starting from 0) of the i-th vertex.
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network:
	 */
	public void randomNetwork(int n, int[] v, double d)
	{
		removeTable();
		emptyStructure(n);
		
		int i, j;
		int[] u = new int [n];
		int[] perm = Generator.permutation(n);
		Configuration cfg;
		
		for (i = 1; i < n; i++)
		{
			for (j = 0; j < i; j++)
			{
				if (Generator.random() < d)
				{
					if (perm[j] < perm[i])
					{
						addStructureEdge(perm[j], perm[i], 1);
					}
					else
					{
						addStructureEdge(perm[i], perm[j], 2);
					}
				}
			}
		}
		
		order = perm;
		
		fr = new Map [n];
		for (i = 0; i < n; i++)
		{
			fr[i] = new TreeMap<Configuration, double[]>();
			cfg = new Configuration();
			cfg.zero(parents[i].size());
			
			j = 0;
			for (Integer k : parents[i])
			{
				u[j] = v[k];
				j++;
			}
			
			while (true)
			{
				fr[i].put(cfg, Generator.randomDistribution(v[i]));
				cfg = cfg.next(u);
				if (cfg == null)
				{
					break;
				}
			}
		}
		
		state = 2;
	}
	
	/**
	 * Runs removeTable() function, and then creates random Bayesian network, randomly generated are both structure and parameters of the network.
	 * @param n	Number of vertices in the network.
	 * @param v	Value v[i] represents number of possible values (consecutive integers starting from 0) of the i-th vertex.
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network.
	 * @param tr	Maximum number of elements inserted to the fr[i] map for each i, for each not inserted configuration of values of parents of vertex i we assume uniform distribution of variable i.
	 */
	public void randomNetwork(int n, int[] v, double d, int tr)
	{
		removeTable();
		emptyStructure(n);
		
		int i, j;
		int[] u = new int [n];
		int[] perm = Generator.permutation(n);
		Configuration cfg;
		
		for (i = 1; i < n; i++)
		{
			for (j = 0; j < i; j++)
			{
				if (Generator.random() < d)
				{
					if (perm[j] < perm[i])
					{
						addStructureEdge(perm[j], perm[i], 1);
					}
					else
					{
						addStructureEdge(perm[i], perm[j], 2);
					}
				}
			}
		}
		
		order = perm;
		
		fr = new Map [n];
		for (i = 0; i < n; i++)
		{
			fr[i] = new TreeMap<Configuration, double[]>();
			
			j = 0;
			for (Integer k : parents[i])
			{
				u[j] = v[k];
				j++;
			}
			
			for (j = 0; j < tr; j++)
			{
				cfg = Configuration.random(parents[i].size(), u);
				fr[i].put(cfg, Generator.randomDistribution(v[i]));
			}
		}
		
		state = 2;
	}
	
	/**
	 * Runs removeTable() function, and then creates random Bayesian network, randomly generated are both structure and parameters of the network.
	 * @param n	Number of vertices in the network.
	 * @param v	Number of possible values (consecutive integers starting from 0) of each vertex.
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network:
	 */
	public void randomNetwork(int n, int v, double d)
	{
		int[] w = new int [n];
		for (int i = 0; i < n; i++)
		{
			w[i] = v;
		}
		randomNetwork(n, w, d);
	}
	
	/**
	 * Runs removeTable() function, and then creates random Bayesian network, randomly generated are both structure and parameters of the network.
	 * @param n	Number of vertices in the network.
	 * @param v	Number of possible values (consecutive integers starting from 0) of each vertex.
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network:
	 * @param tr	Maximum number of elements inserted to the fr[i] map for each i, for each not inserted configuration of values of parents of vertex i we assume uniform distribution of variable i.
	 */
	public void randomNetwork(int n, int v, double d, int tr)
	{
		int[] w = new int [n];
		for (int i = 0; i < n; i++)
		{
			w[i] = v;
		}
		randomNetwork(n, w, d, tr);
	}
	
	/**
	 * Returns artificially  generated from Bayesian network information system.
	 * @param m	Number of independently generated objects - rows of the table.
	 * @return	The generated graph.
	 */
	public FullTable generateTable(int m)
	{
		if (state < 2)
		{
			System.out.println("Incorrect state of the Graph object for information system generation, it should be 2.");
			System.exit(1);
		}
		
		FullTable tab = new FullTable(m, order.length);
		int i, j, k;
		Configuration cfg;
		
		for (i = 0; i < order.length; i++)
		{
			tab.setV(i, fr[i].entrySet().iterator().next().getValue().length);
		}
		for (i = 0; i < m; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				k = order[j];
				cfg = new Configuration();
				for (Integer l : parents[k])
				{
					cfg.add(tab.getT(i, l));
				}
				tab.setT(i, k, Generator.random(tab.getV(k), fr[k].get(cfg)));
			}
		}
		
		return tab;
	}
	
	/**
	 * Classifies with Baysian network chosen attribute in the indicated in parameters information system on the basis of remaining attributes.
	 * It is assumed that there are no missing values in the table, and that both table and network are consistent, that is they operate on the same set of attributes.
	 * @param t	The information system.
	 * @param a	The index of attribute to classify.
	 * @return	The array of decisions.
	 */
	public int[] classifyTable(Table t, int a)
	{
		if (state < 2)
		{
			System.out.println("Incorrect state of the Graph object for classification task, it should be 2.");
			System.exit(1);
		}
		
		List<Integer> essential = new ArrayList<Integer>();
		int[] cl = new int [t.getM()];
		int i, j, tmp;
		double best_value, value;
		int best_class;
		Configuration cfg;
		
		essential.add(a);
		for (i = 0; i < order.length; i++)
		{
			if (parents[i].contains(a))
			{
				essential.add(i);
			}
		}
		
		for (i = 0; i < t.getM(); i++)
		{
			best_value = Double.NEGATIVE_INFINITY;
			best_class = 0;
			tmp = t.getT(i, a);
			for (j = 0; j < t.getV(a); j++)
			{
				t.setT(i, a, j);
				value = 0;
				for (Integer k : essential)
				{
					cfg = new Configuration();
					for (Integer l : parents[k])
					{
						cfg.add(t.getT(i, l));
					}
					if (fr[k].containsKey(cfg))
					{
						value += Math.log(fr[k].get(cfg)[t.getT(i, k)]);
					}
					else
					{
						value -= Math.log((double) t.getV(k));
					}
				}
				
				if (value > best_value)
				{
					best_value = value;
					best_class = j;
				}
			}
			t.setT(i, a, tmp);
			cl[i] = best_class;
		}
		
		return cl;
	}

	/**
	 * Calculates and returns value corresponding to val[i], for some attribute index i.
	 * In non-virtual tribe it also updates appropriately values val[i] and score_b.
	 * @param i	The attribute index.
	 * @param virt	Indicates whether the method should work in virtual tribe.
	 * @param cnt	Technical parameter. The memory should be allocated here for empty map, the method fills it appropriately, so that other methods can use it to reconstruct fr[i] map without repeating calculations.
	 * @return	The value which should currently correspond to val[i].
	 */
	public double refreshValue(int i, boolean virt, Map<Configuration, int[]> cnt)
	{
		int j, k;
		int[] v;
		Configuration cfg;
		
		for (j = 0; j < table.getM(); j++)
		{
			cfg = new Configuration();
			for (Integer it : parents[i])
			{
				cfg.add(table.getT(j, it));
			}
			if (cnt.containsKey(cfg))
			{
				cnt.get(cfg)[table.getT(j, i)]++;
				cnt.get(cfg)[table.getV(i)]++;
			}
			else
			{
				v = new int [table.getV(i) + 1];
				for (k = 0; k < v.length; k++)
				{
					v[k] = 0;
				}
				v[table.getT(j, i)] = 1;
				v[table.getV(i)] = 1;
				cnt.put(cfg, v);
			}
		}
		
		int nr, tmp;
		double d = 0;
		for (Map.Entry<Configuration, int[]> entry : cnt.entrySet())
		{
			nr = entry.getValue()[table.getV(i)];
			for (k = 0; k < table.getV(i); k++)
			{
				tmp = entry.getValue()[k];
				for (j = 1; j <= tmp; j++)
				{
					d += Math.log((double) j);
				}
			}
			for (j = table.getV(i); j <= table.getV(i) - 1 + nr; j++)
			{
				d -= Math.log((double) j);
			}
		}

		if (!virt)
		{
			score_b = score_b - val[i] + d;
			val[i] = d;
		}

		return d;
	}

	/**
	 * Creates Bayesian network using K2 algorithm for information system stored in variable table.
	 * @return	Boolean information, which is:
	 * <ul>
	 * <li>true - when obtained network satisfies condition, that each vertex is not connected with at least one other vertex,
	 * <li>false - otherwise.
	 * </ul>
	 */
	public boolean learnNetworkK2()
	{
		if (table == null)
		{
			System.out.println("Network learning fails: the information system is missing.");
			System.exit(1);
		}
		
		int i, j, best;
		double best_val, d;
		double[] v;
		int[] is = new int [table.getN()];
		order = Generator.permutation(table.getN());
		Map<Configuration, int[]> tmp;
		Map<Configuration, int[]>[] cnt = new Map [table.getN()];
		for (i = 0; i < table.getN(); i++)
		{
			cnt[i] = new TreeMap<Configuration, int[]>();
		}
		
		emptyStructure(table.getN());
		score_b = 0;
		val = new double [table.getN()];
		for (i = 0; i < table.getN(); i++)
		{
			val[i] = 0;
			refreshValue(i, false, cnt[i]);
		}
		
		for (i = 1; i < table.getN(); i++)
		{
			for (j = 0; j < i; j++)
			{
				is[j] = 0;
			}
			
			best_val = val[order[i]];
			while (true)
			{
				best = -1;
				
				for (j = 0; j < i; j++)
				{
					if (is[j] == 0)
					{
						tmp = new TreeMap<Configuration, int[]>();
						parents[order[i]].add(order[j]);
						d = refreshValue(order[i], true, tmp);
						parents[order[i]].remove(order[j]);
						if (d > best_val)
						{
							best_val = d;
							best = j;
							cnt[order[i]] = tmp;
						}
					}
				}
				
				if (best >= 0)
				{
					is[best] = 1;
					if (order[best] < order[i])
					{
						addStructureEdge(order[best], order[i], 1);
					}
					else
					{
						addStructureEdge(order[i], order[best], 2);
					}
					score_b = score_b - val[order[i]] + best_val;
					val[order[i]] = best_val;
				}
				else
				{
					break;
				}
			}
		}
		
		fr = new Map [table.getN()];
		for (i = 0; i < table.getN(); i++)
		{
			fr[i] = new TreeMap<Configuration, double[]>();
			for (Map.Entry<Configuration, int[]> entry : cnt[i].entrySet())
			{
				v = new double [table.getV(i)];
				for (j = 0; j < v.length; j++)
				{
					v[j] = ((double) entry.getValue()[j]) / ((double) entry.getValue()[table.getV(i)]);
				}
				fr[i].put(entry.getKey(), v);
			}
		}
		
		state = 2;
		
		for (i = 0; i < table.getN(); i++)
		{
			if (parents[i].size() + children[i].size() == table.getN() - 1)
			{
				root_sparseness = false;
				return false;
			}
		}
		
		root_sparseness = true;
		return true;
	}
	
	/**
	 * Creates Bayesian network using K2 algorithm for information system given as parameter, the table is also included into object via setTable method.
	 * @param t	The information system.
	 * @return	Boolean information, which is:
	 * <ul>
	 * <li>true - when obtained network satisfies condition, that each vertex is not connected with at least one other vertex,
	 * <li>false - otherwise.
	 * </ul>
	 */
	public boolean learnNetworkK2(Table t)
	{
		setTable(t);
		return learnNetworkK2();
	}
	
	/**
	 * Creates Bayesian network structure (graph without parameters) using K2 algorithm for information system stored in variable table.
	 * @return	Boolean information, which is:
	 * <ul>
	 * <li>true - when obtained structure satisfies condition, that each vertex is not connected with at least one other vertex,
	 * <li>false - otherwise.
	 * </ul>
	 */
	public boolean learnStructureK2()
	{
		if (table == null)
		{
			System.out.println("Structure learning fails: the information system is missing.");
			System.exit(1);
		}
		
		int i, j, best;
		double best_val, d;
		double[] v;
		int[] is = new int [table.getN()];
		order = Generator.permutation(table.getN());
		
		emptyStructure(table.getN());
		score_b = 0;
		val = new double [table.getN()];
		for (i = 0; i < table.getN(); i++)
		{
			val[i] = 0;
			refreshValue(i, false, new TreeMap<Configuration, int[]>());
		}
		
		for (i = 1; i < table.getN(); i++)
		{
			for (j = 0; j < i; j++)
			{
				is[j] = 0;
			}
			
			best_val = val[order[i]];
			while (true)
			{
				best = -1;
				
				for (j = 0; j < i; j++)
				{
					if (is[j] == 0)
					{
						parents[order[i]].add(order[j]);
						d = refreshValue(order[i], true, new TreeMap<Configuration, int[]>());
						parents[order[i]].remove(order[j]);
						if (d > best_val)
						{
							best_val = d;
							best = j;
						}
					}
				}
				
				if (best >= 0)
				{
					is[best] = 1;
					if (order[best] < order[i])
					{
						addStructureEdge(order[best], order[i], 1);
					}
					else
					{
						addStructureEdge(order[i], order[best], 2);
					}
					score_b = score_b - val[order[i]] + best_val;
					val[order[i]] = best_val;
				}
				else
				{
					break;
				}
			}
		}
		
		state = 1;
		
		for (i = 0; i < table.getN(); i++)
		{
			if (parents[i].size() + children[i].size() == table.getN() - 1)
			{
				root_sparseness = false;
				return false;
			}
		}
		
		root_sparseness = true;
		return true;
	}
	
	/**
	 * Creates Bayesian network structure (graph without parameters) using K2 algorithm for information system given as parameter, the table is also included into object via setTable method.
	 * @param t	The information system.
	 * @return	Boolean information, which is:
	 * <ul>
	 * <li>true - when obtained network satisfies condition, that each vertex is not connected with at least one other vertex,
	 * <li>false - otherwise.
	 * </ul>
	 */
	public boolean learnStructureK2(Table t)
	{
		setTable(t);
		return learnStructureK2();
	}
	
	
	/**
	 * For the given network structure the parameters describing conditional distributions are determined from the information system.
	 */
	public void learnParameters()
	{
		if (state < 1)
		{
			System.out.println("Parameters learning fails: the network structure is missing.");
			System.exit(1);
		}
		if (table == null)
		{
			System.out.println("Parameters learning fails: the information system is missing.");
			System.exit(1);
		}
		
		int i, j, k;
		int[] v;
		double[] u;
		Configuration cfg;
		
		Map<Configuration, int[]>[] cnt = new Map [table.getN()];
		for (i = 0; i < table.getN(); i++)
		{
			cnt[i] = new TreeMap<Configuration, int[]>();
			
			for (j = 0; j < table.getM(); j++)
			{
				cfg = new Configuration();
				for (Integer it : parents[i])
				{
					cfg.add(table.getT(j, it));
				}
				if (cnt[i].containsKey(cfg))
				{
					cnt[i].get(cfg)[table.getT(j, i)]++;
					cnt[i].get(cfg)[table.getV(i)]++;
				}
				else
				{
					v = new int [table.getV(i) + 1];
					for (k = 0; k < v.length; k++)
					{
						v[k] = 0;
					}
					v[table.getT(j, i)] = 1;
					v[table.getV(i)] = 1;
					cnt[i].put(cfg, v);
				}
			}
		}
		
		fr = new Map [table.getN()];
		for (i = 0; i < table.getN(); i++)
		{
			fr[i] = new TreeMap<Configuration, double[]>();
			for (Map.Entry<Configuration, int[]> entry : cnt[i].entrySet())
			{
				u = new double [table.getV(i)];
				for (j = 0; j < u.length; j++)
				{
					u[j] = ((double) entry.getValue()[j]) / ((double) entry.getValue()[table.getV(i)]);
				}
				fr[i].put(entry.getKey(), u);
			}
		}
		
		state = 2;
	}
	
	/**
	 * Merges in the Graph object two given as parameters Bayesian network structures based on subtables of the same table in the following way:
	 * <ul>
	 * <li>the merged structure is based on subtable - result of merging subtables corresponding to merged graphs, so it contains vertices from both graphs,
	 * <li>for each pair of common vertices of both structures the edge between them is inserted in the merged structure if and only if this edge occurs in both graphs,
	 * <li>for each remaining pair of vertices the edge is inserted if and only if it occurs in one of the graphs.
	 * </ul>
	 * @param g	First Bayesian network structure to merge.
	 * @param h	Second Bayesian network structure to merge.
	 * @return	True - when all current roots of the created graph in the merging process satisfy sparseness condition, false otherwise.
	 */
	public boolean mergeStructures(Graph g, Graph h)
	{
		if (g.state < 1 || h.state < 1)
		{
			System.out.println("Incorrect state of graph parameters in MergeStructures function: both graphs should have at least state 1.");
			System.exit(1);
		}
		if (g.table == null || h.table == null)
		{
			System.out.println("Incorrect state of graph parameters in MergeStructures function: both graphs should refer to some table.");
			System.exit(1);
		}		
		if (!(g.table instanceof SubTable) || !(h.table instanceof SubTable))
		{
			System.out.println("Incorrect graph parameters in MergeStructures function: both graphs should refer to instance of SubTable, not FullTable.");
			System.exit(1);
		}
		
		int i, x, y, sx, sy;
		
		removeTable();
		
		SubTable gs = (SubTable) g.table;
		SubTable hs = (SubTable) h.table;
		TableMergeReturn tmr = SubTable.merge(gs, hs);
		table = tmr.getSubtable();
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		Set<Integer> inters = tmr.getIntersection();
		int[] nia = tmr.getNewIndexA();
		int[] nib = tmr.getNewIndexB();
		int[] rib = tmr.getRevIndexB();
		
		emptyStructure(order.length);
		
		for (Pair p : g.e)
		{
			x = p.getX();
			y = p.getY();
			sx = gs.getS(x);
			sy = gs.getS(y);
			
			if (inters.contains(sx) && inters.contains(sy))
			{
				if (h.matrix[rib[sx]][rib[sy]].state > 0)
				{
					if (perm_pos[nia[x]] < perm_pos[nia[y]])
					{
						addStructureEdge(nia[x], nia[y], 1);
					}
					else
					{
						addStructureEdge(nia[x], nia[y], 2);
					}
				}
			}
			else
			{
				if (perm_pos[nia[x]] < perm_pos[nia[y]])
				{
					addStructureEdge(nia[x], nia[y], 1);
				}
				else
				{
					addStructureEdge(nia[x], nia[y], 2);
				}
			}
		}
		
		for (Pair p : h.e)
		{
			x = p.getX();
			y = p.getY();
			sx = hs.getS(x);
			sy = hs.getS(y);
			
			if (!(inters.contains(sx) && inters.contains(sy)))
			{
				if (perm_pos[nib[x]] < perm_pos[nib[y]])
				{
					addStructureEdge(nib[x], nib[y], 1);
				}
				else
				{
					addStructureEdge(nib[x], nib[y], 2);
				}
			}
		}
		
		state = 1;
		root_sparseness = g.root_sparseness && h.root_sparseness;
		
		return root_sparseness;
	}
	
	/**
	 * Compares given in parameters two Graph objects in the sense of consistency of graph skeletons - that is consistency of set of edges of both structures when forgetting about their direction.
	 * @param g	First Bayesian network structure to compare.
	 * @param h	Second Bayesian network structure to compare.
	 * @return	Number of common connections in both graphs divided by number of all different connections appearing in at least one graph.
	 */
	public static double compareStructuresSkeleton(Graph g, Graph h)
	{
		if (g.state < 1 || h.state < 1)
		{
			System.out.println("Incorrect state of graph parameters in compareStructuresSkeleton function: both graphs should have at least state 1.");
			System.exit(1);
		}
		if (g.order.length != h.order.length)
		{
			System.out.println("Incorrect graph parameters in compareStructuresSkeleton function: both graphs should have the same number of vertices.");
			System.exit(1);
		}
		
		int inters = 0;
		int sum = g.e.size();
		
		for (Pair p : h.e)
		{
			if (g.matrix[p.getX()][p.getY()].state == 0)
			{
				sum++;
			}
			else
			{
				inters++;
			}
		}
		
		return (double) inters / (double) sum;
	}
}


