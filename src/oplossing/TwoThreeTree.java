package oplossing;

import opgave.SearchTree;

import java.util.*;

public class TwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {

    private int size = 0;
    public Node<E> root = new Node<>(null, null); // Er is bij aanvang nog geen root node

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
        while (node != null && !node.hasValue(o)) {
            node = node.getChild(o);
        }
        return node != null;
    }

    @Override
    public boolean add(E o) {

        Stack<Node<E>> stack = new Stack<>(); // Stack met de doorlopen nodes.
        Node<E> node = root;
        while (node  != null && ! node.hasValue(o)) {
            stack.push(node);
            node = node.getChild(o); // daal af in de boom
        }

        if (node != null){ // Het element zit al in de boom.
            return false;
        }

        // Toevoegen van de waarde o. Dit maakt van de 2-3-boom een bijna-2-3-boom.
        Node<E> newNode = new Node<>(o, null);
        stack.peek().setChild(newNode); // Voeg de nieuwe node toe aan de laatst bezochte top.
        this.size += 1;

        // Doorloop de nodes terug in omgekeerde volgorde en herbalanceer zodat de boom terug geldig wordt.
        // Maak een binaire boom van de parent tot er een plaatsje vrij is om de tweede sleutel toe te voegen.
        // Wanneer dit plaatsje vrij is, percoleer je de huidige node naar boven.
        node = newNode;
        while (!stack.isEmpty() && stack.peek().hasRightValue()) {
            Node<E> parent = stack.pop();
            parent.convertToBinary(o); // Maak een kleine binaire boom van de node
            node = parent;
        }

        if (!stack.isEmpty()) {
            node.percolateUp(stack.pop(), o);
        }

        return true;
    }

    public boolean addRecursive(E o) {
        Node<E> newNode = new Node<>(o, null);
        return addRecursiveHulp(root, null, newNode);
    }

    public boolean addRecursiveHulp(Node<E> to, Node<E> parent, Node<E> newNode){
        if (to == null){
            parent.setChild(newNode);
            return true;
        }
        addRecursiveHulp(to.getChild(newNode.leftValue), to, newNode);
        if (parent != null && ! parent.hasRightValue()){
            to.percolateUp(parent, newNode.leftValue);
            return true;
        }
        parent.convertToBinary(newNode.leftValue);
        return true;
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
        List<E> valueList = new ArrayList<>();
        inorder(root, valueList);
        return valueList.listIterator();
    }

    private void inorder(Node<E> node, List<E> list) {
        if (node == null){
            return;
        }
        inorder(node.leftChild, list);
        list.add(node.leftValue);
        inorder(node.middleChild, list);
        if (node.rightValue != null) {
            list.add(node.rightValue);
            inorder(node.rightChild, list);
        }

    }
}
