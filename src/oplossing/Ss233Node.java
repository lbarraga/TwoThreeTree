package oplossing;

public class Ss233Node<E extends Comparable<E>> extends Node<E>{

    private Ss233Node<E> leftChild;
    private Ss233Node<E> middleChild;
    private Ss233Node<E> rightChild;


    public Ss233Node(E leftValue, E rightValue) {
        super(leftValue, rightValue);
    }

    public Ss233Node<E> getChild(E o) {
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            return getLeftChild();
        }
        if (hasRightValue() && rightValue.compareTo(o) < 0) {
            return getRightChild();
        }
        return getMiddleChild();
    }

    public void setChild(Ss233Node<E> newNode){
        E o = newNode.leftValue;
        if (hasLeftValue() && leftValue.compareTo(o) > 0) {
            setLeftChild(newNode);
        } else if (hasRightValue() && rightValue.compareTo(o) < 0) {
            setRightChild(newNode);
        } else {
            setMiddleChild(newNode);
        }
    }

    public void setSplayValue(E value) {
        if (leftValue == null) {
            leftValue = value;
        } else if (rightValue == null) {
            rightValue = value;
        }
    }

    /**
     *   [ A B ] (deze node)    [ B ]
     *         \        ->     /     \
     *          [ C ]        [ A ]  [ C ]
     */
    public void redistribute1() {
        Ss233Node<E> linker = new Ss233Node<E>(leftValue, null);
        linker.setLeftChild(getLeftChild());
        linker.setMiddleChild(getMiddleChild());
        leftValue = rightValue;
        rightValue = null;
        setMiddleChild(getRightChild());
        setRightChild(null);
        setLeftChild(linker);
    }

    /**
     *     [ B C ]          [ B ]
     *    /          ->    /    \
     *  [ A ]            [ A ] [ C ]
     */
    public void redistribute2() {
        Ss233Node<E> rechter = new Ss233Node<>(rightValue, null);
        rechter.setLeftChild(getMiddleChild());
        rechter.setMiddleChild(getRightChild());
        rightValue = null;
        setMiddleChild(rechter);
    }

    /**
     *     [ A C ]            [ B ]
     *        |      ->      /     \
     *      [ B ]          [ A ]  [ C ]
     */
    public void redistribute3() {
        Ss233Node<E> linker = new Ss233Node<>(leftValue, null);
        linker.setLeftChild(getLeftChild());
        linker.setMiddleChild(getMiddleChild().getLeftChild());
        Ss233Node<E> rechter = new Ss233Node<>(rightValue, null);
        rechter.setLeftChild(getMiddleChild().getLeftChild());
        rechter.setMiddleChild(getRightChild());

        rightValue = null;
        setRightChild(null);
        leftValue = getMiddleChild().leftValue;
        setLeftChild(linker);
        setMiddleChild(rechter);

    }

    /**
     * Gegeven een van deze node zijn drie kinderen, zet hem op een andere waarde
     * @param child het kind dat een andere node zal worden
     * @param to de node waarin child zal veranderen
     */
    public void setChildTo(Ss233Node<E> child, Ss233Node<E> to) {
        if (getLeftChild() == child) {
            setLeftChild(to);
        } else if (getMiddleChild() == child) {
            setMiddleChild(to);
        } else if (getRightChild() == child) {
            setRightChild(to);
        }
    }

    /**
     * @param e Het element waarvan we willen weten of hij een in order successor heeft
     * @return of het element e een in order successor heeft
     */
    public boolean hasInOrderSuccessor(E e) {
        return (leftValue.compareTo(e) == 0 && getMiddleChild() != null) ||
                (hasRightValue() && rightValue.compareTo(e) == 0 && getRightChild() != null);
    }

    public Ss233Node<E> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Ss233Node<E> leftChild) {
        this.leftChild = leftChild;
    }

    public Ss233Node<E> getMiddleChild() {
        return middleChild;
    }

    public void setMiddleChild(Ss233Node<E> middleChild) {
        this.middleChild = middleChild;
    }

    public Ss233Node<E> getRightChild() {
        return rightChild;
    }

    public void setRightChild(Ss233Node<E> rightChild) {
        this.rightChild = rightChild;
    }
}
