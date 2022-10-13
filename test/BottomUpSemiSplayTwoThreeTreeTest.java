import opgave.SearchTree;
import oplossing.BottomUpSemiSplayTwoThreeTree;

public class BottomUpSemiSplayTwoThreeTreeTest implements SearchTreeTest {
    @Override
    public SearchTree<Integer> createTree() {
        return new BottomUpSemiSplayTwoThreeTree<>();
    }
}
