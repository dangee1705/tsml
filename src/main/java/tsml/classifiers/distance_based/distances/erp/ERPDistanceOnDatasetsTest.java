package tsml.classifiers.distance_based.distances.erp;

import org.junit.runners.Parameterized;
import tsml.classifiers.distance_based.distances.DistanceMeasureOnDatasetsTest;
import tsml.classifiers.distance_based.distances.DistanceMeasureSpaceBuilder;

import java.util.Collection;

public class ERPDistanceOnDatasetsTest
        extends DistanceMeasureOnDatasetsTest {

    @Override public DistanceMeasureSpaceBuilder getBuilder() {
        return DistanceMeasureSpaceBuilder.ERP;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() throws Exception {
        return standardDatasets;
    }
}
