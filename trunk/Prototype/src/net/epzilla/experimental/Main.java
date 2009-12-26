package net.epzilla.experimental;

/**
 * Created by IntelliJ IDEA.
 * User: rajeev
 * Date: Dec 2, 2009
 * Time: 6:08:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        Tree t = new Tree();
        t.addValue(5);
        t.addValue(51);
        t.addValue(1);
        t.addValue(2);
        t.addValue(0);
        t.addValue(55);
        t.addValue(54);
        t.addValue(35);
        t.addValue(5);
        

        // to eliminate the need to feed the root as a parameter, wrap these methods using another method which will
        // pass those parameters
        // eg: (inside Tree calss)
        // public void inOrderTraversal() {
        //    inOrderTraversal(root);
        // }
        System.out.println("in order traversal - returns sorted order.");
        t.inOrderTraversal(t.getRoot());

        System.out.println("pre order traversal");
        t.preOrderTraversal(t.getRoot());
        
        System.out.println("post order traversal");
        t.postOrderTraversal(t.getRoot());
    }
}
