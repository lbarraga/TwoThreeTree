import opgave.SearchTree;
import opgave.samplers.Sampler;
import oplossing.Node;
import oplossing.TwoThreeTree;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public interface SearchTreeTest {

    SearchTree<Integer> createTree();

    @Test
    default void empty() {
        SearchTree<Integer> tree = createTree();
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
    }

    @Test
    default void addOne() {
        SearchTree<Integer>tree = createTree();

        assertFalse(tree.contains(1));
        tree.add(1);
        assertTrue(tree.contains(1));
        assertEquals(1, tree.size());
    }

    @Test
    default void removeOne() {
        SearchTree<Integer>tree = createTree();

        assertFalse(tree.contains(1));
        tree.add(1);
        assertTrue(tree.contains(1));
        tree.remove(1);
        assertFalse(tree.contains(1));
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
    }

    @Test
    default void addMultiple() {
        SearchTree<Integer> tree = createTree();
        int n = 5_000_000;
        for (int i = 0; i < n; i++) {
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < n; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
    }

    @Test
    default void addMultipleDescending() {
        SearchTree<Integer> tree = createTree();

        for (int i = 10; i >= 0; i--) {
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
        System.out.println(tree);
    }

    @Test
    default void addFewRandoms() {
        addRandoms(20, 100);
    }

    @Test
    default void addManyRandoms() {
        addRandoms(10_000, 100_000);
    }

    @Test
    default void addManyDuplicatesRandoms() {
        addRandoms(10_000, 100);
    }

    @Test
    default void addEvenMoreRandoms() {
        addRandoms(1_000_000, 10_000_000);
    }

    default ArrayList<Integer> randomArrayList(int n, int range)
    {
        ArrayList<Integer> list = new ArrayList<>();
        Random random = new Random();
        random.setSeed(1);

        for (int i = 0; i < n; i++)
        {
            list.add(random.nextInt(range));
        }
        return list;
    }

    @Test
    default void removeMultiple() {
        SearchTree<Integer>tree = createTree();

        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
            assertTrue(tree.remove(i), String.format("should change when removing %d", i));
            assertFalse(tree.contains(i), String.format("should not contain %d anymore", i));
        }
        assertEquals(0, tree.size(), "should be empty");
    }

    @Test
    default void myRemoveMultiple(){
        SearchTree<Integer>tree = createTree();

        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
        }
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
        System.out.println(tree);
        assertTrue(tree.remove(5), String.format("should change when removing %d", 5));
        System.out.println(tree);
        assertFalse(tree.contains(5), String.format("should not contain %d anymore", 5));
        // assertEquals(0, tree.size(), "should be empty");
    }

    @Test
    default void iterator() {
        SearchTree<Integer> tree = createTree();
        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
            expected.add(i);
        }
        assertIterableEquals(expected, tree);
    }

    @Test
    default void iteratorRandoms() {
        SearchTree<Integer> tree = createTree();
        List<Integer> expected = new ArrayList<>();

        for (int i = 10; i >= 0; i--) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
            expected.add(i);
        }
        expected.sort(Comparator.comparingInt(value -> value)); // sorteer de lijst.
        assertIterableEquals(expected, tree);
    }

    default void addRandoms(int n, int range) {
        SearchTree<Integer> tree = createTree();
        List<Integer> randoms = randomArrayList(n, range);
        for (Integer random : randoms) {
            tree.add(random);
        }

        for (Integer random : randoms) {
            assertTrue(tree.contains(random), String.format("should contain %d", random));
        }
    }
}
