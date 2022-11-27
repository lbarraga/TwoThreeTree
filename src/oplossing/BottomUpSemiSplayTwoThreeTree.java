package oplossing;

import opgave.SearchTree;

import java.util.*;

public class BottomUpSemiSplayTwoThreeTree<E extends  Comparable<E>> implements SearchTree<E> {

    public int size = 0;
    public Ss233Node<E> root = new Ss233Node<>(null, null);

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
        if (root != null && root.isEmpty()) {
            root.leftValue = o;
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
        System.out.println(splayPad);
        while (splayPad.size() >= 3) {
            Ss233Node<E> third = splayPad.pop();
            Ss233Node<E> second = splayPad.pop();
            Ss233Node<E> first = splayPad.pop();
            Ss233Node<E> parent = splayPad.isEmpty() ? null : splayPad.pop();

            splayOne(first, second, third, parent);
        }
    }

    public void splayOne(Ss233Node<E> first, Ss233Node<E> second, Ss233Node<E> third, Ss233Node<E> parent) {
        Queue<E> values = new PriorityQueue<>();
        Stack<Ss233Node<E>> children = new Stack<>();
        Queue<Ss233Node<E>> nodes = new PriorityQueue<>(Comparator.comparing(n -> n.leftValue));
        nodes.addAll(List.of(first, second, third));

        // Haal de kinderen (geen null kinderen en geen kind dat gelijk is aan first, second of third)
        extractToChildren(first, second, third, children);

        // Zorg ervoor dat first < second < third. In verslag zeggen waarom geen prio queue
        first = nodes.remove();
        second = nodes.remove();
        third = nodes.remove();

        // Haal Waarden op volgorde uit de nodes
        extractIntoValues(first, values);
        extractIntoValues(second, values);
        extractIntoValues(third, values);


//        extractIntoChildren(third, first, second, children);
//        extractIntoChildren(second, first, third, children);
//        extractIntoChildren(first, second, third, children);


        // De nieuwe structuur van de nodes
        Ss233Node<E> new1 = new Ss233Node<>(null, null);
        Ss233Node<E> new2 = new Ss233Node<>(null, null);
        Ss233Node<E> new3 = new Ss233Node<>(null, null);
        Ss233Node<E> new4 = new Ss233Node<>(null, null);
        new1.leftChild = new2;
        new1.middleChild = new3;
        new1.rightChild = new4;

        System.out.println("values: " + values);
        // vullen van de values in de nodes
        Stack<Ss233Node<E>> positions = new Stack<>();
        positions.addAll(List.of(new4, new4, new1, new3, new1, new2));
        while (!positions.isEmpty() && !values.isEmpty()) {
            positions.pop().setSplayValue(values.remove());
        }

//        System.out.println("===============");
//        while (!children.isEmpty()){
//            System.out.println(children.pop());
//        }
//        System.out.println("===============");

        System.out.println(children.size());
        System.out.println(children);

        setSplayChilds(new2, children);
        setSplayChilds(new3, children);
        yeet(new4, children);

        if (new1.rightChild.isEmpty()){
            new1.rightChild = new1.rightChild.leftChild;
        }

        if (parent == null){
            root = new1;
            return;
        }
        parent.setChild(new1);
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
            System.out.println("pushed " + child + " from " + node);
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
        if (node.hasRightValue()){ // TODO weg
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

    private void extractIntoChildren(Ss233Node<E> node, Ss233Node<E> other1, Ss233Node<E> other2, Stack<Ss233Node<E>> children) {
        if (node.hasRightValue()){
            extractChildIntoChildren(node.rightChild, other1, other2, children);
        }
        extractChildIntoChildren(node.middleChild, other1, other2, children);
        extractChildIntoChildren(node.leftChild, other1, other2, children);
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
