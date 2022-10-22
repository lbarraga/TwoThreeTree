package oplossing;

import opgave.SearchTree;

import java.util.Iterator;

public class TwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {

    private int size = 0;
    private Node<E> root = null; // Er is bij aanvang nog geen root node

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
        while (node != null && node.getLeftValue() != o && node.getRightValue() != o) {

            if (node.getLeftValue().compareTo(o) < 0){ // Te zoeken waarde zit mogelijk in de linker deelboom.
                node = node.getLeftChild();
            } else if (node.getRightValue().compareTo(o) > 0) { // Mogelijks in de rechter deelboom.
                node = node.getRightChild();
            } else {
                node = node.getMiddleChild(); // Mogelijks in de middelste deelboom.
            }

        }
        return node != null;
    }

    @Override
    public boolean add(E o) {
        Node<E> node = root;
        return false;
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

        private final E leftValue;
        private final E rightValue;

        private Node<E> leftChild;
        private Node<E> middleChild;
        private Node<E> rightChild;


        public Node(E leftValue, E rightValue) {
            this.leftValue = leftValue;
            this.rightValue = rightValue;
        }

        public E getLeftValue() {
            return leftValue;
        }

        public E getRightValue() {
            return rightValue;
        }

        public void setLeftChild(Node<E> leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(Node<E> rightChild) {
            this.rightChild = rightChild;
        }

        public void setMiddleChild(Node<E> middleChild) {
            this.middleChild = middleChild;
        }

        public Node<E> getLeftChild() {
            return leftChild;
        }

        public Node<E> getMiddleChild() {
            return middleChild;
        }

        public Node<E> getRightChild() {
            return rightChild;
        }

        @Override
        public String toString(){
            return "(" + leftValue + "|" + rightValue + ")";
        }
    }
}
