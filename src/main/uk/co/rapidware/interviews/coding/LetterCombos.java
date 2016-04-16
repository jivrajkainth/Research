package main.rapidware.interviews.coding;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Sonny on 29/04/2014.
 */
public class LetterCombos {

    public void printLetterCombos(final List<String> inputStrings) {

        final StringBuilder comboSoFar = new StringBuilder(inputStrings.size());
        recursivePrintLetterCombos(comboSoFar, inputStrings);

    }

    private void recursivePrintLetterCombos(final StringBuilder comboSoFar, final List<String> inputStrings) {
        if (inputStrings.isEmpty()) {
            System.out.println(comboSoFar);
        } else {
            final String nextString = inputStrings.get(0);
            final int length = comboSoFar.length();
            for (final char character : nextString.toCharArray()) {
                recursivePrintLetterCombos(comboSoFar.append(character), inputStrings.subList(1, inputStrings.size()));
                comboSoFar.setLength(length);
            }
        }
    }

    public static void main(String[] args) {
        final LetterCombos letterCombos = new LetterCombos();

        letterCombos.printLetterCombos(
                Arrays.asList(
                        "ONE", "TWO", "THREE", "FOUR"
                )
        );
    }
}
