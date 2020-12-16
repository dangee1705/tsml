package tsml.classifiers.distance_based.distances.dtw;

import tsml.classifiers.distance_based.distances.transformed.MatrixBasedTransformDistanceMeasure;
import tsml.classifiers.distance_based.distances.transformed.TransformDistanceMeasure;
import tsml.classifiers.distance_based.utils.collections.params.ParamSpace;
import tsml.classifiers.distance_based.utils.collections.params.ParamSpaceBuilder;
import tsml.classifiers.distance_based.utils.collections.params.distribution.double_based.UniformDoubleDistribution;
import tsml.transformers.Derivative;
import weka.core.Instances;

import java.util.stream.IntStream;

import static tsml.classifiers.distance_based.distances.DistanceMeasure.DISTANCE_MEASURE_FLAG;
import static tsml.classifiers.distance_based.distances.dtw.DTW.WINDOW_SIZE_FLAG;
import static tsml.classifiers.distance_based.utils.collections.CollectionUtils.newArrayList;
import static utilities.ArrayUtilities.range;
import static utilities.ArrayUtilities.unique;

public class DTWDistanceConfigs {


    public static class DTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildDTWSpace(data);
        }
    }

    public static class FullWindowDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildFullWindowDTWSpace();
        }
    }

    public static class FullWindowDDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildFullWindowDDTWSpace();
        }
    }

    public static class ContinuousDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildContinuousDTWSpace(data);
        }
    }

    public static class RestrictedContinuousDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildRestrictedContinuousDTWSpace(data);
        }
    }

    public static class DDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildDDTWSpace(data);
        }
    }

    public static class ContinuousDDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildContinuousDDTWSpace(data);
        }
    }

    public static class RestrictedContinuousDDTWSpaceBuilder implements ParamSpaceBuilder {

        @Override public ParamSpace build(final Instances data) {
            return buildRestrictedContinuousDDTWSpace(data);
        }
    }

    /**
     * param space containing full derivative DTW params (i.e. full window)
     *
     * @return
     */
    public static ParamSpace buildDdtwFullWindowSpace() {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG,
                newArrayList(newDDTWDistance()),
                buildFullWindowDTWParams());
    }

    /**
     * build DDTW
     *
     * @return
     */
    public static TransformDistanceMeasure newDDTWDistance() {
        return new MatrixBasedTransformDistanceMeasure("DDTWDistance", new Derivative(), new DTWDistance());
    }

    /**
     * Build DTW space with corresponding params. This includes ED and Full DTW
     * @param instances
     * @return
     */
    public static ParamSpace buildDTWSpace(Instances instances) {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG, newArrayList(new DTWDistance()),
                buildDTWParams(instances));
    }

    /**
     * Build DTW params. This includes ED and Full DTW
     * @param instances
     * @return
     */
    public static ParamSpace buildDTWParams(Instances instances) {
        return new ParamSpace()
                       .add(WINDOW_SIZE_FLAG, IntStream.range(0, 100).mapToDouble(i -> (double) i / 100d).toArray());
    }

    /**
     * Same as DTW version but with derivative. This includes ED and Full DTW (deriv versions)
     * @param instances
     * @return
     */
    public static ParamSpace buildDDTWSpace(Instances instances) {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG, newArrayList(newDDTWDistance()),
                buildDTWParams(instances));
    }

    public static ParamSpace buildRestrictedContinuousDTWParams(Instances data) {
        final ParamSpace subSpace = new ParamSpace();
        // pf implements this as randInt((len + 1) / 4), so range is from 0 to (len + 1) / 4 - 1 inclusively.
        // above doesn't consider class value, so -1 from len
        subSpace.add(WINDOW_SIZE_FLAG, new UniformDoubleDistribution(0d,
//                ((double) (data.numAttributes()) / 4 - 1) / 100
                0.25d)
        );
        return subSpace;
    }

    public static ParamSpace buildRestrictedContinuousDTWSpace(Instances data) {
        final ParamSpace space = new ParamSpace();
        space.add(DISTANCE_MEASURE_FLAG, newArrayList(new DTWDistance()),
                  buildRestrictedContinuousDTWParams(data));
        return space;
    }
    
    public static ParamSpace buildContinuousDTWParams(Instances data) {
        return new ParamSpace().add(WINDOW_SIZE_FLAG, new UniformDoubleDistribution(0d, 1d));
    }
    
    public static ParamSpace buildContinuousDTWSpace(Instances data) {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG, newArrayList(new DTWDistance()), buildContinuousDTWParams(data));
    }

    public static ParamSpace buildContinuousDDTWSpace(Instances data) {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG, newArrayList(newDDTWDistance()), buildContinuousDTWParams(data));
    }

    public static ParamSpace buildRestrictedContinuousDDTWSpace(Instances data) {
        final ParamSpace space = new ParamSpace();
        space.add(DISTANCE_MEASURE_FLAG, newArrayList(newDDTWDistance()),
                  buildRestrictedContinuousDTWParams(data));
        return space;
    }

    /**
     * param space containing full DTW
     *
     * @return
     */
    public static ParamSpace buildFullWindowDTWSpace() {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG, newArrayList(new DTWDistance()),
                buildFullWindowDTWParams());
    }

    /**
     * param space containing full DDTW
     *
     * @return
     */
    public static ParamSpace buildFullWindowDDTWSpace() {
        return new ParamSpace().add(DISTANCE_MEASURE_FLAG, newArrayList(newDDTWDistance()),
                buildFullWindowDTWParams());
    }

    /**
     * param space containing full DTW params (i.e. full window)
     *
     * @return
     */
    public static ParamSpace buildFullWindowDTWParams() {
        ParamSpace params = new ParamSpace();
        params.add(WINDOW_SIZE_FLAG, newArrayList(1d));
        return params;
    }

}
