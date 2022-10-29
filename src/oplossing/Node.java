package oplossing;

public class Node<E extends Comparable<E>> {

    public E leftValue;
    public E rightValue;

    public Node<E> leftChild;
    public Node<E> middleChild;
    public Node<E> rightChild;


    public Node(E leftValue, E rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public Node<E> getChild(E o) {
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            return leftChild;
        }
        if (hasRightValue() && rightValue.compareTo(o) < 0) {
            return rightChild;
        }
        return middleChild;
    }

    public void setChild(Node<E> newNode){
        E o = newNode.leftValue;
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            leftChild = newNode;
        } else if (hasRightValue() && rightValue.compareTo(o) < 0) {
            rightChild = newNode;
        } else {
            middleChild = newNode;
        }
    }

    public boolean isEmpty(){
        return leftValue == null && rightValue == null;
    }

    public boolean hasValue(E o) {
        return (hasLeftValue() && leftValue.compareTo(o) == 0)
                || (hasRightValue() && rightValue.compareTo(o) == 0);
    }

    public void swapValues(){ // TODO wegdoen
        E tmp = rightValue;
        this.rightValue = this.leftValue;
        this.leftValue = tmp;

    }

    private void percolateUpFromRight(Node<E> parent){
        parent.rightValue = leftValue;
        parent.middleChild = leftChild;
        parent.rightChild = middleChild;
    }

    private void percolateUpFromLeft(Node<E> parent) {
        parent.rightValue = leftValue;
        parent.swapValues();
        parent.leftChild = leftChild;
        parent.rightChild = parent.middleChild;
        parent.middleChild = middleChild;
    }

    public void percolateUp(Node<E> parent, E o) {
        // parent left value kan enkel null zijn wanneer de root (null, null) is // TODO wegdoen
        if (!parent.hasLeftValue() || parent.leftValue.compareTo(o) > 0) {
            percolateUpFromLeft(parent);
        } else {
            percolateUpFromRight(parent);
        }
    }

    public void convertToBinary(E o){
        if (rightValue.compareTo(o) < 0) {
            convertToBinaryFromRight();
        } else if (leftValue.compareTo(o) > 0){
            convertToBinaryFromLeft();
        } else {
            convertToBinaryFromMiddle();
        }
    }

    private void convertToBinaryFromRight() {
        Node<E> linkerNode = new Node<>(leftValue, null);
        linkerNode.leftChild = leftChild;
        linkerNode.middleChild = middleChild;
        leftValue = rightValue;
        leftChild = linkerNode;
        middleChild = rightChild;
        rightValue = null;
        rightChild = null;
    }

    private void convertToBinaryFromLeft() {
        Node<E> rechterNode = new Node<>(rightValue, null);
        rechterNode.leftChild = middleChild;
        rechterNode.middleChild = rightChild;
        middleChild = rechterNode;
        rightValue = null;
        rightChild = null;
    }

    private void convertToBinaryFromMiddle() {
        Node<E> linkerNode = new Node<>(leftValue, null);
        Node<E> rechterNode = new Node<>(rightValue, null);
        linkerNode.leftChild = leftChild;
        linkerNode.middleChild = middleChild.leftChild;
        rechterNode.middleChild = rightChild;
        rechterNode.leftChild = middleChild.middleChild;

        leftValue = middleChild.leftValue;
        leftChild = linkerNode;
        middleChild = rechterNode;
        rightValue = null;
        rightChild = null;
    }

    private void redistributeFromLeft(Node<E> verwijderNode){
        verwijderNode.leftValue = leftValue;
        leftValue = middleChild.leftValue;
        middleChild.leftValue = middleChild.rightValue;
        middleChild.rightValue = null;

        verwijderNode.leftChild = verwijderNode.middleChild;
        verwijderNode.middleChild = middleChild.leftChild;
        middleChild.leftChild = middleChild.middleChild;
        middleChild.middleChild = middleChild.rightChild;
        middleChild.rightChild = null;
    }

    private void redistributeFromMiddle1(Node<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = leftChild.rightValue;
        leftChild.rightValue = null;

        middleChild.leftChild = leftChild.rightChild;
        leftChild.rightChild = null;

    }

    private void redistributeFromMiddle2(Node<E> verwijderNode) {
        verwijderNode.leftValue = rightValue;
        rightValue = rightChild.leftValue;
        rightChild.leftValue = rightChild.rightValue;
        rightChild.rightValue = null;

        verwijderNode.leftChild = verwijderNode.middleChild;
        verwijderNode.middleChild = rightChild.leftChild;
        rightChild.leftChild = rightChild.middleChild;
        rightChild.middleChild = rightChild.rightChild;
        rightChild.rightChild = null;
    }

    private void redistributeFromRight(Node<E> verwijderNode) {
        verwijderNode.leftValue = rightValue;
        rightValue = middleChild.rightValue;
        middleChild.rightValue = null;

        rightChild.leftChild = middleChild.rightChild;
        middleChild.rightChild = null;
    }
    
    public boolean redistribute(Node<E> verwijderNode) {
        if (verwijderNode == leftChild && middleChild.numberOfKeys() == 2){
            redistributeFromLeft(verwijderNode);
            return true;
        } else if (verwijderNode == middleChild && leftChild.numberOfKeys() == 2) {
            redistributeFromMiddle1(verwijderNode);
            return true;
        } else if (verwijderNode == middleChild && rightChild != null && rightChild.numberOfKeys() == 2) {
            redistributeFromMiddle2(verwijderNode);
            return true;
        } else if (verwijderNode == rightChild && middleChild.numberOfKeys() == 2) {
            redistributeFromRight(verwijderNode);
            return true;
        }
        return false;
    }

    private void mergeIntoLeftChild() {
        leftChild.leftValue = leftValue;
        leftChild.rightValue = middleChild.leftValue;
        leftValue = null;
        swapValues();

        leftChild.leftChild = leftChild.middleChild;
        leftChild.middleChild = middleChild.leftChild;
        leftChild.rightChild = middleChild.middleChild;
        middleChild = rightChild;
        rightChild = null;
    }

    private void mergeIntoMiddleChild() {
        leftChild.rightValue = leftValue;
        leftValue = rightValue;
        rightValue = null;

        leftChild.rightChild = middleChild.middleChild;
        middleChild = rightChild;
        rightChild = null;
    }

    private  void mergeIntoRightChild() {
        middleChild.rightValue = rightValue;
        rightValue = null;
        middleChild.rightChild = rightChild.middleChild;
        rightChild = null;
    }

    public void mergeIntoChild(Node<E> verwijderNode) {
        if (leftChild == verwijderNode){
            mergeIntoLeftChild();
        } else if (middleChild == verwijderNode){
            mergeIntoMiddleChild();
        } else if (rightChild == verwijderNode) {
            mergeIntoRightChild();
        }
    }

    private void geval2NodesLeft(Node<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = null;
        verwijderNode.rightValue = middleChild.leftValue;
        verwijderNode.leftChild = verwijderNode.middleChild;
        verwijderNode.middleChild = middleChild.leftChild;
        verwijderNode.rightChild = middleChild.middleChild;
        middleChild = leftChild;
        leftChild = null; // TODO het kind van de (null, null) node niet middle child laten zijn maar leftchild
    }

    private void geval2NodesMiddle(Node<E> verwijderNode) {
        verwijderNode.rightValue = leftValue;
        verwijderNode.rightChild = middleChild.middleChild;
        leftValue = null;
        middleChild.leftValue = leftChild.leftValue;
        middleChild.middleChild = leftChild.middleChild;
        middleChild.leftChild = leftChild.leftChild;
        leftChild = null;
    }

    public void geval2nodes(Node<E> verwijderNode){
        if (verwijderNode == leftChild) {
            geval2NodesLeft(verwijderNode);
        } else {
            geval2NodesMiddle(verwijderNode);
        }
    }

    public void replace(E value, E with){
        if (leftValue.compareTo(value) == 0) {
            leftValue = with;
        } else if (rightValue.compareTo(value) == 0){
            rightValue = with;
        }
    }

    public int numberOfKeys(){
        return (rightValue == null) ? 1 : 2;
    }

    public boolean isLeaf(){
        return this.leftChild == null;
    }

    public boolean hasRightValue(){
        return this.rightValue != null;
    }

    public boolean hasLeftValue() {
        return this.leftValue != null;
    }

    @Override
    public String toString(){
        return "(" + leftValue + "|" + rightValue + ")";
    }
}