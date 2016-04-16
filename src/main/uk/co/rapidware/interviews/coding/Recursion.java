package main.rapidware.interviews.coding;

import java.util.logging.Logger;

/**
 * Created by Sonny on 28/04/2014.
 */
public class Recursion {

    private static final Logger LOGGER = Logger.getLogger(Recursion.class.getName());

    public static Logger getLogger() {
        return LOGGER;
    }

    public void recurseAndPrint(final int val, final StringBuilder builder) {
        if (val > 0) {
            recurseAndPrint(val - 1, builder);
        }

        builder.append(val);
    }

    public void preDecrementRecurseAndPrint(int val, final StringBuilder builder) {
        if (val > 0) {
            preDecrementRecurseAndPrint(--val, builder);
        }

        builder.append(val);
    }

    public void postDecrementRecurseAndPrint(int val, final StringBuilder builder) {
        if (val > 0) {
            postDecrementRecurseAndPrint(val--, builder);
        }

        builder.append(val);
    }

    public static void main(String[] args) {
        final Recursion recursion = new Recursion();
        final StringBuilder builder = new StringBuilder();

        recursion.recurseAndPrint(6, builder);
        getLogger().info(builder.toString());

        builder.setLength(0);
        recursion.preDecrementRecurseAndPrint(6, builder);
        getLogger().info(builder.toString());

        builder.setLength(0);
        recursion.postDecrementRecurseAndPrint(6, builder);
        getLogger().info(builder.toString());

    }
}
