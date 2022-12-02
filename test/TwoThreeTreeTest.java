import opgave.SearchTree;
import oplossing.Node233;
import oplossing.TwoThreeTree;

import static org.junit.jupiter.api.Assertions.*;

public class TwoThreeTreeTest implements SearchTreeTest {

    @Override
    public SearchTree<Integer> createTree() {
        return new TwoThreeTree<>();
    }

    @Override
    public void assertIntegrity(SearchTree<Integer> tree) {
        assertTrue(is23Tree((TwoThreeTree<Integer>) tree), "Boom is geen 2-3-boom.");
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

    private Integer depth;

    private boolean allNodesSameHeight(TwoThreeTree<Integer> tree) {
        this.depth = null;
        return allNodesSameHeightHulp(tree.root, 0);
    }

    private boolean allNodesSameHeightHulp(Node233<Integer> node, int level) {
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

        return allNodesSameHeightHulp(node.getLeftChild(), level + 1)
                && allNodesSameHeightHulp(node.getMiddleChild(), level + 1)
                && (node.rightValue == null || allNodesSameHeightHulp(node.getRightChild(), level + 1));
    }

    private boolean isLeaf(Node233<Integer> node){
        return node.getLeftChild() == null && node.getRightChild() == null && node.getMiddleChild() == null;
    }

}
