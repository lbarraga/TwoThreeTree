package oplossing;

import opgave.SearchTree;
import oplossing.benchmarks.TwoThreeTreeBenchMarker;

import java.util.*;

public class BottomUpSemiSplayTwoThreeTree<E extends  Comparable<E>> implements SearchTree<E> {

    public int size = 0;
    public Ss233Node<E> root = null;

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E o) {
        Stack<Ss233Node<E>> splayPad = new Stack<>();
        Ss233Node<E> node = root;
        while (node != null && !node.hasValue(o)) {
            splayPad.push(node);
            node = node.getChild(o);
        }
        splay(splayPad);
        return node != null;
    }

    @Override
    public boolean add(E o) {
        Stack<Ss233Node<E>> stack = new Stack<>(); // Stack met de doorlopen nodes.
        Ss233Node<E> node = root;
        while (node  != null && ! node.hasValue(o)) {
            stack.push(node);
            node = node.getChild(o); // daal af in de boom
        }

        if (node != null){ // Het element zit al in de boom.
            return false;
        }
        this.size += 1;

        // Toevoegen van de waarde o.
        if (root == null) {
            root = new Ss233Node<>(o, null);
            return true;
        }
        if (!stack.peek().hasRightValue() && stack.peek().leftValue.compareTo(o) < 0) {
            stack.peek().rightValue = o;
        } else {
            Ss233Node<E> newNode = new Ss233Node<>(o, null);
            stack.peek().setChild(newNode); // Voeg de nieuwe node toe aan de laatst bezochte top.
            if (stack.peek().hasRightValue() && stack.peek().leftChild == newNode) {
                stack.peek().redistribute2();
                System.out.println(2);
            } else if (stack.peek().rightChild == newNode) {
                stack.peek().redistribute1();
                System.out.println(1);
            } else if (stack.peek().middleChild == newNode) {
                stack.peek().redistribute3();
                System.out.println(3);
            }
        }
        System.out.println(stack);
        // Splay de bezochte toppen van onder naar boven.
        splay(stack);
        return true;
    }

    /**
     * Voer een splay-bewerking uit op een meegegeven splay pad
     * @param splayPad het pad waarover gesplayed moet worden
     */
    private void splay(Stack<Ss233Node<E>> splayPad) {
        while (splayPad.size() >= 3) {
            //System.out.println("splay");
            Ss233Node<E> third = splayPad.pop();
            Ss233Node<E> second = splayPad.pop();
            Ss233Node<E> first = splayPad.pop();
            // parent wordt gebruikt om later terug aan het pad te kunnen hechten
            Ss233Node<E> parent = safePop(splayPad);

            splayOne(first, second, third, parent);
        }
    }

    /**
     * Globaal idee van splay-bewerking:
     * 1. Haal alle waarden (3 tot 6) uit de drie toppen en stop ze in een nieuwe toppen structuur:
     *           [ B D ]
     *          /   |   \
     *        [A]  [C]  [E F]
     * 2. Voeg de kinderen van de toppen in volgorde toe aan [A], [C] en [E F]
     * @param first de eerste top in het splay pad van 3 toppen groot
     * @param second de tweede top
     * @param third de derde top
     * @param parent de parent van de eerste top, gebruikt om de nieuwe toppen structuur terug aan de boom te hechten.
     */
    public void splayOne(Ss233Node<E> first, Ss233Node<E> second, Ss233Node<E> third, Ss233Node<E> parent) {
        Queue<E> values = new PriorityQueue<>(); // Gebruikt om de waardes op volgorde uit de toppen te halen.
        // gebruikt om kinderen uit de toppen te halen (stack om null-waarden toe te laten)
        Stack<Ss233Node<E>> children = new Stack<>();

        // Haal de kinderen (geen null kinderen en geen kind dat gelijk is aan first, second of third)
        // en stop ze in children. Terwijl de toppen bezocht worden, steek ook de sleutels in values
        extractFromNodes(first, second, third, children, values);

        // De nieuwe structuur van de nodes (zoals te zien in documentatie)
        Ss233Node<E> newParent = new Ss233Node<>(null, null);
        newParent.leftChild = first;
        newParent.middleChild = second;
        newParent.rightChild = third;

        // vullen van de values in de nodes, in volgende volgorde: A B C D E F (zie docstring)
        // Het is niet per se sneller wanneer dit uitdrukkelijk onder elkaar geschreven wordt zonder Stack.
        Stack<Ss233Node<E>> positions = new Stack<>();
        positions.addAll(List.of(third, third, newParent, second, newParent, first));
        while (!positions.isEmpty() && !values.isEmpty()) {
            positions.pop().setSplayValue(values.remove());
        }

        int aantalKinderen = children.size();

        setSingleKeyedChild(first, children); // voeg de kinderen toe aan newChild1
        setSingleKeyedChild(second, children); // Voeg de kinderen toe aan newChild2
        setTwoKeyedChild(third, children); // Voeg de kinderen toe aan newChild3

        if (newParent.rightChild.rightChild != null && aantalKinderen == 7) {
            newParent.rightChild.redistribute1();
        }

        // Wanneer er 4 nodes en 5 children zijn doet het probleem zich voor dat
        // dat het linkerkind van newChild4 (EF in docs) het vijfde kind is.
        // Dit kan makkelijk opgelost worden door dat linkerkind de nieuwe Child4 te maken
        if (newParent.rightChild.isEmpty()){
            newParent.rightChild = newParent.rightChild.leftChild;
        }

        // Wanneer De eerste 3 nodes van de boom ge-splayed zijn is er geen parent waaraan
        // de nieuwe toppen aan gehecht kunnen worden, dus wordt newParent de nieuwe root
        if (parent == null){
            root = newParent;
            return;
        }

        parent.setChild(newParent);
    }

    /**
     * Recursieve methode die gebruikt wordt om een 'wandeling' te maken rond de drie toppen, hun waarden in values te steken,
     * hun kinderen in children te steken en de waarden op null zetten zodat de toppen opnieuw gebruikt
     * kunnen worden voor de nieuwe toppen structuur.
     * @param node: de node die op dit moment bekeken wordt (first, second of third)
     * @param other1: een van de andere toppen die niet node is.
     * @param other2: de laatste andere top die niet node is.
     * @param children: verwijzing naar children hier de kinderen aan toe te voegen.
     * @param values: verwijzing naar values hier de sleutelwaarden aan toe te voegen.
     */
    private void extractFromNodes(Ss233Node<E> node, Ss233Node<E> other1, Ss233Node<E> other2, Stack<Ss233Node<E>> children, Queue<E> values) {
        values.add(node.leftValue);
        node.leftValue = null; // Voor hergebruik in nieuwe structuur
        if (node.hasRightValue()) {
            values.add(node.rightValue);
            node.rightValue = null;
            extractOneToChildren(node.rightChild, node, other1, other2, children, values);
        }
        extractOneToChildren(node.middleChild, node, other1, other2, children, values);
        extractOneToChildren(node.leftChild, node, other1, other2, children, values);
    }

    /**
     * Hulpfunctie voor extractFromNodes
     * Wanneer het te bekijken kind NIET een van de twee andere toppen is moet deze aan de kinderen worden toegevoegd.
     * Wanneer dit wel het geval is, zet je de wandeling rond de toppen voort naar die top.
     * Recursie in `extractFromNodes` zorgt ervoor dat de wandeling langs de andere kan ook terugkeert.
     * @param child
     * andere parameters: zie params van extractFromNodes
     */
    private void extractOneToChildren(Ss233Node<E> child, Ss233Node<E> node, Ss233Node<E> other1, Ss233Node<E> other2, Stack<Ss233Node<E>> children, Queue<E> values) {
        if (child != other1 && child != other2) {
            children.push(child);
        } else if (child == other1) {
            extractFromNodes(other1, node, other2, children, values);
        } else {
            extractFromNodes(other2, node, other1, children, values);
        }
    }

    private void setSingleKeyedChild(Ss233Node<E> node, Stack<Ss233Node<E>> children) {
        node.leftChild = children.pop();
        node.middleChild = children.pop();
        node.rightChild = null;
    }

    private void setTwoKeyedChild(Ss233Node<E> node, Stack<Ss233Node<E>> children){
        node.leftChild = safePop(children);
        node.middleChild = safePop(children);
        node.rightChild = safePop(children);
    }

    private Ss233Node<E> safePop(Stack<Ss233Node<E>> children) {
        return children.isEmpty() ? null : children.pop();
    }

    @Override
    public boolean remove(E e) {
        Stack<Ss233Node<E>> pad = new Stack<>(); // Pad tot aan de node met de te verwijderen waarde.

        // Zoek de node met de te verwijderen waarde.
        Ss233Node<E> current = root;
        while (current != null && !current.hasValue(e)){
            pad.push(current);
            current = current.getChild(e);
        }

        if (current == null){ // De sleutel zat nog niet in de boom, en kan dus niet verwijderd worden.
            return false;
        }
        size -= 1; // Zal nu zeker verwijderd worden
        Ss233Node<E> verwijderNode = current; // De node met de te verwijderen sleutel.

        // Als de node met de te verwijderen sleutel geen blad is, vervang de te verwijderen
        // waarde dan met zijn 'in-order predecessor'
        if (verwijderNode.hasInOrderPredecessor(e)) {
            pad.push(current);
            current = (current.leftValue.compareTo(e) == 0) ? current.leftChild : current.middleChild;
            System.out.println(current);
            while (current != null) { // Zoek in-order predecessor.
                pad.push(current); // vul pad verder aan van verwijderNode naar het vervang-blad.
                current = current.hasRightValue() ? current.rightChild : current.middleChild;
            }

            Ss233Node<E> leaf = pad.pop();
            E predecessor = leaf.hasRightValue() ? leaf.rightValue : leaf.leftValue;
            System.out.println("Predecessor: " + leaf);

            // Vervang de te verwijderen sleutel door zijn predecessor.
            verwijderNode.replace(e, predecessor);
            verwijderNode = leaf; // de leaf moet nu verwijderd worden
        }

        if (verwijderNode.hasRightValue() && verwijderNode.rightValue.compareTo(e) == 0) { // todo tweede weg
            verwijderNode.rightValue = null;
            return true;
        }

        if (pad.isEmpty()){
            root = null;
            return true;
        }

        if (verwijderNode.leftValue.compareTo(e) == 0 && verwijderNode.hasRightValue()) {
            verwijderNode.leftValue = verwijderNode.rightValue;
            verwijderNode.leftChild = verwijderNode.middleChild;
            verwijderNode.middleChild = verwijderNode.rightChild;
            verwijderNode.rightChild = null;
        } else if (pad.peek().leftChild == verwijderNode){
            pad.peek().leftChild = verwijderNode.leftChild;
            System.out.println(888);
        } else if (pad.peek().middleChild == verwijderNode){
            pad.peek().middleChild = verwijderNode.leftChild;
        } else {
            pad.peek().rightChild = verwijderNode.leftChild;
        }
        return true;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        drawWithIndent(0, root, "", builder);
        return builder.toString();
    }

    private void drawWithIndent(int indent, Ss233Node<E> node, String type, StringBuilder stringBuilder) {
        if (node == null) {
            return;
        }

        stringBuilder.append("     |".repeat(indent)).append("-> ").append(type).append(" ").append(node).append("\n");

        drawWithIndent(indent + 1, node.leftChild  , "L", stringBuilder);
        drawWithIndent(indent + 1, node.middleChild, "M", stringBuilder);
        drawWithIndent(indent + 1, node.rightChild , "R", stringBuilder);

    }
}
