package oplossing;

import opgave.SearchTree;

import java.util.*;
import java.util.List;

public class TwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {

    private int size = 0;
    public Node233<E> root = new Node233<>(null, null); // Er is bij aanvang nog geen root node

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
        Node233<E> node = root;
        while (node != null && !node.hasValue(o)) {
            node = node.getChild(o);
        }
        return node != null;
    }

    @Override
    public boolean add(E o) {

        Stack<Node233<E>> stack = new Stack<>(); // Stack met de doorlopen nodes.
        Node233<E> node = root;
        while (node  != null && ! node.hasValue(o)) {
            stack.push(node);
            node = node.getChild(o); // daal af in de boom
        }

        if (node != null){ // Het element zit al in de boom.
            return false;
        }

        // Toevoegen van de waarde o. Dit maakt van de 2-3-boom een bijna-2-3-boom.
        Node233<E> newNode = new Node233<>(o, null);
        stack.peek().setChild(newNode); // Voeg de nieuwe node toe aan de laatst bezochte top.
        this.size += 1;

        // Doorloop de nodes terug in omgekeerde volgorde en herbalanceer zodat de boom terug geldig wordt.
        // Maak een binaire boom van de parent tot er een plaatsje vrij is om de tweede sleutel toe te voegen.
        // Wanneer dit plaatsje vrij is, percoleer je de huidige node naar boven.
        node = newNode;
        while (!stack.isEmpty() && stack.peek().hasRightValue()) {
            Node233<E> parent = stack.pop();
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
        Stack<Node233<E>> pad = new Stack<>(); // Pad tot aan de node met de te verwijderen waarde.

        // Zoek de node met de te verwijderen waarde.
        Node233<E> current = root;
        while (current != null && !current.hasValue(e)){
            pad.push(current);
            current = current.getChild(e);
        }

        if (current == null){ // De sleutel zat nog niet in de boom, en kan dus niet verwijderd worden.
            return false;
        }
        size -= 1; // Zal nu zeker verwijderd worden
        Node233<E> verwijderNode = current; // De node met de te verwijderen sleutel.

        // Als de node met de te verwijderen sleutel geen blad is, vervang de te verwijderen
        // waarde dan met zijn 'in-order successor'
        if (! verwijderNode.isLeaf()) { // Todo search in aparte functie zetten
            pad.push(current);
            current = (current.leftValue.compareTo(e) == 0) ? current.middleChild : current.rightChild;
            while (current != null) { // Zoek in order successor.
                pad.push(current); // vul pad verder aan van verwijderNode naar het vervang-blad.
                current = current.leftChild;
            }

            Node233<E> leaf = pad.pop();
            E successor = leaf.leftValue;

            // Vervang de te verwijderen sleutel door zijn successor.
            verwijderNode.replace(e, successor);
            leaf.leftValue = e; // Verwisseld.
            verwijderNode = leaf; // de leaf moet nu verwijderd worden
        }

        // verwijderNode is nu zeker weten een blad.
        assert verwijderNode.isLeaf(); // TODO weghalen

        // Verwijder de sleutel in het blad.
        if (verwijderNode.leftValue.compareTo(e) == 0){
            verwijderNode.leftValue = verwijderNode.rightValue;
        }
        verwijderNode.rightValue = null;

        if (verwijderNode.hasLeftValue()){ // Er waren twee nodes. De boom is dus nog steeds een geldige 2-3-boom
            return true; // Verwijderen gelukt.
        }


        // verwijderNode heeft maar één sleutel.
        // We proberen de node met zijn broers te re-distribueren. Is dit niet mogelijk, dan mergen we ze

        Node233<E> parent; // Parent van de leaf node
        while (!pad.isEmpty()) {
            parent = pad.pop();

            if (parent.redistribute(verwijderNode)) {
                return true; // Herdistributie is gelukt
            }

            // De siblings kunnen niet geherdistribueerd worden en zullen moeten mergen.
            if (parent.numberOfKeys() == 2) {
                parent.mergeIntoChild(verwijderNode);
                return true;
            }

            // er zijn maar twee sleutels over in deze deelboom, het probleem wordt naar boven geduwd.
            parent.geval2nodes(verwijderNode);

            verwijderNode = parent;
        }

        if (verwijderNode.middleChild != null){
            this.root = verwijderNode.middleChild;
        }

        return true;
    }

    @Override
    public void clear() {
        size = 0;
        this.root = new Node233<>(null, null); // thx garbage collector :)
    }

    @Override
    public Iterator<E> iterator() {
        List<E> valueList = new ArrayList<>();
        if (root.isEmpty()) {
            return valueList.iterator();
        }
        inorder(root, valueList);
        return valueList.listIterator();
    }

    private void inorder(Node233<E> node, List<E> list) {
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

    private void drawWithIndent(int indent, Node233<E> node, String type, StringBuilder stringBuilder) {
        if (node == null) {
            return;
        }

        stringBuilder.append("     |".repeat(indent)).append("-> ").append(type).append(" ").append(node).append("\n");

        drawWithIndent(indent + 1, node.leftChild  , "L", stringBuilder);
        drawWithIndent(indent + 1, node.middleChild, "M", stringBuilder);
        drawWithIndent(indent + 1, node.rightChild , "R", stringBuilder);

    }
}
