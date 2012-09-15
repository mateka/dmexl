package pl.edu.mimuw.dmexl_examples;

import java.util.*;
import java.util.concurrent.ExecutionException;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import static pl.edu.mimuw.dmexlib.dmexl.*;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.TaskExecutionContext;
import pl.edu.mimuw.dmexlib.executors.TaskExecutor;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException, ExecutionException
    {
        IExecutionContext e = new SimpleSequentialExecutionContext();
        
        IExecutionContext t = new TaskExecutionContext();
        
        List<Integer> is = new ArrayList<>();
        for(int i=0;i<10000000; ++i) is.add(i);
        
        // Sum all elements
        Algorithm<Integer> sumAlg = I(accumulate(
                transform(is, new TwoMulOp()),
                //is,
                new SumOp(),
                0
        ));
        // SumOp always succeeds
        int sum = e.execute(sumAlg).get();
        
        System.out.println("The sum is: " + sum);
        
        // mul2 all elements
        Algorithm<? extends Collection<Integer>> mulAlg = transform(transform(is, new TwoMulOp()), new TwoMulOp());
        
        
        long start2 = System.nanoTime();
        Collection<Integer> l_ = t.execute(mulAlg).get();
        long end2 = System.nanoTime();
        Iterator<Integer> it_=l_.iterator();
        while(it_.hasNext()) System.out.print(it_.next() +",");
        System.out.println();
        
        long start1 = System.nanoTime();
        Collection<Integer> l = e.execute(mulAlg).get();
        long end1 = System.nanoTime();
        Iterator<Integer> it=l.iterator();
        while(it.hasNext()) System.out.print(it.next() +",");
        System.out.println();
        
        
        System.out.println("Seq: " + (end1-start1)/ 100000000.0 + " tasks: " + (end2-start2) / 100000000.0);
        
        // Filter odd elements
//        Algorithm<? extends Collection<Integer>> removeOddAlg = filter(is, new IsEvenOp());
//        Collection<Integer> ev = e.execute(removeOddAlg).get();
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
        
        ((TaskExecutor)t.getExecutor()).getExecService().shutdownNow();
    }
    
    private static class SumOp implements IAccumulateOperation<Integer, Integer> {

        @Override
        public ResultType<Integer> invoke(Integer left, Integer right) {
            int r = left + right;
            return new ResultType<>(r);
        }
        
    }
    
    private static class TwoMulOp implements ITransformOperation<Integer, Integer> {

        @Override
        public ResultType<Integer> invoke(Integer arg) {
            return new ResultType<>(2 * arg);
        }
        
    }
    
    private static class IsEvenOp implements IFilterOperation<Integer> {

        @Override
        public boolean invoke(Integer arg) {
            return 0 == (arg % 2);
        }
        
    }
}
