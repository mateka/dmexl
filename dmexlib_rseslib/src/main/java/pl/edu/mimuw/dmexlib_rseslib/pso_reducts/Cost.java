/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib_rseslib.data_views.DoubleDataTableView;
import pl.edu.mimuw.dmexlib_rseslib.data_views.HeaderView;
import rseslib.processing.classification.ClassifierSet;
import rseslib.processing.classification.CrossValidationTest;
import rseslib.processing.classification.MultipleTestResult;
import rseslib.processing.classification.rules.roughset.RoughSetRuleClassifier;
import rseslib.structure.attribute.formats.HeaderFormatException;
import rseslib.structure.data.formats.DataFormatException;
import rseslib.structure.table.ArrayListDoubleDataTable;
import rseslib.structure.table.DoubleDataTable;
import rseslib.system.PropertyConfigurationException;
import rseslib.system.progress.EmptyProgress;

/**
 *
 * @author Mateusz
 */
public class Cost implements ICostFunction<FitnessValue, Position> {

    private final String CLASSIFIER_NAME = "GReducts";

    public Cost(String fileName) throws PropertyConfigurationException, IOException, HeaderFormatException, DataFormatException, InterruptedException {
        File dataFile = new File(fileName);
        this.table = new ArrayListDoubleDataTable(dataFile, new EmptyProgress());
    }

    @Override
    public FitnessValue evaluate(Position p) {
        final double alpha = 0.9;
        final double beta = 0.1;

        final double length = (p.dimensions() - p.cardinality()) / p.dimensions();
        final double acc = accuracy(p);
        
        updateStats(p.cardinality(), acc);

        return new FitnessValue(alpha * acc + beta * length, acc);
    }

    public int noOfAttr() {
        return table.attributes().noOfAttr();
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public double getMinAccuracy() {
        return minAccuracy;
    }

    public double getMaxAccuracy() {
        return maxAccuracy;
    }

    private double accuracy(Position p) {
        try {
            final Properties testProp = new Properties();
            final ClassifierSet classifiers = new ClassifierSet();

            testProp.setProperty(CrossValidationTest.NO_OF_FOLDS_PROPERTY_NAME, "10");
            testProp.setProperty(RoughSetRuleClassifier.s_sRuleGenerator, "GlobalReducts");

            classifiers.addClassifier(CLASSIFIER_NAME, RoughSetRuleClassifier.class);
            
            // Create table with some columns filtered out
            HeaderView head = new HeaderView(table.attributes(), p.mapping());
            DoubleDataTable posTable = new DoubleDataTableView(table, head);
            
            // Do cross-cvalidation test
            CrossValidationTest crossValid = new CrossValidationTest(testProp, classifiers);
            Map<String, MultipleTestResult> results = crossValid.test(posTable, new EmptyProgress());
            
            return results.get(CLASSIFIER_NAME).getAverage();// Create table with some columns filtered out
        } catch (PropertyConfigurationException ex) {
            return 0.0;
        } catch (InterruptedException ex) {
            return 0.0;
        }
    }
    
    private void updateStats(int length, double accuracy) {
        minLength = Math.min(length, minLength);
        maxLength = Math.max(length, maxLength);
        minAccuracy = Math.min(accuracy, minAccuracy);
        maxAccuracy = Math.max(accuracy, maxAccuracy);
    }

    private final DoubleDataTable table;
    private int minLength = Integer.MAX_VALUE;
    private int maxLength = Integer.MIN_VALUE;
    private double minAccuracy = Double.MAX_VALUE;
    private double maxAccuracy = Double.MIN_VALUE;
    
}
