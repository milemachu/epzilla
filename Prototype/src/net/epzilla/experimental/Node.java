package net.epzilla.experimental;

/**
 * Created by IntelliJ IDEA.
 * User: rajeev
 * Date: Dec 2, 2009
 * Time: 6:05:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Node {
    // tree node
     public static final int left = 0;
    public static final int right = 1;

    // to hold left and right child nodes.
    // made public to code easily.
    // can support n-ary trees (for n > 2) using this array approach rather than using individual variables to hold child nodes.
    public Node[] children = new Node[2];

    private int value;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
