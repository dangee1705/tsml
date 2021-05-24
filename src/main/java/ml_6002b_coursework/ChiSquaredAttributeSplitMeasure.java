package ml_6002b_coursework;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import weka.core.Attribute;
import weka.core.Instances;

public class ChiSquaredAttributeSplitMeasure implements AttributeSplitMeasure, Serializable {
	private boolean useYates = false;
	
    @Override
	public double computeAttributeQuality(Instances data, Attribute att) {
		return useYates ? AttributeMeasures.measureChiSquaredYates(AttributeMeasures.makeContingencyTable(data, att)) : AttributeMeasures.measureChiSquared(AttributeMeasures.makeContingencyTable(data, att));
	}

	public void useYates(boolean useYates) {
		this.useYates = useYates;
	}

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader("./src/main/java/ml_6002b_coursework/test_data/part1.arff");
        Instances instances = new Instances(reader);
        instances.setClassIndex(3);

        ChiSquaredAttributeSplitMeasure chiSquaredAttributeSplitMeasure = new ChiSquaredAttributeSplitMeasure();
        for(int i = 0; i < instances.numAttributes() - 1; i++) {
            Attribute attribute = instances.attribute(i);
            System.out.println("measure chi squared for attribute " + attribute.name() + " splitting diagnosis = " + chiSquaredAttributeSplitMeasure.computeAttributeQuality(instances, attribute));
        }
        
        reader.close();
    }

    @Override
    public double computeAttributeQuality(int[][] contingencyTable) {
        return useYates ? AttributeMeasures.measureChiSquaredYates(contingencyTable) : AttributeMeasures.measureChiSquared(contingencyTable);
    }
}
