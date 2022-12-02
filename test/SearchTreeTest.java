import opgave.SearchTree;
import opgave.samplers.Sampler;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public interface SearchTreeTest {


    Random RG = new Random(223222);

    SearchTree<Integer> createTree();
    void assertIntegrity(SearchTree<Integer> tree);

    @Test
    default void empty() {
        SearchTree<Integer> tree = createTree();
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
        assertIntegrity(tree);
    }

    @Test
    default void addOne() {
        SearchTree<Integer>tree = createTree();

        assertFalse(tree.contains(1));
        tree.add(1);
        assertTrue(tree.contains(1));
        assertEquals(1, tree.size());
        assertIntegrity(tree);
    }

    @Test
    default void removeOnEmptyAndAddOne() {
        SearchTree<Integer> tree = createTree();
        int n = 100;
        for (int i = 0; i < n; i++) {
            assertFalse(tree.remove(i));
        }
        for (int i = 0; i < n; i++) {
            assertFalse(tree.contains(i), String.format("should not contain %d", i));
        }
        assertIntegrity(tree);
        assertTrue(tree.add(2));
        assertTrue(tree.contains(2));
        assertIntegrity(tree);
    }

    @Test
    default void addMultiple() {
        SearchTree<Integer> tree = createTree();
        int n = 6;
        for (int i = 0; i < n; i++) {
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < n; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
        assertIntegrity(tree);
    }

    @Test
    default void addAndContainsMixed() {
        SearchTree<Integer> tree = createTree();
        int n = 100_000;
        for (int i = 0; i < n; i++) {
            assertTrue(tree.add(i));
        }
        for (int i = 0; i < n; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
        assertIntegrity(tree);
    }

    @Test
    default void addList() {
        SearchTree<Integer> tree = createTree();
        List<Integer> l = List.of(5, 1, 3, 2);
        for (Integer e : l) {
            assertTrue(tree.add(e));
        }
        for (Integer e : l) {
            assertTrue(tree.contains(e), String.format("should contain %d", e));
        }
        assertIntegrity(tree);
    }

    @Test
    default void addMultipleDescending() {
        SearchTree<Integer> tree = createTree();
        int n = 100;
        for (int i = n; i >= 0; i--) {
            assertTrue(tree.add(i));
            assertTrue(tree.contains(i), String.format("should contain %d :)", i));
        }
        for (int i = 0; i < n; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
        assertIntegrity(tree);
    }

    @Test
    default void addFewRandoms() {
        addRandoms(100);
    }

    @Test
    default void addManyDuplicatesRandoms() {
        addRandoms(10_000);
    }

    @Test
    default void addManyRandoms() {
        addRandoms(20_000);
    }

    @Test
    default void addEvenMoreRandoms() {
        addRandoms(100_000);
    }

    @Test
    default void removeOne() {
        SearchTree<Integer>tree = createTree();
        assertIntegrity(tree);
        assertFalse(tree.contains(1));
        tree.add(1);
        assertTrue(tree.contains(1));
        tree.remove(1);
        assertFalse(tree.contains(1));
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
        assertTrue(tree.add(1));
        assertIntegrity(tree);
    }

    @Test
    default void removeFewRandoms() {
        for (int i = 0; i < 100; i++) {
            Random myRG = new Random(i);
            removeRandoms(100, myRG);
        }
    }

    @Test
    default void removeSame() {
        SearchTree<Integer> tree = createTree();
        for (int i = 0; i < 5; i++) {
            assertFalse(tree.remove(i));
            assertFalse(tree.contains(i));
            assertEquals(0, tree.size());
        }
        for (int i = 5; i < 10; i++) {
            assertTrue(tree.add(i));
        }
        for (Integer integer : List.of(7, 6, 9, 5, 8)) {
            assertTrue(tree.remove(integer));
        }
        assertIntegrity(tree);
        assertEquals(tree.size(), 0);
    }

    @Test
    default void removeList() {
        SearchTree<Integer> tree = createTree();
        List<Integer> l1 = List.of(9, 10, 13, 0, 14);
        for (Integer e : l1) {
            assertTrue(tree.add(e));
        }
        for (Integer e : l1) {
            assertTrue(tree.contains(e), String.format("should contain %d", e));
        }
        List<Integer> l2 = List.of(0, 9, 14, 13, 10);
        for (Integer integer : l2) {
            assertTrue(tree.remove(integer), String.format("Should change when removing %d", integer));
            assertFalse(tree.contains(integer), String.format("should not contain %d", integer));
        }
        for (Integer integer : l2) {
            assertFalse(tree.contains(integer), String.format("should not contain %d", integer));
        }
        assertTrue(tree.isEmpty(), "tree should be empty.");
        assertIntegrity(tree);
    }

    @Test
    default void RemoveManyDuplicatesRandoms() {
        removeRandoms(100, RG);
    }

    @Test
    default void myRemoveMultiple(){
        SearchTree<Integer>tree = createTree();

        for (int i = 1; i < 16; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
        }
        for (int i = 1; i < 16; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
        }
        assertTrue(tree.remove(6), String.format("should change when removing %d", 5));
        assertFalse(tree.contains(6), String.format("should not contain %d anymore", 5));
        assertIntegrity(tree);
    }

    @Test
    default void removeManyRandoms() {
        removeRandoms(1000, RG);
    }

    @Test
    default void removeMultiple() {
        SearchTree<Integer>tree = createTree();

        for (int i = 0; i < 100_000; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
        }
        for (int i = 0; i < 100_000; i++) {
            assertTrue(tree.contains(i), String.format("should contain %d", i));
            assertTrue(tree.remove(i), String.format("should change when removing %d", i));
            assertFalse(tree.contains(i), String.format("should not contain %d anymore", i));
        }
        assertEquals(0, tree.size(), "should be empty");
        assertIntegrity(tree);
    }

    @Test
    default void removeEvenMoreRandoms() {
        removeRandoms(1000, RG);
    }

    default List<Integer> randomArrayList(int n) {
        return new Sampler(RG, n).sample(n);
    }

    @Test
    default void iterator() {
        SearchTree<Integer> tree = createTree();
        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
            expected.add(i);
        }
        assertIterableEquals(expected, tree);
        assertIntegrity(tree);
    }

    @Test
    default void emptyIterator() {
        SearchTree<Integer> tree = createTree();
        assertEquals(tree.iterator(), Collections.emptyIterator(), "iterator is not empty");
    }

    @Test
    default void iteratorRandom() {
        Sampler sampler = new Sampler(new Random(), 100_000);
        SearchTree<Integer> tree = createTree();
        List<Integer> expected = new ArrayList<>();
        for (int i : sampler.getElements()) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
            expected.add(i);
        }
        assertIterableEquals(expected.stream().sorted().toList(), tree);
        assertIntegrity(tree);
    }

    @Test
    default void iteratorRandoms() {
        SearchTree<Integer> tree = createTree();
        List<Integer> expected = new ArrayList<>();
        int n = 10_000;
        for (int i = n; i > 0; i--) {
            assertTrue(tree.add(i), String.format("should change when adding %d", i));
            expected.add(i);
        }
        assertEquals(tree.size(), n, "Size should be " + n);
        expected.sort(Comparator.comparingInt(value -> value)); // sorteer de lijst.
        assertIterableEquals(expected, tree);
        assertIntegrity(tree);
    }

    @Test
    default void ultimateTest() {
        SearchTree<Integer> tree = createTree();
        Set<Integer> hashSet = new HashSet<>();
        int n = 1_000;
        Sampler sampler = new Sampler(RG, n);
        List<Integer> waardes = sampler.getElements();
        ArrayList<Integer> l = new ArrayList<>();
        IntStream.range(0, n).forEach(value -> l.add(RG.nextInt(2))); // O voeg toe 1 verwijder 2 clear
        int size = 0;
        for (int i = 0; i < n; i++) {
            int operation = l.get(i);
            int value = waardes.get(i);
            if (operation == 0){
                if(tree.add(value)){
                    size += 1;
                }
                hashSet.add(value);

            } else if (operation == 1) {
                hashSet.remove(value);
                if (tree.remove(value)){
                    size -= 1;
                }
            } else if (operation == 2){
                size = 0;
                hashSet.clear();
                tree.clear();
            }
            assertEquals(size, tree.size());
            assertIterableEquals(hashSet.stream().sorted().toList(), tree);
            assertIntegrity(tree);
        }
    }

    default void addRandoms(int n) {
        SearchTree<Integer> tree = createTree();
        List<Integer> randoms = randomArrayList(n);
        for (Integer random : randoms) {
            tree.add(random);
        }
        for (Integer random : randoms) {
            assertTrue(tree.contains(random), String.format("should contain %d", random));
        }
        assertIntegrity(tree);
    }

    default void removeRandoms(int n, Random RG) {
        SearchTree<Integer> tree = createTree();
        List<Integer> randoms = randomArrayList(n);
        for (Integer random : randoms) {
            tree.add(random);
        }

        Collections.shuffle(randoms, RG);
        for (int i = 0; i < randoms.size() / 2; i++) {
            Integer random = randoms.get(i);
            tree.remove(random);
            assertFalse(tree.contains(random), String.format("Should not contain %d", random));
        }
        assertIntegrity(tree);
    }
}