import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 21, 2010
 * Time: 4:15:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExampleUI {

    public static void main(String[] args) {
        // example, and very ugly ui using Epzilla layout.

        JFrame frame = new JFrame("frame");
        frame.setLayout(new EzLayout("10, 20, 10, 30%,10, 70%",
                new String[]{"100%",             // entire first row is occupied by single cell. it's 10 points high.
                        "10, 60f, 10",           // 'f' in second value indicates that cell will be filled - that's the size of  first component we add to the frame.
                        "100%",                      /// similar to first row - going to leave (no 'f' means, it's not filled.
                        "30%f, 12, 70%f, 10",     // first and third cells will be filled. 70% - means 70% of the remaining width (70% of [total - (12  + 10)] )
                        "40f, 100%",              // first cell will be filled. its 40 points wide. and 10 points high.
                        " 100%"                   // last row. again one cell. we're going to leave this.
                }));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b = new JButton("btn 1");
        frame.add(b);

        JLabel l = new JLabel("label1");
        frame.add(l);

        JTextField f = new JTextField("text");
        frame.add(f);

        l = new JLabel("lbl2");
        frame.add(l);

        frame.setSize(250, 250);
        frame.setVisible(true);
    }
}
