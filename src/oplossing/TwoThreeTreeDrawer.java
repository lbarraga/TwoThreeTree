package oplossing;

public class TwoThreeTreeDrawer<E extends  Comparable<E>> {

    private final TwoThreeTree.Node<E> root;

    public TwoThreeTreeDrawer(TwoThreeTree.Node<E> root){
        this.root = root;
    }

    public void draw(){
        drawWithIndent(0, root, "");
    }

    private void drawWithIndent(int indent, TwoThreeTree.Node<E> node, String type) {
        if (node == null) {
            return;
        }

        printWithIndent(indent, node, type);

        drawWithIndent(indent + 1, node.leftChild  , "L");
        drawWithIndent(indent + 1, node.middleChild, "M");
        drawWithIndent(indent + 1, node.rightChild , "R");

    }

    private void printWithIndent(int indent, TwoThreeTree.Node<E> node, String type) {
        System.out.println("     |".repeat(indent) + "-> " + type + " " + node);
    }

    public static void main(String[] args) {
        TwoThreeTree.Node<Integer> n1 = new TwoThreeTree.Node<>(1, 10);
        TwoThreeTree.Node<Integer> n2 = new TwoThreeTree.Node<>(2, 11);
        TwoThreeTree.Node<Integer> n3 = new TwoThreeTree.Node<>(3, null);
        TwoThreeTree.Node<Integer> n4 = new TwoThreeTree.Node<>(4, 13);
        TwoThreeTree.Node<Integer> n5 = new TwoThreeTree.Node<>(5, 14);
        TwoThreeTree.Node<Integer> n6 = new TwoThreeTree.Node<>(6, 15);
        TwoThreeTree.Node<Integer> n7 = new TwoThreeTree.Node<>(7, 16);
        TwoThreeTree.Node<Integer> n8 = new TwoThreeTree.Node<>(8, 17);
        TwoThreeTree.Node<Integer> n9 = new TwoThreeTree.Node<>(9, 18);
        TwoThreeTree.Node<Integer> n10 = new TwoThreeTree.Node<>(10, 18);
        TwoThreeTree.Node<Integer> n11 = new TwoThreeTree.Node<>(11, 18);
        TwoThreeTree.Node<Integer> n12 = new TwoThreeTree.Node<>(12, 18);

        TwoThreeTreeDrawer<Integer> drawer = new TwoThreeTreeDrawer<>(n1);
        drawer.draw();
    }

}
