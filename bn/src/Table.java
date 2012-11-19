package bn;

/**
 * Interface of the information system with nominal attributes.
 * @author	Pawel Betlinski
 * @version	1.0
 * @since	2012
 */
public interface Table
{
	/**
	 * Gives access to the array containing table values, possible values of each attribute are always consecutive integers starting from 0. First dimension is about rows, second - about columns.
	 * @param i	Row index.
	 * @param j	Column index.
	 * @return	Value of the j-th attribute for the i-th row.
	 */
	int getT(int i, int j);
	
	/**
	 * Gives access to memory address of the array containing table values.
	 * @return	Array representing our table.
	 */
	int[][] getT();
	
	/**
	 * Gives access to the vector of attributes description.
	 * @param i	Index.
	 * @return	Number of possible values of the i-th attribute.
	 */
	int getV(int i);
	
	/**
	 * Gives access to memory address of the vector of attributes description.
	 * @return	Array representing specified vector.
	 */
	int[] getV();
	
	/**
	 * @return	Number of rows.
	 */
	int getM();
	
	/**
	 * @return	Number of columns.
	 */
	int getN();
	
	/**
	 * Sets value of the table array element specified in parameters.
	 * @param i	Row index of the element.
	 * @param j	Column index of the element.
	 * @param x	New value of the element.
	 */
	void setT(int i, int j, int x);
	
	/**
	 * Sets value of the attributes description array element specified in parameters.
	 * @param i	Index of the element.
	 * @param x	New value of the element.
	 */
	void setV(int i, int x);
	
	/**
	 * Returns accuracy of prediction indicated attribute.
	 * @param cl	Vector of proposed classification.
	 * @param i	Attribute for which classification was done.
	 * @return	Accuracy of prediction.
	 */
	double checkAccuracy(int[] cl, int i);
}
