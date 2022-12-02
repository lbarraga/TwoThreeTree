package oplossing;

import opgave.SearchTree;

import java.util.*;

public class TopDownSemiSplayTwoThreeTree<E extends  Comparable<E>> extends SemiSplayTwoThreeTree<E> implements SearchTree<E>{

    @Override
    public boolean contains(E o) {
        Stack<Ss233Node<E>> splayPad = find(root, o);
        return splayPad.peek() != null;
    }

    public Stack<Ss233Node<E>> find(Ss233Node<E> node, E o) {
        Stack<Ss233Node<E>> splayWindow = new Stack<>();
        Stack<Ss233Node<E>> pad = new Stack<>();

        splayWindow.push(null);
        while (node != null && !node.hasValue(o)) {
            splayWindow.push(node);
            if (splayWindow.size() == 4) {
                node = splayOne(splayWindow.pop(), splayWindow.pop(), splayWindow.pop(), splayWindow.pop());
                splayWindow.push(node);
            }
            pad.push(node);
            node = node.getChild(o);
        }
        pad.push(node);
        return pad;
    }

    @Override
    public boolean add(E o) {
        Stack<Ss233Node<E>> stack = find(root, o);
        Ss233Node<E> node = stack.pop();
        if (node != null){ // Het element zit al in de boom.
            return false;
        }
        addValue(stack, o);
        return true;
    }

    @Override
    protected void searchInOrderSuccessor(Stack<Ss233Node<E>> pad, Ss233Node<E> current, E e) {
        Stack<Ss233Node<E>> splayWindow = new Stack<>();
        splayWindow.push(current);
        pad.push(current);
        current = (current.leftValue.compareTo(e) == 0) ? current.getMiddleChild() : current.getRightChild();
        while (current != null) { // Zoek in-order successor.
            splayWindow.push(current);
            if (splayWindow.size() == 4){
                current = splayOne(splayWindow.pop(), splayWindow.pop(), splayWindow.pop(), splayWindow.pop());
                splayWindow.push(current);
            }
            pad.push(current); // vul pad verder aan van verwijderNode naar het vervang-blad.
            current = current.getLeftChild();
        }
    }

    @Override
    public boolean remove(E e) {
        Stack<Ss233Node<E>> pad = find(root, e);
        Ss233Node<E> current = pad.pop();

        if (current == null){ // De sleutel zat nog niet in de boom, en kan dus niet verwijderd worden.
            return false;
        }
        removeValue(pad, current, e);
        //splay(pad);
        return true;
    }

}
