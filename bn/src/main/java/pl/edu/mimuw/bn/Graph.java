package pl.edu.mimuw.bn;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
		
		/*
		 * Cover state of the edge between some ordered pair of nodes A and B:
		 * <ul>
		 * <li>0 - not covered,
		 * <li>1 - covered.
		 * </ul>
		private int cover;
		 */
		
		/**
		 * Position of the edge in the vector <code>e</code> in class Graph.
		 */
		private int t;
		
		/*
		 * If the object represents edge between some nodes A and B, then v[I] indicate whether what kind of v-form is present between edges A, I, and B:
		 * <ul>
		 * <li>0 - v-form not present,
		 * <li>1 - v-form present, with converging arrows,
		 * <li>2 - v-form present, but without converging arrows.
		 * </ul>
		
		private int[] v;
		 */
		
		/**
		 * Constructor, only allocates memory, for the specified in corresponding table size.
		 */
		public Edge()
		{
			 /*
		
			v = new int [table.getN()];
			 */
		
		}
		
		/**
		 * Constructor, only allocates memory, but for the specified as parameter size.
		 * @param n	Size of the array v.
		 */
		public Edge(int n)
		{
			 /*
			v = new int [n];
			 */
		}
		
		/**
		 * Constructor, copies given object.
		 * @param a	Object to be copied.
		 */
		public Edge(Edge a)
		{
			int i;
			state = a.state;
			 /*cover = a.cover;
			 */
		
			t = a.t;
			
			 /*
			v = new int [table.getN()];
			
			for (i = 0; i < table.getN(); i++) { v[i] = a.v[i]; }
			 */
		
		}
		
		/**
		 * Copies given object.
		 * @param a	Object to be copied.
		 */
		public Edge set(Edge a)
		{
			int i;
			state = a.state;
			 /*cover = a.cover;
			 */
			t = a.t;
			
			 /*
			for (i = 0; i < table.getN(); i++) { v[i] = a.v[i]; }
			 */
			
			return this;
		}

		/**
		 * Sets value of state variable.
		 * @param a	New value.
		 */
		public void setState(int a) { state = a; }
		
		/*
		 * Sets value of cover variable.
		 * @param a	New value.
		
		public void setCover(int a) { cover = a; }
		 */
		
		/**
		 * Sets value of t variable.
		 * @param a	New value.
		 */
		public void setT(int a) { t = a; }
		
		/*
		 * Sets value of v variable.
		 * @param a	New value.
		
		public void setV(int[] a) { v = a; }
		 */
		
		/*
		 * Sets value of the specified element of v array.
		 * @param i	Element index.
		 * @param a	New value.
		
		public void setV(int i, int a) { v[i] = a; }
		 */
		
		/**
		 * @return	Value of state variable.
		 */
		public int getState() { return state; }
		
		/*
		 * @return	Value of cover variable.
		
		public int getCover() { return cover; }
		 */
		
		/**
		 * @return	Value of t variable.
		 */
		public int getT() { return t; }
		
		/*
		 * @return	Value of v variable.
		
		public int[] getV() { return v; }
		 */
		
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
	
	/*
	 * Set of all covered edges in the graph, each element - object of type Pair representing ordered pair of vertices (i, j) - means that in the graph there is present covered edge from i to j.
	private Set<Pair> cover;
	 */

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
		/*
		cover = null;
		 */
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
		/*
		cover = null;
		 */
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
		/*
		cover = null;
		 */
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
				/*
				matrix[i][j].setCover(0);
				for (k = 0; k < n; k++) { matrix[i][j].setV(k, 0); }
				 */
			}
		}
		for (i = 0; i < n; i++)
		{
			parents[i] = new HashSet<Integer>();
			children[i] = new HashSet<Integer>();
		}
		
		/*
		cover = new HashSet<Pair>();
		 */
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
		
		 /*
		if (parents[x].equals(parents[y])) { matrix[x][y].setCover(1); p = new Pair(x, y); cover.add(p); }
		else { matrix[x][y].setCover(0); }
		 */
		
		
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
		
		 /*
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
		 */
		
	}
	
	/**
	 * Runs removeTable() function, and then creates random Bayesian network, randomly generated are both structure and parameters of the network.
	 * @param n	Number of vertices in the network.
	 * @param v	Value v[i] represents number of possible values (consecutive integers starting from 0) of the i-th vertex.
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network.
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
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network.
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
	 * @param d	Density of the generated network - that is probability of connection of two edges in the network.
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
	 * Runs removeTable() function, and then creates random Bayesian network, randomly generated are both structure and parameters of the network.
	 * @param n	Number of vertices in the network.
	 * @param v	Value v[i] represents number of possible values (consecutive integers starting from 0) of the i-th vertex.
	 * @param p	Approximately average number of parents of each vertex in the generated network.
	 * @param s	Most vertices in the generated network has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 */
	public void randomSparseNetwork(int n, int[] v, int p, int s)
	{
		removeTable();
		emptyStructure(n);
		
		int i, j;
		int[] sample;
		int[] u = new int [n];
		int[] perm = Generator.permutation(n);
		Configuration cfg;
		
		for (i = 1; i < n; i++)
		{
			j = Math.min(p - s + Generator.random(2*s + 1), i);
			sample = Generator.sample(j, i);
			for (int l : sample)
			{
				if (perm[l] < perm[i])
				{
					addStructureEdge(perm[l], perm[i], 1);
				}
				else
				{
					addStructureEdge(perm[i], perm[l], 2);
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
	 * @param v	Number of possible values (consecutive integers starting from 0) of each vertex.
	 * @param p	Approximately average number of parents of each vertex in the generated network.
	 * @param s	Most vertices in the generated network has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 */
	public void randomSparseNetwork(int n, int v, int p, int s)
	{
		int[] w = new int [n];
		for (int i = 0; i < n; i++)
		{
			w[i] = v;
		}
		randomSparseNetwork(n, w, p, s);
	}
	
	/**
	 * Returns size of the Bayesian network, that is number of vertices in the graph.
	 * @return	Size of the network.
	 */
	public int getSize()
	{
		return order.length;
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
	 * Classifies with local Baysian network chosen attribute in the indicated in parameters information system on the basis of remaining attributes in the network.
	 * It is assumed that there are no missing values in the table, and that both table and network are consistent, that is the network operates on the subset of attributes of the table.
	 * It is also required, that the table is an instance of FullTable.
	 * @param t	The information system.
	 * @param a	The index of attribute in the table to classify.
	 * @return	The array of decisions, or null - in the case when indicated attribute does not belong to the network.
	 */
	public int[] classifyLocallyTable(Table t, int a)
	{
		SubTable st = (SubTable) table;
		int dec = st.indexOf(a);
		
		if (dec == -1)
		{
			return null;
		}
		
		if (state < 2)
		{
			System.out.println("Incorrect state of the Graph object for classification task, it should be 2.");
			System.exit(1);
		}
		if (!(t instanceof FullTable))
		{
			System.out.println("Incorrect table parameter in classifyLocallyTable function: it should be an instance of FullTable.");
			System.exit(1);
		}
		
		List<Integer> essential = new ArrayList<Integer>();
		int[] cl = new int [t.getM()];
		int i, j, tmp;
		double best_value, value;
		int best_class;
		Configuration cfg;
		
		essential.add(dec);
		for (i = 0; i < order.length; i++)
		{
			if (parents[i].contains(dec))
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
						cfg.add(t.getT(i, st.getS(l)));
					}
					if (fr[k].containsKey(cfg))
					{
						value += Math.log(fr[k].get(cfg)[t.getT(i, st.getS(k))]);
					}
					else
					{
						value -= Math.log((double) st.getV(k));
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
	 * Classifies with local Baysian network chosen attribute in the indicated in parameters information system on the basis of remaining attributes in the network.
	 * It is assumed that there are no missing values in the table, and that both table and network are consistent, that is the network operates on the subset of attributes of the table.
	 * It is also required, that the table is an instance of FullTable.
	 * @param t	The information system.
	 * @param a	The index of attribute in the table to classify.
	 * @return	The array of weights assigned to each possible value of decision for each object - so as the result it is two-dimensional array, or null - in the case when indicated attribute does not belong to the network.
	 */
	public double[][] weightClassifyLocallyTable(Table t, int a)
	{
		SubTable st = (SubTable) table;
		int dec = st.indexOf(a);
		
		if (dec == -1)
		{
			return null;
		}
		
		if (state < 2)
		{
			System.out.println("Incorrect state of the Graph object for classification task, it should be 2.");
			System.exit(1);
		}
		if (!(t instanceof FullTable))
		{
			System.out.println("Incorrect table parameter in classifyLocallyTable function: it should be an instance of FullTable.");
			System.exit(1);
		}
		
		List<Integer> essential = new ArrayList<Integer>();
		int ncl = t.getV(a);
		double[][] cl = new double [t.getM()][ncl];
		int i, j, tmp;
		double sum_pr, log_pr;
		Configuration cfg;
		
		essential.add(dec);
		for (i = 0; i < order.length; i++)
		{
			if (parents[i].contains(dec))
			{
				essential.add(i);
			}
		}
		
		for (i = 0; i < t.getM(); i++)
		{
			tmp = t.getT(i, a);
			sum_pr = 0;
			for (j = 0; j < ncl; j++)
			{
				t.setT(i, a, j);
				log_pr = 0;
				for (Integer k : essential)
				{
					cfg = new Configuration();
					for (Integer l : parents[k])
					{
						cfg.add(t.getT(i, st.getS(l)));
					}
					if (fr[k].containsKey(cfg))
					{
						log_pr += Math.log(fr[k].get(cfg)[t.getT(i, st.getS(k))]);
					}
					else
					{
						log_pr -= Math.log((double) st.getV(k));
					}
				}
				
				cl[i][j] = log_pr;
				sum_pr += Math.exp(log_pr);
			}
			
			for (j = 0; j < ncl; j++)
			{
				cl[i][j] -= Math.log(sum_pr);
				cl[i][j] = Math.exp(cl[i][j]);
			}
			
			t.setT(i, a, tmp);
		}
		
		return cl;
	}
	
	/**
	 * Classifies with local Baysian networks chosen attribute in the indicated in parameters information system. For each object chosen decision is the most often decision returned by local classifiers.
	 * It is assumed that there are no missing values in the table, and that both table and networks are consistent, that is the networks operate on the subset of attributes of the table.
	 * It is also required, that the table is an instance of FullTable.
	 * @param g	Array of classifiers - local Bayesian networks.
	 * @param t	The information system.
	 * @param a	The index of attribute in the table to classify.
	 * @return	The array of decisions.
	 */
	static public int[] classifyTableByVoting(Graph[] g, Table t, int a)
	{
		List<int[]> vote = new ArrayList<int[]>();
		int[] cl = new int [t.getM()];
		int[] cnt = new int [t.getV(a)];
		int[] temp;
		int i, j, best_cl, best_cnt;
		
		for (Graph h : g)
		{
			temp = h.classifyLocallyTable(t, a);
			if (temp != null)
			{
				vote.add(temp);
			}
		}
		
		for (i = 0; i < t.getM(); i++)
		{
			for (j = 0; j < cnt.length; j++)
			{
				cnt[j] = 0;
			}
			
			for (j = 0; j < vote.size(); j++)
			{
				cnt[vote.get(j)[i]]++;
			}
			
			best_cnt = cnt[0];
			best_cl = 0;
			for (j = 1; j < cnt.length; j++)
			{
				if (cnt[j] > best_cnt)
				{
					best_cnt = cnt[j];
					best_cl = j;
				}
			}
			
			cl[i] = best_cl;
		}
		
		return cl;
	}
	
	/**
	 * Classifies with local Baysian networks chosen attribute in the indicated in parameters information system. For each object chosen decision is this decision which obtains the biggest sum of weights assigned to it from all appropriate (that is containing this attribute) local classifiers.
	 * It is assumed that there are no missing values in the table, and that both table and networks are consistent, that is the networks operate on the subset of attributes of the table.
	 * It is also required, that the table is an instance of FullTable.
	 * @param g	Array of classifiers - local Bayesian networks.
	 * @param t	The information system.
	 * @param a	The index of attribute in the table to classify.
	 * @return	The array of decisions.
	 */
	static public int[] classifyTableByWeightedVoting(Graph[] g, Table t, int a)
	{
		int ncl = t.getV(a);
		int nobj = t.getM();
		double[][] sum_w = new double [nobj][ncl];
		int[] cl = new int [nobj];
		double[][] temp;
		double best_w;
		int i, j, best_cl;
		
		for (i = 0; i < nobj; i++)
		{
			for (j = 0; j < ncl; j++)
			{
				sum_w[i][j] = 0;
			}
		}
		
		for (Graph h : g)
		{
			temp = h.weightClassifyLocallyTable(t, a);
			if (temp != null)
			{
				for (i = 0; i < nobj; i++)
				{
					for (j = 0; j < ncl; j++)
					{
						sum_w[i][j] += temp[i][j];
					}
				}
			}
		}
		
		for (i = 0; i < nobj; i++)
		{
			best_w = sum_w[i][0];
			best_cl = 0;
			for (j = 1; j < ncl; j++)
			{
				if (sum_w[i][j] > best_w)
				{
					best_w = sum_w[i][j];
					best_cl = j;
				}
			}
			
			cl[i] = best_cl;
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
	 * <li>for each remaining pair of vertices included in at lest one graph the edge is inserted if and only if it occurs in one of the graphs,
	 * <li>each other pair of vertices is inserted.
	 * </ul>
	 * Ancestral order in the result graph is chosen randomly.
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
		Set<Integer> oia = tmr.getOutIndexA();
		Set<Integer> oib = tmr.getOutIndexB();
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
		
		for (Integer k : oia)
		{
			for (Integer l : oib)
			{
				x = Math.min(nia[k], nib[l]);
				y = Math.max(nia[k], nib[l]);
				
				if (perm_pos[x] < perm_pos[y])
				{
					addStructureEdge(x, y, 1);
				}
				else
				{
					addStructureEdge(x, y, 2);
				}
			}
		}
		
		state = 1;
		root_sparseness = g.root_sparseness && h.root_sparseness;
		
		return root_sparseness;
	}
	
	/**
	 * TEST ONLY
	 * Merges in the Graph object given as parameter group of Bayesian network structures based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics) graphs in the group,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly.
	 * @param ref	Real Bayesian network for TEST PURPOSE.
	 * @param gl	List of Bayesian network structures to merge.
	 */
	 /*
	 * @param hl	List of Bayesian network structures of size lev - for purpose of comparison.
	 * @param lev	' Size (number of vertices ) of Baysian networks to merge ' + 1.
	 */
	public void TESTmergeStructures(Graph ref, Graph[] gl /*, Graph[] hl, int lev */)
	{
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		 /*
		for (Graph h : hl)
		{
			if (h.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (h.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(h.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		 */
	
		int i, j, k, x, y;
		
		removeTable();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		
		 /*
		SubTable[] hst = new SubTable [hl.length];
		for (i = 0; i < hst.length; i++)
		{
			hst[i] = (SubTable) hl[i].table;
		}
		
		int[][] rih = new int [hl.length][order.length];
		for (i = 0; i < hl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				rih[i][j] = -1;
			}
			
			for (j = 0; j < hst[i].getN(); j++)
			{
				rih[i][hst[i].getS(j)] = j;
			}
		}
		 */
	
		
		state = 1;
		
		double sim, best_sim = 0;
		int best_ind = 0, ind = 0, tresh = 4*eol.size() / order.length;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			sim = compareStructuresSkeleton(this, ref);
			if (sim >= best_sim)
			{
				best_sim = sim;
				best_ind = ind;
			}
			if (ind == tresh)
			{
				break;
			}
			ind++;
		}
		
		emptyStructure(order.length);
		ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		state = 1;
	}
	
	/**
	 * TEST ONLY
	 * Merges in the Graph object given as parameter group of Bayesian network structures of size lev-1 based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics compared with analogical statistics for graphs of size one bigger) graphs in the group,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly. Some special data are also produced - with the aim of visualization of the statistics.
	 * @param ref	Real Bayesian network for TEST PURPOSE.
	 * @param gl	List of Bayesian network structures of size lev-1 to merge.
	 * @param hl	List of Bayesian network structures of size lev - for purpose of comparison.
	 * @param file	The file to which the special statistics are written.
	 * @return	Time spent on calculating occurrence statistics and ordering them.
	 */
	 
	 /* @param lev	' Size (number of vertices ) of Baysian networks to merge ' + 1.
	
	 */
	public long TESTmergeStructuresWithVisualization(Graph ref, Graph[] gl, Graph[] hl, /* int lev, */ String file)
	{
		long start, time = 0;
		
		try
		{
		String nl = System.getProperty("line.separator");
		FileWriter fwr = new FileWriter(file, true);
		PrintWriter pwr = new PrintWriter(fwr, true);
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		 /*
		for (Graph h : hl)
		{
			if (h.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (h.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(h.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		 */
		int i, j, k, x, y;
		
		removeTable();
		
		start = System.nanoTime();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		time = System.nanoTime() - start;
		
		SubTable[] hst = new SubTable [hl.length];
		for (i = 0; i < hst.length; i++)
		{
			hst[i] = (SubTable) hl[i].table;
		}
		
		int[][] rih = new int [hl.length][order.length];
		for (i = 0; i < hl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				rih[i][j] = -1;
			}
			
			for (j = 0; j < hst[i].getN(); j++)
			{
				rih[i][hst[i].getS(j)] = j;
			}
		}
		
		state = 1;
		
		for (EdgeOccurrence eo : eol)
		{
			 /*System.out.print(eo.getOccurrence() + " ");
			 */
			pwr.print(eo.getOccurrence() + " ");
		}
		 /*System.out.println();
		 */
		pwr.print(nl);
		
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();
			
			if (ref.matrix[i][j].state != 0)
			{
				 /*System.out.println("1");*/
				pwr.print("1 ");
			}
			else
			{
				 /*System.out.println("0");*/
				pwr.print("0 ");
			}
		}
		
		 /*System.out.println();
		 */
		pwr.print(nl);
		
		int cnt;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();
			cnt = 0;
			
			for (k = 0; k < hl.length; k++)
			{
				x = rih[k][i];
				y = rih[k][j];
				
				if (x != -1 && y != -1)
				{
					if (hl[k].matrix[x][y].state == 0)
					{
						cnt++;
					}
				}
			}
			
			pwr.print(cnt + " ");
		}
		
		pwr.print(nl);
		pwr.print(nl);
		double sim, best_sim = 0;
		int best_ind = 0, ind = 0, tresh = 4*eol.size() / order.length;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();
			
			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			sim = compareStructuresSkeleton(this, ref);
			if (sim >= best_sim)
			{
				best_sim = sim;
				best_ind = ind;
			}
			if (ind == tresh)
			{
				break;
			}
			ind++;
		}
		
		emptyStructure(order.length);
		ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		state = 1;
		
		pwr.close();
		fwr.close();
		}
		catch (IOException exc)
		{
			System.exit(1);
		}
		
		return time;
	}
	
	/**
	 * TEST ONLY
	 * Merges in the Graph object given as parameter group of Bayesian network structures of size lev-1 based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics compared with analogical statistics for graphs of size one bigger) graphs in the group,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly. Some special data are also produced - with the aim of visualization of the statistics.
	 * @param ref	Real Bayesian network for TEST PURPOSE.
	 * @param gl	List of Bayesian network structures of size lev-1 to merge.
	 * @return	Time spent on calculating occurrence statistics and ordering them.
	 */
	 
	 /* @param lev	' Size (number of vertices ) of Baysian networks to merge ' + 1.
	
	 */
	public long TESTmergeStructuresAveragePerformance(Graph ref, Graph[] gl)
	{
		long start, time;
		
		
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		int i, j, k, x, y, lev = gl[0].order.length;
		
		removeTable();
		
		start = System.nanoTime();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		Map<Pair, Integer> cnt = new HashMap<Pair, Integer>();
		Pair p;
		Integer in;
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < lev - 1; j++)
			{
				for (k = j+1; k < lev; k++)
				{
					if (gl[i].matrix[j][k].state == 0)
					{
						x = st[i].getS(j);
						y = st[i].getS(k);
						p = new Pair(x, y);
						in = cnt.get(p);
						if (in == null)
						{
							cnt.put(p, new Integer(1));
						}
						else
						{
							cnt.put(p, new Integer(in+1));
						}
					}
				}
			}
		}

		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				p = new Pair(i, j);
				in = cnt.get(p);
				if (in != null)
				{
					eo.setOccurrence(in);
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		time = System.nanoTime() - start;
		
		emptyStructure(order.length);
		state = 1;
		
		double sim, best_sim = 0;
		int best_ind = 0, ind = 0, tresh = Integer.MAX_VALUE/*4*eol.size() / order.length*/;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();
			
			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			sim = (compareInclusionOfStructuresSkeleton(this, ref) + compareInclusionOfStructuresSkeleton(ref, this)) / 2;
			if (sim >= best_sim)
			{
				best_sim = sim;
				best_ind = ind;
			}
			if (ind == tresh)
			{
				break;
			}
			ind++;
		}
		
		emptyStructure(order.length);
		ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		state = 1;
		
		
		return time;
	}
	
	public long TESTmergeStructuresLeftPerformance(Graph ref, Graph[] gl)
	{
		long start, time;
		
		
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		int i, j, k, x, y;
		
		removeTable();
		
		start = System.nanoTime();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		time = System.nanoTime() - start;
		
		state = 1;
		
		double sim, best_sim = 0;
		int best_ind = 0, ind = 0, tresh = Integer.MAX_VALUE/*4*eol.size() / order.length*/;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();
			
			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			sim = compareInclusionOfStructuresSkeleton(this, ref);
			if (sim >= best_sim)
			{
				best_sim = sim;
				best_ind = ind;
			}
			if (ind == tresh)
			{
				break;
			}
			ind++;
		}
		
		emptyStructure(order.length);
		ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		state = 1;
		
		
		return time;
	}
	
	public long TESTmergeStructuresRightPerformance(Graph ref, Graph[] gl)
	{
		long start, time;
		
		
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		int i, j, k, x, y;
		
		removeTable();
		
		start = System.nanoTime();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		time = System.nanoTime() - start;
		
		state = 1;
		
		double sim, best_sim = 0;
		int best_ind = 0, ind = 0, tresh = Integer.MAX_VALUE/*4*eol.size() / order.length*/;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();
			
			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			sim = compareInclusionOfStructuresSkeleton(ref, this);
			if (sim >= best_sim)
			{
				best_sim = sim;
				best_ind = ind;
			}
			if (ind == tresh)
			{
				break;
			}
			ind++;
		}
		
		emptyStructure(order.length);
		ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		state = 1;
		
		
		return time;
	}
	
	/**
	 * TEST ONLY
	 * Merges in the Graph object given as parameter group of Bayesian network structures of size lev-1 based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics compared with analogical statistics for graphs of size one bigger) graphs in the group,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly.
	 * @param ref	Real Bayesian network for TEST PURPOSE.
	 * @param gl	List of Bayesian network structures of size lev-1 to merge.
	 * @param hl	List of Bayesian network structures of size lev - for purpose of comparison.
	 * @param lev	' Size (number of vertices ) of Baysian networks to merge ' + 1.
	 */
	public void TESTMmergeStructures(Graph ref, Graph[] gl, Graph[] hl, int lev)
	{
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		for (Graph h : hl)
		{
			if (h.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (h.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(h.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		int i, j, k, x, y;
		
		removeTable();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		
		SubTable[] hst = new SubTable [hl.length];
		for (i = 0; i < hst.length; i++)
		{
			hst[i] = (SubTable) hl[i].table;
		}
		
		int[][] rih = new int [hl.length][order.length];
		for (i = 0; i < hl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				rih[i][j] = -1;
			}
			
			for (j = 0; j < hst[i].getN(); j++)
			{
				rih[i][hst[i].getS(j)] = j;
			}
		}
		
		state = 1;
		
		double sim, best_sim = 0;
		int best_ind = 0, ind = 0, tresh = 4*eol.size() / order.length;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			sim = compareStructuresSkeleton(this, ref);
			if (sim >= best_sim)
			{
				best_sim = sim;
				best_ind = ind;
			}
			if (ind == tresh)
			{
				break;
			}
			ind++;
		}
		
		emptyStructure(order.length);
		ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		state = 1;
	}
	
	/**
	 * Merges in the Graph object given as parameter group of Bayesian network structures based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly.
	 * @param gl	List of Bayesian network structures to merge.
	 */
	 /*
	 * @param hl	List of Bayesian network structures of size lev+1 - for purpose of comparison.
	 * @param lev	' Size (number of vertices ) of Baysian networks to merge ' + 1.
		  */
	public void mergeStructuresSlow(Graph[] gl  /*,Graph[] hl, int lev*/)
	{
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		 /*
		for (Graph h : hl)
		{
			if (h.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (h.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(h.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		 */
		
		
		int i, j, k, x, y;
		
		removeTable();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		

		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		

		
		for (EdgeOccurrence eo : eol)
		{
			if (eo.getOccurrence() == 0)
			{
				i = eo.getX();
				j = eo.getY();
				
				if (perm_pos[i] < perm_pos[j])
				{
					addStructureEdge(i, j, 1);
				}
				else
				{
					addStructureEdge(i, j, 2);
				}
			}
		}
		
		state = 1;
	}

	/**
	 * Merges in the Graph object given as parameter group of Bayesian network structures based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly.
	 * @param gl	List of Bayesian network structures to merge.
	 */
	public void mergeStructures(Graph[] gl)
	{
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}

		int i, j, k, x, y, lev = gl[0].order.length;

		removeTable();

		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}

		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}

		Map<Pair, Integer> cnt = new HashMap<Pair, Integer>();
		Pair p;
		Integer in;
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < lev - 1; j++)
			{
				for (k = j+1; k < lev; k++)
				{
					if (gl[i].matrix[j][k].state == 0)
					{
						x = st[i].getS(j);
						y = st[i].getS(k);
						p = new Pair(x, y);
						in = cnt.get(p);
						if (in == null)
						{
							cnt.put(p, new Integer(1));
						}
						else
						{
							cnt.put(p, new Integer(in+1));
						}
					}
				}
			}
		}

		emptyStructure(order.length);

		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				p = new Pair(i, j);

				if (!cnt.containsKey(p))
				{
					if (perm_pos[i] < perm_pos[j])
					{
						addStructureEdge(i, j, 1);
					}
					else
					{
						addStructureEdge(i, j, 2);
					}
				}
			}
		}

		state = 1;
	}
	
	/**
	 * Merges in the Graph object given as parameter group of Bayesian network structures based on subtables covering all attributes of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly. The function purpose are dense dependencies 
	 * @param gl	List of Bayesian network structures to merge.
	 */
	 /*
	 * @param hl	List of Bayesian network structures of size lev+1 - for purpose of comparison.
	 * @param lev	' Size (number of vertices ) of Baysian networks to merge ' + 1.
		  */
	public void mergeDenseStructures(Graph[] gl  /*,Graph[] hl, int lev*/)
	{
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		 /*
		for (Graph h : hl)
		{
			if (h.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (h.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(h.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		 */
		
		
		int i, j, k, x, y;
		
		removeTable();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][i];
					y = ri[k][j];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		
		Collections.sort(eol);
		
		
		
		 /*
		SubTable[] hst = new SubTable [hl.length];
		for (i = 0; i < hst.length; i++)
		{
			hst[i] = (SubTable) hl[i].table;
		}
		
		int[][] rih = new int [hl.length][order.length];
		for (i = 0; i < hl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				rih[i][j] = -1;
			}
			
			for (j = 0; j < hst[i].getN(); j++)
			{
				rih[i][hst[i].getS(j)] = j;
			}
		}
		 */
		
		
		
		int hole, best_hole = 0;
		int best_ind = 0;
		for (i = 0; i < eol.size() - 1; i++)
		{
			hole = eol.get(i+1).getOccurrence() - eol.get(i).getOccurrence();
			if (hole > best_hole)
			{
				best_hole = hole;
				best_ind = i;
			}
		}
		
		emptyStructure(order.length);
		int ind = 0;
		for (EdgeOccurrence eo : eol)
		{
			i = eo.getX();
			j = eo.getY();

			if (perm_pos[i] < perm_pos[j])
			{
				addStructureEdge(i, j, 1);
			}
			else
			{
				addStructureEdge(i, j, 2);
			}
			
			if (ind == best_ind)
			{
				break;
			}
			ind++;
		}
		
		
		 /*
		
		for (EdgeOccurrence eo : eol)
		{
			if (eo.getOccurrence() == 0)
			{
				i = eo.getX();
				j = eo.getY();
				
				if (perm_pos[i] < perm_pos[j])
				{
					addStructureEdge(i, j, 1);
				}
				else
				{
					addStructureEdge(i, j, 2);
				}
			}
		}
		 */
		
		state = 1;
	}
	
	/**
	 * Merges in the Graph object given as parameter group of Bayesian network structures based on subtables of the same table in the following way:
	 * <ul>
	 * <li>the edge is inserted between each pair of vertices such that it is connected in all or almost all (rough treshold calculated on the basis of obtained statistics) graphs in the group,
	 * <li>all remaining pairs of vertices are not connected in the result graph.
	 * </ul>
	 * Ancestral order in the graph is chosen randomly.
	 * @param gl	List of Bayesian network structures to merge.
	 * @param max_k	Rough treshold is calculated via k-means clustering repeated for each k between 2 and max_k.
	 * @param rep	For each value of k the k-means clustering is repeated rep times.
	 */
	public void mergeStructures(Graph[] gl, int max_k, int rep)
	{
		for (Graph g : gl)
		{
			if (g.state < 1)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should have at least state 1.");
				System.exit(1);
			}
			if (g.table == null)
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to some table.");
				System.exit(1);
			}
			if (!(g.table instanceof SubTable))
			{
				System.out.println("Incorrect state of input graphs in MergeStructures function: graphs should refer to instance of SubTable, not FullTable.");
				System.exit(1);
			}
		}
		
		int i, j, k, x, y;
		
		removeTable();
		
		SubTable[] st = new SubTable [gl.length];
		for (i = 0; i < st.length; i++)
		{
			st[i] = (SubTable) gl[i].table;
		}
		
		SubTable c = SubTable.merge(st);
		table = c;
		order = Generator.permutation(table.getN());
		int[] perm_pos = new int [order.length];
		for (i = 0; i < order.length; i++)
		{
			perm_pos[order[i]] = i;
		}
		
		int[][] ri = new int [gl.length][order.length];
		for (i = 0; i < gl.length; i++)
		{
			for (j = 0; j < order.length; j++)
			{
				ri[i][j] = -1;
			}
			
			for (j = 0; j < st[i].getN(); j++)
			{
				ri[i][st[i].getS(j)] = j;
			}
		}
		
		emptyStructure(order.length);
		
		List<EdgeOccurrence> eol = new ArrayList<EdgeOccurrence>();
		for (i = 0; i < order.length - 1; i++)
		{
			for (j = i + 1; j < order.length; j++)
			{
				EdgeOccurrence eo = new EdgeOccurrence(i, j);
				
				for (k = 0; k < gl.length; k++)
				{
					x = ri[k][c.getS(i)];
					y = ri[k][c.getS(j)];
					
					if (x != -1 && y != -1)
					{
						if (gl[k].matrix[x][y].state == 0)
						{
							eo.next();
						}
					}
				}
				
				eol.add(eo);
			}
		}
		Collections.sort(eol);
		
		Map<Pair, Integer> clust_rank = new HashMap<Pair, Integer>();
		max_k = Math.max(2, max_k);
		double[] occur = new double [eol.size()];
		Pair[] cluster;
		
		for (i = 0; i < occur.length; i++)
		{
			occur[i] = eol.get(i).getOccurrence();
		}
		
		for (k = 2; k <= max_k; k++)
		{
			for (i = 0; i < rep; i++)
			{
				cluster = Generator.kMeans(occur, k, 1000);
				
				if (clust_rank.containsKey(cluster[0]))
				{
					clust_rank.put(cluster[0], clust_rank.get(cluster[0]) + 1);
				}
				else
				{
					clust_rank.put(cluster[0], 1);
				}
			}
		}
		
		int best_rank = 0;
		Pair best_clust = new Pair(0, 0);
		
		for (Map.Entry<Pair, Integer> entry : clust_rank.entrySet())
		{
			i = entry.getValue();
			
			if (i > best_rank)
			{
				best_rank = i;
				best_clust = entry.getKey();
			}
		}
		
		for (i = 0; i <= best_clust.getY(); i++)
		{
			x = eol.get(i).getX();
			y = eol.get(i).getY();
			
			if (perm_pos[x] < perm_pos[y])
			{
				addStructureEdge(x, y, 1);
			}
			else
			{
				addStructureEdge(x, y, 2);
			}
		}
		
		state = 1;
	}
	
	/**
	 * The function returns array of local Baysian networks learned on the indicated table with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition.
	 * @param t	The information system on the basis of which we learn local Bayesian networks.
	 * @param n	Number of local networks created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and so these graphs can be returned.
	 * @param max_s	Maximal considered size of local structures.
	 * @return	Array of chosen local Bayesian networks.
	 */
	public static Graph[] learnLocalNetworks(Table t, int n, double max_w, int max_s)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		Table s;
		Graph g;
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnNetworkK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		
		return ln;
	}
	
	/**
	 * The function returns array of local Baysian networks learned on the indicated table with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. All generated networks contains one common indicated in parameters attribute.
	 * @param t	The information system on the basis of which we learn local Bayesian networks.
	 * @param n	Number of local networks created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and so these graphs can be returned.
	 * @param max_s	Maximal considered size of local structures.
	 * @param a	Index of common for all generated networks attribute.
	 * @return	Array of chosen local Bayesian networks.
	 */
	public static Graph[] learnLocalNetworks(Table t, int n, double max_w, int max_s, int a)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		Table s;
		Graph g;
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, a);
				g = new Graph();
				if (!g.learnNetworkK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		
		return ln;
	}
	
	/**
	 * TEST ONLY
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size.
	 * @param t	The information system.
	 * @param n	Number of local structures created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @param ref	Real Baysian network for given information system - TEST PURPOSES.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int TESTlearnStructureLocally(Table t, int n, double max_w, int max_s, Graph ref)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		 /*Graph[] ln_next;
		 */
		boolean sparseness;
		Table s;
		Graph g;
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		 /*
		k++;
		ln_next = new Graph [n];
		for (i = 0; i < n; i++)
		{
			s = new SubTable(t, k, i%t.getN());
			g = new Graph();
			g.learnStructureK2(s);
			
			ln_next[i] = g;
		}
		 */

		TESTmergeStructures(ref, ln /*, ln_next, k */);
		
		return k /*- 1 */;
	}
	
	/**
	 * TEST ONLY
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger. Some special data are also produced - with the aim of visualization of the statistics.
	 * @param t	The information system.
	 * @param n	Number of local structures created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @param ref	Real Baysian network for given information system - TEST PURPOSES.
	 * @param file	The file to which the special statistics are written.
	 * @param time	On this memory function saves total time spent on learning statistics of not-connected edge occurrence in generated local structures.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int TESTlearnStructureLocallyWithVisualization(Table t, int n, double max_w, int max_s, Graph ref, String file, long[] time)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		long local_time, merge_time, start;
		Graph[] ln_next;
		
		boolean sparseness;
		Table s;
		Graph g;
		
		start = System.nanoTime();
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		
		local_time = (System.nanoTime() - start);
		
		k++;
		ln_next = new Graph [n];
		for (i = 0; i < n; i++)
		{
			s = new SubTable(t, k, i%t.getN());
			g = new Graph();
			g.learnStructureK2(s);
			
			ln_next[i] = g;
		}
		

		merge_time = TESTmergeStructuresWithVisualization(ref, ln, ln_next /*, k */, file);
		
		time[0] = local_time + merge_time;
		
		return k - 1;
	}
	
	/**
	 * TEST ONLY
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger.
	 * @param t	The information system.
	 * @param n	Number of local structures created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @param ref	Real Baysian network for given information system - TEST PURPOSES.
	 * @param time	On this memory function saves total time spent on learning statistics of not-connected edge occurrence in generated local structures.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int TESTlearnStructureLocallyAveragePerformance(Table t, int n, double max_w, int max_s, Graph ref, long[] time)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		long local_time, merge_time, start;
		
		boolean sparseness;
		Table s;
		Graph g;
		
		start = System.nanoTime();
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		
		local_time = (System.nanoTime() - start);
		
		merge_time = TESTmergeStructuresAveragePerformance(ref, ln);
		
		time[0] = local_time + merge_time;
		
		return k;
	}
	
	/**
	 * TEST ONLY
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger.
	 * @param t	The information system.
	 * @param n	Number of local structures created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @param ref	Real Baysian network for given information system - TEST PURPOSES.
	 * @param time	On this memory function saves total time spent on learning statistics of not-connected edge occurrence in generated local structures.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int TESTlearnStructureLocallyLeftPerformance(Table t, int n, double max_w, int max_s, Graph ref, long[] time)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		long local_time, merge_time, start;
		
		boolean sparseness;
		Table s;
		Graph g;
		
		start = System.nanoTime();
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		
		local_time = (System.nanoTime() - start);
		
		merge_time = TESTmergeStructuresLeftPerformance(ref, ln);
		
		time[0] = local_time + merge_time;
		
		return k;
	}
	
	/**
	 * TEST ONLY
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger.
	 * @param t	The information system.
	 * @param n	Number of local structures created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @param ref	Real Baysian network for given information system - TEST PURPOSES.
	 * @param time	On this memory function saves total time spent on learning statistics of not-connected edge occurrence in generated local structures.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int TESTlearnStructureLocallyRightPerformance(Table t, int n, double max_w, int max_s, Graph ref, long[] time)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		long local_time, merge_time, start;
		
		boolean sparseness;
		Table s;
		Graph g;
		
		start = System.nanoTime();
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		
		local_time = (System.nanoTime() - start);
		
		merge_time = TESTmergeStructuresRightPerformance(ref, ln);
		
		time[0] = local_time + merge_time;
		
		return k;
	}
	
	/**
	 * TEST ONLY
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger.
	 * @param t	The information system.
	 * @param n	Number of local structures created for each size of subsets.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @param ref	Real Baysian network for given information system - TEST PURPOSES.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int TESTMlearnStructureLocally(Table t, int n, double max_w, int max_s, Graph ref)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		Graph[] ln_next;
		boolean sparseness;
		Table s;
		Graph g;
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		k++;
		ln_next = new Graph [n];
		for (i = 0; i < n; i++)
		{
			s = new SubTable(t, k, i%t.getN());
			g = new Graph();
			g.learnStructureK2(s);
			
			ln_next[i] = g;
		}

		TESTMmergeStructures(ref, ln, ln_next, k);
		
		return k - 1;
	}
	
	/**
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger.
	 * @param t	The information system.
	 * @param n	Number of local structures created for size of subsets equal 2. For greater sizes of subsets number of generated local structures is appropriately decreasing so that for each size number of included pairs of vertices in all created subsets is approximately constant.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int learnStructureLocally(Table t, int n, double max_w, int max_s)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		Graph[] ln_next;
		boolean sparseness;
		Table s;
		Graph g;
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		 /*
		k++;
		ln_next = new Graph [n];
		for (i = 0; i < n; i++)
		{
			s = new SubTable(t, k, i%t.getN());
			g = new Graph();
			g.learnStructureK2(s);
			
			ln_next[i] = g;
		}
		 */

		mergeStructures(ln /*, ln_next, k*/);
		
		return k /* - 1 */;
	}
	
	/**
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition. Merging process uses information about graphs of chosen size and graphs of size one bigger. The purpose of this function are dense dependencies.
	 * @param t	The information system.
	 * @param n	Number of local structures created for size of subsets equal 2. For greater sizes of subsets number of generated local structures is appropriately decreasing so that for each size number of included pairs of vertices in all created subsets is approximately constant.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_s	Maximal considered size of local structures.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int learnDenseStructureLocally(Table t, int n, double max_w, int max_s)
	{
		int i, k, lim, cnt;
		Graph[] ln;
		Graph[] ln_next;
		boolean sparseness;
		Table s;
		Graph g;
		
		ln = new Graph [n];
		lim = (int)(max_w*n);
		
		for (k = 2; ; k++)
		{
			cnt = 0;
			for (i = 0; i < n; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < max_s && cnt > lim)
					{
						break;
					}
				}
				ln[i] = g;
			}
			if (cnt <= lim)
			{
				break;
			}
			if (k == max_s)
			{
				break;
			}
		}
		 /*
		k++;
		ln_next = new Graph [n];
		for (i = 0; i < n; i++)
		{
			s = new SubTable(t, k, i%t.getN());
			g = new Graph();
			g.learnStructureK2(s);
			
			ln_next[i] = g;
		}
		 */

		mergeDenseStructures(ln /*, ln_next, k*/);
		
		return k /* - 1 */;
	}
	
	/**
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. Size of the subsets is chosen as such minimal size that almost all networks learned on them satisfy sparseness condition.
	 * @param t	The information system.
	 * @param n	Number of local structures created for size of subsets equal 2. For greater sizes of subsets number of generated local structures is appropriately decreasing so that for each size number of included pairs of vertices in all created subsets is approximately constant.
	 * @param max_w	Maximal percentage of not sparse graphs for considered size which can be ignored - that is procedure assumes that sparseness condition for almost all graphs holds and merging can be performed.
	 * @param max_k	Parameter of mergeStructures function.
	 * @param rep	Parameter of mergeStructures function.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int learnStructureLocally(Table t, int n, double max_w, int max_k, int rep)
	{
		int i, k, m, lim, cnt;
		List<Graph> sn = new ArrayList<Graph>();
		Graph[] ln;
		boolean sparseness;
		Table s;
		Graph g;
		
		for (k = 2; k <= t.getN(); k++)
		{
			m = (2*n) / (k*(k - 1));
			lim = (int)(max_w*m);
			sn.clear();
			cnt = 0;
			for (i = 0; i < m; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					cnt++;
					if (k < t.getN() && cnt > lim)
					{
						break;
					}
				}
				sn.add(g);
			}
			if (cnt <= lim)
			{
				break;
			}
		}

		ln = new Graph [sn.size()];
		i = 0;
		for (Graph h : sn)
		{
			ln[i] = h;
			i++;
		}
		mergeStructures(ln, max_k, rep);
		
		return Math.min(k, t.getN());
	}
	
	/**
	 * Creates Bayesian network structure (graph without parameters) for given as parameter information system induced from many local structures learned with K2 algorithm on random subsets of attributes. The final network is obtained as the result of merging all obtained local graphs of biggest considered size. The process of generating local networks for the next size is stopped when one of the following conditions is true:
	 * <ul>
	 * <li>declared as parameter max_s size limit of the graphs has been reached,
	 * <li>all obtained graphs of last considered size satisfy sparseness condition,
	 * <li>percentage of new discovered pairs of not connected vertices in local graphs of last considered size is lower than specified as parameter min_p limit.
	 * </ul>
	 * @param t	The information system.
	 * @param n	Number of local structures created for size of subsets equal 2. For greater sizes of subsets number of generated local structures is appropriately decreasing so that for each size number of included pairs of vertices in all created subsets is approximately constant.
	 * @param max_s	See description of method above.
	 * @param min_p	Real value between 0 and 1, see description of method above.
	 * @param max_k	Parameter of mergeStructures function.
	 * @param rep	Parameter of mergeStructures function.
	 * @return	Size of random subsets of attributes used to learn local structures.
	 */
	public int learnStructureLocally(Table t, int n, int max_s, double min_p, int max_k, int rep)
	{
		int i, j, k, l, m, prev_len;
		List<Graph> sn = new ArrayList<Graph>();
		Set<Pair> pairs = new HashSet<Pair>();
		Graph[] ln;
		Graph g;
		SubTable s;
		Pair p;
		boolean sparseness;
		
		for (k = 2; k <= max_s; k++)
		{
			m = (2*n) / (k*(k - 1));
			sn.clear();
			sparseness = true;
			prev_len = pairs.size();
			for (i = 0; i < m; i++)
			{
				s = new SubTable(t, k, i%t.getN());
				g = new Graph();
				if (!g.learnStructureK2(s))
				{
					sparseness = false;
				}
				for (j = 0; j < k - 1; j++)
				{
					for (l = j + 1; l < k; l++)
					{
						if (g.matrix[j][l].state == 0)
						{
							p = new Pair(s.getS(j), s.getS(l));
							pairs.add(p);
						}
					}
				}
				sn.add(g);
			}
			if (sparseness)
			{
				break;
			}
			if (prev_len > 0)
			{
				if ((double) (pairs.size() - prev_len) / (double) pairs.size() < min_p)
				{
					break;
				}
			}
		}
		
		ln = new Graph [sn.size()];
		i = 0;
		for (Graph h : sn)
		{
			ln[i] = h;
			i++;
		}
		mergeStructures(ln, max_k, rep);
		
		return k;
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
	
	/**
	 * Compares given in parameters two Graph objects in the sense of consistency of graph skeletons - that is consistency of set of edges of both structures when forgetting about their direction. In this case the consistency is with respect to inclusion relation of graph g in graph h.
	 * @param g	First Bayesian network structure to compare.
	 * @param h	Second Bayesian network structure to compare.
	 * @return	Number of common connections in both graphs divided by number of all different connections appearing in g.
	 */
	public static double compareInclusionOfStructuresSkeleton(Graph g, Graph h)
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
		
		for (Pair p : g.e)
		{
			if (h.matrix[p.getX()][p.getY()].state != 0)
			{
				inters++;
			}
		}
		
		return (double) inters / (double) g.e.size();
	}
}


