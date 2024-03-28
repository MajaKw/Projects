package uj.wmii.pwj.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ListMergerTest {

    @Test
    void nullLists() {
        List<?> result = ListMerger.mergeLists(null, null);
        assertThat(result).isEmpty();
    }

    @Test
    void emptyLists() {
        List<?> result = ListMerger.mergeLists(Collections.emptyList(), new ArrayList<>());
        assertThat(result).isEmpty();
    }

    @Test
    void oneListOnly() {
        List<Object> result = ListMerger.mergeLists(Collections.emptyList(), List.of("a", "b", "c"));
        assertThat(result).containsExactly("a", "b", "c");
    }

    @Test
    void twoSimpleLists() {
        List<Object> result = ListMerger.mergeLists(List.of("x", "y", "z"), List.of("a", "b", "c"));
        assertThat(result).containsExactly("x", "a", "y", "b", "z", "c");
    }

    @Test
    void firstListLonger() {
        List<Object> result = ListMerger.mergeLists(List.of("x", "y", "z", "q", "w"), List.of("a", "b", "c"));
        assertThat(result).containsExactly("x", "a", "y", "b", "z", "c", "q", "w");
    }

    @Test
    void secondListLonger() {
        List<Object> result = ListMerger.mergeLists(List.of("x", "y", "z"), List.of("a", "b", "c", "d", "e", "f"));
        assertThat(result).containsExactly("x", "a", "y", "b", "z", "c", "d", "e", "f");
    }

    @Test
    void checkListNotModifable() {
        List<Object> result = ListMerger.mergeLists(List.of("x", "y", "z"), List.of("a", "b", "c", "d"));
        try {
            result.add("SHOULD NOT ADD ME");
            result.remove(2);
        } catch (Exception ex) {
            //do nothing
        }
        assertThat(result).containsExactly("x", "a", "y", "b", "z", "c", "d");
    }
}
