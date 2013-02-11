package bn;

import java.util.Arrays;
import java.util.Random;

/**
 * Random generation toolbox.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class Generator
{
	/**
	 * Standard random number generator.
	 */
	private static Random r = new Random();

	/**
	 * Generates random real number from the segment [0, 1].
	 * @return	Random number.
	 */
	public static double random()
	{
		return r.nextDouble();
	}

	/**
	 * Generates random number from the set of numbers 0, ..., n-1.
	 * @param n	Size of the set of numbers.
	 * @return	Random number.
	 */
	public static int random(int n)
	{
		return r.nextInt(n);
	}

	/**
	 * Generates random number from the set of numbers 0, ..., n-1; with distribution specified in parameter.
	 * @param n	Size of the set of numbers.
	 * @param p	Vector of length n representing probability distribution of corresponding numbers. If this value is equal to null, then function behaves as random(n).
	 * @return	Random number.
	 */
	public static int random(int n, double[] p)
	{
		if (p == null)
		{
			return r.nextInt(n);
		}
		double d = r.nextDouble();
		double prz = 0;
		int i;
		
		for (i = 0; i < n; i++)
		{
			prz += p[i];
			if (d < prz) { break; }
		}
		
		return Math.min(i, n-1);
	}
	
	/**
	 * Generates random vector of real numbers summing to 1.
	 * @param n	Size of the vector.
	 * @return	The vector.
	 */
	public static double[] randomDistribution(int n)
	{
		double[] v = new double [n];
		double t, sum;
		int i;
		
		sum = 0;
		for (i = 0; i < n; i++)
		{
			t = random();
			sum += t;
			v[i] = t;
		}
		for (i = 0; i < n; i++)
		{
			v[i] = v[i] / sum;
		}
		
		return v;
	}
	
	/**
	 * Generates without repetition random sample of size m from the set of numbers 0, ..., n-1.
	 * @param m	Size of the sample.
	 * @param n	Size of the set of numbers.
	 * @return	Random sample.
	 */
	public static int[] sample(int m, int n)
	{
		int i, k;
		int[] sam = new int [m];
		int[] vec = new int [n];
		
		for (i = 0; i < n; i++) { vec[i] = i; }
		
		for (i = 0; i < m; i++)
		{
			k = r.nextInt(n-i);
			sam[i] = vec[k];
			vec[k] = vec[n-i-1];
		}
		
		return sam;
	}
	
	/**
	 * Generates without repetition random sample of size m from the set of numbers 0, ..., n-1, containing specified element.
	 * @param m	Size of the sample.
	 * @param n	Size of the set of numbers.
	 * @param c	Number included in the sample.
	 * @return	Random sample.
	 */
	public static int[] sample(int m, int n, int c)
	{
		int i, k;
		int[] sam = new int [m];
		int[] vec = new int [n];
		
		for (i = 0; i < n; i++) { vec[i] = i; }
		
		sam[0] = vec[c];
		vec[c] = vec[n-1];
		
		for (i = 1; i < m; i++)
		{
			k = r.nextInt(n-i);
			sam[i] = vec[k];
			vec[k] = vec[n-i-1];
		}
		
		return sam;
	}
	
	/**
	 * Generates random permutation of the set of numbers 0, ..., n-1.
	 * @param n	Size of the set of numbers.
	 * @return	Random permutation.
	 */
	public static int[] permutation(int n)
	{
		int i, k;
		int[] sam = new int [n];
		int[] vec = new int [n];
		
		for (i = 0; i < n; i++) { vec[i] = i; }
		
		for (i = 0; i < n; i++)
		{
			k = r.nextInt(n-i);
			sam[i] = vec[k];
			vec[k] = vec[n-i-1];
		}
		
		return sam;
	}
	
	/**
	 * Performs one-dimensional k-means clustering.
	 * @param x	List of real numbers to group into clusters, sorted in increasing order. This array should have at least two elements.
	 * @param k	Number of clusters to create, minimum 2.
	 * @param iter	Maximal number of iterations, minimum 0 (in the case of 0 clusters are created on the basis of starting random positions of centroids).
	 * @return	Result clusters, the range of each cluster is represented as pair of indexes of x array (first and last position belonging to the cluster).
	 */
	public static Pair[] kMeans(double[] x, int k, int iter)
	{
		if (x.length < 2)
		{
			System.out.println("Incorrect x parameter in kMeans function, it should be an array containing at least two elements.");
			System.exit(1);
		}
		k = Math.max(k, 2);
		iter = Math.max(iter, 0);
		
		boolean stop = false;
		int i, j, l, cur;
		double sum;
		double min_x = x[0];
		double max_x = x[x.length - 1];
		double[] centr = new double [k];
		Pair[] clust = new Pair [k];
		
		for (i = 0; i < k; i++)
		{
			clust[i] = new Pair();
		}
		
		for (i = 0; i < k; i++)
		{
			centr[i] = min_x + (max_x - min_x)*random();
		}
		Arrays.sort(centr);
		
		cur = 0;
		clust[0].setX(0);
		clust[k-1].setY(x.length - 1);
		for (i = 1; i < x.length && cur < k - 1; i++)
		{
			if (Math.abs(centr[cur] - x[i]) > Math.abs(centr[cur + 1] - x[i]))
			{
				clust[cur].setY(i - 1);
				clust[cur + 1].setX(i);
				cur++;
			}
		}
		
		for (j = 0; j < iter && !stop; j++)
		{
			for (l = 0; l < k; l++)
			{
				sum = 0;
				for (i = clust[l].getX(); i <= clust[l].getY(); i++)
				{
					sum += x[i];
				}
				
				centr[l] = sum / (double)(clust[l].getY() - clust[l].getX() + 1);
			}
			
			cur = 0;
			stop = true;
			for (i = 1; i < x.length && cur < k - 1; i++)
			{
				if (Math.abs(centr[cur] - x[i]) > Math.abs(centr[cur + 1] - x[i]))
				{
					if (stop)
					{
						if (clust[cur].getY() != i - 1)
						{
							stop = false;
						}
					}
					
					clust[cur].setY(i - 1);
					clust[cur + 1].setX(i);
					cur++;
				}
			}
		}
		
		return clust;
	}
}
