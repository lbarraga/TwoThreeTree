package oplossing;

public class Ss233Node<E extends Comparable<E>> extends Node<E>{

    public Ss233Node<E> leftChild;
    public Ss233Node<E> middleChild;
    public Ss233Node<E> rightChild;


    public Ss233Node(E leftValue, E rightValue) {
        super(leftValue, rightValue);
    }

    public Ss233Node<E> getChild(E o) {
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            return leftChild;
        }
        if (hasRightValue() && rightValue.compareTo(o) < 0) {
            return rightChild;
        }
        return middleChild;
    }

    public void setChild(Ss233Node<E> newNode){
        E o = newNode.leftValue;
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            leftChild = newNode;
        } else if (hasRightValue() && rightValue.compareTo(o) < 0) {
            rightChild = newNode;
        } else {
            middleChild = newNode;
        }
    }

    public void setSplayValue(E value) {
        if (leftValue == null) {
            leftValue = value;
        } else if (rightValue == null) {
            rightValue = value;
        } else {
            assert false : "Setting value on node that already has two values"; // TODO wegdoen
        }
    }

    public void setSplayChild(Ss233Node<E> child) {
        if (leftChild == null) {
            leftChild = child;
        } else if (middleChild == null) {
            middleChild = child;
        } else if (rightChild == null) {
            rightChild = child;
        } else {
            assert false : "Setting child on node that has already 3 children"; // TODO wegdoen
        }
    }
}
