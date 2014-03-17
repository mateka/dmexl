/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.mimuw.dmexlib_rseslib.examples;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import pl.edu.mimuw.dmexlib.Algorithm;
import pl.edu.mimuw.dmexlib.dmexl;
import pl.edu.mimuw.dmexlib.execution_contexts.IExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.SimpleSequentialExecutionContext;
import pl.edu.mimuw.dmexlib.execution_contexts.TaskExecutionContext;
import pl.edu.mimuw.dmexlib.nodes.operations.pso.IConvergence;
import pl.edu.mimuw.dmexlib_rseslib.pso_reducts.Convergence;
import pl.edu.mimuw.dmexlib_rseslib.pso_reducts.Cost;
import pl.edu.mimuw.dmexlib_rseslib.pso_reducts.Inertia;
import pl.edu.mimuw.dmexlib_rseslib.pso_reducts.PSORSFSInertia;
import pl.edu.mimuw.dmexlib_rseslib.pso_reducts.Particle;
import pl.edu.mimuw.dmexlib_rseslib.pso_reducts.Position;

/**
 *
 * @author Mateusz
 */
public class PSOReductsExperiments implements IExample {

    public static final String name = "PSOReductsExperiments";
// Measure-Command {java -jar .\dmexlib_rseslib-0.1-SNAPSHOT.jar PSOReductsExperiments 4 task .\tic-tac-toe.tab | Out-Default }
    @Override
    public void execute(String[] args) throws Exception {
        if (6 != args.length) {
            System.out.println("Wrong number of arguments.");
            System.out.println("Usage: _.jar <program> <processors> <method> <iterations> <particles> <table>");
            System.out.println("\tprogram - example name");
            System.out.println("\tprocessors - number of processors");
            System.out.println("\tmethod - execution method");
            System.out.println("\titerations - number of iterations");
            System.out.println("\tparticles - number of particles");
            System.out.println("\ttable - table path");
            return;
        }
        final String program = args[0];
        final int processors = Integer.parseInt(args[1]);
        final String method = args[2];
        final int iterations = Integer.parseInt(args[3]);
        final int particles = Integer.parseInt(args[4]);
        final String table = args[5];

        if (!name.equals(program)) {
            throw new Exception("Wrong program name");
        }

        // Create input structures and algorithm
        final Inertia inertia = new PSORSFSInertia(1.4f, iterations);
        final IConvergence conv = new Convergence(iterations, 0.85, inertia);
        final Cost cost = new Cost(table);
        final List<Particle> parts = createParticles(particles, cost.noOfAttr(), inertia);

        Algorithm<Particle> algo = dmexl.pso(parts, cost, conv);

        Particle best = null;

        if ("seq".equalsIgnoreCase(method)) {
            IExecutionContext ctx = new SimpleSequentialExecutionContext();
            try {
                best = ctx.execute(algo);
            } finally {
                ctx.getExecutor().shutdown();
            }
        } else if ("task".equalsIgnoreCase(method)) {
            IExecutionContext ctx = new TaskExecutionContext(4 * processors);
            try {
                best = ctx.execute(algo);
            } finally {
                ctx.getExecutor().shutdown();
            }
        }
        
        if(null != best) {
            System.out.print(best.getPosition());
            System.out.print("\t");
            System.out.println(best.getFitness());
        }
        else {
            System.out.println("Error");
        }
    }

    private List<Particle> createParticles(int n, int attribs, Inertia inertia) {
        final Random rnd = new Random();
        List<Particle> result = new LinkedList<Particle>();
        for (int i = 0; i < n; ++i) {
            final Position pos = new Position(attribs, rnd);
            final int vel = rnd.nextInt(attribs/3);
            
            result.add(new Particle(pos, vel, inertia));
        }

        return result;
    }

}
