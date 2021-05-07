package ml_6002b_coursework;

import java.io.FileNotFoundException;
import java.io.FileReader;

import statistics.distributions.ChiSquareDistribution;
import tsml.classifiers.distance_based.utils.stats.scoring.ScoreUtils;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;

public class Main {
	public static void main(String[] args) throws Exception {
		// ScoreUtils.chiSquared
		Instances instances;
		try {
			instances = new Instances(new FileReader("C:/Users/Daniel/Documents/Uni/Year 3/Machine Learning/Coursework/tsml/src/main/java/ml_6002b_coursework/part1.arff"));
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		System.out.println(instances);

		System.out.println(instances.size());
		instances.setClass(instances.attribute("diagnosis"));

		class MyJ48 extends J48 {
			
		}

		J48 decisionTree = new J48();
		
		decisionTree.buildClassifier(instances);
		System.out.println(decisionTree);
	}
}
