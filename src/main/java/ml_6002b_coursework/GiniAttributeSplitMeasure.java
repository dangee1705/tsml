package ml_6002b_coursework;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import weka.core.Attribute;
import weka.core.Instances;

public class GiniAttributeSplitMeasure implements AttributeSplitMeasure, Serializable {

	@Override
	public double computeAttributeQuality(Instances data, Attribute att) {
		return AttributeMeasures.measureGini(AttributeMeasures.makeContingencyTable(data, att));
	}

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader("./src/main/java/ml_6002b_coursework/test_data/part1.arff");
        Instances instances = new Instances(reader);
        instances.setClassIndex(3);

        GiniAttributeSplitMeasure giniAttributeSplitMeasure = new GiniAttributeSplitMeasure();
        for(int i = 0; i < instances.numAttributes() - 1; i++) {
            Attribute attribute = instances.attribute(i);
            System.out.println("measure gini for attribute " + attribute.name() + " splitting diagnosis = " + giniAttributeSplitMeasure.computeAttributeQuality(instances, attribute));
        }
        
        reader.close();
    }

    @Override
    public double computeAttributeQuality(int[][] contingencyTable) {
        return AttributeMeasures.measureGini(contingencyTable);
    }
}
