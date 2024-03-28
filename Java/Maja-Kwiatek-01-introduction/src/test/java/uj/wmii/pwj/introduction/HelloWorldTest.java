package uj.wmii.pwj.introduction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloWorldTest {

    private ByteArrayOutputStream out;

    @BeforeEach
    public void prepareOutput() {
        out = new ByteArrayOutputStream(512);
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    }

    @Test
    public void emptyParams() {
        HelloWorld.main(new String[0]);
        assertThat(out.toString()).isEqualToIgnoringNewLines("No input parameters provided");
    }

    @Test
    public void oneParam() {
        HelloWorld.main(new String[] {"testParam"});
        assertThat(out.toString()).isEqualTo("testParam\n");
    }

    @Test
    public void fourParams() {
        HelloWorld.main(new String[] {"one", "two", "three", "four"});
        assertThat(out.toString()).isEqualTo("one\ntwo\nthree\nfour\n");
    }
}
