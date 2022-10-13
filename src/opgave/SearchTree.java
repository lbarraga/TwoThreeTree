package opgave;

/**
 * A simple binary search tree with support for basic actions and a direct view
 * on the nodes in the tree through requesting the root node. The
 * <tt>iterator</tt> implemented by this search tree should iterate the elements
 * by traversing the tree in-order.
 *
 * @param <E> the type of elements that can be contained in this tree
 */
public interface SearchTree<E extends Comparable<E>> extends Iterable<E> {

    /**
     * Return the number of elements in this tree.
     *
     * @return the number of elements in this tree
     */
    int size();

    /**
     * Return whether this tree is empty.
     *
     * @return <tt>true</tt> if there are no elements in this tree.
     */
    boolean isEmpty();

    /**
     * Returns <tt>true</tt> if this tree contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this tree contains
     * at least one element <tt>e</tt> such that <tt>Object.equals(o, e)</tt>
     *
     * @param o element whose presence in this tree is to be tested.
     * @return <tt>true</tt> if the tree contains the specified element.
     */
    boolean contains(E o);

    /**
     * Ensures that this tree contains the specified element.
     * Returns <tt>true</tt> if this tree did not contain the specified element
     * before the call, returns <tt>false</tt> if this tree did contain the
     * specified element.
     *
     * @param o element whose presence in this collection is to be ensured
     * @return <tt>true</tt> if the elements in the tree changed as a result of
     * the call.
     */
    boolean add(E o);

    /**
     * Ensures that this tree does not contain the specified element.
     * Returns <tt>true</tt> if this tree did contain the specified element
     * before the call and the element was removed from the tree, returns
     * <tt>false</tt> if this tree did contain the specified element.
     *
     * @param e element to be removed from the tree, if present
     * @return <tt>true</tt> if the element was removed as result of this call
     */
    boolean remove(E e);

    /**
     * Remove all the elements in this tree.
     */
    void clear();
}
