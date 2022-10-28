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

        if (!stack.isEmpty()) { // While is gestopt omdat er een plaats vrij is in de rechter value van de parent
            node.percolateUp(stack.pop(), o);
        }

        return true;
    }

    @Override
    public boolean remove(E e) {
        // Zoek de value op.
        Node<E> current = root;
        Stack<Node<E>> stack = new Stack<>();
        while (current != null && !current.hasValue(e)){
            stack.push(current);
            current = current.getChild(e);
        }

        if (current == null){ // waarde zat er nog niet in, niets wordt verwijderd
            return false;
        }

        Node<E> verwijderNode = current;

        // Vervang current node door de kleinste value in de rechter of middelste boom
        Node<E> parent = current;
        current = (current.leftValue.compareTo(e) == 0) ? current.middleChild : current.rightChild;
        while (current.leftChild != null){
            parent = current;
            current = current.leftChild; // TODO hernoemen naar leaf of zo
        }

        // Verwijder de waarde in het blad
        E kleinsteInDeelBoom = current.leftValue;
        current.leftValue = null;
        current.swapValues(); // rechter waarde naar links

        // Vervang de waarde van het blad in de vervang-node
        if (verwijderNode.leftValue.compareTo(e) == 0){
            verwijderNode.leftValue = kleinsteInDeelBoom;
        } else {
            verwijderNode.rightValue = kleinsteInDeelBoom;
        }

        // OK als er twee waardes in het blad zaten
        if (current.hasLeftValue()){
            return true;
        }

        // Het blad is volledig leeg. Probeer uit de parent node naar onder te duwen
        if (parent.hasRightValue() && parent.rightValue.compareTo(kleinsteInDeelBoom) > 0){
            current.leftValue = parent.leftValue;
            parent.leftValue = parent.leftChild.rightValue;
        } else {
            current.leftValue = parent.rightValue;
            parent.rightValue = parent.middleChild.rightValue;
        }

        if (!parent.hasLeftValue()) {
            parent.swapValues();
        }

        // merge blad met zijn broer aan linkerkant
        parent.leftChild.rightValue = current.leftValue;
        // ook nog de subnodes meedoen
        parent.middleChild = parent.rightChild;
        parent.rightChild = null;

        if (!parent.hasLeftValue()){ // geval met 2 waarden, schuif het probleem door
            // TODO
        }



        return true;
    }

    @Override
    public void clear() {
        this.root = null; // thx garbage collector :)
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
        if (node.hasRightValue()) {
            list.add(node.rightValue);
            inorder(node.rightChild, list);
        }

    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        drawWithIndent(0, root, "", builder);
        return builder.toString();
    }

    private void drawWithIndent(int indent, Node<E> node, String type, StringBuilder stringBuilder) {
        if (node == null) {
            return;
        }

        stringBuilder.append("     |".repeat(indent)).append("-> ").append(type).append(" ").append(node).append("\n");

        drawWithIndent(indent + 1, node.leftChild  , "L", stringBuilder);
        drawWithIndent(indent + 1, node.middleChild, "M", stringBuilder);
        drawWithIndent(indent + 1, node.rightChild , "R", stringBuilder);

    }
}
