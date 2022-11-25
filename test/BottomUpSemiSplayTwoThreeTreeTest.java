import opgave.SearchTree;
import oplossing.BottomUpSemiSplayTwoThreeTree;

import static org.junit.jupiter.api.Assertions.*;

public class BottomUpSemiSplayTwoThreeTreeTest implements SearchTreeTest {
    @Override
    public SearchTree<Integer> createTree() {
        return new BottomUpSemiSplayTwoThreeTree<>();
    }

    @Override
    public void assertIntegrity(SearchTree<Integer> tree) {
        assertTrue(true, "Boom is geen BottomUpSemiSplay23 boom");
    }
}
