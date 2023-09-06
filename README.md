# TwoThreeTree Implementations in Java

This project features implementations of several distinct tree structures: TwoThreeTree, SemiSplay TwoThreeTree, and a Bottom-Up SemiSplay TwoThreeTree. The SemiSplay TwoThreeTree is a variant based on the semi splay tree data structure, a novel concept that combines the principles of semi-splay trees into a two-three tree. You can learn more about the foundational concepts of semi splay trees in the original paper by Sleator and Tarjan, which is available [here](https://www.cs.cmu.edu/~sleator/papers/self-adjusting.pdf).

## Actions

Below are the actions that can be performed on the trees as per the `SearchTree` interface:

- **size()**: Retrieves the number of elements present in the tree.
- **isEmpty()**: Checks if the tree is empty.
- **contains(E o)**: Checks if a specific element is present in the tree.
- **add(E o)**: Adds a specified element to the tree; returns `true` if the tree did not contain the element previously.
- **remove(E e)**: Removes a specified element from the tree; returns `true` if the tree contained the element and it was successfully removed.
- **clear()**: Removes all elements from the tree.

## Iterating Through Elements

The implemented trees adhere to the `SearchTree` interface which extends `Iterable<E>`, allowing iteration over elements in the tree. The elements are traversed in an in-order sequence.

Feel free to explore the code to learn more about these innovative tree implementations. Happy Coding!
