import opgave.SearchTree;
import oplossing.TwoThreeTree;

public class TwoThreeTreeTest implements SearchTreeTest {
    @Override
    public SearchTree<Integer> createTree() {
        return new TwoThreeTree<>();
    }
}
