package pl.edu.mimuw.dmexl_examples;

import java.util.*;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import static pl.edu.mimuw.dmexlib.dmexl.*;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.TaskExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        IExecutionContext e = null, t = null;
        try {
            System.out.println("started 4");

            e = new SimpleSequentialExecutionContext();

            t = new TaskExecutionContext(4);

            List<Integer> is = new ArrayList<>();
            for (int i = 0; i < 100; ++i) {
                is.add(i);
            }

            // Sum all elements
            Algorithm<Float> sumAlg = I(accumulate(
                    transform(is, new TwoMulOp()),
                    //is,
                    new SumOp(),
                    0.0f));
            // SumOp always succeeds

            long start2 = System.nanoTime();
            float sum2 = t.execute(sumAlg);
            long end2 = System.nanoTime();

            long start1 = System.nanoTime();
            float sum = e.execute(sumAlg);
            long end1 = System.nanoTime();

            System.out.println("4 The sum is: " + sum + " concrsum: " + sum2);

            System.out.println("Seq: " + (end1 - start1) / 1000000000.0 + " tasks: " + (end2 - start2) / 1000000000.0);

            // mul2 all elements
            //        Algorithm<? extends List<Integer>> mulAlg = transform(transform(is, new TwoMulOp()), new TwoMulOp());
            //        
            //        long start2 = System.nanoTime();
            //        List<Integer> l_ = t.execute(mulAlg).get();
            //        long end2 = System.nanoTime();
            //        Iterator<Integer> it_=l_.iterator();
            //        for(int i=0; i<10 && it_.hasNext(); ++i) System.out.print(it_.next() +";");
            //        System.out.println();
            //        
            //        long start1 = System.nanoTime();
            //        List<Integer> l = e.execute(mulAlg).get();
            //        long end1 = System.nanoTime();
            //        Iterator<Integer> it=l.iterator();
            //        for(int i=0; i<10 && it.hasNext(); ++i) System.out.print(it.next() +",");
            //        System.out.println();
            //        
            //        
            //        System.out.println("Seq: " + (end1-start1)/ 1000000000.0 + " tasks: " + (end2-start2) / 1000000000.0);

            // Filter odd elements
            //        Algorithm<? extends List<Integer>> removeOddAlg = filter(is, new IsEvenOp());
            //        List<Integer> ev = e.execute(removeOddAlg).get();
            //        Iterator<Integer> it2=ev.iterator();
            //        while(it2.hasNext()) System.out.print(it2.next() +",");
            //        System.out.println();
            //        
            //        // Filter duplicates
            //        List<Integer> duplicates = new ArrayList<>(is);
            //        Iterator<Integer> isToDup = is.iterator();
            //        while(isToDup.hasNext()) duplicates.add(isToDup.next());
            //        Algorithm<? extends Set<Integer>> toSet = set(transform(duplicates, new TwoMulOp()));
            //        Set<Integer> deduplicated = e.execute(toSet).get();
            //        Iterator<Integer> dedit=deduplicated.iterator();
            //        while(dedit.hasNext()) System.out.print(dedit.next() +",");
            //        System.out.println();
        } catch (InterruptedException | ExecutionException error) {
            System.out.println(error.toString() + "\n" + error.getMessage());
        } finally {
            if (e != null) {
                e.getExecutor().shutdown();
            }
            if (t != null) {
                t.getExecutor().shutdown();
            }
        }
    }

    private static class SumOp implements IAccumulateOperation<Float, Float> {

        @Override
        public Float invoke(Float arg) {
            return arg;
        }

        @Override
        public Float invoke(Float left, Float right) {
            float r = left + right;
            try {
                for (int i = 0; i < 50; ++i) {
                    Thread.sleep(1);
                }
            } catch (InterruptedException ex) {
            }
            return r;
        }
    }

    private static class TwoMulOp implements ITransformOperation<Float, Integer> {

        @Override
        public Float invoke(Integer arg) {
//            try {
//                for(int i=0;i<10;++i)Thread.sleep(1);
//            } catch (InterruptedException ex) {}
            return 2.0f * arg;
        }
    }

    private static class IsEvenOp implements IFilterOperation<Integer> {

        @Override
        public boolean invoke(Integer arg) {
            return 0 == (arg % 2);
        }
    }
}
