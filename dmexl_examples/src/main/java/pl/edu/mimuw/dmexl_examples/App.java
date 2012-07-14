package pl.edu.mimuw.dmexl_examples;

import java.util.ArrayList;
import java.util.List;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.ResultType;
import pl.edu.mimuw.dmexlib.dmexl;
import pl.edu.mimuw.dmexlib.executors.IExecutor;
import pl.edu.mimuw.dmexlib.executors.SequentialExecutor;
import pl.edu.mimuw.dmexlib.nodes.operations.IAccumulateOperation;
import pl.edu.mimuw.dmexlib.nodes.operations.ITransformOperation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("hel");
        IExecutor e = new SequentialExecutor();
        
        List<Integer> is = new ArrayList<>();
        for(int i=0;i<10; ++i) is.add(i);
        
        // Sum all elements
        Algorithm<Integer> sumAlg = dmexl.accumulate(
                //dmexl.transform(is, new TwoMulOp()),
                is,
                new SumOp(),
                0
        );
        // SumOp always succeeds
        int sum = e.execute(sumAlg).getResult();
        
        System.out.println("The sum is: " + sum);
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
}
