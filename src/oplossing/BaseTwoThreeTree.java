package oplossing;

import opgave.SearchTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseTwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {

    protected int size = 0;


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public int maxDepth(){
        return maxDepthHulp(getRoot());
    }

    private int maxDepthHulp(Node<E> node) {
        if (node == null) {
            return 0;
        }
        int links  = 1 + maxDepthHulp(node.getLeftChild());
        int midden = 1 + maxDepthHulp(node.getMiddleChild());
        int rechts = 1 + maxDepthHulp(node.getRightChild());
        return Math.max(links, Math.max(midden, rechts));
    }

    protected abstract Node<E> getRoot();

    /**
     * Eigenlijk enkel om te debuggen, maar vond het wel mooie code.
     * !! Dit stuk code heb ik met een aantal mensen gedeeld.
     * @return tree als string
     */
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        drawWithIndent(0, getRoot(), "", builder);
        return builder.toString();
    }

    private void drawWithIndent(int indent, Node<E> node, String type, StringBuilder stringBuilder) {
        if (node == null) {
            return;
        }

        stringBuilder.append("     |".repeat(indent)).append("-> ").append(type).append(" ").append(node).append("\n");

        drawWithIndent(indent + 1, node.getLeftChild(), "L", stringBuilder);
        drawWithIndent(indent + 1, node.getMiddleChild(), "M", stringBuilder);
        drawWithIndent(indent + 1, node.getRightChild(), "R", stringBuilder);

    }

    @Override
    public Iterator<E> iterator() {
        Node<E> root = getRoot();
        List<E> valueList = new ArrayList<>();
        if (root  == null || root.isEmpty()) {
            return Collections.emptyIterator();
        }
        inorder(root, valueList);
        return valueList.listIterator();
    }

    private void inorder(Node<E> node, List<E> list) {
        if (node == null){
            return;
        }
        inorder(node.getLeftChild(), list);
        list.add(node.leftValue);
        inorder(node.getMiddleChild(), list);
        if (node.hasRightValue()) {
            list.add(node.rightValue);
            inorder(node.getRightChild(), list);
        }

    }

}
