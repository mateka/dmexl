package pl.edu.mimuw.dmexl_examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import static pl.edu.mimuw.dmexlib.dmexl.*;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.SequentialExecutor;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.IFilterOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        IExecutor e = new SequentialExecutor();
        
        List<Integer> is = new ArrayList<>();
        for(int i=0;i<10; ++i) is.add(i);
        
        // Sum all elements
        Algorithm<Integer> sumAlg = I(accumulate(
                transform(is, new TwoMulOp()),
                //is,
                new SumOp(),
                0
        ));
        // SumOp always succeeds
        int sum = e.execute(sumAlg).getResult();
        
        System.out.println("The sum is: " + sum);
        
        // mul2 all elements
        Algorithm<Collection<Integer>> mulAlg = transform(is, new TwoMulOp());
        Collection<Integer> l = e.execute(mulAlg).getResult();
        Iterator<Integer> it=l.iterator();
        while(it.hasNext()) System.out.print(it.next() +",");
        System.out.println();
        
        // Filter odd elements
        Algorithm<Collection<Integer>> removeOddAlg = filter(is, new IsEvenOp());
        Collection<Integer> ev = e.execute(removeOddAlg).getResult();
        Iterator<Integer> it2=ev.iterator();
        while(it2.hasNext()) System.out.print(it2.next() +",");
        System.out.println();
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
