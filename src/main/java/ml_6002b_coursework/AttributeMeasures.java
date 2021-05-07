package ml_6002b_coursework;

import java.util.Arrays;

/**
 * Empty class for PArt 2.1 of the coursework
 *
 */
public class AttributeMeasures {
	

	// public static double entropy(int[] x, int c) {
	// 	double total = 0;
	// 	for(int i = 0; i < c; i++) {
	// 		int count = 0;
	// 		for(int j = 0; j < x.length; j++)
	// 			if(x[j] == i)
	// 				count++;
	// 		double p = (double) count / x.length;
	// 		total += nlogn(p);
	// 	}
	// 	return -total;
	// }

	

	

	public static double measureGini(int[][] attributes) {
		return 0;
	}

	public static double measureChiSquared(int[][] attributes) {
		return 0;
	}

	public static double measureChiSquaredYates(int[][] attributes) {
		return 0;
	}

	// --- start of measureInformationGain ---

	public static int[] getColumn(int[][] array, int n) {
		int[] column = new int[array.length];
		for(int i = 0; i < array.length; i++)
			column[i] = array[i][n];
		return column;
	}

	public static int numberOfClasses(int[] column) {
		int max = 0;
		for(int i = 0; i < column.length; i++)
			if(column[i] > max)
				max = column[i];
		return max + 1;
	}

	/*

	converts

	  A B T
	0
	1
	2
	3
	4

	into

       T0 T1 ... TN
	A0
	A1
	B0
	B1
	B2

	*/
	public static int[][] makeContingencyTable(int[][] data) {
		int rows = data.length;
		int cols = data[0].length;

		int[] classCounts = new int[cols];
		int totalAttibuteClasses = 0;
		for(int i = 0; i < cols; i++) {
			classCounts[i] = numberOfClasses(getColumn(data, i));
			if(i < cols - 1)
				totalAttibuteClasses += classCounts[i];
		}
		int totalTargetClasses = classCounts[cols - 1];

		int[][] contingencyTable = new int[totalAttibuteClasses][totalTargetClasses];
		int classesSoFar = 0;
		for(int x = 0; x < cols - 1; x++) {
			for(int y = 0; y < rows; y++)
				contingencyTable[classesSoFar + data[y][x]][data[y][cols - 1]]++;
			classesSoFar += classCounts[x];
		}

		return contingencyTable;
	}

	public static int sumRow(int[] row) {
		int total = 0;
		for(int i = 0; i < row.length; i++)
			total += row[i];
		return total;
	}

	public static int sumRows(int[][] rows) {
		int total = 0;
		for(int[] row : rows)
			total += sumRow(row);
		return total;
	}

	public static double nlogn(double n) {
		return n == 0 ? 0 : n * (Math.log(n) / Math.log(2));
	}

	public static double entropy(int[][] contingencyTable) {
		double total = 0;
		int cols = contingencyTable[0].length;
		int cases = sumRows(contingencyTable);
		for(int i = 0; i < cols; i++)
			total += nlogn((double) sumRow(getColumn(contingencyTable, i)) / cases);
		return -total;
	}
	
	public static double measureInformationGain(int[][] contingencyTable) {
		double total = entropy(contingencyTable);
		int allCases = sumRows(contingencyTable);
		for(int i = 0; i < contingencyTable.length; i++)
			total -= ((double) sumRow(contingencyTable[i]) / allCases) * entropy(new int[][]{contingencyTable[i]});
		return total;
	}

	// --- end of measureInformationGain ---

	public static void main(String[] args) {
		int[][] data = {
			// Outlook, Temp, Humidity, Windy, Play Golf
			{0, 1, 1, 0, 0},
			{0, 1, 1, 1, 0},
			{1, 1, 1, 0, 1},
			{2, 0, 1, 0, 1},
			{2, 0, 1, 0, 1},
			{2, 0, 0, 1, 0},
			{1, 0, 0, 1, 1},
			{0, 0, 1, 0, 0},
			{0, 0, 0, 0, 1},
			{2, 0, 1, 0, 1},
			{0, 0, 0, 1, 1},
			{1, 0, 1, 1, 1},
			{1, 1, 0, 0, 1},
			{2, 0, 1, 1, 0}
		};
	
		int[][] contingencyTable = makeContingencyTable(data);
		System.out.println(Arrays.deepToString(contingencyTable));

		int[][] outlook = {contingencyTable[0], contingencyTable[1], contingencyTable[2]};
		System.out.println(Arrays.deepToString(outlook));

		System.out.println(measureInformationGain(outlook));
	}
}
