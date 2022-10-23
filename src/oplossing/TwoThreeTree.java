package oplossing;

import opgave.SearchTree;

import java.util.*;

public class TwoThreeTree<E extends Comparable<E>> implements SearchTree<E> {

    private int size = 0;
    public Node<E> root = null; // Er is bij aanvang nog geen root node

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(E o) {
        Node<E> node = root;
        while (node != null && !node.hasValue(o)) {
            node = node.getChild(o);
        }
        return node != null;
    }

    @Override
    public boolean add(E o) {
        this.size += 1;
        Node<E> newNode = new Node<>(o, null);
        if (root == null){
            this.root = newNode;
            return true;
        }
        Stack<Node<E>> stack = new Stack<>();
        Node<E> node = root;

        // Stack met de doorlopen nodes
        while (node  != null && ! node.hasValue(o)) {
            stack.push(node);
            node = node.getChild(o);
        }

        if (node != null){ // Het element zit al in de boom.
            size -= 1;
            return false;
        }

        // Toevoegen van de waarde o. Dit maakt van de 2-3-boom een bijna-2-3-boom
        Node<E> blad = stack.peek();
        blad.setChild(newNode);

        stack.push(newNode);
        // doorloop de nodes terug in omgekeerde volgorde en herbalanceer zodat de boom terug geldig wordt.
        while (stack.size() > 1) {
            node = stack.pop();
            Node<E> parent = stack.peek();
            if (parent.rightValue == null) {
                node.percolateUp(parent, o);
                break;
            }
            parent.convertToBinary(o); // Maak een kleine binaire boom van de node
        }
        return true;
    }

    public static void main(String[] args) {
        List<Integer> randoms = randomArrayList(45, 1000);
        //System.out.println(randoms);
        TwoThreeTree<Integer> tree = new TwoThreeTree<>();
        long start = System.currentTimeMillis();
        for (int i = 1; i < 10; i++)  {
            //System.out.println("==================== stap " + i + " ====================");
            tree.add(i);
            //TwoThreeTreeDrawer<Integer> drawer = new TwoThreeTreeDrawer<>(tree.root);
            //drawer.draw();
        }
        System.out.println(tree.contains(477));
        tree.iterator();
        System.out.println(System.currentTimeMillis() - start);

    }

    private static ArrayList<Integer> randomArrayList(int n, int range)
    {
        ArrayList<Integer> list = new ArrayList<>();
        Random random = new Random();
        random.setSeed(1);

        for (int i = 0; i < n; i++)
        {
            list.add(random.nextInt(range));
        }
        return list;
    }

    @Override
    public boolean remove(E e) {
        return false;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public Iterator<E> iterator() {
        List<E> valueList = new ArrayList<>();
        inorder(root, valueList);
        return valueList.listIterator();
    }

    private void inorder(Node<E> node, List<E> list) {
        if (node == null){
            return;
        }
        inorder(node.leftChild, list);
        list.add(node.leftValue);
        inorder(node.middleChild, list);
        if (node.rightValue != null) {
            list.add(node.rightValue);
            inorder(node.rightChild, list);
        }

    }

    public static class Node<E extends Comparable<E>> {

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
            if (leftValue != null && leftValue.compareTo(o) > 0) {
                return leftChild;
            }
            if (rightValue != null && rightValue.compareTo(o) < 0) {
                return rightChild;
            }
            return middleChild;
        }

        public boolean hasValue(E o) {
            return (leftValue != null && leftValue.compareTo(o) == 0)
                    || (rightValue != null && rightValue.compareTo(o) == 0);
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
            if (parent.leftValue.compareTo(o) < 0) {
                percolateUpFromRight(parent);
            } else {
                percolateUpFromLeft(parent);
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
            if (leftValue != null && leftValue.compareTo(o) > 0) {
                leftChild = newNode;
            } else if (rightValue != null && rightValue.compareTo(o) < 0) {
                rightChild = newNode;
            } else {
                middleChild = newNode;
            }
        }

        @Override
        public String toString(){
            return "(" + leftValue + "|" + rightValue + ")";
        }
    }
}
