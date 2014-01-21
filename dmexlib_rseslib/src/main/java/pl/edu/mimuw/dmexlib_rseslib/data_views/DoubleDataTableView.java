/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.data_views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import rseslib.structure.attribute.Header;
import rseslib.structure.data.DoubleData;
import rseslib.structure.table.DoubleDataTable;
import rseslib.structure.table.NumericalStatistics;
import rseslib.system.progress.Progress;

/**
 *
 * @author Mateusz
 */
public class DoubleDataTableView implements DoubleDataTable {

    public DoubleDataTableView(DoubleDataTable table, HeaderView header) {
        this.table = table;
        this.header = header;
    }

    @Override
    public int noOfObjects() {
        return table.noOfObjects();
    }

    @Override
    public void add(DoubleData obj) {
        table.add(obj);
    }

    @Override
    public boolean remove(DoubleData obj) {
        return table.remove(obj);
    }

    @Override
    public ArrayList<DoubleData> getDataObjects() {
        ArrayList<DoubleData> result = new ArrayList<DoubleData>();

        ArrayList<DoubleData> orig = table.getDataObjects();
        for (DoubleData o : orig) {
            result.add(new DoubleDataView(o, header));
        }
        return result;
    }

    @Override
    public NumericalStatistics getNumericalStatistics(int attr) {
        return table.getNumericalStatistics(header.map(attr));
    }

    @Override
    public int[] getDecisionDistribution() {
        return table.getDecisionDistribution();
    }

    @Override
    public int[] getValueDistribution(int attrInd) {
        return table.getValueDistribution(header.map(attrInd));
    }

    @Override
    public ArrayList<DoubleData>[] randomSplit(int noOfPartsForLeft, int noOfPartsForRight) {
        final ArrayList<DoubleData>[] split = table.randomSplit(noOfPartsForLeft, noOfPartsForRight);
        ArrayList<DoubleData>[] result = new ArrayList[2];

        for (int i = 0; i < split.length; ++i) {
            result[i] = new ArrayList<DoubleData>();
            for (DoubleData o : split[i]) {
                result[i].add(new DoubleDataView(o, header));
            }
        }
        return result;
    }

    @Override
    public ArrayList<DoubleData>[] randomPartition(int noOfParts) {
        final ArrayList<DoubleData>[] partition = table.randomPartition(noOfParts);
        ArrayList<DoubleData>[] result = new ArrayList[noOfParts];

        for (int i = 0; i < partition.length; ++i) {
            result[i] = new ArrayList<DoubleData>();
            for (DoubleData o : partition[i]) {
                result[i].add(new DoubleDataView(o, header));
            }
        }
        return result;
    }

    @Override
    public void store(File outputFile, Progress prog) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void storeArff(String name, File outputFile, Progress prog) throws IOException, InterruptedException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Header attributes() {
        return header;
    }

    @Override
    public Object clone() {
        return new DoubleDataTableView((DoubleDataTable) table.clone(), header);
    }

    private final DoubleDataTable table;
    private final HeaderView header;
}
