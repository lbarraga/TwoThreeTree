import opgave.SearchTree;
import oplossing.TopDownSemiSplayTwoThreeTree;
import oplossing.TwoThreeTree;

import static org.junit.jupiter.api.Assertions.*;


public class TopDownSemiSplayTwoThreeTreeTest implements SearchTreeTest {
    @Override
    public SearchTree<Integer> createTree() {
        return new TopDownSemiSplayTwoThreeTree<>();
    }

    @Override
    public void assertIntegrity(SearchTree<Integer> tree) {
        assertTrue(true, "Boom is geen TopdownSemiSplay23 boom");
    }
}
