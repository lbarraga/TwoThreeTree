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

    /**
     *   [ A B ] (deze node)    [ B ]
     *         \        ->     /     \
     *          [ C ]        [ A ]  [ C ]
     */
    public void redistribute1() {
        Ss233Node<E> linker = new Ss233Node<E>(leftValue, null);
        linker.leftChild = leftChild;
        linker.middleChild = middleChild;
        leftValue = rightValue;
        rightValue = null;
        middleChild = rightChild;
        rightChild = null;
        leftChild = linker;
    }

    /**
     *     [ B C ]          [ B ]
     *    /          ->    /    \
     *  [ A ]            [ A ] [ C ]
     */
    public void redistribute2() {
        Ss233Node<E> rechter = new Ss233Node<>(rightValue, null);
        rechter.leftChild = middleChild;
        rechter.middleChild = rightChild;
        rightValue = null;
        middleChild = rechter;
    }

    /**
     *     [ A C ]            [ B ]
     *        |      ->      /     \
     *      [ B ]          [ A ]  [ C ]
     */
    public void redistribute3() {
        Ss233Node<E> linker = new Ss233Node<>(leftValue, null);
        linker.leftChild = leftChild;
        linker.middleChild = middleChild.leftChild;
        Ss233Node<E> rechter = new Ss233Node<>(rightValue, null);
        rechter.leftChild = middleChild.leftChild;
        rechter.middleChild = rightChild;

        rightValue = null;
        rightChild = null;
        leftValue = middleChild.leftValue;
        leftChild = linker;
        middleChild = rechter;

    }

    public void setChildTo(Ss233Node<E> child, Ss233Node<E> to) {
        if (leftChild == child) {
            leftChild = to;
        } else if (middleChild == child) {
            middleChild = to;
        } else if (rightChild == child) {
            rightChild = to;
        }
    }

    public boolean hasInOrderSuccessor(E e) {
        return (leftValue.compareTo(e) == 0 && middleChild != null) ||
                (hasRightValue() && rightValue.compareTo(e) == 0 && rightChild != null);
    }

    private void redistributeFromLeft(Ss233Node<E> verwijderNode){
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

    private void redistributeFromMiddle1(Ss233Node<E> verwijderNode) {
        verwijderNode.leftValue = leftValue;
        leftValue = leftChild.rightValue;
        leftChild.rightValue = null;

        middleChild.leftChild = leftChild.rightChild;
        leftChild.rightChild = null;

    }

    private void redistributeFromMiddle2(Ss233Node<E> verwijderNode) {
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

    private void redistributeFromRight(Ss233Node<E> verwijderNode) {
        verwijderNode.leftValue = rightValue;
        rightValue = middleChild.rightValue;
        middleChild.rightValue = null;

        rightChild.leftChild = middleChild.rightChild;
        middleChild.rightChild = null;
    }

    public boolean redistribute(Ss233Node<E> verwijderNode) {
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

    public void mergeIntoLeftChild() {
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

    public void mergeIntoChild(Ss233Node<E> verwijderNode) {
        if (leftChild == verwijderNode){
            mergeIntoLeftChild();
        } else if (middleChild == verwijderNode){
            mergeIntoMiddleChild();
        } else if (rightChild == verwijderNode) {
            mergeIntoRightChild();
        }
    }

}
