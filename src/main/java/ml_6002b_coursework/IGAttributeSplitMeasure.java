package ml_6002b_coursework;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import weka.core.Attribute;
import weka.core.Instances;

public class IGAttributeSplitMeasure implements AttributeSplitMeasure, Serializable {

    public double computeAttributeQuality(int[][] contingencyTable) {
        return AttributeMeasures.measureInformationGain(contingencyTable);
    }

    @Override
    public double computeAttributeQuality(Instances data, Attribute att) {
        return AttributeMeasures.measureInformationGain(AttributeMeasures.makeContingencyTable(data, att));
    }

    public static void main(String[] args) throws IOException {
        FileReader reader = new FileReader("./src/main/java/ml_6002b_coursework/test_data/part1.arff");
        Instances instances = new Instances(reader);
        instances.setClassIndex(3);

        IGAttributeSplitMeasure igAttributeSplitMeasure = new IGAttributeSplitMeasure();
        for(int i = 0; i < instances.numAttributes() - 1; i++) {
            Attribute attribute = instances.attribute(i);
            System.out.println("measure gini for attribute " + attribute.name() + " splitting diagnosis = " + igAttributeSplitMeasure.computeAttributeQuality(instances, attribute));
        }
        
        reader.close();
    }
}
