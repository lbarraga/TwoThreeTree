package oplossing;

import opgave.SearchTree;

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
        Ss233Node<E> node = root;
        while (node != null && !node.hasValue(o)) {
            node = node.getChild(o);
        }
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
            stack.push(newNode);
        }

        // Splay de bezochte toppen van onder naar boven.
        splay(stack);
        return true;
    }

    private void splay(Stack<Ss233Node<E>> splayPad) {
        while (splayPad.size() >= 3) {
            Ss233Node<E> third = splayPad.pop();
            Ss233Node<E> second = splayPad.pop();
            Ss233Node<E> first = splayPad.pop();
            Ss233Node<E> parent = splayPad.isEmpty() ? null : splayPad.pop();

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
        Queue<E> values = new PriorityQueue<>(); // gebruikt om de waardes op volgorde uit de toppen te halen.
        // gebruikt om kinderen uit de toppen te halen (stack om null-waarden toe te laten)
        Stack<Ss233Node<E>> children = new Stack<>();

        // Haal de kinderen (geen null kinderen en geen kind dat gelijk is aan first, second of third)
        extractToChildren(first, second, third, children);

        // Haal Waarden op volgorde uit de nodes
        extractIntoValues(first, values);
        extractIntoValues(second, values);
        extractIntoValues(third, values);

        // De nieuwe structuur van de nodes (zoals te zien in documentatie)
        Ss233Node<E> newParent = new Ss233Node<>(null, null);
        //Ss233Node<E> newChild1 = new Ss233Node<>(null, null);
        //Ss233Node<E> newChild2 = new Ss233Node<>(null, null);
        //Ss233Node<E> newChild3 = new Ss233Node<>(null, null);
        newParent.leftChild = first;
        newParent.middleChild = second;
        newParent.rightChild = third;

        // values op null zetten
        first.leftValue = null;
        first.rightValue = null;
        second.leftValue = null;
        second.rightValue = null;
        third.leftValue = null;
        third.rightValue = null;

        // vullen van de values in de nodes
        System.out.println("Values: " + values);
        Stack<Ss233Node<E>> positions = new Stack<>();
        positions.addAll(List.of(third, third, newParent, first, newParent, first));
        while (!positions.isEmpty() && !values.isEmpty()) {
            positions.pop().setSplayValue(values.remove());
        }

        System.out.println("Children: " + children);
        setSplayChilds(first, children); // voeg de kinderen toe aan newChild1
        setSplayChilds(second, children); // Voeg de kinderen toe aan newChild2
        yeet(third, children); // Voeg de kinderen toe aan newChild3

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

    private void extractToChildren(Ss233Node<E> node, Ss233Node<E> other1, Ss233Node<E> other2, Stack<Ss233Node<E>> children) {
        if (node.hasRightValue()) {
            extractOneToChildren(node.rightChild, node, other1, other2, children);
        }
        extractOneToChildren(node.middleChild, node, other1, other2, children);
        extractOneToChildren(node.leftChild, node, other1, other2, children);
    }

    private void extractOneToChildren(Ss233Node<E> child, Ss233Node<E> node, Ss233Node<E> other1, Ss233Node<E> other2, Stack<Ss233Node<E>> children) {
        if (child != other1 && child != other2) {
            children.push(child);
        } else if (child == other1) {
            extractToChildren(other1, node, other2, children);
        } else {
            extractToChildren(other2, node, other1, children);
        }
    }

    private void setSplayChilds(Ss233Node<E> node, Stack<Ss233Node<E>> children) {
        node.leftChild = safePop(children);
        node.middleChild = safePop(children);
        if (node.hasRightValue()){
            node.rightChild = safePop(children);
        }
    }

    private void yeet(Ss233Node<E> node, Stack<Ss233Node<E>> children){
        node.leftChild = safePop(children);
        node.middleChild = safePop(children);
        node.rightChild = safePop(children);
    }

    private Ss233Node<E> safePop(Stack<Ss233Node<E>> children) {
        return children.isEmpty() ? null : children.pop();
    }

    private void extractIntoValues(Ss233Node<E> node, Queue<E> values) {
        values.add(node.leftValue);
        if (node.rightValue != null) {
            values.add(node.rightValue);
        }
    }

    private void extractChildIntoChildren(Ss233Node<E> child, Ss233Node<E> other1, Ss233Node<E> other2, Stack<Ss233Node<E>> children) {
        if (child != other1 && child != other2) {
            children.push(child);
        }
    }

    @Override
    public boolean remove(E e) {
        return false;
    }

    @Override
    public void clear() {
        this.size = 0;
        this.root = new Ss233Node<>(null, null);
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
