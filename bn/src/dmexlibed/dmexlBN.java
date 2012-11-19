package dmexlibed;

import bn.Graph;
import bn.SubTable;
import bn.Table;
import java.util.LinkedList;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import static pl.edu.mimuw.dmexlib.dmexl.*;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.TaskExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 *
 * @author matek
 */
public class dmexlBN {

    public static void main(String[] args) {
        if (5 != args.length) {
            System.out.println("Wrong number of arguments.");
            System.out.println("Usage: bn.jar <method> <attributes> <density> <table size> <sub tables>");
            System.out.println("\tmethod - execution method");
            System.out.println("\tattributes - number of vertices of original network");
            System.out.println("\tdensity - density of original network");
            System.out.println("\ttable size - number of objects in original table");
            System.out.println("\tsub tables - number of subtables");
            return;
        }
        final String method = args[0];
        final int attribs = Integer.parseInt(args[1]);
        final double d = Integer.parseInt(args[2]) / 100.0;
        final int objects = Integer.parseInt(args[3]);
        final int subTables = Integer.parseInt(args[4]);

        Graph rn = new Graph();
        rn.randomNetwork(attribs, 2, d);
        Table t = rn.generateTable(objects);

        IExecutionContext ctx;
        switch (method) {
            case "original":
                originalCalc(rn, t, attribs, subTables);
                break;
            case "seq":
                ctx = new SimpleSequentialExecutionContext();
                try {
                    dmexlibCalc(ctx, rn, t, attribs, subTables);
                } finally {
                    ctx.getExecutor().shutdown();
                }
                break;
            case "task":
                ctx = new TaskExecutionContext(8);
                try {
                    dmexlibCalc(ctx, rn, t, attribs, subTables);
                } finally {
                    ctx.getExecutor().shutdown();
                }
                break;
        }
    }

    private static void originalCalc(final Graph rn, final Table t, final int attribs, final int subTables) {
        Graph mln = null;
        double time = 0.0;
        for (int k = 3; k < attribs; k++) {
            List<Table> Ct = new LinkedList<>();
            final int loops = Math.max(subTables/attribs, 1);
            for (int i = 0; i < loops; i++) {
                for (int j = 0; j < attribs; j++) {
                    Ct.add(new SubTable(t, k, j));
                }
            }

            long start = System.nanoTime();
            List<Graph> gs = new LinkedList<>();
            for (Table subt : Ct) {
                Graph grr = new Graph();
                grr.learnStructureK2(subt);
                gs.add(grr);
            }

            Graph tmp;
            mln = new Graph();
            mln.mergeStructures(gs.get(0), gs.get(1));
            for (int i = 2; i < gs.size() - 1; i++) {
                tmp = mln;
                mln = new Graph();
                mln.mergeStructures(tmp, gs.get(i));
            }
            tmp = mln;
            mln = new Graph();
            boolean ok = mln.mergeStructures(tmp, gs.get(gs.size() - 1));
            long end = System.nanoTime();

            time += (end - start) / 1000000000.0;
            if (!ok) {
                mln = null;
            } else {
                break;
            }
        }

        if (null != mln) {
            System.out.println(Graph.compareStructuresSkeleton(rn, mln) + ", " + time);
        } else {
            System.out.println(-1.0 + ", " + time);
        }
    }

    private static void dmexlibCalc(final IExecutionContext ctx, final Graph rn, final Table t, final int attribs, final int subTables) {
        double time = 0.0;
        Graph result = null;
        for (int k = 3; k < attribs; k++) {
            List<Table> Ct = new LinkedList<>();
            final int loops = Math.max(subTables/attribs, 1);
            for (int i = 0; i < loops; i++) {
                for (int j = 0; j < attribs; j++) {
                    Ct.add(new SubTable(t, k, j));
                }
            }

            long start = System.nanoTime();
            Graph zero = new Graph();
            zero.learnStructureK2(Ct.get(0));

            Algorithm<Graph> algo = accumulate(
                    transform(Ct, new Induce()),
                    new Aggregate(),
                    zero);
            try {
                result = ctx.execute(algo);
            } catch (Exception e) {
                result = null;
            }
            long end = System.nanoTime();
            time += (end - start) / 1000000000.0;

            if (null != result) {
                break;
            }
        }

        if (null != result) {
            System.out.println(Graph.compareStructuresSkeleton(rn, result) + ", " + time);
        } else {
            System.out.println(-1.0 + ", " + time);
        }
    }

    private static class Induce implements ITransformOperation<Graph, Table> {

        @Override
        public Graph invoke(Table argmnt) {
            Graph g = new Graph();
            g.learnStructureK2(argmnt);
            return g;
        }
    }

    private static class Aggregate implements IAccumulateOperation<Graph, Graph> {

        @Override
        public Graph invoke(Graph argmnt) {
            return argmnt;
        }

        @Override
        public Graph invoke(Graph a, Graph b) {
            Graph g = new Graph();
            if (!g.mergeStructures(a, b)) {
                throw new MergeError();
            }
            return g;
        }
    }

    private static class MergeError extends RuntimeException {
    }
}
