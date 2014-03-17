/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.pso_reducts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib_rseslib.data_views.DoubleDataTableView;
import pl.edu.mimuw.dmexlib_rseslib.data_views.HeaderView;
import rseslib.processing.classification.ClassifierSet;
import rseslib.processing.classification.CrossValidationTest;
import rseslib.processing.classification.MultipleTestResult;
import rseslib.processing.classification.rules.roughset.RoughSetRuleClassifier;
import rseslib.structure.attribute.ArrayHeader;
import rseslib.structure.attribute.Attribute;
import rseslib.structure.attribute.Header;
import rseslib.structure.attribute.NominalAttribute;
import rseslib.structure.attribute.formats.HeaderFormatException;
import rseslib.structure.data.DoubleData;
import rseslib.structure.data.DoubleDataWithDecision;
import rseslib.structure.data.NumberedDoubleDataObject;
import rseslib.structure.data.formats.DataFormatException;
import rseslib.structure.table.ArrayListDoubleDataTable;
import rseslib.structure.table.DoubleDataTable;
import rseslib.system.PropertyConfigurationException;
import rseslib.system.progress.EmptyProgress;

/**
 *
 * @author Mateusz
 */
public class Cost implements ICostFunction<Double, Position> {

    private final String CLASSIFIER_NAME = "GReducts";

    public Cost(String fileName) throws PropertyConfigurationException, IOException, HeaderFormatException, DataFormatException, InterruptedException {
        File dataFile = new File(fileName);
        this.table = new ArrayListDoubleDataTable(dataFile, new EmptyProgress());
    }

    @Override
    public Double evaluate(Position p) {
        final double alpha = 0.9;
        final double beta = 0.1;

        final double length = (p.dimensions() - p.cardinality()) / p.dimensions();

        return alpha * accuracy(p) + beta * length;
    }

    public int noOfAttr() {
        return table.attributes().noOfAttr();
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

    private final DoubleDataTable table;
}
