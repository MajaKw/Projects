package uj.wmii.pwj.introduction;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

class QuadraticEquationTest {

    @ParameterizedTest(name = "{index} {0}")
    @CsvFileSource(resources = "quadric.csv", numLinesToSkip = 1)
    void findRoots(String name, double a, double b, double c, int expectedSize, double expectedR1, double expectedR2) {
        double[] result = new QuadraticEquation().findRoots(a, b, c);
        assertThat(result.length).isEqualTo(expectedSize);
        switch (expectedSize) {
           case 1 -> assertThat(result[0]).isCloseTo(expectedR1, offset(0.001));
           case 2 -> assertThat(result).containsOnly(new double[] {expectedR1, expectedR2}, offset(0.001));
        }
    }
}
