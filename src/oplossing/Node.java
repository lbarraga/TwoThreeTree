package oplossing;

/**
 * Abstracte superklasse node van de Node233 en SS233Node.
 * In deze twee subklassen is er wat codeDuplicate, aangezien overerving hier moeilijk was.
 * @param <E>
 */
public abstract class Node<E extends Comparable<E>> {

    public E leftValue;
    public E rightValue;

    public Node(E leftValue, E rightValue) {
        this.leftValue = leftValue;
        this.rightValue = rightValue;
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

    public boolean hasRightValue(){
        return this.rightValue != null;
    }

    public boolean hasLeftValue() {
        return this.leftValue != null;
    }

    public int numberOfKeys(){
        return (rightValue == null) ? 1 : 2;
    }

    public void replace(E value, E with){
        if (leftValue.compareTo(value) == 0) {
            leftValue = with;
        } else if (rightValue.compareTo(value) == 0){
            rightValue = with;
        }
    }

    @Override
    public String toString(){
        return "(" + leftValue + "|" + rightValue + ")";
    }
}
