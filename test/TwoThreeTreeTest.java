import opgave.SearchTree;
import oplossing.TwoThreeTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TwoThreeTreeTest implements SearchTreeTest {

    @Override
    public SearchTree<Integer> createTree() {
        return new TwoThreeTree<>();
    }

    @Override
    public void addRandoms(int n, int range) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();
        List<Integer> randoms = randomArrayList(n, range);
        for (Integer random : randoms) {
            tree.add(random);
        }

        for (Integer random : randoms) {
            assertTrue(tree.contains(random), String.format("should contain %d", random));
        }
        assertTrue(is23Tree(tree), "Tree is not a 23Tree");
    }

    @Override
    public boolean isValidTree(SearchTree<Integer> tree) {
        TwoThreeTree<Integer> t2 = (TwoThreeTree<Integer>) tree;
        return isSorted(t2) && allNodesSameHeight(t2);
    }

    public boolean is23Tree(TwoThreeTree<Integer> tree){
        return isSorted(tree) && allNodesSameHeight(tree);
    }

    private boolean isSorted(TwoThreeTree<Integer> tree) {
        Integer prev = null;
        for (Integer i : tree){
            if (prev != null && i <= prev){
                return false;
            }
            prev = i;
        }
        return true;
    }

    private static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    private Integer depth;

    private boolean allNodesSameHeight(TwoThreeTree<Integer> tree) {
        this.depth = null;
        return allNodesSameHeightHulp(tree.root, 0);
    }

    private boolean allNodesSameHeightHulp(TwoThreeTree.Node<?> node, int level) {
        if (node == null) {
            return true;
        }

        if (isLeaf(node)) {
            if (this.depth == null) {
                this.depth = level;
                return true;
            }
            return level == depth;
        }

        return allNodesSameHeightHulp(node.leftChild, level + 1)
                && allNodesSameHeightHulp(node.middleChild, level + 1)
                && (node.rightValue == null || allNodesSameHeightHulp(node.rightChild, level + 1));
    }

    private boolean isLeaf(TwoThreeTree.Node<?> node){
        return node.leftChild == null && node.rightChild == null && node.middleChild == null;
    }

}
