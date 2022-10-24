package oplossing;

public class TwoThreeTreeDrawer<E extends  Comparable<E>> {

    private final Node<E> root;

    public TwoThreeTreeDrawer(Node<E> root){
        this.root = root;
    }

    public void draw(){
        drawWithIndent(0, root, "");
    }

    private void drawWithIndent(int indent, Node<E> node, String type) {
        if (node == null) {
            return;
        }

        printWithIndent(indent, node, type);

        drawWithIndent(indent + 1, node.leftChild  , "L");
        drawWithIndent(indent + 1, node.middleChild, "M");
        drawWithIndent(indent + 1, node.rightChild , "R");

    }

    private void printWithIndent(int indent, Node<E> node, String type) {
        System.out.println("     |".repeat(indent) + "-> " + type + " " + node);
    }

    public static void main(String[] args) {
        Node<Integer> n1 = new Node<>(1, 10);
        Node<Integer> n2 = new Node<>(2, 11);
        Node<Integer> n3 = new Node<>(3, null);
        Node<Integer> n4 = new Node<>(4, 13);
        Node<Integer> n5 = new Node<>(5, 14);
        Node<Integer> n6 = new Node<>(6, 15);
        Node<Integer> n7 = new Node<>(7, 16);
        Node<Integer> n8 = new Node<>(8, 17);
        Node<Integer> n9 = new Node<>(9, 18);
        Node<Integer> n10 = new Node<>(10, 18);
        Node<Integer> n11 = new Node<>(11, 18);
        Node<Integer> n12 = new Node<>(12, 18);

        TwoThreeTreeDrawer<Integer> drawer = new TwoThreeTreeDrawer<>(n1);
        drawer.draw();
    }

}
