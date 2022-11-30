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
    public void removeChild(Ss233Node<E> child) {
        if (leftChild == child) {
            leftChild = null;
        } else if (middleChild == child) {
            middleChild = null;
        } else if (rightChild == child) {
            rightChild = null;
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

    public boolean isLeaf(){
        return this.leftChild == null && this.middleChild == null && this.rightChild == null;
    }

    public boolean hasInOrderPredecessor(E e){
        return (leftValue.compareTo(e) == 0 && leftChild != null) ||
                (rightValue.compareTo(e) == 0 && middleChild != null);
    }

}
