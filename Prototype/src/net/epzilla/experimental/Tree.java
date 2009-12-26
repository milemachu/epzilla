package net.epzilla.experimental;

/**
 * Created by IntelliJ IDEA.
 * User: rajeev
 * Date: Dec 2, 2009
 * Time: 6:08:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tree {
    private Node root = null;

    public Node getRoot() {
        return root;
    }

    public void addValue(int value) {
        if (root == null) {
            root = new Node();
            root.setValue(value);
        } else {
            recursiveAdd(root, value);
        }
    }

    private void recursiveAdd(Node currentNode, int value) {
        int currentVal = currentNode.getValue();
        if (value < currentVal) {
            if (currentNode.children[Node.left] == null) {
                // add the node and stop recursion here.
                currentNode.children[Node.left] = new Node();
                currentNode.children[Node.left].setValue(value);
            } else {
                // have to go further down the tree.
                recursiveAdd(currentNode.children[Node.left], value);
            }
        } else {
            if (currentNode.children[Node.right] == null) {
                // add the node and stop recursion..
                currentNode.children[Node.right] = new Node();
                currentNode.children[Node.right].setValue(value);
            } else {
                // go down
                recursiveAdd(currentNode.children[Node.right], value);
            }
        }
    }

    // idea : visit current node, then left, then right.
    public void preOrderTraversal(Node currentNode) {

        if (currentNode != null) {

            System.out.println(currentNode.getValue()); // + write other operations here.

            // recurrence.
            // goto left subtree
            preOrderTraversal(currentNode.children[Node.left]);
            // goto right subtree
            preOrderTraversal(currentNode.children[Node.right]);

        }

    }

    // idea: goto left child first, then current one, then right child.
    public void inOrderTraversal(Node currentNode) {
        if (currentNode != null) {
            // goto left subtree without performing operations on this node
            inOrderTraversal(currentNode.children[Node.left]);

            // do all the operations on this node here.
            // ......
            System.out.println(currentNode.getValue());
            // ......

            // goto right subtree.
            inOrderTraversal(currentNode.children[Node.right]);
        }
    }

    // idea - goto left, right finally current.
    public void postOrderTraversal(Node currentNode) {
        if (currentNode != null) {
            postOrderTraversal(currentNode.children[Node.left]);
            postOrderTraversal(currentNode.children[Node.right]);


            System.out.println(currentNode.getValue());
            // plus whatever the operations on current node..
        }
    }

}
