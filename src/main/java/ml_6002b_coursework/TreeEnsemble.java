package ml_6002b_coursework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class TreeEnsemble extends AbstractClassifier {
	private ID3Coursework[] classifiers;
	private float percentage;
	private HashMap<ID3Coursework, ArrayList<Attribute>> classifierAttributes;
	private int count = 50;

	public TreeEnsemble(float percentage) {
		classifiers = new ID3Coursework[count];
		classifierAttributes = new HashMap<>();
		for(int i = 0; i < count; i++) {
			classifiers[i] = new ID3Coursework();
			classifierAttributes.put(classifiers[i], new ArrayList<>());
		}
		this.percentage = percentage;
	}

	public TreeEnsemble() {
		this(0.5f);
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {
		Random random = new Random();

		for(int i = 0; i < count; i++) {
			classifiers[i] = new ID3Coursework();
			int o = random.nextInt(4);
			classifiers[i].setOptions(new String[]{o == 0 ? "--ig" : o == 1 ? "--gini" : o == 2 ? "--chiSquared" : "--chiSquaredYates"});

			ArrayList<Attribute> removed = new ArrayList<>();
			ArrayList<Attribute> attributes = new ArrayList<>();
			for(int j = 0; j < data.numAttributes() - 1; j++)
				attributes.add(data.attribute(j));
			
			int amount = (int) ((data.numAttributes() - 1) * percentage);
			while(attributes.size() > amount)
				removed.add(attributes.remove(random.nextInt(attributes.size() - 1)));
				
			classifierAttributes.put(classifiers[i], removed);

			Instances subset = new Instances(data);
			for(int ai = data.numAttributes() - 2; ai >= 0; ai--) {
				Attribute att = data.attribute(ai);
				if(!classifierAttributes.get(classifiers[i]).contains(att)) {
					subset.deleteAttributeAt(ai);
				}
			}
			
			subset.setClassIndex(subset.numAttributes() - 1);
			
			classifiers[i].buildClassifier(subset);
		}
	}

	@Override
	public double classifyInstance(Instance instance) throws Exception {
		HashMap<Double, Integer> counts = new HashMap<>();

		for(ID3Coursework id3Coursework : classifiers) {
			Instance icopy = (Instance) instance.copy();
			for(Attribute att : classifierAttributes.get(id3Coursework))
				icopy.deleteAttributeAt(att.index());

			double cv = id3Coursework.classifyInstance(icopy);
			counts.put(cv, counts.getOrDefault(cv, 0) + 1);
		}

		double majority = -1;
		for(double key : counts.keySet())
			if(counts.get(key) > counts.getOrDefault(majority, 0))
				majority = key;

		return majority;
	}

	@Override
	public double[] distributionForInstance(Instance instance) throws Exception {
		double[] distribution = new double[instance.classAttribute().numValues()];
		for(ID3Coursework id3Coursework : classifiers)
			distribution[(int) id3Coursework.classifyInstance(instance)] += 1f / count;
		return distribution;
	}

	public static void main(String[] args) throws Exception {
		TreeEnsemble treeEnsemble = new TreeEnsemble();
		runClassifier(treeEnsemble, new String[]{"-t", "./src/main/java/ml_6002b_coursework/test_data/Chinatown_TRAIN.arff", "-T", "./src/main/java/ml_6002b_coursework/test_data/Chinatown_TEST.arff"});
	}
}
