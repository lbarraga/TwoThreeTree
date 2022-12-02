package oplossing;

public class Node233<E extends Comparable<E>> extends Node<E>{

    private Node233<E> leftChild;
    private Node233<E> middleChild;
    private Node233<E> rightChild;


    public Node233(E leftValue, E rightValue) {
        super(leftValue, rightValue);
    }

    /**
     * Kleine functie om wat properheid te bewaren in de code.
     * Op basis van een Element o, kijk in welke richten er gegaan moet worden
     * om dichter bij dit element te komen
     * @param o het element waarmee vergeleken wordt
     * @return een van de kinderen van deze node
     */
    public Node233<E> getChild(E o) {
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            return getLeftChild();
        }
        if (hasRightValue() && rightValue.compareTo(o) < 0) {
            return getRightChild();
        }
        return getMiddleChild();
    }

    /**
     * Gegeven een nieuw kind, adopteer hem op de juiste plaats.
     * @param newNode het kind dat geadopteerd zal worden.
     */
    public void setChild(Node233<E> newNode){
        E o = newNode.leftValue;
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            setLeftChild(newNode);
        } else if (hasRightValue() && rightValue.compareTo(o) < 0) {
            setRightChild(newNode);
        } else {
            setMiddleChild(newNode);
        }
    }

    /**
     * Het rechter kind van de parent (deze node dus) in de parent brengen
     * Kan enkel wanneer deze node en de parent maar één sleutel hebben
     */
    private void percolateUpFromRight(Node233<E> parent){
        parent.rightValue = leftValue;
        parent.setMiddleChild(getLeftChild());
        parent.setRightChild(getMiddleChild());
    }

    /**
     * Zelfde percolateUpFromRight maar dan vanaf links
     */
    private void percolateUpFromLeft(Node233<E> parent) {
        parent.rightValue = leftValue;
        parent.swapValues();
        parent.setLeftChild(getLeftChild());
        parent.setRightChild(parent.getMiddleChild());
        parent.setMiddleChild(getMiddleChild());
    }

    public void percolateUp(Node233<E> parent, E o) {
        if (!parent.hasLeftValue() || parent.leftValue.compareTo(o) > 0) {
            percolateUpFromLeft(parent);
        } else {
            percolateUpFromRight(parent);
        }
    }

    /**
     * maakt van een deelboom met twee toppen en drie sleutels een binaire boom:
     *
     *          [ B ]
     *         /     \
     *       [ A ]  [ C ]
     *
     * @param o linkerwaarde in de laagste node
     */
    public void convertToBinary(E o){
        if (rightValue.compareTo(o) < 0) {
            convertToBinaryFromRight();
        } else if (leftValue.compareTo(o) > 0){
            convertToBinaryFromLeft();
        } else {
            convertToBinaryFromMiddle();
        }
    }

    /**
     *  startpositie
     *
     *   [ A B ]
     *          \
     *          [ C ]
     */
    private void convertToBinaryFromRight() {
        Node233<E> linkerNode = new Node233<>(leftValue, null);
        linkerNode.setLeftChild(getLeftChild());
        linkerNode.setMiddleChild(getMiddleChild());
        leftValue = rightValue;
        setLeftChild(linkerNode);
        setMiddleChild(getRightChild());
        rightValue = null;
        setRightChild(null);
    }

    /**
     * startpositie
     *
     *      [ B C ]
     *     /
     *   [ A ]
     */
    private void convertToBinaryFromLeft() {
        Node233<E> rechterNode = new Node233<>(rightValue, null);
        rechterNode.setLeftChild(getMiddleChild());
        rechterNode.setMiddleChild(getRightChild());
        setMiddleChild(rechterNode);
        rightValue = null;
        setRightChild(null);
    }

    /**
     * startpositie
     *
     *   [ A C ]
     *      |
     *    [ B ]
     */
    private void convertToBinaryFromMiddle() {
        Node233<E> linkerNode = new Node233<>(leftValue, null);
        Node233<E> rechterNode = new Node233<>(rightValue, null);
        linkerNode.setLeftChild(getLeftChild());
        linkerNode.setMiddleChild(getMiddleChild().getLeftChild());
        rechterNode.setMiddleChild(getRightChild());
        rechterNode.setLeftChild(getMiddleChild().getMiddleChild());

        leftValue = getMiddleChild().leftValue;
        setLeftChild(linkerNode);
        setMiddleChild(rechterNode);
        rightValue = null;
        setRightChild(null);
    }

    /**
     * De verwijderNode zit in het midden en gebruikt gebruikt zijn midden-sibling om zichzelf op te vullen
     */
    private void redistributeFromLeft(Node233<E> verwijderNode){
        verwijderNode.leftValue = leftValue;
        leftValue = getMiddleChild().leftValue;
        getMiddleChild().leftValue = getMiddleChild().rightValue;
        getMiddleChild().rightValue = null;

        verwijderNode.setLeftChild(verwijderNode.getMiddleChild());
        verwijderNode.setMiddleChild(getMiddleChild().getLeftChild());
        getMiddleChild().setLeftChild(getMiddleChild().getMiddleChild());
        getMiddleChild().setMiddleChild(getMiddleChild().getRightChild());
        getMiddleChild().setRightChild(null);
    }

    /**
     * De verwijderNode zit in het midden en gebruikt zijn linker-sibling met twee sleutels
     */
    private void redistributeFromMiddle1(Node233<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = getLeftChild().rightValue;
        getLeftChild().rightValue = null;

        getMiddleChild().setLeftChild(getLeftChild().getRightChild());
        getLeftChild().setRightChild(null);

    }

    /**
     * De verwijderNode zit in het midden en gebruikt zijn rechter-sibling met twee sleutels
     */
    private void redistributeFromMiddle2(Node233<E> verwijderNode) {
        verwijderNode.leftValue = rightValue;
        rightValue = getRightChild().leftValue;
        getRightChild().leftValue = getRightChild().rightValue;
        getRightChild().rightValue = null;

        verwijderNode.setLeftChild(verwijderNode.getMiddleChild());
        verwijderNode.setMiddleChild(getRightChild().getLeftChild());
        getRightChild().setLeftChild(getRightChild().getMiddleChild());
        getRightChild().setMiddleChild(getRightChild().getRightChild());
        getRightChild().setRightChild(null);
    }

    /**
     * De verwijderNode zit rechts en zijn midden-sibling heeft twee nodes
     */
    private void redistributeFromRight(Node233<E> verwijderNode) {
        verwijderNode.leftValue = rightValue;
        rightValue = getMiddleChild().rightValue;
        getMiddleChild().rightValue = null;

        getRightChild().setLeftChild(getMiddleChild().getRightChild());
        getMiddleChild().setRightChild(null);
    }


    /**
     * Gebruik Een node met twee sleutels naast de verwijderNode
     * @param verwijderNode de lege node die heropgevuld moet worden.
     * @return false als er geen herdistributie is gebeurt
     */
    public boolean redistribute(Node233<E> verwijderNode) {
        if (verwijderNode == getLeftChild() && getMiddleChild().numberOfKeys() == 2){
            redistributeFromLeft(verwijderNode);
            return true;
        } else if (verwijderNode == getMiddleChild() && getLeftChild().numberOfKeys() == 2) {
            redistributeFromMiddle1(verwijderNode);
            return true;
        } else if (verwijderNode == getMiddleChild() && getRightChild() != null && getRightChild().numberOfKeys() == 2) {
            redistributeFromMiddle2(verwijderNode);
            return true;
        } else if (verwijderNode == getRightChild() && getMiddleChild().numberOfKeys() == 2) {
            redistributeFromRight(verwijderNode);
            return true;
        }
        return false;
    }

    private void mergeIntoLeftChild() {
        getLeftChild().leftValue = leftValue;
        getLeftChild().rightValue = getMiddleChild().leftValue;
        leftValue = null;
        swapValues();

        getLeftChild().setLeftChild(getLeftChild().getMiddleChild());
        getLeftChild().setMiddleChild(getMiddleChild().getLeftChild());
        getLeftChild().setRightChild(getMiddleChild().getMiddleChild());
        setMiddleChild(getRightChild());
        setRightChild(null);
    }

    private void mergeIntoMiddleChild() {
        getLeftChild().rightValue = leftValue;
        leftValue = rightValue;
        rightValue = null;

        getLeftChild().setRightChild(getMiddleChild().getMiddleChild());
        setMiddleChild(getRightChild());
        setRightChild(null);
    }

    private  void mergeIntoRightChild() {
        getMiddleChild().rightValue = rightValue;
        rightValue = null;
        getMiddleChild().setRightChild(getRightChild().getMiddleChild());
        setRightChild(null);
    }

    /**
     * Wanneer deze node twee sleutels heeft, kan een van de kinderen de lege node invullen met een van zijn sleutels,
     * Een gat dat daar zou kunnen ontstaan wordt op zijn beurt opgevuld door een van de sleutels uit deze node.
     * @param verwijderNode De lege node waarin gemerged moet worden
     */
    public void mergeIntoChild(Node233<E> verwijderNode) {
        if (getLeftChild() == verwijderNode){
            mergeIntoLeftChild();
        } else if (getMiddleChild() == verwijderNode){
            mergeIntoMiddleChild();
        } else if (getRightChild() == verwijderNode) {
            mergeIntoRightChild();
        }
    }


    /**
     *  startpositie
     *      [ B ]
     *     /
     *  [ A ]
     *
     */
    private void geval2NodesLeft(Node233<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = null;
        verwijderNode.rightValue = getMiddleChild().leftValue;
        verwijderNode.setLeftChild(verwijderNode.getMiddleChild());
        verwijderNode.setMiddleChild(getMiddleChild().getLeftChild());
        verwijderNode.setRightChild(getMiddleChild().getMiddleChild());
        setMiddleChild(getLeftChild());
        setLeftChild(null); // TODO het kind van de (null, null) node niet middle child laten zijn maar leftchild
    }


    /**
     * startpositie
     *    [ A ]
     *         \
     *        [ B ]
     */
    private void geval2NodesMiddle(Node233<E> verwijderNode) {
        verwijderNode.rightValue = leftValue;
        verwijderNode.setRightChild(getMiddleChild().getMiddleChild());
        leftValue = null;
        getMiddleChild().leftValue = getLeftChild().leftValue;
        getMiddleChild().setMiddleChild(getLeftChild().getMiddleChild());
        getMiddleChild().setLeftChild(getLeftChild().getLeftChild());
        setLeftChild(null);
    }

    /**
     *          Leeg
     *           |
     *        [ A B ]
     *
     */
    public void geval2nodes(Node233<E> verwijderNode){
        if (verwijderNode == getLeftChild()) {
            geval2NodesLeft(verwijderNode);
        } else {
            geval2NodesMiddle(verwijderNode);
        }
    }

    public boolean isLeaf(){
        return this.getLeftChild() == null;
    }

    public Node233<E> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node233<E> leftChild) {
        this.leftChild = leftChild;
    }

    public Node233<E> getMiddleChild() {
        return middleChild;
    }

    public void setMiddleChild(Node233<E> middleChild) {
        this.middleChild = middleChild;
    }

    public Node233<E> getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node233<E> rightChild) {
        this.rightChild = rightChild;
    }
}