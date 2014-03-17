package pl.edu.mimuw.dmexlib_rseslib.examples;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {

    static final Map<String, IExample> examples = new HashMap<String, IExample>();

    static {
        // Build list of examples
        examples.put(PSOReductsExperiments.name, new PSOReductsExperiments());
    }

    public static void main(String[] args) throws Exception {
        // Execute example
        if (args.length < 1) {
            System.out.println("Not enough arguments!");
            System.out.println();

            return;
        }

        final String program = args[0];
        examples.get(program).execute(args);
    }

}
