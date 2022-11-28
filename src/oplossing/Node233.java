package oplossing;

public class Node233<E extends Comparable<E>> extends Node<E>{

    public Node233<E> leftChild;
    public Node233<E> middleChild;
    public Node233<E> rightChild;


    public Node233(E leftValue, E rightValue) {
        super(leftValue, rightValue);
    }

    public Node233<E> getChild(E o) {
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            return leftChild;
        }
        if (hasRightValue() && rightValue.compareTo(o) < 0) {
            return rightChild;
        }
        return middleChild;
    }

    public void setChild(Node233<E> newNode){
        E o = newNode.leftValue;
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            leftChild = newNode;
        } else if (hasRightValue() && rightValue.compareTo(o) < 0) {
            rightChild = newNode;
        } else {
            middleChild = newNode;
        }
    }

    private void percolateUpFromRight(Node233<E> parent){
        parent.rightValue = leftValue;
        parent.middleChild = leftChild;
        parent.rightChild = middleChild;
    }

    private void percolateUpFromLeft(Node233<E> parent) {
        parent.rightValue = leftValue;
        parent.swapValues();
        parent.leftChild = leftChild;
        parent.rightChild = parent.middleChild;
        parent.middleChild = middleChild;
    }

    public void percolateUp(Node233<E> parent, E o) {
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
        Node233<E> linkerNode = new Node233<>(leftValue, null);
        linkerNode.leftChild = leftChild;
        linkerNode.middleChild = middleChild;
        leftValue = rightValue;
        leftChild = linkerNode;
        middleChild = rightChild;
        rightValue = null;
        rightChild = null;
    }

    private void convertToBinaryFromLeft() {
        Node233<E> rechterNode = new Node233<>(rightValue, null);
        rechterNode.leftChild = middleChild;
        rechterNode.middleChild = rightChild;
        middleChild = rechterNode;
        rightValue = null;
        rightChild = null;
    }

    private void convertToBinaryFromMiddle() {
        Node233<E> linkerNode = new Node233<>(leftValue, null);
        Node233<E> rechterNode = new Node233<>(rightValue, null);
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

    private void redistributeFromLeft(Node233<E> verwijderNode){
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

    private void redistributeFromMiddle1(Node233<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = leftChild.rightValue;
        leftChild.rightValue = null;

        middleChild.leftChild = leftChild.rightChild;
        leftChild.rightChild = null;

    }

    private void redistributeFromMiddle2(Node233<E> verwijderNode) {
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

    private void redistributeFromRight(Node233<E> verwijderNode) {
        verwijderNode.leftValue = rightValue;
        rightValue = middleChild.rightValue;
        middleChild.rightValue = null;

        rightChild.leftChild = middleChild.rightChild;
        middleChild.rightChild = null;
    }
    
    public boolean redistribute(Node233<E> verwijderNode) {
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

    public void mergeIntoChild(Node233<E> verwijderNode) {
        if (leftChild == verwijderNode){
            mergeIntoLeftChild();
        } else if (middleChild == verwijderNode){
            mergeIntoMiddleChild();
        } else if (rightChild == verwijderNode) {
            mergeIntoRightChild();
        }
    }

    private void geval2NodesLeft(Node233<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = null;
        verwijderNode.rightValue = middleChild.leftValue;
        verwijderNode.leftChild = verwijderNode.middleChild;
        verwijderNode.middleChild = middleChild.leftChild;
        verwijderNode.rightChild = middleChild.middleChild;
        middleChild = leftChild;
        leftChild = null; // TODO het kind van de (null, null) node niet middle child laten zijn maar leftchild
    }

    private void geval2NodesMiddle(Node233<E> verwijderNode) {
        verwijderNode.rightValue = leftValue;
        verwijderNode.rightChild = middleChild.middleChild;
        leftValue = null;
        middleChild.leftValue = leftChild.leftValue;
        middleChild.middleChild = leftChild.middleChild;
        middleChild.leftChild = leftChild.leftChild;
        leftChild = null;
    }

    public void geval2nodes(Node233<E> verwijderNode){
        if (verwijderNode == leftChild) {
            geval2NodesLeft(verwijderNode);
        } else {
            geval2NodesMiddle(verwijderNode);
        }
    }

    public boolean isLeaf(){
        return this.leftChild == null;
    }

}