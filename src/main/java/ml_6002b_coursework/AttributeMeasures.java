package ml_6002b_coursework;

public class AttributeMeasures {
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

	public static double measureGini(int[][] contingencyTable) {
		double gini = 1;
		int cases = sumRows(contingencyTable);
		for(int i = 0; i < contingencyTable[0].length; i++) {
			double p = (double) sumRow(getColumn(contingencyTable, i)) / cases;
			gini -= p * p;
		}
		return gini;
	}

	public static double measureChiSquared(int[][] contingencyTable) {
		int total = sumRows(contingencyTable);

		int[] rowTotals = new int[contingencyTable.length];
		for(int i = 0; i < contingencyTable.length; i++)
			rowTotals[i] = sumRow(contingencyTable[i]);
		int[] columnTotals = new int[contingencyTable[0].length];
		for(int i = 0; i < contingencyTable[0].length; i++)
			columnTotals[i] = sumRow(getColumn(contingencyTable, i));
		
		double chiSquared = 0;
		for(int i = 0; i < contingencyTable.length; i++) {
			for(int j = 0; j < contingencyTable[0].length; j++) {
				double observed = contingencyTable[i][j];
				double expected = rowTotals[i] * ((double) columnTotals[j] / total);
				double diff = observed - expected;
				chiSquared += (diff * diff) / expected;
			}
		}
		return chiSquared;
	}

	public static double measureChiSquaredYates(int[][] contingencyTable) {
		int total = sumRows(contingencyTable);

		int[] rowTotals = new int[contingencyTable.length];
		for(int i = 0; i < contingencyTable.length; i++)
			rowTotals[i] = sumRow(contingencyTable[i]);
		int[] columnTotals = new int[contingencyTable[0].length];
		for(int i = 0; i < contingencyTable[0].length; i++)
			columnTotals[i] = sumRow(getColumn(contingencyTable, i));
		
		double chiSquaredYates = 0;
		for(int i = 0; i < contingencyTable.length; i++) {
			for(int j = 0; j < contingencyTable[0].length; j++) {
				double observed = contingencyTable[i][j];
				double expected = rowTotals[i] * ((double) columnTotals[j] / total);
				double diff = Math.abs(observed - expected) - 0.5;
				chiSquaredYates += (diff * diff) / expected;
			}
		}
		return chiSquaredYates;
	}


	public static void main(String[] args) {
		int[][] meningitis = {
			{1, 1, 1, 1},
			{0, 1, 1, 1},
			{0, 1, 0, 1},
			{1, 0, 1, 1},
			{0, 1, 1, 1},
			{1, 0, 1, 1},
			{0, 1, 0, 0},
			{0, 0, 1, 0},
			{0, 1, 0, 0},
			{1, 0, 1, 0},
			{1, 0, 0, 0},
			{0, 0, 0, 0}
		};
		int[][] meningitisContingencyTable = makeContingencyTable(meningitis);
		int headache[][] = {meningitisContingencyTable[0], meningitisContingencyTable[1]};

		System.out.println("measure information gain for headache splitting diagnosis = " + measureInformationGain(headache));
		System.out.println("measure gini for headache splitting diagnosis = " + measureGini(headache));
		System.out.println("measure chi squared for headache splitting diagnosis = " + measureChiSquared(headache));
		System.out.println("measure chi squared yates for headache splitting diagnosis = " + measureChiSquaredYates(headache));
	}
}
