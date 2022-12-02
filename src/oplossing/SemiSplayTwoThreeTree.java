package oplossing;

import opgave.SearchTree;

import java.util.*;

public abstract class SemiSplayTwoThreeTree<E extends Comparable<E>> extends BaseTwoThreeTree<E> {

    protected Ss233Node<E> root = null;

    protected void addValue(Stack<Ss233Node<E>> stack, E o) {

        this.size += 1;

        // Toevoegen van de waarde o.
        if (root == null) {
            root = new Ss233Node<>(o, null);
            return;
        }

        if (!stack.peek().hasRightValue() && stack.peek().leftValue.compareTo(o) < 0) {
            stack.peek().rightValue = o;
            return;
        }

        Ss233Node<E> newNode = new Ss233Node<>(o, null);
        stack.peek().setChild(newNode); // Voeg de nieuwe node toe aan de laatst bezochte top.

        // Om lange slierten te vermijden en de boom wat gebalanceerd te houden, doen we enkele herdistributies
        // bij het toevoegen, Dit heeft ook het effect dat er meer toppen zijn met maar één sleutel, wat toevoegen
        // in de toekomst kan vergemakkelijken.
        if (stack.peek().hasRightValue() && stack.peek().getLeftChild() == newNode) {
            stack.peek().redistribute2();
        } else if (stack.peek().getRightChild() == newNode) {
            stack.peek().redistribute1();
        } else if (stack.peek().getMiddleChild() == newNode) {
            stack.peek().redistribute3();
        }

    }


    /**
     * Verwijder de waarde uit de boom.
     * @param pad Het pad dat tot nu toe al doorlopen is
     * @param verwijderNode  de node met de waarde e die verwijderd moet worden
     * @param e de waarde die verwijderd moet worden
     */
    public void removeValue(Stack<Ss233Node<E>> pad, Ss233Node<E> verwijderNode, E e) {
        size -= 1; // Zal nu zeker verwijderd worden

        // Als de node met de te verwijderen sleutel geen blad is, vervang de te verwijderen
        // waarde dan met zijn 'in-order successor'
        if (verwijderNode.hasInOrderSuccessor(e)) {
            searchInOrderSuccessor(pad, verwijderNode, e);
            Ss233Node<E> leaf = pad.pop();
            verwijderNode = switchValues(leaf, verwijderNode, e);
        }

        // Kan nooit behalve als er geen successor onder hem is, maar wel naast. (te verwijderen waarde zat rechts)
        if (verwijderNode.leftValue.compareTo(e) != 0) {
            verwijderNode.rightValue = null;
            return;
        }

        // Schuif de rechter sleutel op naar de linker sleutel, en verwijder zo de te verwijderen sleutel
        // (die waarde zal altijd links zitten aangezien het een successor is)
        verwijderNode.leftValue = verwijderNode.rightValue;
        verwijderNode.rightValue = null;

        if (verwijderNode.leftValue != null) { // de te verwijderen waarde zat links en is opgegeten door de rechtse
            if (verwijderNode.getLeftChild() == null) { // schuif middelste op naar linkerkind
                verwijderNode.setLeftChild(verwijderNode.getMiddleChild());
                verwijderNode.setMiddleChild(null);
            }
            if (verwijderNode.getMiddleChild() == null) { // schuif rechterkind op naar middelste kind
                verwijderNode.setMiddleChild(verwijderNode.getRightChild());
                verwijderNode.setRightChild(null);
            }
            return;
        }


        if (verwijderNode.getMiddleChild() == null) {
            verwijderNode.setMiddleChild(verwijderNode.getRightChild());
        }

        if (pad.isEmpty()) {
            root = verwijderNode.getLeftChild();
            return;
        }

        // het kan zijn dat de node enkel een linkerkind heeft, en dus geen successor onder hem.
        if (verwijderNode.getLeftChild() != null) {
            pad.peek().setChild(verwijderNode.getLeftChild());
        } else if (verwijderNode.getMiddleChild() != null) {
            pad.peek().setChild(verwijderNode.getMiddleChild());
        } else {
            pad.peek().setChildTo(verwijderNode, verwijderNode.getMiddleChild());
        }

    }

    abstract void searchInOrderSuccessor(Stack<Ss233Node<E>> pad, Ss233Node<E> current, E e);

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
    protected Ss233Node<E> splayOne(Ss233Node<E> third, Ss233Node<E> second, Ss233Node<E> first, Ss233Node<E> parent) {
        Queue<E> values = new PriorityQueue<>(); // Gebruikt om de waardes op volgorde uit de toppen te halen.
        // gebruikt om kinderen uit de toppen te halen (stack om null-waarden toe te laten)
        Stack<Ss233Node<E>> children = new Stack<>();

        // Haal de kinderen (geen null kinderen en geen kind dat gelijk is aan first, second of third)
        // en stop ze in children. Terwijl de toppen bezocht worden, steek ook de sleutels in values
        extractFromNodes(first, second, third, children, values);

        // De nieuwe structuur van de nodes (zoals te zien in documentatie)
        Ss233Node<E> newParent = new Ss233Node<>(null, null);
        newParent.setLeftChild(first);
        newParent.setMiddleChild(second);
        newParent.setRightChild(third);

        // vullen van de values in de nodes, in volgende volgorde: A B C D E F (zie docstring)
        // Het is niet per se sneller wanneer dit uitdrukkelijk onder elkaar geschreven wordt zonder Stack.
        Stack<Ss233Node<E>> positions = new Stack<>();
        positions.addAll(List.of(third, third, newParent, second, newParent, first));
        while (!positions.isEmpty() && !values.isEmpty()) {
            positions.pop().setSplayValue(values.remove());
        }

        setSingleKeyedChild(first, children); // voeg de kinderen toe aan newChild1
        setSingleKeyedChild(second, children); // Voeg de kinderen toe aan newChild2
        setTwoKeyedChild(third, children); // Voeg de kinderen toe aan newChild3

        // Wanneer er 4 nodes en 5 children zijn doet het probleem zich voor dat
        // dat het linkerkind van newChild4 (EF in docs) het vijfde kind is.
        // Dit kan makkelijk opgelost worden door dat linkerkind de nieuwe Child4 te maken
        if (newParent.getRightChild().isEmpty()){
            newParent.setRightChild(newParent.getRightChild().getLeftChild());
        }


        // Wanneer De eerste 3 nodes van de boom ge-splayed zijn is er geen parent waaraan
        // de nieuwe toppen aan gehecht kunnen worden, dus wordt newParent de nieuwe root
        if (parent == null){
            root = newParent;
            return newParent;
        }

        parent.setChild(newParent); // hecht de gesplayede subtree terug aan de parent
        return newParent;
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
            extractOneToChildren(node.getRightChild(), node, other1, other2, children, values);
        }
        extractOneToChildren(node.getMiddleChild(), node, other1, other2, children, values);
        extractOneToChildren(node.getLeftChild(), node, other1, other2, children, values);
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

    /**
     * Node [ A ] en [ C ] (zie docs splayOne) hebben maar twee kinderen, het rechterkind kan dus overgeslaan worden
     * @param node de singlekeyed node
     * @param children de stack met kinderen
     */
    private void setSingleKeyedChild(Ss233Node<E> node, Stack<Ss233Node<E>> children) {
        node.setLeftChild(children.pop());
        node.setMiddleChild(children.pop());
        node.setRightChild(null);
    }

    /**
     * [ E F ] heeft misschien twee sleutels, dus probeer in tegenstelling tot `setSingleKeyedChild`
     * ook het rechter kind te setten
     */
    private void setTwoKeyedChild(Ss233Node<E> node, Stack<Ss233Node<E>> children){
        node.setLeftChild(safePop(children));
        node.setMiddleChild(safePop(children));
        node.setRightChild(safePop(children));
    }

    protected Ss233Node<E> safePop(Stack<Ss233Node<E>> children) {
        return children.isEmpty() ? null : children.pop();
    }

    /**
     * vervang een waarde in een node met zijn in order successor
     * @param leaf het blad waarin de in order successor van de waarde in de verwijdernode zit.
     * @param verwijderNode de node met de waarde in die verwijderd moet worden
     * @param e de waarde die verwisseld moet worden met de successor
     * @return het blad waaruit de in order successor gehaald is
     */
    protected Ss233Node<E> switchValues(Ss233Node<E> leaf, Ss233Node<E> verwijderNode, E e){
        E successor = leaf.leftValue;
        // Vervang de te verwijderen sleutel door zijn successor.
        verwijderNode.replace(e, successor);
        leaf.leftValue = e;
        return leaf; // de leaf moet nu verwijderd worden
    }


    @Override
    public void clear() {
        this.size = 0;
        this.root = null;
    }

    @Override
    protected Node<E> getRoot() {
        return this.root;
    }

}
