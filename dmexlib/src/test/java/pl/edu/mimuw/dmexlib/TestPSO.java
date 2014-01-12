/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib;

import static pl.edu.mimuw.dmexlib.dmexl.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import pl.edu.mimuw.dmexlib.execution_contexts.CustomizableTaskExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;
import pl.edu.mimuw.dmexlib.executors.CustomizableTaskExecutor;
import pl.edu.mimuw.dmexlib.executors.SimpleTaskManager;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.ICostFunction;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IParticle;
import pl.edu.mimuw.dmexlib.optimizers.ITreeOptimizer;
import pl.edu.mimuw.dmexlib.optimizers.NoOpOptimizer;

/**
 *
 * @author Mateusz
 */
public class TestPSO extends TestCase {

    public TestPSO(String testName) {
        super(testName);
    }

    public void testH1PSO() throws Exception {
        runPSOs(new H1());
    }

    public void testH2PSO() throws Exception {
        runPSOs(new H2());
    }

    private void runPSOs(BaseH h) throws Exception {
        final long seed = 1234567890;
        final ITreeOptimizer optimizer = new NoOpOptimizer();

        IExecutionContext ctx;
        Random rnd;

        rnd = new Random(seed);
        ctx = new SimpleSequentialExecutionContext(optimizer, rnd);
        long seqStart = System.nanoTime();
        double seqFitness = execPSO(ctx, h, rnd);
        long seqStop = System.nanoTime();

        rnd = new Random(seed);
        CustomizableTaskExecutor taskExecutor = new CustomizableTaskExecutor(new SimpleTaskManager(8));
        ctx = new CustomizableTaskExecutionContext(taskExecutor, optimizer, rnd);
        long parStart = System.nanoTime();
        double parFitness = execPSO(ctx, h, rnd);
        long parStop = System.nanoTime();

        assert (Math.abs(seqFitness - parFitness) < 2 * h.getError());
        
        System.out.println();
        System.out.println("Seq: " + (seqStop - seqStart) / 1000000000.0 + " tasks: " + (parStop - parStart) / 1000000000.0);
        System.out.println();
    }

    private double execPSO(IExecutionContext ctx, BaseH h, Random rnd) throws Exception {
        List<Particle> parts = createParticles(1000, 100.0, 100.0, rnd);

        Convergence conv = new Convergence(10000, h.getTarget(), h.getError());
        Algorithm<Particle> algo = dmexl.pso(parts, h, conv);

        Particle best = ctx.execute(algo);
        System.out.print("(");
        System.out.print(best.getPosition().x);
        System.out.print(", ");
        System.out.print(best.getPosition().y);
        System.out.print(") = ");
        System.out.println(best.getFitness());

        return best.getFitness();
    }

    private List<Particle> createParticles(int n, double x, double y, Random rnd) {
        List<Particle> result = new LinkedList<Particle>();
        for (int i = 0; i < n; ++i) {
            final Point p = new Point(rnd.nextDouble() * 2 * x - x, rnd.nextDouble() * 2 * y - y);
            final Point v = new Point(rnd.nextDouble() * x - 0.5 * x, rnd.nextDouble() * y - 0.5 * y);
            result.add(new Particle(p, v));
        }

        return result;
    }

    private class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private interface BaseH extends ICostFunction<Double, Point> {

        public double getTarget();

        public double getError();
    }

    private class H1 implements BaseH {

        @Override
        public Double evaluate(Point p) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {}
            
            final double d = Math.sqrt(Math.pow(p.x - 8.6998, 2.0) + Math.pow(p.y - 6.7665, 2.0));
            return (f(p.x, -p.y) + f(p.y, p.x)) / (1 + d);
        }

        private double f(double x, double y) {
            return Math.pow(Math.sin(x + y / 8), 2.0);
        }

        @Override
        public double getTarget() {
            return 2.0;
        }

        @Override
        public double getError() {
            return 0.001;
        }
    }

    private class H2 implements BaseH {

        @Override
        public Double evaluate(Point p) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {}
            
            final double sumSq = Math.pow(p.x, 2.0) + Math.pow(p.y, 2.0);
            final double numerator = Math.pow(Math.sin(Math.sqrt(sumSq)), 2.0) - 0.5;
            final double denominator = Math.pow(1 + 0.001 * sumSq, 2.0);
            final double d = Math.sqrt(Math.pow(p.x - 8.6998, 2.0) + Math.pow(p.y - 6.7665, 2.0));
            return 0.5 - numerator / denominator;
        }

        @Override
        public double getTarget() {
            return 1.0;
        }

        @Override
        public double getError() {
            return 0.001;
        }
    }

    private class Convergence implements IConvergence<Double> {

        public Convergence(int loops, double target, double error) {
            this.loops = loops;
            this.target = target;
            this.error = error;
        }

        @Override
        public boolean isConvergent(int generation, Double best, Double current) {
            return generation > loops || Math.abs(target - current) < error;
        }
        private final int loops;
        private final double target;
        private final double error;
    }

    private class Particle implements IParticle<Double, Point> {

        public Particle(Point position, Point velocity) {
            this.position = position;
            this.best = new Point(position.x, position.y);
            this.velocity = velocity;
            this.fitness = 0.0;
            this.bestFitness = 0.0;
        }

        @Override
        public Point getPosition() {
            return position;
        }

        @Override
        public Double getFitness() {
            return fitness;
        }

        @Override
        public void update(Point globalBest, Random rnd) {
            Point prev = new Point(position.x, position.y); // store current position

            // Move
            position.x += velocity.x;
            position.y += velocity.y;

            // Update velocity
            velocity.x = updateSpeed(velocity.x, best.x - prev.x, globalBest.x - prev.x, rnd);
            velocity.y = updateSpeed(velocity.y, best.y - prev.y, globalBest.y - prev.y, rnd);
            inertia *= 0.85;

            // Update particle's best
            if (bestFitness < fitness) {
                best = prev;
                bestFitness = fitness;
            }
        }

        @Override
        public void setFitness(Double newFitness) {
            fitness = newFitness;
        }

        private double updateSpeed(double s, double d1, double d2, Random rnd) {
            return inertia * s + 0.6 * rnd.nextDouble() * d1 + 0.4 * rnd.nextDouble() * d2;
        }

        private final Point position;
        private final Point velocity;
        private Double fitness;
        private Double inertia = 1.0;

        private Point best;
        private Double bestFitness;
    }
}
