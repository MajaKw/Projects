package uj.wmii.pwj.introduction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

class BannerTest {

    @ParameterizedTest(name = "{index} {0}")
    @MethodSource("toBannerInput")
    void toBanner(String input, List<String> expected) {
        String[] result = new Banner().toBanner(input);
        for (int i = 0; i < expected.size(); i++) {
            assertThat(result[i].strip()).isEqualTo(expected.get(i).strip());
        }
    }

    @Test
    void toBannerNull() {
        String[] result = new Banner().toBanner(null);
        assertThat(result).isEmpty();
    }


    static Stream<Arguments> toBannerInput() {
        return Stream.of(
            of("Ala", readBanner("ala")),
            of("ABCDEFGHIJKLMNOPQRSTUWVXYZ", readBanner("alfabet")),
            of("abcdefGhijklMnopqrstuwvxYz", readBanner("alfabet")),
            of("Ala ma kota", readBanner("ala-ma-kota"))
        );
    }

    private static List<String> readBanner(String name) {
        var input = BannerTest.class.getResourceAsStream("banners/" + name + ".txt");
        var br = new BufferedReader(new InputStreamReader(input));
        return br.lines().collect(Collectors.toList());
    }

}
