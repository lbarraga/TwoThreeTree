package oplossing;

import opgave.SearchTree;

import java.util.*;

public class BottomUpSemiSplayTwoThreeTree<E extends  Comparable<E>> extends SemiSplayTwoThreeTree<E> implements SearchTree<E> {


    @Override
    public boolean contains(E o) {
        Stack<Ss233Node<E>> splayPad = find(root, o);
        Ss233Node<E> last = splayPad.pop();
        splay(splayPad);
        return last != null;
    }

    private Stack<Ss233Node<E>> find(Ss233Node<E> node, E o) {
        Stack<Ss233Node<E>> stack = new Stack<>(); // Stack met de doorlopen nodes.
        while (node  != null && ! node.hasValue(o)) {
            stack.push(node);
            node = node.getChild(o); // daal af in de boom
        }
        stack.push(node);
        return stack;
    }

    @Override
    public boolean add(E o) {
        Stack<Ss233Node<E>> stack = find(root, o);

        Ss233Node<E> node = stack.pop();
        if (node != null){ // Het element zit al in de boom.
            splay(stack);
            return false;
        }
        addValue(stack, o);
        splay(stack); // Splay de bezochte toppen van onder naar boven.
        return true;
    }

    /**
     * Voer een splay-bewerking uit op een meegegeven splay pad
     * @param splayPad het pad waarover gesplayed moet worden
     */
    private void splay(Stack<Ss233Node<E>> splayPad) {
        while (splayPad.size() >= 3) {
            splayOne(splayPad.pop(), splayPad.pop(), splayPad.pop(), safePop(splayPad));
        }
    }

    @Override
    protected void searchInOrderSuccessor(Stack<Ss233Node<E>> pad, Ss233Node<E> current, E e) {
        pad.push(current);
        current = (current.leftValue.compareTo(e) == 0) ? current.middleChild: current.rightChild;
        while (current != null) { // Zoek in-order successor.
            pad.push(current); // vul pad verder aan van verwijderNode naar het vervang-blad.
            current = current.leftChild;
        }
    }

    @Override
    public boolean remove(E e) {
        Stack<Ss233Node<E>> pad = find(root, e);
        Ss233Node<E> current = pad.pop();

        if (current == null){ // De sleutel zat nog niet in de boom, en kan dus niet verwijderd worden.
            splay(pad);
            return false;
        }

        removeValue(pad, current, e);
        splay(pad);
        return true;
    }
}
