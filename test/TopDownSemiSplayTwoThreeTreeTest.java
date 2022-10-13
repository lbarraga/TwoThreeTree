import opgave.SearchTree;
import oplossing.TopDownSemiSplayTwoThreeTree;
import oplossing.TwoThreeTree;

public class TopDownSemiSplayTwoThreeTreeTest implements SearchTreeTest {
    @Override
    public SearchTree<Integer> createTree() {
        return new TopDownSemiSplayTwoThreeTree<>();
    }
}
