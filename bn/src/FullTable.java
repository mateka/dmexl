package bn;

/**
 * Information system with nominal attributes.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public class FullTable implements Table
{
	/**
	 * Array containing table values, possible values of each attribute are always consecutive integers starting from 0. First dimension is about rows, second - about columns.
	 */
	private int[][] t;
	
	/**
	 * Vector containing number of possible values of each attribute.
	 */
	private int[] v;
	
	/**
	 * Number of rows of the table.
	 */
	private int m;
	
	/**
	 * Number of columns of the table.
	 */
	private int n;
	
	/**
	 * Constructor, creates table of the specified size - that is allocates memory for t and v arrays.
	 * @param nrow	Number of rows of the table.
	 * @param ncol	Number of columns of the table.
	 */
	public FullTable(int nrow, int ncol)
	{
		m = nrow;
		n = ncol;
		t = new int [m][n];
		v = new int [n];
	}
	
	/**
	 * Gives access to the array containing table values, possible values of each attribute are always consecutive integers starting from 0. First dimension is about rows, second - about columns.
	 * @param i	Row index.
	 * @param j	Column index.
	 * @return	Value of the j-th attribute for the i-th row.
	 */
	@Override public int getT(int i, int j) { return t[i][j]; }
	
	/**
	 * Gives access to memory address of the array containing table values.
	 * @return	Array representing whole table.
	 */
	@Override public int[][] getT() { return t; }
	
	/**
	 * Gives access to the vector of attributes description.
	 * @param i	Index.
	 * @return	Number of possible values of the i-th attribute.
	 */
	@Override public int getV(int i) { return v[i]; }
	
	/**
	 * Gives access to memory address of the vector of attributes description.
	 * @return	Array representing specified vector.
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
	 * @param j	Column index of the element.
	 * @param x	New value of the element.
	 */
	@Override public void setT(int i, int j, int x) { t[i][j] = x; }
	
	/**
	 * Sets value of the v array element specified in parameters.
	 * @param i	Index of the element.
	 * @param x	New value of the element.
	 */
	@Override public void setV(int i, int x) { v[i] = x; }
	
	/**
	 * Returns accuracy of prediction of indicated attribute.
	 * @param cl	Vector of proposed classification.
	 * @param i	Attribute (index) for which classification was done.
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
