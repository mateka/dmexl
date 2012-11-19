package bn;

/**
 * Main class demonstrating this package.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class Bn
{
	/**
	 * @param args Command line arguments, not used here.
	 */
	public static void main(String[] args)
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

	}
}
