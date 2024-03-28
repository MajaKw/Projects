package uj.wmii.pwj.delegations;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class CalcTest {

    @ParameterizedTest(name = "{index} {0}")
    @CsvFileSource(resources = "/delegations.csv", numLinesToSkip = 1)
    void checkDelegations(String name, String start, String end, BigDecimal dailyRate, BigDecimal expected) {
        Calc c = new Calc();
        var calculated = c.calculate(name, start, end, dailyRate);
        System.out.println("start: " + start + ", end: " + end + ", rate: " + dailyRate + ", expected: " + expected + ", calculated: " + calculated);
        assertThat(calculated).isEqualTo(expected);
    }
}
