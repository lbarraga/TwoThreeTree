package oplossing;

import opgave.SearchTree;

import java.util.Iterator;
import java.util.Stack;

public class TwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {

    private int size = 0;
    public Node<E> root = null; // Er is bij aanvang nog geen root node

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E o) {
        Node<E> node = root;
        while (node != null && node.leftValue != o && node.rightValue != o) {
            node = node.getChild(o);
        }
        return node != null;
    }

    @Override
    public boolean add(E o) {
        this.size += 1;
        Stack<Node<E>> stack = new Stack<>();
        Node<E> newNode = new Node<>(o, null);
        Node<E> node = root;

        if (root == null){
            this.root = newNode;
            return true;
        }
        // Stack met de doorlopen nodes
        while (node  != null) {
            stack.push(node);
            node = node.getChild(o);
        }

        // Toevoegen van de waarde o. Dit maakt van de 2-3-boom een bijna-2-3-boom
        node = stack.peek();
        if (node.leftValue != null && node.leftValue.compareTo(o) > 0) {
            node.leftChild = newNode;
            //System.out.println(newNode + " is nu het linker kind van " + node);
        } else if (node.rightValue != null && node.rightValue.compareTo(o) < 0) {
            node.rightChild = newNode;
            //System.out.println(newNode + " is nu het rechter kind van " + node);
        } else {
            node.middleChild = newNode;
            //System.out.println(newNode + " is nu het middel kind van " + node);
        }
        stack.push(newNode);
        // System.out.println(stack);
        // doorloop de nodes terug in omgekeerde volgorde en herbalanceer zodat de boom terug geldig wordt.
        while (stack.size() > 1) {
            node = stack.pop();
            Node<E> parent = stack.peek();
            if (parent.leftValue == null || parent.rightValue == null) {
                parent.rightValue = node.leftValue;
                parent.middleChild = node.leftChild;
                parent.rightChild = node.middleChild;
                // System.out.println("in");
                break;
            }
            // Maak een kleine binaire boom van de node
            Node<E> linkerNode = new Node<>(parent.leftValue, null);
            linkerNode.leftChild = parent.leftChild;
            linkerNode.middleChild = parent.middleChild;
            parent.leftValue = parent.rightValue;
            parent.rightValue = null;
            parent.leftChild = linkerNode;
            parent.middleChild = parent.rightChild;
            parent.rightChild = null;
        }
        return true;
    }

    public static void main(String[] args) {
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();
        tree.add(0);
        TwoThreeTreeDrawer<Integer> drawer = new TwoThreeTreeDrawer<>(tree.root);
        //drawer.draw();
        long start = System.currentTimeMillis();
        for (int i = 1; i < 100_000_000; i++) {
            //System.out.println("==================== stap " + i + " ====================");
            tree.add(i);
            // drawer.draw();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Override
    public boolean remove(E e) {
        return false;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    public static class Node<E extends Comparable<E>> {

        public E leftValue;
        public E rightValue;

        public Node<E> leftChild;
        public Node<E> middleChild;
        public Node<E> rightChild;


        public Node(E leftValue, E rightValue) {
            this.leftValue = leftValue;
            this.rightValue = rightValue;
        }

        public Node<E> getChild(E o) {
            if (leftValue != null && leftValue.compareTo(o) > 0) {
                return leftChild;
            }
            if (rightValue != null && rightValue.compareTo(o) < 0) {
                return rightChild;
            }
            return middleChild;
        }

        @Override
        public String toString(){
            return "(" + leftValue + "|" + rightValue + ")";
        }
    }
}
