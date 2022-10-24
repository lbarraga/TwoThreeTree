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

    public boolean hasValue(E o) {
        return (hasLeftValue() && leftValue.compareTo(o) == 0)
                || (hasRightValue() && rightValue.compareTo(o) == 0);
    }

    public void swapValues(){
        E tmp = rightValue;
        this.rightValue = this.leftValue;
        this.leftValue = tmp;

    }

    public void percolateUpFromRight(Node<E> parent){
        parent.rightValue = leftValue;
        parent.middleChild = leftChild;
        parent.rightChild = middleChild;
    }

    public void percolateUpFromLeft(Node<E> parent) {
        parent.rightValue = leftValue;
        parent.swapValues();
        parent.leftChild = leftChild;
        parent.rightChild = parent.middleChild;
        parent.middleChild = middleChild;
    }

    public void percolateUp(Node<E> parent, E o) {
        // parent left value kan enkel null zijn wanneer de root (null, null) is
        if (!hasLeftValue() || parent.leftValue.compareTo(o) > 0) {
            percolateUpFromLeft(parent);
        } else {
            percolateUpFromRight(parent);
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

    public void convertToBinary(E o){
        if (rightValue.compareTo(o) < 0) {
            convertToBinaryFromRight();
        } else if (leftValue.compareTo(o) > 0){
            convertToBinaryFromLeft();
        } else {
            convertToBinaryFromMiddle();
        }
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