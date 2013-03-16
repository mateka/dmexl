package pl.edu.mimuw.bn;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Main class demonstrating this package.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class Bn
{
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function learnStructureLocally - old version). The comparison is with respect to the real network generating artificial data.
	 * @param file	File to which write results of comparison.
	 * @param min_m	Logarithm of minimal considered number of rows in table.
	 * @param max_m	Logarithm of maximal considered number of rows in table.
	 * @param n	Number of columns in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param d	Density of Baysian network used to generate table.
	 * @param nr	Number of comparison repetitions of both learning methods for each row size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of rows) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved.
	 * @param ns	Parameter n of learnStructureLocally function.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param max_k	Parameter max_k of learnStructureLocally function.
	 * @param rep	Parameter rep of learnStructureLocally function.
	 */
	public static void localVsGlobalComparison(String file, int min_m, int max_m, int n, int v, double d, int nr, int ns, double max_w, int max_k, int rep)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, ln, gn;
			Table t;
			int i, j, m;
			double sum_local_sim, sum_global_sim;
			
			m = 1;
			for (i = 1; i <= min_m; i++)
			{
				m *= 2;
			}
			for (i = min_m; i <= max_m; i++)
			{
				pwr.print(i + "\t");
				sum_local_sim = 0;
				sum_global_sim = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomNetwork(n, v, d);
					t = rn.generateTable(m);
					
					gn = new Graph();
					gn.learnStructureK2(t);
					
					ln = new Graph();
					ln.learnStructureLocally(t, ns, max_w, max_k, rep);
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
				}
				
				pwr.print(sum_local_sim/nr + "\t" + sum_global_sim/nr + nl);
				pwr.flush();
				System.out.println(i + ":\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr);
				
				m *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function learnStructureLocally - old version). The comparison is with respect to the real network generating artificial data.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_s	Parameter max_s of learnStructureLocally function.
	 * @param min_p	Parameter min_p of learnStructureLocally function.
	 * @param max_k	Parameter max_k of learnStructureLocally function.
	 * @param rep	Parameter rep of learnStructureLocally function.
	 */
	public static void localVsGlobalComparison(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, int max_s, double min_p, int max_k, int rep)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = new Graph();
					ln.learnStructureLocally(t, 50*n*(n - 1), max_s, min_p, max_k, rep);
					sum_local_time += (System.nanoTime() - start);
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + nl);
				pwr.flush();
				System.out.println(i + ":\tsimilarity:\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr);
				System.out.println(i + ":\ttime:\t" + sum_local_time/(((long)1000000000)*nr) + "\t" + sum_global_time/(((long)1000000000)*nr));
				
				n *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function learnStructureLocally - old version). The comparison is with respect to the real network generating artificial data.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param max_k	Parameter max_k of learnStructureLocally function.
	 * @param rep	Parameter rep of learnStructureLocally function.
	 */
	public static void localVsGlobalComparison(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int max_k, int rep)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = new Graph();
					sum_local_k += ln.learnStructureLocally(t, 4*n*(n - 1), max_w, max_k, rep);
					sum_local_time += (System.nanoTime() - start);
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + sum_local_k/nr + nl);
				pwr.flush();
				System.out.println(i + ":\tsimilarity:\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr);
				System.out.println(i + ":\ttime:\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr));
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function learnStructureLocally - old version). The comparison is with respect to the real network generating artificial data.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 */
	public static void localVsGlobalComparison(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = new Graph();
					sum_local_k += ln.TESTlearnStructureLocally(t, 4*n*n, max_w, n - 1, rn);
					sum_local_time += (System.nanoTime() - start);
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + (double) sum_local_time/(double) sum_global_time + "\t" + sum_local_k/nr + nl);
				pwr.flush();
				System.out.println(i + ":\tsimilarity (local, global, ratio):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim);
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}

	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function TESTlearnStructureLocallyWithVisualization). The comparison is with respect to the real network generating artificial data. Some special data are also produced - with the aim of visualization of the statistics.
	 * @param file	File to which write results of comparison.
	 * @param sfile	File to which special statistics are written.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param c	Function TESTlearnStructureLocallyWithVisualization generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalComparisonWithVisualization(String file, String sfile, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			long[] time = new long [1];
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				FileWriter fwr = new FileWriter(file, true);
				PrintWriter pwr = new PrintWriter(fwr, true);
				
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					ln = new Graph();
					sum_local_k += ln.TESTlearnStructureLocallyWithVisualization(t, c*n*n, max_w, n - 1, rn, sfile, time);
					sum_local_time += time[0];
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + (double) sum_local_time/(double) sum_global_time + "\t" + sum_local_k/nr + nl);
				System.out.println(i + ":\tsimilarity (local, global, ratio):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim);
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
				
				pwr.close();
				fwr.close();
			}
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function TESTlearnStructureLocallyWithVisualization). The comparison is with respect to the real network generating artificial data. 
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity (average from left and right similarity) of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param c	Function TESTlearnStructureLocallyWithVisualization generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalAveragePerformanceComparison(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			long[] time = new long [1];
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				FileWriter fwr = new FileWriter(file, true);
				PrintWriter pwr = new PrintWriter(fwr, true);
				
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					ln = new Graph();
					sum_local_k += ln.TESTlearnStructureLocallyAveragePerformance(t, c*n*n, max_w, n - 1, rn, time);
					sum_local_time += time[0];
					
					sum_global_sim += (Graph.compareInclusionOfStructuresSkeleton(gn, rn) + Graph.compareInclusionOfStructuresSkeleton(rn, gn))/2;
					sum_local_sim += (Graph.compareInclusionOfStructuresSkeleton(ln, rn) + Graph.compareInclusionOfStructuresSkeleton(rn, ln))/2;
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time + "\t" + (double) sum_local_k/(double) nr + nl);
				System.out.println(i + ":\tsimilarity (local average, global average, ratio):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim);
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + (double) sum_local_k/(double) nr);
				
				n *= 2;
				
				pwr.close();
				fwr.close();
			}
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function TESTlearnStructureLocallyWithVisualization). The comparison is with respect to the real network generating artificial data. 
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity (average from left and right similarity) of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param c	Function TESTlearnStructureLocallyWithVisualization generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalLeftPerformanceComparison(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			long[] time = new long [1];
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				FileWriter fwr = new FileWriter(file, true);
				PrintWriter pwr = new PrintWriter(fwr, true);
				
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					ln = new Graph();
					sum_local_k += ln.TESTlearnStructureLocallyLeftPerformance(t, c*n*n, max_w, n - 1, rn, time);
					sum_local_time += time[0];
					
					sum_global_sim += Graph.compareInclusionOfStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareInclusionOfStructuresSkeleton(ln, rn);
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + (double) sum_local_time/(double) sum_global_time + "\t" + (double) sum_local_k/(double) nr + nl);
				System.out.println(i + ":\tsimilarity (local left, global left, ratio):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim);
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + (double) sum_local_k/(double) nr);
				
				n *= 2;
				
				pwr.close();
				fwr.close();
			}
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function TESTlearnStructureLocallyWithVisualization). The comparison is with respect to the real network generating artificial data. 
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity (average from left and right similarity) of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param c	Function TESTlearnStructureLocallyWithVisualization generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalRightPerformanceComparison(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			long[] time = new long [1];
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				FileWriter fwr = new FileWriter(file, true);
				PrintWriter pwr = new PrintWriter(fwr, true);
				
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					ln = new Graph();
					sum_local_k += ln.TESTlearnStructureLocallyRightPerformance(t, c*n*n, max_w, n - 1, rn, time);
					sum_local_time += time[0];
					
					sum_global_sim += Graph.compareInclusionOfStructuresSkeleton(rn, gn);
					sum_local_sim += Graph.compareInclusionOfStructuresSkeleton(rn, ln);
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + (double) sum_local_time/(double) sum_global_time + "\t" + (double) sum_local_k/(double) nr + nl);
				System.out.println(i + ":\tsimilarity (local right, global right, ratio):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim);
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + (double) sum_local_k/(double) nr);
				
				n *= 2;
				
				pwr.close();
				fwr.close();
			}
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function learnStructureLocally - new version). The comparison is with respect to the real network generating artificial data.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param c	Function learnStructureLocally generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalLearning(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim, sum_local_left_sim, sum_local_right_sim, sum_global_left_sim, sum_global_right_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				FileWriter fwr = new FileWriter(file, true);
				PrintWriter pwr = new PrintWriter(fwr, true);
				
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_global_left_sim = 0;
				sum_global_right_sim = 0;
				sum_local_left_sim = 0;
				sum_local_right_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = new Graph();
					sum_local_k += ln.learnStructureLocally(t, c*n*n, max_w, n - 1);
					sum_local_time += (System.nanoTime() - start);
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
					
					sum_global_left_sim += Graph.compareInclusionOfStructuresSkeleton(gn, rn);
					sum_local_left_sim += Graph.compareInclusionOfStructuresSkeleton(ln, rn);
					
					sum_global_right_sim += Graph.compareInclusionOfStructuresSkeleton(rn, gn);
					sum_local_right_sim += Graph.compareInclusionOfStructuresSkeleton(rn, ln);
					
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_left_sim/nr + "\t" + sum_global_left_sim/nr + "\t" + sum_local_left_sim/sum_global_left_sim + "\t" + sum_local_right_sim/nr + "\t" + sum_global_right_sim/nr + "\t" + sum_local_right_sim/sum_global_right_sim + "\t" + (sum_local_left_sim+sum_local_right_sim)/(2*nr) + "\t" + (sum_global_left_sim+sum_global_right_sim)/(2*nr) + "\t" + (sum_local_left_sim+sum_local_right_sim)/(sum_global_left_sim+sum_global_right_sim) + "\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time + "\t" + (double)sum_local_k/(double)nr + nl);
				System.out.println(i + ":\tsimilarity (local full, global full, ratio full, local left, global left, ratio left, local righ, global right, ratio right, local average, global average, ratio average):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_left_sim/nr + "\t" + sum_global_left_sim/nr + "\t" + sum_local_left_sim/sum_global_left_sim + "\t" + sum_local_right_sim/nr + "\t" + sum_global_right_sim/nr + "\t" + sum_local_right_sim/sum_global_right_sim + "\t" + (sum_local_left_sim+sum_local_right_sim)/(2*nr) + "\t" + (sum_global_left_sim+sum_global_right_sim)/(2*nr) + "\t" + (sum_local_left_sim+sum_local_right_sim)/(sum_global_left_sim+sum_global_right_sim));
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + (double) sum_local_k/ (double) nr);
				
				n *= 2;
				
				pwr.close();
				fwr.close();
			}
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network structure learning (function learnStructureK2) and merging based structure learning induced from local structures (function learnDenseStructureLocally). The comparison is with respect to the real network generating artificial data.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in table.
	 * @param max_n	Logarithm of maximal considered number of columns in table.
	 * @param m	Number of rows in table.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	An average ratio of connected edges in generated networks.
	 * @param nr	Number of comparison repetitions of both learning methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network table of current size (number of columns) is created. Then both local and global methods are used to learn network on the basis of table, and these network are compared with real network. In each line of the file the average from all comparisons similarity of local-based network and global-based network with real network are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnStructureLocally function.
	 * @param c	Function learnStructureLocally generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalDenseLearning(String file, int min_n, int max_n, int m, int v, double p, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			Graph rn, ln, gn;
			Table t;
			int i, j, n;
			double sum_local_sim, sum_global_sim, sum_local_left_sim, sum_local_right_sim, sum_global_left_sim, sum_global_right_sim;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				FileWriter fwr = new FileWriter(file, true);
				PrintWriter pwr = new PrintWriter(fwr, true);
				
				sum_local_sim = 0;
				sum_global_sim = 0;
				sum_global_left_sim = 0;
				sum_global_right_sim = 0;
				sum_local_left_sim = 0;
				sum_local_right_sim = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomNetwork(n, v, p);
					t = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnStructureK2(t);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = new Graph();
					sum_local_k += ln.learnDenseStructureLocally(t, c*n*n, max_w, n - 1);
					sum_local_time += (System.nanoTime() - start);
					
					sum_global_sim += Graph.compareStructuresSkeleton(gn, rn);
					sum_local_sim += Graph.compareStructuresSkeleton(ln, rn);
					
					sum_global_left_sim += Graph.compareInclusionOfStructuresSkeleton(gn, rn);
					sum_local_left_sim += Graph.compareInclusionOfStructuresSkeleton(ln, rn);
					
					sum_global_right_sim += Graph.compareInclusionOfStructuresSkeleton(rn, gn);
					sum_local_right_sim += Graph.compareInclusionOfStructuresSkeleton(rn, ln);
					
				}
				
				pwr.print(i + "\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_left_sim/nr + "\t" + sum_global_left_sim/nr + "\t" + sum_local_left_sim/sum_global_left_sim + "\t" + sum_local_right_sim/nr + "\t" + sum_global_right_sim/nr + "\t" + sum_local_right_sim/sum_global_right_sim + "\t" + (sum_local_left_sim+sum_local_right_sim)/(2*nr) + "\t" + (sum_global_left_sim+sum_global_right_sim)/(2*nr) + "\t" + (sum_local_left_sim+sum_local_right_sim)/(sum_global_left_sim+sum_global_right_sim) + nl);
				System.out.println(i + ":\tsimilarity (local full, global full, ratio full, local left, global left, ratio left, local righ, global right, ratio right, local average, global average, ratio average):\t" + sum_local_sim/nr + "\t" + sum_global_sim/nr + "\t" + sum_local_sim/sum_global_sim + "\t" + sum_local_left_sim/nr + "\t" + sum_global_left_sim/nr + "\t" + sum_local_left_sim/sum_global_left_sim + "\t" + sum_local_right_sim/nr + "\t" + sum_global_right_sim/nr + "\t" + sum_local_right_sim/sum_global_right_sim + "\t" + (sum_local_left_sim+sum_local_right_sim)/(2*nr) + "\t" + (sum_global_left_sim+sum_global_right_sim)/(2*nr) + "\t" + (sum_local_left_sim+sum_local_right_sim)/(sum_global_left_sim+sum_global_right_sim));
				System.out.println(i + ":\ttime (local, global, ratio):\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr) + "\t" + (double) sum_local_time/(double) sum_global_time);
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
				
				pwr.close();
				fwr.close();
			}
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network classifier (the network is learned via learnNetworkK2) and voting based classifier for local networks induced from subsets of attributes (learned with learnLocalNetworks function). The comparison is with respect to the average classification ability of all attributes.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in train and test tables.
	 * @param max_n	Logarithm of maximal considered number of columns in train and test tables.
	 * @param m	Number of rows in generated train and test tables.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both classification methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network train and test table of current size (number of columns) and m rows is created. Then both local and global classifiers are learned on the basis of train table. In each line of the file the average from classification ability on test tables of all attributes of local and global classifier are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnLocalNetworks function.
	 * @param c	Function generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalClassifier(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, gn;
			Graph[] ln;
			int[] cl;
			Table train, test;
			int i, j, k, n;
			double acc, sum_local_acc, sum_global_acc;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				sum_local_acc = 0;
				sum_global_acc = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					train = rn.generateTable(m);
					test = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnNetworkK2(train);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = Graph.learnLocalNetworks(train, c*n*n, max_w, n-1);
					sum_local_k += ln[0].getSize();
					sum_local_time += (System.nanoTime() - start);
					
					acc = 0;
					for (k = 0; k < n; k++)
					{
						cl = gn.classifyTable(test, k);
						acc += test.checkAccuracy(cl, k);
					}
					
					sum_global_acc += acc;
					
					acc = 0;
					for (k = 0; k < n; k++)
					{
						cl = Graph.classifyTableByWeightedVoting(ln, test, k);
						acc += test.checkAccuracy(cl, k);
					}
					
					sum_local_acc += acc;
				}
				
				pwr.print(i + "\t" + sum_local_acc/(nr*n) + "\t" + sum_global_acc/(nr*n) + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + sum_local_k/nr + nl);
				pwr.flush();
				System.out.println(i + ":\taccuracy:\t" + sum_local_acc/(nr*n) + "\t" + sum_global_acc/(nr*n));
				System.out.println(i + ":\ttime:\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr));
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network classifier (the network is learned via learnNetworkK2) and voting based classifier for local networks induced from subsets of attributes (learned with learnLocalNetworks function). The comparison is with respect to the average classification ability of all attributes.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in train and test tables.
	 * @param max_n	Logarithm of maximal considered number of columns in train and test tables.
	 * @param m	Number of rows in generated train and test tables.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	An average ratio of connected edges in generated networks.
	 * @param nr	Number of comparison repetitions of both classification methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network train and test table of current size (number of columns) and m rows is created. Then both local and global classifiers are learned on the basis of train table. In each line of the file the average from classification ability on test tables of all attributes of local and global classifier are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnLocalNetworks function.
	 * @param c	Function generates c*n*n local networks, where n is current considered number of rows in table.
	 */
	public static void localVsGlobalDenseClassifier(String file, int min_n, int max_n, int m, int v, double p, int nr, double max_w, int c)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, gn;
			Graph[] ln;
			int[] cl;
			Table train, test;
			int i, j, k, n;
			double acc, sum_local_acc, sum_global_acc;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				sum_local_acc = 0;
				sum_global_acc = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomNetwork(n, v, p);
					train = rn.generateTable(m);
					test = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnNetworkK2(train);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = Graph.learnLocalNetworks(train, c*n*n, max_w, n-1);
					sum_local_k += ln[0].getSize();
					sum_local_time += (System.nanoTime() - start);
					
					acc = 0;
					for (k = 0; k < n; k++)
					{
						cl = gn.classifyTable(test, k);
						acc += test.checkAccuracy(cl, k);
					}
					
					sum_global_acc += acc;
					
					acc = 0;
					for (k = 0; k < n; k++)
					{
						cl = Graph.classifyTableByWeightedVoting(ln, test, k);
						acc += test.checkAccuracy(cl, k);
					}
					
					sum_local_acc += acc;
				}
				
				pwr.print(i + "\t" + sum_local_acc/(nr*n) + "\t" + sum_global_acc/(nr*n) + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + sum_local_k/nr + nl);
				pwr.flush();
				System.out.println(i + ":\taccuracy:\t" + sum_local_acc/(nr*n) + "\t" + sum_global_acc/(nr*n));
				System.out.println(i + ":\ttime:\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr));
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function performs comparison between direct Baysian network classifier (the network is learned via learnNetworkK2) and voting based classifier for local networks induced from subsets of attributes (learned with learnLocalNetworks function). The comparison is with respect to the classification ability of one chosen attribute.
	 * @param file	File to which write results of comparison.
	 * @param min_n	Logarithm of minimal considered number of columns in train and test tables.
	 * @param max_n	Logarithm of maximal considered number of columns in train and test tables.
	 * @param m	Number of rows in generated train and test tables.
	 * @param v	Number of possible values of each attribute in table.
	 * @param p	Approximately average number of parents of each vertex in the generated networks.
	 * @param s	Most vertices in the generated networks has number of parents drawn uniformly from the set of integer values between p - s and p + s.
	 * @param nr	Number of comparison repetitions of both classification methods for each column size of generated table. In each repetition Baysian network of considered size is generated, from the network train and test table of current size (number of columns) and m rows is created. Then both local and global classifiers are learned on the basis of train table. In each line of the file the average from classification ability on test tables of local and global classifier are saved, together with average computation time of both approaches.
	 * @param max_w	Parameter max_w of learnLocalNetworks function.
	 */
	public static void localVsGlobalOneAttributeClassifier(String file, int min_n, int max_n, int m, int v, int p, int s, int nr, double max_w)
	{
		try
		{
			String nl = System.getProperty("line.separator");
			FileWriter fwr = new FileWriter(file, false);
			PrintWriter pwr = new PrintWriter(fwr, true);
			Graph rn, gn;
			Graph[] ln;
			int[] cl;
			Table train, test;
			int i, j, k, n;
			double acc, sum_local_acc, sum_global_acc;
			long sum_local_time, sum_global_time, start, sum_local_k;
			
			n = 1;
			for (i = 1; i <= min_n; i++)
			{
				n *= 2;
			}
			for (i = min_n; i <= max_n; i++)
			{
				sum_local_acc = 0;
				sum_global_acc = 0;
				sum_local_time = 0;
				sum_global_time = 0;
				sum_local_k = 0;
				
				for (j = 0; j < nr; j++)
				{
					rn = new Graph();
					rn.randomSparseNetwork(n, v, p, s);
					train = rn.generateTable(m);
					test = rn.generateTable(m);
					
					start = System.nanoTime();
					gn = new Graph();
					gn.learnNetworkK2(train);
					sum_global_time += (System.nanoTime() - start);
					
					start = System.nanoTime();
					ln = Graph.learnLocalNetworks(train, 4*n*(n-1), max_w, n-1, 0);
					sum_local_k += ln[0].getSize();
					sum_local_time += (System.nanoTime() - start);
					
					cl = gn.classifyTable(test, 0);
					sum_global_acc += test.checkAccuracy(cl, 0);
					
					cl = Graph.classifyTableByWeightedVoting(ln, test, 0);
					sum_local_acc += test.checkAccuracy(cl, 0);
				}
				
				pwr.print(i + "\t" + sum_local_acc/nr + "\t" + sum_global_acc/nr + "\t" + sum_local_time/nr + "\t" + sum_global_time/nr + "\t" + sum_local_k/nr + nl);
				pwr.flush();
				System.out.println(i + ":\tsimilarity:\t" + sum_local_acc/nr + "\t" + sum_global_acc/nr);
				System.out.println(i + ":\ttime:\t" + sum_local_time/(((long)1000000)*nr) + "\t" + sum_global_time/(((long)1000000)*nr));
				System.out.println(i + ":\tlevel:\t" + sum_local_k/nr);
				
				n *= 2;
			}
			
			pwr.close();
			fwr.close();
			
		}
		catch (IOException e)
		{
			System.exit(1);
		}
	}
	
	/**
	 * Function demonstrating this package.
	 */
	public static void demonstration()
	{
		int i, j, k;
		
		System.out.println("Package bn demonstration:");
		System.out.println("- creating random Bayesian network 'rn', with 10 vertices and density 0.2, where each variable has 2 possible values (0 and 1);");
		Graph rn = new Graph();
		rn.randomNetwork(10, 2, 0.2);
		
		System.out.println("- generating information system 't' from network 'rn', with 1000 objects;");
		Table t = rn.generateTable(1000);
		
		System.out.println("- learning Bayesian network structure 'ln' from information system 't',");
		Graph ln = new Graph();
		boolean condition = ln.learnStructureK2(t);
		if (condition)
		{
			System.out.println("  structure 'ln' satisfies condition that each vertex is not connected with at least one other vertex;");
		}
		else
		{
			System.out.println("  structure 'ln' does not satisfy condition that each vertex is not connected with at least one other vertex;");
		}
		
		System.out.println("- comparing skeletons of structures 'rn' and 'ln', that is comparing similarity of connections in both graphs,");
		double similarity = Graph.compareStructuresSkeleton(rn, ln);
		System.out.println("  the similarity is " + similarity + ";");
		
		System.out.println("- 'ln' contains so far only network structure, now we learn parameters so that 'ln' will become Baysian network representing table 't';");
		ln.learnParameters();
		
		System.out.println("- comparing classification ability of original network 'rn' and learned network 'ln', assuming that for example attribute nr 4 is decison attribute,");
		int[] rn_decision = rn.classifyTable(t, 4);
		int[] ln_decision = ln.classifyTable(t, 4);
		double rn_accuracy = t.checkAccuracy(rn_decision, 4);
		double ln_accuracy = t.checkAccuracy(ln_decision, 4);
		System.out.println("  accuracy of network 'rn' prediction is " + rn_accuracy + ", while accuracy of network 'ln' prediction is " + ln_accuracy + ";");
		
		System.out.println("- generating 100 random subtables of table 't' induced by attribute subsets of size 5, covering together all attributes, and learning Baysian network structure for each of them;");
		Table subtable;
		Graph[] sn = new Graph [100];
		k = 0;
		for (i = 0; i < 10; i++)
		{
			for (j = 0; j < 10; j++)
			{
				subtable = new SubTable(t, 5, i);
				sn[k] = new Graph();
				sn[k].learnStructureK2(subtable);
				k++;
			}
		}
		
		System.out.println("- merging all obtained subgraphs into one global network structure 'mln',");
		Graph mln;
		Graph tmp;
		boolean sparseness;
		mln = new Graph();
		mln.mergeStructures(sn[0], sn[1]);
		for (i = 2; i < 99; i++)
		{
			tmp = mln;
			mln = new Graph();
			mln.mergeStructures(tmp, sn[i]);
		}
		tmp = mln;
		mln = new Graph();
		sparseness = mln.mergeStructures(tmp, sn[99]);
		
		if (sparseness)
		{
			System.out.println("  all roots in the merging process, that is all 100 local networks satisfy sparseness condition: each vertex is not connected with at least one other;");
		}
		else
		{
			System.out.println("  not all roots in the merging process, that is not all 100 local networks satisfy sparseness condition: each vertex is not connected with at least one other;");
		}
		
		System.out.println("- comparing skeletons of structures 'rn' and 'mln',");
		similarity = Graph.compareStructuresSkeleton(rn, mln);
		System.out.println("  the similarity is " + similarity + ";");
		
		System.out.println("- generating 1000 random subtables of table 't' covering all attributes induced by attribute subsets of such smallest size, that almost all networks learned from them satisfy sparseness condition, and learning Baysian network structure for each of them;");
		
		mln = new Graph();
		mln.learnStructureLocally(t, 1000, 0.05, 10, 100);

		System.out.println("- we have merged all obtained subgraphs of size " + Math.min(k, 10) + " into one global network structure 'mln', this time using rough clustering approach;");
		
		System.out.println("- comparing skeletons of structures 'rn' and 'mln',");
		similarity = Graph.compareStructuresSkeleton(rn, mln);
		System.out.println("  the similarity is " + similarity + ";");
	}
	
	/**
	 * Test of merging two graphs correctness.
	 */
	public static void test()
	{
		int i, j, k;

		System.out.println("Package bn demonstration:");
		System.out.println("- creating random Bayesian network 'rn', with 10 vertices and density 0.2, where each variable has 2 possible values (0 and 1);");
		Graph rn = new Graph();
		rn.randomNetwork(10, 2, 0.2);

		System.out.println("- generating information system 't' from network 'rn', with 1000 objects;");
		Table t = rn.generateTable(1000);

		System.out.println("- learning Bayesian network structure 'ln' from information system 't',");
		Graph ln = new Graph();
		boolean condition = ln.learnStructureK2(t);
		if (condition)
		{
			System.out.println("  structure 'ln' satisfies condition that each vertex is not connected with at least one other vertex;");
		}
		else
		{
			System.out.println("  structure 'ln' does not satisfy condition that each vertex is not connected with at least one other vertex;");
		}

		System.out.println("- comparing skeletons of structures 'rn' and 'ln', that is comparing similarity of connections in both graphs,");
		double similarity = Graph.compareStructuresSkeleton(rn, ln);
		System.out.println("  the similarity is " + similarity + ";");

		System.out.println("- 'ln' contains so far only network structure, now we learn parameters so that 'ln' will become Baysian network representing table 't';");
		ln.learnParameters();

		System.out.println("- comparing classification ability of original network 'rn' and learned network 'ln', assuming that for example attribute nr 4 is decison attribute,");
		int[] rn_decision = rn.classifyTable(t, 4);
		int[] ln_decision = ln.classifyTable(t, 4);
		double rn_accuracy = t.checkAccuracy(rn_decision, 4);
		double ln_accuracy = t.checkAccuracy(ln_decision, 4);
		System.out.println("  accuracy of network 'rn' prediction is " + rn_accuracy + ", while accuracy of network 'ln' prediction is " + ln_accuracy + ";");

		System.out.println("- generating 100 random subtables of table 't' induced by attribute subsets of size 5, covering together all attributes, and learning Baysian network structure for each of them;");
		Table subtable;
		Graph[] sn = new Graph [100];
		k = 0;
		for (i = 0; i < 10; i++)
		{
			for (j = 0; j < 10; j++)
			{
				subtable = new SubTable(t, 5, i);
				sn[k] = new Graph();
				sn[k].learnStructureK2(subtable);
				k++;
			}
		}

		System.out.println("- merging all obtained subgraphs into one global network structure 'mln',");
		Graph mln;
		Graph tmp;
		boolean sparseness;
		mln = new Graph();
		mln.mergeStructures(sn[0], sn[1]);
		for (i = 2; i < 99; i++)
		{
			tmp = mln;
			mln = new Graph();
			mln.mergeStructures(tmp, sn[i]);
		}
		tmp = mln;
		mln = new Graph();
		sparseness = mln.mergeStructures(tmp, sn[99]);

		if (sparseness)
		{
			System.out.println("  all roots in the merging process, that is all 100 local networks satisfy sparseness condition: each vertex is not connected with at least one other;");
		}
		else
		{
			System.out.println("  not all roots in the merging process, that is not all 100 local networks satisfy sparseness condition: each vertex is not connected with at least one other;");
		}

		System.out.println("- comparing skeletons of structures 'rn' and 'mln',");
		similarity = Graph.compareStructuresSkeleton(rn, mln);
		System.out.println("  the similarity is " + similarity + ";");

                // Merge left half, right half and then results:
                Graph left;
		left = new Graph();
		left.mergeStructures(sn[0], sn[1]);
		for (i = 2; i < 49; i++)
		{
			tmp = left;
			left = new Graph();
			left.mergeStructures(tmp, sn[i]);
		}
		tmp = left;
		left = new Graph();
		left.mergeStructures(tmp, sn[49]);

                Graph right;
		right = new Graph();
		right.mergeStructures(sn[50], sn[51]);
		for (i = 52; i < 99; i++)
		{
			tmp = right;
			right = new Graph();
			right.mergeStructures(tmp, sn[i]);
		}
		tmp = right;
		right = new Graph();
		right.mergeStructures(tmp, sn[99]);

                Graph all = new Graph();
                boolean sp2 = all.mergeStructures(left, right);
                if (sp2)
		{
			System.out.println("  L+R: all roots in the merging process, that is all 100 local networks satisfy sparseness condition: each vertex is not connected with at least one other;");
		}
		else
		{
			System.out.println("  L+R: not all roots in the merging process, that is not all 100 local networks satisfy sparseness condition: each vertex is not connected with at least one other;");
		}

		System.out.println("- L+R: comparing skeletons of structures 'rn' and 'mln',");
		double similarity2 = Graph.compareStructuresSkeleton(rn, all);
		System.out.println("  L+R: the similarity is " + similarity2 + ";");

                System.out.println("The same similarity: " + (similarity==similarity2));
	}

	/**
	 * @param args Command line arguments, not used here.
	 */
	public static void main(String[] args)
	{
		test();
		/**
		 * localVsGlobalLearning("comparison.txt", 2, 10, 1000, 2, 2, 1, 256, 0, 1);
		 * localVsGlobalAveragePerformanceComparison("comparison.txt", 2, 10, 1000, 2, 4, 1, 256, 0, 1);
		
		localVsGlobalDenseLearning("comparison.txt", 2, 10, 1000, 2, 0.1, 10, 0, 8);
		
		 * localVsGlobalClassifier("comparison.txt", 4, 10, 1000, 2, 2, 1, 100, 0);
		 * localVsGlobalDenseClassifier("comparison.txt", 2, 10, 1000, 2, 0.1, 10, 0.05, 8);
		 * localVsGlobalComparisonWithVisualization("comparison.txt", "pict.txt", 2, 10, 1000, 2, 2, 1, 128, 0, 2);
		 */
	}
}
