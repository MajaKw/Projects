package uj.wmii.pwj.introduction;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;

class ReverserTest {

    @ParameterizedTest(name = "{index} {0}")
    @CsvFileSource(resources = "reverse.csv", numLinesToSkip = 1)
    void reverse(String name, String input, String expected) {
        var result = new Reverser().reverse(input);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest(name = "{index} {0}")
    @CsvFileSource(resources = "reverse-words.csv", numLinesToSkip = 1)
    void reverseWords(String name, String input, String expected) {
        var result = new Reverser().reverseWords(input);
        assertThat(result).isEqualTo(expected);
    }

}
