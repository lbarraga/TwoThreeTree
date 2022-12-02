package oplossing;

import java.util.*;
import java.util.List;

public class TwoThreeTree<E extends Comparable<E>> extends BaseTwoThreeTree<E> {

    public Node233<E> root = new Node233<>(null, null); // Er is bij aanvang nog geen root node

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

        Stack<Node233<E>> pad = find(root,o);
        Node233<E> node = pad.pop();

        if (node != null){ // Het element zit al in de boom.
            return false;
        }

        // Toevoegen van de waarde o. Dit maakt van de 2-3-boom een bijna-2-3-boom.
        Node233<E> newNode = new Node233<>(o, null);
        pad.peek().setChild(newNode); // Voeg de nieuwe node toe aan de laatst bezochte top.
        this.size += 1;

        // Doorloop de nodes terug in omgekeerde volgorde en herbalanceer zodat de boom terug geldig wordt.
        // Maak een binaire boom van de parent tot er een plaatsje vrij is om de tweede sleutel toe te voegen.
        // Wanneer dit plaatsje vrij is, percoleer je de huidige node naar boven.
        node = newNode;
        while (!pad.isEmpty() && pad.peek().hasRightValue()) {
            Node233<E> parent = pad.pop();
            parent.convertToBinary(o); // Maak een kleine binaire boom van de node
            node = parent;
        }

        if (!pad.isEmpty()) { // While is gestopt omdat er een plaats vrij is in de rechter value van de parent
            node.percolateUp(pad.pop(), o);
        }

        return true;
    }

    public Stack<Node233<E>> find(Node233<E> current, E e) {
        Stack<Node233<E>> pad = new Stack<>(); // Pad tot aan de node met de te verwijderen waarde.
        // Zoek de node met de te verwijderen waarde.
        while (current != null && !current.hasValue(e)){
            pad.push(current);
            current = current.getChild(e);
        }
        pad.push(current);
        return pad;
    }

    @Override
    public boolean remove(E e) {
        Stack<Node233<E>> pad = find(root, e);
        Node233<E> current = pad.pop();

        if (current == null){ // De sleutel zat nog niet in de boom, en kan dus niet verwijderd worden.
            return false;
        }
        size -= 1; // Zal nu zeker verwijderd worden
        Node233<E> verwijderNode = current; // De node met de te verwijderen sleutel.

        // Als de node met de te verwijderen sleutel geen blad is, vervang de te verwijderen
        // waarde dan met zijn 'in-order successor'
        if (! verwijderNode.isLeaf()) { // Todo search in aparte functie zetten
            pad.push(current);
            current = (current.leftValue.compareTo(e) == 0) ? current.getMiddleChild() : current.getRightChild();
            while (current != null) { // Zoek in order successor.
                pad.push(current); // vul pad verder aan van verwijderNode naar het vervang-blad.
                current = current.getLeftChild();
            }

            Node233<E> leaf = pad.pop();
            E successor = leaf.leftValue;

            // Vervang de te verwijderen sleutel door zijn successor.
            verwijderNode.replace(e, successor);
            leaf.leftValue = e; // Verwisseld.
            verwijderNode = leaf; // de leaf moet nu verwijderd worden
        }

        // verwijderNode is nu zeker weten een blad.

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

        if (verwijderNode.getMiddleChild() != null){
            this.root = verwijderNode.getMiddleChild();
        }

        return true;
    }

    @Override
    public void clear() {
        size = 0;
        this.root = new Node233<>(null, null); // thx garbage collector :)
    }

    @Override
    protected Node<E> getRoot() {
        return this.root;
    }
}
